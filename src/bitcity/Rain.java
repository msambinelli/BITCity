package bitcity;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Rain {
	
	private boolean raining = false;
	private int drop_max;
	private int width;
	private int height;
	private int time;
	private int elapsed;

	final private int lenght_min = 4;
	
	public Rain(int width, int height){
		this.drop_max = (int) (width * height / 2);
		this.width = width;
		this.height = height;
	}
	
	public Point[] getDrops(){
		//int size = (int) (Math.random() * drop_max);
		
		int meio = (int) (this.time / 2);
		float rain_force;
		if (this.elapsed <= meio){
			rain_force = (float) (this.elapsed) / meio;
		} else {
			rain_force = 1 - (float) (this.elapsed - meio ) / (this.time - meio);
		}
		
		if (rain_force > 0.8 && Math.random() < 0.3){
			//this.thunder();
		}
		
		int size = (int) (drop_max * rain_force);
		Point[] rain = new Point[size];
		
		//System.out.println("ela: " + this.elapsed + " time: " + this.time + "force " + rain_force);
		Random generator = new Random();
		for (int i = 0; i < size; i++){
			rain[i] = new Point();
			rain[i].x = Math.abs(generator.nextInt()) % this.width;
			rain[i].y = Math.abs(generator.nextInt()) % this.height;
		}
		this.elapsed++;
		if (this.elapsed == this.time){
			this.raining = false;
			//Sound.RAIN.stop();
		}
		return rain;
	}
	
	public void startRain(int frame_rate){
		this.elapsed = 0;
		Random generator = new Random();
		this.time = this.lenght_min + (Math.abs(generator.nextInt()) % 16);
		this.time *= frame_rate;
		this.raining = true;
		//Sound.RAIN.loop();
	}

	public boolean isRaining() {
		return raining;
	}

	public void setRaining(boolean raining) {
		this.raining = raining;
	}
	
	private void thunder(){
		
		new Thread(new Runnable() {
			
			public void run(){
				File soundFile;
		
				soundFile = new File("data/thunder.wav");
		
		
				AudioInputStream audioInputStream = null;
				try { 
					audioInputStream = AudioSystem.getAudioInputStream(soundFile);
				} catch (UnsupportedAudioFileException e1) { 
					e1.printStackTrace();
					return;
				} catch (IOException e1) { 
					e1.printStackTrace();
					return;
				} 
		
				AudioFormat format = audioInputStream.getFormat();
				SourceDataLine auline = null;
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		
				try { 
					auline = (SourceDataLine) AudioSystem.getLine(info);
					auline.open(format);
				} catch (LineUnavailableException e) { 
					e.printStackTrace();
					return;
				} catch (Exception e) { 
					e.printStackTrace();
					return;
				} 
		
				auline.start();
				int nBytesRead = 0;
				byte[] abData = new byte[524288]; // 128Kb 
		
				try { 
					while (nBytesRead != -1) { 
						nBytesRead = audioInputStream.read(abData, 0, abData.length);
						if (nBytesRead >= 0) 
							auline.write(abData, 0, nBytesRead);
					} 
				} catch (IOException e) { 
					e.printStackTrace();
					return;
				} finally { 
					auline.drain();
					auline.close();
				}
			}
		}).start();
	}
}
