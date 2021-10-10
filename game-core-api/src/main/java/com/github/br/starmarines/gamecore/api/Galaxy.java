package com.github.br.starmarines.gamecore.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.br.starmarines.game.api.galaxy.Planet;


public class Galaxy {

	private final int maxPlayersCount;
	private final GalaxyType galaxyType;
	private final Set<Planet> planets;
	private final Set<Planet> startPoints;
	private final Set<Edge> edges;

	private Galaxy(final int maxPlayersCount,
				   final GalaxyType galaxyType,
					final Collection<Planet> planets, 
					final Set<Edge> edges,
					final Set<Planet> startPoints) {
		this.galaxyType = galaxyType;
		this.planets = new HashSet<Planet>(planets);
		this.edges = new HashSet<Galaxy.Edge>(edges);
		this.startPoints = startPoints;
		this.maxPlayersCount = maxPlayersCount;
	}

	public int getMaxPlayersCount() {
		return maxPlayersCount;
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

	public Set<Edge> getEdges() {
		return Collections.unmodifiableSet(edges);
	}


	public static class Builder {

		private int maxPlayersCount;
		private final Map<String, Planet> planets;
		private final Set<Edge> edges;
		private final GalaxyType galaxyType;
		private final Set<Planet> startPoints;

		public Builder(final GalaxyType galaxyType) {
			this.galaxyType = galaxyType;			
			this.planets = new HashMap<>();
			this.edges = new HashSet<>();
			this.startPoints = new HashSet<>();
		}

		public Builder addEdge(Planet source, Planet target) {
			Planet s = planets.get(source.getId());
			Planet t = planets.get(target.getId());
			s.addNeighbour(t);
			t.addNeighbour(s);
			edges.add(new Edge(s, t));
			return this;
		}

        public Builder addPlanet(Planet planet, boolean isStartPoint) {
            if (isStartPoint) startPoints.add(planet);
            planets.put(planet.getId(), planet);
            return this;
        }

		public Builder maxPlayersCount(int maxPlayersCount) {
			this.maxPlayersCount = maxPlayersCount;
			return this;
		}

		public Galaxy build() {
			return new Galaxy(maxPlayersCount, galaxyType, planets.values(), edges, startPoints);
		}
	}

	public static class Edge {

		private Planet from;
		private Planet to;

		public Edge(Planet from, Planet to){
			this.from = from;
			this.to = to;
		}

		public Planet getFrom() {
			return from;
		}

		public Planet getTo() {
			return to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
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
			Edge other = (Edge) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
		}

	}

}
