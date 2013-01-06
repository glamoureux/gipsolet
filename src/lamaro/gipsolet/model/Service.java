package lamaro.gipsolet.model;

import android.graphics.PointF;

public class Service {
	public Integer id;
	public PointF position = new PointF();
	public String description;
	public Building building;
	public Integer floor;
	public String keywords;
	
	@Override
	public String toString() {
		String result = id + "\n";
		result += position.x + "/" + position.y + "\n";
		result += "Etage " + floor + "\n";
		result += description + "\n";
		
		return result;
	}
	
	@Override
	public int hashCode() {
		return id * "service".hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return o.hashCode() == hashCode();
	}
}
