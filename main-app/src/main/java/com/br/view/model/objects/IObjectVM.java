package com.br.view.model.objects;

import com.br.model.objects.IModel;

public interface IObjectVM<M> {

	void addListener(IObjectVMListener<M> listener);
    
	void removeListener(IObjectVMListener<M> listener);

    void updateListeners();
    void setModelListeners(IModel model);
	
}
