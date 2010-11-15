package widget;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Semaphore {
	
	private int x;
	private int y;
	
	private static final int width = 12;
	private static final int height = 30;
	
	private Color red, yellow, green, light_red, light_yellow, light_green;
	private int signal = 0; /* 0: closed; 1: alert; 2: open */
	
	public void draw(Graphics2D g2D){ 
		
		g2D.setColor(Color.BLACK);
		g2D.fillRect(x, y, width, height);
		
		
		switch (signal) {
		case 0:
			g2D.setColor(light_red);
			g2D.fillOval(x + 3, y + 2, 6, 6);
			g2D.setColor(yellow);
			g2D.fillOval(x + 3, y + 12, 6, 6);
			g2D.setColor(green);
			g2D.fillOval(x + 3, y + 22, 6, 6);
			break;
		case 1:
			g2D.setColor(red);
			g2D.fillOval(x + 3, y + 2, 6, 6);
			g2D.setColor(light_yellow);
			g2D.fillOval(x + 3, y + 12, 6, 6);
			g2D.setColor(green);
			g2D.fillOval(x + 3, y + 22, 6, 6);
			break;
		case 2:
			g2D.setColor(red);
			g2D.fillOval(x + 3, y + 2, 6, 6);
			g2D.setColor(yellow);
			g2D.fillOval(x + 3, y + 12, 6, 6);
			g2D.setColor(light_green);
			g2D.fillOval(x + 3, y + 22, 6, 6);
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
		signal = 0;
	}
	
	public void open(){
		signal = 2;
	}
}
