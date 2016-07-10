package com.github.br.starmarines.gamecore;

import java.util.Collection;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.gamecore.api.GalaxyType;
import com.github.br.starmarines.gamecore.api.IGameService;
import com.github.br.starmarines.gamecore.api.Player;
import com.github.br.starmarines.gamecore.spi.IGameEventListener;

@Provides
@Instantiate
@Component
public class GameServiceImpl implements IGameService {
	
	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Requires(policy=BindingPolicy.STATIC, proxy=false)
	private GameContainer gameContainer;	


	@Requires(policy=BindingPolicy.STATIC, proxy=false)
	private GameExecutorManager gameExecutor;
	

	
	//TODO для галактики сделать фабрику, а здесь принимать именно тип из определенного в фабрике
	public Long createGame(String title, int playersCount, Integer maxGameStepAmount, 
			GalaxyType type, IGameEventListener lisneter){
		Galaxy galaxy = GalaxyFactory.getGalaxy(playersCount, type);
		return gameContainer.createGame(title, playersCount, galaxy, maxGameStepAmount, lisneter);
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
