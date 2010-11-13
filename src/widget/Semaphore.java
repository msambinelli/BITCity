package widget;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Semaphore extends Canvas {
	int width = 12;
	int height = 30;
	int x, y;
	Color red, yellow, green, light_red, light_yellow, light_green;
	int signal = 0; /* 0: closed; 1: alert; 2: open */
	
	public void paint(Graphics g){ 
		Graphics2D g2D=(Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setColor(Color.BLACK);
		g2D.translate(x, y);
		g2D.fillRect(0, 0, width, height);
		
		
		switch (signal) {
		case 0:
			g2D.setColor(light_red);
			g2D.fillOval(3, 2, 6, 6);
			g2D.setColor(yellow);
			g2D.fillOval(3, 12, 6, 6);
			g2D.setColor(green);
			g2D.fillOval(3, 22, 6, 6);
			break;
		case 1:
			g2D.setColor(red);
			g2D.fillOval(3, 2, 6, 6);
			g2D.setColor(light_yellow);
			g2D.fillOval(3, 12, 6, 6);
			g2D.setColor(green);
			g2D.fillOval(3, 22, 6, 6);
			break;
		case 2:
			g2D.setColor(red);
			g2D.fillOval(3, 2, 6, 6);
			g2D.setColor(yellow);
			g2D.fillOval(3, 12, 6, 6);
			g2D.setColor(light_green);
			g2D.fillOval(3, 22, 6, 6);
			break;
		}
	}
	
	/* coordenada x, y do canto superior esquerdo */
	public Semaphore(int x, int y){
	
		light_red = new Color(255, 0, 0);
		red = new Color(168, 0, 34);
		light_yellow = new Color(255, 255, 0);
		yellow = new Color(133, 133, 0);
		light_green = new Color(0, 255, 0);
		green = new Color(100, 180, 102);
		
		this.x  = x;
		this.y = y;
		
	}
	
	public void close(){
		signal = 1;
		repaint(x, y, width, height);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		signal = 0;
		repaint(x, y, width, height);
	}
	
	public void open(){
		signal = 2;
		repaint (x, y, width, height);
	}
}
