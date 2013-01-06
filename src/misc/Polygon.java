package misc;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

/**
 * Minimum Polygon class for Android.
 */
public class Polygon {
	// Polygon coordinates.
	private List<PointF> points;

	public Polygon() {
		points = new ArrayList<PointF>();
	}
	
	public void addPoint(PointF p) {
		points.add(p);
	}
	
	/**
	 * Checks if the Polygon contains a point.
	 * 
	 * @see "http://alienryderflex.com/polygon/"
	 */
	public boolean contains(PointF p) {
		boolean oddTransitions = false;

		for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
			if ((points.get(i).y < p.y && points.get(j).y >= p.y) || (points.get(j).y < p.y && points.get(i).y >= p.y)) {
				if (points.get(i).x + (p.y - points.get(i).y) / (points.get(j).y - points.get(i).y) * (points.get(j).x - points.get(i).x) < p.x) {
					oddTransitions = !oddTransitions;
				}
			}
		}
		
		return oddTransitions;
	}
	
	public static Polygon createFrom(String zone) {
		Polygon p = new Polygon();
		String[] stringPoints = zone.split(",");

		for (String stringPoint: stringPoints) {
			String[] stringCoords = stringPoint.split(" ");
			p.addPoint(new PointF(Float.parseFloat(stringCoords[0]), Float.parseFloat(stringCoords[1])));
		}
		
		return p;
	}
	
	@Override
	public String toString() {
		String result = "";
		for (PointF p: points) {
			result += p.x + "/" + p.y + " ";
		}
		
		return result;
	}
}