package com.github.br.starmarines.gamecore.services;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.gamecore.model.GameContainer;
import com.github.br.starmarines.gamecore.model.Player;


@Component
public class GameService {
	
	private GameContainer gameContainer;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private void setGameContainer(GameContainer gameContainer){
		this.gameContainer = gameContainer;
	}
	

	public void step(Long gameId, Player player, Collection<Move> moves){
		gameContainer.putMoves(gameId, player, moves);
	}
	
	
	
	
	
}
