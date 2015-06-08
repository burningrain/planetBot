package com.br.starwors.lx.logic.utils.fs;

import com.br.game.api.galaxy.Planet;



public interface ICriteria {

    boolean isRemoveFromPath(Planet planet);

    boolean isTarget(Planet planet);

}
