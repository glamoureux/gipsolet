package lamaro.gipsolet.model;

import java.util.List;

import lamaro.gipsolet.GipsoletApplication;
import lamaro.gipsolet.R;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;

public class Building extends ContainerEntity implements CampusEntity {
	public Integer id;
	public String label;
	public LatLng latlng = new LatLng(0.0, 0.0);
	public List<LatLng> shape;
	public Integer number;
	public String keywords;

	public boolean isInBuilding(Location location) {
		return contains(new LatLng(location.getLatitude(), location.getLongitude()), shape);
	}

	@Override
	public String toString() {
		String result = id + "\n";
		result += shape + "\n";
		result += latlng.latitude + "/" + latlng.longitude + "\n";
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
	public String getName() {
		if (number == 0) {
			return label;
		} else if (label.length() > 0) {
			return GipsoletApplication.getAppContext().getString(R.string.building) + " " + number + " (" + label + ")";
		} else {
			return GipsoletApplication.getAppContext().getString(R.string.building) + " " + number.toString();
		}
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public LatLng getLatLng() {
		return latlng;
	}

	@Override
	public List<LatLng> getShape() {
		return shape;
	}

	@Override
	public Building getBuilding() {
		return null;
	}
}
