package mydraw;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*; //++

/** This class implements the GUI for our application */
class DrawGUIs extends JFrame {
	Draw app; // A reference to the application, to send commands to.
	Color color;
	JDrawingArea drawingArea; 
	
	/**
	 * The GUI constructor does all the work of creating the GUI and setting up
	 * event listeners. Note the use of local and anonymous classes.
	 */
	public DrawGUIs(Draw application) {
		super("Draw"); // Create the window
		app = application; // Remember the application reference
		color = Color.black; // the current drawing color

		
		// selector for drawing modes
		Choice shape_chooser = new Choice();
		shape_chooser.add("Scribble");
		shape_chooser.add("Rectangle");
		shape_chooser.add("Oval");

		// selector for drawing colors
		Choice color_chooser = new Choice();
		color_chooser.add("Black");
		color_chooser.add("Green");
		color_chooser.add("Red");
		color_chooser.add("Blue");

		// Create menu buttons
		JButton clear = new JButton("Clear");
		JButton quit = new JButton("Quit");
		JButton auto = new JButton("Auto");
		JButton save = new JButton("Save");
		JButton load = new JButton("Load");

		// Set a LayoutManager, and add the choosers and buttons to the menu.
		JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		menu.add(new JLabel("Shape:"));
		menu.add(shape_chooser);
		menu.add(new JLabel("Color:"));
		menu.add(color_chooser);
		menu.add(clear);
		menu.add(quit);
		menu.add(auto);
		menu.add(save);
		menu.add(load);
		
		// Setup DrawingArea with a new BufferedImage, Default BGColor: white
		drawingArea = new JDrawingArea(new BufferedImage(400, 320, BufferedImage.TYPE_INT_ARGB));
		drawingArea.getImageGraphics().setColor(Color.WHITE);
		drawingArea.getImageGraphics().fillRect(0,0, 400, 320);
		drawingArea.setBackground(Color.WHITE);
		drawingArea.setLayout(new BorderLayout());
		
		// New Layout for Frame. TODO elaborate
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();		
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		this.add(menu, c);
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		this.add(drawingArea, c);
		
		// Here's a local class used for action listeners for the buttons
		class DrawActionListener implements ActionListener {
			private String command;

			public DrawActionListener(String cmd) {
				command = cmd;
			}

			public void actionPerformed(ActionEvent e) {
				app.doCommand(command);
			}
		}

		// Define action listener adapters that connect the buttons to the app
		clear.addActionListener(new DrawActionListener("clear"));
		quit.addActionListener(new DrawActionListener("quit"));
		auto.addActionListener(new DrawActionListener("auto"));
		save.addActionListener(new DrawActionListener("save"));
		load.addActionListener(new DrawActionListener("load"));
		
		shape_chooser.addItemListener(new ShapeManager(this));

		class ColorItemListener implements ItemListener {

			// user selected new color => store new color in DrawGUIs
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem().equals("Black")) {
					color = Color.black;
				} else if (e.getItem().equals("Green")) {
					color = Color.green;
				} else if (e.getItem().equals("Red")) {
					color = Color.red;
				} else if (e.getItem().equals("Blue")) {
					color = Color.blue;
				}
			}
		}

		color_chooser.addItemListener(new ColorItemListener());

		// Handle the window close request similarly
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.doCommand("quit");
			}
		});

		// Finally, set the size of the window, and pop it up
		this.setSize(500, 400);		
		this.setBackground(Color.white); // TODO obsolete. remove 
		// this.show(); //chg
		this.setVisible(true); // ++
	}
	
	/**
	 * draws a rectangle onto the drawingArea.
	 * @param x coordinate of the rectangle's upper left corner
	 * @param y coordinate of the rectangle's upper left corner
	 * @param x2 coordinate of the rectangle's bottom right corner
	 * @param y2 coordinate of the rectangle's bottom right corner
	 */
	public void drawRectangle(int x, int y, int x2, int y2) {
		ShapeManager shapeManager = new ShapeManager(this);
		RectangleDrawer rectDrawer = new RectangleDrawer(shapeManager);
		Graphics g = drawingArea.image.getGraphics();
		g.setColor(this.color);
		rectDrawer.doDraw(x, y, x2, y2, g);
		shapeManager.dispose();
	}
	
	/**
	 * draws an oval between two points onto the drawingArea.
	 * @param x coordinate of the first point
	 * @param y coordinate of the first point
	 * @param x2 coordinate of the first point
	 * @param y2 coordinate of the first point
	 */
	public void drawOval(int x, int y, int x2, int y2) {
		ShapeManager shapeManager = new ShapeManager(this);
		OvalDrawer ovalDrawer = new OvalDrawer(shapeManager);
		Graphics g = drawingArea.image.getGraphics();
		g.setColor(this.color);
		ovalDrawer.doDraw(x, y, x2, y2, g);
		shapeManager.dispose();
	}

	/**
	 * 
	 * @return 
	 */
	public Image getDrawing() {
		
		return drawingArea.image;
	}
	
	/**
	 * 
	 */
	public void setDrawing(Image img) {
		drawingArea.image = img;
		drawingArea.repaint();
	}
	
	/**
	 * @return the active color as a string 
	 */
	public String getFGColor() {
		return color.toString();
	}
	
	/**
	 * Sets the foreground color to the desired color.
	 * @param new_color one of the colors defined in the color_pool file as a String. Not case sensitive.
	 * @throws ColorException
	 */
	public void setFGColor(String new_color) throws ColorException{
		setAColor(color, new_color);			
	}
	
	/**
	 * Sets the background color to the desired color.
	 * @param new_color one of the colors defined in the color_pool file as a String. Not case sensitive.
	 * @throws ColorException
	 */
	public void setBGColor (String new_color) throws ColorException{
		Color bgcolor = Color.white;
		setAColor(bgcolor, new_color);
		drawingArea.setBackground(bgcolor);
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
	
	/**
	 * @return the width of the drawing area
	 */
	public int getWidth() {
		return drawingArea.getWidth();
	}
	
	/**
	 * @return the height of the drawing area
	 */
	public int getHeight() {
		return drawingArea.getHeight();
	}
	
	/**
	 * 
	 * @return the background color as String
	 */
	public String getBGColor() {
		return drawingArea.getBackground().toString();
	}
}