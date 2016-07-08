package com.github.br.starmarines.gamecore.db.entities;

import java.util.Collections;
import java.util.Set;

public class GameEntity {
	
	private Long id;
	private final String title;
	private final GameStatus status;
	
	private final Set<User> users;			
	private final Set<Planet> planets;	
	
	
	public GameEntity(Long id, String title, GameStatus status, Set<User> users, Set<Planet> planets){
		this.id = id;
		this.title = title;
		this.status = status;
		this.users = (users != null)? Collections.unmodifiableSet(users) : null;
		this.planets = (planets != null)? Collections.unmodifiableSet(planets) : null;
	}
	
	public GameEntity(String title, GameStatus status, Set<User> users, Set<Planet> planets){
		this(null, title, status, users, planets);			
	}
	
	public GameEntity(String title, GameStatus status){
		this(title, status, null, null);
	}
	
	public GameEntity(String title){
		this(title, GameStatus.WAITING_FOR_PLAYERS);
	}	
	
	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public GameStatus getStatus() {
		return status;
	}

	public Set<User> getUsers() {
		return users;
	}

	public Set<Planet> getPlanets() {
		return planets;
	}	

}
