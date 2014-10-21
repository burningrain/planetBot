package starwors.view;


import starwors.model.lx.bot.BotModel;
import starwors.model.lx.bot.IBotModelListener;
import starwors.model.lx.logic.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SwingView implements IBotModelListener {

    public static int START_WIDTH;
    public static int START_HEIGHT;

    private BotModel model;
    private SwingView self;

    Map<String, Color> playersColors = new HashMap<String, Color>();
    final Random random = new Random();


    private JLabel topLabel;
    private JLabel rightLabel;


    public SwingView(BotModel model) {
        self = this;
        this.model = model;
        this.model.addListener(this);

        init();
    }


    @Override
    public void update(final BotModel model) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if(playersColors.isEmpty()){
                    fillPlayersColor(model.getPlayers());
                }


                if (topLabel != null) {
                    topLabel.setText("STEP: " + Game.STEP);
                }
                if (rightLabel != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>PLAYERS: <br>");
                    Map<String, Integer> unitsMap = model.getUnitsMap();
                    for (Map.Entry<String, Integer> entry : unitsMap.entrySet()) {
                        sb.append("<span style='color:" + formatColor(playersColors.get(entry.getKey())) + "'>" + entry.getKey() + " : " + entry.getValue() + "<br></span>");
                    }
                    sb.append("</html>");
                    rightLabel.setText(sb.toString());
                }
            }

        });
    }

    private void fillPlayersColor(Set<String> players){
        for(String player : players){
            playersColors.put(player, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
    }

    public static final String formatColor(Color c) {
        String r = (c.getRed() < 16) ? "0" + Integer.toHexString(c.getRed()) : Integer.toHexString(c.getRed());
        String g = (c.getGreen() < 16) ? "0" + Integer.toHexString(c.getGreen()) : Integer.toHexString(c.getGreen());
        String b = (c.getBlue() < 16) ? "0" + Integer.toHexString(c.getBlue()) : Integer.toHexString(c.getBlue());
        return "#" + r + g + b;
    }


    private void init() {


        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
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
                START_WIDTH = 660;
                START_HEIGHT = 660;

                jf.setSize(START_WIDTH, START_HEIGHT);
                jf.setLocation((screenSizeWidth - START_WIDTH) / 2, (screenSizeHeigth - START_HEIGHT) / 2);

                // установка пиктораммы

                Image img = kit.getImage(this.getClass().getClassLoader().getResource("icon.gif"));
                jf.setIconImage(img);


                // установка содержимого окна

                // панель графа
                JPanel grPanel = new GraphPanel(model, self);

                // панель кнопок
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());
                JButton btnStart = new JButton("Start");
                btnStart.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.start();
                    }
                });
                JButton btnReply = new JButton("Reply");
                btnReply.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.reply();
                    }
                });
                buttonPanel.add(btnStart);
                buttonPanel.add(btnReply);

                // верхняя панель с надписью
                topLabel = new JLabel("STEP: ");
                Font font = new Font("Verdana", Font.PLAIN, 16);
                topLabel.setHorizontalAlignment(JLabel.CENTER);
                topLabel.setFont(font);

                // панель справа с общим числом юнитов
                rightLabel = new JLabel("PLAYERS");
                Font rightFont = new Font("Verdana", Font.PLAIN, 12);
                rightLabel.setHorizontalAlignment(JLabel.LEFT);
                rightLabel.setVerticalAlignment(JLabel.TOP);
                rightLabel.setFont(rightFont);

                // главная панель фрейма
                JPanel rootPanel = new JPanel();
                rootPanel.setLayout(new BorderLayout());
                rootPanel.add(grPanel, BorderLayout.CENTER);
                rootPanel.add(buttonPanel, BorderLayout.SOUTH);
                rootPanel.add(topLabel, BorderLayout.NORTH);
                rootPanel.add(rightLabel, BorderLayout.EAST);

                jf.getContentPane().add(rootPanel);
                //jf.pack();

                jf.setVisible(true);
            }
        });
    }


}
