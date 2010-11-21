package bitcity;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;

public class Parser {
	private FileReader fileIn;
	private Scanner input;
	private int height_tiles, width_tiles;
	
	public static final char SENTINEL = '!';
	public static final char SIDEWALK = '#';
	public static final char GARAGE = '&';
	public static final char TREE_ROOT = '.';
	
	public Parser(String filename) throws FileNotFoundException {
		this.fileIn = new FileReader(filename);
		this.input = new Scanner(fileIn);
	}
	
	public int getHeightTiles() {
		return this.height_tiles;
	}
	
	public int getWidthTiles() {
		return this.width_tiles;
	}

	public World parse() throws Exception {
		String line;
		HashMap<Character, Semaphore> semaphores = new HashMap<Character, Semaphore>();
		char worldMap[][], curr;
		int roadMap[][];
		Point startPos[];
		int rows, cols, startAmount, toSync, carLimit;
		int i, j;
		ArrayList<TrafficLight> trafficLight = new ArrayList<TrafficLight>();
		ArrayList<Point> tree = new ArrayList<Point>();
		
		try {
			rows = input.nextInt();
			cols = input.nextInt();
			startAmount = input.nextInt();
			toSync = input.nextInt();
			carLimit = input.nextInt();
			input.nextLine();
			
			this.height_tiles = rows;
			this.width_tiles = cols;
			
			if (startAmount <= 0) {
				throw new Exception("Invalid amount of starting points: " + startAmount);
			}
			
			worldMap = new char[rows + 2][cols + 2]; /* +2 for sentinels. */
			roadMap = new int[rows + 2][cols + 2];
			
			startPos = new Point[startAmount];
			
			/* Sentinels. */
			for (j = 0; j <= cols + 1; j++) {
				worldMap[0][j] = SENTINEL;
				worldMap[rows + 1][j] = SENTINEL;
			}
			for (i = 1; i <= rows; i++) {
				worldMap[i][0] = SENTINEL;
				worldMap[i][cols + 1] = SENTINEL;
			}
			
			for (i = 1; i <= rows; i++) {
				line = input.nextLine();
				for (j = 1; j <= cols; j++) {
					curr = line.charAt(j - 1);
					if (curr == '\t') {
						curr = ' ';
						System.err.println("Warning: A tab at (" + i + ", " + j + ") has been replaced " +
								"by a space");
					}
					
					if (curr == '*') {
						if (startAmount == 0) {
							throw new Exception("There are more starting points than declared");
						}
						startAmount--;
						startPos[startAmount] = new Point(i, j);
					} else if (curr == '.') {
						/* Árvores */
						if (Application.DEBUG) System.out.println("Tree at: " + i + ", " + j);
						tree.add(new Point(i, j));
					} else {
						/* Semáforos */
						if (((int)(curr) - (int)'A') >= 0 && ((int)(curr) - (int)'A') <= ((int)'Z' - (int)'A')) {
							if (Application.DEBUG) {
								System.out.println("Traffic light at " + i + ", " + j);
								System.out.println("Key " + curr);
							}
							if (!semaphores.containsKey(curr)){
								toSync--;
								semaphores.put(curr, new Semaphore());
							} else {
								semaphores.get(curr).incGroupSize();
							}
					
							trafficLight.add(new TrafficLight(i, j, semaphores.get(curr)));
					
						}
					}
					worldMap[i][j] = curr;
					roadMap[i][j] = 0;
				}
			}
			
			if (toSync != 0) {
				throw new Exception("Check your traffic light groups " + toSync);
			}
			if (startAmount != 0) {
				System.err.println("Maybe I should have found " + startAmount + " more starting points, " +
						"but I failed.");
			}
		} finally {
			fileIn.close();
		}
		
		Iterator<Semaphore> it = semaphores.values().iterator();
		while (it.hasNext()) {
			Semaphore item = it.next();
			item.adjustDelay();
		}
		
		/* Construct the road map now. */
		buildRoadMap(worldMap, roadMap, startPos[0].x, startPos[0].y);
		
		return new World(worldMap, startPos, semaphores, roadMap, trafficLight, tree, carLimit);
	}
	
	
	private void buildRoadMap(char map[][], int roadMap[][], int row, int col) {
		if (this.isNotRoad(map, row, col)) {
			return;
		}
		if (roadMap[row][col] == 0) {
			roadMap[row][col] = 1; /* This tile is a road. */
			buildRoadMap(map, roadMap, row, col + 1);
			buildRoadMap(map, roadMap, row, col - 1);
			buildRoadMap(map, roadMap, row + 1, col);
			buildRoadMap(map, roadMap, row - 1, col);
		}
	}
	
	private boolean isNotRoad(char map[][], int row, int col) {
		if (map[row][col] == SENTINEL || map[row][col] == SIDEWALK || map[row][col] == GARAGE ||
				map[row][col] == '$')
			return true;
		return false;
	}
}
