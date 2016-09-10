package com.github.br.starmarines.gamecore.spi;

public enum GameStatus {
	CREATED,
	ALL_PLAYERS_RECRUITED, // игроки набраны, игра еще не начата
	// пока игра в процессе, она переключается между этими двумя статусами
	COMPUTE_STEP,          // вычисление нового состояния игры, игроки в это время ходить не могут
	WAITING_PLAYERS_STEPS, // ожидание ходов игроков
	FINISH	
}
