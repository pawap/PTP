package mydraw;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*; //++

import com.sun.xml.internal.ws.util.StringUtils;



/** This class implements the GUI for our application */
class DrawGUIs extends JFrame {
	Draw app; // A reference to the application, to send commands to.
	Color color, bgColor;
	JDrawingArea drawingArea;
	JSizeMenu sizeMenu;
	Choice bg_color_chooser;

	/**
	 * The GUI constructor does all the work of creating the GUI and setting up
	 * event listeners. Note the use of local and anonymous classes.
	 */
	public DrawGUIs(Draw application) {
		super("Draw"); // Create the window
		app = application; // Remember the application reference
		color = Color.black; // the current drawing color
		bgColor = Color.white; //the background color


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
		
		bg_color_chooser = new Choice();
		bg_color_chooser.add("White");
		bg_color_chooser.add("Black");
		bg_color_chooser.add("Green");
		bg_color_chooser.add("Red");
		bg_color_chooser.add("Blue");
		
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
		menu.add(new JLabel("BGColor:"));
		menu.add(bg_color_chooser);
		menu.add(clear);
		menu.add(quit);
		menu.add(auto);
		menu.add(save);
		menu.add(load);

		// Setup DrawingArea with a new BufferedImage, Default BGColor: white
		drawingArea = new JDrawingArea(new BufferedImage(400, 320, BufferedImage.TYPE_INT_ARGB), new Dimension (400,320));
		drawingArea.getImageGraphics().setColor(bgColor);
		drawingArea.getImageGraphics().fillRect(0,0, 400, 320);
		drawingArea.setBackground(bgColor);
		drawingArea.setLayout(new BorderLayout());
		
		// Setup SizeMenu
		sizeMenu = new JSizeMenu(new Dimension(400,320));
		menu.add(sizeMenu);
		sizeMenu.addPropertyChangeListener(new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName() == "size") {
					Dimension size = (Dimension) evt.getNewValue();
					System.out.println(""+evt.getPropertyName());
					app.setHeight((int) size.getHeight());
					app.setWidth((int) size.getWidth());
				}
			}

		});
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
		class BGColorItemListener implements ItemListener {

			// user selected new bgColor => store new bgColor in DrawGUIs
			public void itemStateChanged(ItemEvent e) {
				try {
					app.setBGColor((String) e.getItem());
				} catch (ColorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		bg_color_chooser.addItemListener(new BGColorItemListener());
		// Handle the window close request similarly
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.doCommand("quit");
			}
		});

		// Finally, set the size of the window, and pop it up
		this.setSize(1000, 400);		
		this.setBackground(Color.white); // TODO obsolete. remove 
		// this.show(); //chg
		this.setVisible(true); // ++
	}

	public Color getBGColor() {
		return bgColor;
	}

	public void setBGColor(Color bgColor) {
		String colorStr = MyColor.colorToString(bgColor).toLowerCase();
		colorStr = StringUtils.capitalize(colorStr);
		bg_color_chooser.select(colorStr);
		this.bgColor = bgColor;
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

	protected JDrawingArea getDrawingArea() {
		return drawingArea;
	}

	protected void setFGColor(Color c) {
		color = c;
	}

	/**
	 * @return the active color as a Color 
	 */
	protected Color getFGColor() {
		return color;
	}

	/**
	 * 
	 * @return 
	 */
	public Image getImage() {

		return drawingArea.image;
	}

	/**
	 * 
	 */
	public void setImage(Image img) {
		drawingArea.image = img;
		drawingArea.repaint();
	}

	public void drawPolyLine(List<Point> points) {
		Graphics g = drawingArea.getImageGraphics();
		g.setColor(color);
		Point currentPoint = points.get(0);
		for (Point point: points) {
			g.drawLine(currentPoint.x,currentPoint.y,point.x, point.y);
			currentPoint = point;
		}
		drawingArea.repaint();

	}

	public void resizeDrawingArea(int w, int h) {
		BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = newImg.getGraphics();
		drawingArea.setSize(w, h);
		this.validate();
		g.fillRect(0,0, w, h);
		g.drawImage(drawingArea.image,0,0,null);
		drawingArea.image = newImg;
		drawingArea.repaint();		
	}

	public void switchColor(Color new_col, Color bgColor2) {
		BufferedImage image = (BufferedImage) drawingArea.image; 
		for (int x = 0; x < image.getWidth(null); ++x) {//check each pixel and change its color if needed
			for (int y = 0; y < image.getHeight(null); ++y) {
				if (image.getRGB(x, y) == getBGColor().getRGB()){
					image.setRGB(x, y, new_col.getRGB());
					
				}
			}
		}
		
	}
}