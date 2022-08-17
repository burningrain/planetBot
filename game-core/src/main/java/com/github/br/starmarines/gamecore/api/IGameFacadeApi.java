package com.github.br.starmarines.gamecore.api;

import com.github.br.starmarines.game.api.galaxy.Player;

import java.util.List;
import java.util.UUID;

public interface IGameFacadeApi {

    GameInfo createGame(String title);

    void deleteGame(UUID gameId);

    void addPlayerToGame(UUID gameId, Player player);

    void deletePlayerFormGame(UUID gameId, Player player);

    List<Player> getPLayers(UUID gameId);

    GameInfo startGame(UUID gameId);

    void pauseGame(UUID gameId);

    void stopAllGame();

    byte[] getGameState(int gameId);

    List<GameInfo> getGamesList(int from, int to);

    StepResultEnum addPlayerMoves(int gameId, short playerId, long[] moves);

}
