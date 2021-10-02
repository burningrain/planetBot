package com.github.br.starmarines.gameui.impl;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import com.github.br.starmarines.gameui.AbstractOrderComponent;
import com.github.br.starmarines.ui.api.IMenuItem;
import javafx.scene.control.ToolBar;
import org.apache.felix.ipojo.annotations.Validate;


public class MenuItemComponent extends AbstractOrderComponent<Menu, MenuItem, IMenuItem> {

	@Override
	protected String getConstUID(IMenuItem uiComponentImpl) {
		return uiComponentImpl.getNode().getText();
	}

}
