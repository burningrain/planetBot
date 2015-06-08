package com.br.app;


import java.lang.reflect.InvocationTargetException;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.controller.IController;
import com.br.controller.impl.OnlineGameController;
import com.br.controller.impl.ReplayController;
import com.br.model.logic.Logic;
import com.br.model.objects.IModel;
import com.br.model.objects.Model;
import com.br.view.View;
import com.br.view.model.CommonVM;
import com.br.view.widgets.IWidget;
import com.br.view.wm.AbstractWidgetModel;
import com.br.view.wm.impl.ChartWM;
import com.br.view.wm.impl.GraphWM;
import com.br.view.wm.impl.MenuOnlineGameWM;
import com.br.view.wm.impl.MenuReplayGameWM;



@Component
@Instantiate
public class App implements IApp {	
	
	@Requires
	private IModel model;
	
	@Requires
	private Logic logic;
	
	@Requires(optional = true)
	private LogService logService;

	
	
	@Validate
	public void start() {
		
		View view = new View();
		try {
			view.init();
		} catch (InvocationTargetException | InterruptedException e) {
			logService.log(LogService.LOG_INFO, "view does not initialized");
		}
		CommonVM commonVM = new CommonVM();
		commonVM.getColorVM().setModelListeners(model);
		
		IWidget<ChartWM, IController> chartWidget = view.getChartWidget();
		ChartWM chartWM = new ChartWM();
		chartWM.setModelListeners(model);
		chartWM.setWidget(chartWidget);
		chartWidget.setModel(chartWM);
		chartWM.setViewModelListeners(commonVM);
		
		IWidget<GraphWM, IController> graphWidget = view.getGraphWidget();
		GraphWM graphWM = new GraphWM();
		graphWM.setModelListeners(model);
		graphWM.setWidget(graphWidget);
		graphWidget.setModel(graphWM);
		graphWM.setViewModelListeners(commonVM);
		
		IWidget<MenuOnlineGameWM, OnlineGameController> menuOnlineWidget = view.getMenuOnlineGameWidget();
		AbstractWidgetModel menuOnlineGameWM = new MenuOnlineGameWM();
		menuOnlineGameWM.setModelListeners(model);
		menuOnlineGameWM.setWidget(menuOnlineWidget);
		menuOnlineGameWM.setViewModelListeners(commonVM);
		
		IWidget<MenuReplayGameWM, ReplayController> menuReplayWidget = view.getMenuReplayGameWidget();
		AbstractWidgetModel menuReplayGameWM = new MenuReplayGameWM();
		menuReplayGameWM.setModelListeners(model);
		menuReplayGameWM.setWidget(menuReplayWidget);
		menuReplayGameWM.setViewModelListeners(commonVM);
		
		IController<ReplayController> replyController = new ReplayController();
		replyController.setLogic(logic);
		replyController.setWidget(menuReplayWidget);
		
		IController<OnlineGameController> onController = new OnlineGameController();
		onController.setLogic(logic);
		onController.setWidget(menuOnlineWidget);		
		
	}	
	
	
	@Invalidate
	public void stop(){
		
	}
	
}
