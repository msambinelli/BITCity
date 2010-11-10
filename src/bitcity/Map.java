package bitcity;
import java.awt.*; 
import widget.Street;
import javax.swing.*;

public class Map extends JFrame {
  public Map() {
    super("BITCity");
    setBounds(0,0,300,300);// não sei o que isso faz =)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container con=this.getContentPane();
    con.setBackground(Color.GREEN);
    Street canvas=new Street(0, 0, 300, 300); 
    con.add(canvas); 
    setVisible(true);
  }
}


