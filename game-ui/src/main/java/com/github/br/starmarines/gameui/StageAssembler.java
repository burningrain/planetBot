package com.github.br.starmarines.gameui;

import com.github.br.starmarines.ui.api.StageContainer;

import com.github.br.starmarines.gameui.impl.CenterComponent;
import com.github.br.starmarines.gameui.impl.TopComponent;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.osgi.service.component.annotations.*;

@Component
public class StageAssembler {

	@Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.MANDATORY)
	private StageContainer stageContainer;

	@Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.MANDATORY)
	private TopComponent topComponent;

	@Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.MANDATORY)
	private CenterComponent centerComponent;

	@Activate
	public void init() {
		showStage();
	}

	private void showStage() {
		Platform.runLater(() -> {
			Scene scene = new Scene(createMainPane(), 800, 600); //TODO вынести в настройки
			Stage stage = stageContainer.getStage();
			stage.setTitle("Planet bot");
			stage.setScene(scene);
			stage.show();
		});
	}

	private Parent createMainPane() {
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topComponent.getNode());
		borderPane.setCenter(centerComponent.getNode());
		return borderPane;
	}

}
