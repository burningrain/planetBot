package com.github.br.starmarines.game.api.strategy;

import java.util.Collection;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;

public interface IStrategy {
	
	
	String getTitle();
	
	Collection<Move> step(Collection<Planet> galaxy);

}
