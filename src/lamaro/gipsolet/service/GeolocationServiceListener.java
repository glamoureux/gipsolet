package lamaro.gipsolet.service;

import lamaro.gipsolet.model.Building;

public interface GeolocationServiceListener {
	void positionChanged(boolean inCampus, Building insideOfBuilding); 
}
