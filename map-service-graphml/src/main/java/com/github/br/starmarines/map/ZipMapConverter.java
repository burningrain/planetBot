package com.github.br.starmarines.map;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.GalaxyIOData;
import com.github.br.starmarines.map.converter.MapConverter;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipMapConverter {

    private static final String GRAPH = "graph.graphml";
    private static final String MINIMAP = "minimap.png";

    private final MapConverter converter = new MapConverter();

    public Galaxy getMap(File map) {
        try {
            ZipFile zipFile = new ZipFile(map);
            ZipEntry graphZip = zipFile.getEntry(GRAPH);
            ZipEntry minimapZip = zipFile.getEntry(MINIMAP);
            String mapAsString = new String(zipFile.getInputStream(graphZip).readAllBytes(), StandardCharsets.UTF_8);
            return converter.toGalaxy(map.getName(), new GalaxyIOData(mapAsString, zipFile.getInputStream(minimapZip).readAllBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMap(OutputStream out, Galaxy galaxy) {
        GalaxyIOData galaxyIOData = converter.toByteArrayData(galaxy);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
            zipOutputStream.putNextEntry(new ZipEntry(GRAPH));
            zipOutputStream.write(galaxyIOData.getMapAsString().getBytes(StandardCharsets.UTF_8));
            zipOutputStream.putNextEntry(new ZipEntry(MINIMAP));
            zipOutputStream.write(galaxyIOData.getMinimap());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
