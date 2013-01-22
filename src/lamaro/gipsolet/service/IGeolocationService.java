package lamaro.gipsolet.service;

import lamaro.gipsolet.model.Building;

public interface IGeolocationService {

	boolean isOnCampus();
	
	Building getInsideOfBuilding();
	
	void addListener(GeolocationServiceListener listener);
	void removeListener(GeolocationServiceListener listener);
}
