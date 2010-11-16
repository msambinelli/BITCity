package bitcity;

import java.awt.Graphics2D;
import java.awt.Point;

public class TrafficLight extends WorldObject {

	private Point position;
	
	public TrafficLight(Point position) {
		this.position = position;
	}
	
	/* XXX Not used */
	public void draw(Graphics2D ctx) {
		
	}
}
