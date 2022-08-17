package com.github.br.starmarines.gameui.impl;

import javafx.scene.layout.VBox;

import com.github.br.starmarines.ui.api.IUiComponent;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = TopComponent.class)
public class TopComponent implements IUiComponent<VBox> {
	
	@Reference
	private MenuBarComponent menuBarComponent;

	@Reference
	private ToolbarComponent toolbarComponent;

	@Override
	public VBox getNode() {	
		VBox vBox = new VBox();
		vBox.getChildren().add(menuBarComponent.getNode());
		vBox.getChildren().add(toolbarComponent.getNode());
		return vBox;
	}

}
