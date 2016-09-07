package com.github.br.starmarines.gamecore;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.api.Player;
import com.github.br.starmarines.gamecore.mistakes.GameStepMistake;
import com.github.br.starmarines.gamecore.spi.GameEvent;
import com.github.br.starmarines.gamecore.spi.IGameEventListener;
import com.github.br.starmarines.gamecore.spi.GameInfo;
import com.github.br.starmarines.gamecore.spi.GameStatus;
import com.github.br.starmarines.gamecore.spi.GameStepResult;

class Game {
	
	private final GameInfo info;        // хранит метаиформацию об игре
	private volatile GameStatus status; // один писатель, N читателей. Потокобезопасно.

	private final Set<Player> players;
	private AtomicInteger currentPlayersCount = new AtomicInteger(0);
	private GalaxyEngine galaxy;  // мутабельная структура
	
	private ArrayBlockingQueue<Moves> playersMoves;	
	private IGameEventListener listener;

	public Game(final Long id, 
			final String title, 
			final int playersCount, 			 
			final Galaxy galaxy, 
			final Integer maxStepAmount, 
			IGameEventListener listener) {
		if(playersCount <=1) throw new IllegalStateException("Число игроков должно быть больше 1");
		if(maxStepAmount != null && maxStepAmount <=1) throw new IllegalStateException("Макс. число шагов должно быть больше 1");
		
		info = new GameInfo(id, title, playersCount, maxStepAmount, galaxy.getGalaxyType());		
		fillGalaxyEngine(galaxy);				
		players = new ConcurrentSkipListSet<>();
		playersMoves = new ArrayBlockingQueue<>(playersCount);
		
		this.listener = listener;
		changeStatus(GameStatus.CREATED, null);
	}
	
	//TODO уродливая штука, переделать
	private void fillGalaxyEngine(Galaxy galaxy){
		GalaxyEngine.Builder builder = new GalaxyEngine.Builder(galaxy.getGalaxyType());
		Set<Planet> startPoints = galaxy.getStartPoints();
		Set<Planet> planets = galaxy.getPlanets();	
		
		for(Planet planet : planets){
			boolean isStartPoint = startPoints.contains(planet);
			builder.addPlanet(planet, isStartPoint);
		}		
		
		for(Planet planet : planets){
			List<Planet> neighbours = planet.getNeighbours();
			for(Planet neigbour : neighbours){
				builder.addEdge(planet, neigbour);
			}
		}
		
		this.galaxy = builder.build();
	}
	
	/**
	 * Перед стартом игры необходимы дополнительные приготовления, такие дела
	 */
	public void init(){
		// расставляем игроков по начальным позициям
		Set<String> owners = new HashSet<>();
		for(Player player : players){
			owners.add(player.getName());
		}
		galaxy.initPlayersPositions(owners);		
	}

	// мутабельный потокобезопасный??? метод
	void addPlayer(Player player) {
		if(status.equals(GameStatus.CREATED)){
			int count = currentPlayersCount.incrementAndGet();
			if(count == info.getPlayersCount()){
				players.add(player);
				changeStatus(GameStatus.ALL_PLAYERS_RECRUITED, null);				
			} else if(count <= info.getPlayersCount()){
				players.add(player);
			} else{
				//TODO выкидывать оишбку, что игроки набраны и подсоединиться нельзя
			}
		} else{
			//TODO выкидывать оишбку, что игроки набраны и подсоединиться нельзя
		}		
	}
	
	// мутабельный потокобезопасный??? метод
	void addMoves(Player player, Collection<Move> moves){
		if(status.equals(GameStatus.WAITING_PLAYERS_STEPS)){
			if(playersMoves.contains(moves)){
				//TODO выкинуть ошибку, что игрок уже сходил
			} else{
				playersMoves.add(new Moves(moves, player));
			}		
		} else{
			//TODO выкинуть ошибку, что игра обсчитывает состояние
		}
	}
	
	/**
	 * мутабельный метод. Исполняется всегда в одном потоке
	 * @return true если игра окончена, иначе false
	 */
	boolean computeSituation(){
		changeStatus(GameStatus.COMPUTE_STEP, null);
		
		Map<Player, List<GameStepMistake>> mistakes = new HashMap<>();
		Collection<Planet> result = galaxy.updateState(playersMoves, mistakes);			
		boolean gameEnd = isGameEnd(result);
		Integer maxStepAmount = info.getMaxStepAmount();
		gameEnd = gameEnd || (maxStepAmount != null && galaxy.getYear() >= maxStepAmount);
		if(gameEnd) status = GameStatus.FINISH;
		GameStepResult gameStep = new GameStepResult(gameEnd, result, mistakes, galaxy.getYear(),  getPlayers());
		
		playersMoves.clear();
		changeStatus(GameStatus.WAITING_PLAYERS_STEPS, gameStep);
		return gameEnd;
	}

	public Set<Player> getPlayers() {
		return Collections.unmodifiableSet(players);
	}
	
	public Long getId(){
		return info.getId();
	}	
	
	private void changeStatus(GameStatus newStatus, GameStepResult stepResult){
		status = newStatus;
		if(listener != null) listener.eventPerformed(new GameEvent(newStatus, info, stepResult));
		if(GameStatus.FINISH.equals(status)) listener = null;
	}

	
	// TODO  сделать класс "ресолвер" и перенести это в него
	// сделать дашбоард, который заполняется ресолвером, когда игра окончена
	private static boolean isGameEnd(Collection<Planet> result) {
		// если все планеты принадлежат одному игроку, то игра окончена
		String lastOwner = null;
		boolean gameEnd = true;
		for (Planet planet : result) {
			String owner = planet.getOwner();
			if (lastOwner != null && !owner.equals(lastOwner)) {
				gameEnd = false;
				break;
			}
			lastOwner = owner;
		}
		return gameEnd;
	}

}
