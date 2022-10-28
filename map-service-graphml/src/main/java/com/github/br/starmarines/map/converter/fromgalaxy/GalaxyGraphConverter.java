package com.github.br.starmarines.map.converter.fromgalaxy;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;
import org.jgrapht.graph.SimpleGraph;

import java.util.Collection;

public class GalaxyGraphConverter implements Converter<Galaxy, SimpleGraph<VertexPlanet, GalaxyEdge>> {

    @Override
    public SimpleGraph<VertexPlanet, GalaxyEdge> convert(Galaxy galaxy) {
        SimpleGraph<VertexPlanet, GalaxyEdge> graph = new SimpleGraph<VertexPlanet, GalaxyEdge>(
                GalaxyEdge.class);
        Collection<Planet> startPoints = galaxy.getStartPoints();
        galaxy.getPlanets()
                .forEach(
                        p -> graph.addVertex(new VertexPlanet(p, startPoints
                                .contains(p))));
        galaxy.getEdges()
                .forEach(
                        e -> {
                            VertexPlanet from = new VertexPlanet(e.getFrom(),
                                    startPoints.contains(e.getFrom()));
                            VertexPlanet to = new VertexPlanet(e.getTo(),
                                    startPoints.contains(e.getTo()));
                            graph.addEdge(from, to,
                                    new GalaxyEdge(from, to));
                        });

        return graph;
    }

}
