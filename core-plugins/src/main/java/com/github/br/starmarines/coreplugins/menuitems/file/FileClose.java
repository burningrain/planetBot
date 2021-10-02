package com.github.br.starmarines.coreplugins.menuitems.file;

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
		return FileOrderEnum.CLOSE.getOrder();
	}

	@Override
	public MenuItem getNode() {
		MenuItem close = new MenuItem("Close");
		close.setOnAction(event -> {
			System.exit(0);
		});

		return close;
	}

	@Override
	public String getMenuTitle() {
		return "File";
	}

}
