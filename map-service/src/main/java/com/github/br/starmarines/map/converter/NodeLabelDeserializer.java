package com.github.br.starmarines.map.converter;

import java.lang.reflect.Type;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class NodeLabelDeserializer implements JsonDeserializer<Planet> {

	@Override
	public Planet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Planet planet = null;
		JsonObject jsonObject = json.getAsJsonObject();
		planet = new Planet(jsonObject.get("id").getAsString());
		planet.setType(typeMaper(jsonObject.get("type").getAsString()));
		planet.setUnits(jsonObject.get("units").getAsInt());
		planet.setOwner(jsonObject.get("owner").getAsString());
		return planet;
	}

	private PlanetType typeMaper(String type) {
		PlanetType result = null;
		switch (type) {
		case "TYPE_A":
			result = PlanetType.TYPE_A;
			break;
		case "TYPE_B":
			result = PlanetType.TYPE_B;
			break;
		case "TYPE_C":
			result = PlanetType.TYPE_C;
			break;
		case "TYPE_D":
			result = PlanetType.TYPE_D;
			break;
		default:
			result = PlanetType.TYPE_A;
			break;
		}
		return result;
	}

}
