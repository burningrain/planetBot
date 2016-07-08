package com.github.br.starmarines.gamecore.model;

import java.util.Collection;

import com.github.br.starmarines.game.api.galaxy.Move;

public class Moves {
	
	private Collection<Move> moves;
	private Player player;
	
	public Moves(Collection<Move> moves, Player player){
		this.moves = moves;
		this.player = player;
	}
	
	public Collection<Move> get(){
		return moves;
	}
	
	public Player getPlayer(){
		return player;
	}	

}
