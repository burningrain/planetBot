package com.github.br.starmarines.main.model.objects;

public interface IModelObject<M> {

	void addListener(IModelListener<M> listener);
    
	void removeListener(IModelListener<M> listener);

    void updateListeners();
	
}
