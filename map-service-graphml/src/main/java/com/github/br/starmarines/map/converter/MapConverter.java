package com.github.br.starmarines.map.converter;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.fromgalaxy.GalaxyGraphConverter;
import com.github.br.starmarines.map.converter.fromgalaxy.GraphStringConverter;
import com.github.br.starmarines.map.converter.togalaxy.GraphGalaxyConverter;
import com.github.br.starmarines.map.converter.togalaxy.StringGraphConverter;
import org.jgrapht.graph.SimpleGraph;

public class MapConverter {

    // --> to Galaxy
    private final Converter<String, SimpleGraph<VertexPlanet, GalaxyEdge>> stringGraphConverter = new StringGraphConverter();
    private final GraphGalaxyConverter graphGalaxyConverter = new GraphGalaxyConverter();
    // <-- from Galaxy
    private final Converter<Galaxy, SimpleGraph<VertexPlanet, GalaxyEdge>> galaxyGraphConverter = new GalaxyGraphConverter();
    private final Converter<SimpleGraph<VertexPlanet, GalaxyEdge>, String> graphStringConverter = new GraphStringConverter();


    public Galaxy toGalaxy(String title, GalaxyIOData galaxyIOData) {
        String mapAsString = galaxyIOData.getMapAsString();
        SimpleGraph<VertexPlanet, GalaxyEdge> graph = stringGraphConverter.convert(mapAsString);
        return graphGalaxyConverter.convert(title, galaxyIOData.getMinimap(), graph);
    }

    public GalaxyIOData toByteArrayData(Galaxy galaxy) {
        SimpleGraph<VertexPlanet, GalaxyEdge> graph = galaxyGraphConverter.convert(galaxy);
        return new GalaxyIOData(graphStringConverter.convert(graph), galaxy.getMinimap());
    }

}
