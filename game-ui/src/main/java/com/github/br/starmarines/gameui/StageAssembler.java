package com.github.br.starmarines.gameui;

import com.github.br.starmarines.ui.api.StageContainer;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import com.github.br.starmarines.gameui.impl.CenterComponent;
import com.github.br.starmarines.gameui.impl.TopComponent;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@Instantiate
@Component(publicFactory = false)
public class StageAssembler {

	@Requires(policy = BindingPolicy.STATIC, proxy = false)
	private StageContainer stageContainer;

	@Requires(policy = BindingPolicy.STATIC, proxy = false)
	private TopComponent topComponent;

	@Requires(policy = BindingPolicy.STATIC, proxy = false)
	private CenterComponent centerComponent;

	@Validate
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
