package mydraw;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*; //++
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.xml.internal.ws.util.StringUtils;



/** This class implements the GUI for our application */
class DrawGUIs extends JFrame {
	Draw app; // A reference to the application, to send commands to.
	Color color, bgColor;
	int pencilSize;
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
		pencilSize = 1; 

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
		
		JSlider pencil_size_slider = new JSlider(1,20);
		pencil_size_slider.setValue(pencilSize);
		pencil_size_slider.setPaintTicks(true);
		pencil_size_slider.setPaintLabels(true);
		pencil_size_slider.setMajorTickSpacing(7);
		pencil_size_slider.setMinorTickSpacing(1);
		// Create menu buttons
		JButton clear = new JButton("Clear");
		JButton quit = new JButton("Quit");
		JButton auto = new JButton("Auto");
		JButton save = new JButton("Save");
		JButton load = new JButton("Load");



		// Set a LayoutManager, and add the choosers and buttons to the menu.
		JPanel menu = new JPanel(new GridLayout(2,1));
		JPanel toolMenu = new JPanel((new FlowLayout(FlowLayout.LEFT)));
		toolMenu.add(pencil_size_slider);
		toolMenu.add(new JLabel("Shape:"));
		toolMenu.add(shape_chooser);
		toolMenu.add(new JLabel("Color:"));
		toolMenu.add(color_chooser);
		toolMenu.add(new JLabel("BGColor:"));
		toolMenu.add(bg_color_chooser);
		toolMenu.add(clear);
		JPanel imageMenu = new JPanel(new FlowLayout(FlowLayout.LEFT,5,1));
		imageMenu.add(quit);
		imageMenu.add(auto);
		imageMenu.add(save);
		imageMenu.add(load);
		menu.add(toolMenu);
		menu.add(imageMenu);

		// Setup DrawingArea with a new BufferedImage, Default BGColor: white
		drawingArea = new JDrawingArea(new BufferedImage(400, 320, BufferedImage.TYPE_INT_ARGB), new Dimension (400,320));
		drawingArea.getImageGraphics().setColor(bgColor);
		drawingArea.getImageGraphics().fillRect(0,0, 400, 320);
		drawingArea.setBackground(bgColor);
		drawingArea.setLayout(new BorderLayout());
		
		// Setup SizeMenu
		sizeMenu = new JSizeMenu(new Dimension(400,320));
		imageMenu.add(sizeMenu);
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
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		this.add(menu, c);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
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
		
		class SliderListener implements ChangeListener {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				pencilSize = slider.getValue();
				
			}
			
		}
		
		pencil_size_slider.addChangeListener(new SliderListener());
		
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
		this.pack();
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