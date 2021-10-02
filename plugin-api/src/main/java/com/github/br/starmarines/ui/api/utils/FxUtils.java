package com.github.br.starmarines.ui.api.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 03.02.2018.
 */
public final class FxUtils {

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void runOnBackgroundThread(Task task) {
        executorService.submit(task);
    }

    public void runOnBackgroundThread(Runnable runnable) {
        VoidTask voidTask = new VoidTask() {
            @Override
            protected void callWithoutResult() {
                runnable.run();
            }
        };
        voidTask.setOnFailed(event -> {
            throw new RuntimeException(voidTask.getException());
        });
        runOnBackgroundThread(voidTask);
    }

    public void runOnFxThread(Task task) {
        Platform.runLater(task);
    }

    public static <T> T loadFxml(ClassLoader classLoader, Object controller, String resourcePath) {
        FXMLLoader customFxmlLoader = new FXMLLoader();
        customFxmlLoader.setController(controller);
        URL location = classLoader.getResource(resourcePath);
        customFxmlLoader.setLocation(location);

        try {
            return customFxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
