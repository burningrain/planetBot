package com.github.br.starmarines.map.converter;

public class GalaxyIOData {

    private final String mapAsString;
    private final byte[] minimap;

    public GalaxyIOData(String mapAsString, byte[] minimap) {
        this.mapAsString = mapAsString;
        this.minimap = minimap;
    }

    public String getMapAsString() {
        return mapAsString;
    }

    public byte[] getMinimap() {
        return minimap;
    }

}
