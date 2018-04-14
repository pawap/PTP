/**
 * 
 */
package mydraw.test;

import static org.junit.Assert.*;

import org.junit.Test;

import mydraw.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.IllegalArgumentException;

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
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link mydraw.Draw#setHeight(int)}.
	 */
	@Test
	public void testSetHeight() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link mydraw.Draw#getFGColor()}.
	 */
	@Test
	public void testGetFGColor() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link mydraw.Draw#getBGColor()}.
	 */
	@Test
	public void testGetBGColor() {
		fail("Not yet implemented");
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
		BufferedImage img = new BufferedImage(draw.getWidth(),draw.getHeight(),BufferedImage.TYPE_INT_ARGB);
		img.getGraphics().drawImage(draw.getDrawing(), 0, 0, null);	
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
		
		BufferedImage img = new BufferedImage(draw.getWidth(),draw.getHeight(),BufferedImage.TYPE_INT_ARGB);
		img.getGraphics().drawImage(draw.getDrawing(), 0, 0, null);	
		assertEquals(img.getRGB(10, 10),Color.BLACK.getRGB());
		assertNotEquals(img.getRGB(20, 20),Color.WHITE.getRGB());
	}

}
