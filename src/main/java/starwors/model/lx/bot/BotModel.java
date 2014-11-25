package starwors.model.lx.bot;


import starwors.model.lx.galaxy.Action;
import starwors.model.lx.logic.GameInfo;

import java.util.*;

public class BotModel {

    private List<IBotModelListener> listeners;
    private GameCore serverGameCore;
    private ReplayCore replayCore;

    private Response lastStep = null;
    private Response currentStep = null;
    private Collection<Action> currentActions = null;

    private GameInfo gameInfo;


    public BotModel(){
        listeners = new LinkedList<IBotModelListener>();
        gameInfo = new GameInfo();
        this.serverGameCore = new GameCore(this);
        this.replayCore = new ReplayCore(this);
    }

    void updateResponse(Response response) {
        this.currentActions = null; // FIXME неочевидно.  сделано, чтобы увидеть изменения хода бота

        lastStep = currentStep;
        currentStep = response;

        if(currentStep != null && currentStep.getPlanets() != null){
            this.updateListenersInfo();
        }
    }

    void updateCurrentActions(Collection<Action> actions) {
        this.currentActions = actions;

        if(currentActions != null){
            this.updateListenersInfo();
        }
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void start(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                serverGameCore.start();
            }
        });
        t.start();
    }

    public void stop(){
        serverGameCore.stop();
    }

    public void startReply(){
        replayCore.startReplay();
    }


    // OBSERVER METHODS

    public void addListener(IBotModelListener listener){
        listeners.add(listener);
    }

    public void removeListener(IBotModelListener listener){
        listeners.remove(listener);
    }

    public void updateListenersInfo(){
        for(IBotModelListener listener : listeners){
            listener.update(this);
        }
    }

    /////////////////////////////////

    public Response getCurrentStep() {
        return currentStep;
    }

    public Response getLastStep() {
        return lastStep;
    }

    public Collection<Action> getCurrentActions() {
        return currentActions;
    }

}
