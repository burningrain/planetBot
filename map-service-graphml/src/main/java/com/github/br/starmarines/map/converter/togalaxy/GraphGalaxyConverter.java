package com.github.br.starmarines.map.converter.togalaxy;

import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;

@Component(service=GraphGalaxyConverter.class)
public class GraphGalaxyConverter {

	public Galaxy convert(String title, byte[] minimap, UndirectedGraph<VertexPlanet, GalaxyEdge> graph) {
		Galaxy.Builder builder = new Galaxy.Builder(title, minimap);
		
		Set<VertexPlanet> planets = graph.vertexSet();
		Set<GalaxyEdge> edges = graph.edgeSet();
		planets.forEach(p -> builder.addPlanet(p.getPlanet(), p.isStartPoint()));
		edges.forEach((e) -> builder.addEdge(e.getFrom().getPlanet().getId(), e.getTo().getPlanet().getId()));
		
		return builder.build();
	}

}
