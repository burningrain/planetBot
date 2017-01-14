package com.github.br.starmarines.gamecore.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.br.starmarines.game.api.galaxy.Planet;


public class Galaxy {
	
	private final GalaxyType galaxyType;
	private final Set<Planet> planets;
	private final Set<Planet> startPoints;

	private Galaxy(final GalaxyType galaxyType,  
					final Collection<Planet> planets, 
					final Set<Planet> startPoints) {
		this.galaxyType = galaxyType;
		this.planets = new HashSet<Planet>(planets);		
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

		private final Map<String, Planet> planets;
		private final GalaxyType galaxyType;
		private final Set<Planet> startPoints;

		public Builder(final GalaxyType galaxyType) {
			this.galaxyType = galaxyType;			
			planets = new HashMap<>();
			startPoints = new HashSet<>();
		}

		public Builder addEdge(Planet source, Planet target) {
			Planet s = planets.get(source.getId());
			Planet t = planets.get(target.getId());
			s.addNeighbour(t);
			t.addNeighbour(s);
			return this;
		}

		public Builder addPlanet(Planet planet, boolean isStartPoint) {
			if(isStartPoint) startPoints.add(planet);			
			planets.put(planet.getId(), planet);
			return this;
		}

		public Galaxy build() {
			return new Galaxy(galaxyType, planets.values(), startPoints);
		}
	}

}
