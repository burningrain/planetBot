package com.github.br.starmarines.gamecore.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.exception.GameStepMistake;

@Component
public class GameExecutorManager {
	
	private GameContainer gameContainer;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private void setGameContainer(GameContainer gameContainer) {
		this.gameContainer = gameContainer;
	}

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1); // TODO число потоков должно настраиваться в конфигах
	private ConcurrentMap<Long, ScheduledFuture<?>> gameFutures = new ConcurrentHashMap<>();

	public void startGame(Long gameId, GameEventListener listener) {
		ScheduledFuture<?> future = executor.scheduleWithFixedDelay(new GameTask(gameId, gameContainer, this, listener), 0, 2, TimeUnit.SECONDS); //TODO периодичность тоже должна настраиваться
		gameFutures.put(gameId, future);
	}
	
	public void stopGame(Long gameId){
		ScheduledFuture<?> future = gameFutures.get(gameId);
		future.cancel(false);
		gameFutures.remove(gameId);
	}

	private static class GameTask implements Runnable {

		private final Long gameId;
		private final GameContainer gameContainer;
		private final GameExecutorManager gameExecutorManager;
		private final GameEventListener listener;

		public GameTask(final Long gameId, final GameContainer gameContainer, 
				final GameExecutorManager gameExecutorManager, final GameEventListener listener) {
			this.gameId = gameId;
			this.gameContainer = gameContainer;
			this.gameExecutorManager = gameExecutorManager;
			this.listener = listener;
		}

		@Override
		public void run() {			
			GameEventType eventType = GameEventType.GAME_UPDATE;
			GameStepResult result = gameContainer.computeSituation(gameId);
			if(result.isGameEnd()){
				eventType = GameEventType.GAME_END;
				gameExecutorManager.stopGame(gameId);
			}
			//ответ для игроков
			listener.eventPerformed(new GameEvent<GameStepResult>(eventType, result));
		}

	}

}
