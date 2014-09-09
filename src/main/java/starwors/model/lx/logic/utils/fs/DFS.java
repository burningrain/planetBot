package starwors.model.lx.logic.utils.fs;


import starwors.model.lx.galaxy.Planet;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DFS extends FS {

    private Set<Planet> visitedPlanets = new HashSet<Planet>();
    private List<Planet> targets = new LinkedList<Planet>();


    public DFS(ICriteria criteria) {
        super(criteria);
    }


    @Override
    protected List<Planet> findTarget(Planet startPoint) {
        makeThree(startPoint);
        return targets;
    }

    private void makeThree(Planet planet) {
        visitedPlanets.add(planet);

        if (criteria.isSuccess(planet)) {
            targets.add(planet);
        } else {
            List<Planet> planets = planet.getNeighbours();
            for (Planet pl : planets) {
                if (!visitedPlanets.contains(pl)) {
                    pl.setParent(planet);
                    makeThree(pl);
                }
            }
        }

    }

}
