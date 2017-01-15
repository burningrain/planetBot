package com.github.br.starmarines.map.converter.togalaxy;

import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.api.GalaxyType;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;

@Component(service=GraphGalaxyConverter.class)
public class GraphGalaxyConverter implements
		Converter<UndirectedGraph<VertexPlanet, GalaxyEdge>, Galaxy> {

	@Override
	public Galaxy convert(UndirectedGraph<VertexPlanet, GalaxyEdge> graph) {
		//TODO вообще убрать параметр после fx. По факту нужен, чтобы верно отрисовать граф
		Galaxy.Builder builder = new Galaxy.Builder(GalaxyType.SMALL_BASES);
		
		Set<VertexPlanet> planets = graph.vertexSet();
		Set<GalaxyEdge> edges = graph.edgeSet();
		planets.stream().forEach(p -> builder.addPlanet(p.getPlanet(), p.isStartPoint()));
		edges.stream().forEach((e) -> builder.addEdge(e.getFrom().getPlanet(), e.getTo().getPlanet()));		
		
		return builder.build();
	}

}
