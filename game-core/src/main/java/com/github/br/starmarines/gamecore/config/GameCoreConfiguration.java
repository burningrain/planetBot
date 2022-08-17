package com.github.br.starmarines.gamecore.config;

import com.github.br.starmarines.gamecore.api.IGameCoreApi;
import com.github.br.starmarines.gamecore.api.IGameFacadeApi;
import com.github.br.starmarines.gamecore.api.store.IGameInfoStore;
import com.github.br.starmarines.gamecore.api.store.IGamePlayerStore;
import com.github.br.starmarines.gamecore.engine.IGameLoopExecutor;
import com.github.br.starmarines.gamecore.impl.GameCoreApiImpl;
import com.github.br.starmarines.gamecore.impl.GameFacadeApiImpl;
import com.github.br.starmarines.gamecore.impl.GameLoopExecutorImpl;
import com.github.br.starmarines.gamecore.impl.store.MemoryGameInfoStore;
import com.github.br.starmarines.gamecore.impl.store.MemoryGamePlayerStore;
import com.github.br.starmarines.map.service.api.MapService;

import java.util.Objects;

public class GameCoreConfiguration {

    private final GameCoreSettings gameCoreSettings;
    private final IGameCoreApi gameCoreApi;
    private final IGameFacadeApi gameFacadeApi;
    private final IGameLoopExecutor gameLoopExecutor;

    private final IGameInfoStore gameInfoStore;
    private final IGamePlayerStore gamePlayerStore;

    private GameCoreConfiguration(GameCoreSettings gameCoreSettings,
                                  IGameCoreApi gameCoreApi,
                                  IGameFacadeApi gameFacadeApi,
                                  IGameLoopExecutor gameLoopExecutor,
                                  IGameInfoStore gameInfoStore,
                                  IGamePlayerStore gamePlayerStore) {
        this.gameCoreSettings = Objects.requireNonNull(gameCoreSettings);
        this.gameCoreApi = Objects.requireNonNull(gameCoreApi);
        this.gameFacadeApi = Objects.requireNonNull(gameFacadeApi);
        this.gameLoopExecutor = Objects.requireNonNull(gameLoopExecutor);
        this.gameInfoStore = Objects.requireNonNull(gameInfoStore);
        this.gamePlayerStore = Objects.requireNonNull(gamePlayerStore);
    }

    public GameCoreSettings getGameCoreSettings() {
        return gameCoreSettings;
    }

    public IGameCoreApi getGameCoreApi() {
        return gameCoreApi;
    }

    public IGameFacadeApi getGameFacadeApi() {
        return gameFacadeApi;
    }

    public IGameLoopExecutor getGameLoopExecutor() {
        return gameLoopExecutor;
    }

    public IGameInfoStore getGameInfoStore() {
        return gameInfoStore;
    }

    public IGamePlayerStore getGamePlayerStore() {
        return gamePlayerStore;
    }


    public static class Builder {

        private GameCoreSettings gameCoreSettings;
        private MapService mapService;
        private IGameInfoStore gameInfoStore = new MemoryGameInfoStore();
        private IGamePlayerStore gamePlayerStore = new MemoryGamePlayerStore();

        public Builder setGameCoreSettings(GameCoreSettings gameCoreSettings) {
            this.gameCoreSettings = Objects.requireNonNull(gameCoreSettings);
            return this;
        }

        public Builder setGameInfoStore(IGameInfoStore gameInfoStore) {
            this.gameInfoStore = gameInfoStore;
            return this;
        }

        public Builder setGamePlayerStore(IGamePlayerStore gamePlayerStore) {
            this.gamePlayerStore = Objects.requireNonNull(gamePlayerStore);
            return this;
        }

        public Builder setMapService(MapService mapService) {
            this.mapService = Objects.requireNonNull(mapService);
            return this;
        }

        public GameCoreConfiguration build() {
            Objects.requireNonNull(mapService);
            Objects.requireNonNull(gameCoreSettings);
            Objects.requireNonNull(gameInfoStore);
            Objects.requireNonNull(gamePlayerStore);

            IGameCoreApi gameCoreApi = new GameCoreApiImpl(gameCoreSettings, mapService);
            IGameLoopExecutor gameLoopExecutor = new GameLoopExecutorImpl(gameCoreSettings, gameCoreApi);
            IGameFacadeApi gameFacadeApi = new GameFacadeApiImpl(
                    gameLoopExecutor,
                    gameCoreApi,
                    gameInfoStore,
                    gamePlayerStore
            );

            return new GameCoreConfiguration(
                    gameCoreSettings,
                    gameCoreApi,
                    gameFacadeApi,
                    gameLoopExecutor,
                    gameInfoStore,
                    gamePlayerStore
            );
        }

    }

}
