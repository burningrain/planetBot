package starwors.model.lx.bot;

import starwors.model.lx.bot.BotModel;
import starwors.view.SwingView;

public class Application {


	public static void main(String[] args){ 

        BotModel model = new BotModel();
        SwingView view = new SwingView(model);

	}

}
