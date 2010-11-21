package bitcity;

import java.awt.Graphics2D;

public abstract class WorldObject extends Thread {
	
	abstract void draw(Graphics2D g, float tileWidth, float tileHeight);
}
