package widget;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/* Classe que Desenha uma Rua dado um ponto (x0, y0) inicial e um (x1, y1) final */
public class Street extends Canvas {
	
	int x0, y0, x1, y1;
	int street_height;
	int street_weight;
	int band_height;
	int band_weight;
	int gap;

  public void paint(Graphics g){ 
	  
	  Graphics2D g2D=(Graphics2D) g;
	  g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
	  
	  double size = Math.sqrt((y1 - y0) * (y1 - y0) + (x1 - x0) * (x1 - x0));
	  
	  g2D.translate(x0, y0);
	  
	  if (y1 - y0 != 0){
		  double co = y1 - y0;
		  System.out.println(co);
		  double hip = size;
		  System.out.println(hip);
		  double angle = Math.asin(co / hip);
		  g2D.rotate(angle);
		  System.out.println(angle);
	  }
	
	  g2D.setColor(Color.GRAY);
	  g2D.fillRect(0, -(street_height / 2), (int) size, street_height);
	  
	  g2D.setColor(Color.YELLOW);
	  for (int i = 0; i + band_weight < (int) size; i+= band_weight + gap){
		  g2D.fillRect(i,  - (band_height / 2), band_weight, band_height);
	  }
  }
  
  public Street(int x0, int y0, int x1, int y1){
	  band_height = 5;
	  band_weight = 10;
	  street_height = 30;
	  gap = 2;
	  
	  if (x0 < x1){
		  this.x0 = x0;
		  this.x1 = x1;
	  }	else {
		  this.x0 = x1;
		  this.x1 = x0;
	  }
	  
	  if (y0 < y1){
		  this.y0 = y0;
		  this.y1 = y1;
	  } else {
		  this.y0 = y1;
		  this.y1 = y0;
	  }
  }
}
