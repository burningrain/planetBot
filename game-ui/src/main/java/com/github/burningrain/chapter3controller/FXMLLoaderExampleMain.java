package com.github.burningrain.chapter3controller;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.jfoenix.controls.JFXDecorator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Component
@Instantiate
public class FXMLLoaderExampleMain extends Application {

	@Requires(policy = BindingPolicy.STATIC, proxy = false)
	FXMLLoaderExampleController fxmlController;

	@Requires(policy = BindingPolicy.DYNAMIC, optional=true)
	StageService stageService;

	@Override
	public void start(Stage primaryStage) throws Exception {
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		bc.registerService(StageService.class, new StageServiceImpl(primaryStage), null);
	}

	private void showStage() {
		System.out.println("Contr: " + fxmlController);
		Platform.runLater(()->{
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader
					.setLocation(FXMLLoaderExampleMain.class
							.getResource("/com/github/burningrain/chapter3controller/2.fxml"));
			fxmlController.setName("name: FXMLLoaderExampleController");
			System.out.println(fxmlController.getName());
			fxmlLoader.setController(fxmlController);
			Node vBox = null;
			try {
				vBox = fxmlLoader.load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Stage stage = stageService.getStage();
			JFXDecorator decorator = new JFXDecorator(stage, vBox);
			decorator.setCustomMaximize(true);				
			Scene scene = new Scene(decorator, 600, 400);
//			scene.getStylesheets().add(FXMLLoaderExampleMain.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
//			scene.getStylesheets().add(FXMLLoaderExampleMain.class.getResource("/resources/css/jfoenix-design2.css").toExternalForm());
			stage.setTitle("FXMLLoader Example");
			stage.setScene(scene);
			stage.show();
		});		
	}

	@Validate
	public void main() throws IOException, InterruptedException {
		Executors
				.defaultThreadFactory()
				.newThread(
						() -> {
							Thread.currentThread().setContextClassLoader(
									this.getClass().getClassLoader());
							launch();

						}).start();
		Thread.sleep(5000); //TODO вот с этим что-нибудь сделать бы
		showStage();
	}

}
