package com.github.br.starmarines.coreplugins.pane;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.github.br.starmarines.ui.api.IPane;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

@Provides
@Instantiate
@Component(publicFactory = false)
public class YFilesPane implements IPane{

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node getNode() {
		StackPane pane = new StackPane();
		pane.getChildren().add(new Button("кнопка FX"));
		return pane;
	}

}
