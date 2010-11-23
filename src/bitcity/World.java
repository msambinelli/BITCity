package bitcity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;

public class World {
	private char world[][];
	private Point startPos[];
	private HashMap<Character, Semaphore> semaphores;
	private int road[][];
	private ArrayList<TrafficLight> trafficLight;
	private HashMap<Point, Tree> tree;
	
	private int worldSpeed = 1;
	public int carLimit;
	private Rain rain;
	
	final public static int ROAD = 1;
	final public static int AMBULANCE = 16;
	final public static int CAR = 4;
	final public static int CAR_RUNNING = 5;
	final public static int CAR_STOPPED = 6;
	final public static int CAR_HONKING = 8;

	final public static double RAIN_PROBABILITY = 1/1000.; /* 1 in 1000 frames. */

	public World(char world[][], Point startPos[], HashMap<Character, Semaphore> semaphores2, 
			int road[][], ArrayList<TrafficLight> t, ArrayList<Point> treelist, int carLimit) {
		this.world = world;
		this.startPos = startPos;
		this.semaphores = semaphores2;
		this.road = road;
		this.trafficLight = t;
		this.carLimit = carLimit;
		
		this.rain = new Rain(this);
		this.rain.start();
		
		this.tree = new HashMap<Point, Tree>();
		Iterator<Point> it = treelist.iterator();
		while (it.hasNext()) {
			Point pt = it.next();
			this.tree.put(pt, new Tree(this, pt));
		}
	}
	
	public void setMapElement(int row, int col, char value) {
		this.world[row][col] = value;
	}
	
	public char getElementAhead(char direction, Point pos) throws Exception {
		switch (direction) {
		case '+':
			return this.world[pos.x - 1][pos.y];
		case '-':
			return this.world[pos.x + 1][pos.y];
		case '<':
			return this.world[pos.x][pos.y - 1];
		case '>':
			return this.world[pos.x][pos.y + 1];
		default:
			throw new Exception("Invalid direction '" + direction + "'");
		}
	}
	
	public Point getStartPos(int index) {
		return this.startPos[index];
	}
	
	public Point getRandomStartPos() {
		return this.startPos[new Random().nextInt(this.startPos.length)];
	}
	
	public char getElementAt(Point pos) {
		return this.world[pos.x][pos.y];
	}
	
	public char getElementAt(int x, int y) {
		return this.world[x][y];
	}
	
	public HashMap<Character, Semaphore> getSemaphores() {
		return this.semaphores;
	}
	
	public Point[] getStartPoints() {
		return this.startPos;
	}
	
	public int rowCount() {
		return this.world.length - 1;
	}
	
	public int columnCount() {
		return this.world[0].length - 1;
	}
	
	public boolean isRoad(int row, int col) {
		return this.road[row][col] == ROAD;
	}
	
	public void setRoadElement(int row, int col, int value) {
		this.road[row][col] = value;
	}
	
	public int getRoadElement(int row, int col) {
		return this.road[row][col];
	}

	public int getTrafficLightId(int i, int j) {
		Point p = new Point(i, j);
		for (int k = 0; k < this.trafficLight.size(); k++){
			if (this.trafficLight.get(k).getPosition().equals(p)){
				return k;
			}
		}
		return -1;
	}

	public  ArrayList<TrafficLight> getTrafficLight() {
		
		return this.trafficLight;
	}
	
	
	public Tree getTree(int i, int j) {
		try {
			return this.tree.get(new Point(i, j));
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public Iterator<Tree> getTrees() throws NullPointerException {
		return this.tree.values().iterator();
	}
	
	public void dropRandomLeavesFromTrees() {
		Iterator<Tree> it;
		try {
			it = this.getTrees();
		} catch (NullPointerException e) {
			/* No trees in this world, fine :/ */
			return;
		}
		while (it.hasNext()) {
			it.next().maybeDropALeaf();
		}
	}
	

	public int getWorldSpeed() {
		return this.worldSpeed;
	}
	
	public void setWorldSpeed(int speed) {
		this.worldSpeed = speed;
	}
	
	public void incSpeed(boolean increase) {
		if (increase && this.worldSpeed > 4)
			return;
		else if (!increase && this.worldSpeed == 1)
			return;
		
		this.setWorldSpeed(this.getWorldSpeed() + (increase ? 1 : -1));
		WorldMap.FPS = 27 + this.getWorldSpeed() * 3;
	}

	public Rain getRain() {
		return rain;
	}

	public void setRain(Rain rain) {
		this.rain = rain;
	}
}
