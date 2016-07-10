package strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.strategy.IStrategy;



@Component(service = IStrategy.class)
public class StrategyImpl3 implements IStrategy {

	@Override
	public String getTitle() {		
		return "Unpredictable2";
	}
	
	private Planet basePlanet;
	
	private static Random random = new Random();
	
	private static int getRandom(int min, int max){
		return random.nextInt(max - min + 1) + min;
	}
	
	

	@Override
	public Collection<Move> step(Collection<Planet> galaxy) {
		
		int rnd = getRandom(0, 1);
		
		return (rnd == 0)? ordinaryStep(galaxy) : nonOrdinaryStep(galaxy);
	}
	
	
	private Collection<Move> nonOrdinaryStep(Collection<Planet> galaxy) {
		List<Planet> myPlanet = new ArrayList<Planet>();
		for(Planet planet : galaxy){
			if(getTitle().equals(planet.getOwner())){
				myPlanet.add(planet);
			}
		}
		int r = getRandom(0, myPlanet.size()-1);
		basePlanet = myPlanet.get(r);
		List<Planet> neighbours = basePlanet.getNeighbours();
		
		r = getRandom(0, neighbours.size()-1);
		Planet neigbour = neighbours.get(r);
		
		List<Move> moves = new LinkedList<Move>();
		int units = getRandom(0, basePlanet.getUnits());
		moves.add(new Move(basePlanet, neigbour, units));
		
		return moves;
		
	}



	private Collection<Move> ordinaryStep(Collection<Planet> galaxy){
		basePlanet = getBasePlanet(galaxy);
		return getMoves();
	}
	
	private Collection<Move> getMoves() {
		List<Planet> neighbours = basePlanet.getNeighbours();
		Collection<Move> moves = new LinkedList<>();
		for(Planet n : neighbours){
			if(basePlanet.getUnits() > 100) {
				moves.add(new Move(basePlanet, n, basePlanet.getUnits()/8));
			}			
		}
		return moves;
	}

	private Planet getBasePlanet(Collection<Planet> galaxy){
		for(Planet planet : galaxy){
			if(getTitle().equals(planet.getOwner())) {
				return planet;
			}
		}
		return null;
	}
	
	
	

}
