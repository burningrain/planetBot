package com.github.br.starmarines.map.converter;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.fromgalaxy.GalaxyGraphConverter;
import com.github.br.starmarines.map.converter.fromgalaxy.GraphStringConverter;
import com.github.br.starmarines.map.converter.togalaxy.GraphGalaxyConverter;
import com.github.br.starmarines.map.converter.togalaxy.GraphmlConverter;
import com.github.br.starmarines.map.converter.togalaxy.StringGraphConverter;
import org.jgrapht.UndirectedGraph;

public class MapConverter {

    // --> to Galaxy
    private final Converter<String, String> xmlToXmlConverter = new GraphmlConverter();
    private final Converter<String, UndirectedGraph<VertexPlanet, GalaxyEdge>> stringGraphConverter = new StringGraphConverter();
    private final GraphGalaxyConverter graphGalaxyConverter = new GraphGalaxyConverter();
    // <-- from Galaxy
    private final Converter<Galaxy, UndirectedGraph<VertexPlanet, GalaxyEdge>> galaxyGraphConverter = new GalaxyGraphConverter();
    private final Converter<UndirectedGraph<VertexPlanet, GalaxyEdge>, String> graphStringConverter = new GraphStringConverter();


    public Galaxy toGalaxy(String title, GalaxyIOData galaxyIOData) {
        String mapAsString = galaxyIOData.getMapAsString();
        try {
            mapAsString = xmlToXmlConverter.convert(mapAsString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UndirectedGraph<VertexPlanet, GalaxyEdge> graph = stringGraphConverter.convert(mapAsString);
        return graphGalaxyConverter.convert(title, galaxyIOData.getMinimap(), graph);
    }

    public GalaxyIOData toByteArray(Galaxy galaxy) {
        UndirectedGraph<VertexPlanet, GalaxyEdge> graph = galaxyGraphConverter
                .convert(galaxy);
        return new GalaxyIOData(graphStringConverter.convert(graph), galaxy.getMinimap());
    }


}
