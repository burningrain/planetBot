package com.github.br.starmarines.gamecore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.Player;

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
	
	/**
	 * возвращает карту "планета, откуда улетают юниты-суммарное число юнитов, которое улетает"  
	 */
	public Map<Planet, Integer> getComputeFromPlanets(){
		HashMap<Planet, Integer> planets = new HashMap<>();
		for(Move move : moves){
			Planet from = move.getFrom();
			int value = 0;
			if(planets.containsKey(from)){
				value = planets.get(from) + move.getAmount();				
			} else{
				value = move.getAmount();
			}
			planets.put(from, value);
		}
		return planets;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
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
		Moves other = (Moves) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	
	

}
