package com.github.br.starmarines.coreplugins.toolbar;

import javafx.scene.Node;
import javafx.scene.control.Button;

import com.github.br.starmarines.ui.api.IToolBar;
import org.osgi.service.component.annotations.Component;

@Component(service = IToolBar.class)
public class ToolbarBtnExample implements IToolBar {

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Node getNode() {
		Button btn = new Button("кнопка");
		return btn;
	}

}
