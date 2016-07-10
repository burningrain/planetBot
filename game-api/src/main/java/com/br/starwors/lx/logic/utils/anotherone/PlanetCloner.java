package com.br.starwors.lx.logic.utils.anotherone;



import java.util.*;

import com.github.br.starmarines.game.api.galaxy.Planet;


public class PlanetCloner {


    public static Collection<Planet> clonePlanets(Collection<Planet> planets){
        Map<String, Planet> map = new HashMap<String, Planet>();
        Collection<Planet> clones = new ArrayList<Planet>(planets.size());

        // копируем все планеты без потомков
        for(Planet planet : planets){
            Planet cl = lazyCopyPlanet(planet);
            clones.add(cl);
            map.put(cl.getId(), cl);
        }

        // обходим потомков
        // сделано в 2 цикла, так как набор копированных планет УЖЕ должен быть для ссылочной целостности
        for(Planet planet : planets){
            Planet clone = map.get(planet.getId());
            List<Planet> neighbours = planet.getNeighbours();
            if(neighbours != null && !neighbours.isEmpty()){
                for(Planet neighbour : neighbours){
                    clone.addNeighbour(map.get(neighbour.getId()));
                }
            }
        }

        return clones;
    }


    public static Planet lazyCopyPlanet(Planet planet){
        Planet clone = new Planet(planet.getId());
        clone.setType(planet.getType());
        clone.setUnits(planet.getUnits());
        clone.setOwner(planet.getOwner());

        return clone;
    }


}
