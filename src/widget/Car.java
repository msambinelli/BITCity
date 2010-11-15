package widget;

import java.awt.Color;
import java.awt.Graphics2D;

public class Car {

	private int x;
	private int y;
	
	private static final int width = 10;
	private static final int height = 10;
	
	public Car(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics2D g2D){
		g2D.setColor(Color.blue);
		g2D.fillOval(x, y, width, height);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
