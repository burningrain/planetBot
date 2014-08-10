package starwors.model.lx.logic.strategies.tasks;


import starwors.model.lx.galaxy.Move;
import starwors.model.lx.galaxy.Planet;
import starwors.model.lx.galaxy.PlanetType;
import starwors.model.lx.logic.utils.PlanetUtils;

import java.util.LinkedList;
import java.util.List;

public class CPlanetTask implements ITask {


    @Override
    public boolean canUse(Planet planet) {
        return PlanetUtils.isMyPlanet(planet) && PlanetType.TYPE_C.equals(planet.getType());
    }

    @Override
    public List<Move> execute(Planet planet) {
        List<Move> moves = new LinkedList<Move>();
        int units = planet.getUnits();

        Planet dPlanet = null;

//        if(units < PlanetType.TYPE_C.getLimit()*1.5){
//            return new LinkedList<Move>();
//        }

        List<Planet> enemyPlanetsC = new LinkedList<Planet>();
        List<Planet> myPlanetsC = new LinkedList<Planet>();

        List<Planet> neighbours = planet.getNeighbours();
        for(Planet neighbour : neighbours){
            if(PlanetType.TYPE_C.equals(neighbour.getType()) && !PlanetUtils.isMyPlanet(neighbour)){
                enemyPlanetsC.add(neighbour);
            } else if(PlanetType.TYPE_C.equals(neighbour.getType()) && PlanetUtils.isMyPlanet(neighbour)){
                myPlanetsC.add(neighbour);
            }

            if(PlanetType.TYPE_D.equals(neighbour.getType())){
                if(PlanetUtils.isEnemyPlanet(neighbour)){
                    dPlanet = neighbour;
                    break;
                }
            }
        }

        if(dPlanet != null){
            if(PlanetUtils.canItakePlanet(planet, dPlanet)){
                int attack = planet.getUnits();
                moves.add(new Move(planet, dPlanet, attack));
            } else{
                // ничего не делаем, копим
            }

        } else if(enemyPlanetsC.size() != 0){
            Planet lowPlanet = PlanetUtils.getPlanetWithMinUnitsCount(enemyPlanetsC);
            if(PlanetUtils.canIHoldPlanet(planet, lowPlanet)){
                int attack = PlanetUtils.getUnitCountForHoldPlanet(lowPlanet);
                moves.add(new Move(planet, lowPlanet, attack));
            } else{

            }

//            for(Planet pl : enemyPlanetsC){
//                int cook = Math.round(PlanetType.TYPE_C.getLimit()/enemyPlanetsC.size());
//                pl.setUnits(pl.getUnits() + cook);
//                moves.add(new Move(planet, pl, cook));
//            }
        } else{
            // все планеты наши
            for(Planet myPl : myPlanetsC){
                int cook = Math.round(PlanetType.TYPE_C.getLimit()/myPlanetsC.size());
                moves.add(new Move(planet, myPl, cook));
            }
        }

        return moves;
    }
}
