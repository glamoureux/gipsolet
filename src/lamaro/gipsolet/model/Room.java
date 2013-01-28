package lamaro.gipsolet.model;

import java.util.List;

import lamaro.gipsolet.GipsoletApplication;
import lamaro.gipsolet.R;

import com.google.android.gms.maps.model.LatLng;

public class Room implements CampusEntity {
	public Integer id;
	public String label;
	public LatLng latlng = new LatLng(0.0, 0.0);
	public Building building;
	public Integer floor;
	public int type;

	public String getType() {
		if (type == RoomType.AMPHI) {
			return GipsoletApplication.getAppContext().getString(R.string.hall);
		} else if (type == RoomType.SC) {
			return GipsoletApplication.getAppContext().getString(R.string.class_room);
		} else {
			return GipsoletApplication.getAppContext().getString(R.string.tutorial_room);
		}
	}
	
	public String getFloor() {
		if (floor == 0) {
			return GipsoletApplication.getAppContext().getString(R.string.ground_floor);
		} else {
			return GipsoletApplication.getAppContext().getString(R.string.floor) + " " + floor;
		}
	}
	
	@Override
	public String toString() {
		String result = id + "\n";
		result += latlng.latitude + "/" + latlng.longitude + "\n";
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
	public Integer getId() {
		return id;
	}

	@Override
	public String getName() {
		return label;
	}

	@Override
	public LatLng getLatLng() {
		return latlng;
	}

	@Override
	public List<LatLng> getShape() {
		return null;
	}

	@Override
	public Building getBuilding() {
		return building;
	}
}