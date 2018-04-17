package mydraw;

import java.awt.Color;

/**
 * MyColor offers access to a subset of the colors offered by java.awt.Color. It offers the possibility 
 * to map strings containing valid color names to the respective colors via the stringToColor method.
 * @author 6runge
 *
 */
public enum MyColor {
	BLACK, GREEN, RED, BLUE, WHITE; 

	/**
	 * checks if the input string can be mapped to one of the MyColor colors. The check is
	 * case sensitive and the input string should consist of only uppercase letters.
	 * @param str
	 * @return true if the input matches a valid color
	 */
	public static boolean isAColor(String str) {
		for (MyColor c : MyColor.values()) {
			if (c.toString().equals(str)) {
				return true;
			}
		}
		return false;
	}


	public static Color stringToColor(String str) throws ColorException{

		String col = str.toUpperCase();


		if (MyColor.isAColor(col)) {
			switch (col) {
			case "BLACK": return Color.black;

			case "GREEN": return Color.green;

			case "RED":   return Color.red;

			case "BLUE":  return Color.blue;

			case "WHITE": return Color.white;

			default:      System.out.println("The String reads " + col);
			return Color.pink;
			}
		}
		else {
			System.out.println(col);
			throw new ColorException(col + " is not a valid color");
		}
	}

	/**
	 * returns a string representation of a color with the first letter being uppercase
	 * and the rest of the letters being lowercase. 
	 * @param col the color to be mapped to a string
	 * @return the string representation of the color
	 */
	public static String colorToString(Color col) {
		try {
			for (MyColor mc : MyColor.values()) {
				String col_str = mc.toString();

				if (col.equals(stringToColor(col_str))) {
					return col_str.substring(0, 1).toUpperCase() + col_str.substring(1).toLowerCase();
				}
			}
		}
		catch (ColorException e) {
			System.out.println(e.toString());
		}
		return "not a valid color";
	}
}

