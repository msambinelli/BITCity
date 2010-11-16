package bitcity;

import java.awt.Point;
import java.util.Set;
import java.util.Random;

public class World {
	private char world[][];
	private Point startPos[];
	private Set<Character> semaphores;
	private int road[][];
	
	final public static int ROAD = 1;
	final public static int CAR = 2;
	
	public World(char world[][], Point startPos[], Set<Character> semaphores, int road[][]) {
		this.world = world;
		this.startPos = startPos;
		this.semaphores = semaphores;
		this.road = road;
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
	
	public Set<Character> getSemaphores() {
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
	
}
