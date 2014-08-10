package starwors.model.lx.bot;


import starwors.model.lx.galaxy.Move;
import starwors.model.lx.xml.MovesWriteException;
import starwors.model.lx.xml.MovesWriter;
import starwors.model.lx.xml.ResponseReadException;
import starwors.model.lx.xml.ResponseReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

class GameDataService {

    private String server = "hardinv.ru";
    private int port = 10040;
    private String selfName = "";
    private String token = "";

    private boolean gameInProcess = false;

    private List<IGameDataServiceListener> listeners;

    public GameDataService(){
        listeners = new LinkedList<IGameDataServiceListener>();
    }


    public void stop(){
        gameInProcess = false;
    }


    public void start() {
        gameInProcess = true;
        // загружаем настройки из файла
        loadProperties();

        // создаем необходимые объекты
        Logic logic = new Logic(selfName);
        ResponseReader reader = new ResponseReader();
        MovesWriter writer = new MovesWriter(token);

        // до начала игры список команд пустой
        Collection<Move> moves = Collections.emptyList();
        Response response = null;
        try {
            do {
                // подключаемся к серверу
                Socket socket = new Socket(server, port);
                // отправляем список команд
                writer.writeMoves(new BufferedOutputStream(socket.getOutputStream()), moves);
                // читаем состояние планет от сервера
                response = reader.readGalaxy(new BufferedInputStream(socket.getInputStream()));
                // на этом ходу общение с сервером закончилось, закрываем соединение
                socket.close();

                //даем новые данные слушателям
                this.update(response);

                // выводим на экран ошибки
                printErrors(response);
                // даем боту походить: передаем список планет, получаем команды на передвижение
                moves = logic.step(response.getPlanets());
            } while (response.isGameRunning() && gameInProcess);
            // отлавливаем возможные исключения
        } catch (IOException ex) {
            System.out.println("network porblem: " + ex.getMessage());
        } catch (MovesWriteException ex) {
            System.out.println("can not send request to server: " + ex.getMessage());
        } catch (ResponseReadException ex) {
            System.out.println("can not read server response: " + ex.getMessage());
        }
    }



    private void printErrors(Response response) {
        for (String error : response.getErrors()) {
            System.out.println(error);
        }
    }

    private void loadProperties() {
        FileInputStream file = null;
        try {
            // открыть файл bot.prop
            file = new FileInputStream("bot.prop");
            Properties props = new Properties();
            // загрузить настройки из файла
            props.load(file);

            selfName = props.getProperty("name");
            token = props.getProperty("token");
            server = props.getProperty("server");
            port = Integer.parseInt(props.getProperty("port"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    // do nothing
                }
            }
        }
    }



    // OBSERVER METHODS

    public void addListener(IGameDataServiceListener listener){
        listeners.add(listener);
    }

    public void removeListener(IGameDataServiceListener listener){
        listeners.remove(listener);
    }

    public void update(Response response){
        for(IGameDataServiceListener listener : listeners){
            listener.update(response);
        }
    }



}

