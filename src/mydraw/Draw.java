package mydraw;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

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
		window.drawingArea.requestFocusInWindow(); // TODO is this still necessary?
	}

	protected DrawGUIs window; // chg

	/**
	 * This is the application method that processes commands sent by the GUI
	 */
	public void doCommand(String command) {
		if (command.equals("clear")) { // clear the GUI window
			// It would be more modular to include this functionality in the GUI
			// class itself. But for demonstration purposes, we do it here.
			Graphics g = window.getDrawing().getGraphics(); //get Graphics for Image
			g.setColor(window.getBackground());
			g.fillRect(0, 0, window.getSize().width, window.getSize().height);
			window.repaint();
		} else if (command.equals("quit")) { // quit the application
			window.dispose(); // close the GUI
			System.exit(0); // and exit.
		} else if (command.equals("auto")) { // draw auto-generated pic
			autoDraw();
		} else if (command.equals("save")) { // save current image as bitmap
			try {
				writeImage(getDrawing(),"bull.bmp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else if (command.equals("load")) { // load saved image
			try {
				Image orgImg = readImage("bull.bmp");
				Image img = new BufferedImage(orgImg.getWidth(null), orgImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				img.getGraphics().drawImage(orgImg, 0, 0, null);
				window.setDrawing(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// TODO add javadoc & other comments!
	
	public Image getDrawing(){
		return window.getDrawing();
	}
	
	public void writeImage(Image image, String filename) throws IOException{
			MyBMPFile.write(filename, image);		
	}
	
	public Image readImage(String filename) throws IOException{
		return MyBMPFile.read(filename);		
	}
	
	public void drawRectangle(Point upper_left, Point lower_right) {
		window.drawRectangle(upper_left.x, upper_left.y, lower_right.x, lower_right.y);
	}
	
	public void drawOval(Point upper_left, Point lower_right) {
		window.drawOval(upper_left.x, upper_left.y, lower_right.x, lower_right.y);
	} 
	
	public void drawPolyLine(java.util.List<Point> points) {
		window.drawPolyLine(points);		
	}
	public void autoDraw() {
		// TODO Auto-generated method stub
		drawOval(new Point(100,200), new Point (200,300));
		drawOval(new Point(10,10), new Point (100,200));
		drawOval(new Point(70,100), new Point (300,200));

		LinkedList<Point> polyLine = new LinkedList<Point>();
		polyLine.add(new Point(100,200));
		polyLine.add(new Point (200,300));
		polyLine.add(new Point(10,10));
		polyLine.add(new Point (100,200));
		polyLine.add(new Point(70,100));
		polyLine.add(new Point (300,200));
		drawPolyLine(polyLine);
	
	}
	
}