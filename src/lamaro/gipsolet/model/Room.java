package lamaro.gipsolet.model;

import android.graphics.PointF;

public class Room implements CampusEntity {
	public Integer id;
	public PointF position = new PointF();
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
	
	@Override
	public int hashCode() {
		return id * "room".hashCode();
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
		return label;
	}
	
	@Override
	public long getId() {
		return id;
	}
}