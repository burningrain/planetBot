package com.github.br.starmarines.main.view.widgets.menu;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.controller.impl.OnlineGameController;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.main.view.wm.impl.MenuOnlineGameWM;

public class MenuOnlineGameWidget extends JPanel implements
		IWidget<MenuOnlineGameWM, OnlineGameController> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuOnlineGameWM wm;
	private OnlineGameController controller;

	private JButton btnStart;

	public MenuOnlineGameWidget() {
		// панель кнопок
		setLayout(new FlowLayout());
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.start();
			}
		});
		this.add(btnStart);
	}

	@Override
	public void setModel(MenuOnlineGameWM wm) {
		this.wm = wm;
	}

	@Override
	public MenuOnlineGameWM getModel() {
		return wm;
	}

	@Override
	public void update() {
		if(wm.isActive()){
			btnStart.setEnabled(false);			
		} else{
			btnStart.setEnabled(true);	
		}
	}

	@Override
	public void setController(OnlineGameController controller) {
		this.controller = controller;		
	}

}
