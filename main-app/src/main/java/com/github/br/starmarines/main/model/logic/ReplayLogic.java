package com.github.br.starmarines.main.model.logic;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.apache.felix.ipojo.annotations.BindingPolicy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.starwors.lx.galaxy.Action;
import com.br.starwors.lx.logic.utils.CoreUtils;
import com.github.br.starmarines.main.model.objects.inner.IGameInfo;
import com.github.br.starmarines.main.model.objects.inner.IStepInfo;
import com.github.br.starmarines.main.model.objects.inner.impl.GameInfo;
import com.github.br.starmarines.main.model.objects.inner.impl.StepInfo;
import com.github.br.starmarines.main.model.services.inner.IReaderService;
import com.github.br.starmarines.main.model.services.inner.IReplayReaderService;
import com.github.br.starmarines.main.model.services.inner.beans.Response;

@Component
@Instantiate
@Provides
public class ReplayLogic {
	
	@Requires(optional = true)
	private LogService logService;
	
	@Validate
	private void validate(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " start");
	}
	
	@Invalidate
	private void invalidate(){
		logService.log(LogService.LOG_DEBUG, this.getClass().getName() + " stop");
	}
	

	@Requires
    private IReaderService reader;
	
	@Requires
    private IReplayReaderService replayService;
    
	@Requires(proxy = false, policy = BindingPolicy.STATIC)
    private IGameInfo gameInfo;  //TODO подумать о многопоточке
	
	@Requires(proxy = false, policy = BindingPolicy.STATIC)
    private IStepInfo step;  //TODO подумать о многопоточке    


    public void start() {
        try {
            final List<String> responses = replayService.getResponsesList("replay.smbot");
            final List<String> moves = replayService.getActionsList("actions.smbot");
            final Thread thread = new Thread(new ReplayThread(responses, moves));
            thread.start();

        } catch (Exception e) {
        	logService.log(LogService.LOG_ERROR, "erro while replay do it", e);
        }

    }


    // TODO добавить в UI обработку перемещений (выделение некорректных ходов красным цветом)
    private void showStep(InputStream responses, InputStream moves) {
        Response response = null;
        Collection<Action> actionsList = null;
        try {
            byte[] content = CoreUtils.readByteArrayFromInputStream(responses);
            response = reader.readGalaxy(new ByteArrayInputStream(content));
            byte[] actions = CoreUtils.readByteArrayFromInputStream(moves);
            actionsList = reader.readActions(new ByteArrayInputStream(actions));

            ((GameInfo) gameInfo).updateGalaxy(response.getPlanets()); //FIXME
            //даем новые данные слушателям
            ((StepInfo) step).updateResponse(response);   //FIXME        // информация о распределении сил на планетах            
            ((StepInfo) step).updateCurrentActions(actionsList); //FIXME  // информация о действиях бота в сложившейся ситуации
        } catch (Exception e) {
        	logService.log(LogService.LOG_ERROR, "error while showStep", e);
        }

    }

    private class ReplayThread implements Runnable {

        private List<String> responses;
        private List<String> moves;

        private int count = 0;

        public ReplayThread(List<String> responses, List<String> moves){
            this.responses = responses;
            this.moves = moves;
        }

        @Override
        public void run() {
            while(count < responses.size()){
                count++;
                ((GameInfo) gameInfo).setStepsCount(count); // FIXME
                showStep(new ByteArrayInputStream(responses.get(count).getBytes(StandardCharsets.UTF_8)),
                        new ByteArrayInputStream(moves.get(count).getBytes(StandardCharsets.UTF_8)));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                	logService.log(LogService.LOG_ERROR, "interrupt", e);
                }
            }
        }
    }

}
