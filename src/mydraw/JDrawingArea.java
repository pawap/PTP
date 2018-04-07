package mydraw;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class JDrawingArea extends JPanel {
	Image image;
	
	public JDrawingArea(Image image) {
		super();
		this.image = image;
		// TODO get real size
		this.setPreferredSize(new Dimension(400,320));
		this.setOpaque(true);
	}
	
	@Override 
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
        
   }
}
