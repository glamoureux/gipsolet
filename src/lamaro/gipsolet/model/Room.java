package lamaro.gipsolet.model;

import android.graphics.PointF;

public class Room {
	public Integer id;
	public PointF position;
	public Building building;
	public Integer floor;
	public int type;
	public String label;

	@Override
	public String toString() {
		String result = id + "\n";
		result += position.x + "/" + position.y + "\n";
		result += "Etage " + floor + "\n";
		result += label + "\n";
		result += type + "\n";
		
		return result;
	}
}