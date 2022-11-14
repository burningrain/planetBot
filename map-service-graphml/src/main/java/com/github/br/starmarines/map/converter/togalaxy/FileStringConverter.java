package com.github.br.starmarines.map.converter.togalaxy;

import com.github.br.starmarines.map.converter.Converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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