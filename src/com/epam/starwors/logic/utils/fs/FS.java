package com.epam.starwors.logic.utils.fs;

import com.epam.starwors.galaxy.Planet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public abstract class FS {

    protected ICriteria criteria;

    protected Map<Planet, List<Planet>> paths = new HashMap<Planet, List<Planet>>();

    public FS(ICriteria criteria){
        this.criteria = criteria;
    }

    /**
     *
     * @param startPoint - начальная планета
     * @return возвращает карту "цель-путь к цели"
     */
    public Map<Planet, List<Planet>> find(Planet startPoint){
        Map<Planet, List<Planet>> result = new HashMap<Planet, List<Planet>>();
        List<Planet> targets = findTarget(startPoint);
        for(Planet target : targets){
            result.put(target, getPath(startPoint, target));
        }

        return result;
    }

    protected abstract List<Planet> findTarget(Planet startPoint);

    protected boolean isFind(Planet planet){
        return criteria.isSuccess(planet);
    }

    private List<Planet> getPath(Planet startPoint, Planet target){
        List<Planet> path = new LinkedList<Planet>();
        while(!target.equals(startPoint)){
            path.add(target);
            target = target.getParent();
        }

        return path;
    }


}