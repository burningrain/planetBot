package com.br.model.logic;


import java.io.*;
import java.net.Socket;
import java.util.*;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;

import com.br.game.api.galaxy.Move;
import com.br.model.config.properties.BotProperties;
import com.br.model.objects.inner.IGameInfo;
import com.br.model.objects.inner.IStepInfo;
import com.br.model.objects.inner.impl.GameInfo;
import com.br.model.objects.inner.impl.StepInfo;
import com.br.model.services.inner.IMovesWriterService;
import com.br.model.services.inner.IReaderService;
import com.br.model.services.inner.IReplayWriterService;
import com.br.model.services.inner.beans.Response;
import com.br.model.services.inner.exception.MovesWriteException;
import com.br.model.services.inner.exception.ResponseReadException;
import com.br.model.services.remote.IStrategyService;
import com.br.starwors.lx.galaxy.Action;
import com.br.starwors.lx.logic.utils.CoreUtils;

@Component
@Instantiate
@Provides
public class OnlineGameLogic {
	
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
    private IReplayWriterService replayWriter;
    
    @Requires
    private BotProperties botProperties;
    
    @Requires
    private IReaderService readerService;
    
    @Requires
    private IMovesWriterService writerService;
    
    @Requires
    private IStrategyService strategyService;
        
    
    @Requires
    private IGameInfo gameInfo; 
	
	@Requires
    private IStepInfo step; 
	

	
    private volatile boolean gameInProcess = false;
	private Thread thread;	
	
	

    public synchronized void stop() {
        gameInProcess = false;
    }

//TODO сделать проверкой на interrupt, убрать synchronized
    public synchronized void start() {
    	thread = new Thread(new Runnable() {
			
			@Override
			public void run() {		
				logService.log(LogService.LOG_DEBUG, botProperties.toString());
				
				gameInProcess = true;
		        replayWriter.clearLastReplay();          

		        // до начала игры список команд пустой
		        Collection<Move> moves = Collections.emptyList();
		        Response response = null;
		        try {
		            do {
		                // подключаемся к серверу
		                Socket socket = new Socket(botProperties.getServer(), botProperties.getPort());
		                // отправляем список команд
		                writerService.writeMoves(new BufferedOutputStream(socket.getOutputStream()), moves, botProperties.getToken());
		                // читаем состояние планет от сервера
		                InputStream inputStream = socket.getInputStream();
		                //InputStream inputStream = new BufferedInputStream(socket.getInputStream());

		                byte[] content = CoreUtils.readByteArrayFromInputStream(inputStream);
		                response = readerService.readGalaxy(new ByteArrayInputStream(content));
		                // на этом ходу общение с сервером закончилось, закрываем соединение
		                socket.close();
		                ((StepInfo) step).updateResponse(response); //FIXME // обновляем информацию о распределении сил на планетах

		                ((GameInfo) gameInfo).updateGalaxy(response.getPlanets()); //FIXME  // обновляем информацию об игровой ситуации
		                // даем боту походить: передаем список планет, получаем команды на передвижение
		                moves = strategyService.step(response.getPlanets());
		                
		                Collection<Action> actions = CoreUtils.toActionList(moves);
		                ((StepInfo) step).updateCurrentActions(actions); //FIXME // информация о действиях бота в сложившейся ситуации
		                replayWriter.writeReplay(content, actions); // записываем текущую ситуацию и наш следующий ход для реплея
		            } while (response.isGameRunning() && gameInProcess);
		            // отлавливаем возможные исключения
		        } catch (IOException ex) {
		        	logService.log(LogService.LOG_ERROR, "network porblem: ", ex);
		        } catch (MovesWriteException ex) {
		        	logService.log(LogService.LOG_ERROR, "can not send request to server: ", ex);
		        } catch (ResponseReadException ex) {
		        	logService.log(LogService.LOG_ERROR, "can not read server response: ", ex);
		        } 
				
			}
		});
        thread.start();
    }


}

