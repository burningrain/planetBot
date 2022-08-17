package com.github.br.starmarines.coreplugins.menu;

import javafx.scene.control.Menu;
import com.github.br.starmarines.ui.api.IMenu;
import org.osgi.service.component.annotations.Component;

@Component(service = IMenu.class)
public class Help implements IMenu {

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Menu getNode() {
	    return new Menu("Help");
	}


}
