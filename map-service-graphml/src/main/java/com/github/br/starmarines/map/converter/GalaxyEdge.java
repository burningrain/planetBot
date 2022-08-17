package com.github.br.starmarines.map.converter;

import org.jgrapht.graph.DefaultEdge;

import java.util.Objects;

public class GalaxyEdge extends DefaultEdge {

	private static final long serialVersionUID = 1L;

	private final VertexPlanet from;
	private final VertexPlanet to;
	
	public GalaxyEdge(VertexPlanet from, VertexPlanet to){
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
	}

	public VertexPlanet getFrom() {
		return from;
	}

	public VertexPlanet getTo() {
		return to;
	}	

}
