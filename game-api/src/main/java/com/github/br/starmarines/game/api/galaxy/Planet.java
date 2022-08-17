package com.github.br.starmarines.game.api.galaxy;

import java.util.Objects;

/**
 * Содержит информацию о планете
 */
public class Planet {

	public static final short EMPTY_OWNER = -1;

	// координаты
	private float x;
	private float y;

	private short id;
	private short ownerId = EMPTY_OWNER;
	private int units;
	private PlanetType type;

	/**
	 * Создать планету с заданным id
	 */
	public void setId(short id) {
		this.id = id;
	}

	/**
	 * Получить id планеты
	 */
	public short getId() {
		return id;
	}

	/**
	 * Получить владельца планеты
	 */
	public short getOwnerId() {
		return ownerId;
	}

	/**
	 * Задать владельца планеты
	 */
	public void setOwnerId(short ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * Получить количество юнитов на планете
	 */
	public int getUnits() {
		return units;
	}

	/**
	 * Задать количество юнитов на планете
	 */
	public void setUnits(int units) {
		this.units = units;
	}

	/**
	 * Получить тип планеты
	 */
	public PlanetType getType() {
		return type;
	}

	/**
	 * Задать тип планеты
	 */
	public void setType(PlanetType type) {
		this.type = type;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Planet{" +
				   "x=" + x +
				   ", y=" + y +
				   ", id='" + id + '\'' +
				   ", owner='" + ownerId + '\'' +
				   ", units=" + units +
				   ", type=" + type +
				   '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Planet planet = (Planet) o;
		return id == planet.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
