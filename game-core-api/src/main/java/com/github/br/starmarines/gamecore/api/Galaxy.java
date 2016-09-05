package com.github.br.starmarines.gamecore.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.github.br.starmarines.game.api.galaxy.Planet;


public class Galaxy {
	
	private final GalaxyType galaxyType;
	private final Set<Planet> planets;
	private final Set<Planet> startPoints;

	private Galaxy(final GalaxyType galaxyType,  
					final Set<Planet> planets, 
					final Set<Planet> startPoints) {
		this.galaxyType = galaxyType;
		this.planets = planets;		
		this.startPoints = startPoints;
	}		

	public GalaxyType getGalaxyType() {
		return galaxyType;
	}
	
	public Set<Planet> getStartPoints(){
		return Collections.unmodifiableSet(startPoints);
	}
	
	public Set<Planet> getPlanets(){
		return Collections.unmodifiableSet(planets);
	}			

	public static class Builder {

		private Set<Planet> planets;
		private final GalaxyType galaxyType;
		private Set<Planet> startPoints;

		public Builder(final GalaxyType galaxyType) {
			this.galaxyType = galaxyType;			
			planets = new HashSet<>();
			startPoints = new HashSet<>();
		}

		public Builder addEdge(Planet source, Planet target) {
			source.addNeighbour(target);
			target.addNeighbour(source);
			return this;
		}

		public Builder addPlanet(Planet planet, boolean isStartPoint) {
			if(isStartPoint) startPoints.add(planet);			
			planets.add(planet);
			return this;
		}

		public Galaxy build() {
			return new Galaxy(galaxyType, planets, startPoints);
		}
	}

}
