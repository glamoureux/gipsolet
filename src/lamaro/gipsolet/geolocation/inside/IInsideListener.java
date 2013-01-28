package lamaro.gipsolet.geolocation.inside;

import lamaro.gipsolet.model.Building;

public interface IInsideListener {
	void insideStateChanged(boolean onCampus, Building insideOfBuilding); 
}
