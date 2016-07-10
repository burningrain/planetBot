package com.github.br.starmarines.gamecore.spi;


import com.github.br.starmarines.gamecore.api.GalaxyType;

/**
 * Класс содержит статическую метаинформацию об игре
 * @author user
 *
 */
public class GameInfo {
	
	private final Long id;
	private final String title;
	private final int playersCount;      // на сколько игроков создана игра
	private final Integer maxStepAmount; // число ходов до конца игры. Если null, то число шагов неограничено
	private final GalaxyType galaxyType;
	
	
	public GameInfo(final Long id, final String title, 
			final int playersCount, final Integer maxStepAmount, final GalaxyType galaxyType){
		this.id = id;
		this.title = title;
		this.playersCount = playersCount;
		this.maxStepAmount = maxStepAmount;
		this.galaxyType = galaxyType;
	}


	public Long getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public int getPlayersCount() {
		return playersCount;
	}


	public Integer getMaxStepAmount() {
		return maxStepAmount;
	}


	public GalaxyType getGalaxyType() {
		return galaxyType;
	}	
	

}
