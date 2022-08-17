package com.github.br.starmarines.gamecore.impl.store;

import com.github.br.starmarines.game.api.galaxy.Player;
import com.github.br.starmarines.gamecore.api.store.IGamePlayerStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryGamePlayerStore implements IGamePlayerStore {

    private final ConcurrentHashMap<UUID, Players> playersMap = new ConcurrentHashMap<>();

    @Override
    public int add(UUID gameId, Player player) {
        Players players = playersMap.get(gameId);
        if (players == null) {
            players = new Players();
            playersMap.put(gameId, players);
        }
        synchronized (players) {
            return players.add(player);
        }
    }

    @Override
    public void delete(UUID gameId, Player player) {
        Players players = playersMap.get(gameId);
        if (players == null) {
            return;
        }
        synchronized (players) {
            players.delete(player);
        }
    }

    @Override
    public List<Player> getAllPlayersByGameId(UUID gameId) {
        Players players = playersMap.get(gameId);
        if (players == null) {
            return null;
        }
        synchronized (players) {
            return players.getAll();
        }
    }

    @Override
    public int getPlayersAmountByGameId(UUID gameId) {
        Players players = playersMap.get(gameId);
        if (players == null) {
            return -1;
        }
        synchronized (players) {
            return players.size();
        }
    }

    @Override
    public void update(UUID gameId, Player player) {
        //do nothing
    }

    private static class Players {

        private final ArrayList<Player> players = new ArrayList<>(6);

        public int add(Player player) {
            players.add(player);
            return players.size() - 1;
        }

        public int size() {
            return players.size();
        }

        public List<Player> getAll() {
            return players;
        }

        public void delete(Player player) {
            players.remove(player);
        }

    }

}
