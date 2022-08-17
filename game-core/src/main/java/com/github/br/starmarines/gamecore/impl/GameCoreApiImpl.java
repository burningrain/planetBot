package com.github.br.starmarines.gamecore.impl;

import com.github.br.starmarines.gamecore.api.GalaxyCreateGameDelta;
import com.github.br.starmarines.gamecore.api.GameStateEnum;
import com.github.br.starmarines.gamecore.api.IGameCoreApi;
import com.github.br.starmarines.gamecore.api.StepResultEnum;
import com.github.br.starmarines.gamecore.collections.AtomicRemoveIndexKeepArray;
import com.github.br.starmarines.gamecore.collections.Pool;
import com.github.br.starmarines.gamecore.engine.GameContainerEngine;
import com.github.br.starmarines.gamecore.config.GameCoreSettings;
import com.github.br.starmarines.gamecore.engine.utils.GameStateUtils;
import com.github.br.starmarines.map.service.api.MapService;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class GameCoreApiImpl implements IGameCoreApi {

    private final GameCoreSettings gameCoreSettings;

    private final ReentrantLock mutex = new ReentrantLock();
    // @GuardedBy(mutex)
    private final HashMap<String, Pool<GameContainerEngine>> gamesPools;
    private final AtomicRemoveIndexKeepArray<GameContainerEngine> gamesList;

    private final MapService mapService;

    public GameCoreApiImpl(GameCoreSettings gameCoreSettings, MapService mapService) {
        this.gameCoreSettings = gameCoreSettings;
        this.mapService = mapService;

        gamesPools = new HashMap<>();
        gamesList = new AtomicRemoveIndexKeepArray<>(this.gameCoreSettings.getGamesMaxCapacity());
    }

    //FIXME найти способ убрать synchronized с вставки и удаления
    @Override
    public int createGame(String title, GalaxyCreateGameDelta delta) {
        mutex.lock();
        try {
            Pool<GameContainerEngine> pool = gamesPools.putIfAbsent(title, new Pool<GameContainerEngine>() {
                @Override
                protected GameContainerEngine newObject() {
                    try {
                        return new GameContainerEngine(Objects.requireNonNull(mapService.getMap(title)));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            GameContainerEngine engine = pool.obtain();
            engine.reset(delta);

            return gamesList.add(engine); // index of element in array
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void deleteGame(int gameId) {
        mutex.lock();
        try {
            GameContainerEngine engine = checkGame(gamesList.remove(gameId), gameId);
            Pool<GameContainerEngine> pool = gamesPools.get(engine.getTitle());
            pool.free(engine);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void startGame(int gameId) {
        GameContainerEngine engine = checkGame(gamesList.get(gameId), gameId);
        GameStateEnum gameState = GameStateUtils.getGameState(engine.getCurrentGameState());
        if(GameStateEnum.PAUSED == gameState) {
            GameStateUtils.setGameState(GameStateEnum.IN_PROCESS, engine.getCurrentGameState());
        } else if (GameStateEnum.NOT_STARTED != gameState) {
            throw new IllegalStateException("The game is not be started. Current state=[" + gameState + "]");
        }
    }

    @Override
    public void pauseGame(int gameId) {
        GameContainerEngine engine = checkGame(gamesList.get(gameId), gameId);
        GameStateUtils.setGameState(GameStateEnum.PAUSED, engine.getCurrentGameState());
    }

    @Override
    public byte[] calculateNextState(int gameId) {
        GameContainerEngine engine = checkGame(gamesList.get(gameId), gameId);
        GameStateEnum state = engine.getGameStatus();
        if (GameStateEnum.FINISHED == state) {
            return engine.getCurrentGameState();
        }

        return engine.computeNextState();
    }

    @Override
    public byte[] getGameState(int gameId) {
        return checkGame(gamesList.get(gameId), gameId)
                .getCurrentGameState();
    }

    @Override
    public List<Integer> getGamesList(int from, int to) {
        return gamesList.asCollection(from, to).stream().map(it -> it.id).collect(Collectors.toList());
    }

    @Override
    public StepResultEnum addPlayerMoves(int gameId, short playerId, long[] moves) {
        return checkGame(gamesList.get(gameId), gameId).addPlayerMoves(playerId, moves);
    }

    private <T> T checkGame(T object, int gameId) {
        return Objects.requireNonNull(object, "game is not found for id=[" + gameId + "]");
    }

}
