package com.github.br.starmarines.main.model.services.inner;

import java.io.OutputStream;
import java.util.Collection;

import com.github.br.starmarines.game.api.galaxy.Move;
import com.github.br.starmarines.main.model.services.inner.exception.MovesWriteException;

public interface IMovesWriterService {

	/**
	 * Преобразовывает список команд на передвижение в XML формат и записывает результат в указанный поток
	 * 
	 * @param output поток, в который будет записан результат
	 * @param moves список команд
	 */
	void writeMoves(OutputStream output, Collection<Move> moves, String token)
			throws MovesWriteException;

}