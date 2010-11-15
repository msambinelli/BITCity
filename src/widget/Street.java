package widget;


import java.awt.Color;
import java.awt.Graphics2D;


/* Classe que Desenha uma Rua dado um ponto (x0, y0) inicial e um (x1, y1) final */
public class Street implements Widget {
	
	private int x0, y0, x1, y1;
	
	private static final int street_height = 30;
	private static final int band_height = 5;
	private static final int band_width = 10;
	private static final int gap = 2;

  public void draw(Graphics2D g2D){ 

	  double size = Math.sqrt((y1 - y0) * (y1 - y0) + (x1 - x0) * (x1 - x0));
	  double angle = 0;
	  g2D.translate(x0, y0);
	  
	  if (y1 - y0 != 0){
		  double co = y1 - y0;
		  double hip = size;
		  angle = Math.asin(co / hip);
		  g2D.rotate(angle);
	  }
	
	  g2D.setColor(Color.GRAY);
	  g2D.fillRect(0, -(street_height / 2), (int) size, street_height);
	  
	  g2D.setColor(Color.YELLOW);
	  for (int i = 0; i + band_width < (int) size; i+= band_width + gap){
		  g2D.fillRect(i,  - (band_height / 2), band_width, band_height);
	  }
	  
	  g2D.rotate(- angle);
	  g2D.translate(-x0, -y0);
  }
  
  public Street(int x0, int y0, int x1, int y1){

	  this.x0 = Math.min(x0, x1);
	  this.x1 = Math.max(x0, x1);

	  this.y0 = Math.min(y0, y1);
	  this.y1 = Math.max(y0, y1);
	  
  }
}
