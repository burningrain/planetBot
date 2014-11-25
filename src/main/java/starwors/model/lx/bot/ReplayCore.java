package starwors.model.lx.bot;


import starwors.model.lx.galaxy.Action;
import starwors.model.lx.logic.GameInfo;
import starwors.model.lx.logic.utils.CoreUtils;
import starwors.model.lx.xml.ReaderService;
import starwors.model.lx.xml.ReplayDataService;
import starwors.model.lx.xml.XmlReaderService;
import starwors.model.lx.xml.XmlReplayDataService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class ReplayCore {

    private ReaderService reader;
    private ReplayDataService replayDataService;
    private BotModel model;
    private GameInfo gameInfo; //TODO подумать о многопоточке

    public ReplayCore(BotModel model){
        // TODO в ближайшем будущем вынести в фабрики или DI
        reader = new XmlReaderService();
        replayDataService = new XmlReplayDataService();
        this.model = model;
        gameInfo = model.getGameInfo();
    }


    public void startReplay() {
        try {
            final List<String> responses = replayDataService.getResponsesList("replay.smbot");
            final List<String> moves = replayDataService.getActionsList("actions.smbot");
            final Thread thread = new Thread(new ReplyThread(responses, moves));
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // TODO добавить в UI обработку перемещений (выделение некорректных ходов красным цветом)
    void showStep(InputStream responses, InputStream moves) {
        Response response = null;
        Collection<Action> actionsList = null;
        try {
            byte[] content = CoreUtils.readByteArrayFromInputStream(responses);
            response = reader.readGalaxy(new ByteArrayInputStream(content));
            byte[] actions = CoreUtils.readByteArrayFromInputStream(moves);
            actionsList = reader.readActions(new ByteArrayInputStream(actions));

            gameInfo.fill(response.getPlanets());
            //даем новые данные слушателям
            model.updateResponse(response);           // информация о распределении сил на планетах
            Thread.sleep(500);
            model.updateCurrentActions(actionsList); // информация о действиях бота в сложившейся ситуации
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ReplyThread implements Runnable {

        private List<String> responses;
        private List<String> moves;

        private int count = 0;

        public ReplyThread(List<String> responses, List<String> moves){
            this.responses = responses;
            this.moves = moves;
        }

        @Override
        public void run() {
            while(count < responses.size() - 1){
                count++;
                gameInfo.setStep(count);
                showStep(new ByteArrayInputStream(responses.get(count).getBytes(StandardCharsets.UTF_8)),
                        new ByteArrayInputStream(moves.get(count).getBytes(StandardCharsets.UTF_8)));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
