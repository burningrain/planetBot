package com.github.br.starmarines.gamecore.spi;

/**
 * имплементация листенера должна быть потокобезопасной, т.к. обращение идет из потока каждой игры
 * @author user
 *
 */
public interface IGameEventListener {
	
	void eventPerformed(GameEvent event);

}
