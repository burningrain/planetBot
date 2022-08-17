package com.github.br.starmarines.map.converter.fromgalaxy;

import java.util.Collection;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;

@Component(service = GalaxyGraphConverter.class)
public class GalaxyGraphConverter implements
        Converter<Galaxy, UndirectedGraph<VertexPlanet, GalaxyEdge>> {

    @Override
    public UndirectedGraph<VertexPlanet, GalaxyEdge> convert(Galaxy galaxy) {
        UndirectedGraph<VertexPlanet, GalaxyEdge> graph = new SimpleGraph<VertexPlanet, GalaxyEdge>(
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
