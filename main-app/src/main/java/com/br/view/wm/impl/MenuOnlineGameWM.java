package com.br.view.wm.impl;

import com.br.model.objects.IModel;
import com.br.model.objects.IModelListener;
import com.br.model.objects.inner.IStepInfo;
import com.br.view.model.CommonVM;
import com.br.view.wm.AbstractWidgetModel;

public class MenuOnlineGameWM extends AbstractWidgetModel {
	
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
