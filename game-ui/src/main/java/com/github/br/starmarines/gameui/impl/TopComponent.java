package com.github.br.starmarines.gameui.impl;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import javafx.scene.layout.VBox;

import com.github.br.starmarines.ui.api.IUiComponent;

@Provides(specifications= TopComponent.class)
@Instantiate
@Component(publicFactory=false)
public class TopComponent implements IUiComponent<VBox> {
	
	@Requires
	private MenuBarComponent menuBarComponent;
	
	@Requires
	private ToolbarComponent toolbarComponent;

	@Override
	public VBox getNode() {	
		VBox vBox = new VBox();
		vBox.getChildren().add(menuBarComponent.getNode());
		vBox.getChildren().add(toolbarComponent.getNode());
		return vBox;
	}

}
