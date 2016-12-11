package com.github.burningrain.chapter1;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MetronomePathTransitionMain {

	Button startButton;
	Button pauseButton;
	Button resumeButton;
	Button stopButton;
	Ellipse ellipse;
	Path path;

	public void start(Stage stage) {
		ellipse = new Ellipse(100, 50, 4, 8);
		ellipse.setFill(Color.BLUE);
		path = new Path(new MoveTo(100, 50), new ArcTo(350, 350, 0, 300, 50,
				false, true));
		PathTransition anim = new PathTransition(new Duration(1000.0), path,
				ellipse);
		anim.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		anim.setInterpolator(Interpolator.LINEAR);
		anim.setAutoReverse(true);
		anim.setCycleCount(Timeline.INDEFINITE);
		startButton = new Button("start");
		startButton.setOnAction(e -> anim.playFromStart());
		pauseButton = new Button("pause");
		pauseButton.setOnAction(e -> anim.pause());
		resumeButton = new Button("resume");
		resumeButton.setOnAction(e -> anim.play());
		stopButton = new Button("stop");
		stopButton.setOnAction(e -> anim.stop());
		HBox commands = new HBox(10, startButton, pauseButton, resumeButton,
				stopButton);
		commands.setLayoutX(60);
		commands.setLayoutY(420);
		Group group = new Group(ellipse, commands);
		Scene scene = new Scene(group, 400, 500);
		startButton.disableProperty().bind(
				anim.statusProperty().isNotEqualTo(Animation.Status.STOPPED));
		pauseButton.disableProperty().bind(
				anim.statusProperty().isNotEqualTo(Animation.Status.RUNNING));
		resumeButton.disableProperty().bind(
				anim.statusProperty().isNotEqualTo(Animation.Status.PAUSED));
		stopButton.disableProperty().bind(
				anim.statusProperty().isEqualTo(Animation.Status.STOPPED));
		stage.setScene(scene);
		stage.setTitle("Metronome using PathTransition");
		stage.show();
	}

}
