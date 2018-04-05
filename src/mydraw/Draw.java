package mydraw;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*; //++

/** The application class. Processes high-level commands sent by GUI */
public class Draw {
	/** main entry point. Just create an instance of this application class */
	public static void main(String[] args) {
		new Draw();
	}

	/** Application constructor: create an instance of our GUI class */
	public Draw() {
		window = new DrawGUIs(this);
	}

	protected DrawGUIs window; // chg

	/**
	 * This is the application method that processes commands sent by the GUI
	 */
	public void doCommand(String command) {
		if (command.equals("clear")) { // clear the GUI window
			// It would be more modular to include this functionality in the GUI
			// class itself. But for demonstration purposes, we do it here.
			Graphics g = window.getGraphics();
			g.setColor(window.getBackground());
			g.fillRect(0, 0, window.getSize().width, window.getSize().height);
		} else if (command.equals("quit")) { // quit the application
			window.dispose(); // close the GUI
			System.exit(0); // and exit.
		} else if (command.equals("auto")) { // quit the application
			autoDraw();
		}
	}
	public void drawOval(Point upper_left, Point lower_right) {
		window.drawOval(upper_left.x, upper_left.y, lower_right.x, lower_right.y);
	} 
	public void autoDraw() {
		// TODO Auto-generated method stub
		drawOval(new Point(100,200), new Point (400,400));
		drawOval(new Point(300,300), new Point (500,500));
		drawOval(new Point(700,600), new Point (1000,800));
	}
}