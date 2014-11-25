package starwors.model.lx.bot;

import starwors.model.lx.galaxy.Action;
import starwors.model.lx.galaxy.Move;
import starwors.model.lx.logic.utils.BotProperties;
import starwors.model.lx.logic.utils.CoreUtils;
import starwors.model.lx.xml.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

class GameCore {

    private volatile boolean gameInProcess = false;

    private BotModel model;
    private ReplayWriter replayWriter;
    private BotProperties properties;

    public GameCore(BotModel model) {
        this.model = model;
        // TODO в ближайшем будущем вынести в фабрики или DI
        replayWriter = new ReplayWriter();
        properties = new BotProperties();
        // загружаем настройки соединения с сервером из файла
        properties.loadProperties("bot.prop");
    }


    public synchronized void stop() {
        gameInProcess = false;
    }


    public synchronized void start() {
        gameInProcess = true;
        replayWriter.clearLastReplay();

        // создаем необходимые объекты
        Logic logic = new Logic();
        XmlReaderService reader = new XmlReaderService();
        MovesWriter writer = new MovesWriter(properties.getToken());

        // до начала игры список команд пустой
        Collection<Move> moves = Collections.emptyList();
        Response response = null;
        try {
            do {
                // подключаемся к серверу
                Socket socket = new Socket(properties.getServer(), properties.getPort());
                // отправляем список команд
                writer.writeMoves(new BufferedOutputStream(socket.getOutputStream()), moves);
                // читаем состояние планет от сервера
                InputStream inputStream = socket.getInputStream();
                //InputStream inputStream = new BufferedInputStream(socket.getInputStream());

                byte[] content = CoreUtils.readByteArrayFromInputStream(inputStream);
                response = reader.readGalaxy(new ByteArrayInputStream(content));
                // на этом ходу общение с сервером закончилось, закрываем соединение
                socket.close();
                model.updateResponse(response); //  информация о распределении сил на планетах

                model.getGameInfo().fill(response.getPlanets()); // обновляем информацию об игровой ситуации
                // даем боту походить: передаем список планет, получаем команды на передвижение
                moves = logic.step(response.getPlanets());

                Thread.sleep(30); // FIXME сделано т.к. без этой задержки почему-то нет перерисовки стрелок
                Collection<Action> actions = CoreUtils.toActionList(moves);
                model.updateCurrentActions(actions); // информация о действиях бота в сложившейся ситуации
                replayWriter.writeReplay(content, actions); // записываем текущую ситуацию и наш следующий ход для реплея
            } while (response.isGameRunning() && gameInProcess);
            // отлавливаем возможные исключения
        } catch (IOException ex) {
            System.out.println("network porblem: " + ex.getMessage());
        } catch (MovesWriteException ex) {
            System.out.println("can not send request to server: " + ex.getMessage());
        } catch (ResponseReadException ex) {
            System.out.println("can not read server response: " + ex.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

