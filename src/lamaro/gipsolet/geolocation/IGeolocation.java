package lamaro.gipsolet.geolocation;

import android.location.LocationManager;

public interface IGeolocation {
	void setLocationManager(LocationManager locationManager);
	void addListener(IGeolocationListener listener);
	void removeListener(IGeolocationListener listener);
}
