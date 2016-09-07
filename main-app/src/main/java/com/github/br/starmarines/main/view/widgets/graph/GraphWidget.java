package com.github.br.starmarines.main.view.widgets.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.collections15.Transformer;

import com.br.starwors.lx.galaxy.Action;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.model.services.inner.beans.Response;
import com.github.br.starmarines.main.view.View;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.main.view.wm.impl.GraphWM;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class GraphWidget extends JPanel implements
		IWidget<GraphWM, IController> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GraphWM vm;

	private Transformer<Planet, Paint> vertexPaint = new Transformer<Planet, Paint>() {

		@Override
		public Paint transform(Planet planet) {
			Color color = vm.getPlayersColors().get(planet.getOwner());
			return color == null ? Color.DARK_GRAY : color;
		}
	};
	
	private Transformer<Planet, Paint> vertexHolePaint = new Transformer<Planet, Paint>() {
		@Override
		public Paint transform(Planet planet) {			
			return Color.DARK_GRAY;
		}
	};

	private Transformer<Planet, Shape> vertexSize = new Transformer<Planet, Shape>() {

		@Override
		public Shape transform(Planet planet) {			
			int i = 0;
			switch (planet.getType()) {
			case TYPE_A:
				i = 1;
				break;
			case TYPE_B:
				i = 2;
				break;
			case TYPE_C:
				i = 3;
				break;
			case TYPE_D:
				i = 4;
				break;

			}
			Ellipse2D circle = new Ellipse2D.Double(-5, -5, 10, 10);
			Shape shape = AffineTransform.getScaleInstance(i, i).createTransformedShape(circle);			
			return shape;
		}
	};
	
	private Transformer<Planet, Shape> vertexHole = new Transformer<Planet, Shape>() {

		@Override
		public Shape transform(Planet planet) {
			Shape shape = vertexSize.transform(planet);
			
			double buseUnitsPercent = (double) planet.getUnits()
					/ planet.getType().getLimit();
			if (buseUnitsPercent > 1) {
				buseUnitsPercent = 1;
			}
			double freeUnitsPercent = 1.0 - buseUnitsPercent;

			shape = AffineTransform.getScaleInstance(freeUnitsPercent,
					freeUnitsPercent).createTransformedShape(shape);						
			return shape;
		}
	};
	
	private class UglyHuckRenderer extends BasicRenderer<Planet, String>{
		
		/**
		 * необходимо вырезать дыру в круге.
		 * после добавления {@link Area#subtract(Area)} в трансформер 
		 * переставали рендериться стрелки. Ничего умнее этого хака не придумал.
		 */
		public void renderVertex(RenderContext<Planet, String> rc, Layout<Planet, String> layout, Planet v) {			
			super.renderVertex(rc, layout, v);	        
	        rc.setVertexShapeTransformer(vertexHole);
	        rc.setVertexFillPaintTransformer(vertexHolePaint);
	        super.renderVertex(rc, layout, v);  	        
	        rc.setVertexShapeTransformer(vertexSize);
			rc.setVertexFillPaintTransformer(vertexPaint);	                
	    }
		
	}
	

	private Transformer<String, Paint> edgePaint = new Transformer<String, Paint>() {

		@Override
		public Paint transform(String s) {
			Collection<Planet> planets = graph.getEndpoints(s);
			String owner = null;
			boolean isOwnerPlanets = true;
			for (Planet planet : planets) {
				if (owner == null) {
					owner = planet.getOwner();
				} else {
					isOwnerPlanets = isOwnerPlanets
							&& owner.equals(planet.getOwner());
				}
			}

			if (isOwnerPlanets && !owner.isEmpty()) {
				return vm.getPlayersColors().get(owner);
			}
			return Color.GRAY;
		}
	};

	private Transformer<String, String> edgeLabel = new Transformer<String, String>() {

		@Override
		public String transform(String edge) {
			// TODO кривота. сложность алгоритма слишком высока N^2
			Collection<Action> currentActions = vm.getCurrentActions();
			if (currentActions != null && !currentActions.isEmpty()) {
				Pair<Planet> twoPlanets = graph.getEndpoints(edge);
				for (Action action : currentActions) {
					if (action.getFrom().equals(twoPlanets.getFirst().getId())
							&& action.getTo().equals(
									twoPlanets.getSecond().getId())) {
						return String.valueOf(action.getAmount());
					}
				}
			}

			return new String("");
		}
	};

	private Graph<Planet, String> graph;
	private Map<String, Planet> planetsInGraph;
	private VisualizationViewer<Planet, String> vv;

	// private Set<String> edges = new HashSet<String>();

	Transformer<Planet, Point2D> locationTransformer = null;

	private void repaintBotSteps(Collection<Action> actions, boolean directed) {
		for (Action action : actions) {
			Planet from = planetsInGraph.get(action.getFrom());
			Planet to = planetsInGraph.get(action.getTo());
			Collection<String> edgeSet = graph.findEdgeSet(from, to);
			Iterator<String> iterator = edgeSet.iterator();
			while (iterator.hasNext()) {
				graph.removeEdge(iterator.next());
			}
			if (directed) {
				graph.addEdge(from.getId() + "-" + to.getId(), from, to,
						EdgeType.DIRECTED);
			} else {
				graph.addEdge(from.getId() + "-" + to.getId(), from, to,
						EdgeType.UNDIRECTED);
			}
		}
	}

	private void repaintGraph(Collection<Planet> planets) {
		/**
		 * пробегаемся по всем планетам, обноляем планеты, которые уже в графе
		 * так как Equals не сработает и в памяти объекты тоже разные
		 */
		for (Planet planet : planets) {
			Planet graphPlanet = planetsInGraph.get(planet.getId());
			graphPlanet.setOwner(planet.getOwner());
			graphPlanet.setUnits(planet.getUnits());
		}

		vv.repaint();
	}

	private void paintGraph(Collection<Planet> planets) {
		planetsInGraph = new HashMap<String, Planet>();
		graph = new SparseMultigraph<Planet, String>();

		for (Planet planet : planets) {
			graph.addVertex(planet);
			planetsInGraph.put(planet.getId(), planet);
		}

		paintEdges();
		locationTransformer = new LocationTransformer(
				Collections.unmodifiableCollection(planets), vm.getGameType());
		final Layout<Planet, String> layout = new StaticLayout<Planet, String>(graph,
				locationTransformer);
		layout.setSize(new Dimension(View.START_WIDTH, View.START_HEIGHT));

		vv = new VisualizationViewer<Planet, String>(layout);
		vv.setPreferredSize(new Dimension(View.START_WIDTH, View.START_HEIGHT));
		vv.setRenderer(new UglyHuckRenderer());
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);
		// vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
		vv.getRenderContext().setArrowDrawPaintTransformer(edgePaint);
		vv.getRenderContext().setEdgeLabelTransformer(edgeLabel);

		vv.getRenderer().getVertexLabelRenderer()
				.setPosition(Renderer.VertexLabel.Position.CNTR);

		vv.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<Planet, String>());			

		this.add(vv);
		updateUI();
		// this.validate();
		this.repaint();
	}

	private void paintEdges() {
		for (Planet planet : graph.getVertices()) {
			for (Planet neighbour : planet.getNeighbours()) {
				// if(!edges.contains(planet.getId() + "-" +
				// neighbour.getId())){
				/**
				 * сделано во избежание рисования дублирующихся связей
				 */
				graph.addEdge(planet.getId() + "-" + neighbour.getId(), planet,
						neighbour);
				// graph.addEdge(neighbour.getId() + "-" + planet.getId(),
				// planet, neighbour);
				// edges.add(planet.getId() + "-" + neighbour.getId());
				// edges.add(neighbour.getId() + "-" + planet.getId());
				// }
			}
		}
	}

	@Override
	public void update() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Response response = vm.getCurrentStep();
				if (response == null || response.getPlanets() == null) {
					return;
				}

				boolean isGraphNotInit = graph == null
						|| planetsInGraph == null || planetsInGraph.isEmpty();
				if (isGraphNotInit) {
					paintGraph(response.getPlanets());
				} else {
					repaintGraph(response.getPlanets());
					// repaint actions
					Collection<Action> actions = vm.getCurrentActions();
					Collection<Action> lastActions = vm.getLastActions();

					repaintBotSteps(lastActions, false);
					repaintBotSteps(actions, true);
				}
			}
		});

	}

	@Override
	public void setModel(GraphWM vo) {
		this.vm = vo;
	}

	@Override
	public GraphWM getModel() {
		return vm;
	}

	@Override
	public void setController(IController controller) {
		// TODO Auto-generated method stub

	}

}
