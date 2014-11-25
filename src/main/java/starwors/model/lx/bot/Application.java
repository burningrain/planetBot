package starwors.model.lx.bot;

import starwors.model.lx.bot.BotModel;
import starwors.view.SwingView;

public final class Application {

    private Application(){
    }

	public static void main(String[] args){ 

        BotModel model = new BotModel();
        SwingView view = new SwingView(model);

	}

}
