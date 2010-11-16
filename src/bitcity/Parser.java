package bitcity;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;

public class Parser {
	private FileReader fileIn;
	private Scanner input;
	public static final char SENTINEL = '!';
	public static final char SIDEWALK = '#';
	public static final char GARAGE = '&';
	
	public Parser(String filename) throws FileNotFoundException {
		this.fileIn = new FileReader(filename);
		this.input = new Scanner(fileIn);
	}
	
	public World parse() throws Exception {
		String line;
		HashSet<Character> semaphores = new HashSet<Character>();
		char worldMap[][], curr;
		int roadMap[][];
		Point startPos[];
		int rows, cols, startAmount, toSync;
		int i, j;
		
		try {
			rows = input.nextInt();
			cols = input.nextInt();
			startAmount = input.nextInt();
			toSync = input.nextInt();
			input.nextLine();
			
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
					if (curr == '*') {
						if (startAmount == 0) {
							throw new Exception("There are more starting points than declared");
						}
						startAmount--;
						startPos[startAmount] = new Point(i, j);
					} else {
						/* Sem‡foros */
						if (((int)(curr) - (int)'A') >= 0 && ((int)(curr) - (int)'A') <= ((int)'Z' - (int)'A')) {
							System.out.println("Traffic light at " + i + ", " + j);
							semaphores.add(curr);
						}
					}
					worldMap[i][j] = curr;
					roadMap[i][j] = 0;
				}
			}
		} finally {
			fileIn.close();
		}
		
		/* Construct the road map now. */
		buildRoadMap(worldMap, roadMap, startPos[0].x, startPos[0].y);
		
		return new World(worldMap, startPos, semaphores, roadMap);
	}
	
	
	private void buildRoadMap(char map[][], int roadMap[][], int row, int col) {
		if (map[row][col] == SENTINEL || map[row][col] == SIDEWALK || map[row][col] == GARAGE) {
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
}
