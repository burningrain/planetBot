package com.github.br.starmarines.map.impl;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.GalaxyIOData;
import com.github.br.starmarines.map.converter.MapConverter;
import com.github.br.starmarines.map.service.api.MapService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Component(service = MapService.class)
public class MapServiceImpl implements MapService {

    private static final String GRAPH = "graph.graphml";
    private static final String MINIMAP = "minimap.png";

    private final MapConverter converter = new MapConverter();
    private volatile LogService logService;

    @Override
    public List<String> getAllTitles() {
        File folder = getMapsDirectory();
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null) {
            listOfFiles = new File[0];
        }

        List<String> titles = new ArrayList<String>(listOfFiles.length);
        for (File file : listOfFiles) {
            if (file.isFile()) {
                titles.add(file.getName());
            }
        }
        return titles;
    }

    @Override
    public List<String> getTitles(int startIndex, int count) {
        throw new RuntimeException("Sorry, this logic is not implemented yet");
    }

    @Override
    public Galaxy getMap(String title) {
        try {
            File map = getMapFile(title);
            ZipFile zipFile = new ZipFile(map);
            ZipEntry graphZip = zipFile.getEntry(GRAPH);
            ZipEntry minimapZip = zipFile.getEntry(MINIMAP);
            String mapAsString = new String(zipFile.getInputStream(graphZip).readAllBytes(), StandardCharsets.UTF_8);
            return converter.toGalaxy(title, new GalaxyIOData(mapAsString, zipFile.getInputStream(minimapZip).readAllBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMap(Galaxy galaxy) {
        GalaxyIOData galaxyIOData = converter.toByteArray(galaxy);

        String path = System.getProperty("user.dir") + File.separator + "maps" + File.separator + galaxy.getTitle();
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)
        ) {
            zipOutputStream.putNextEntry(new ZipEntry(GRAPH));
            zipOutputStream.write(galaxyIOData.getMapAsString().getBytes(StandardCharsets.UTF_8));
            zipOutputStream.putNextEntry(new ZipEntry(MINIMAP));
            zipOutputStream.write(galaxyIOData.getMinimap());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File getMapFile(String title) {
        File folder = getMapsDirectory();
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null) {
            listOfFiles = new File[0];
        }

        File map = null;
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().equals(title)) {
                map = file;
                break;
            }
        }
        if (map == null)
            throw new IllegalStateException("map is not found");

        return map;
    }

    private File getMapsDirectory() {
        File file = new File(System.getProperty("user.dir") + File.separator + "maps");
        if (!file.exists() || !file.isDirectory()) {
            throw new FileSystemNotFoundException("Folder 'maps' is not found");
        }
        return file;
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "unSetLogService")
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void unSetLogService(LogService logService) {
        this.logService = null;
    }

}
