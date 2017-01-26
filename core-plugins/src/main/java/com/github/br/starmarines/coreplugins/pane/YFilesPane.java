package com.github.br.starmarines.coreplugins.pane;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.github.br.starmarines.ui.api.IPane;
import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.geometry.RectD;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.graph.styles.ShapeNodeStyle;
import com.yworks.yfiles.graph.styles.ShinyPlateNodeStyleRenderer;
import com.yworks.yfiles.view.input.GraphEditorInputMode;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


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
		com.yworks.yfiles.view.GraphControl graphControl = new GraphControl();
		ShapeNodeStyle nodeStyle = new ShapeNodeStyle();
		// Sets a default style.
		graphControl.getGraph().getNodeDefaults();
		// Gets the IGraph
		IGraph graph = graphControl.getGraph();
		// and creates some nodes.
		graph.createNode(new RectD(10, 10, 100, 100), nodeStyle);
		graph.createNode(new RectD(150, 150, 100, 100), nodeStyle);
		graph.createNode(new RectD(250, 250, 100, 100), nodeStyle);
		graphControl.setInputMode(new GraphEditorInputMode());
		pane.getChildren().add(graphControl);
		return pane;
	}

}
