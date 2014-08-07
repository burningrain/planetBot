package com.epam.starwors.logic.strategies.tasks;

import java.util.List;

import com.epam.starwors.galaxy.Move;
import com.epam.starwors.galaxy.Planet;

public interface ITask {
	
	boolean canUse(Planet planet);
	
	List<Move> execute(Planet planet);

}
