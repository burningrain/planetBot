package starwors.model.lx.logic.utils;

import starwors.model.lx.galaxy.Planet;

public final class PlanetUtils {

    private static final String FREE_PLANET_OWNER_NAME = "";
    static String MY_PLANET_OWNER_NAME;

    private PlanetUtils() {
    }

    public static boolean isMyPlanet(Planet planet) {
        return MY_PLANET_OWNER_NAME.equals(planet.getOwner());
    }

    public static boolean isFreePlanet(Planet planet) {
        return FREE_PLANET_OWNER_NAME.equals(planet.getOwner());
    }

    public static boolean isEnemyPlanet(Planet planet) {
        return !isMyPlanet(planet) && !isFreePlanet(planet);
    }

}
