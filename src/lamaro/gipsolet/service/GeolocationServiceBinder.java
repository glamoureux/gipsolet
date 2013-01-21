package lamaro.gipsolet.service;

import android.os.Binder;

public class GeolocationServiceBinder extends Binder {
	private IGeolocationService service = null;

	public GeolocationServiceBinder(IGeolocationService s) {
		super();
		service = s;
	}

	public IGeolocationService getService() {
		return service;
	}
}
