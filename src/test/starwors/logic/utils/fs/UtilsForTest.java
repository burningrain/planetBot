package test.starwors.logic.utils.fs;


import main.starwors.galaxy.Planet;
import main.starwors.galaxy.PlanetType;

public final class UtilsForTest {


    public static Planet getPlanet(String id, String owner, int units, PlanetType type){
        Planet planet = new Planet(id);
        planet.setType(type);
        planet.setUnits(units);
        planet.setOwner(owner);

        return planet;
    }



}
