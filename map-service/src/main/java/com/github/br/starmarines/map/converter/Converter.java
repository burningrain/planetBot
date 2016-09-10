package com.github.br.starmarines.map.converter;

import java.io.IOException;

public interface Converter<IN, OUT> {
	
	OUT convert(IN in) throws IOException;

}
