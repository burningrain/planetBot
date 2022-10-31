package com.github.br.starmarines.map.converter.fromgalaxy;

import java.io.StringWriter;
import java.util.HashMap;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;

public class GraphStringConverter implements Converter<SimpleGraph<VertexPlanet, GalaxyEdge>, String> {

    @Override
    public String convert(SimpleGraph<VertexPlanet, GalaxyEdge> graph) {
        // example
        // https://github.com/jgrapht/jgrapht/blob/master/jgrapht-demo/src/main/java/org/jgrapht/demo/GraphMLDemo.java
        GraphMLExporter<VertexPlanet, GalaxyEdge> exporter = new GraphMLExporter<>();

        exporter.setVertexAttributeProvider(component -> {
            HashMap<String, Attribute> map = new HashMap<String, Attribute>();
            Planet planet = component.getPlanet();
            map.put("Owner", DefaultAttribute.createAttribute(String.valueOf(planet.getOwnerId())));
            map.put("Type", DefaultAttribute.createAttribute(planet.getType().name()));
            map.put("Units", DefaultAttribute.createAttribute(planet.getUnits()));
            map.put("IsStartPoint", DefaultAttribute.createAttribute(component.isStartPoint()));
            map.put("x", DefaultAttribute.createAttribute(String.valueOf(planet.getX())));
            map.put("y", DefaultAttribute.createAttribute(String.valueOf(planet.getY())));

            return map;
        });

        // set to export the internal edge weights
        exporter.setExportEdgeWeights(false);

        // register additional color attribute for vertices
        exporter.registerAttribute("Owner", GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute("Type", GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute("Units", GraphMLExporter.AttributeCategory.NODE, AttributeType.INT);
        exporter.registerAttribute("IsStartPoint", GraphMLExporter.AttributeCategory.NODE, AttributeType.BOOLEAN);
        exporter.registerAttribute("x", GraphMLExporter.AttributeCategory.NODE, AttributeType.FLOAT);
        exporter.registerAttribute("y", GraphMLExporter.AttributeCategory.NODE, AttributeType.FLOAT);

        StringWriter writer = new StringWriter();
        try {
            exporter.exportGraph(graph, writer);
        } catch (ExportException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

}
