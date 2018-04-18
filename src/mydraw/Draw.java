package mydraw;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import javax.swing.*; //++


/** The application class. Processes high-level commands sent by GUI */
public class Draw {
	
	protected DrawGUIs window; //the GUI
	protected ShapeManager shapeManager;
		
	/** main entry point. 
	 * Just creates an instance of this application class 
	 */
	public static void main(String[] args) {
		new Draw();
	}

	/** Application constructor:  
	 * creates an instance of our GUI class and initializes the default colors. 
	 */
	public Draw() {
		this(Color.black, Color.white, 1, 400, 320);
	}
	
	public Draw(Color fg, Color bg, int pSize, int w, int h) {
		window = new DrawGUIs(this, fg, bg, pSize, w, h);
		shapeManager = new ShapeManager(window);
		window.initShapeManager(shapeManager);
		window.drawingArea.requestFocusInWindow(); // TODO is this still necessary?
	}

	/**
	 * This is the application method that processes commands sent by the GUI
	 * @param command an element from {"clear", "quit", "auto", "save", "load"} 
	 */
	public void doCommand(String command) {
		if (command.equals("clear")) { // clear the GUI window
			// It would be more modular to include this functionality in the GUI
			// class itself. But for demonstration purposes, we do it here.
			Graphics g = window.getImage().getGraphics(); //get Graphics for Image
			g.setColor(window.getBGColor());
			g.fillRect(0, 0, window.getSize().width, window.getSize().height);
			window.repaint();
		} else if (command.equals("quit")) { // quit the application
			window.dispose(); // close the GUI
			System.exit(0); // and exit.
		} else if (command.equals("auto")) { // draw auto-generated image
			autoDraw();
		} else if (command.equals("save")) { // save current image as bitmap
	        JFileChooser chooser = new JFileChooser();
	        int result = chooser.showOpenDialog(null);
	        if(result == JFileChooser.APPROVE_OPTION) {
	        	try {
					writeImage(getDrawing(),chooser.getSelectedFile().getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				} 
	        }
			
		} else if (command.equals("load")) { // load saved image
	        JFileChooser chooser = new JFileChooser();
	        int result = chooser.showOpenDialog(null);
	        if(result == JFileChooser.APPROVE_OPTION) {
				try {
					Image orgImg = readImage(chooser.getSelectedFile().getAbsolutePath());
					Image img = new BufferedImage(orgImg.getWidth(null), orgImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
					img.getGraphics().drawImage(orgImg, 0, 0, null);
					window.setImage(img);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		}
	}
	// TODO add javadoc & other comments!
	/**
	 * returns the image drawn on the GUI
	 * @return the current image
	 */
	public Image getDrawing(){
		return window.getImage();
	}

	/**
	 * Saves an image as Windows bitmap file (*.bmp) under the specified name.
	 * @param image the image to be saved
	 * @param filename the name under which the image will be saved
	 * @throws IOException
	 */
	public void writeImage(Image image, String filename) throws IOException{
		MyBMPFile.write(filename, image);		
	}

	/**
     * Read a Windows bitmap file (*.bmp) 
     * @param filename the image's file name
     * @return the image read 
	 * @throws IOException
	 */
	public Image readImage(String filename) throws IOException{
		return MyBMPFile.read(filename);		
	}

	/**
	 * Draws a rectangle on the GUI's drawing area
	 * @param upper_left the top left corner of the rectangle
	 * @param lower_right the bottom right corner of the rectangle
	 */
	public void drawRectangle(Point upper_left, Point lower_right) {
		window.drawRectangle(upper_left.x, upper_left.y, lower_right.x, lower_right.y);
	}

	/**
	 * Draws an oval on the GUI's drawing area
	 * @param upper_left the top left corner of the invisible rectangle that encloses the oval
	 * @param lower_right the bottom right corner of the invisible rectangle that encloses the oval
	 */
	public void drawOval(Point upper_left, Point lower_right) {
		window.drawOval(upper_left.x, upper_left.y, lower_right.x, lower_right.y);
	} 

	/**
	 * connects the points it receives with lines on the drawing area.
	 * @param points a list of points that you want to be connected by lines
	 */
	public void drawPolyLine(java.util.List<Point> points) {
		window.drawPolyLine(points);		
	}
	
	/**
	 * draws a predefined image onto the drawing area using every available color
	 * and every available drawing method.
	 */
	public void autoDraw() {
		try {	
			setBGColor("green");
			setFGColor("WHITE");
			drawOval(new Point(100,200), new Point (200,300));
			setFGColor("rEd");
			drawOval(new Point(10,10), new Point (100,200));
			setFGColor("Blue");
			drawOval(new Point(70,100), new Point (300,200));
			setFGColor("blACK");
			LinkedList<Point> polyLine = new LinkedList<Point>();
			polyLine.add(new Point(100,200));
			polyLine.add(new Point (200,300));
			polyLine.add(new Point(10,10));
			polyLine.add(new Point (100,200));
			polyLine.add(new Point(70,100));
			polyLine.add(new Point (300,200));
			drawPolyLine(polyLine);

			drawRectangle(new Point(100,200), new Point (200,100));
			drawRectangle(new Point(70,100), new Point (300,300));
			drawRectangle(new Point(10,10),new Point (200,300));
		}
		catch (ColorException e){
			System.out.println(e.getMessage());			
		}
	}

	/**Returns the width of the drawing area.
	 * @return the width of the drawing area
	 */
	public int getWidth() {
		Image img = window.getImage();
		return img.getWidth(null);
	}

	/**
	 * Sets the width of the drawing area.
	 * @param new_width the desired width
	 */
	public void setWidth(int new_width) {
		window.resizeDrawingArea(new_width, getHeight());
	}

	/**Returns the height of the drawing area.
	 * @return the height of the drawing area
	 */
	public int getHeight() {
		Image img = window.getImage();
		return img.getHeight(null);
	}
	
	/**
	 * Sets the height of the drawing area. 
	 * @param new_height the desired height
	 */
	public void setHeight(int new_height) {
		window.resizeDrawingArea(getWidth(), new_height);
	}

	/**Returns a string representing the current drawing color.
	 * @return the active color as a string 
	 */
	public String getFGColor() {
		return MyColor.colorToString(window.getFGColor());
	}

	/**Returns a string representing the current background color.
	 * @return the background color as a string 
	 */
	public String getBGColor() {
		return MyColor.colorToString(window.getBGColor());
	}

	/**
	 * Sets the foreground color to the desired color.
	 * @param new_color one of the colors defined in the color_pool file as a String. Not case sensitive.
	 * @throws ColorException
	 */
	public void setFGColor(String new_color) throws ColorException{
		window.setFGColor(MyColor.stringToColor(new_color));
	}

	/**
	 * Sets the background color to the desired color and changes each pixel that has the old color to the new one.
	 * @param new_color one of the colors defined in the color_pool file as a String. Not case sensitive.
	 * @throws ColorException
	 */
	public void setBGColor(String new_color) throws ColorException{
		//local vars
		JDrawingArea drawingArea = window.getDrawingArea();
		Color new_col = MyColor.stringToColor(new_color);
		
		window.switchColor(new_col, window.getBGColor());
		window.setBGColor(new_col);
		drawingArea.repaint();
	}




}