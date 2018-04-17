package mydraw;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
		this.drawingArea = shapeManager.getDrawingArea();
		this.gui = shapeManager.getGui();
		
		
	}

	int lastx, lasty;

	public void mousePressed(MouseEvent e) {
		lastx = e.getX();
		lasty = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		Graphics2D g = (Graphics2D) drawingArea.getImageGraphics();
		int x = e.getX();
		int y = e.getY();
		Point topLeft = drawingArea.getLocation();
		
		if (!gui.getContentPane().contains(x+topLeft.x, y+topLeft.y)) {	
			lastx = x;
			lasty = y;
			return;
		}
		g.setColor(gui.fgColor);
		g.setStroke(new BasicStroke(gui.pencilSize,                     // Line width
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
		g.setPaintMode();
		g.drawLine(lastx, lasty, x, y);
		drawingArea.repaint();
		lastx = x;
		lasty = y;
	}
}