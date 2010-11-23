package bitcity;

import javax.swing.JPanel;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;

public class WorldMap extends JPanel implements Runnable {

	/**
	 * XXX ? 
	 */
	private static final long serialVersionUID = 2398485906470095960L;

	public static boolean ambulanceAround = false;
	public static int carCount = 0;
	public static int FPS = 30;
	
	/* Color */
	private static final Color BACKGROUND_COLOR = new Color(0, (float)0.4, 0);
	private static final Color SIDEWALK_COLOR = new Color((float)0.5, (float)0.5, (float)0.5);
	private static final Color DECORATION_COLOR = new Color(0, (float)0.2, 0);
	private static final Color ROAD_COLOR = Color.black;
	
	private World world;
	
	public WorldMap(World world) {
		this.world = world;
	}
	
	public void run() {
		Point startPos;
		
		for (int i = 0; i < this.world.getTrafficLight().size(); i++){
			this.world.getTrafficLight().get(i).start();
		}
		
		try {
			Iterator<Tree> it = this.world.getTrees();
			while (it.hasNext()) {
				it.next().start();
			}
		} catch (NullPointerException e) {
			/* No trees in the world. */
		}
		
		while (true) {
			try {
				Thread.sleep((int)(1000./WorldMap.FPS));
				if (WorldMap.carCount < this.world.carLimit && Math.random() < 0.15) {
					/* Add a new car. */
					for (int i = 0; i < 2; i++) {
						startPos = this.world.getRandomStartPos();
						if ((this.world.getRoadElement(startPos.x, startPos.y) & World.CAR) != 0) {
							if (Application.DEBUG) System.out.println(">> There is a car in the soup");
						} else {
							if (Application.DEBUG) System.out.println(">> Add car");
							Car.createCar(this.world, startPos).start();
							break;
						}
					}
				} else if ((WorldMap.carCount >= this.world.carLimit - 5) && !WorldMap.ambulanceAround &&
						Math.random() < 0.005) {
					startPos = this.world.getRandomStartPos();
					Ambulance.createCar(this.world, startPos).start();
				}
				if (!world.getRain().isRaining() && Math.random() < World.RAIN_PROBABILITY) {
					synchronized (world.getRain()) {
						world.getRain().notify();
					}
				}
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
				break;
			} catch (Exception e) {
				System.err.println("Nice unwanted exception :) " + e.getMessage());
			}
			repaint();
		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D ctx = (Graphics2D)g;
		int width, height;
		int i, j;
		float stepw, steph;
		char elem;
		
		width = this.getParent().getWidth();
		height = this.getParent().getHeight();
		stepw = (float)width / (this.world.columnCount() - 1);
		steph = (float)height / (this.world.rowCount() - 1);
		//System.out.println("stepw = " + stepw + ", steph = " + steph);
		
		ctx.setColor(WorldMap.BACKGROUND_COLOR);
		ctx.fillRect(0, 0, width, height);
		
		//System.out.println("redrawing");
		for (i = 1; i < this.world.rowCount(); i++) {
			for (j = 1; j < this.world.columnCount(); j++) {
				elem = this.world.getElementAt(i, j);

				if (elem == Parser.SIDEWALK || elem == Parser.GARAGE) {
					ctx.setColor(WorldMap.SIDEWALK_COLOR);
				} else if (elem == '$') {
					/* Some kind of decoration. */
					ctx.setColor(WorldMap.DECORATION_COLOR);
				} else if (elem == Parser.TREE_ROOT) {
					Tree tree = this.world.getTree(i, j);
					if (tree != null)
						tree.draw(ctx, stepw, steph);
					else {
						/* XXX Talvez reportar erro. */
					}
					continue;
				} else if (this.world.getSemaphores().containsKey(elem)) {
					/* Traffic light. */
					ctx.setColor(WorldMap.ROAD_COLOR);
					if ((this.world.getRoadElement(i, j) & World.CAR) != 0) {
						ctx.setColor(Car.COLOR);
					} else if (this.world.getRoadElement(i, j) == World.AMBULANCE) {
						ctx.setColor(Ambulance.COLOR);
					}
					ctx.fillRect((int)((j - 1) * stepw), (int)((i - 1) * steph), 
							(int)stepw + 1, (int)steph + 1);
					
					switch (this.world.getSemaphores().get(elem).status(i, j)) {
					case ALERT:
						ctx.setColor(Color.YELLOW);
						break;
					case CLOSED:
						ctx.setColor(Color.RED);
						break;
					case OPEN:
						ctx.setColor(Color.GREEN);
						break;
					}
					ctx.fillRect((int)((j - 1) * stepw), (int)((i - 1) * steph), 
							(int)stepw + 1, (int)stepw + 1);
					
					ctx.setColor(WorldMap.ROAD_COLOR);
					
					ctx.drawString("" + this.world.getElementAt(i, j) + "",
							(int)((j - 1) * stepw), (int)((i - 1) * steph) + (steph / 2));
					
					continue;
				} else if ((this.world.getRoadElement(i, j) & World.CAR) != 0) { 
					/* This is a car. */
					ctx.setColor(Car.COLOR);
				} else if (this.world.getRoadElement(i, j) == World.AMBULANCE) {
					ctx.setColor(Ambulance.COLOR);
				} else if (this.world.isRoad(i, j)) {
					/* Road. */
					ctx.setColor(WorldMap.ROAD_COLOR);
				} else {
					continue;
				}
				
				ctx.fillRect((int)((j - 1) * stepw), (int)((i - 1) * steph), 
						(int)stepw + 1, (int)steph + 1);
				if ((this.world.getRoadElement(i, j) & World.CAR_HONKING) != 0) {
					ctx.setColor(Color.WHITE);
					ctx.fillRect((int)(((j - 1) * stepw) + 1), (int)(((i - 1) * steph) + (steph/4.)), 
							(int)(stepw - 2), (int)((steph - 1)/2.));
				}
			}
		}
		
		this.world.getRain().draw(ctx, stepw, steph);
	}
}
