package com.github.br.starmarines.coreplugins.pane;

import java.util.Random;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;

import com.github.br.starmarines.coreplugins.event.Request;
import com.github.br.starmarines.coreplugins.event.tools.AbstractRequestHandler;
import com.yworks.yfiles.geometry.RectD;
import com.yworks.yfiles.graph.IGraph;
import com.yworks.yfiles.view.GraphControl;


@Provides(specifications = GalaxyRequestHandler.class)
@Instantiate
@Component(publicFactory = false)
public class GalaxyRequestHandler extends AbstractRequestHandler {

	private volatile GraphControl graphControl;
	
	@Validate
	public void validate()  {
		
	}
	
	@Override
	public void handleRequest(Request request) {
		System.out.println("GALAXY EDIT " + request.getRequest());
		IGraph graph = graphControl.getGraph();
		Random random = new Random();
		
		
		graph.createNode(new RectD(10 + random.nextInt(401), 10 + random.nextInt(401), 20, 20));
	}
	
	public void setGraphControl(GraphControl graphControl) {
		this.graphControl = graphControl;
	}
}
