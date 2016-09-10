package com.github.br.starmarines.map.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.service.api.MapService;

@Component
public class MapServiceImpl implements MapService {
	
	private Converter<File, Galaxy> converter;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	protected void setConverter(Converter<File, Galaxy> converter){
		this.converter = converter;
	}
	

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
	public Galaxy getMap(String title) {
		File folder = getMapsDirectory();
		File[] listOfFiles = folder.listFiles();		
		
		File map = null;
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().equals(title)) {
		        map = file;
		        continue;
		    }
		}
		if(map == null) throw new IllegalStateException("map not found");
		
		try {
			return converter.convert(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private File getMapsDirectory(){
		System.out.println(System.getProperty("user.dir"));
		File file = new File(System.getProperty("user.dir") + File.separator + "maps");
		if(!file.exists() || !file.isDirectory()){
			throw new RuntimeException("Folder 'maps' not found");
		}
		return file;
	}

}
