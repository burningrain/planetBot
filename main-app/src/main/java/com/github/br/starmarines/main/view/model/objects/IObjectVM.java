package com.github.br.starmarines.main.view.model.objects;

import com.github.br.starmarines.main.model.objects.IModel;

public interface IObjectVM<M> {

	void addListener(IObjectVMListener<M> listener);
    
	void removeListener(IObjectVMListener<M> listener);

    void updateListeners();
    void setModelListeners(IModel model);
	
}
