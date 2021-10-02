package com.github.br.starmarines.coreplugins.internal.newgame;

import javafx.scene.control.*;

import java.util.Collection;

public class ComboBoxTableCell<T, E> extends TableCell<T, E> {

    private final ComboBox<E> comboBox;

    public ComboBoxTableCell(Collection<E> items, TableColumn<T, E> column) {
        this.comboBox = new ComboBox<>();
        this.comboBox.editableProperty().bind(column.editableProperty());
        this.comboBox.disableProperty().bind(column.editableProperty().not());
        this.comboBox.setOnShowing(event -> {
            final TableView<T> tableView = getTableView();
            tableView.getSelectionModel().select(getTableRow().getIndex());
            tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);
        });
        this.comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(isEditing()) {
                commitEdit(newValue);
            }
        });
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.comboBox.minWidthProperty().bind(column.minWidthProperty());
        this.comboBox.prefWidthProperty().bind(column.prefWidthProperty());
        this.comboBox.maxWidthProperty().bind(column.maxWidthProperty());

        this.comboBox.getItems().addAll(items);
    }

    @Override
    protected void updateItem(E item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        if(empty) {
            setGraphic(null);
        } else {
            this.comboBox.getSelectionModel().select(item);
            this.setGraphic(this.comboBox);
        }
    }

}
