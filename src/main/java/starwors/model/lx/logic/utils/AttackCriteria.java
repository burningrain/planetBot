package starwors.model.lx.logic.utils;


import starwors.model.lx.galaxy.Planet;

public interface AttackCriteria {


    /**
     * @param from собственная планета
     * @param to планета
     * @return число юнитов, которое планета может отдать (в абсолютной величине)
     */
    int giveUnits(Planet from, Planet to);




}
