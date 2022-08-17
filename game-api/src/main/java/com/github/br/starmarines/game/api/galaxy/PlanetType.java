package com.github.br.starmarines.game.api.galaxy;

/**
 * Тип планеты
 */
public enum PlanetType {
	TYPE_A((byte)0, 10, 100),
	TYPE_B((byte)1, 15, 200),
	TYPE_C((byte)2, 20, 500),
	TYPE_D((byte)3, 30, 1000);

	private final byte code;
	private final int increment;
	private final int limit;

	PlanetType(byte code, int increment, int limit) {
		if(increment < 0 || increment > 100) {
			throw new IllegalArgumentException("increment must be between 0 and 100 percents");
		}
		if(limit < 0) {
			throw new IllegalArgumentException("limit must be grower zero");
		}

		this.code = code;
		this.increment = increment;
		this.limit = limit;
	}

	public static PlanetType getByCode(byte b) {
		switch (b) {
			case 0: return TYPE_A;
			case 1: return TYPE_B;
			case 2: return TYPE_C;
			case 3: return TYPE_D;
			default: throw new IllegalArgumentException("type for code=[" + b + "] is not found");
		}
	}

	public byte getCode() {
		return code;
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
