package com.github.br.starmarines.main.model.services.inner.exception;

/**
 * Ошибка при отправке сообщения серверу
 */
public class MovesWriteException extends Exception {

	public MovesWriteException() {
		super();
	}

	public MovesWriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public MovesWriteException(String message) {
		super(message);
	}

	public MovesWriteException(Throwable cause) {
		super(cause);
	}

}
