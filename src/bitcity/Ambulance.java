package bitcity;

import java.awt.Point;

public class Ambulance extends Car {

	private static double changeDirectionProb = 0.15;
	
	public Ambulance(World world, Point startPos, char direction) {
		super(world, startPos, direction);
		WorldMap.carCount--; /* Do not count this as a car. */
		WorldMap.ambulanceAround = true;
	}
	
	public void move() throws Exception {
		char ahead = this.world.getElementAhead(this.direction, this.pos);
		Point nextPos;
		double prob;
		
		if (ahead == Parser.SENTINEL || ahead == Parser.GARAGE) {
			this.world.setRoadElement(this.pos.x, this.pos.y, World.ROAD);
			throw new Exception("Destroy ambulance");
		}
		
		this.world.setRoadElement(this.pos.x, this.pos.y, World.ROAD);
		this.pos = this.getNextPos(this.direction, this.pos);
		this.world.setRoadElement(this.pos.x, this.pos.y, World.AMBULANCE);
		if (ahead != ' ' && ahead != Parser.SIDEWALK && ahead != this.direction && 
				!this.world.getSemaphores().containsKey(ahead)) {
			/* Chance de mudar de direçao. */
			nextPos = this.getNextPos(ahead, this.pos);
			if (this.world.getElementAt(nextPos) == ahead) {
				/* Com certeza precisa mudar de direçao. */
				this.direction = ahead;
			} else {
				/* Tem chance de trocar de direçao. */
				prob = Math.random();
				if (prob < changeDirectionProb) {
					this.direction = ahead;
				}
			}
		}
	}
	
	public void run() {
		try {
			Sound.AMBULANCE_SIREN.loop();
			while (true) {
				try {
					this.move();
					Thread.sleep((int)(1000/25.));
				} catch (Exception e) {
					System.out.println("#### " + e.getMessage());
					break;
				}
			}
		} finally {
			Sound.AMBULANCE_SIREN.stop();
			WorldMap.ambulanceAround = false;	
		}
	}
	
	
	/* XXX Não tem um jeito de não duplicar isso da classe Car ? */
	public static Ambulance createCar(World world, Point startPos) throws Exception {
		char up, down, left, right;
		char direction;
		
		up = world.getElementAt(startPos.x - 1, startPos.y);
		down = world.getElementAt(startPos.x + 1, startPos.y);
		left = world.getElementAt(startPos.x, startPos.y - 1);
		right = world.getElementAt(startPos.x, startPos.y + 1);
		if (up != ' ' && up != Parser.SENTINEL && up != Parser.SIDEWALK && up != '$') {
			direction = up;
		} else if (down != ' ' && down != Parser.SENTINEL && down != Parser.SIDEWALK && down != '$') {
			direction = down;
		} else if (left != ' ' && left != Parser.SENTINEL && left != Parser.SIDEWALK && left != '$') {
			direction = left;
		} else if (right != ' ' && right != Parser.SENTINEL && right != Parser.SIDEWALK && right != '$') {
			direction = right;
		} else {
			throw new Exception("No initial direction found.");
		}
		
		return new Ambulance(world, startPos, direction);
	}
}
