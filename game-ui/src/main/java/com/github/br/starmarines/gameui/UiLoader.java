package com.github.br.starmarines.gameui;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.github.br.starmarines.ui.api.StageContainer;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * стартует javafx
 */
@Component
@Instantiate
public class UiLoader extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // регистрируем primaryStage в реестре
        BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        bc.registerService(StageContainer.class, new StageContainerImpl(primaryStage), null);
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
    }

    @Invalidate
    public void stop() {
        Platform.exit();
    }

}
