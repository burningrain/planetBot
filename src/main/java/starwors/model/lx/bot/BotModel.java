package starwors.model.lx.bot;


import starwors.model.lx.logic.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BotModel implements IGameDataServiceListener {

    private List<IBotModelListener> listeners;
    private GameDataService dataService;


    private Response lastStep = null;
    private Response currentStep = null;


    public BotModel(){
        listeners = new LinkedList<IBotModelListener>();
        dataService = new GameDataService();
        dataService.addListener(this);
    }


    @Override
    public void update(Response response) {
        lastStep = currentStep;
        currentStep = response;

        if(currentStep != null && currentStep.getPlanets() != null){
            this.updateListenersInfo();
        }
    }


    public Set<String> getPlayers(){
        return Game.getPlayers();
    }


    public void start(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                dataService.start();
            }
        });
        t.start();
    }

    public void stop(){
        dataService.stop();
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

}
