package com.github.br.starmarines.main.view.widgets.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.br.starmarines.main.controller.impl.GameEngineController;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.main.view.wm.impl.MenuGameEngineWM;
import com.github.br.starmarines.map.service.api.MapValidationException;

public class MenuGameEngineWidget extends JPanel implements
		IWidget<MenuGameEngineWM, GameEngineController> {

	private GameEngineController controller;
	private MenuGameEngineWM wm;

	private JButton btnStart;
	private JButton btnStop;

	public MenuGameEngineWidget() {
		// панель кнопок
		setLayout(new FlowLayout());
		btnStart = new JButton("Start Game");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO открыть модальное окно с выбором
				List<String> titles = controller.getAllTitles();
				JDialog dialog = new ChooseMapDialog(titles);
				dialog.setVisible(true);
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

	private class ChooseMapDialog extends JDialog {

		private JList<String> list;
		private JButton ok;

		public ChooseMapDialog(List<String> titles) {
			super();
			
			list = new JList<String>(titles.toArray(new String[0]));
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list.setLayoutOrientation(JList.VERTICAL);
			list.setVisibleRowCount(-1);

			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setPreferredSize(new Dimension(250, 80));

			ok = new JButton("ok");
			ok.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					setVisible(false);
					//TODO обработка выбора мапы
					int index = list.getSelectedIndex();
					String mapTitle = list.getModel().getElementAt(index);
					
					setVisible(false);
					ChooseMapDialog.this.dispatchEvent(new WindowEvent(
							ChooseMapDialog.this, WindowEvent.WINDOW_CLOSING));
					try {
						MenuGameEngineWidget.this.startGame(mapTitle); // FIXME
					} catch (MapValidationException e) {
						//TODO: Обработать
						JOptionPane.showMessageDialog(new JFrame(),
							    e.getMessage(),
							    "Ошибка игравой карты",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			JPanel panel = new JPanel();
			panel.add(listScroller);
			panel.add(ok);
			add(panel);
			setSize(260, 160);
		}
	}

	public void startGame(String galaxy) throws MapValidationException{
		Galaxy g = controller.getMap(galaxy);
		controller.start(g);
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
