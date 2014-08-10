package starwors.model.lx.logic.strategies;

import starwors.model.lx.galaxy.Move;
import starwors.model.lx.galaxy.Planet;

import java.util.Collection;
import java.util.List;

public interface IStrategy {
	
	List<Move> execute(Collection<Planet> galaxy);

}
