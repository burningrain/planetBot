package com.br.app;

import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Validate;

public interface IApp {

	public void start();

	public void stop();

}