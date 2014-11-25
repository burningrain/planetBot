package starwors.model.lx.bot;

import starwors.model.lx.galaxy.Move;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;
import starwors.model.lx.logic.utils.Attack;
import starwors.model.lx.logic.utils.AttackCriteria;
import starwors.model.lx.logic.utils.PlanetCloner;
import starwors.model.lx.logic.utils.PlanetUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Logic {
	
	public Logic() {
	}
	
	public Collection<Move> step(Collection<Planet> galaxy) {
		Attack attack = new Attack(new AttackCriteria() {
			@Override
			public int giveUnits(Planet from, Planet to) {
				int units = from.getUnits();
				if(PlanetType.TYPE_D.equals(from.getType())){
					if(!PlanetUtils.isMyPlanet(to)){
						return Double.valueOf(Math.ceil(units)).intValue();
					} else{
						return Double.valueOf(Math.ceil(units)/5).intValue();
					}

				} else if(PlanetType.TYPE_C.equals(from.getType())){
					if(!PlanetUtils.isMyPlanet(to)){
						return Double.valueOf(Math.ceil(units)).intValue();
					} else{
						return Double.valueOf(Math.ceil(units)/4).intValue();
					}

				} else{
					if(!PlanetUtils.isMyPlanet(to)){
						return Double.valueOf(Math.ceil(units)/1.3).intValue();
					} else{
						return Double.valueOf(Math.ceil(units)/5).intValue();
					}

				}
			}
		});



		try{
			List<Move> moves = attack.attack(getTargetPlanet(PlanetCloner.clonePlanets(galaxy)));
			return moves;

		} catch (Exception e){
			return new LinkedList<Move>();
		}
	}

	private Planet getTargetPlanet(Collection<Planet> galaxy) {
		List<Planet> myPlanets = new LinkedList<Planet>();
		for (Planet planet : galaxy) {
			if (PlanetUtils.isMyPlanet(planet)) {
				myPlanets.add(planet);
			}
		}

		//List<Planet> othersPlanet = new LinkedList<Planet>();
		for (Planet myPlanet : myPlanets) {
			for (Planet neighbour : myPlanet.getNeighbours()) {
				if (!PlanetUtils.isMyPlanet(neighbour)) {
					return neighbour;
				}
			}
		}
		return null;
	}

}
