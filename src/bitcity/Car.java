package bitcity;

import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

/*import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;*/

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import bitcity.Semaphore.SEM_STATUS;

public class Car extends MovingObject {
	private static double changeDirectionProb = 0.2;
	protected World world;
	private int state = World.CAR_RUNNING;
	public static final Color COLOR = Color.BLUE;
	//private boolean honking = false;
	
	public Car(World world, Point startPos, char direction) {
		super(startPos, direction);
		this.world = world;
		WorldMap.carCount++;
	}
	
	public static Car createCar(World world, Point startPos) throws Exception {
		char up, down, left, right;
		char direction;
		
		/* XXX Sempre considere x e y como linha e coluna, respectivamente. */
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
		
		return new Car(world, startPos, direction);
	}
	
	public int moveRate() {
		return this.world.getWorldSpeed() * 5;
	}
	
	public synchronized void move() throws Exception {
		Point nextPos;
		double prob;
		char ahead = this.world.getElementAhead(this.direction, this.pos);
		
		if (ahead == Parser.SENTINEL || ahead == Parser.GARAGE) {
			/* '&' simboliza estacionamento agora. */
			this.world.setRoadElement(this.pos.x, this.pos.y, World.ROAD);
			throw new Exception("Destroy car");
		}
		
		if (this.world.getRoadElement(this.pos.x, this.pos.y) == World.AMBULANCE) {
			throw new Exception("I've been wiped out!");
		}
		
		Point pAhead = this.getNextPos(this.direction, this.pos);
		
		if (this.world.getSemaphores().containsKey(ahead)) {
			
			if (this.world.getSemaphores().get(ahead).status(pAhead) != SEM_STATUS.CLOSED){
				if ((this.world.getRoadElement(pAhead.x, pAhead.y) & World.CAR_STOPPED) != World.CAR_STOPPED){
					this.world.setRoadElement(this.pos.x, this.pos.y, World.ROAD);
					this.pos = this.getNextPos(this.direction, this.pos);
					this.state = World.CAR_RUNNING;
				} else {
					this.state = World.CAR_STOPPED | World.CAR_HONKING;
					this.world.setRoadElement(this.pos.x, this.pos.y, this.state);
					this.bell();
				}
			} else {
				this.state = World.CAR_STOPPED;
				this.world.setRoadElement(this.pos.x, this.pos.y, this.state);
				//System.out.println(this.world.getRoadElement(this.pos.x, this.pos.y));
			}
			
		} else {
			//System.out.println("ahead " + ahead );
			if ((this.world.getRoadElement(pAhead.x, pAhead.y) & World.CAR_STOPPED) != World.CAR_STOPPED){
				this.world.setRoadElement(this.pos.x, this.pos.y, World.ROAD);
				this.pos = this.getNextPos(this.direction, this.pos);
				this.state = World.CAR_RUNNING;
				if (ahead != ' ' && ahead != Parser.SIDEWALK && ahead != this.direction) {
					/* Chance de mudar de direçao. */
					nextPos = this.getNextPos(ahead, this.pos);
					if (this.world.getElementAt(nextPos) == ahead) {
						/* Com certeza precisa mudar de direção. */
						this.direction = ahead;
					} else {
						/* Tem chance de trocar de direção. */
						prob = Math.random();
						if (prob < changeDirectionProb) {
							this.direction = ahead;
						}
					}
				}
			} else {
				//System.out.println("proximo esta parado");
				this.state = World.CAR_STOPPED;
			}
			
			
		}
		this.world.setRoadElement(this.pos.x, this.pos.y, this.state);
	}
	
	/* XXX Not used */
	public void draw(Graphics2D g, float tileWidth, float tileHeight) {
	}
	
	public void run() {
		while (true) {
			try {
				this.move();
				Thread.sleep((int)(1000./this.moveRate()));
			} catch (Exception e) {
				if (Application.DEBUG) System.out.println(">>>> " + e.getMessage());
				break;
			}
			//System.out.println(this.pos);
		}
		WorldMap.carCount--;
	}
	
	public void bell(){
		File soundFile;
		int pick = (int)Math.round(Math.random() * 2);
		
		switch (pick) {
		case 0:
			soundFile = new File("data/beepbeep.wav");
			break;
		case 1:
			soundFile = new File("data/car-honk-1.wav");
			break;
		case 2:
			soundFile = new File("data/car-honk-2.wav");
			break;
		default:
			soundFile = new File("data/beepbeep.wav");
			System.err.println("Nice, 2 * 1 > 2");
		}
		

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
		/*
		if (honking == false) {
			Sound.CAR_HONK.play();
			Sound.CAR_HONK.addListener(new LineListener() {
				public void update(LineEvent event) {
					if (event.getType() != LineEvent.Type.STOP) {
						return;
					}
					honking = false;
				}
			});
			honking = true;
		}*/
	}

}

