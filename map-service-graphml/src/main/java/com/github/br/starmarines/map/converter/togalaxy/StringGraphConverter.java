package com.github.br.starmarines.map.converter.togalaxy;

import java.io.StringReader;
import java.util.Map;

import org.jgrapht.graph.SimpleGraph;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;
import org.jgrapht.io.*;

// не работает корректно в юнит-тестах, неправильный путь для xsd формирует
// https://github.com/jgrapht/jgrapht/issues/310 - бага, будет фикс в 1.01
// а пока сидим на снепшоте!!!
public class StringGraphConverter implements Converter<String, SimpleGraph<VertexPlanet, GalaxyEdge>> {

    @Override
    public SimpleGraph<VertexPlanet, GalaxyEdge> convert(String graph1AsGraphML) {

        GraphMLImporter<VertexPlanet, GalaxyEdge> importer = new GraphMLImporter<>(
                new VertexProvider<VertexPlanet>() {
                    @Override
                    public VertexPlanet buildVertex(String id,
                                                    Map<String, Attribute> attributes) {
                        Planet planet = new Planet();
                        planet.setId(Short.parseShort(id));
                        Attribute owner = attributes.get("Owner");
                        planet.setOwnerId(owner == null ? Planet.EMPTY_OWNER : Short.parseShort(owner.getValue()));
                        planet.setType(PlanetType.valueOf(attributes.get("Type").getValue()));
                        planet.setUnits(Integer.parseInt(attributes.get("Units").getValue()));
                        return new VertexPlanet(planet, Boolean.parseBoolean(attributes.get("IsStartPoint").getValue()));
                    }
                }, new EdgeProvider<VertexPlanet, GalaxyEdge>() {

            @Override
            public GalaxyEdge buildEdge(VertexPlanet from, VertexPlanet to,
                                        String label, Map<String, Attribute> attributes) {
                return new GalaxyEdge(from, to);
            }
        });
        SimpleGraph<VertexPlanet, GalaxyEdge> graph = new SimpleGraph<>(
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