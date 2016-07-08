package com.github.br.starmarines.gamecore.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import com.br.starwors.lx.logic.utils.PlanetCloner;
import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.exception.GameStepMistake;

public class Galaxy {
	
	private UndirectedGraph<PlanetVertex, PlanetEdge> graph;
	private Map<Planet, PlanetVertex> planets;
	private int year = 0;

	private Galaxy(UndirectedGraph<PlanetVertex, PlanetEdge> graph, Map<Planet, PlanetVertex> planets) {
		this.graph = graph;
		this.planets = planets;
	}
	
	
	// TODO мутабельный метод
	public Collection<Planet> updateState(Collection<Moves> moves, Map<Player, List<GameStepMistake>> refEmptyMap){
		year++;
		
		for(Moves m : moves){
			LinkedList<GameStepMistake> mistakesList = new LinkedList<>();
			Player player = m.getPlayer();
			
			Collection<Move> actions = m.get();
			for(Move move : actions){
				PlanetVertex sourceVertex = planets.get(move.getFrom());
				PlanetVertex targetVertex = planets.get(move.getTo());
				
				if(!graph.containsEdge(sourceVertex, targetVertex)){
					mistakesList.add(new GameStepMistake("Между планетами нет связи", player, move));
					continue;
				}	
				
				// брать юнитов можно только со своей планеты
				String owner = move.getFrom().getOwner();
				if(!player.getName().equals(owner)){
					mistakesList.add(new GameStepMistake("Брать юнитов можно только со своей планеты", player, move));
					continue;
				}	
				
				if(!sourceVertex.canMoveUnits(move.getAmount())){
					mistakesList.add(new GameStepMistake("Нельзя брать с планеты юнитов больше, чем есть", player, move));
					continue;
				}
								
				sourceVertex.addStep(new Step(player, -move.getAmount()));
				targetVertex.addStep(new Step(player, move.getAmount()));				
			}
			refEmptyMap.put(player, mistakesList);
		}
		
		Set<PlanetVertex> vertexes = graph.vertexSet();
		for(PlanetVertex vertex : vertexes){
			vertex.computePlanetsUnitsAmount();
			vertex.unitsRegeneration();
			vertex.choosePlanetsMasters();
		}
		
		return PlanetCloner.clonePlanets(planets.keySet());	
	}	
	
	public int getYear() {
		return year;
	}

	public static class PlanetVertex{
		
		private Planet planet;
		private List<Step> ownerSteps;
		private List<Step> enemySteps;
		
		public PlanetVertex(Planet planet){
			this.planet = planet;
			ownerSteps = new LinkedList<>();
			enemySteps = new LinkedList<>();
		}
		
		public boolean canMoveUnits(int amount){
			return (planet.getUnits() - amount) >= 0;
		}
		
		public void addStep(Step step){
			if(step.getPlayer().getName().equals(planet.getOwner())){
				ownerSteps.add(step);
			} else{
				enemySteps.add(step);
			}			
		}
		
		/**
		 * Вычисляется количество юнитов хозяина планеты, с учетом улетевших с этой планеты 
		 * и прилетевших на нее юнитов этого игрока.
		 */
		public void computePlanetsUnitsAmount(){
			for(Step step : ownerSteps){
				int units = step.getUnits();
				int result = planet.getUnits() + units; // units могут быть и "+" и "-"
				if(result < 0) result = 0;
				planet.setUnits(result);
			}
			ownerSteps.clear();
		}
		
		/**
		 * Получившееся количество увеличивается по правилам регенерации юнитов для этой планеты.
		 * TODO регенерация - фича самой планеты и должна быть в ней, а не здесь в галактике.
		 */
		public void unitsRegeneration(){
			String owner = planet.getOwner();
			PlanetType planetType = planet.getType();
			int units = planet.getUnits();
			if(owner != null && !owner.isEmpty()){
				units = units + units * planetType.getIncrement()/100;
				if(units > planetType.getLimit()){
					units = planetType.getLimit();
				}
				planet.setUnits(units);
			}
		}
		
		/**
		 * После этого происходит выбор нового хозяина планеты (в случае, если на планету были посланы чьи-то еще юниты). 
		 * Хозяином планеты становится игрок с наибольшим количеством юнитов. При этом юниты всех остальных игроков 
		 * уничтожаются, а у победителя остается число юнитов, на которое он превосходил следующего по численности 
		 * игрока. 
		 */
		public void choosePlanetsMasters(){
			int max = 0; // только "+" может быть у вражеских ходов
			Player strongestPlayer = null;
			for(Step step : enemySteps){
				int units = step.getUnits();
				if(units > max) {
					max = units; 
					strongestPlayer = step.getPlayer();
				}
			}
			int result = planet.getUnits() - max;
			if(result > 0){
				// планету не захватили
				planet.setUnits(result);
			} else if(result == 0){
				// не захватили, но всех убили
				planet.setUnits(result);
				planet.setOwner("");
			} else{ 
				// захватили
				result = Math.abs(result);
				planet.setUnits(result);
				planet.setOwner(strongestPlayer.getName());
			}
			
			enemySteps.clear();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((planet == null) ? 0 : planet.hashCode());
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
			PlanetVertex other = (PlanetVertex) obj;
			if (planet == null) {
				if (other.planet != null)
					return false;
			} else if (!planet.getId().equals(other.planet.getId()))
				return false;
			return true;
		}	
		
	}
	
	private static class Step{
		
		private Player player;
		private int units;
		
		public Step(Player player, int units){
			this.player = player;
			this.units = units;
		}

		public Player getPlayer() {
			return player;
		}

		public int getUnits() {
			return units;
		}		
		
	}
	
	
	private static class PlanetEdge{
		
	}
		

	public static class Builder {

		private UndirectedGraph<PlanetVertex, PlanetEdge> graph;
		private Map<Planet, PlanetVertex> planets;

		public Builder() {
			graph = new SimpleGraph<>(PlanetEdge.class);
			planets = new HashMap<>();
		}

		public Builder addEdge(Planet source, Planet target) {
			source.addNeighbour(target);
			target.addNeighbour(source);
			graph.addEdge(planets.get(source), planets.get(target));
			return this;
		}

		public Builder addPlanet(Planet planet) {
			PlanetVertex vertex = new PlanetVertex(planet);
			planets.put(planet, vertex);
			graph.addVertex(vertex);
			return this;
		}

		public Galaxy build() {
			return new Galaxy(graph, planets);
		}
	}

}
