package com.github.br.starmarines.main.model.objects.inner.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.starwors.lx.galaxy.Action;
import com.github.br.starmarines.main.model.objects.IModelListener;
import com.github.br.starmarines.main.model.objects.inner.IStepInfo;
import com.github.br.starmarines.main.model.services.inner.beans.Response;

@Component
@Instantiate
@Provides
public class StepInfo implements IStepInfo {
	
	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void v(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " start");
	}
	
	@Invalidate
	private void stop(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getSimpleName() + " stop");
	}
	
	
    private Response currentStep;
    private Collection<Action> currentActions;
    
    private List<IModelListener<IStepInfo>> listeners = new LinkedList<IModelListener<IStepInfo>>();     
    
    public void updateResponse(Response response) {
        //this.currentActions = null; // FIXME неочевидно.  сделано, чтобы увидеть изменения хода бота
        
        currentStep = response;

        //if(currentStep != null && currentStep.getPlanets() != null){
            this.updateListeners();
        //}
    }

    public void updateCurrentActions(Collection<Action> actions) {
        this.currentActions = actions;

        //if(currentActions != null){
            this.updateListeners();
        //}
    }
    
  
    @Override
	public Response getCurrentStep() {
        return currentStep;
    }    

    
    @Override
	public Collection<Action> getCurrentActions() {
        return currentActions;
    }
    
    
    // OBSERVER'S METHODS
   
    @Override
	public void addListener(IModelListener<IStepInfo> listener){
        listeners.add(listener);
    }

    
    @Override
	public void removeListener(IModelListener<IStepInfo> listener){
        listeners.remove(listener);
    }

    @Override
    public void updateListeners(){
        for(IModelListener<IStepInfo> listener : listeners){
            listener.update(this);
        }
    }
    /////////////////////////////////
	
}
