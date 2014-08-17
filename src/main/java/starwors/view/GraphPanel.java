package starwors.view;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;
import starwors.model.lx.bot.BotModel;
import starwors.model.lx.bot.IBotModelListener;
import starwors.model.lx.bot.Response;
import starwors.model.lx.galaxy.Planet;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.*;


public class GraphPanel extends JPanel implements IBotModelListener {

    private final BotModel model;
    private SwingView view;


    private Transformer<Planet, Paint> vertexPaint = new Transformer<Planet, Paint>() {

        @Override
        public Paint transform(Planet planet) {
            Color color = view.playersColors.get(planet.getOwner());
            return color == null? Color.darkGray : color;
        }
    };

    private Transformer<Planet, Shape> vertexSize = new Transformer<Planet, Shape>() {

        @Override
        public Shape transform(Planet planet) {
            Ellipse2D circle = new Ellipse2D.Double(-5, -5, 10, 10);
            // in this case, the vertex is twice as large
            int i = 0;
            switch(planet.getType()){
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
            return AffineTransform.getScaleInstance(i, i).createTransformedShape(circle);
        }
    };

    private Transformer<String, Paint> edgePaint = new Transformer<String, Paint>() {

        @Override
        public Paint transform(String s) {
            return Color.GRAY;
        }
    };

    private Graph<Planet, String> graph;
    private Map<String, Planet> planetsInGraph;
    private VisualizationViewer<Planet, String> vv;

    private Set<String> edges = new HashSet<String>();

    Transformer<Planet, Point2D> locationTransformer = new LocationTransformer();


    public GraphPanel(BotModel model, SwingView view) {
        this.model = model;
        this.view = view;
        this.model.addListener(this);
    }


    @Override
    public void update(final BotModel model) {

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                Response response = model.getCurrentStep();
                if(response == null || response.getPlanets() == null){
                    return;
                }

                if(planetsInGraph == null || planetsInGraph.isEmpty() && graph != null){
                    paintGraph(response.getPlanets());
                } else{
                    repaintGraph(response.getPlanets());
                }
            }
        });
    }


    private void repaintGraph(Collection<Planet> planets){
        /**
         * пробегаемся по всем планетам, обноляем планеты, которые уже в графе
         * так каак Equals не сработает и в памяти объекты тоже разные
         */
        for(Planet planet : planets){
            Planet graphPlanet = planetsInGraph.get(planet.getId());
            graphPlanet.setOwner(planet.getOwner());
            graphPlanet.setUnits(planet.getUnits());
        }


        vv.repaint();
    }


    private void paintGraph(Collection<Planet> planets){
        planetsInGraph = new HashMap<String, Planet>();
        graph = new SparseMultigraph<Planet, String>();

        for(Planet planet : planets){
            graph.addVertex(planet);
            planetsInGraph.put(planet.getId(), planet);
        }

        for(Planet planet : graph.getVertices()){
            for(Planet neighbour : planet.getNeighbours()){
                if(!edges.contains(planet.getId() + "-" + neighbour.getId())){
                    /**
                     * сделано во избежание рисования дублирующихся связей
                     */
                    graph.addEdge(planet.getId() + "-" + neighbour.getId(), planet, neighbour);
                    edges.add(planet.getId() + "-" + neighbour.getId());
                    edges.add(neighbour.getId() + "-" + planet.getId());
                }
            }
        }

        Layout<Planet, String> layout = new StaticLayout<Planet, String>(graph, locationTransformer);
        layout.setSize(new Dimension(SwingView.START_WIDTH, SwingView.START_HEIGHT)); // sets the initial size of the

        vv = new VisualizationViewer<Planet, String>(layout);
        vv.setPreferredSize(new Dimension(SwingView.START_WIDTH, SwingView.START_HEIGHT)); // Sets the viewing area

        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        // vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        this.add(vv);
        updateUI();
        //this.validate();
        this.repaint();
    }


}
