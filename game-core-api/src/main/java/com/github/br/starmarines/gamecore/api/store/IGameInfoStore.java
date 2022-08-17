package com.github.br.starmarines.gamecore.api.store;

import com.github.br.starmarines.gamecore.api.GameInfo;

import java.util.List;
import java.util.UUID;

public interface IGameInfoStore {

    void put(UUID uuid, GameInfo info);

    GameInfo getById(UUID gameId);

    List<GameInfo> getByGameIds(List<Integer> gamesList);

    GameInfo delete(UUID gameId);

    void update(UUID uuid, GameInfo gameInfo);

}
