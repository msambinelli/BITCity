package bitcity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Tree extends WorldObject {

	private static int GROW_DELAY = 7000;
	private World world;
	private Point root;
	private int size = 1;
	private int maxSize = 13;
	private int[] buildOrder;

	public Tree(World world, Point root) {
		this.root = root;
		this.world = world;
		this.buildOrder = new int[this.maxSize];
	}
	
	public void run() {
		int index, temp;
		int delay;
		
		int[] availableOrder = new int[this.maxSize];
		for (int i = 0; i < this.maxSize; i++) {
			availableOrder[i] = i;
		}
		
		while (true) {
			delay = (int)Math.round(Tree.GROW_DELAY / (this.world.getWorldSpeed() * 1.5));
			try {
				Thread.sleep(delay);
				if (Math.random() < 0.3 && this.size <= this.maxSize) {
					index = Application.random.nextInt(this.maxSize - this.size + 1);
					this.buildOrder[this.size - 1] = availableOrder[index] + 1;
					temp = availableOrder[this.maxSize - this.size];
					availableOrder[this.maxSize - this.size] = availableOrder[index];
					availableOrder[index] = temp;
					
					/*if (this.maxSize == this.size) {
						for (int i = 0; i < this.size; i++) {
							System.out.println(this + ", i = " + i + ", " + this.buildOrder[i]);
						}
					}*/
					this.size++;
				}
			} catch (InterruptedException e) {
				System.err.println("Finishing: " + e.getMessage());
			}
		}
	}
	
	@Override
	void draw(Graphics2D ctx, float tileWidth, float tileHeight) {
		int i = this.root.x, j = this.root.y;

		ctx.setColor(new Color((float)0.57, (float)0.24, (float)0.07));
		ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 3.)), (int)((i - 1) * tileHeight), 
				(int)(tileWidth/3. + 1), (int)tileHeight + 1);
		ctx.fillRect((int)((j - 1) * tileWidth), (int)((i - 1) * tileHeight + (tileHeight / 3.)), 
				(int)(tileWidth/3. + 1), 2);
		ctx.fillRect((int)((j - 1) * tileWidth + tileWidth/2. + 2),
				(int)((i - 1) * tileHeight + (tileHeight / 1.5)), 
				(int)(tileWidth/3. + 1), 2);
		
		ctx.setColor(new Color((float)0.19, (float)0.80, (float)0.19));
		
		for (int x = 0; x < this.buildOrder.length; x++) {
			switch (this.buildOrder[x]) {
			case 13:
				ctx.fillRect((int)((j - 1) * tileWidth - (tileWidth / 3.5)), 
						(int)((i - 1) * tileHeight + (tileHeight / 4.)), 
						(int)(tileWidth/3. + 1 + 2), (int)(tileHeight/3.5 + 1));
				break;
			case 12:
				ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 3.)), 
						(int)((i - 1) * tileHeight - (0.1 * tileHeight)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3.5 + 1));
				break;
			case 11:
				ctx.fillRect((int)((j - 1) * tileWidth + (0.2 * tileWidth)), 
						(int)((i - 1) * tileHeight + (0.2 * tileHeight)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3.5 + 1));
				break;
			case 10:
				ctx.fillRect((int)((j - 1) * tileWidth), 
						(int)((i - 1) * tileHeight), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3.5 + 1));
				break;
			case 9:
				ctx.fillRect((int)((j - 1) * tileWidth), 
						(int)((i - 1) * tileHeight + (tileHeight / 2.)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3.5 + 1));
				break;
			case 8:
				ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 10.)), 
						(int)((i - 1) * tileHeight + (tileHeight / 2.)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3.5 + 1));
				break;
			case 7:
				ctx.fillRect((int)((j - 1) * tileWidth), 
						(int)((i - 1) * tileHeight + (tileHeight / 2.)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3.5 + 1));
				break;
			case 6:
				ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 2.5)), 
						(int)((i - 1) * tileHeight + (tileHeight / 2.)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3.5 + 1));
				break;
			case 5:
				ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 2.5)), 
						(int)((i - 1) * tileHeight + (tileHeight / 2.5)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/5. + 1));
				break;
			case 4:
				ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 2.5)), 
						(int)((i - 1) * tileHeight + (tileHeight / 3.)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/5. + 1));
				break;
			case 3:
				ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 1.5)), 
						(int)((i - 1) * tileHeight + (tileHeight / 3.)), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3. + 1));
				break;
			case 2:
				ctx.fillRect((int)((j - 1) * tileWidth + (tileWidth / 2.)), (int)((i - 1) * tileHeight), 
						(int)(tileWidth/3. + 1), (int)(tileHeight/3. + 1));
				break;
			}
		}
	}

}
