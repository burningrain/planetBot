package main.starwors.logic.utils.fs;


import main.starwors.galaxy.Planet;
import main.starwors.logic.utils.PlanetCloner;
import main.starwors.logic.utils.PlanetUtils;

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
        if(neighbours.size() != 0){
            //Collection<Planet> neighboursClones = PlanetCloner.clonePlanets(neighbours);
//            for (Planet neighbour : neighbours) {
//                levelStates.remove(neighbour);
//                levelStates.addAll(getNeighbours(neighbour));
//            }
            List<Planet> tempLevelStates = new LinkedList<Planet>();
            Iterator<Planet> iterator = neighbours.iterator();
            while(iterator.hasNext()){
                Planet neighbour = iterator.next();
                tempLevelStates.addAll(getNeighbours(neighbour));
                iterator.remove();

            }
            levelStates = tempLevelStates;
            if(!levelStates.isEmpty()){
                makeThree(levelStates);
            }
        }
    }

    private List<Planet> getNeighbours(Planet planet){
        visitedPlanets.add(planet);
        if(criteria.isSuccess(planet)){
            targets.add(planet);
        }

        List<Planet> neighbours = planet.getNeighbours();
        Iterator<Planet> iterator = neighbours.iterator();
        while(iterator.hasNext()){
            Planet neighbour = iterator.next();

            if(!visitedPlanets.contains(neighbour)){
                visitedPlanets.add(neighbour);
                neighbour.setParent(planet);
            } else{
                iterator.remove();
            }

            if(criteria.isRemoveFromPath(neighbour)){
                iterator.remove();
            }
        }

        return neighbours;
    }




}
