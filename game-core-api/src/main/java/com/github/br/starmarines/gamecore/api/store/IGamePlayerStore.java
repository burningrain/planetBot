package com.github.br.starmarines.gamecore.api.store;

import com.github.br.starmarines.game.api.galaxy.Player;

import java.util.List;
import java.util.UUID;

public interface IGamePlayerStore {

    int add(UUID gameId, Player player);

    void delete(UUID gameId, Player player);

    List<Player> getAllPlayersByGameId(UUID gameId);

    int getPlayersAmountByGameId(UUID gameId);

    void update(UUID gameId, Player player);

}
