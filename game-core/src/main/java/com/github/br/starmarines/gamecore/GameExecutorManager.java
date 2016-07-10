package com.github.br.starmarines.gamecore;

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

@Component(service=GameExecutorManager.class, immediate=true, enabled=true)
public class GameExecutorManager {
	
	private GameContainer gameContainer;

	@Reference(cardinality=ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private void setGameContainer(GameContainer gameContainer) {
		this.gameContainer = gameContainer;
	}	

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1); // TODO число потоков должно настраиваться в конфигах
	private ConcurrentMap<Long, ScheduledFuture<?>> gameFutures = new ConcurrentHashMap<>();
	
	
	public void startGame(Long gameId) {
		if(gameFutures.get(gameId) == null){
			gameContainer.initGame(gameId);
			ScheduledFuture<?> future = executor.scheduleWithFixedDelay(new GameTask(gameId, gameContainer, this), 0, 2, TimeUnit.SECONDS); //TODO периодичность тоже должна настраиваться
			gameFutures.put(gameId, future);
		} else{
			//TODO выкинуть исключение игра уже начата
		}
		
	}
	
	public void stopGame(Long gameId){
		ScheduledFuture<?> future = gameFutures.get(gameId);
		if(future != null){
			future.cancel(false);
			gameFutures.remove(gameId);
		} else{
			//TODO выкинуть исключение, что игра с таким id не найдена
		}		
	}
	

	private static class GameTask implements Runnable {

		private final Long gameId;
		private final GameContainer gameContainer;
		private final GameExecutorManager gameExecutorManager;

		public GameTask(final Long gameId, final GameContainer gameContainer, 
				final GameExecutorManager gameExecutorManager) {
			this.gameId = gameId;
			this.gameContainer = gameContainer;
			this.gameExecutorManager = gameExecutorManager;
		}

		@Override
		public void run() {				
			boolean isGameEnd = gameContainer.computeSituation(gameId); 
			if(isGameEnd){
				gameExecutorManager.stopGame(gameId);
			}			
		}

	}

}
