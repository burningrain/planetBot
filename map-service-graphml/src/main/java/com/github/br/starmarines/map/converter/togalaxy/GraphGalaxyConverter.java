package com.github.br.starmarines.map.converter.togalaxy;

import java.util.Set;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.GameData;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;
import org.jgrapht.graph.SimpleGraph;

public class GraphGalaxyConverter {

	public Galaxy convert(String title, byte[] minimap, GameData gameData, SimpleGraph<VertexPlanet, GalaxyEdge> graph) {
		Galaxy.Builder builder = new Galaxy.Builder(title, minimap);
		builder.maxStepsCount(gameData.getMaxStepsCount());
		
		Set<VertexPlanet> planets = graph.vertexSet();
		Set<GalaxyEdge> edges = graph.edgeSet();
		planets.forEach(p -> builder.addPlanet(p.getPlanet(), p.isStartPoint()));
		edges.forEach((e) -> builder.addEdge(e.getFrom().getPlanet().getId(), e.getTo().getPlanet().getId()));
		
		return builder.build();
	}

}
