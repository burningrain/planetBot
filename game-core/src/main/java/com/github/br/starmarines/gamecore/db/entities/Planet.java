package com.github.br.starmarines.gamecore.db.entities;

import javax.persistence.Column;

public class Planet {
	
	@Column(name = "planet_id")
	private final long id;
	
	@Column(name = "units")
	private final int units;
	
	@Column(name = "max_units")
	private final int maxUnits;
	
	@Column(name = "owner_id")
	private final long ownerId;
	
	@Column(name = "game_id")
	private final long gameId;	
	

	public Planet(long id, long ownerId, int units, int maxUnits, long gameId){
		this.id = id;
		this.ownerId = ownerId;
		this.units = units;
		this.maxUnits = maxUnits;
		this.gameId = gameId;
	}
	
	public long getId() {
		return id;
	}

	public int getUnits() {
		return units;
	}

	public int getMaxUnits() {
		return maxUnits;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public long getGameId() {
		return gameId;
	}

}
