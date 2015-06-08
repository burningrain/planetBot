package com.lix.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SplashScreen;

public class Splash {

	private volatile boolean isVisibleSplash = true;

	static void renderSplashFrame(Graphics2D g, int frame) {
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(100, 200, 150, 30);
		g.setPaintMode();
		g.setColor(Color.WHITE);
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < (frame / 10) % 10; i++){
			sb.append(".");
		}
		g.drawString("Loading " + sb.toString(), 170, 210);
	}

	public Splash() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				SplashScreen splash = SplashScreen.getSplashScreen();
				if (splash == null) {
					System.out.println("SplashScreen.getSplashScreen() returned null");
					return;
				}
				Graphics2D g = splash.createGraphics();
				if (g == null) {
					System.out.println("g is null");
					return;
				}
				
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
				Font font = g.getFont();
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.setColor(Color.WHITE);
				g.drawString("STAR MARINES", 105, 50);
				g.setFont(font);
				
				int i = 0;
				while (isVisibleSplash) {
					renderSplashFrame(g, i);
					splash.update();
					i++;
				}

				splash.close();
			}
		}).start();

	}

	public void dispose() {
		isVisibleSplash = false;
	}

}