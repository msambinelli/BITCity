package bitcity;

import java.awt.Point;
import java.util.Random;

public class Rain {
	
	private boolean raining = false;
	private int drop_max;
	private int width;
	private int height;
	private int time;
	private int elapsed;

	final private int lenght_min = 4;
	
	public Rain(int width, int height){
		this.drop_max = (int) (width * height/ 2);
		this.width = width;
		this.height = height;
	}
	
	public Point[] getDrops(){
		int size = (int) (Math.random() * drop_max);
		Point[] rain = new Point[size];
		
		System.out.println("ela: " + this.elapsed + " time: " + this.time );
		Random generator = new Random();
		for (int i = 0; i < size; i++){
			rain[i] = new Point();
			rain[i].x = generator.nextInt() % this.width;
			rain[i].y = generator.nextInt() % this.height;
		}
		this.elapsed++;
		if (this.elapsed == this.time){
			this.raining = false;
			System.out.println("parei");
		}
		return rain;
	}
	
	public void startRain(int frame_rate){
		this.elapsed = 0;
		Random generator = new Random();
		this.time = this.lenght_min + (Math.abs(generator.nextInt()) % 16);
		this.time *= frame_rate;
		this.raining = true;
	}

	public boolean isRaining() {
		return raining;
	}

	public void setRaining(boolean raining) {
		this.raining = raining;
	}
}
