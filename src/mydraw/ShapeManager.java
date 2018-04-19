package mydraw;
/**
 * @author ptp18-d06(Pawel Rasch, Tim Runge)
 */

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;

// this class determines how mouse events are to be interpreted,
// depending on the shape mode currently set
class ShapeManager implements ItemListener {
	DrawGUIs gui;
	// TODO fix cyclic reference
	ScribbleDrawer scribbleDrawer;
	RectangleDrawer rectDrawer;
	OvalDrawer ovalDrawer;
	ShapeDrawer currentDrawer;

	public ShapeManager(DrawGUIs itsGui) {
		gui = itsGui;
		scribbleDrawer = new ScribbleDrawer(this);
		rectDrawer = new RectangleDrawer(this);
		ovalDrawer = new OvalDrawer(this);		
		// default: scribble mode
		currentDrawer = scribbleDrawer;
		// activate scribble drawer
		gui.drawingArea.addMouseListener(currentDrawer);
		gui.drawingArea.addMouseMotionListener(currentDrawer);
	}

	// reset the shape drawer
	public void setCurrentDrawer(ShapeDrawer l) {
		if (currentDrawer == l)
			return;

		// deactivate previous drawer
		gui.drawingArea.removeMouseListener(currentDrawer);
		gui.drawingArea.removeMouseMotionListener(currentDrawer);
		// activate new drawer
		currentDrawer = l;
		gui.drawingArea.addMouseListener(currentDrawer);
		gui.drawingArea.addMouseMotionListener(currentDrawer);
	}

	// user selected new shape => reset the shape mode
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem().equals("Scribble")) {
			setCurrentDrawer(scribbleDrawer);
		} else if (e.getItem().equals("Rectangle")) {
			setCurrentDrawer(rectDrawer);
		} else if (e.getItem().equals("Oval")) {
			setCurrentDrawer(ovalDrawer);
		}
	}
	/**
	 * Get this shapeManager's gui. TODO implement with listener(?) to eliminate cyclic reference
	 * 
	 * @return
	 */
	public DrawGUIs getGui() {
		return gui;
	}
	
	/**
	 * Get the drawingArea of this shapeManager's gui.
	 * @return
	 */
	public JDrawingArea getDrawingArea() {
		return gui.drawingArea;
	}
	
		// TODO fix structure to make this unnecessary
	public void dispose(){
		gui.drawingArea.removeMouseListener(currentDrawer);
		gui.drawingArea.removeMouseMotionListener(currentDrawer);
		gui = null;
		
	}
}

