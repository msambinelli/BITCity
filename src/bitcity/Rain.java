package bitcity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Rain extends WorldObject {
	
	private boolean raining = false;
	private int dropMax;
	private int width;
	private int height;
	private int time;
	private int elapsed;
	private World world;

	final private static double THUNDER_PROBABILITY = 1/3.; /* 1 in 3 raining frames. */
	final private int lenghtMin = 4;

	public Rain(World world){
		this.world = world;
		this.width = this.world.columnCount();
		this.height = this.world.rowCount();
		this.dropMax = (int) (width * height / 2);
	}
	
	public Point[] getDrops(){
		int meio = (int) (this.time / 2);
		float rain_force;
		if (this.elapsed <= meio){
			rain_force = (float) (this.elapsed) / meio;
		} else {
			rain_force = 1 - (float) (this.elapsed - meio ) / (this.time - meio);
		}
		
		if (rain_force > 0.8 && Math.random() < Rain.THUNDER_PROBABILITY) {
			if (!Sound.THUNDER.isRunning()) {
				Sound.THUNDER.play();
			}
		}
		
		int size = (int) (dropMax * rain_force);
		Point[] rain = new Point[size];
		
		//System.out.println("ela: " + this.elapsed + " time: " + this.time + "force " + rain_force);
		for (int i = 0; i < size; i++){
			rain[i] = new Point();
			rain[i].x = Math.abs(Application.random.nextInt()) % this.width;
			rain[i].y = Math.abs(Application.random.nextInt()) % this.height;
		}
		this.world.dropRandomLeavesFromTrees();
		this.elapsed++;
		if (this.elapsed == this.time){
			Sound.RAIN.stop();
			this.raining = false;
		}
		return rain;
	}
	
	
	
	public void run() {
		int frameRate;
		
		try {
			while (true) {
				try {
					synchronized (this) {
						this.wait();
					}
					System.out.println("Starting to rain, slow down.");
					/* Actually, slowing down the world seems to cause a weird visual effect.
					 * Just take the message above as a warning.
					 */
					
					frameRate = WorldMap.FPS;
					this.elapsed = 0;
					this.time = this.lenghtMin + (Math.abs(Application.random.nextInt()) % 16);
					this.time *= frameRate;
					this.raining = true;
					Sound.RAIN.loop();
				} catch (InterruptedException e) {
					System.out.println("Rain exception: " + e.getMessage());
					break;
				}
			}
		} finally {
			System.out.println("Rain is leaving this world..");
		}
	}

	public boolean isRaining() {
		return raining;
	}

	public void setRaining(boolean raining) {
		this.raining = raining;
	}
	
	@Override
	void draw(Graphics2D ctx, float tileWidth, float tileHeight) {
		if (this.isRaining()) {
			Point[] drops = this.getDrops();
			ctx.setColor(Color.CYAN);
			float woffset = Math.abs(Application.random.nextInt()) % tileWidth;
			float hoffset = Math.abs(Application.random.nextInt()) % tileHeight;
			for (int i = 0; i < drops.length; i++){
				ctx.fillOval((int)(drops[i].x * tileWidth + woffset), 
						(int)(drops[i].y * tileHeight + hoffset), 2, 2);
			}
		}
	}
}
