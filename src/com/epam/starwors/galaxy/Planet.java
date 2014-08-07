/*
 * © EPAM Systems, 2012  
 */
package com.epam.starwors.galaxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Содержит информацию о планете
 */
public class Planet {

	private String id;
	private String owner;
	private int units;
	private PlanetType type;
	private List<Planet> neighbours = new ArrayList<Planet>();

	/**
	 * Создать планету с заданным id
	 */
	public Planet(String id) {
		this.id = id;
	}

	/**
	 * Получить id планеты
	 */
	public String getId() {
		return id;
	}

	/**
	 * Получить владельца планеты
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Задать владельца планеты
	 */
	public void setOwner(String owner) {
		this.owner = owner;
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

	/**
	 * Получить список соседей планеты
	 */
	public List<Planet> getNeighbours() {
		return Collections.unmodifiableList(neighbours);
	}

	/**
	 * Добавить соседа
	 */
	public void addNeighbours(Planet neighbour) {
		neighbours.add(neighbour);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Planet neighbour : neighbours) {
			result.append(neighbour.getId()).append(", ");
		}

		return "Planet [id=" + id + ", owner=" + owner + ", Units=" + units + "/" + type.getLimit() + ", neighbours=" + result.toString() + "]\n";
	}

}
