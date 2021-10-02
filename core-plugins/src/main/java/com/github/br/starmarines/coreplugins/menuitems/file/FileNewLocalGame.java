package com.github.br.starmarines.coreplugins.menuitems.file;

import com.github.br.starmarines.coreplugins.internal.newgame.CreateLocalGameDialogController;
import com.github.br.starmarines.map.service.api.MapService;
import com.github.br.starmarines.service.strategy.IStrategyService;
import com.github.br.starmarines.ui.api.IMenuItem;
import com.github.br.starmarines.ui.api.StageContainer;
import com.github.br.starmarines.ui.api.utils.FxUtils;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import org.apache.felix.ipojo.annotations.*;

@Provides
@Instantiate
@Component
public class FileNewLocalGame implements IMenuItem {

    private static final String NEW_LOCAL_GAME = "new local game";

    @Requires(policy = BindingPolicy.STATIC, proxy = false)
    private StageContainer stageContainer;

    @Requires(policy = BindingPolicy.STATIC, proxy = false)
    private MapService mapService;

    @Requires(policy = BindingPolicy.STATIC, proxy = false)
    private IStrategyService strategyService;

    @Override
    public int getOrder() {
        return FileOrderEnum.NEW_LOCAL_GAME.getOrder();
    }

    @Override
    public String getMenuTitle() {
        return "File";
    }

    @Override
    public MenuItem getNode() {
        MenuItem newLocalGameMenuItem = new MenuItem(NEW_LOCAL_GAME);
        newLocalGameMenuItem.setOnAction(event -> {
            showCreateLocalGameDialog();
        });

        return newLocalGameMenuItem;
    }

    private void showCreateLocalGameDialog() {
        CreateLocalGameDialogController controller = new CreateLocalGameDialogController();
        Parent parent = FxUtils.loadFxml(this.getClass().getClassLoader(), controller, "fxml/CreateLocalGameDialog.fxml");
        controller.init(mapService, strategyService,
                startGameDto -> {
                    System.out.println(startGameDto);
                    //TODO
                });
        stageContainer.showWindow("new game", Modality.WINDOW_MODAL, parent);
    }

}
