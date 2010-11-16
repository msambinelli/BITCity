package bitcity;

import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public class Application {

	World world;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Application app = new Application();
		Parser parser;
		
		if (args.length != 1) {
			System.err.println("A map is required");
			return;
		}
		
		try {
			parser = new Parser(args[0]);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			return;
		}
	
		try {
			app.world = parser.parse();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Out of Bounds: " + e.getMessage());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			System.err.println("Exception caught while parsing: " + e.getMessage());
			return;
		}
		
		/* XXX Testing. */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Bitcity");
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				//frame.setPreferredSize(new Dimension(640, 398));
				frame.setPreferredSize(new Dimension(800, (int)(800/1.608)));
				frame.getContentPane().add(new WorldMap(app.world));
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
