package lamaro.gipsolet.model;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Room implements CampusEntity {
	public Integer id;
	public String label;
	public LatLng latlng = new LatLng(0.0, 0.0);
	public Building building;
	public Integer floor;
	public int type;

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