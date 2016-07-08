package com.github.br.starmarines.main.view.widgets.chart;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.br.starmarines.main.controller.IController;
import com.github.br.starmarines.main.view.widgets.IWidget;
import com.github.br.starmarines.main.view.wm.impl.ChartWM;

public class ChartWidget extends JPanel implements
		IWidget<ChartWM, IController> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(ChartWidget.class);

	private ChartWM wm;

	private DefaultPieDataset myColoredPieChart;
	private JFreeChart myColoredChart;

	private JLabel rightLabel;

	@Override
	public void setModel(ChartWM wm) {
		this.wm = wm;
	}

	@Override
	public ChartWM getModel() {
		return wm;
	}

	@Override
	public void setController(IController controller) {
		// TODO Auto-generated method stub

	}

	public ChartWidget() {		
		
		rightLabel = new JLabel();
		Font rightFont = new Font("Verdana", Font.PLAIN, 12);
		rightLabel.setHorizontalAlignment(JLabel.LEFT);
		rightLabel.setVerticalAlignment(JLabel.TOP);
		rightLabel.setFont(rightFont);
		this.add(rightLabel);		
		
		
		myColoredPieChart = new DefaultPieDataset();
		myColoredChart = ChartFactory.createPieChart3D("", myColoredPieChart,
				false, false, false);
		myColoredChart.setBorderVisible(false);
		myColoredChart.setBackgroundPaint(new Color(0, 0, 0, 0)); // transparent
																	// black
		PiePlot configurator = (PiePlot) myColoredChart.getPlot();
		configurator.setBackgroundPaint(new Color(0, 0, 0, 0));
		configurator.setLabelGenerator(null);
		configurator.setOutlineVisible(false);
		ChartPanel chartPanel = new ChartPanel(myColoredChart, 150, 150, 150,
				150, 250, 250, true, false, false, false, false, false);
		this.add(chartPanel);
	}

	@Override
	public void update() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				StringBuilder sb = new StringBuilder();
				sb.append("<html>");
				sb.append("<font size='2' color='silver' face='Verdana'>step: " + wm.getStep() + "</font><br>");
				sb.append("PLAYERS: <br>");
				Map<String, Integer> unitsMap = wm.getUnits();

				for (Map.Entry<String, Integer> entry : unitsMap.entrySet()) {
					sb.append("<span style='color:"
							+ formatColor(wm.getPlayersColors().get(
									entry.getKey())) + "'>" + entry.getKey()
							+ " : " + entry.getValue() + "<br></span>");

					myColoredPieChart.setValue(entry.getKey(), entry.getValue());
				}
				sb.append("</html>");
				rightLabel.setText(sb.toString());
			
				fillColorChart(); //TODO должно срабатывать только раз при нажатии на старт
			}
		});

	}

	private void fillColorChart(){
        PiePlot configurator = (PiePlot)myColoredChart.getPlot();
        for(Map.Entry<String, Color> player : wm.getPlayersColors().entrySet()){
            configurator.setSectionPaint(player.getKey(), player.getValue());
        }
	}

	private static String formatColor(Color c) {
		String r = (c.getRed() < 16) ? "0" + Integer.toHexString(c.getRed())
				: Integer.toHexString(c.getRed());
		String g = (c.getGreen() < 16) ? "0"
				+ Integer.toHexString(c.getGreen()) : Integer.toHexString(c
				.getGreen());
		String b = (c.getBlue() < 16) ? "0" + Integer.toHexString(c.getBlue())
				: Integer.toHexString(c.getBlue());
		return "#" + r + g + b;
	}

}
