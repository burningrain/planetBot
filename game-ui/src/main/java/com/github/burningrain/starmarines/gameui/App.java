package com.github.burningrain.starmarines.gameui;

import java.util.concurrent.Executors;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.LogService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

@Component
@Instantiate
public class App extends Application {
	
	@Validate
	public void startBundle() {
		Executors
				.defaultThreadFactory()
				.newThread(
						() -> {
							Thread.currentThread().setContextClassLoader(
									this.getClass().getClassLoader());
							launch();
						}).start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

//		BundleContext bc = FrameworkUtil.getBundle(this.getClass())
//				.getBundleContext();
//		DependencyManager dm = new DependencyManager(bc);
//
//		dm.add(dm.createComponent()
//				.setInterface(StageService.class.getName(), null)
//				.setImplementation(new StageServiceImpl(primaryStage)));
		
		primaryStage.show();
	}

	@Invalidate
	public void stopBundle() {
		Platform.exit();
	}
}
