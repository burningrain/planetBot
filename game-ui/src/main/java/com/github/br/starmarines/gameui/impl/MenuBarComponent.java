package com.github.br.starmarines.gameui.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import com.github.br.starmarines.ui.api.IUiComponent;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

import com.github.br.starmarines.gameui.AbstractOrderComponent;
import com.github.br.starmarines.gameui.PairFxContainer;
import com.github.br.starmarines.ui.api.IMenu;
import com.github.br.starmarines.ui.api.IMenuItem;
import com.github.br.starmarines.ui.api.IUiOrderComponent;
import org.osgi.service.component.annotations.*;

@Component(service = MenuBarComponent.class)
public class MenuBarComponent extends AbstractOrderComponent<MenuBar, Menu, IMenu>
        implements IUiOrderComponent<MenuBar> {

    @Activate
    public void validate() {
        MenuBar menubar = new MenuBar();
        init(menubar, menubar.getMenus());
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Reference(unbind = "unset", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void set(IMenu uiComponentImpl) {
        CompletableFuture<PairFxContainer<MenuBar, Menu, IMenu>> pairFxContainer = bind(uiComponentImpl);
        pairFxContainer.thenAccept(menuBarMenuIMenuPairFxContainer -> {
            bindItemController(menuBarMenuIMenuPairFxContainer);
        });
    }

    public void unset(IMenu uiComponentImpl) {
        PairFxContainer<MenuBar, Menu, IMenu> pairFxContainer = unbind(uiComponentImpl);
        unbindItemController(pairFxContainer);
    }

    @Override
    protected String getConstUID(IMenu uiComponentImpl) {
        return uiComponentImpl.getNode().getText();
    }

    // ////////////////////////
    // управление menuItem-ами
    // ////////////////////////
    private ConcurrentHashMap<String, MenuItemComponent> itemComponentsMap = new ConcurrentHashMap<>();

    private void bindItemController(PairFxContainer<MenuBar, Menu, IMenu> pairFxContainer) {
        MenuItemComponent itemController = new MenuItemComponent();
        Menu menu = pairFxContainer.getFxChild();
        itemController.init(menu, menu.getItems());
        String menuTitle = menu.getText();
        itemComponentsMap.put(menuTitle, itemController);
    }

    private void unbindItemController(PairFxContainer<MenuBar, Menu, IMenu> pairFxContainer) {
        String menuTitle = pairFxContainer.getFxChild().getText();
        itemComponentsMap.remove(menuTitle);
    }

    @Reference(unbind = "unsetMenuItem", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void setMenuItem(IMenuItem menuItem) {
        MenuItemComponent itemController = itemComponentsMap.get(menuItem.getMenuTitle());
        if (itemController == null) {
            new Thread(() -> {
                while (itemComponentsMap.get(menuItem.getMenuTitle()) == null) {
                    // spin lock
                }
                MenuItemComponent ic = itemComponentsMap.get(menuItem.getMenuTitle());
                ic.bind(menuItem);
            }).start();
        } else {
            itemController.bind(menuItem);
        }

    }

    public void unsetMenuItem(IMenuItem menuItem) {
        MenuItemComponent itemController = itemComponentsMap.get(menuItem
                .getMenuTitle());
        if (itemController != null) {
            itemController.unbind(menuItem);
        } else {
            //TODO в логгер же
            System.out.printf("Отвязать элемент '%s' не удалась, так как элемент '%s' уже отвязан%n",
                    menuItem.getNode().getText(),
                    menuItem.getMenuTitle());
        }
    }
    // ////////////////////////
    // управление menuItem-ами
    // ////////////////////////

}
