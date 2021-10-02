package com.github.br.starmarines.gameui.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

import com.github.br.starmarines.gameui.AbstractOrderComponent;
import com.github.br.starmarines.gameui.PairFxContainer;
import com.github.br.starmarines.ui.api.IMenu;
import com.github.br.starmarines.ui.api.IMenuItem;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

@Provides(specifications = MenuBarComponent.class)
@Instantiate
@Component(publicFactory = false)
public class MenuBarComponent extends AbstractOrderComponent<MenuBar, Menu, IMenu>
        implements IUiOrderComponent<MenuBar> {

    @Validate
    public void validate() {
        MenuBar menubar = new MenuBar();
        init(menubar, menubar.getMenus());
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
    public void set(IMenu uiComponentImpl) {
        CompletableFuture<PairFxContainer<MenuBar, Menu, IMenu>> pairFxContainer = bind(uiComponentImpl);
        pairFxContainer.thenAccept(menuBarMenuIMenuPairFxContainer -> {
            bindItemController(menuBarMenuIMenuPairFxContainer);
        });
    }

    @Unbind
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

    @Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
    public void setMenuItem(IMenuItem menuItem) {
        MenuItemComponent itemController = itemComponentsMap.get(menuItem.getMenuTitle());
        if (itemController == null) {
            new Thread(() -> {
                while (itemComponentsMap.get(menuItem.getMenuTitle()) == null) {
                }
                MenuItemComponent ic = itemComponentsMap.get(menuItem.getMenuTitle());
                ic.bind(menuItem);
            }).start();
        } else {
            itemController.bind(menuItem);
        }

    }

    @Unbind
    public void unsetMenuItem(IMenuItem menuItem) {
        MenuItemComponent itemController = itemComponentsMap.get(menuItem
                .getMenuTitle());
        if (itemController != null) {
            itemController.unbind(menuItem);
        } else {
            System.out.println(String
                    .format("Отвязать элемент '%s' не удалась, так как элемент '%s' уже отвязан",
                            menuItem.getNode().getText(),
                            menuItem.getMenuTitle()));
        }
    }
    // ////////////////////////
    // управление menuItem-ами
    // ////////////////////////

}
