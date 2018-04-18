package mydraw;

import java.awt.*;
import javax.swing.*;

public class JDrawingArea extends JPanel {
	Image image;
	Dimension drawingSize;

	public JDrawingArea(Image image, Dimension size) {
		super();
		this.image = image;
		// TODO get real size
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.drawingSize = size;
		//this.setOpaque(true);
	}

	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = new Dimension (this.image.getWidth(null), this.image.getHeight(null));
		g.clipRect(0, 0, size.width , size.height);
		g.drawImage(image, 0, 0, null);
		if (!size.equals(this.getSize())) {
			this.setPreferredSize(size);
			this.setMinimumSize(size);
			this.setSize(size);
		}
	}
	/**
	 * Get Graphics Instance of the image
	 * 
	 * @return
	 */
	public Graphics getImageGraphics() {
		return image.getGraphics();
	}

	public Image getImage() {
		return image;
	}
}
