package main.starwors.logic.utils.fs;


import main.starwors.galaxy.Planet;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DFS extends FS {

    private Set<Planet> visitedPlanets = new HashSet<Planet>();
    private List<Planet> targets = new LinkedList<Planet>();
    private List<Planet> levelStates = new LinkedList<Planet>();


    public DFS(ICriteria criteria) {
        super(criteria);
    }


    @Override
    protected List<Planet> findTarget(Planet startPoint) {
        return targets;
    }



}
