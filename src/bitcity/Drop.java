package bitcity;

import java.awt.Point;

public class Drop {

	private Point pt;
	private int time;
	
	public Drop(int x, int y, int time){
		this.pt = new Point(x, y);
		this.time = time;
	}
	
	public float getForce(int time_now){
		return (float) (1 - (time_now - this.time) / 30.0);
	}

	public float getX() {
		// TODO Auto-generated method stub
		return this.pt.x;
	}

	public float getY() {
		// TODO Auto-generated method stub
		return this.pt.y;
	}
}
