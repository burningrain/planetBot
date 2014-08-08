package com.epam.starwors.logic.utils.fs;


import com.epam.starwors.galaxy.Planet;

import java.util.*;

public class BFS extends FS {

    private Set<Planet> visitedPlanets = new HashSet<Planet>();

    private List<Planet> targets = new LinkedList<Planet>();
    private List<Planet> levelStates = new LinkedList<Planet>();

    public BFS(ICriteria targetCriteria) {
        super(targetCriteria);
    }


    @Override
    protected List<Planet> findTarget(Planet startPoint) {
        visitedPlanets.add(startPoint);
        makeThree(getNeighbours(startPoint));

        return targets;
    }

    private void makeThree(List<Planet> neighbours){
        for(Planet neighbour : neighbours){
            levelStates.remove(neighbour);
            levelStates.addAll(getNeighbours(neighbour));
        }
        makeThree(levelStates);

    }

    private List<Planet> getNeighbours(Planet planet){
        visitedPlanets.add(planet);
        if(criteria.isSuccess(planet)){
            targets.add(planet);
        }

        List<Planet> neighbours = planet.getNeighbours();
        int length = neighbours.size() - 1;
        for(int i = length; i >= 0; i--){
            if(!visitedPlanets.contains(neighbours.get(i))){
                visitedPlanets.add(neighbours.get(i));
                neighbours.get(i).setParent(planet);
            } else{
                neighbours.remove(i);
            }
        }

        return neighbours;
    }




}
