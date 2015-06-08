package com.br.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.br.view.widgets.chart.ChartWidget;
import com.br.view.widgets.graph.GraphWidget;
import com.br.view.widgets.menu.MenuOnlineGameWidget;
import com.br.view.widgets.menu.MenuReplayGameWidget;

public class View {
	
	public static int START_WIDTH = 730;
    public static int START_HEIGHT = 660;
    
    
    private GraphWidget graphWidget;
    private ChartWidget chartWidget;
    private MenuOnlineGameWidget menuOnlineGameWidget;
    private MenuReplayGameWidget menuReplayGameWidget;
    
    
    
    public GraphWidget getGraphWidget() {
		return graphWidget;
	}



	public ChartWidget getChartWidget() {
		return chartWidget;
	}



	public MenuOnlineGameWidget getMenuOnlineGameWidget() {
		return menuOnlineGameWidget;
	}



	public MenuReplayGameWidget getMenuReplayGameWidget() {
		return menuReplayGameWidget;
	}



	public void init() throws InvocationTargetException, InterruptedException {


        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {

                try {
                	UIManager.getDefaults().put("ClassLoader", 
                            this.getClass().getClassLoader());
                    UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel");
                } catch (Exception e) {
                    System.out.println("Substance Graphite failed to initialize");
                }

                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);

                final JFrame jf = new JFrame("StarMarines Bot");
                jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


                // определение размеров экрана
                Toolkit kit = Toolkit.getDefaultToolkit();
                Dimension screenSize = kit.getScreenSize();
                int screenSizeWidth = screenSize.width;
                int screenSizeHeigth = screenSize.height;

                // размещение фрейма по центру
                START_WIDTH = 730;
                START_HEIGHT = 660;

                jf.setSize(START_WIDTH, START_HEIGHT);
                jf.setLocation((screenSizeWidth - START_WIDTH) / 2, (screenSizeHeigth - START_HEIGHT) / 2);

                // установка пиктораммы
//TODO исправить
//                Image img = kit.getImage(this.getClass().getClassLoader().getResource("icon.gif"));
//                jf.setIconImage(img);               

                // верхняя панель с надписью
//                topLabel = new JLabel("STEP: ");
//                Font font = new Font("Verdana", Font.PLAIN, 16);
//                topLabel.setHorizontalAlignment(JLabel.CENTER);
//                topLabel.setFont(font);

                                
                graphWidget = new GraphWidget();
                chartWidget = new ChartWidget();
                menuOnlineGameWidget = new MenuOnlineGameWidget();
                menuReplayGameWidget = new MenuReplayGameWidget();
                // главная панель фрейма
                JPanel rootPanel = new JPanel();
                rootPanel.setLayout(new BorderLayout());
                rootPanel.add(graphWidget, BorderLayout.CENTER);
                rootPanel.add(menuOnlineGameWidget, BorderLayout.SOUTH);
                rootPanel.add(menuReplayGameWidget, BorderLayout.NORTH);
                rootPanel.add(chartWidget, BorderLayout.EAST);

                jf.getContentPane().add(rootPanel);
                //jf.pack();

                jf.setVisible(true);
            }
        });
    }
	

}
