package fr.enst.markingmenus.views;

public class MenuCalculations {

	public static float pythagore(float x1, float y1, float x2, float y2) {
		float adj = x2 - x1;
		float opp = y2 - y1;
		return (float) Math.sqrt(adj * adj + opp * opp);
	}

	public static float arctan(float x1, float y1, float x2, float y2) {
		float adj = x2 - x1;
		float opp = y2 - y1;
		return (float) (Math.atan2(opp, adj) * 180 / Math.PI);
	}
}
