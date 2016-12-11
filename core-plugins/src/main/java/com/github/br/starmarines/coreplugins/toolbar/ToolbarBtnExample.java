package com.github.br.starmarines.coreplugins.toolbar;

import javafx.scene.Node;
import javafx.scene.control.Button;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.github.br.starmarines.ui.api.IToolBar;

@Provides
@Instantiate
@Component(publicFactory=false)
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
