package lamaro.gipsolet.model;

import android.graphics.PointF;

public class Service {
	public Integer id;
	public PointF position;
	public String description;
	public Building building;
	public Integer floor;
	
	@Override
	public String toString() {
		String result = id + "\n";
		result += position.x + "/" + position.y + "\n";
		result += "Etage " + floor + "\n";
		result += description + "\n";
		
		return result;
	}
}
