package com.github.br.starmarines.map.converter;

import java.lang.reflect.Type;

import com.github.br.starmarines.gamecore.api.GalaxyType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class GameMetaInfoDeserializer implements JsonDeserializer<GameMetaInfo> {

	@Override
	public GameMetaInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		GameMetaInfo info = null;
		JsonObject jsonObject = json.getAsJsonObject();
		info = new GameMetaInfo();
		if(jsonObject.has("isStartPoint")) {
			info.setIsStartPoint(jsonObject.get("isStartPoint").getAsBoolean());
		}
		if(jsonObject.has("galaxyType")) {
			String type = jsonObject.get("galaxyType").getAsString();
			info.setType(typeMaper(type));
		}
		return info;
	}

	private GalaxyType typeMaper(String type) {
		GalaxyType result = null;
		switch (type) {
		case "BIG_BASES":
			result = GalaxyType.BIG_BASES;
			break;
		case "SMALL_BASES":
			result = GalaxyType.SMALL_BASES;
			break;
		default:
			result = GalaxyType.SMALL_BASES;
			break;
		}
		return result;
	}
}
