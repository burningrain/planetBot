package starwors.view;


import starwors.model.lx.bot.BotModel;
import starwors.model.lx.bot.IBotModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingView implements IBotModelListener {

    public static int START_WIDTH;
    public static int START_HEIGHT;

    private BotModel model;



    public SwingView(BotModel model){
        this.model = model;
        this.model.addListener(this);

        init();
    }


    @Override
    public void update(BotModel model) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void init(){


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
                START_WIDTH = screenSizeWidth / 2;
                START_HEIGHT = screenSizeHeigth / 2;

                jf.setSize(START_WIDTH, START_HEIGHT);
                jf.setLocation(screenSizeWidth / 4, screenSizeHeigth / 4);

                // установка пиктораммы

                Image img = kit.getImage(getClass().getResource("/icon.gif"));
                jf.setIconImage(img);


                // установка содержимого окна

                JPanel grPanel = new GraphPanel(model);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());
                JButton btnStart = new JButton("Start");
                btnStart.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.start();
                    }
                });
                buttonPanel.add(btnStart);

                JPanel rootPanel = new JPanel();
                rootPanel.setLayout(new BorderLayout());
                rootPanel.add(grPanel, BorderLayout.CENTER);
                rootPanel.add(buttonPanel, BorderLayout.SOUTH);

                jf.getContentPane().add(rootPanel);
                //jf.pack();

                jf.setVisible(true);
            }
        });
    }


}
