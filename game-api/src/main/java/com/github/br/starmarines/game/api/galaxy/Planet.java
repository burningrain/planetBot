package com.github.br.starmarines.game.api.galaxy;

import java.util.ArrayList;
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
		//return Collections.unmodifiableList(neighbours);
        return neighbours;
	}

	/**
	 * Добавить соседа
	 */
	public void addNeighbour(Planet neighbour) {
		neighbours.add(neighbour);
	}

	@Override
	public String toString() {
		//return id;
        return String.valueOf(units);  //TODO изменить
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        if (!id.equals(planet.id)) return false;
        if (type != planet.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }    


}
