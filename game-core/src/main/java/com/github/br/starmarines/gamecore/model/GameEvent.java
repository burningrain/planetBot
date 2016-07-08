package com.github.br.starmarines.gamecore.model;

public class GameEvent<T> {
	
	private final T target;
	private final GameEventType eventType;
	
	public GameEvent(final GameEventType eventType, final T target){
		this.eventType = eventType;
		this.target = target;
	}
	
	public T getTarget(){
		return target;
	}

	public GameEventType getEventType() {
		return eventType;
	}		

}
