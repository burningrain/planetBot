package com.github.br.starmarines.coreplugins.pane;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import com.github.br.starmarines.ui.api.IPane;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.input.GraphEditorInputMode;
import com.yworks.yfiles.view.input.MouseHoverInputMode;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

@Provides
@Instantiate
@Component(publicFactory = false)
public class YFilesPane implements IPane {
	
	@Requires
	GalaxyRequestHandler requestHandlerService;

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node getNode() {
		StackPane pane = new StackPane();
		GraphControl graphControl = new GraphControl();
		requestHandlerService.setGraphControl(graphControl);
		graphControl.setInputMode(new MouseHoverInputMode());
		pane.getChildren().add(graphControl);
		return pane;

	}

}
