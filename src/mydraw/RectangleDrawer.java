package mydraw;

import java.awt.Graphics;
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
		Graphics g = this.shapeManager.gui.drawingArea.image.getGraphics(); //draw on image
		if (lastx != -1) {
			// first undraw a rubber rect
			g.setXORMode(this.shapeManager.gui.color);
			g.setColor(this.shapeManager.gui.getBackground());
			doDraw(pressx, pressy, lastx, lasty, g);
			lastx = -1;
			lasty = -1;
		}
		// these commands finish the rubberband mode
		g.setPaintMode();
		g.setColor(this.shapeManager.gui.color);
		// draw the finel rectangle
		doDraw(pressx, pressy, e.getX(), e.getY(), g);
	}

	// mouse released => temporarily set second corner of rectangle
	// draw the resulting shape in "rubber-band mode"
	public void mouseDragged(MouseEvent e) {
		Graphics g = this.shapeManager.gui.drawingArea.image.getGraphics();
		// these commands set the rubberband mode
		g.setXORMode(this.shapeManager.gui.color);
		g.setColor(this.shapeManager.gui.getBackground());
		System.out.println("BG: " + this.shapeManager.gui.getBackground());
		System.out.println("Clr: " + this.shapeManager.gui.color);
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
		this.shapeManager.gui.drawingArea.repaint();
	}
}