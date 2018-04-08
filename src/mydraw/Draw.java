package mydraw;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
		
		drawRectangle(new Point(100,200), new Point (200,100));
		drawRectangle(new Point(70,100), new Point (300,300));
		drawRectangle(new Point(10,10),new Point (200,300));
	}
	
		/**
	 * @return the width of the drawing area
	 */
	public int getWidth() {
		JDrawingArea drawingArea = window.getDrawingArea();
		return drawingArea.getImage().getWidth(drawingArea);
	}
	
	/**
	 * @return the height of the drawing area
	 */
	public int getHeight() {
		JDrawingArea drawingArea = window.getDrawingArea();
		return drawingArea.getImage().getHeight(drawingArea);
	}
	
	/**
	 * @return the active color as a string 
	 */
	public String getFGColor() {
		return window.getFGColor().toString();
	}
	
	/**
	 * @return the background color as a string 
	 */
	public String getBGColor() {
		return window.getDrawingArea().getBackground().toString();
	}
	
	/**
	 * Sets the foreground color to the desired color.
	 * @param new_color one of the colors defined in the color_pool file as a String. Not case sensitive.
	 * @throws ColorException
	 */
	public void setFGColor(String new_color) throws ColorException{
		Color fgColor = Color.black;
		setAColor(fgColor, new_color);
		window.setFGColor(fgColor);		
	}
	
	/**
	 * Sets the foreground color to the desired color.
	 * @param new_color one of the colors defined in the color_pool file as a String. Not case sensitive.
	 * @throws ColorException
	 */
	public void setBGColor(String new_color) throws ColorException{
		Color bgColor = Color.white;
		setAColor(bgColor, new_color);
		window.getDrawingArea().setBackground(bgColor);
	}
	
	/**
	 * helper method for setting colors.
	 * @param c_to_set the Color you want changed
	 * @param new_color one of the colors defined in the color_pool file as a String. Not case sensitive.
	 * @throws ColorException
	 */
	private void setAColor(Color c_to_set, String new_color) throws ColorException{
		List<String> color_pool = new ArrayList<String>();
		String n_c = new_color.toLowerCase();
				
		try {
			color_pool = Files.readAllLines(new File("color_pool").toPath());
		}
		catch (IOException e) 
		{
			System.out.println(e.toString());
		}
		
		if (color_pool.contains(n_c)) {
			switch (n_c) {
			case "black": c_to_set = Color.black;
						  break;				
			case "green": c_to_set = Color.green;
				          break;
			case "red":   c_to_set = Color.red;
				          break;
			case "blue":  c_to_set = Color.blue;
				          break;
			default:      System.out.println("setAColor did something stupid.");
			}
		}
		else {
			throw new ColorException();
		}
	}

	public void setHeight(int height) {
		BufferedImage newImg = new BufferedImage(this.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = newImg.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(), height);
		g.drawImage(window.drawingArea.image,0,0,null);
		window.drawingArea.image = newImg;
		window.drawingArea.repaint();
		
	}

	public void setWidth(int width) {
		// TODO Auto-generated method stub
		
	}
	
	
}