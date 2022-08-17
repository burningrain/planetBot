package com.github.br.starmarines.gamecore.impl.store;

import com.github.br.starmarines.gamecore.api.GameInfo;
import com.github.br.starmarines.gamecore.api.store.IGameInfoStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryGameInfoStore implements IGameInfoStore {

    private ReentrantLock mutex = new ReentrantLock();

    private HashMap<UUID, GameInfo> uuidMap = new HashMap<>();
    private HashMap<Integer, GameInfo> intMap = new HashMap<>();

    @Override
    public void put(UUID uuid, GameInfo info) {
        mutex.lock();
        try {
            uuidMap.put(uuid, info);
            intMap.put(info.getGameId(), info);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public GameInfo getById(UUID gameId) {
        mutex.lock();
        try {
            return uuidMap.get(gameId);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public List<GameInfo> getByGameIds(List<Integer> gamesList) {
        mutex.lock();
        try {
            ArrayList<GameInfo> result = new ArrayList<>(gamesList.size());
            for (Integer id : gamesList) {
                GameInfo gameInfo = intMap.get(id);
                if (gameInfo != null) {
                    result.add(gameInfo);
                }
            }
            return result;
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public GameInfo delete(UUID gameId) {
        mutex.lock();
        try {
            GameInfo gameInfo = uuidMap.get(gameId);
            intMap.remove(gameInfo.getGameId());
            uuidMap.remove(gameInfo.getId());

            return gameInfo;
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void update(UUID uuid, GameInfo gameInfo) {
        mutex.lock();
        try {
            uuidMap.put(uuid, gameInfo);
            intMap.put(gameInfo.getGameId(), gameInfo);
        } finally {
            mutex.unlock();
        }
    }

}
