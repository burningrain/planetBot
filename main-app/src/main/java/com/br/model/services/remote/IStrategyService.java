package com.br.model.services.remote;

import java.util.Collection;

import com.br.game.api.galaxy.Move;
import com.br.game.api.galaxy.Planet;

public interface IStrategyService {
	
	Collection<Move> step(Collection<Planet> galaxy);
	
	void setCurrentStrategy(String title);

}
