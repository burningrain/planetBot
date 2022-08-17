package com.github.br.starmarines.gamecore.engine.utils;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;

public final class PlanetUtils {

    private PlanetUtils() {
    }

    public static final int GAME_STATE_IN_BYTES_SIZE = 1;
    public static final int GAME_STEP_IN_BYTES_SIZE = 4;
    public static final int PLANET_IN_BYTES_SIZE = 10;

    /**
     * Метод пакует планету в бинарный вид.
     * для ускорения массив сортирован и сделан так, что "id планеты" = "id элемента в массиве"
     * 16 бит  (2 байта) идентификатор планеты
     * 16 бит  (2 байта) игрок-хозяин планеты
     * 32 бита (4 байта) число юнитов на планете
     * 8 бит   (1 байт)  тип планеты. Тип определяет прирост юнитов
     * 8 бит   (1 байт) МНОГО!!! является ли планета базой для игрока
     */
    public static byte[] toByteArrayViewPlanet(short planetId, Planet planet, boolean isStartPoint) {
        byte[] result = new byte[PLANET_IN_BYTES_SIZE];
        ByteUtils.fillByteArrayFromUnsignedInt(result, 0, 1, planetId);
        ByteUtils.fillByteArrayFromUnsignedInt(result, 2, 3, planet.getOwnerId());
        ByteUtils.fillByteArrayFromUnsignedInt(result, 4, 7, planet.getUnits());
        result[8] = planet.getType().getCode();
        result[9] = isStartPoint ? (byte) 1 : (byte) 0;

        return result;
    }

    public static short getPlanetIdByIndex(byte[] planets, int startPlanetIndex) {
        return ByteUtils.toUnsignedShort(planets[startPlanetIndex], planets[startPlanetIndex + 1]);
    }

    public static short getOwnerIdByIndex(byte[] planets, int startPlanetIndex) {
        return ByteUtils.toUnsignedShort(planets[startPlanetIndex + 2], planets[startPlanetIndex + 3]);
    }

    public static int getUnitsCountByIndex(byte[] planets, int startPlanetIndex) {
        return ByteUtils.toUnsignedInt(planets[startPlanetIndex + 4], planets[startPlanetIndex + 5],
                planets[startPlanetIndex + 6], planets[startPlanetIndex + 7]);
    }

    public static PlanetType getPlanetTypeByIndex(byte[] planets, int startPlanetIndex) {
        return PlanetType.getByCode(planets[startPlanetIndex + 8]);
    }

    public static boolean isPlanetStartPoint(byte[] planets, int startPlanetIndex) {
        return planets[startPlanetIndex + 9] == 1;
    }

    public static int getIndexByPlanetId(short planetId) {
        return GAME_STATE_IN_BYTES_SIZE + GAME_STEP_IN_BYTES_SIZE + planetId * PLANET_IN_BYTES_SIZE;
    }

    public static int getUnitsCount(byte[] planets, short planetId) {
        int index = getIndexByPlanetId(planetId);
        return ByteUtils.toUnsignedInt(
                planets[index + 4],
                planets[index + 5],
                planets[index + 6],
                planets[index + 7]
        );
    }

    public static short getOwner(byte[] planets, short planetId) {
        int index = getIndexByPlanetId(planetId);
        return ByteUtils.toUnsignedShort(
                planets[index + 2],
                planets[index + 3]
        );
    }

    public static void setUnitCountByIndex(byte[] planets, int index, int unitsCount) {
        ByteUtils.fillByteArrayFromUnsignedInt(planets, index + 4, index + 7, unitsCount);
    }

    public static void setOwnerByIndex(byte[] planets, int index, short ownerId) {
        ByteUtils.fillByteArrayFromUnsignedInt(planets, index + 2, index + 3, ownerId);
    }

}
