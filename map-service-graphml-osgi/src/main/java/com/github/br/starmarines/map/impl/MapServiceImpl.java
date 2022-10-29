package com.github.br.starmarines.map.impl;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.ZipMapConverter;
import com.github.br.starmarines.map.service.api.MapService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Component(service = MapService.class)
public class MapServiceImpl implements MapService {

    private final ZipMapConverter converter = new ZipMapConverter();
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
            return converter.toGalaxy(map.getName(), Files.readAllBytes(map.toPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveMap(Galaxy galaxy) {
        String path = System.getProperty("user.dir") + File.separator + "maps" + File.separator + galaxy.getTitle();
        byte[] bytes = converter.toByteArray(galaxy);
        try {
            Files.write(Paths.get(path), bytes, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
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
