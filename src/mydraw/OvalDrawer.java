package mydraw;

/**
 * @author ptp18-d06(Pawel Rasch, Tim Runge)
 */
import java.awt.Graphics;

// if this class is active, ovals are drawn
class OvalDrawer extends RectangleDrawer {
	OvalDrawer(ShapeManager shapeManager) {
		super(shapeManager);
		// TODO Auto-generated constructor stub
	}

	public void doDraw(int x0, int y0, int x1, int y1, Graphics g) {
		int x = Math.min(x0, x1);
		int y = Math.min(y0, y1);
		int w = Math.abs(x1 - x0);
		int h = Math.abs(y1 - y0);
		// draw oval instead of rectangle
		g.drawOval(x, y, w, h);
		this.shapeManager.getDrawingArea().repaint();
	}
}