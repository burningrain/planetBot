package com.epam.starwors.logic.utils.fs;


import com.epam.starwors.galaxy.Planet;

public interface ICriteria {

    boolean isRemoveFromPath(Planet planet);

    boolean isSuccess(Planet planet);

}
