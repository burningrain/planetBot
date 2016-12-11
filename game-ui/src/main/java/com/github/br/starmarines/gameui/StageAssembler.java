package com.github.br.starmarines.gameui;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import com.github.br.starmarines.gameui.impl.TopController;

@Instantiate
@Component(publicFactory = false)
public class StageAssembler {

	@Requires(policy = BindingPolicy.STATIC, proxy = false)
	private StageService stageService;
	
	@Requires(policy = BindingPolicy.STATIC, proxy = false)
	private TopController topController; 

	@Validate
	public void init() {
		showStage();
	}

	private void showStage() {
		Platform.runLater(() -> {
			Scene scene = new Scene(fillScene(), 800, 600);
			Stage stage = stageService.getStage();
			stage.setTitle("Planet bot");
			stage.setScene(scene);
			stage.show();
		});
	}

	private Parent fillScene() {
		BorderPane borderPane = new BorderPane(); 
		borderPane.setTop(topController.getNode());
		return borderPane;
	}

}
