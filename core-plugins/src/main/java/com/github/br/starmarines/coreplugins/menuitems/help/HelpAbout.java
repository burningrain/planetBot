package com.github.br.starmarines.coreplugins.menuitems.help;

import javafx.scene.control.MenuItem;

import com.github.br.starmarines.ui.api.IMenuItem;
import org.osgi.service.component.annotations.Component;


@Component(service = IMenuItem.class)
public class HelpAbout implements IMenuItem {

	@Override
	public int getOrder() {		
		return Integer.MAX_VALUE;
	}

	@Override
	public MenuItem getNode() {
		return new MenuItem("About");
	}

	@Override
	public String getMenuTitle() {
		return "Help";
	}

}
