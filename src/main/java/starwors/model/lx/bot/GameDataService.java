package starwors.model.lx.bot;

import starwors.model.lx.galaxy.Move;
import starwors.model.lx.logic.Game;
import starwors.model.lx.xml.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

class GameDataService {

    private String server = "hardinv.ru";
    private int port = 10040;
    private String selfName = "";
    private String token = "";

    private volatile boolean gameInProcess = false;

    private List<IGameDataServiceListener> listeners;

    public GameDataService() {
        listeners = new LinkedList<IGameDataServiceListener>();
    }


    public synchronized void stop() {
        gameInProcess = false;
    }


    public synchronized void start() {
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
                InputStream inputStream = socket.getInputStream();
                //InputStream inputStream = new BufferedInputStream(socket.getInputStream());

                byte[] content = readByteArrayFromInputStream(inputStream);
                response = reader.readGalaxy(new ByteArrayInputStream(content));
                // записываем реплей
                writeReplay(content);

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

    public void replay(){
        try{
            final List<String> responses = XmlReplaySplitter.split();

//            long startTime = System.currentTimeMillis();
//            long elapsedTime = 0L;
//
//            int length = responses.size();
//            int i = 0;
//            while (elapsedTime < 2*1000*length && i < length) {
//                showStep(new ByteArrayInputStream(responses.get(i).getBytes(StandardCharsets.UTF_8)));
//                i++;
//                //perform db poll/check
//                elapsedTime = (new Date()).getTime() - startTime;
//            }

            Thread thread = new Thread(new ReplyThread(responses, this));
            thread.start();

        } catch(Exception e){
            e.printStackTrace();
        }

    }



    void showStep(InputStream inputStream) {
        ResponseReader reader = new ResponseReader();
        Response response = null;
        try {
            //do {
                byte[] content = readByteArrayFromInputStream(inputStream);
                response = reader.readGalaxy(new ByteArrayInputStream(content));

                Game.init(response.getPlanets());
                //даем новые данные слушателям
                this.update(response);

                // выводим на экран ошибки
                printErrors(response);

            //} while (response.isGameRunning() && gameInProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void writeReplay(byte[] content) throws IOException {
        File file = new File("replay.smbot");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter wr = new BufferedWriter(new FileWriter(file.getName(), true));


        BufferedReader r = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8));
        String str = null;
        StringBuilder sb = new StringBuilder();
        while ((str = r.readLine()) != null) {
            sb.append(str);
        }
        wr.write(sb.toString());
        wr.close();
    }

    private byte[] readByteArrayFromInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int bytesRead = 0;
        while ((bytesRead = in.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        return baos.toByteArray();
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

    public void addListener(IGameDataServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IGameDataServiceListener listener) {
        listeners.remove(listener);
    }

    public void update(Response response) {
        for (IGameDataServiceListener listener : listeners) {
            listener.update(response);
        }
    }


}

