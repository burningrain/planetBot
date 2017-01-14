package com.github.br.starmarines.gamecore;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import com.br.starwors.lx.logic.utils.anotherone.PlanetCloner;
import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.api.GalaxyType;
import com.github.br.starmarines.gamecore.api.Player;
import com.github.br.starmarines.gamecore.mistakes.GameStepMistake;

public class GalaxyEngine {
	
	private final GalaxyType galaxyType;
	private UndirectedGraph<PlanetVertex, DefaultWeightedEdge> graph;
	private Map<Planet, PlanetVertex> planets;
	private int year = 0;
	
	private List<Planet> startPoints;

	private GalaxyEngine(final GalaxyType galaxyType, 
					UndirectedGraph<PlanetVertex, DefaultWeightedEdge> graph, 
					Map<Planet, PlanetVertex> planets, 
					List<Planet> startPoints) {
		this.galaxyType = galaxyType;
		this.graph = graph;
		this.planets = planets;
		
		this.startPoints = startPoints;
	}
	
	public void initPlayersPositions(Set<String> owners){
		if(owners.size() > startPoints.size()) throw new IllegalStateException("Стартовых точек меньше, чем игроков");
		
		Collections.shuffle(startPoints);
		Iterator<Planet> it = startPoints.iterator();
		for(String owner : owners){
			Planet planet = it.next();
			planet.setOwner(owner);
		}
	}	

	// TODO мутабельный метод
	public Collection<Planet> updateState(Collection<Moves> moves, Map<Player, List<GameStepMistake>> refEmptyMap){
		year++;

		for(Moves m : moves){
			LinkedList<GameStepMistake> mistakesList = new LinkedList<>();
			Player player = m.getPlayer();
			
			// добавленные юниты не считаются. Нельзя перекинуть на планету с 1 юнитом 100 юнитов и сразу же взять с неё 101 юнит
			Map<Planet, Integer> fromPlanets = m.getComputeFromPlanets(); 
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
				
				int fromMove = fromPlanets.get(move.getFrom()); // берем суммарную потерю юнитов на планете от всех ходов игрока
				if(!sourceVertex.canMoveUnits(fromMove)){
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

	public GalaxyType getGalaxyType() {
		return galaxyType;
	}



	public static class PlanetVertex{
		
		private Planet planet;
		private List<Step> ownerSteps;
		private HashMap<Player, Integer> enemySteps;
		
		public PlanetVertex(Planet planet){
			this.planet = planet;
			ownerSteps = new LinkedList<>();
			enemySteps = new HashMap<>();
		}
		
		public boolean canMoveUnits(int amount){
			return (planet.getUnits() - amount) >= 0;
		}
		
		public void addStep(Step step){
			if(step.getPlayer().getName().equals(planet.getOwner())){
				ownerSteps.add(step);
			} else{
				// враг может сделать несколько ходов на одну планету (н-р, с разных планет). Считаем его суммарный удар.
				int value = 0;
				if(enemySteps.get(step.getPlayer()) == null){
					value = step.getUnits();
				} else{
					value = enemySteps.get(step.getPlayer()) + step.getUnits();
				}
				enemySteps.put(step.getPlayer(), value);
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
				//TODO выкидывать ошибку? Отправлено юнитов больше, чем есть?
				if(result <= 0) {
					result = 0;
					planet.setOwner("");
				} 
				planet.setUnits(result);
			}
			ownerSteps.clear();
		}
		
		/**
		 * Получившееся количество увеличивается по правилам регенерации юнитов для этой планеты. 
		 */
		public void unitsRegeneration(){
			String owner = planet.getOwner();
			PlanetType planetType = planet.getType();
			int units = planet.getUnits();
			if(owner != null && !owner.isEmpty()){
				boolean isNeedRegeneration = units < planetType.getLimit();
				if(isNeedRegeneration){
					units = units + units * planetType.getIncrement()/100;
					if(units > planetType.getLimit()){
						units = planetType.getLimit();
					}
					planet.setUnits(units);
				}				
			}
		}
		
		/**
		 * После этого происходит выбор нового хозяина планеты (в случае, если на планету были посланы чьи-то еще юниты). 
		 * Хозяином планеты становится игрок с наибольшим количеством юнитов. При этом юниты всех остальных игроков 
		 * уничтожаются, а у победителя остается число юнитов, на которое он превосходил следующего по численности 
		 * игрока. 
		 */
		public void choosePlanetsMasters(){
			// если враги не послали на планету юнитов, считать нечего
			if(enemySteps.size() == 0){
				return; 
			}			
			
			enemySteps.put(new Player(planet.getOwner()), planet.getUnits()); //TODO кривота, убрать
			// только "+" может быть у вражеских ходов;
			// сортируем по убыванию
			Object[] steps = enemySteps.entrySet().toArray();
			Arrays.sort(steps, new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					return ((Map.Entry<Player, Integer>) o2).getValue()
			                   .compareTo(((Map.Entry<Player, Integer>) o1).getValue());
				}
				
			});
			Map.Entry<Player, Integer> first = (Map.Entry<Player, Integer>)steps[0];
			Map.Entry<Player, Integer> second = (Map.Entry<Player, Integer>)steps[1];
			
			int result = first.getValue() - second.getValue();
			if(result > 0){
				// планету захватили или хозяин отбился
				planet.setUnits(result);
				planet.setOwner(first.getKey().getName());
			} else if(result == 0){
				// не захватили, но всех убили
				planet.setUnits(result);
				planet.setOwner("");
			} else{ 
				// второй по числу юнитов обошел первого. Как такое случилось?
				throw new IllegalStateException("Ошибка при подсчете хозяина планеты");
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
	
	
//	public static class PlanetEdge {
//
//	}
		

	public static class Builder {

		private UndirectedGraph<PlanetVertex, DefaultWeightedEdge> graph;
		private Map<Planet, PlanetVertex> planets;
		private final GalaxyType galaxyType;
		private List<Planet> startPoints;		
		private Map<String, Planet> planetsMap;

		public Builder(final GalaxyType galaxyType) {
			this.galaxyType = galaxyType;
			graph = new SimpleGraph<>(DefaultWeightedEdge.class);
			planets = new HashMap<>();
			startPoints = new LinkedList<>();
			planetsMap = new HashMap<>();
		}

		public Builder addEdge(Planet source, Planet target) {
			Planet s = planetsMap.get(source.getId());
			Planet t = planetsMap.get(target.getId());
			s.addNeighbour(t);
			t.addNeighbour(s);
			graph.addEdge(planets.get(s), planets.get(t));
			return this;
		}

		public Builder addPlanet(Planet planet, boolean isStartPoint) {
			Planet copy = PlanetCloner.lazyCopyPlanet(planet);
			planetsMap.put(copy.getId(), copy);
			
			if(isStartPoint) startPoints.add(copy);
			
			PlanetVertex vertex = new PlanetVertex(copy);
			planets.put(copy, vertex);
			graph.addVertex(vertex);
			return this;
		}

		public GalaxyEngine build() {
			return new GalaxyEngine(galaxyType, graph, planets, startPoints);
		}
	}

}
