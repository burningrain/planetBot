package com.github.br.starmarines.map.converter;

public class GalaxyIOData {

    private final String mapAsString;
    private final byte[] minimap;
    private final String gameDataAsString;

    public GalaxyIOData(String mapAsString, byte[] minimap, String gameDataAsString) {
        this.mapAsString = mapAsString;
        this.minimap = minimap;
        this.gameDataAsString = gameDataAsString;
    }

    public String getMapAsString() {
        return mapAsString;
    }

    public byte[] getMinimap() {
        return minimap;
    }

    public String getGameDataAsString() {
        return gameDataAsString;
    }

}
