package com.github.br.starmarines.gameui.impl;

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
import com.github.br.starmarines.ui.api.IMenu;

@Provides(specifications = MenuController.class)
@Instantiate
@Component(publicFactory = false)
public class MenuController extends AbstractOrderController<MenuBar, Menu, IMenu> {

	private MenuBar menubar = new MenuBar();
	
	@Validate
	public void validate(){
		init(menubar, menubar.getMenus());
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public MenuBar getNode() {
		return menubar;
	}

	@Bind(aggregate = true, optional = true, policy = BindingPolicy.DYNAMIC)
	public void set(IMenu uiComponentImpl) {
		bind(uiComponentImpl);
	}

	@Unbind
	public void unset(IMenu uiComponentImpl) {
		unbind(uiComponentImpl);
	}

	@Override
	protected void putUID(Menu element, String uid) {
		element.getProperties().put("menuUID", uid);
	}

	@Override
	protected String getUID(Menu e) {		
		return (String) e.getProperties().get("menuUID");
	}

	

	

}
