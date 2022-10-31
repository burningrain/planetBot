package com.github.br.starmarines.gamecore.api;

import java.util.*;

import com.github.br.starmarines.game.api.galaxy.Planet;


public class Galaxy {

    private final String title;
    private final int maxPlayersCount;
    private final Collection<Planet> planets;
    private final Collection<Planet> startPoints;
    private final Collection<Edge> edges;
    private final int maxStepsCount;

    private final byte[] minimap;

    private Galaxy(String title,
                   int maxPlayersCount,
                   Collection<Planet> planets,
                   Collection<Edge> edges,
                   Collection<Planet> startPoints,
                   int maxStepsCount,
                   byte[] minimap
    ) {
        this.title = title;
        this.planets = planets;
        this.startPoints = startPoints;
        this.edges = new HashSet<Galaxy.Edge>(edges);
        this.maxPlayersCount = maxPlayersCount;
        this.maxStepsCount = maxStepsCount;
        this.minimap = minimap;
    }

    @Override
    public String toString() {
        return "Galaxy{" +
                "title='" + title + '\'' +
                ", maxPlayersCount=" + maxPlayersCount +
                ", planets=" + planets +
                ", startPoints=" + startPoints +
                ", edges=" + edges +
                ", maxStepsCount=" + maxStepsCount +
                ", minimap=" + Arrays.toString(minimap) +
                '}';
    }

    public int getMaxStepsCount() {
        return maxStepsCount;
    }

    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }

    public String getTitle() {
        return title;
    }

    public Collection<Planet> getPlanets() {
        return Collections.unmodifiableCollection(planets);
    }

    public Collection<Planet> getStartPoints() {
        return Collections.unmodifiableCollection(startPoints);
    }

    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    public byte[] getMinimap() {
        return minimap;
    }

    public static class Builder {

        private final String title;
        private final Map<Short, Planet> planets;
        private final Set<Planet> startPoints;
        private final Set<Edge> edges;
        private final byte[] minimap;
        private int maxStepsCount;

        public Builder(String title, byte[] minimap) {
            this.title = Objects.requireNonNull(title);
            this.minimap = Objects.requireNonNull(minimap);
            this.planets = new HashMap<>();
            this.edges = new HashSet<>();
            this.startPoints = new HashSet<>();
        }

        public Builder addEdge(short sourceId, short targetId) {
            Planet s = Objects.requireNonNull(planets.get(sourceId));
            Planet t = Objects.requireNonNull((planets.get(targetId)));
            edges.add(new Edge(s, t));
            return this;
        }

        public Builder addPlanet(Planet planet, boolean isStartPoint) {
            Objects.requireNonNull(planet);
            if(isStartPoint) startPoints.add(planet);
            planets.put(planet.getId(), planet);
            return this;
        }

        public Builder maxStepsCount(int maxStepsCount) {
            this.maxStepsCount = maxStepsCount;

            return this;
        }

        public Galaxy build() {
            //TODO выровнять нумерации планет для движка (с 0 или 1 без пропусков)
            return new Galaxy(title, startPoints.size(), planets.values(), edges, startPoints, maxStepsCount, minimap);
        }

    }

    public static class Edge {

        private final Planet from;
        private final Planet to;

        public Edge(Planet from, Planet to) {
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

        @Override
        public String toString() {
            return "{" + from.getId() + "-" + to.getId() + '}';
        }
    }

}
