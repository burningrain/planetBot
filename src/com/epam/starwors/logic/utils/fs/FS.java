package com.epam.starwors.logic.utils.fs;


import com.epam.starwors.galaxy.Planet;
import java.util.List;

public abstract class  FS {

    ICriteria criteria;


    FS(){

    }

    public abstract List<Planet> getPath();

}
