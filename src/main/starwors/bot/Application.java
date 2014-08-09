package main.starwors.bot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import main.starwors.galaxy.Move;
import main.starwors.xml.MovesWriteException;
import main.starwors.xml.ResponseReadException;
import main.starwors.xml.ResponseReader;
import main.starwors.xml.MovesWriter;

public class Application {

	private static String server = "hardinv.ru";
	private static int port = 10040;
	private static String selfName = "";
	private static String token = "";

	public static void main(String[] args) {
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

				// выводим на экран ошибки
				printErrors(response);
				// даем боту походить: передаем список планет, получаем команды на передвижение
				moves = logic.step(response.getPlanets());
			} while (response.isGameRunning());
		// отлавливаем возможные исключения
		} catch (IOException ex) {
			System.out.println("network porblem: " + ex.getMessage());
		} catch (MovesWriteException ex) {
			System.out.println("can not send request to server: " + ex.getMessage());
		} catch (ResponseReadException ex) {
			System.out.println("can not read server response: " + ex.getMessage());
		}
	}

	private static void printErrors(Response response) {
		for (String error : response.getErrors()) {
			System.out.println(error);
		}
	}

	private static void loadProperties() {
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
}
