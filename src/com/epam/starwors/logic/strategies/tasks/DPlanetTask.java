package com.epam.starwors.logic.strategies.tasks;

import java.util.LinkedList;
import java.util.List;

import com.epam.starwors.galaxy.Move;
import com.epam.starwors.galaxy.Planet;
import com.epam.starwors.galaxy.PlanetType;
import com.epam.starwors.logic.utils.PlanetUtils;

public class DPlanetTask implements ITask {

	@Override
	public boolean canUse(Planet planet) {
		return PlanetUtils.isMyPlanet(planet) && PlanetType.TYPE_D.equals(planet.getType());
	}

	@Override
	public List<Move> execute(Planet planet) {
		List<Move> moves = new LinkedList<Move>();
		int units = planet.getUnits();
		
		if(units < PlanetType.TYPE_C.getLimit() + 100){
			return new LinkedList<Move>();
		}		
		
		List<Planet> planetsC = new LinkedList<Planet>();
		
		List<Planet> neighbours = planet.getNeighbours();
		for(Planet neighbour : neighbours){	
			if(PlanetType.TYPE_C.equals(neighbour.getType())){
				planetsC.add(neighbour);				
			}
		}
		
		for(Planet pl : planetsC){
            int cook = Math.round(PlanetType.TYPE_C.getLimit()/planetsC.size());
            //pl.setUnits(pl.getUnits() + cook);
			moves.add(new Move(planet, pl, cook));
		}
		
		return moves;		
		
	}

}
