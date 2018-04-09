package mydraw;

import java.awt.Color;

public enum MyColor {
	BLACK, GREEN, RED, BLUE, WHITE; 

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
}

