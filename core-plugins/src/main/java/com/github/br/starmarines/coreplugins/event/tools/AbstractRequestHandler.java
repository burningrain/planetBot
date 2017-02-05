package com.github.br.starmarines.coreplugins.event.tools;

import com.github.br.starmarines.coreplugins.event.Request;

public abstract class AbstractRequestHandler {

	protected AbstractRequestHandler successor;

	public void setSuccessor(AbstractRequestHandler successor) {
		this.successor = successor;
	}

	public void handleRequest(Request request) {
		if (successor != null) {
			successor.handleRequest(request);
		}
	}

}
