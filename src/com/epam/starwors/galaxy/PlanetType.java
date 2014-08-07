/*
 * © EPAM Systems, 2012  
 */
package com.epam.starwors.galaxy;

/**
 * Тип планеты
 */
public enum PlanetType {
	TYPE_A(10, 100), TYPE_B(15, 200), TYPE_C(20, 500), TYPE_D(30, 1000);

	private final int increment;
	private final int limit;

	private PlanetType(int increment, int limit) {
		this.increment = increment;
		this.limit = limit;
	}

	/**
	 * Получить прирост юнитов на планете данного типа в процентах (например 10 %)
	 */
	public int getIncrement() {
		return increment;
	}

	/**
	 * Получить предел юнитов на планете данного типа. При достижении предела
	 * юниты на планете перестанут регенирировать (однако их можно отправить с
	 * другой планеты)
	 */
	public int getLimit() {
		return limit;
	}

}
