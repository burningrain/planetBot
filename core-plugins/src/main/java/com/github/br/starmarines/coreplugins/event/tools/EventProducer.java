package com.github.br.starmarines.coreplugins.event.tools;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import com.github.br.starmarines.coreplugins.event.Request;
import com.github.br.starmarines.coreplugins.pane.GalaxyRequestHandler;

@Provides(specifications = EventProducer.class)
@Instantiate
@Component(publicFactory = false)
public class EventProducer {

	@Requires
	GalaxyRequestHandler requestHandlerService;

	private ScheduledExecutorService scheduledExecutorService;
	private ScheduledFuture scheduledFuture;

	@Validate
	public void validate() throws InterruptedException, ExecutionException {
		scheduledExecutorService = Executors.newScheduledThreadPool(5);
		scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("EventProducer Executed!");
				Request request = new Request();
				request.setRequest("GALAXY info");
				requestHandlerService.handleRequest(request);
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	@Invalidate
	protected void stop() {
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
			System.out.println("Scheduler down");
		}
	}

}
