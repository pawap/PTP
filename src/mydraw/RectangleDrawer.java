package mydraw;
/**
 * @author ptp18-d06(Pawel Rasch, Tim Runge)
 */

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

// if this class is active, rectangles are drawn
class RectangleDrawer extends ShapeDrawer {
	/**
	 * 
	 */
	protected final ShapeManager shapeManager;

	/**
	 * @param shapeManager
	 */
	RectangleDrawer(ShapeManager shapeManager) {
		this.shapeManager = shapeManager;
	}

	int pressx, pressy;
	int lastx = -1, lasty = -1;

	// mouse pressed => fix first corner of rectangle
	public void mousePressed(MouseEvent e) {
		pressx = e.getX();
		pressy = e.getY();
	}

	// mouse released => fix second corner of rectangle
	// and draw the resulting shape
	public void mouseReleased(MouseEvent e) {
		Graphics2D g = (Graphics2D) this.shapeManager.getDrawingArea().getImageGraphics(); //draw on image
		g.setStroke(new BasicStroke(shapeManager.gui.pencilSize,                     // Line width
                BasicStroke.CAP_SQUARE,    // End-cap style
                BasicStroke.JOIN_MITER));
		if (lastx != -1) {
			// first undraw a rubber rect
			g.setXORMode(this.shapeManager.gui.fgColor);
			g.setColor(this.shapeManager.gui.getBackground());
			doDraw(pressx, pressy, lastx, lasty, g);
			lastx = -1;
			lasty = -1;
		}
		// these commands finish the rubberband mode
		
		g.setPaintMode();
		g.setColor(this.shapeManager.gui.fgColor);
		// draw the finel rectangle
		doDraw(pressx, pressy, e.getX(), e.getY(), g);
	}

	// mouse released => temporarily set second corner of rectangle
	// draw the resulting shape in "rubber-band mode"
	public void mouseDragged(MouseEvent e) {
		Graphics2D g = (Graphics2D) this.shapeManager.getDrawingArea().getImageGraphics();
		g.setStroke(new BasicStroke(shapeManager.gui.pencilSize,                     // Line width
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER));
		// these commands set the rubberband mode
		g.setXORMode(this.shapeManager.gui.fgColor);
		g.setColor(this.shapeManager.gui.getBackground());
		if (lastx != -1) {
			// first undraw previous rubber rect
			doDraw(pressx, pressy, lastx, lasty, g);

		}
		lastx = e.getX();
		lasty = e.getY();
		// draw new rubber rect
		doDraw(pressx, pressy, lastx, lasty, g);
	}

	public void doDraw(int x0, int y0, int x1, int y1, Graphics g) {
		// calculate upperleft and width/height of rectangle
		int x = Math.min(x0, x1);
		int y = Math.min(y0, y1);
		int w = Math.abs(x1 - x0);
		int h = Math.abs(y1 - y0);
		// draw rectangle
		g.drawRect(x, y, w, h);
		this.shapeManager.getDrawingArea().repaint();
	}
}