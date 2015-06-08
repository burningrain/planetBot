package com.br.starwors.lx.logic.utils.fs;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.br.game.api.galaxy.Planet;


public abstract class FS {

    protected ICriteria criteria;

    public FS(ICriteria criteria){
        this.criteria = criteria;
    }

    /**
     * @param startPoint - начальная планета
     * @return возвращает карту "цель : путь к цели"
     */
    public Map<Planet, List<Planet>> find(Planet startPoint){
        Map<Planet, List<Planet>> result = new HashMap<Planet, List<Planet>>();
        List<Planet> targets = findTarget(startPoint);
        for(Planet target : targets){
            result.put(target, getPath(startPoint, target));
        }

        return result;
    }

    /**
     * возвращает список целей
     * @param startPoint
     * @return
     */
    protected abstract List<Planet> findTarget(Planet startPoint);

    private List<Planet> getPath(Planet startPoint, Planet target){
        List<Planet> path = new LinkedList<Planet>();
        while(!target.equals(startPoint)){
            path.add(target);
            target = target.getParent();
        }

        return path;
    }


}
