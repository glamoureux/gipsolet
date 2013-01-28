package lamaro.gipsolet.model;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public interface CampusEntity {
	Integer getId();
	String getName();
	LatLng getLatLng();
	List<LatLng> getShape();
	Building getBuilding();
}
