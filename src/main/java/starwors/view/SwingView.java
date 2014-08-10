package starwors.view;


import starwors.model.lx.bot.BotModel;
import starwors.model.lx.bot.IBotModelListener;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SwingView implements IBotModelListener {

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

                jf.setSize(screenSizeWidth / 2, screenSizeHeigth / 2);
                jf.setLocation(screenSizeWidth / 4, screenSizeHeigth / 4);

                // установка пиктораммы

                Image img = kit.getImage(getClass().getResource("/icon.gif"));
                jf.setIconImage(img);


                // установка содержимого окна

//                JPanel bpanel = createBasicPanel();
//                bpanel.setAlignmentX(RIGHT_ALIGNMENT);
//                bpanel.setAlignmentY(TOP_ALIGNMENT);
//                jf.setContentPane(bpanel);
//                jf.pack();


                jf.setVisible(true);
            }
        });
    }


}
