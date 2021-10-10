package com.github.br.starmarines.map.converter.togalaxy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.map.converter.Converter;
import com.github.br.starmarines.map.converter.fromgalaxy.GraphStringConverter;

@Component(service=FileStringConverter.class)
public class FileStringConverter implements Converter<File, String> {

	@Override
	public String convert(File file) {
		try {
			return readFile(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String readFile(File file) throws IOException {		
		byte[] encoded = Files.readAllBytes(file.toPath());
		return new String(encoded, StandardCharsets.UTF_8);
	}

}
