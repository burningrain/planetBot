package com.github.br.starmarines.map.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.GalaxyEdge;
import com.github.br.starmarines.map.converter.VertexPlanet;
import com.github.br.starmarines.map.converter.fromgalaxy.GalaxyGraphConverter;
import com.github.br.starmarines.map.converter.fromgalaxy.GraphStringConverter;
import com.github.br.starmarines.map.converter.togalaxy.FileStringConverter;
import com.github.br.starmarines.map.converter.togalaxy.GraphGalaxyConverter;
import com.github.br.starmarines.map.converter.togalaxy.GraphmlConverter;
import com.github.br.starmarines.map.converter.togalaxy.StringGraphConverter;
import com.github.br.starmarines.map.service.api.MapService;
import com.github.br.starmarines.map.service.api.MapValidationException;

@Component
public class MapServiceImpl implements MapService {

	// --> to Galaxy
	private Converter<File, String> fileStringConverter;
	private Converter<String, String> xmlToXmlConverter;
	private Converter<String, UndirectedGraph<VertexPlanet, GalaxyEdge>> stringGraphConverter;
	private Converter<UndirectedGraph<VertexPlanet, GalaxyEdge>, Galaxy> graphGalaxyConverter;
	// <-- from Galaxy
	private Converter<Galaxy, UndirectedGraph<VertexPlanet, GalaxyEdge>> galaxyGraphConverter;
	private Converter<UndirectedGraph<VertexPlanet, GalaxyEdge>, String> graphStringConverter;

	private volatile LogService logService;

	@Override
	public List<String> getAllTitles() {
		File folder = getMapsDirectory();
		File[] listOfFiles = folder.listFiles();

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
		throw new RuntimeException("Sorry, this logic not implemented yet");
	}

	@Override
	public Galaxy getMap(String title) throws MapValidationException {
		File map = getMapFile(title);
		String mapAsString = null;
		try {
			mapAsString = fileStringConverter.convert(map);
			mapAsString = xmlToXmlConverter.convert(mapAsString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UndirectedGraph<VertexPlanet, GalaxyEdge> graph = stringGraphConverter
				.convert(mapAsString);
		Galaxy galaxy = graphGalaxyConverter.convert(graph);
		return galaxy;
	}

	public void saveMap(Galaxy galaxy, String title) {
		UndirectedGraph<VertexPlanet, GalaxyEdge> graph = galaxyGraphConverter
				.convert(galaxy);
		String graphML = graphStringConverter.convert(graph);
		File file = new File(System.getProperty("user.dir") + File.separator
				+ "maps" + File.separator + title);		
		
		try {
			Files.write(file.toPath(), graphML.getBytes());
		} catch (IOException e) {			
			throw new RuntimeException(e);
		}
	}

	private File getMapFile(String title) {
		File folder = getMapsDirectory();
		File[] listOfFiles = folder.listFiles();

		File map = null;
		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().equals(title)) {
				map = file;
				break;
			}
		}
		if (map == null)
			throw new IllegalStateException("map not found");

		return map;
	}

	private File getMapsDirectory() {
		File file = new File(System.getProperty("user.dir") + File.separator
				+ "maps");
		if (!file.exists() || !file.isDirectory()) {
			throw new FileSystemNotFoundException("Folder 'maps' not found");
		}
		return file;
	}
	

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	protected void setFileStringConverter(FileStringConverter fileStringConverter) {
		this.fileStringConverter = fileStringConverter;
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	protected void setStringGraphConverter(StringGraphConverter stringGraphConverter) {
		this.stringGraphConverter = stringGraphConverter;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	protected void setGraphGalaxyConverter(GraphGalaxyConverter graphGalaxyConverter) {
		this.graphGalaxyConverter = graphGalaxyConverter;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	protected void setGalaxyGraphConverter(GalaxyGraphConverter galaxyGraphConverter) {
		this.galaxyGraphConverter = galaxyGraphConverter;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	protected void setGraphStringConverter(GraphStringConverter graphStringConverter) {
		this.graphStringConverter = graphStringConverter;
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy=ReferencePolicy.STATIC)
	protected void setXmlToXmlConverter(GraphmlConverter xmlToXmlConverter) {
		this.xmlToXmlConverter = xmlToXmlConverter;
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy=ReferencePolicy.DYNAMIC, unbind="unSetLogService")
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public void unSetLogService(LogService logService) {
		this.logService = null;
	}

}
