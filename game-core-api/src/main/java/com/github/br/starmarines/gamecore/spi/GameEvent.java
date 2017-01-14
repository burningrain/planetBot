package com.github.br.starmarines.gamecore.spi;


public class GameEvent {
	
	private final GameStepResult stepResult;
	private final GameStatus eventType;
	private final GameInfo info;
	
	public GameEvent(final GameStatus eventType, final GameInfo info, final GameStepResult stepResult){
		this.eventType = eventType;
		this.info = info;
		this.stepResult = stepResult;
	}
	
	public GameStepResult getStepResult(){
		return stepResult;
	}

	public GameStatus getEventType() {
		return eventType;
	}

	public GameInfo getInfo() {
		return info;
	}			

}
