package com.github.br.starmarines.gamecore.engine.converter;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.gamecore.api.GameResultEnum;
import com.github.br.starmarines.gamecore.api.GameStateEnum;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.gamecore.engine.ByteGalaxy;
import com.github.br.starmarines.gamecore.engine.utils.ByteUtils;
import com.github.br.starmarines.gamecore.engine.utils.GameStateUtils;
import com.github.br.starmarines.gamecore.engine.utils.PlanetMatrixUtils;
import com.github.br.starmarines.gamecore.engine.utils.PlanetUtils;

import java.util.BitSet;
import java.util.Collection;

public class GalaxyConverter {

    public ByteGalaxy toByteArray(Galaxy galaxy) {
        int size = galaxy.getPlanets().size();
        int halfOfMatrix = size * (size - 1) / 2; // половина матрицы смежности (т.к. ребра двунаправленные всегда) без диагонали симметрии (чтобы не двмгать нумерацию)
        BitSet planetMatrix = new BitSet(halfOfMatrix);
        byte[] planets = new byte[
                PlanetUtils.GAME_STATE_IN_BYTES_SIZE +
                        PlanetUtils.GAME_STEP_IN_BYTES_SIZE +
                        size * PlanetUtils.PLANET_IN_BYTES_SIZE
                ];

        ByteGalaxy byteGalaxy = new ByteGalaxy(galaxy.getTitle(), planets, planetMatrix, galaxy.getMaxStepsCount(), planets.length);
        resetPlanetsState(byteGalaxy, galaxy, true);
        return byteGalaxy;
    }

    // не для использования в бизнес-логике. Обратное преобразование только для тестов
    public Galaxy fromByteArray(ByteGalaxy byteGalaxy) {
        byte[] planets = byteGalaxy.getPlanets();
        BitSet planetMatrix = byteGalaxy.getPlanetMatrix();

        Galaxy.Builder builder = new Galaxy.Builder(byteGalaxy.getTitle(), null);
        byte start = PlanetUtils.GAME_STATE_IN_BYTES_SIZE + PlanetUtils.GAME_STEP_IN_BYTES_SIZE;
        int planetsCount = 0;
        for (int i = start; i < planets.length; i += PlanetUtils.PLANET_IN_BYTES_SIZE) {
            Planet planet = new Planet();
            planet.setId(PlanetUtils.getPlanetIdByIndex(planets, i));
            planet.setType(PlanetUtils.getPlanetTypeByIndex(planets, i));
            planet.setOwnerId(PlanetUtils.getOwnerIdByIndex(planets, i));
            planet.setUnits(PlanetUtils.getUnitsCountByIndex(planets, i));
            //planet.setX(); //TODO
            //planet.setY(); //TODO

            builder.addPlanet(planet, PlanetUtils.isPlanetStartPoint(planets, i));
            planetsCount++;
        }

        // обходим только половину связей, так как двунаправленные
        for (short i = 0; i < planetsCount; i++) {
            for (short j = (short) (i + 1); j < planetsCount; j++) {
                if (PlanetMatrixUtils.checkEdgeInMatrix(planetMatrix, planetsCount, i, j)) {
                    builder.addEdge(i, j);
                }
            }
        }

        return builder.build();
    }

    public void resetPlanetsState(ByteGalaxy byteGalaxy, Galaxy galaxy, boolean isFirstInit) {
        byte[] planets = byteGalaxy.getPlanets();
        BitSet planetMatrix = byteGalaxy.getPlanetMatrix();

        int currentStepNumber = 0;
        planets[0] = GameStateUtils.createState(GameStateEnum.NOT_STARTED, GameResultEnum.UNKNOWN);
        ByteUtils.fillByteArrayFromUnsignedInt(planets, 1, 4, currentStepNumber);

        short i = 0; // id планеты
        for (Planet planet : galaxy.getPlanets()) {
            Collection<Planet> startPoints = galaxy.getStartPoints();
            // заполнение единого массива планет каждой отдельной планетой
            byte[] planetBytes = PlanetUtils.toByteArrayViewPlanet(i, planet, startPoints.contains(planet));
            int count = PlanetUtils.GAME_STATE_IN_BYTES_SIZE + PlanetUtils.GAME_STEP_IN_BYTES_SIZE +
                    PlanetUtils.PLANET_IN_BYTES_SIZE * i;
            for (byte planetByte : planetBytes) {
                planets[count] = planetByte;
                count++;
            }
            i++;
        }
        // установка/заполнение матрицы смежности
        if (isFirstInit) {
            for (Galaxy.Edge edge : galaxy.getEdges()) {
                PlanetMatrixUtils.createEdgeInMatrix(planetMatrix, planets.length, edge.getFrom().getId(), edge.getTo().getId());
            }
        }
    }

}
