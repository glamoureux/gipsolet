package lamaro.gipsolet.geolocation;

import java.util.HashSet;
import java.util.Set;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Geolocation implements IGeolocation {
	
	private static IGeolocation geolocation = null;
	private LocationManager locationManager = null;
	private NetworkLocationListener networkLocationListener = null;
	private GpsLocationListener gpsLocationListener = null;
	private Set<IGeolocationListener> geolocationListeners = null;
	private Location networkLocation = null;
	private Location gpsLocation = null;
	
	private Geolocation() {
		
	}

	public static IGeolocation getInstance() {
		if(geolocation == null) {
			geolocation = new Geolocation();
		}
		
		return geolocation;
	}
	
	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;

		if(this.locationManager != null) {
			if(networkLocationListener == null) {
				networkLocationListener = new NetworkLocationListener();
			}
			if(gpsLocationListener == null) {
				gpsLocationListener = new GpsLocationListener();
			}
			if(geolocationListeners == null) {
				geolocationListeners = new HashSet<IGeolocationListener>();
			}
			this.locationManager.removeUpdates(networkLocationListener);
			this.locationManager.removeUpdates(gpsLocationListener);
			this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, networkLocationListener);
			this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, gpsLocationListener);
		}
	}

	@Override
	public void addListener(IGeolocationListener listener) {
		geolocationListeners.add(listener);
	}

	@Override
	public void removeListener(IGeolocationListener listener) {
		geolocationListeners.remove(listener);
	}
	
	private void broadcastLocation() {
		Location location = null;
		
		if (gpsLocation != null && networkLocation == null) {
			location = gpsLocation;
		} else if (gpsLocation == null && networkLocation != null) {
			location = networkLocation;
		} else if (gpsLocation != null && networkLocation != null) {
			if (gpsLocation.getTime() > networkLocation.getTime()) {
				location = gpsLocation;
			} else {
				location = networkLocation;
			}
		}
		
		for(IGeolocationListener listener: geolocationListeners) {
			listener.positionChanged(location);
		}
	}
	
	private class NetworkLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			networkLocation = location;
			broadcastLocation();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

	}
	
	private class GpsLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			gpsLocation = location;
			broadcastLocation();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

	}

}
