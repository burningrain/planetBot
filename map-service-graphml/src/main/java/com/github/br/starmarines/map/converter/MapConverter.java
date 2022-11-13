package com.github.br.starmarines.map.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.GameData;
import com.github.br.starmarines.map.converter.fromgalaxy.GalaxyGraphConverter;
import com.github.br.starmarines.map.converter.fromgalaxy.GraphStringConverter;
import com.github.br.starmarines.map.converter.togalaxy.GraphGalaxyConverter;
import com.github.br.starmarines.map.converter.togalaxy.StringGraphConverter;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MapConverter {

    // --> to Galaxy
    private final Converter<String, SimpleGraph<VertexPlanet, GalaxyEdge>> stringGraphConverter = new StringGraphConverter();
    private final GraphGalaxyConverter graphGalaxyConverter = new GraphGalaxyConverter();
    // <-- from Galaxy
    private final Converter<Galaxy, SimpleGraph<VertexPlanet, GalaxyEdge>> galaxyGraphConverter = new GalaxyGraphConverter();
    private final Converter<SimpleGraph<VertexPlanet, GalaxyEdge>, String> graphStringConverter = new GraphStringConverter();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Galaxy toGalaxy(GalaxyIOData galaxyIOData) {
        SimpleGraph<VertexPlanet, GalaxyEdge> graph = stringGraphConverter.convert(galaxyIOData.getMapAsString());
        GameData gameData = gameDataFromBytes(galaxyIOData.getGameDataAsString());
        return graphGalaxyConverter.convert(galaxyIOData.getMinimap(), gameData, graph);
    }

    public GalaxyIOData toByteArrayData(Galaxy galaxy) {
        SimpleGraph<VertexPlanet, GalaxyEdge> graph = galaxyGraphConverter.convert(galaxy);
        return new GalaxyIOData(
                graphStringConverter.convert(graph),
                galaxy.getMinimap(), new String(gameDataToBytes(galaxy), StandardCharsets.UTF_8)
        );
    }

    private GameData createGameData(Galaxy galaxy) {
        GameData gameData = new GameData();
        gameData.setTitle(galaxy.getTitle());
        gameData.setDescription(galaxy.getDescription());
        gameData.setMaxStepsCount(galaxy.getMaxStepsCount());

        return gameData;
    }

    private byte[] gameDataToBytes(Galaxy galaxy) {
        try {
            return objectMapper.writeValueAsBytes(createGameData(galaxy));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private GameData gameDataFromBytes(String json) {
        try {
            return objectMapper.readValue(json, GameData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
