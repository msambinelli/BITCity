package bitcity;

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class World {
	private char world[][];
	private Point startPos[];
	private HashMap<Character, Semaphore> semaphores;
	private int road[][];
	private ArrayList<TrafficLight> trafficLight;
	
	final public static int ROAD = 1;
	final public static int CAR_RUNING = 2;
	public static final int CAR_STOPED = 3;
	
	public World(char world[][], Point startPos[], HashMap<Character, Semaphore> semaphores2, 
			int road[][], ArrayList<TrafficLight> t) {
		this.world = world;
		this.startPos = startPos;
		this.semaphores = semaphores2;
		this.road = road;
		this.trafficLight = t;
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
}
