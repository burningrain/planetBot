package com.github.br.starmarines.main.model.services.remote;

import java.util.Collection;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;


public interface IStrategyService {
	
	Collection<Move> step(Collection<Planet> galaxy);
	
	void setCurrentStrategy(String title);

}
