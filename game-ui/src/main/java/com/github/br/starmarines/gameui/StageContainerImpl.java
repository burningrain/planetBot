package com.github.br.starmarines.gameui;

import com.github.br.starmarines.ui.api.StageContainer;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * контейнер для хранения основного стейджа
 */
public class StageContainerImpl implements StageContainer {

    private final Stage primaryStage;

    public StageContainerImpl(Stage stage) {
        primaryStage = stage;
    }

    @Override
    public Stage getStage() {
        return primaryStage;
    }

    @Override
    public void showOpenDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer) {
        FileChooser fileChooser = createFileChooser(title, desc, exts);
        Platform.runLater(() -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            //todo в другом потоке.
            if (file != null) {
                consumer.accept(file);
            }
        });
    }

    @Override
    public void showSaveDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer) {
        FileChooser fileChooser = createFileChooser(title, desc, exts);
        Platform.runLater(() -> {
            File file = fileChooser.showSaveDialog(primaryStage);
            //todo в другом потоке.
            if (file != null) {
                consumer.accept(file);
            }
        });
    }

    @Override
    public void showWindow(String title, Modality modality, Parent node) {
        Stage modalWindow = new Stage();
        modalWindow.setResizable(true);
        modalWindow.setTitle(title);
        modalWindow.setScene(new Scene(node));
        modalWindow.initModality(modality);
        modalWindow.initOwner(primaryStage);
        Platform.runLater(modalWindow::show);
    }

    private FileChooser createFileChooser(String title, String desc, List<String> exts) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(desc, exts);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        return fileChooser;
    }


}
