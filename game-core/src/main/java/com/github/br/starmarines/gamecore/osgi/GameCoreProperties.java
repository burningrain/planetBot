package com.github.br.starmarines.gamecore.osgi;

import org.osgi.service.log.LogService;

import java.util.Dictionary;
import java.util.Map;


//@Component(managedservice = "gamecore")
//@Instantiate
//@Provides

//@ComponentPropertyType
public class GameCoreProperties {

    //@Property(name="game-loop-executors-count")
    private int gameLoopExecutorsCount;

    //@Property(name="computing-step-time-in-sec")
    private int computingStepTimeInSec;

    //@Property(name="games-max-capacity")
    private int gamesMaxCapacity;

    public GameCoreProperties(Map<String, String> properties) {
        this.gameLoopExecutorsCount = Integer.parseInt(properties.get("gamecore.game-loop-executors-count"));
        this.computingStepTimeInSec = Integer.parseInt(properties.get("gamecore.computing-step-time-in-sec"));
        this.gamesMaxCapacity = Integer.parseInt(properties.get("gamecore.games-max-capacity"));
    }
    
//    @Requires(optional = true)
//    private LogService logService;
//
//    @Updated
//    public void updated(Dictionary<?, ?> conf) {
//        if(logService != null) {
//            logService.log(LogService.LOG_INFO, "the gamecore configuraion was updated:\n" + conf);
//        }
//    }

    public int getGameLoopExecutorsCount() {
        return gameLoopExecutorsCount;
    }

    public int getComputingStepTimeInSec() {
        return computingStepTimeInSec;
    }

    public int getGamesMaxCapacity() {
        return gamesMaxCapacity;
    }

    @Override
    public String toString() {
        return "GameCoreProperties{" +
                "gameLoopExecutorsCount=" + gameLoopExecutorsCount +
                ", computingStepTimeInSec=" + computingStepTimeInSec +
                ", gamesMaxCapacity=" + gamesMaxCapacity +
                '}';
    }

}
