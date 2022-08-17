package com.github.br.starmarines.gamecore.engine.utils;

public final class PlayerMoveUtils {

    private PlayerMoveUtils() {
    }

    private static final int UNITS_OFFSET = 32;
    private static final int PLAYER_ID_OFFSET = 16;

    /**
     * 0-31    32 бита (4 байта) число юнитов, которое игрок переводит между планетами
     * 32-47   16 бит  (2 байта) идентификатор игрока
     * @param units
     * @param playerId
     * @return
     */
    public static long createPlayerMove(int units, short playerId) {
        return ((long) units << UNITS_OFFSET) | ((int)(playerId << PLAYER_ID_OFFSET));
    }

}
