package com.github.br.starmarines.gamecore.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.exception.GameStepMistake;

public class GameStepResult {

	private final Collection<Planet> galaxy;
	private final boolean gameEnd;
	private final Map<Player, List<GameStepMistake>> mistakes;
	private final int stepNumber;
	private final Collection<Player> players;
	
	public GameStepResult(final boolean gameEnd, 
							final Collection<Planet> galaxy,
							final Map<Player, List<GameStepMistake>> mistakes,
							final int stepNumber,
							Collection<Player> players){
		this.galaxy = galaxy;
		this.gameEnd = gameEnd;
		this.mistakes = mistakes;
		this.stepNumber = stepNumber;
		this.players = players;
	}

	public Collection<Planet> getGalaxy() {
		return Collections.unmodifiableCollection(galaxy);
	}

	public boolean isGameEnd() {
		return gameEnd;
	}

	public Map<Player, List<GameStepMistake>> getMistakes() {
		return Collections.unmodifiableMap(mistakes);
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public Collection<Player> getPlayers() {
		return players;
	}		
	
}
