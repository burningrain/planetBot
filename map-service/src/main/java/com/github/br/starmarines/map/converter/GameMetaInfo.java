package com.github.br.starmarines.map.converter;

import com.github.br.starmarines.gamecore.api.GalaxyType;

public class GameMetaInfo {
	private Boolean isStartPoint;
	private GalaxyType type;

	public GalaxyType getType() {
		return type;
	}

	public void setType(GalaxyType type) {
		this.type = type;
	}

	public Boolean getIsStartPoint() {
		return isStartPoint;
	}

	public void setIsStartPoint(Boolean isStartPoint) {
		this.isStartPoint = isStartPoint;
	}

	@Override
	public String toString() {
		return "GameMetaInfo [isStartPoint=" + isStartPoint + ", type=" + type + "]";
	}

}
