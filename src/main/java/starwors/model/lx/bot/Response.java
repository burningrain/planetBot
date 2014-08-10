/*
 * © EPAM Systems, 2012  
 */
package starwors.model.lx.bot;

import starwors.model.lx.galaxy.Planet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Хранит ответ от сервера: список планет и ошибок (если есть)
 */
public class Response {

	private Collection<Planet> planets;
	private Collection<String> errors;

	public Response() {
		planets = new ArrayList<Planet>();
		errors = new ArrayList<String>();
	}

	/**
	 * Получить список планет
	 */
	public Collection<Planet> getPlanets() {
		return Collections.unmodifiableCollection(planets);
	}

	/**
	 * Добавить плнету в список
	 */
	public void addPlanet(Planet planet) {
		planets.add(planet);
	}

	/**
	 * Получить список ошибок
	 */
	public Collection<String> getErrors() {
		return Collections.unmodifiableCollection(errors);
	}

	/**
	 * Добавить ошибку в список
	 */
	public void addError(String error) {
		errors.add(error);
	}

	/**
	 * Проверить, не закончилась ли игра
	 * @return {@code true} если игра еще идет, {@code false} — если игра завершилась
	 */
	public boolean isGameRunning() {
		boolean result = true;
		for (String error : errors) {
			if (error.startsWith("Game is finished. ")) {
				result = false;
			}
		}
		return result;
	}

}
