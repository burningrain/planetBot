package com.github.br.starmarines.gamecore;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.gamecore.api.GalaxyType;
import com.github.br.starmarines.gamecore.api.IGameService;
import com.github.br.starmarines.gamecore.api.Player;
import com.github.br.starmarines.gamecore.spi.IGameEventListener;


@Component(service=IGameService.class, immediate=true, enabled=true)
public class GameServiceImpl implements IGameService {
	
	private GameContainer gameContainer;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private void setGameContainer(GameContainer gameContainer){
		this.gameContainer = gameContainer;
	}
	
	private GameExecutorManager gameExecutor;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private void setGameContainer(GameExecutorManager gameExecutor){
		this.gameExecutor = gameExecutor;
	}
	
	//TODO для галактики сделать фабрику, а здесь принимать именно тип из определенного в фабрике
	public void createGame(String title, int playersCount, Integer maxGameStepAmount, 
			GalaxyType type, IGameEventListener lisneter){
		Galaxy galaxy = GalaxyFactory.getGalaxy(playersCount, type);
		gameContainer.createGame(title, playersCount, galaxy, maxGameStepAmount, lisneter);
	}
	
	public void addPlayerToGame(final Long gameId, Player player){
		gameContainer.addPlayerToGame(gameId, player);
	}	
	
	public void startGame(Long gameId){		
		gameExecutor.startGame(gameId);	
	}
	
	public void stopGame(Long gameId){
		gameExecutor.stopGame(gameId);
	}		

	public void step(Long gameId, Player player, Collection<Move> moves){
		gameContainer.putMoves(gameId, player, moves);
	}	
	
}
