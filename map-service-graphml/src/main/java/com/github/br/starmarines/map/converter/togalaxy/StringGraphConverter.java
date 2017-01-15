package com.github.br.starmarines.map.converter.togalaxy;

import java.io.StringReader;
import java.util.Map;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.EdgeProvider;
import org.jgrapht.ext.GraphMLImporter;
import org.jgrapht.ext.ImportException;
import org.jgrapht.ext.VertexProvider;
import org.jgrapht.graph.SimpleGraph;
import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;

// не работает корректно в юнит-тестах, неправильный путь для xsd формирует
// https://github.com/jgrapht/jgrapht/issues/310 - бага, будет фикс в 1.01
// а пока сидим на снепшоте!!!
@Component(service=StringGraphConverter.class)
public class StringGraphConverter implements Converter<String, UndirectedGraph<VertexPlanet, GalaxyEdge>> {

	@Override
	public UndirectedGraph<VertexPlanet, GalaxyEdge> convert(String graph1AsGraphML) {

		GraphMLImporter<VertexPlanet, GalaxyEdge> importer = new GraphMLImporter<>(
				new VertexProvider<VertexPlanet>() {

					@Override
					public VertexPlanet buildVertex(String id,
							Map<String, String> attributes) {						
						Planet planet = new Planet(id);
						planet.setOwner(attributes.get("Owner"));
						planet.setType(PlanetType.valueOf(attributes.get("Type")));
						planet.setUnits(Integer.parseInt(attributes.get("Units")));
						return new VertexPlanet(planet, Boolean.valueOf(attributes.get("IsStartPoint")));
					}
				}, new EdgeProvider<VertexPlanet, GalaxyEdge>() {

					@Override
					public GalaxyEdge buildEdge(VertexPlanet from, VertexPlanet to,
							String label, Map<String, String> attributes) {
						return new GalaxyEdge(from, to);
					}
				});
		UndirectedGraph<VertexPlanet, GalaxyEdge> graph = new SimpleGraph<>(
				GalaxyEdge.class);
		ClassLoader currentClassloader = null;
		try {
			currentClassloader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(importer.getClass().getClassLoader());
			importer.importGraph(graph, new StringReader(graph1AsGraphML));
		} catch (ImportException e) {
			throw new RuntimeException(e);
		} finally {
			Thread.currentThread().setContextClassLoader(currentClassloader);
		}
		return graph;
	}

}