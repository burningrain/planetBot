package starwors.logic.utils.fs;


import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;

public final class UtilsForTest {


    public static Planet getPlanet(String id, String owner, int units, PlanetType type){
        Planet planet = new Planet(id);
        planet.setType(type);
        planet.setUnits(units);
        planet.setOwner(owner);

        return planet;
    }



}
