package com.github.br.starmarines.gameui.impl;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import com.github.br.starmarines.gameui.AbstractOrderController;
import com.github.br.starmarines.ui.api.IMenuItem;



public class MenuItemController extends AbstractOrderController<Menu, MenuItem, IMenuItem> {
	

	@Override
	protected String generateUID(IMenuItem uiComponentImpl) {		
		return uiComponentImpl.getNode().getText();
	}

}
