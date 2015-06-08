package com.br.starwors.lx.logic.utils.fs;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.br.game.api.galaxy.Planet;

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

        if (criteria.isTarget(planet)) {
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
