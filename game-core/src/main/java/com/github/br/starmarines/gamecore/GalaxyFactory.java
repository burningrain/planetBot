package com.github.br.starmarines.gamecore;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.api.GalaxyType;

public class GalaxyFactory {

	//TODO
	public static Galaxy getGalaxy(int playersCount, GalaxyType type) {
		Galaxy galaxy = null;
		switch (type) {
			default:
				galaxy = buildTest();
				break;
		}
		return galaxy; 
	}
	
	private static Galaxy buildTest(){
		Galaxy galaxy = null;
		Galaxy.Builder builder = new Galaxy.Builder(GalaxyType.BIG_BASES);
		
		Planet planet1 = getPlanet("1", PlanetType.TYPE_A, 0, "");
		Planet planet2 = getPlanet("2", PlanetType.TYPE_D, 500, "");
		Planet planet3 = getPlanet("3", PlanetType.TYPE_C, 0, "");
		Planet planet4 = getPlanet("4", PlanetType.TYPE_D, 500, "");
		Planet planet5 = getPlanet("5", PlanetType.TYPE_C, 0, "");
		builder.addPlanet(planet1, false).
				addPlanet(planet2, true).
				addPlanet(planet3, false).
				addPlanet(planet4, true).
				addPlanet(planet5, false);
		builder.addEdge(planet1, planet2).
				addEdge(planet1, planet3).
				addEdge(planet1, planet4).
				addEdge(planet1, planet5).
				addEdge(planet2, planet3).
				addEdge(planet2, planet5).
				addEdge(planet4, planet3).
				addEdge(planet4, planet5);
		galaxy = builder.build();
		
		return galaxy;
	}
	
	private static Planet getPlanet(String id, PlanetType type, int units, String owner){
		Planet planet = new Planet(id);
		planet.setType(type);
		planet.setUnits(units);
		planet.setOwner(owner);
		return planet;
	}

}
