package com.github.br.starmarines.main.model.logic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.starwors.lx.galaxy.Action;
import com.br.starwors.lx.logic.utils.CoreUtils;
import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.api.IGameService;
import com.github.br.starmarines.gamecore.api.Player;
import com.github.br.starmarines.gamecore.spi.GameEvent;
import com.github.br.starmarines.gamecore.spi.GameInfo;
import com.github.br.starmarines.gamecore.spi.GameStatus;
import com.github.br.starmarines.gamecore.spi.GameStepResult;
import com.github.br.starmarines.gamecore.spi.IGameEventListener;
import com.github.br.starmarines.main.model.objects.inner.IGameInfo;
import com.github.br.starmarines.main.model.objects.inner.IStepInfo;
import com.github.br.starmarines.main.model.objects.inner.impl.StepInfo;
import com.github.br.starmarines.main.model.services.inner.beans.Response;
import com.github.br.starmarines.main.model.services.remote.IStrategyService;

@Component
@Instantiate
@Provides
public class GameEngineLogic {
	
	@Requires(optional = true)
	private LogService logService;

	@Requires
	private IGameService gameService;
	
	@Requires
    private IStrategyService strategyService;
	
	@Requires(proxy = false, policy = BindingPolicy.STATIC)
    private IGameInfo gameInfo;  //TODO подумать о многопоточке
	
	@Requires(proxy = false, policy = BindingPolicy.STATIC)
    private IStepInfo step;  //TODO подумать о многопоточке 
	
	
	private GameEventListener listener;	
	private Long gameId;
	
	@Validate
	private void validate(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " start");
		listener = new GameEventListener(gameService, strategyService, gameInfo, step);
	}
	
	public void startGame(Galaxy galaxy){
		if(gameId != null) return;
	
		gameId = gameService.createGame("test", 2, null, galaxy, listener);
		Set<String> strategies = strategyService.getStrategies();
		Set<Player> players = new HashSet<>();
		for(String strategy : strategies){
			players.add(new Player(strategy));
		}
		
		for(Player player : players){
			gameService.addPlayerToGame(gameId, player);
		}		
		
	}
	
	public void stopGame(){
		gameService.stopGame(gameId);
		gameId = null;
	}
	
	private static class GameEventListener implements IGameEventListener{
		
		private IGameService gameService;
		private IStrategyService strategyService;
		
		private IGameInfo gameInfo;
		private IStepInfo step;
		
		public GameEventListener(IGameService gameService, IStrategyService strategyService, 
				IGameInfo gameInfo, IStepInfo step) {
			this.gameService = gameService;
			this.strategyService = strategyService;
			
			this.gameInfo = gameInfo;
			this.step = step;
		}

		@Override
		public void eventPerformed(GameEvent event) {
			GameStatus status = event.getEventType();
			System.out.println(status);	
			GameInfo info = event.getInfo();
			if(GameStatus.ALL_PLAYERS_RECRUITED.equals(status)){
				gameService.startGame(info.getId());
			}
			
			//TODO не многопоточно, ну да хер с ним, все надо переписывать во всем модуле. Кругом говно!
			if(GameStatus.WAITING_PLAYERS_STEPS.equals(status)){
				GameStepResult stepResult = event.getStepResult();
				Collection<Player> players = stepResult.getPlayers();
								
				gameInfo.updateGalaxy(stepResult.getGalaxy());
				Response reponse = new Response();
				for(Planet planet : stepResult.getGalaxy()){
					reponse.addPlanet(planet);
				}
				step.updateResponse(reponse);
				Collection<Action> actions = null;
				for(Player player : players){
					strategyService.setCurrentStrategy(player.getName());
					Collection<Move> moves = strategyService.step(stepResult.getGalaxy());
					gameService.step(info.getId(), player, moves);
					
					actions = CoreUtils.toActionList(moves);
					System.out.println("Step, player = " + player.getName() + " moves=" + actions);
				}
				step.updateCurrentActions(actions);
				
				//replayWriter.writeReplay(content, actions); // записываем текущую ситуацию и наш следующий ход для реплея
			}
		}
		
	}
	
	
	
	
	

}
