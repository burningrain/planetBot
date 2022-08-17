package com.github.br.starmarines.map.converter;

import com.github.br.starmarines.game.api.galaxy.Planet;

public class VertexPlanet {
	
	private final Planet planet;
	private final boolean startPoint;
	
	public VertexPlanet(Planet planet, boolean isStartPoint){
		this.planet = planet;
		this.startPoint = isStartPoint;
	}

	public Planet getPlanet() {
		return planet;
	}

	public boolean isStartPoint() {
		return startPoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planet == null) ? 0 : planet.hashCode());
		result = prime * result + (startPoint ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VertexPlanet other = (VertexPlanet) obj;
		if (planet == null) {
			if (other.planet != null)
				return false;
		} else if (!planet.equals(other.planet))
			return false;
		if (startPoint != other.startPoint)
			return false;
		return true;
	}
	
}
