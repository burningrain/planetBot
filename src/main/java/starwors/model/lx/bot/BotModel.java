package starwors.model.lx.bot;


import java.util.LinkedList;
import java.util.List;

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

        this.update();
    }




    public void start(){
        dataService.start();
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

    public void update(){
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
