package fr.enst.markingmenus.objects;

/**
 * Class containing computing methods.
 * 
 * @author Goui
 * 
 */
public class MenuCalculations {

	/**
	 * Method used to compute the distance between 2 points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance
	 */
	public static float pythagore(float x1, float y1, float x2, float y2) {
		float adj = x2 - x1;
		float opp = y2 - y1;
		return (float) Math.sqrt(adj * adj + opp * opp);
	}

	/**
	 * Method used to compute the angle between 2 points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the angle in degree
	 */
	public static float arctan(float x1, float y1, float x2, float y2) {
		float adj = x2 - x1;
		float opp = y2 - y1;
		return (float) (Math.atan2(opp, adj) * 180 / Math.PI);
	}

	/**
	 * Method used to compute the slope between 2 points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the slope
	 */
	public static float slope(float x1, float y1, float x2, float y2) {
		return (y2 - y1) / (x2 - x1);
	}
}
