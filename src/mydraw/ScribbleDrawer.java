package mydraw;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

// if this class is active, the mouse is interpreted as a pen
class ScribbleDrawer extends ShapeDrawer {
	/**
	 * 
	 */
	private ShapeManager shapeManager;
	private JDrawingArea drawingArea;
	private DrawGUIs gui;
	/**
	 * @param shapeManager
	 */
	ScribbleDrawer(ShapeManager shapeManager) {
		this.shapeManager = shapeManager;
		System.out.println(shapeManager.getDrawingArea());
		this.drawingArea = shapeManager.getDrawingArea();
		this.gui = shapeManager.getGui();
		
		
	}

	int lastx, lasty;

	public void mousePressed(MouseEvent e) {
		lastx = e.getX();
		lasty = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		Graphics g = drawingArea.getImageGraphics();
		int x = e.getX();
		int y = e.getY();
		Point topLeft = drawingArea.getLocation();
		
		if (!gui.getContentPane().contains(x+topLeft.x, y+topLeft.y)) {	
			lastx = x;
			lasty = y;
			return;
		}
		g.setColor(gui.color);
		g.setPaintMode();
		g.drawLine(lastx, lasty, x, y);
		drawingArea.repaint();
		lastx = x;
		lasty = y;
	}
}