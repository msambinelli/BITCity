package bitcity;

import java.awt.Point;


public class Semaphore {
	
	private Point position;
	
	public Semaphore(){
		this.position = new Point(-1, -1);
	}
	
	public synchronized void open(Point p){
		this.position = p;
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isopen(int i, int j){
		Point p = new Point(i, j);
		return this.position.equals(p);
	}
	
	public boolean isopen(Point p){
		return this.position.equals(p);
	}
}
