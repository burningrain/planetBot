package com.github.br.starmarines.coreplugins.menu;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import com.github.br.starmarines.ui.api.IMenu;

@Provides
@Instantiate
@Component(publicFactory=false)
public class About implements IMenu {

	@Override
	public Menu getNode() {		
		Menu menu = new Menu("Help");		
		MenuItem newMenuItem = new MenuItem("About");
	    menu.getItems().addAll(newMenuItem);
	    return menu;
	}

	@Override
	public int getOrder() {
		return 10000;
	}

}
