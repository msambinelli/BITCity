package bitcity;
import java.awt.*; 
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import widget.Semaphore;

public class Map extends Frame {
  
	private static final long serialVersionUID = -7474379826429102235L;
	private ArrayList<Semaphore> semaphore;
	
	public Map(){
		super("BITCity");
		
		semaphore = new ArrayList<Semaphore>();
		
		setSize(500, 400);
		center();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				dispose();
				System.exit(0);
			}
		});

		this.setVisible(true);
	}
	
	public void center() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		int x = (screenSize.width - frameSize.width) / 2;
		int y = (screenSize.height - frameSize.height) / 2;
		setLocation(x, y);
	}
	
	public int addSemaphore(int x, int y){
		Semaphore e = new Semaphore(x, y);
		semaphore.add(e);
		return semaphore.size() - 1;
	}
	
	public void openSemaphore(int id){
		semaphore.get(id).open();
	}
	
	public void closeSemaphore(int id){
		semaphore.get(id).close();
	}
	
	// da pra fazer um update mais eficente, talvez...
	public void update(Graphics g){ paint(g); }
	
	public void paint(Graphics g) {
		Graphics2D g2D=(Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Dimension d = getSize();
		Image mImage = createImage(d.width, d.height);
		Graphics2D offG = (Graphics2D) mImage.getGraphics();
		offG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		offG.setColor(getBackground());
		offG.fillRect(0, 0, d.width, d.height);
	
		for (int i = 0; i < semaphore.size(); i++){
			semaphore.get(i).draw(offG);
		}
		g2D.drawImage(mImage, 0, 0, null);
	}

	public int sizeSemaphore() {
		return semaphore.size();
	}
}


