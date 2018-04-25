package mydraw;

import java.awt.Color;

/**
 * MyColor offers access to a subset of the colors offered by java.awt.Color. It offers the possibility 
 * to map strings containing valid color names to the respective colors via the stringToColor method.
 * @author ptp18-d06(Pawel Rasch, Tim Runge)
 *
 */
public class MyColor {
	final static String[] valid_colors = {"Black", "Green", "Red", "Blue", "White"};

	/**
	 * checks if the input string can be mapped to one of the MyColor colors.
	 * @param str
	 * @return true if the input matches a valid color
	 */
	public static boolean isAColor(String str) {
		String input = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		for (String val_col : valid_colors) {
			if (val_col.toString().equals(input)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * If the input string can be mapped to one of the valid colors, the corresponding color will be returned.
	 * @param str member of {"Black", "Green", "Red", "Blue", "White"} (case insensitive)
	 * @return the color corresponding to the input String
	 * @throws ColorException
	 */
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
			for (String col_str : valid_colors) {
				if (col.equals(stringToColor(col_str))) {
					return col_str;
				}
			}
		}
		catch (ColorException e) {
			System.out.println(e.toString());
		}
		return "not a valid color";
	}
}

