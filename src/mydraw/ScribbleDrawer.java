package mydraw;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

// if this class is active, the mouse is interpreted as a pen
class ScribbleDrawer extends ShapeDrawer {
	/**
	 * 
	 */
	private ShapeManager shapeManager;

	/**
	 * @param shapeManager
	 */
	ScribbleDrawer(ShapeManager shapeManager) {
		this.shapeManager = shapeManager;
	}

	int lastx, lasty;

	public void mousePressed(MouseEvent e) {
		lastx = e.getX();
		lasty = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		Graphics g = this.shapeManager.gui.getGraphics();
		int x = e.getX(), y = e.getY();
		g.setColor(this.shapeManager.gui.color);
		g.setPaintMode();
		g.drawLine(lastx, lasty, x, y);
		lastx = x;
		lasty = y;
	}
}