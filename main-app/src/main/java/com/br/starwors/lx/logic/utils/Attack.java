package com.br.starwors.lx.logic.utils;

import java.util.*;

import com.br.game.api.galaxy.Move;
import com.br.game.api.galaxy.Planet;

public class Attack {

    private AttackCriteria criteria;


    public Attack(AttackCriteria criteria){
        this.criteria = criteria;
    }


    /**
     *
     * @param target планета, на которую надо нанести удар
     */
    public List<Move> attack(Planet target){
        bfs(target);

        List<Move> moves = new LinkedList<Move>();
        Queue<Planet> nodes = new LinkedList<Planet>();
        nodes.add(target);
        while(!nodes.isEmpty()){
            Planet parent = nodes.remove();
            for(Planet child : parent.getChildren()){
                moves.add(new Move(child, parent, criteria.giveUnits(child, parent)));
                nodes.add(child);
            }

        }

        return moves;
    }


    private void bfs(Planet target){
        Set<Planet> visited = new HashSet<Planet>();
        Queue<Planet> open = new LinkedList<Planet>();

        open.add(target);
        visited.add(target);

        while(!open.isEmpty()){
            Planet node = open.remove();
            List<Planet> children = new LinkedList<Planet>();
            for(Planet neighbour : node.getNeighbours()){
                if(PlanetUtils.isMyPlanet(neighbour) && !visited.contains(neighbour)){
                    open.add(neighbour);
                    visited.add(neighbour);

                    neighbour.setParent(node);
                    children.add(neighbour);
                }
            }
            node.setChildren(children);
        }
    }

}
