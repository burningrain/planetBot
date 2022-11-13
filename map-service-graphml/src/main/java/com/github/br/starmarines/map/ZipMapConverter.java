package com.github.br.starmarines.map;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.GalaxyIOData;
import com.github.br.starmarines.map.converter.MapConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipMapConverter {

    private static final String GRAPH = "graph.graphml";
    private static final String MINIMAP = "minimap.png";
    private static final String GAME_DATA = "game-data.json";

    private final MapConverter converter = new MapConverter();

    public Galaxy toGalaxy(byte[] map) {
        HashMap<String, byte[]> result = new HashMap<>();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(map))) {
            ZipEntry zipEntry = null;
            while ((zipEntry = zis.getNextEntry()) != null) {
                byte[] array = zis.readAllBytes();
                result.put(zipEntry.getName(), array);
                zis.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String mapAsString = new String(result.get(GRAPH), StandardCharsets.UTF_8);
        String gameDataAsString = new String(result.get(GAME_DATA), StandardCharsets.UTF_8);
        return converter.toGalaxy(
                new GalaxyIOData(
                        mapAsString,
                        result.get(MINIMAP),
                        gameDataAsString
                )
        );
    }

    public byte[] toByteArray(Galaxy galaxy) {
        GalaxyIOData galaxyIOData = converter.toByteArrayData(galaxy);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
            zipOutputStream.putNextEntry(new ZipEntry(GRAPH));
            zipOutputStream.write(galaxyIOData.getMapAsString().getBytes(StandardCharsets.UTF_8));
            zipOutputStream.putNextEntry(new ZipEntry(MINIMAP));
            zipOutputStream.write(galaxyIOData.getMinimap());
            zipOutputStream.putNextEntry(new ZipEntry(GAME_DATA));
            zipOutputStream.write(galaxyIOData.getGameDataAsString().getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }

}