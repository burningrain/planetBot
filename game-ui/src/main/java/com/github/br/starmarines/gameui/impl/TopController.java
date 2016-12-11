package com.github.br.starmarines.gameui.impl;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import javafx.scene.layout.VBox;

import com.github.br.starmarines.ui.api.IUiComponent;

@Provides(specifications=TopController.class)
@Instantiate
@Component(publicFactory=false)
public class TopController implements IUiComponent<VBox> {
	
	@Requires
	private MenuController menuController;
	
	@Requires
	private ToolbarController toolbarController;

	@Override
	public VBox getNode() {	
		VBox vBox = new VBox();
		vBox.getChildren().add(menuController.getNode());
		vBox.getChildren().add(toolbarController.getNode());
		return vBox;
	}

}
