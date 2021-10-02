package com.github.br.starmarines.ui.api;

import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public interface StageContainer {

	Stage getStage();

	void showOpenDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer);

	void showSaveDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer);

	void showWindow(String title, Modality modality, Parent node);

}