package com.github.br.starmarines.coreplugins.internal.newgame;


import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.coreplugins.internal.dto.PlayerDto;
import com.github.br.starmarines.coreplugins.internal.dto.StartGameDto;
import com.github.br.starmarines.map.service.api.MapService;
import com.github.br.starmarines.map.service.api.MapValidationException;
import com.github.br.starmarines.service.strategy.IStrategyService;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CreateLocalGameDialogController {

    @FXML
    private ListView<String> mapsListView;

    @FXML
    private ImageView mapImageView;

    @FXML
    private ChoiceBox<Integer> playersCountChoiceBox;

    @FXML
    private Button startButton;

    @FXML
    private TableView<PlayerBean> playersTableView;

    @FXML
    private TableColumn<PlayerBean, String> baseTableColumn;

    @FXML
    private TableColumn<PlayerBean, Color> colourTableColumn;

    @FXML
    private TableColumn<PlayerBean, String> playerTableColumn;

    @FXML
    private TableColumn<PlayerBean, String> strategyTableColumn;


    private Consumer<StartGameDto> onStartConsumer;
    private MapService mapService;
    private IStrategyService strategies;

    private String selectedGalaxy;

    public void init(MapService mapService, IStrategyService strategies, Consumer<StartGameDto> onStartConsumer) {
        this.mapService = mapService;
        this.strategies = strategies;
        this.onStartConsumer = onStartConsumer;
        Platform.runLater(() -> {
            startButton.setOnAction(event -> {
                onStartConsumer.accept(new StartGameDto(selectedGalaxy,
                        playersTableView.getItems()
                                .stream()
                                .map(playerBean ->
                                        new PlayerDto(playerBean.getColor().toString(), playerBean.getName(), playerBean.getStrategy()))
                                .collect(Collectors.toList()))
                );
            });

            // настройка чекбоса числа игроков
            playersCountChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue > oldValue) {
                    // добавляем новых игроков
                    for (int i = oldValue; i < newValue; i++) {
                        addPlayerToTable(i);
                    }
                } else if(newValue < oldValue) {
                    // подрезаем список игроков с конца
                    removePlayerFromTable(newValue, oldValue);
                }
            });

            // настройка таблицы
            BaseCellFactory baseCellFactory = new BaseCellFactory();

            baseTableColumn.setCellFactory(baseCellFactory);
            baseTableColumn.setCellValueFactory(new PropertyValueFactory<>("base"));

            colourTableColumn.setCellFactory(param -> new ColorTableCell<>(param));
            colourTableColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

            playerTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            playerTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            strategyTableColumn.setCellFactory(param -> new ComboBoxTableCell<>(strategies.getStrategies(), param));
            strategyTableColumn.setCellValueFactory(new PropertyValueFactory<>("strategy"));

            playersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            playersTableView.setEditable(true);

            // список карт
            mapsListView.getItems().addAll(mapService.getAllTitles());
            MultipleSelectionModel<String> selectionModel = mapsListView.getSelectionModel();
            selectionModel.selectionModeProperty().set(SelectionMode.SINGLE);
            selectionModel.getSelectedItems().addListener((ListChangeListener<String>) c -> {
                if (!c.next()) {
                    return;
                }
                String selectedMap = c.getAddedSubList().get(0);
                this.selectedGalaxy = selectedMap;

                Galaxy map;
                try {
                    map = mapService.getMap(selectedGalaxy);
                } catch (MapValidationException e) {
                    throw new RuntimeException(e);
                }

                baseCellFactory.setBases(map.getStartPoints().size());
                updatePlayersCountChoiceBox(map.getMaxPlayersCount() == 0 ? 2 : map.getMaxPlayersCount()); //TODO пофиксать потом
                updatePlayersTable();
            });
            selectionModel.selectFirst();
        });
    }

    private void updatePlayersCountChoiceBox(int playersCount) {
        playersCountChoiceBox.getItems().clear();
        for (int i = 0; i < playersCount; i++) {
            playersCountChoiceBox.getItems().add(i + 1);
        }
    }

    private void updatePlayersTable() {
        playersTableView.getItems().clear();

        SingleSelectionModel<Integer> selectionModel = playersCountChoiceBox.getSelectionModel();
        selectionModel.selectLast();
        int amount = selectionModel.getSelectedItem();
        for (int i = 0; i < amount; i++) {
            addPlayerToTable(i);
        }
    }

    private void addPlayerToTable(int i) {
        String strategyTitle = strategies.getStrategies().stream().findFirst().get();
        final Random random = new Random();
        int number = i + 1; // чтобы не с нуля начиналось
        playersTableView.getItems().add(
                new PlayerBean(
                        number,
                        Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)),
                        number + "-" + strategyTitle,
                        strategyTitle
                )
        );
    }

    private void removePlayerFromTable(int from, int to) {
        playersTableView.getItems().remove(from, to);
    }

    private static class BaseCellFactory<S> implements Callback<TableColumn<S, Integer>, TableCell<S, Integer>> {

        private Collection<Integer> data;

        public void setBases(int maxBaseCount) {
            ArrayList<Integer> bases = new ArrayList<>(maxBaseCount);
            for (int i = 0; i < maxBaseCount; i++) {
                bases.add(i + 1);
            }
            data = bases;
        }

        @Override
        public TableCell<S, Integer> call(TableColumn<S, Integer> param) {
            return new ComboBoxTableCell<>(data, param);
        }

    }

}
