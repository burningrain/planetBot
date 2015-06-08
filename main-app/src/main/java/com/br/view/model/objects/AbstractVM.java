package com.br.view.model.objects;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.model.objects.IModel;
import com.br.view.model.objects.impl.ColorVM;



public abstract class AbstractVM<VM> implements IObjectVM<VM> {
	private Logger logger = LoggerFactory.getLogger(AbstractVM.class); 
	
	private List<IObjectVMListener<VM>> listeners = new LinkedList<>();
	
	
	public abstract void setModelListeners(IModel model);
	
	public void addListener(IObjectVMListener<VM> listener){
		listeners.add(listener);
	}
    
	public void removeListener(IObjectVMListener<VM> listener){
		listeners.remove(listener);
	}

	public void updateListeners(){
    	for(IObjectVMListener<VM> listener : listeners){
    		listener.update((VM) this);    	 //FIXME нисходящее приведение типов, надо исправить	
    	}
    }
    

}
