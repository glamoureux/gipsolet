package lamaro.gipsolet.model;

import android.graphics.PointF;
import misc.Polygon;

public class Building implements CampusEntity {
	public Integer id;
	public Polygon zone;
	public PointF position = new PointF();
	public Integer number;
	public String label;
	public String keywords;

	public boolean isInBuilding(PointF p) {
		return zone.contains(p);
	}

	@Override
	public String toString() {
		String result = id + "\n";
		result += zone + "\n";
		result += position.x + "/" + position.y + "\n";
		result += "Batiment " + number + "\n";
		result += label + "\n";
		result += keywords + "\n";

		return result;
	}

	@Override
	public int hashCode() {
		return id * "building".hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o.hashCode() == hashCode();
	}

	@Override
	public PointF getPosition() {
		return position;
	}

	@Override
	public String getName() {
		if (number == 0) {
			return label;
		} else if (label.length() > 0) {
			return number + " (" + label + ")";
		} else {
			return number.toString();
		}
	}

	@Override
	public long getId() {
		return id;
	}
}
