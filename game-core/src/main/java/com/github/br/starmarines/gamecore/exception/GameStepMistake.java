package com.github.br.starmarines.gamecore.exception;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.gamecore.model.Player;

public class GameStepMistake {

	private Player player;
	private Move move;
	private final String message;
	
	public GameStepMistake(String message, Player player, Move move){
		this.message = message;
		this.player = player;
		this.move = move;
	}

	public Player getPlayer() {
		return player;
	}

	public Move getMove() {
		return move;
	}

	public String getMessage() {
		return message;
	}	
	
}
