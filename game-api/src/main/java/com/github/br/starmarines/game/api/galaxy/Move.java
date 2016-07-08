package com.github.br.starmarines.game.api.galaxy;

/**
 * Содержит информацию об одной команде на передвижение юнитов.
 */
public class Move {

	private final Planet from;
	private final Planet to;
	private final int amount;

	/*
	 * Создать новую команду
	 */
	public Move(Planet from, Planet to, int amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	/**
	 * Получить планету, с которой отправляются юниты
	 */
	public Planet getFrom() {
		return from;
	}

	/**
	 * Получить планету, на которую отправляются юниты
	 */
	public Planet getTo() {
		return to;
	}

	/**
	 * Получить количество отправляемых юнитов
	 */
	public int getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("from ").append(from.getId()).append(" to ")
		.append(to.getId()).append(" -- ").append(amount);
		
		return  sb.toString();
	}
	
}
