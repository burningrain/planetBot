package starwors.model.lx.logic.strategies.tasks;

import starwors.model.lx.galaxy.Move;
import starwors.model.lx.galaxy.Planet;

import java.util.List;

public interface ITask {
	
	boolean canUse(Planet planet);
	
	List<Move> execute(Planet planet);

}
