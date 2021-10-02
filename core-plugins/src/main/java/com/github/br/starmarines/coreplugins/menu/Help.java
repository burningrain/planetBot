package com.github.br.starmarines.coreplugins.menu;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import javafx.scene.control.Menu;
import com.github.br.starmarines.ui.api.IMenu;

@Provides
@Instantiate
@Component
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
