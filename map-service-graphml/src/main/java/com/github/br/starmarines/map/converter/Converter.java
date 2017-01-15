package com.github.br.starmarines.map.converter;


public interface Converter<IN, OUT> {
	
	OUT convert(IN in);

}
