package lamaro.gipsolet.geolocation;

import android.location.Location;

public interface IGeolocationListener {
	void positionChanged(Location location); 
}
