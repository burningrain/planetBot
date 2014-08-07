package com.epam.starwors.logic.strategies;

import java.util.Collection;
import java.util.List;

import com.epam.starwors.galaxy.Move;
import com.epam.starwors.galaxy.Planet;

public interface IStrategy {
	
	List<Move> execute(Collection<Planet> galaxy);

}
