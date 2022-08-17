package com.github.br.starmarines.gamecore.impl;

import com.github.br.starmarines.game.api.galaxy.Player;
import com.github.br.starmarines.gamecore.api.*;
import com.github.br.starmarines.gamecore.api.store.IGameInfoStore;
import com.github.br.starmarines.gamecore.api.store.IGamePlayerStore;
import com.github.br.starmarines.gamecore.engine.IGameLoopExecutor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * входная точка к взаимодействию с логикой ядра
 */
public class GameFacadeApiImpl implements IGameFacadeApi {

    private final IGameCoreApi gameCoreApi;
    private final IGameInfoStore gameInfoStore;
    private final IGamePlayerStore gamePlayerStore;

    private final IGameLoopExecutor gameLoopExecutor;

    public GameFacadeApiImpl(IGameLoopExecutor gameLoopExecutor, IGameCoreApi gameCoreApi,
                             IGameInfoStore gameInfoStore, IGamePlayerStore gamePlayerStore) {
        this.gameLoopExecutor = gameLoopExecutor;
        this.gameCoreApi = gameCoreApi;
        this.gameInfoStore = gameInfoStore;
        this.gamePlayerStore = gamePlayerStore;
    }

    @Override
    public GameInfo createGame(String title) {
        UUID uuid = UUID.randomUUID();
        GameInfo info = new GameInfo(uuid, title);
        gameInfoStore.put(uuid, info);
        return info;
    }

    @Override
    public void deleteGame(UUID gameId) {
        GameInfo gameInfo = gameInfoStore.delete(gameId);
        gameCoreApi.deleteGame(gameInfo.getGameId());
    }

    @Override
    public void addPlayerToGame(UUID gameId, Player player) {
        gamePlayerStore.add(gameId, player);
    }

    @Override
    public void deletePlayerFormGame(UUID gameId, Player player) {
        gamePlayerStore.delete(gameId, player);
    }

    @Override
    public List<Player> getPLayers(UUID gameId) {
        return gamePlayerStore.getAllPlayersByGameId(gameId);
    }

    @Override
    public GameInfo startGame(UUID gameId) {
        GameInfo gameInfo = Objects.requireNonNull(gameInfoStore.getById(gameId));

        // нумеруем игроков от 0 до N
        List<Player> allPlayersByGameId = gamePlayerStore.getAllPlayersByGameId(gameId);
        int playersAmount = allPlayersByGameId.size();
        for(int i = 0; i < playersAmount; i++) {
            Player player = allPlayersByGameId.get(i);
            player.setInnerGamePlayerId(i);
            gamePlayerStore.update(gameId, player);
        }

        GalaxyCreateGameDelta delta = new GalaxyCreateGameDelta();
        delta.setPlayersCount(playersAmount);
        int innerGameId = gameCoreApi.createGame(gameInfo.getTitle(), delta);

        gameInfo.setGameId(innerGameId);
        gameInfoStore.update(gameId, gameInfo);
        gameCoreApi.startGame(innerGameId);
        gameLoopExecutor.start(innerGameId);

        return gameInfo;
    }

    @Override
    public void pauseGame(UUID gameId) {
        GameInfo gameInfo = Objects.requireNonNull(gameInfoStore.getById(gameId));
        gameCoreApi.pauseGame(gameInfo.getGameId());
    }

    @Override
    public void stopAllGame() {
        gameLoopExecutor.stopAll();
    }

    @Override
    public byte[] getGameState(int gameId) {
        return gameCoreApi.getGameState(gameId);
    }

    @Override
    public List<GameInfo> getGamesList(int from, int to) {
        List<Integer> gamesList = gameCoreApi.getGamesList(from, to);
        return gameInfoStore.getByGameIds(gamesList);
    }

    @Override
    public StepResultEnum addPlayerMoves(int gameId, short playerId, long[] moves) {
        return gameCoreApi.addPlayerMoves(gameId, playerId, moves);
    }

}
