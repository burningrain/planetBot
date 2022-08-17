package com.github.br.starmarines.gameui;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.github.br.starmarines.ui.api.StageContainer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.osgi.service.component.annotations.*;
import org.osgi.service.log.LogService;

/**
 * стартует javafx
 */
@Component
public class UiLoader extends Application {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    private volatile LogService logService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // регистрируем primaryStage в реестре
        BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        bc.registerService(StageContainer.class, new StageContainerImpl(primaryStage), null);
    }


    @Activate
    public void main() throws IOException, InterruptedException {
        Executors
                .defaultThreadFactory()
                .newThread(
                        () -> {
                            try {
                                Thread.currentThread().setContextClassLoader(
                                        this.getClass().getClassLoader());
                                launch();
                            } catch (Exception e) {
                                e.printStackTrace();
                                logService.log(LogService.LOG_ERROR, e.getMessage(), e);
                            }
                        }).start();
    }

    @Deactivate
    public void stop() {
        AbstractOrderComponent.preDestroy();
        Platform.exit();
    }

}
