package com.github.br.starmarines.gamecore.db.entities;

import javax.persistence.Column;

public class UserStep {
	
	@Column(name = "step_id")
	private final long id;
	
	@Column(name = "user_id")
	private final long userId;
	
	@Column(name = "from")
	private final int from;
	
	@Column(name = "to")
	private final int to;
	
	@Column(name = "amount")
	private final int amount;	
	

	public UserStep(long id, long userId, int from, int to, int amount){
		this.id = id;
		this.userId = userId;
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	
	
	public long getId() {
		return id;
	}


	public long getUserId() {
		return userId;
	}


	public int getFrom() {
		return from;
	}


	public int getTo() {
		return to;
	}


	public int getAmount() {
		return amount;
	}

}
