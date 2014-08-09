package main.starwors.logic.strategies;

import java.util.Collection;
import java.util.List;

import main.starwors.galaxy.Move;
import main.starwors.galaxy.Planet;

public interface IStrategy {
	
	List<Move> execute(Collection<Planet> galaxy);

}
