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



        return moves;
    }

    private void addMove(List<Move> moves, Planet planet, Planet target, int units){
        moves.add(new Move(planet, target, units));
    }

}
