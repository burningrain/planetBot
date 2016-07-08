package com.github.br.starmarines.gamecore.db.entities;

public enum UserGameStatus {	
	
	DEFEAT((byte)0),
	DRAW((byte)1),
	VICTORY((byte)2),
	
	IN_PROCESS((byte)3)
	;
	
	private final byte status;	

	private UserGameStatus(byte status) {
		this.status = status;
	}
	
	public byte getStatus() {
		return status;
	}
	
}
