package com.github.br.starmarines.gamecore.db.entities;

import java.util.Collections;
import java.util.Map;

import javax.persistence.Column;

public class User {
	
	@Column(name = "user_id")
	private long id;
	
	@Column(name = "login")
	private final String login;
	
	@Column(name = "password")
	private final String password;	
	
	private Map<GameEntity, UserGameStatus> statuses;		

	public User(String login, String password){
		this.login = login;
		this.password = password;
	}
	
	public User(long id, String login, String password, Map<GameEntity, UserGameStatus> statuses){
		this(login, password);
		this.id = id;		
		this.statuses = Collections.unmodifiableMap(statuses);
		
	}
	
	public long getId() {
		return id;
	}


	public String getLogin() {
		return login;
	}


	public String getPassword() {
		return password;
	}
	
	public Map<GameEntity, UserGameStatus> getStatuses() {
		return statuses;
	}	

}
