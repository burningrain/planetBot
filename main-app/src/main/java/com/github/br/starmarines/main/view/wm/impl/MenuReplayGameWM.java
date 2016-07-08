package com.github.br.starmarines.main.view.wm.impl;

import com.github.br.starmarines.main.model.objects.IModel;
import com.github.br.starmarines.main.model.objects.IModelListener;
import com.github.br.starmarines.main.model.objects.inner.IStepInfo;
import com.github.br.starmarines.main.view.model.CommonVM;
import com.github.br.starmarines.main.view.wm.AbstractWidgetModel;

public class MenuReplayGameWM extends AbstractWidgetModel {
	
	private boolean active;		


	private IModelListener<IStepInfo> stepListener = new IModelListener<IStepInfo>() {
		@Override
		public void update(IStepInfo model) {
			active = model.getCurrentStep().isGameRunning();			
		}
	};

	@Override
	public void setModelListeners(IModel model) {
		model.getStep().addListener(stepListener);			
	}

	@Override
	public void setViewModelListeners(CommonVM model) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isActive() {
		return active;
	}

}
