package com.github.br.starmarines.coreplugins.event;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.api.GalaxyType;

public class StatePropertiesVO {
	
	private boolean isInit = false; // Завязка на внутренние свойства специфичные для UI

	
	
	//com.github.br.starmarines.gamecore.spi.GameInfo
	private  Long gameInfoId;
	private  String gameInfoTitle;
	private  int gameInfoPlayersCount;      // на сколько игроков создана игра
	private  Integer gameInfoMaxStepAmount; // число ходов до конца игры. Если null, то число шагов неограничено
	//com.github.br.starmarines.gamecore.api.GalaxyType
	private GalaxyType galaxyType;
	//com.github.br.starmarines.gamecore.spi.GameStatus нужны ?
	
	//gamecore-api
	private Galaxy galaxy;
	
	
}
