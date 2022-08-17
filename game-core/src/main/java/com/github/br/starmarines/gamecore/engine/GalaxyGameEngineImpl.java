package com.github.br.starmarines.gamecore.engine;

import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.api.GameResultEnum;
import com.github.br.starmarines.gamecore.api.GameStateEnum;
import com.github.br.starmarines.gamecore.api.StepResultEnum;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.api.GalaxyCreateGameDelta;
import com.github.br.starmarines.gamecore.engine.converter.GalaxyConverter;
import com.github.br.starmarines.gamecore.engine.utils.*;
import com.github.br.starmarines.gamecore.collections.Pool;

import java.util.*;

/**
 * Not threadsafe. Не потокобезопасно. Содержит состояние.
 * <p>
 * Для работы обязательны условия:
 * 1) игроки отсортированы по идентификатору по возрастанию с нуля без пропусков
 * 2) пленеты отсортированы по идентификатору по возрастанию с нуля без пропусков
 * 3) пустые планеты принадлежат игроку 0
 */
public final class GalaxyGameEngineImpl implements IGalaxyGameEngine, Pool.Poolable {

    private static final GalaxyConverter GALAXY_CONVERTER = new GalaxyConverter();

    /**
     * игровая карта в виде:
     *  - описания вершин графа ми их свойств в виде массива байт
     *  - описание ребер графа в виде BitSet.
     */
    private final ByteGalaxy byteGalaxy;

    /**
     * ходы игроков
     * [] - идентификатор равен идентификатору планеты
     * 16 бит  (2 байта) идентификатор планеты
     * [][] - ходы игроков. длина каждого массива = числу игроков. Игроки отсортированы по идентификаторам. "ид массива" = "ид игрока"
     * 0-31    32 бита (4 байта) число юнитов, которое игрок переводит между планетами
     * 32-47   16 бит  (2 байта) идентификатор игрока
     */
    private long[][] playersMove;

    /**
     * текущий номер шага игры|обсчета графа.
     */
    private int currentStepNumber;

    /**
     * ссылка
     */
    private final Galaxy galaxy;

    public GalaxyGameEngineImpl(Galaxy galaxy) {
        this.galaxy = Objects.requireNonNull(galaxy);
        byteGalaxy = GALAXY_CONVERTER.toByteArray(galaxy);
        this.playersMove = initPlayersSteps(galaxy);
    }

    @Override
    public void reset(GalaxyCreateGameDelta delta) {
        Objects.requireNonNull(delta);

        currentStepNumber = 0;
        this.playersMove = initPlayersSteps(galaxy);
        GALAXY_CONVERTER.resetPlanetsState(this.byteGalaxy, galaxy, false);
    }

    @Override
    public void reset() {
    }

    private long[][] initPlayersSteps(Galaxy galaxy) {
        return new long[galaxy.getPlanets().size()][galaxy.getMaxPlayersCount()];
    }

    /**
     * 1 команда движения - это 64 бит (8 байт), т.е. long. В него зашито:
     * 0-32 - 32 бита (4 байта) число юнитов, которое игрок переводит между планетами
     * 32-48 - 16 бит (2 байта) идентификатор планеты, КУДА переводит юниты. итого 65536 планет максимум
     * 49-64 - 16 бит (2 байта) идентификатор планеты, ОТКУДА переводит юниты. итого 65536 планет максимум
     *
     * @param playerId - идентификатор игрока. Идет без пропусков, начиная с 0 для ускорения
     * @param moves    - ходы игрока
     * @return "OK" - в случае успешной валидации параметров, иначе описание ошибки
     */
    @Override
    public StepResultEnum addPlayerMoves(short playerId, long[] moves) {
        Objects.requireNonNull(moves);

        int size = byteGalaxy.getSize();
        BitSet planetMatrix = byteGalaxy.getPlanetMatrix();
        byte[] planets = byteGalaxy.getPlanets();

        for (long move : moves) {
            int units = MoveUtils.getUnits(move);
            if (units <= 0) {
                return StepResultEnum.UNITS_AMOUNT_IS_NEGATIVE_OR_ZERO_ERROR;
            }

            short fromId = MoveUtils.getPlanetFrom(move);
            short toId = MoveUtils.getPlanetTo(move);
            // проверяем, что есть ребро и такой ход в принципе возможен
            if (!PlanetMatrixUtils.checkEdgeInMatrix(planetMatrix, size, fromId, toId)) {
                return StepResultEnum.EDGE_NOT_EXIST_ERROR;
            }
            int unitsCount = PlanetUtils.getUnitsCount(planets, fromId);
            short ownerId = PlanetUtils.getOwner(planets, fromId);

            // проверяем, что игрок может перемещать юнитов с этой планеты (овнерство)
            if (ownerId != playerId) {
                return StepResultEnum.PLAYER_IS_NOT_OWNER_ERROR;
            }
            // проверяем, что столько юнитов действительно есть (число юнитов)
            if (unitsCount < units) {
                return StepResultEnum.UNITS_ARE_NOT_ENOUGH_ERROR;
            }

            playersMove[fromId][playerId] = PlayerMoveUtils.createPlayerMove(-units, playerId);
            playersMove[toId][playerId] = PlayerMoveUtils.createPlayerMove(units, playerId);
        }

        return StepResultEnum.OK;
    }

    @Override
    public byte[] computeNextState() {
        byte[] planets = byteGalaxy.getPlanets();
        int maxStepsCount = byteGalaxy.getMaxStepsCount();

        short lastOwnerId = 0;
        for (int i = PlanetUtils.GAME_STATE_IN_BYTES_SIZE + PlanetUtils.GAME_STEP_IN_BYTES_SIZE;
             i < planets.length;
             i += PlanetUtils.PLANET_IN_BYTES_SIZE) {
            short planetId = PlanetUtils.getPlanetIdByIndex(planets, i);
            long[] moves = playersMove[planetId];
            setNewPlanetOwnerAndUnitsCount(planets, i, moves);
            setNewUnitsCountByType(planets, i);

            short currentOwnerId = PlanetUtils.getOwnerIdByIndex(planets, i);
            if (currentOwnerId != 0) {
                if (lastOwnerId == 0) {
                    lastOwnerId = currentOwnerId;
                } else if (lastOwnerId != -1 && lastOwnerId != currentOwnerId) {
                    lastOwnerId = -1;
                }
            }
        }

        clearMoves();
        currentStepNumber++;

        GameStateEnum state = GameStateEnum.IN_PROCESS;
        GameResultEnum gameResult = GameResultEnum.UNKNOWN;
        if (lastOwnerId != -1) {
            state = GameStateEnum.FINISHED;
            gameResult = GameResultEnum.PLAYER_WIN; // кто именно победил - можно брать из первой же планеты
        }
        if (currentStepNumber == maxStepsCount) {
            state = GameStateEnum.FINISHED;
            gameResult = GameResultEnum.STEPS_AMOUNT_IS_OVER;
        }
        planets[0] = GameStateUtils.createState(state, gameResult);

        return planets;
    }

    @Override
    public byte[] getCurrentGameState() {
        return byteGalaxy.getPlanets();
    }

    @Override
    public String getTitle() {
        return byteGalaxy.getTitle();
    }

    private void clearMoves() {
        for (long[] moves : playersMove) {
            Arrays.fill(moves, 0);
        }
    }

    private void setNewUnitsCountByType(byte[] planets, int index) {
        PlanetType planetType = PlanetUtils.getPlanetTypeByIndex(planets, index);
        int unitsCount = PlanetUtils.getUnitsCountByIndex(planets, index);
        PlanetUtils.setUnitCountByIndex(
            planets,
            index,
            Math.min(planetType.getLimit(), unitsCount + (int)((float) unitsCount * planetType.getIncrement() / 100f))
        );
    }

    private void setNewPlanetOwnerAndUnitsCount(byte[] planets, int index, long[] moves) {
        short ownerId = PlanetUtils.getOwnerIdByIndex(planets, index);
        int unitsCount = PlanetUtils.getUnitsCountByIndex(planets, index);

        // сначала свои юниты улетают или прилетают на планету
        unitsCount += moves[ownerId];

        // ищем победителя
        int maxUnits = unitsCount;
        short maxUnitsOwnerId = ownerId;
        for (int i = 0; i < moves.length; i++) {
            // пропускаем самого себя, так как уже посчитали выше
            if (i == ownerId) {
                continue;
            }
            int units = MoveUtils.getUnits(moves[i]);
            if (units > maxUnits) {
                maxUnits = units;
                maxUnitsOwnerId = (short) i;
            }
        }
        PlanetUtils.setUnitCountByIndex(planets, index, maxUnits);
        PlanetUtils.setOwnerByIndex(planets, index, maxUnitsOwnerId);
    }

}
