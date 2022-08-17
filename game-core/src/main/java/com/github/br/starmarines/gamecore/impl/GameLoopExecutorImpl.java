package com.github.br.starmarines.gamecore.impl;

import com.github.br.starmarines.gamecore.api.GameStateEnum;
import com.github.br.starmarines.gamecore.api.IGameCoreApi;
import com.github.br.starmarines.gamecore.config.GameCoreSettings;
import com.github.br.starmarines.gamecore.engine.IGameLoopExecutor;
import com.github.br.starmarines.gamecore.engine.utils.GameStateUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GameLoopExecutorImpl implements IGameLoopExecutor {

    private final ScheduledExecutorService scheduledExecutorService;

    private final IGameCoreApi gameCoreApi;
    private final GameCoreSettings gameCoreSettings;


    public GameLoopExecutorImpl(GameCoreSettings gameCoreSettings, IGameCoreApi gameCoreApi) {
        this.gameCoreApi = gameCoreApi;
        this.gameCoreSettings = gameCoreSettings;

        scheduledExecutorService = Executors.newScheduledThreadPool(this.gameCoreSettings.getGameLoopExecutorsCount(), new ThreadFactory() {

            private final AtomicInteger counter = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("game-step-computing-thread-" + counter.getAndIncrement());
                return thread;
            }
        });
    }

    @Override
    public void start(int gameId) {
        scheduleComputingGameStep(gameId);
    }

    @Override
    public void stopAll() {
        scheduledExecutorService.shutdownNow();
    }

    private void scheduleComputingGameStep(int gameId) {
        scheduledExecutorService.schedule(() -> {
            byte[] state = gameCoreApi.calculateNextState(gameId);
            GameStateEnum gameState = GameStateUtils.getGameState(state);
            if (GameStateEnum.FINISHED == gameState || GameStateEnum.PAUSED == gameState) {
                //TODO удалять игру из списка активных
                return;
            }
            scheduleComputingGameStep(gameId);
        }, this.gameCoreSettings.getComputingStepTimeInSec(), TimeUnit.SECONDS);
    }

}
