package com.github.br.starmarines.main.view.widgets.menu;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.br.starmarines.main.controller.impl.ReplayController;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.main.view.wm.impl.MenuReplayGameWM;

public class MenuReplayGameWidget extends JPanel implements
IWidget<MenuReplayGameWM, ReplayController> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MenuReplayGameWM wm;
	private ReplayController controller;
	
	
	
	private JButton btnStart;
	
	
	public MenuReplayGameWidget(){
		// панель кнопок
		setLayout(new FlowLayout());
        btnStart = new JButton("Reply");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	controller.start();
            }
        });
        this.add(btnStart);
	}

	@Override
	public void setModel(MenuReplayGameWM wm) {
		this.wm = wm;		
	}

	@Override
	public MenuReplayGameWM getModel() {		
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
	public void setController(ReplayController controller) {
		this.controller = controller;		
	}

}
