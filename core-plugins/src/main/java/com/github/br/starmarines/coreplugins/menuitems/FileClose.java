package com.github.br.starmarines.coreplugins.menuitems;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import javafx.scene.control.MenuItem;

import com.github.br.starmarines.ui.api.IMenuItem;

@Provides
@Instantiate
@Component
public class FileClose implements IMenuItem {

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public MenuItem getNode() {		
		return new MenuItem("Close");
	}

	@Override
	public String getMenuTitle() {
		return "File";
	}

}
