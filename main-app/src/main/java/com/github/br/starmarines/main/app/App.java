package com.github.br.starmarines.main.app;


import java.lang.reflect.InvocationTargetException;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.controller.impl.GameEngineController;
import com.github.br.starmarines.main.controller.impl.OnlineGameController;
import com.github.br.starmarines.main.controller.impl.ReplayController;
import com.github.br.starmarines.main.model.logic.Logic;
import com.github.br.starmarines.main.model.objects.IModel;
import com.github.br.starmarines.main.view.View;
import com.github.br.starmarines.main.view.model.CommonVM;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.main.view.wm.AbstractWidgetModel;
import com.github.br.starmarines.main.view.wm.impl.ChartWM;
import com.github.br.starmarines.main.view.wm.impl.GraphWM;
import com.github.br.starmarines.main.view.wm.impl.MenuGameEngineWM;
import com.github.br.starmarines.main.view.wm.impl.MenuOnlineGameWM;
import com.github.br.starmarines.main.view.wm.impl.MenuReplayGameWM;



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
		
		IWidget<MenuGameEngineWM, GameEngineController> menuGameEngineWidget = view.getMenuGameEngineWidget();
		AbstractWidgetModel menuGameEngineWM = new MenuGameEngineWM();
		menuGameEngineWM.setModelListeners(model);
		menuGameEngineWM.setWidget(menuGameEngineWidget);
		menuGameEngineWM.setViewModelListeners(commonVM);
		
		IController<ReplayController> replyController = new ReplayController();
		replyController.setLogic(logic);
		replyController.setWidget(menuReplayWidget);
		
		IController<OnlineGameController> onController = new OnlineGameController();
		onController.setLogic(logic);
		onController.setWidget(menuOnlineWidget);	
		
		IController<GameEngineController> engineController = new GameEngineController();
		engineController.setLogic(logic);
		engineController.setWidget(menuGameEngineWidget);		
	}	
	
	
	@Invalidate
	public void stop(){
		
	}
	
}
