package com.github.br.starmarines.gamecore.engine.utils;

public final class MoveUtils {

    private MoveUtils() {
    }

    private static final int UNITS_OFFSET = 32;
    private static final int FROM_PLANET_ID_OFFSET = 16;

    /**
     * @param units
     * @param fromId
     * @param toId
     * @return перемещение в виде числа long (64 бита - 8 байт)
     * 0-32 - 32 бита (4 байта) число юнитов, которое игрок переводит между планетами
     * 32-48 - 16 бит (2 байта) идентификатор планеты, КУДА переводит юниты. итого 65536 планет максимум
     * 49-64 - 16 бит (2 байта) идентификатор планеты, ОТКУДА переводит юниты. итого 65536 планет максимум
     */
    public static long createMove(int units, short fromId, short toId) {
        long result = ((long) units) << UNITS_OFFSET;
        return result | (int)(fromId << FROM_PLANET_ID_OFFSET) | toId;
    }

    public static int getUnits(long move) {
        return (int) (move >>> UNITS_OFFSET);
    }

    public static short getPlanetFrom(long move) {
        return (short) (move >>> FROM_PLANET_ID_OFFSET);
    }

    public static short getPlanetTo(long move) {
        return (short) move;
    }

}