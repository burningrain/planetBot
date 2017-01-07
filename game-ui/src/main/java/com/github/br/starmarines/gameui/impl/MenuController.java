package com.github.br.starmarines.gameui.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

import com.github.br.starmarines.gameui.AbstractOrderController;
import com.github.br.starmarines.gameui.PairFxService;
import com.github.br.starmarines.ui.api.IMenu;
import com.github.br.starmarines.ui.api.IMenuItem;
import com.github.br.starmarines.ui.api.IUiOrderComponent;

@Provides(specifications = MenuController.class)
@Instantiate
@Component(publicFactory = false)
public class MenuController extends
		AbstractOrderController<MenuBar, Menu, IMenu> implements
		IUiOrderComponent<MenuBar> {

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
		PairFxService<MenuBar, Menu, IMenu> pairFxService = bind(uiComponentImpl);
		bindItemController(pairFxService);		
	}

	@Unbind
	public void unset(IMenu uiComponentImpl) {
		PairFxService<MenuBar, Menu, IMenu> pairFxService = unbind(uiComponentImpl);
		unbindItemController(pairFxService);
	}

	@Override
	protected String generateUID(IMenu uiComponentImpl) {
		return uiComponentImpl.getNode().getText();
	}

	// ////////////////////////
	// управление menuItem-ами
	// ////////////////////////
	private ConcurrentHashMap<String, MenuItemController> itemControllers = new ConcurrentHashMap<>();

	private void bindItemController(PairFxService<MenuBar, Menu, IMenu> pairFxService) {
		MenuItemController itemController = new MenuItemController();
		Menu menu = pairFxService.getFxChild();
		itemController.init(menu, menu.getItems());
		String menuTitle = menu.getText();
		itemControllers.put(menuTitle, itemController);
	}

	private void unbindItemController(PairFxService<MenuBar, Menu, IMenu> pairFxService) {
		String menuTitle = pairFxService.getFxChild().getText();
		itemControllers.remove(menuTitle);
	}

	@Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
	public void setMenuItem(IMenuItem menuItem) {
		MenuItemController itemController = itemControllers.get(menuItem
				.getMenuTitle());
		if(itemController == null){
			new Thread(() -> {
				while(itemControllers.get(menuItem
						.getMenuTitle()) == null){}
				MenuItemController ic = itemControllers.get(menuItem.getMenuTitle());
				ic.bind(menuItem);				
			}).start();			
		} else {
			itemController.bind(menuItem);	
		}
		
	}

	@Unbind
	public void unsetMenuItem(IMenuItem menuItem) {
		MenuItemController itemController = itemControllers.get(menuItem
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
