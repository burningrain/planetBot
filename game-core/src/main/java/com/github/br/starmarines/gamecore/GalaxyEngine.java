package com.github.br.starmarines.gamecore;

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
		if(owners.size() > startPoints.size()) throw new IllegalStateException("РЎС‚Р°СЂС‚РѕРІС‹С… С‚РѕС‡РµРє РјРµРЅСЊС€Рµ, С‡РµРј РёРіСЂРѕРєРѕРІ");
		
		Collections.shuffle(startPoints);
		Iterator<Planet> it = startPoints.iterator();
		for(String owner : owners){
			Planet planet = it.next();
			planet.setOwner(owner);
		}
	}
	
	
	// TODO РјСѓС‚Р°Р±РµР»СЊРЅС‹Р№ РјРµС‚РѕРґ
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
					mistakesList.add(new GameStepMistake("РњРµР¶РґСѓ РїР»Р°РЅРµС‚Р°РјРё РЅРµС‚ СЃРІСЏР·Рё", player, move));
					continue;
				}	
				
				// Р±СЂР°С‚СЊ СЋРЅРёС‚РѕРІ РјРѕР¶РЅРѕ С‚РѕР»СЊРєРѕ СЃРѕ СЃРІРѕРµР№ РїР»Р°РЅРµС‚С‹
				String owner = move.getFrom().getOwner();
				if(!player.getName().equals(owner)){
					mistakesList.add(new GameStepMistake("Р‘СЂР°С‚СЊ СЋРЅРёС‚РѕРІ РјРѕР¶РЅРѕ С‚РѕР»СЊРєРѕ СЃРѕ СЃРІРѕРµР№ РїР»Р°РЅРµС‚С‹", player, move));
					continue;
				}	
				
				if(!sourceVertex.canMoveUnits(move.getAmount())){
					mistakesList.add(new GameStepMistake("РќРµР»СЊР·СЏ Р±СЂР°С‚СЊ СЃ РїР»Р°РЅРµС‚С‹ СЋРЅРёС‚РѕРІ Р±РѕР»СЊС€Рµ, С‡РµРј РµСЃС‚СЊ", player, move));
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
		 * Р’С‹С‡РёСЃР»СЏРµС‚СЃСЏ РєРѕР»РёС‡РµСЃС‚РІРѕ СЋРЅРёС‚РѕРІ С…РѕР·СЏРёРЅР° РїР»Р°РЅРµС‚С‹, СЃ СѓС‡РµС‚РѕРј СѓР»РµС‚РµРІС€РёС… СЃ СЌС‚РѕР№ РїР»Р°РЅРµС‚С‹ 
		 * Рё РїСЂРёР»РµС‚РµРІС€РёС… РЅР° РЅРµРµ СЋРЅРёС‚РѕРІ СЌС‚РѕРіРѕ РёРіСЂРѕРєР°.
		 */
		public void computePlanetsUnitsAmount(){
			for(Step step : ownerSteps){
				int units = step.getUnits();
				int result = planet.getUnits() + units; // units РјРѕРіСѓС‚ Р±С‹С‚СЊ Рё "+" Рё "-"
				if(result < 0) result = 0;
				planet.setUnits(result);
			}
			ownerSteps.clear();
		}
		
		/**
		 * РџРѕР»СѓС‡РёРІС€РµРµСЃСЏ РєРѕР»РёС‡РµСЃС‚РІРѕ СѓРІРµР»РёС‡РёРІР°РµС‚СЃСЏ РїРѕ РїСЂР°РІРёР»Р°Рј СЂРµРіРµРЅРµСЂР°С†РёРё СЋРЅРёС‚РѕРІ РґР»СЏ СЌС‚РѕР№ РїР»Р°РЅРµС‚С‹. 
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
		 * РџРѕСЃР»Рµ СЌС‚РѕРіРѕ РїСЂРѕРёСЃС…РѕРґРёС‚ РІС‹Р±РѕСЂ РЅРѕРІРѕРіРѕ С…РѕР·СЏРёРЅР° РїР»Р°РЅРµС‚С‹ (РІ СЃР»СѓС‡Р°Рµ, РµСЃР»Рё РЅР° РїР»Р°РЅРµС‚Сѓ Р±С‹Р»Рё РїРѕСЃР»Р°РЅС‹ С‡СЊРё-С‚Рѕ РµС‰Рµ СЋРЅРёС‚С‹). 
		 * РҐРѕР·СЏРёРЅРѕРј РїР»Р°РЅРµС‚С‹ СЃС‚Р°РЅРѕРІРёС‚СЃСЏ РёРіСЂРѕРє СЃ РЅР°РёР±РѕР»СЊС€РёРј РєРѕР»РёС‡РµСЃС‚РІРѕРј СЋРЅРёС‚РѕРІ. РџСЂРё СЌС‚РѕРј СЋРЅРёС‚С‹ РІСЃРµС… РѕСЃС‚Р°Р»СЊРЅС‹С… РёРіСЂРѕРєРѕРІ 
		 * СѓРЅРёС‡С‚РѕР¶Р°СЋС‚СЃСЏ, Р° Сѓ РїРѕР±РµРґРёС‚РµР»СЏ РѕСЃС‚Р°РµС‚СЃСЏ С‡РёСЃР»Рѕ СЋРЅРёС‚РѕРІ, РЅР° РєРѕС‚РѕСЂРѕРµ РѕРЅ РїСЂРµРІРѕСЃС…РѕРґРёР» СЃР»РµРґСѓСЋС‰РµРіРѕ РїРѕ С‡РёСЃР»РµРЅРЅРѕСЃС‚Рё 
		 * РёРіСЂРѕРєР°. 
		 */
		public void choosePlanetsMasters(){
			// РµСЃР»Рё РІСЂР°РіРё РЅРµ РїРѕСЃР»Р°Р»Рё РЅР° РїР»Р°РЅРµС‚Сѓ СЋРЅРёС‚РѕРІ, СЃС‡РёС‚Р°С‚СЊ РЅРµС‡РµРіРѕ
			if(enemySteps.size() == 0){
				return; 
			}			
			
			enemySteps.add(new Step(new Player(planet.getOwner()), planet.getUnits())); //TODO РєСЂРёРІРѕС‚Р°, СѓР±СЂР°С‚СЊ
			// С‚РѕР»СЊРєРѕ "+" РјРѕР¶РµС‚ Р±С‹С‚СЊ Сѓ РІСЂР°Р¶РµСЃРєРёС… С…РѕРґРѕРІ;
			// СЃРѕСЂС‚РёСЂСѓРµРј РїРѕ РІРѕР·СЂР°СЃС‚Р°РЅРёСЋ
			enemySteps.sort(new Comparator<Step>() {
				@Override
				public int compare(Step o1, Step o2) {					
					return o1.getUnits() - o2.getUnits();
				}
			});
			Step first = enemySteps.get(enemySteps.size() - 1);
			Step second = enemySteps.get(enemySteps.size() - 2);
			
			int result = first.getUnits() - second.getUnits();
			if(result > 0){
				// РїР»Р°РЅРµС‚Сѓ Р·Р°С…РІР°С‚РёР»Рё РёР»Рё С…РѕР·СЏРёРЅ РѕС‚Р±РёР»СЃСЏ
				result = Math.abs(result);
				planet.setUnits(result);
				planet.setOwner(first.getPlayer().getName());
			} else if(result == 0){
				// РЅРµ Р·Р°С…РІР°С‚РёР»Рё, РЅРѕ РІСЃРµС… СѓР±РёР»Рё
				planet.setUnits(result);
				planet.setOwner("");
			} else{ 
				// РІС‚РѕСЂРѕР№ РїРѕ С‡РёСЃР»Сѓ СЋРЅРёС‚РѕРІ РѕР±РѕС€РµР» РїРµСЂРІРѕРіРѕ. РљР°Рє С‚Р°РєРѕРµ СЃР»СѓС‡РёР»РѕСЃСЊ?
				throw new IllegalStateException("РћС€РёР±РєР° РїСЂРё РїРѕРґСЃС‡РµС‚Рµ С…РѕР·СЏРёРЅР° РїР»Р°РЅРµС‚С‹");
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
