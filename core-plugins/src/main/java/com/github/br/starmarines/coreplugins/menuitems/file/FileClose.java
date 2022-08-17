package com.github.br.starmarines.coreplugins.menuitems.file;

import javafx.scene.control.MenuItem;

import com.github.br.starmarines.ui.api.IMenuItem;
import org.osgi.service.component.annotations.Component;


@Component(service = IMenuItem.class)
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
