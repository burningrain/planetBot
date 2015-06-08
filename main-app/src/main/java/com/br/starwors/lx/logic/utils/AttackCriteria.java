package com.br.starwors.lx.logic.utils;

import com.br.game.api.galaxy.Planet;



public interface AttackCriteria {


    /**
     * @param from собственная планета
     * @param to планета
     * @return число юнитов, которое планета может отдать (в абсолютной величине)
     */
    int giveUnits(Planet from, Planet to);




}
