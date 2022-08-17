package com.github.br.starmarines.gamecore.osgi;

import com.github.br.starmarines.game.api.galaxy.Player;
import com.github.br.starmarines.gamecore.api.GameInfo;
import com.github.br.starmarines.gamecore.api.IGameFacadeApi;
import com.github.br.starmarines.gamecore.api.StepResultEnum;
import com.github.br.starmarines.gamecore.config.GameCoreConfiguration;
import com.github.br.starmarines.gamecore.config.GameCoreSettings;
import com.github.br.starmarines.gamecore.impl.store.MemoryCacheMapService;
import com.github.br.starmarines.gamecore.impl.store.MemoryGameInfoStore;
import com.github.br.starmarines.gamecore.impl.store.MemoryGamePlayerStore;
import com.github.br.starmarines.map.service.api.MapService;
import org.osgi.service.component.annotations.*;
import org.osgi.service.log.LogService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

//TODO перенести OSGi в другой модуль. здесь оставить только чистое ядро без привязок к чему-либо
@Component(service = IGameFacadeApi.class)
public class GameCoreOsgiFacade implements IGameFacadeApi {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
    private LogService logService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
    private MapService mapService;


    private IGameFacadeApi gameFacadeApi;

    @Activate
    public void init(Map<String, String> properties) {
        GameCoreProperties gameCoreProperties = new GameCoreProperties(properties);

        GameCoreConfiguration.Builder builder = new GameCoreConfiguration.Builder();
        GameCoreConfiguration gameCoreConfiguration = builder
                .setGameCoreSettings(
                        new GameCoreSettings.Builder()
                                .setGameLoopExecutorsCount(gameCoreProperties.getGameLoopExecutorsCount())
                                .setComputingStepTimeInSec(gameCoreProperties.getComputingStepTimeInSec())
                                .setGamesMaxCapacity(gameCoreProperties.getGamesMaxCapacity())
                                .build())
                .setMapService(new MemoryCacheMapService(mapService))
                .setGameInfoStore(new MemoryGameInfoStore())
                .setGamePlayerStore(new MemoryGamePlayerStore())
                .build();
        gameFacadeApi = gameCoreConfiguration.getGameFacadeApi();
    }

    @Deactivate
    public void stop() {
        this.stopAllGame();
    }

    @Override
    public GameInfo createGame(String title) {
        return gameFacadeApi.createGame(title);
    }

    @Override
    public void deleteGame(UUID gameId) {
        gameFacadeApi.deleteGame(gameId);
    }

    @Override
    public void addPlayerToGame(UUID gameId, Player player) {
        gameFacadeApi.addPlayerToGame(gameId, player);
    }

    @Override
    public void deletePlayerFormGame(UUID gameId, Player player) {
        gameFacadeApi.deletePlayerFormGame(gameId, player);
    }

    @Override
    public List<Player> getPLayers(UUID gameId) {
        return gameFacadeApi.getPLayers(gameId);
    }

    @Override
    public GameInfo startGame(UUID gameId) {
        return gameFacadeApi.startGame(gameId);
    }

    @Override
    public void pauseGame(UUID gameId) {
        gameFacadeApi.pauseGame(gameId);
    }

    @Override
    public void stopAllGame() {
        gameFacadeApi.stopAllGame();
    }

    @Override
    public byte[] getGameState(int gameId) {
        return gameFacadeApi.getGameState(gameId);
    }

    @Override
    public List<GameInfo> getGamesList(int from, int to) {
        return gameFacadeApi.getGamesList(from, to);
    }

    @Override
    public StepResultEnum addPlayerMoves(int gameId, short playerId, long[] moves) {
        return gameFacadeApi.addPlayerMoves(gameId, playerId, moves);
    }

}
