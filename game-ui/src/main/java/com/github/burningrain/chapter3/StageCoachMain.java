package com.github.burningrain.chapter3;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Validate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
@Instantiate
public class StageCoachMain extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		final StageStyle stageStyle = configStageStyle();
		FXMLLoader fxmlLoader = new FXMLLoader(
				StageCoachMain.class
						.getResource("/com/github/burningrain/chapter3/StageCoach.fxml"));
		Group rootGroup = fxmlLoader.load();
		final StageCoachController controller = fxmlLoader.getController();
		controller.setStage(stage);
		controller.setupBinding(stageStyle);
		Scene scene = new Scene(rootGroup, 250, 350);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.setOnCloseRequest(we -> System.out.println("Stage is closing"));
		stage.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4);
	}

	@Validate
	public void main() {
		Executors
		.defaultThreadFactory()
		.newThread(
				() -> {
					Thread.currentThread().setContextClassLoader(
							this.getClass().getClassLoader());
					launch();
				}).start();
	}

	private StageStyle configStageStyle() {
		StageStyle stageStyle = StageStyle.DECORATED;
		List<String> unnamedParams = getParameters().getUnnamed();
		if (unnamedParams.size() > 0) {
			String stageStyleParam = unnamedParams.get(0);
			if (stageStyleParam.equalsIgnoreCase("transparent")) {
				stageStyle = StageStyle.TRANSPARENT;
			} else if (stageStyleParam.equalsIgnoreCase("undecorated")) {
				stageStyle = StageStyle.UNDECORATED;
			} else if (stageStyleParam.equalsIgnoreCase("utility")) {
				stageStyle = StageStyle.UTILITY;
			}
		}
		return stageStyle;
	}

}
