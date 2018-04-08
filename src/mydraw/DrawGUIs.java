package mydraw;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

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
	 * 
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
}