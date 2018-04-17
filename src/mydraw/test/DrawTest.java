/**
 * 
 */
package mydraw.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import mydraw.ColorException;
import mydraw.Draw;

/**
 * @author Paw
 *
 */
public class DrawTest {

	/**
	 * Test method for {@link mydraw.Draw#getDrawing()}.
	 */
	@Test
	public void testGetDrawing() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link mydraw.Draw#getWidth()}.
	 */
	@Test
	public void testGetWidth() {
		Draw draw = new Draw();
		assertNotEquals(draw.getWidth(),0);
		draw.setWidth(300);
		assertEquals(draw.getWidth(),300);
	}

	/**
	 * Test method for {@link mydraw.Draw#setWidth(int)}.
	 */
	@Test
	public void testSetWidth() {
		Draw draw = new Draw();
		draw.setWidth(300);
		assertEquals(draw.getWidth(),300);
		
		try {
			draw.setWidth(-10);
			fail("No exception thrown for neg. Input");
		} catch (IllegalArgumentException e) {
			
		} catch (Exception e) {
			fail("Wrong exception thrown for inacceptable input.");
		}
			
		
	}

	/**
	 * Test method for {@link mydraw.Draw#getHeight()}.
	 */
	@Test
	public void testGetHeight() {
		Draw draw = new Draw();
		assertTrue(draw.getHeight() > 0);
		draw.setHeight(777);
		assertEquals(draw.getHeight(),777);
	}

	/**
	 * Test method for {@link mydraw.Draw#setHeight(int)}.
	 */
	@Test
	public void testSetHeight() {
		Draw draw = new Draw();
		draw.setHeight(555);
		assertEquals(draw.getHeight(),555);
		
		try {
			draw.setHeight(-1);
			fail("No exception thrown for neg. Input");
		} catch (IllegalArgumentException e) {
			
		} catch (Exception e) {
			fail("Wrong exception thrown for inacceptable input.");
		}
		
	}

	/**
	 * Test method for {@link mydraw.Draw#getFGColor()}.
	 */
	@Test
	public void testGetFGColor() {
		Draw draw = new Draw();
		String fgColor = draw.getFGColor();
		assertTrue(!fgColor.equals("") );
		
		try {
			draw.setFGColor("green");
		} catch (ColorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Testcolor : green not accepted as FG Color");
		}
		assertEquals(draw.getFGColor().toLowerCase(),"green");
	}

	/**
	 * Test method for {@link mydraw.Draw#getBGColor()}.
	 */
	@Test
	public void testGetBGColor() {
		Draw draw = new Draw();
		String bgColor = draw.getBGColor();
		assertTrue(!bgColor.equals("") );
		
		try {
			draw.setBGColor("green");
		} catch (ColorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Testcolor : green not accepted as BG Color");
		}
		assertEquals(draw.getBGColor().toLowerCase(),"green");
	}

	/**
	 * Test method for {@link mydraw.Draw#setFGColor(java.lang.String)}.
	 * @throws ColorException 
	 */
	@Test
	public void testSetFGColor() throws ColorException {
		Draw draw = new Draw();
		try {
			draw.setFGColor("pink");
			fail("No exception thrown for inacceptable color.");
		} catch (ColorException e) {
			
		} catch (Exception e) {
			fail("Wrong exception thrown for inacceptable input.");
		}
		
		draw.setFGColor("Black");
		assertEquals(draw.getFGColor(),"BLACK");
		
		draw.drawRectangle(new Point(10,10),new Point(20,20));
		BufferedImage img = (BufferedImage) draw.getDrawing();	
		assertEquals(img.getRGB(10, 10),Color.BLACK.getRGB());
		assertNotEquals(img.getRGB(20, 20),Color.WHITE.getRGB());
		
	}

	/**
	 * Test method for {@link mydraw.Draw#setBGColor(java.lang.String)}.
	 * @throws ColorException 
	 */
	@Test
	public void testSetBGColor() throws ColorException {
		Draw draw = new Draw();
		try {
			draw.setBGColor("pink");
			fail("No exception thrown for inacceptable color.");
		} catch (ColorException e) {
			
		} catch (Exception e) {
			fail("Wrong exception thrown for inacceptable input.");
		}
		
		draw.setBGColor("Black");
		assertEquals(draw.getBGColor(),"BLACK");
		
		BufferedImage img = (BufferedImage) draw.getDrawing();	
		assertEquals(img.getRGB(10, 10),Color.BLACK.getRGB());
		assertNotEquals(img.getRGB(20, 20),Color.WHITE.getRGB());
	}
	@Test
	public void testDrawing() {
		Draw draw = new Draw();
		draw.autoDraw();
		Image img = draw.getDrawing();
		Image referenceImg = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
		try {
			referenceImg = draw.readImage("bull.bmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		if (width != referenceImg.getWidth(null)){
			fail("Widths not equal");
		}		
		if (height != referenceImg.getHeight(null)){
			fail("Heights not equal");
		}
		
		int[] diff = comparePixels(img, referenceImg);
		if (diff.length != 0) {
			int x = diff[0];
			int y = diff[1];
			int is = diff[2];
			int should = diff[3];
			fail("Pixels at (" + x + ", " + y + ") not equal. "
					+ "Is: " + is + ", Should: " + should  );
		}
	

	}
	private int[] comparePixels(Image img, Image referenceImg) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		int[] result = new int[4];
		
		BufferedImage buffImg = (BufferedImage) img;
		// necessary because cast does not work on image read from disk
		BufferedImage referenceBuffImg = new BufferedImage(width ,height, BufferedImage.TYPE_INT_ARGB);
		referenceBuffImg.getGraphics().drawImage(referenceImg, 0, 0, null);	 
		for (int x = 1; x < width; x++) {  
			for (int y = 0; y < height; y++) {
				if (referenceBuffImg.getRGB(x, y) != buffImg.getRGB(x, y)) {
			        result[0] = x;
			        result[1] = y;
			        result[2] = buffImg.getRGB(x, y);
			        result[3] = referenceBuffImg.getRGB(x, y);
			        return result;
				}
		    }
		}
		return new int[0];
	}
}
