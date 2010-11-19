package bitcity;

import java.awt.Graphics2D;
import java.awt.Point;

public class TrafficLight extends WorldObject {

	private Point position;
	private Semaphore controler;
	private static final int delay = 420;
	
	public TrafficLight(int i, int j, Semaphore semaphore) {
		this.position = new Point(i, j);
		this.controler = semaphore;
	}
	
	public void run(){
		while (true){
			this.controler.open(this.position);
			try {
				Thread.sleep(TrafficLight.delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* XXX Not used */
	public void draw(Graphics2D ctx) {
		
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Semaphore getControler() {
		return controler;
	}

	public void setControler(Semaphore controler) {
		this.controler = controler;
	}
}
