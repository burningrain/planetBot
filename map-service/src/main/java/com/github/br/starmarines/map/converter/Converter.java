package com.github.br.starmarines.map.converter;

import com.github.br.starmarines.map.service.api.MapValidationException;

import java.io.IOException;

public interface Converter<IN, OUT> {
	
	OUT convert(IN in) throws IOException, MapValidationException;

}
