package com.github.br.starmarines.gamecore.api;

import java.util.Collection;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.gamecore.spi.IGameEventListener;

public interface IGameService {

	void createGame(String title, 
					int playersCount, 
					Integer maxGameStepAmount, 
					GalaxyType type, 
					IGameEventListener lisneter);
	
	void addPlayerToGame(final Long gameId, Player player);
	void startGame(Long gameId);
	void stopGame(Long gameId);
	void step(Long gameId, Player player, Collection<Move> moves);
	
	
	
}
