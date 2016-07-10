package com.github.br.starmarines.main.view.widgets.menu;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.br.starmarines.main.controller.impl.GameEngineController;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.main.view.wm.impl.MenuGameEngineWM;

public class MenuGameEngineWidget extends JPanel implements
		IWidget<MenuGameEngineWM, GameEngineController> {
	
	private GameEngineController controller;
	private MenuGameEngineWM wm;
	
	private JButton btnStart;
	private JButton btnStop;
	
	
	public MenuGameEngineWidget(){
		// панель кнопок
		setLayout(new FlowLayout());
        btnStart = new JButton("Start Game");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	controller.start();
            }
        });
        this.add(btnStart);
        
        btnStop = new JButton("Stop Game");
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	controller.stop();
            }
        });
        this.add(btnStop);
	}

	@Override
	public void setModel(MenuGameEngineWM wm) {
		this.wm = wm;		
	}

	@Override
	public MenuGameEngineWM getModel() {		
		return wm;
	}

	@Override
	public void setController(GameEngineController controller) {
		this.controller = controller;		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
