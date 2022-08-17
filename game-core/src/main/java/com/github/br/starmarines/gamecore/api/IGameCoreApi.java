package com.github.br.starmarines.gamecore.api;

import java.util.List;


public interface IGameCoreApi {

    int createGame(String title, GalaxyCreateGameDelta delta);

    void deleteGame(int gameId);

    /**
     * сначала добавляем игроков к игре, а после уже стартуем игру
     */
    void startGame(int gameId);

    void pauseGame(int gameId);

    byte[] calculateNextState(int gameId);

    byte[] getGameState(int gameId);

    List<Integer> getGamesList(int from, int to);

    StepResultEnum addPlayerMoves(int gameId, short playerId, long[] moves);

}
