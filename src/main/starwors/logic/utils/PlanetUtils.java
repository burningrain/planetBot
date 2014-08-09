package main.starwors.logic.utils;

import main.starwors.bot.Logic;
import main.starwors.galaxy.Planet;

import java.util.*;

public final class PlanetUtils {

    private PlanetUtils() {
    }

    public static boolean isMyBase(Planet planet) {
        return Logic.myBasePlanet != null ? Logic.myBasePlanet.getId().equals(
                planet.getId())
                && Logic.botName.equals(planet.getOwner()) :
                Logic.botName.equals(planet.getOwner());
    }

    public static boolean isMyPlanet(Planet planet) {
        return Logic.botName.equals(planet.getOwner());
    }

    public static boolean isFreePlanet(Planet planet) {
        return Logic.FREE_PLANET.equals(planet.getOwner());
    }

    public static boolean isEnemyPlanet(Planet planet) {
        return !isMyPlanet(planet) && !isFreePlanet(planet);
    }

    public static boolean isEnemyBase(Planet planet) {
        return Logic.enemyBasePlanet != null ?
                Logic.enemyBasePlanet.getId().equals(planet.getId()) :
                Logic.enemyBasePlanet != null
                        && !Logic.botName.equals(planet.getOwner())
                        && !Logic.FREE_PLANET.equals(planet.getOwner());
    }

    public static Planet getPlanetWithMinUnitsCount(List<Planet> planets) {
        Collections.sort(planets, new Comparator<Planet>() {
            @Override
            public int compare(Planet o1, Planet o2) {
                return o1.getUnits() - o2.getUnits();
            }
        });

        return planets.get(planets.size() - 1);
    }

    public static int getEnemyUnitsCountAround(Planet planet) {
        List<Planet> planets = planet.getNeighbours();
        List<Planet> enemyPlanets = new LinkedList<Planet>();
        for (Planet p : planets) {
            if (isMyPlanet(p)) {
                continue;
            }
            enemyPlanets.add(p);
        }

        int count = 0;
        for (Planet enPl : enemyPlanets) {
            count += enPl.getUnits();
        }
        return count;
    }

    public static boolean canItakePlanet(Planet myPlanet, Planet planet) {
        return myPlanet.getUnits() * (100 + myPlanet.getType().getIncrement()) / 100 >= planet.getUnits() * (100 + planet.getType().getIncrement()) / 100;
    }

    public static int getUnitCountForTakePlanet(Planet planet) {
        //return 1 + Math.round(myPlanet.getUnits()*(100 + myPlanet.getType().getIncrement())/100 - planet.getUnits()*(100 + planet.getType().getIncrement())/100);
        return 1 + Double.valueOf(Math.floor(planet.getUnits() * (100 + planet.getType().getIncrement()) / 100)).intValue();
    }

    public static boolean canIHoldPlanet(Planet myPlanet, Planet planet) {
        return myPlanet.getUnits() * (100 + myPlanet.getType().getIncrement()) / 100 >= planet.getUnits() * (100 + planet.getType().getIncrement()) / 100 + getEnemyUnitsCountAround(planet);
    }

    public static int getUnitCountForHoldPlanet(Planet planet) {
        return planet.getUnits() * (100 + planet.getType().getIncrement()) / 100 + getEnemyUnitsCountAround(planet);
    }


}
