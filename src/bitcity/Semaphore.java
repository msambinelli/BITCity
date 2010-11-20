package bitcity;

import java.awt.Point;


public class Semaphore {
	
	private Point position;
	public static enum SEM_STATUS {CLOSED, ALERT, OPEN};
	private int delay1, delay2;
	private SEM_STATUS status;
	
	/* groupSize = amount of Semaphores for the same letter. */
	private int groupSize;
	private boolean delayAdjusted = false;
	
	public Semaphore() {
		this.position = new Point(-1, -1);
		this.status = SEM_STATUS.OPEN;
		this.groupSize = 1;
		this.delay1 = (int)(Math.random() * 500) + 2700;
		this.delay2 = (int)(Math.random() * 700) + 1700;
		if (Application.DEBUG)
			System.out.println("Delay 1 = " + this.delay1 + ", Delay 2 = " + this.delay2);
	}
	
	public void incGroupSize() {
		this.groupSize++;
	}
	
	public void adjustDelay() {
		if (this.delayAdjusted)
			return;
		if (Application.DEBUG) System.out.println("Size: " + this.groupSize);
		this.delay1 = (int)Math.round(this.delay1 / (0.5 * this.groupSize));
		this.delay2 = (int)Math.round(this.delay2 / (0.5 * this.groupSize));
		if (Application.DEBUG) System.out.println("    " + this.delay1 + ", " + this.delay2);
	}
	
	/* Ilustração do primeiro problema de não se usar syncronized neste local
	 * 
	 * + A thread T1 invoca open(P1), onde P1 é o seu ponto de localização
	 * + O ponto P1 ficou verde
	 * + 1 milésimo de segundo depois a thread T2 invoca open(P2)
	 * + O ponto P2 ficou verde e P1 vermelho, mas não se passou ainda os 5s que T1 dinha direito 
	 * de ficar aberto
	 * 
	 * Ilustração do segundo problema
	 * 
	 * 
	 * 
	 */
	public synchronized void open(Point p){
		this.position = p;
		this.status = SEM_STATUS.OPEN;
		try {
			Thread.sleep(this.delay1);
			this.status = SEM_STATUS.ALERT;
			Thread.sleep(this.delay2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SEM_STATUS status(int i, int j){
		Point p = new Point(i, j);
		if (this.position.equals(p)){
			return this.status;
		} else {
			return SEM_STATUS.CLOSED;
		}
	}
	
	public SEM_STATUS status(Point p){
		if (this.position.equals(p)){
			return this.status;
		} else {
			return SEM_STATUS.CLOSED;
		}
	}
}
