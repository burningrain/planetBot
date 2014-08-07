/*
 * © EPAM Systems, 2012  
 */
package com.epam.starwors.xml;

/**
 * Ошибка при чтении сообщения с сервера
 */
public class ResponseReadException extends Exception {

	public ResponseReadException() {
		super();
	}

	public ResponseReadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResponseReadException(String message) {
		super(message);
	}

	public ResponseReadException(Throwable cause) {
		super(cause);
	}

}
