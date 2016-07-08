package com.github.br.starmarines.gamecore.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.exception.GameStepMistake;

class Game {

	private final Long id;
	private final int playersCount; // на сколько игроков создана игра
	private final String title;
	private final Set<Player> players;
	private final Galaxy galaxy; // FIXME мутабельная структура
	private final Integer maxStepAmount; // если null, то число шагов неограничено
	
	private ArrayBlockingQueue<Moves> playersMoves;

	public Game(final Long id, final String title, final int playersCount, final Galaxy galaxy, Integer maxStepAmount) {
		if(playersCount <=1) throw new IllegalStateException("Число игроков должно быть больше 1");
		if(maxStepAmount != null && maxStepAmount <=1) throw new IllegalStateException("Макс. число шагов должно быть больше 1");
		
		this.id = id;
		this.title = title;
		this.playersCount = playersCount;
		this.galaxy = galaxy; 		
		this.maxStepAmount = maxStepAmount;
		
		players = new ConcurrentSkipListSet<>();
		playersMoves = new ArrayBlockingQueue<>(playersCount);
	}

	// мутабельный потокобезопасный метод
	void addPlayer(Player player) {
		players.add(player);
	}
	
	// мутабельный потокобезопасный метод
	void addMoves(Player player, Collection<Move> moves){
		playersMoves.add(new Moves(moves, player));
	}
	
	// мутабельный потоконебезопасный метод
	GameStepResult computeSituation(){		
		Map<Player, List<GameStepMistake>> mistakes = new HashMap<>();
		Collection<Planet> result = galaxy.updateState(playersMoves, mistakes);			
		boolean gameEnd = isGameEnd(result);
		gameEnd = gameEnd || (maxStepAmount != null && galaxy.getYear() >= maxStepAmount);
		GameStepResult gameStep = new GameStepResult(gameEnd, result, mistakes, galaxy.getYear(),  getPlayers());
		return gameStep;
	}

	public Set<Player> getPlayers() {
		return Collections.unmodifiableSet(players);
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getPlayersCount() {
		return playersCount;
	}	

	public Integer getMaxStepAmount() {
		return maxStepAmount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (playersCount != other.playersCount)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
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
