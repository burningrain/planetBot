package com.github.br.starmarines.coreplugins.internal.newgame;

import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class ColorTableCell<T> extends TableCell<T, Color>  {

    private final ColorPicker colorPicker;

    public ColorTableCell(TableColumn<T, Color> column) {
        this.colorPicker = new ColorPicker();
        this.colorPicker.editableProperty().bind(column.editableProperty());
        this.colorPicker.disableProperty().bind(column.editableProperty().not());
        this.colorPicker.setOnShowing(event -> {
            final TableView<T> tableView = getTableView();
            tableView.getSelectionModel().select(getTableRow().getIndex());
            tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);
        });
        this.colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(isEditing()) {
                commitEdit(newValue);
            }
        });
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.colorPicker.minWidthProperty().bind(column.minWidthProperty());
        this.colorPicker.prefWidthProperty().bind(column.prefWidthProperty());
        this.colorPicker.maxWidthProperty().bind(column.maxWidthProperty());
    }

    @Override
    protected void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        if(empty) {
            setGraphic(null);
        } else {
            this.colorPicker.setValue(item);
            this.setGraphic(this.colorPicker);
        }
    }

}
