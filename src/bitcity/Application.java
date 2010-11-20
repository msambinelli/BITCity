package bitcity;

import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.WindowConstants;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.Action;
import javax.swing.AbstractAction;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class Application {

	private World world;
	private JMenuItem increaseSpeed, decreaseSpeed, showWorldSpeed;
	private JCheckBoxMenuItem fullscreen;
	private JPopupMenu popup;
	private JFrame frame;
	private Dimension currSize;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Application app = new Application();
		final Parser parser;
		
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
		
		Sound.init();
		
		WorldMap bitcity = new WorldMap(app.world);
		Thread mapThread = new Thread(bitcity);
		
		
		/* GUI */
		app.frame = new JFrame("Bitcity");
		
		app.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		app.frame.setPreferredSize(new Dimension(parser.getWidthTiles() * 10,
				parser.getHeightTiles() * 17));
		app.frame.getContentPane().add(bitcity);
		app.frame.pack();
		app.frame.setLocationRelativeTo(null);
		app.frame.setVisible(true);
		
		@SuppressWarnings("serial")
		Action actionIncSpeed = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				app.world.incSpeed(true);
				app.showWorldSpeed.setText("World speed: " + app.world.getWorldSpeed());
			}
		};
		@SuppressWarnings("serial")
		Action actionDecSpeed = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				app.world.incSpeed(false);
				app.showWorldSpeed.setText("World speed: " + app.world.getWorldSpeed());
			}
		};
		@SuppressWarnings("serial")
		Action actionLeaveFullscreen = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				if (gd.getFullScreenWindow() == null)
					return;
				
				app.frame.dispose();
				app.frame.setUndecorated(false);
				app.frame.setSize(app.currSize);
				app.frame.setVisible(true);
				app.fullscreen.setSelected(false);
				gd.setFullScreenWindow(null);
			}
		};
		InputMap inputMap = bitcity.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke('+'), "incSpeed");
		inputMap.put(KeyStroke.getKeyStroke('='), "incSpeed");
		inputMap.put(KeyStroke.getKeyStroke('-'), "decSpeed");
		inputMap.put(KeyStroke.getKeyStroke('_'), "decSpeed");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "escape");
		ActionMap actionMap = bitcity.getActionMap();
		actionMap.put("incSpeed", actionIncSpeed);
		actionMap.put("decSpeed", actionDecSpeed);
		actionMap.put("escape", actionLeaveFullscreen);
		
		app.increaseSpeed = new JMenuItem("Faster");
		app.decreaseSpeed = new JMenuItem("Slower");
		app.increaseSpeed.setAccelerator(KeyStroke.getKeyStroke('+'));
		app.decreaseSpeed.setAccelerator(KeyStroke.getKeyStroke('-'));
		ButtonHandler handler = app.new ButtonHandler();
		app.increaseSpeed.addActionListener(handler);
		app.decreaseSpeed.addActionListener(handler);
		app.fullscreen = new JCheckBoxMenuItem("Fullscreen");
		CheckHandler checkHandler = app.new CheckHandler();
		app.fullscreen.addItemListener(checkHandler);
		
		app.showWorldSpeed = new JMenuItem("World speed: " + app.world.getWorldSpeed());
		app.popup = new JPopupMenu();
		app.popup.add(app.showWorldSpeed);
		app.popup.getComponent(0).setEnabled(false);
		app.popup.add(app.increaseSpeed);
		app.popup.add(app.decreaseSpeed);
		app.popup.addSeparator();
		app.popup.add(app.fullscreen);
		
		app.frame.addMouseListener(
				new MouseAdapter() {
					public void mousePressed(MouseEvent event) {
						if (event.isPopupTrigger()) {
							app.popup.show(event.getComponent(), event.getX(), event.getY());
						}
					}
				});
		/* End GUI. */
		
		mapThread.start();
	}

	
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			world.incSpeed(event.getSource() == increaseSpeed);
			showWorldSpeed.setText("World speed: " + world.getWorldSpeed());
		}
	}

	private class CheckHandler implements ItemListener {
		public void itemStateChanged(ItemEvent s) {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			
			if (fullscreen.isSelected()) {
				currSize = frame.getSize(); 
				frame.dispose();
				frame.setUndecorated(true);
				frame.setVisible(true);
				
				gd.setFullScreenWindow(frame);
			} else {
				frame.dispose();
				frame.setUndecorated(false);
				frame.setSize(currSize);
				frame.setVisible(true);

				gd.setFullScreenWindow(null);
			}
		}
	}
}