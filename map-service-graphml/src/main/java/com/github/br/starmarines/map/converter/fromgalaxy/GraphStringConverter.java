package com.github.br.starmarines.map.converter.fromgalaxy;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.GraphMLExporter.AttributeCategory;
import org.jgrapht.ext.GraphMLExporter.AttributeType;
import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;

@Component(service=GraphStringConverter.class)
public class GraphStringConverter implements
		Converter<UndirectedGraph<VertexPlanet, GalaxyEdge>, String> {

	@Override
	public String convert(UndirectedGraph<VertexPlanet, GalaxyEdge> graph) {
		// example
		// https://github.com/jgrapht/jgrapht/blob/master/jgrapht-demo/src/main/java/org/jgrapht/demo/GraphMLDemo.java
		GraphMLExporter<VertexPlanet, GalaxyEdge> exporter = new GraphMLExporter<>();

		exporter.setVertexAttributeProvider(new ComponentAttributeProvider<VertexPlanet>() {
			
			@Override
			public Map<String, String> getComponentAttributes(VertexPlanet component) {
				HashMap<String, String> map = new HashMap<String, String>();
				Planet planet = component.getPlanet();
				map.put("Owner", planet.getOwner());
				map.put("Type", planet.getType().name());
				map.put("Units", String.valueOf(planet.getUnits()));
				map.put("IsStartPoint", String.valueOf(component.isStartPoint()));
				
				return map;
			}
		});
		
		// set to export the internal edge weights
		exporter.setExportEdgeWeights(false);

		// register additional color attribute for vertices
		exporter.registerAttribute("Owner", AttributeCategory.NODE,
				AttributeType.STRING);
		exporter.registerAttribute("Type", AttributeCategory.NODE,
				AttributeType.STRING);
		exporter.registerAttribute("Units", AttributeCategory.NODE,
				AttributeType.INT);
		exporter.registerAttribute("IsStartPoint", AttributeCategory.NODE,
				AttributeType.BOOLEAN);
		
		StringWriter writer = new StringWriter();
		try {
			exporter.exportGraph(graph, writer);
		} catch (ExportException e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

}
