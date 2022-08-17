package com.github.br.starmarines.gamecore.engine;

import com.github.br.starmarines.gamecore.api.GameStateEnum;
import com.github.br.starmarines.gamecore.api.StepResultEnum;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.api.GalaxyCreateGameDelta;
import com.github.br.starmarines.gamecore.engine.utils.GameStateUtils;
import com.github.br.starmarines.gamecore.collections.Pool;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Threadsafe
 * Потокобезопасная обертка для {@link GalaxyGameEngineImpl}
 */
public final class GameContainerEngine implements IGalaxyGameEngine, Pool.Poolable {

    private final GalaxyGameEngineImpl gameEngine;

    private final AtomicReference<GameStateSync> stateSync = new AtomicReference<>();

    /**
     * ссылка на массив, который может прямо сейчас обсчитываться
     * в GalaxyGameEngineImpl он закеширован
     */
    private volatile byte[] gameState = null;

    public GameContainerEngine(Galaxy galaxy) {
        gameEngine = new GalaxyGameEngineImpl(galaxy);

    }

    @Override
    public void reset(GalaxyCreateGameDelta delta) {
        gameEngine.reset(delta);
        stateSync.set(new GameStateSync(delta.getPlayersCount()));
    }

    @Override
    public StepResultEnum addPlayerMoves(short playerId, long[] moves) {
        GameStateSync gameStateSync = null;
        GameStateSync newGameState = null;
        do {
            gameStateSync = stateSync.get();
            if (GameStateSync.StateEnum.COMPUTE_STATE == gameStateSync.state) {
                return StepResultEnum.TOO_LATE_THE_GAME_IN_COMPUTING_STATE_ERROR;
            }
            newGameState = new GameStateSync(gameStateSync);
            newGameState.version++;
            newGameState.moves.set(playerId, moves);

        } while (!stateSync.compareAndSet(gameStateSync, newGameState));

        return StepResultEnum.OK;
    }

    @Override
    public byte[] computeNextState() {
        GameStateSync gameStateSync = null;
        GameStateSync newGameState = null;
        do {
            gameStateSync = stateSync.get();
            newGameState = new GameStateSync(gameStateSync); // копирование
            newGameState.version++;
            newGameState.state = GameStateSync.StateEnum.COMPUTE_STATE;
        } while (!stateSync.compareAndSet(gameStateSync, newGameState));

        gameState = null;
        for (short i = 0; i < newGameState.moves.length(); i++) {
            long[] moves = newGameState.moves.get(i);
            if (moves != null) {
                gameEngine.addPlayerMoves(i, moves);
            }
        }
        newGameState.clearMoves();
        byte[] gameState = gameEngine.computeNextState();

        do {
            gameStateSync = stateSync.get();
            newGameState = new GameStateSync(gameStateSync);
            newGameState.version++;
            newGameState.state = GameStateSync.StateEnum.WAIT_PLAYERS_STEPS;
        } while (!stateSync.compareAndSet(gameStateSync, newGameState));

        this.gameState = gameState;
        return this.gameState;
    }

    @Override
    public byte[] getCurrentGameState() {
        byte[] state = null;
        while ((state = gameState) == null) {
            // spin lock
        }
        return state;
    }

    public GameStateEnum getGameStatus() {
        return GameStateUtils.getGameState(getCurrentGameState());
    }

    @Override
    public String getTitle() {
        return gameEngine.getTitle();
    }

    @Override
    public void reset() {
    }

    private static final class GameStateSync {

        int version = 0;
        AtomicReferenceArray<long[]> moves;
        StateEnum state = StateEnum.WAIT_PLAYERS_STEPS;

        public GameStateSync(GameStateSync stateSync) {
            this.version = stateSync.version;
            this.moves = stateSync.moves;
            this.state = stateSync.state;
        }

        public GameStateSync(int playersCount) {
            this.moves = new AtomicReferenceArray<>(playersCount);
        }

        public void clearMoves() {
            int length = moves.length();
            for (int i = 0; i < length; i++) {
                moves.set(i, null);
            }
        }

        public enum StateEnum {
            WAIT_PLAYERS_STEPS,
            COMPUTE_STATE
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameStateSync that = (GameStateSync) o;
            return version == that.version && state == that.state;
        }

        @Override
        public int hashCode() {
            return Objects.hash(version, state);
        }

    }


}
