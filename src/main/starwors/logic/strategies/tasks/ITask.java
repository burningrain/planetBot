package main.starwors.logic.strategies.tasks;

import java.util.List;

import main.starwors.galaxy.Move;
import main.starwors.galaxy.Planet;

public interface ITask {
	
	boolean canUse(Planet planet);
	
	List<Move> execute(Planet planet);

}
