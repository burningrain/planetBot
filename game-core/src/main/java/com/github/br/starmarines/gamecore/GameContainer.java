package com.github.br.starmarines.gamecore;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.osgi.service.component.annotations.Component;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.gamecore.api.Player;
import com.github.br.starmarines.gamecore.spi.IGameEventListener;


@Component(service=GameContainer.class, immediate=true, enabled=true)
public class GameContainer {

	private AtomicLong gameSequence = new AtomicLong();	
	private ConcurrentHashMap<Long, Game> games = new ConcurrentHashMap<>(); 		
	
	
	public Long createGame(String title, int playersCount, Galaxy galaxy, Integer maxGameStepAmount,
			IGameEventListener lisneter){
		Game game = new Game(gameSequence.incrementAndGet(), title, playersCount, galaxy, maxGameStepAmount, lisneter);
		games.put(game.getId(), game);				
		return game.getId();
	}	
	
	
	public void addPlayerToGame(Long gameId, Player player){
		Game game = games.get(gameId);
		game.addPlayer(player);
	}		
	
	
	public void putMoves(Long gameId, Player player, Collection<Move> moves){
		Game game = games.get(gameId);
		game.addMoves(player, moves);
	}
	
	
	void initGame(Long gameId){
		Game game = games.get(gameId);
		game.init();
	}
	
	boolean computeSituation(Long gameId){
		return games.get(gameId).computeSituation();
	}

}
