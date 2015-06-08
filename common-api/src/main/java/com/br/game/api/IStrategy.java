package com.br.game.api;

import java.util.Collection;

import com.br.game.api.galaxy.Move;
import com.br.game.api.galaxy.Planet;

public interface IStrategy {
	
	
	String getTitle();
	
	Collection<Move> step(Collection<Planet> galaxy);

}
