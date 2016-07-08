package com.github.br.starmarines.gamecore.db.entities;

public enum GameStatus {
	
	WAITING_FOR_PLAYERS((byte)0),
	IN_PROCESS((byte)1),
	COMPLETED((byte)2);
	
	private final byte id;	

	private GameStatus(byte id){
		this.id = id;		
	}
	
	public byte getId() {
		return id;
	}

}
