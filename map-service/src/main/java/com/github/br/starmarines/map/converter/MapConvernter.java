package com.github.br.starmarines.map.converter;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.service.api.MapValidationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.gef4.dot.internal.DotImport;
import org.eclipse.gef4.graph.Edge;
import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.graph.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Component
public class MapConvernter implements Converter<File, Galaxy> {

    private volatile LogService logService;
    private static final String CODE_PAGE = "utf-8";

    @Reference(unbind = "unSetLogService", cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void unSetLogService(LogService logService) {
        this.logService = null;
    }

    @Override
    public Galaxy convert(File in) throws IOException, MapValidationException {
        if (logService != null) {
            logService.log(LogService.LOG_DEBUG, "Convertation start");
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(Planet.class, new NodeLabelDeserializer())
                .registerTypeAdapter(GameMetaInfo.class, new GameMetaInfoDeserializer()).create();
        String graphFile = getContent(in);
        Graph graph = new DotImport().importDot(graphFile);
        Map<String, Object> atribMap = graph.getAttributes();
        if ((String) atribMap.get("_name") == null) {
            throw new MapValidationException("Errero parse map from file", "Galaxy meta" +
                    " information lost in _name attrib in Graph name");
        }
        GameMetaInfo galaxyInfo = gson.fromJson((String) atribMap.get("_name"), GameMetaInfo.class);
        Galaxy.Builder gBuilder = new Galaxy.Builder(galaxyInfo.getType());
        Galaxy galaxy = null;
        List<Node> nProps = graph.nodesProperty();
        for (Node nodeProp : nProps) {
            Map<String, Object> nodePropAttrMap = nodeProp.getAttributes();
            Planet planet = gson.fromJson((String) nodePropAttrMap.get("label"), Planet.class);
            GameMetaInfo gameInfo = gson.fromJson((String) nodePropAttrMap.get("label"), GameMetaInfo.class);
            gBuilder.addPlanet(planet, gameInfo.getIsStartPoint());
        }
        List<Edge> edges = graph.getEdges();
        for (Edge edge : edges) {
            Node from = edge.getSource();
            Map<String, Object> fromAttr = from.getAttributes();
            Planet planetFrom = gson.fromJson((String) fromAttr.get("label"), Planet.class);
            Node to = edge.getTarget();
            Map<String, Object> toAttr = to.getAttributes();
            Planet planetTo = gson.fromJson((String) toAttr.get("label"), Planet.class);
            gBuilder.addEdge(planetFrom, planetTo);

        }
        galaxy = gBuilder.build();
        if (logService != null) {
            logService.log(LogService.LOG_DEBUG, "galaxy = " + galaxy.getGalaxyType());
        }

        return galaxy;
    }

    private String getContent(File in) throws IOException {
        char[] tmp = new char[4096];
        StringBuilder b = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(in),
                Charset.forName(CODE_PAGE))) {
            while (true) {
                int len = reader.read(tmp);
                if (len < 0) {
                    break;
                }
                b.append(tmp, 0, len);
            }
            reader.close();
        }
        return b.toString();
    }

}
