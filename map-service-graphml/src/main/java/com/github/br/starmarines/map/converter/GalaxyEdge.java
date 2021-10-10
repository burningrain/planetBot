package com.github.br.starmarines.map.converter;

import org.jgrapht.graph.DefaultEdge;

public class GalaxyEdge extends DefaultEdge {

	private static final long serialVersionUID = 1L;

	private VertexPlanet from;
	private VertexPlanet to;
	
	public GalaxyEdge(VertexPlanet from, VertexPlanet to){
		this.from = from;
		this.to = to;
	}

	public VertexPlanet getFrom() {
		return from;
	}

	public VertexPlanet getTo() {
		return to;
	}	

}
