package lamaro.gipsolet.service;

import android.location.Location;
import lamaro.gipsolet.model.Building;

public interface IGeolocationService {

	boolean isOnCampus();
	Building getInsideOfBuilding();
	Location getCurrentLocation();
	
	void addListener(GeolocationServiceListener listener);
	void removeListener(GeolocationServiceListener listener);
}
