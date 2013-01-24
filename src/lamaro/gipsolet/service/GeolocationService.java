package lamaro.gipsolet.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lamaro.gipsolet.R;
import lamaro.gipsolet.activity.MainActivity;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import misc.Polygon;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class GeolocationService extends IntentService implements IGeolocationService {

	public static final int NOTIFICATION_ID = 0;
	public static final Polygon zoneTrioletCampus = buildZoneTriolet();

	private Set<GeolocationServiceListener> listeners;

	private LocationManager locationManager;
	private List<Building> buildings;
	private LocationListener locationListener;

	private Binder binder;

	private boolean onCampus;
	private Building insideOfBuilding;
	private Location currentLocation;

	public GeolocationService() {
		super("lamaro.gipsolet.service.GeolocationService");
	}

	@Override
	public void onCreate() {
		super.onCreate();

		locationListener = new Listener();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);

		buildings = new Database(getBaseContext()).getBuildings();

		onCampus = false;
		insideOfBuilding = null;

		binder = new GeolocationServiceBinder(this);

		listeners = new HashSet<GeolocationServiceListener>();

		initializeLastKnownPosition();
	}

	private void onPositionChanged(boolean oldOnCampus, Building oldInsideOfBuilding) {
		if (oldOnCampus != onCampus) {
			if (onCampus) {
				showNotification(getBaseContext().getString(R.string.location_changed),
						getBaseContext().getString(R.string.location_changed_enter_campus));
			} else {
				showNotification(getBaseContext().getString(R.string.location_changed),
						getBaseContext().getString(R.string.location_changed_quit_campus));
			}
		}

		if (oldInsideOfBuilding != insideOfBuilding) {
			if (insideOfBuilding == null) {
				showNotification(
						getBaseContext().getString(R.string.location_changed),
						getBaseContext().getString(R.string.location_changed_quit_building,
								oldInsideOfBuilding.getName()));
			} else {
				showNotification(getBaseContext().getString(R.string.location_changed),
						getBaseContext()
								.getString(R.string.location_changed_enter_building, insideOfBuilding.getName()));
			}
		}

		for (GeolocationServiceListener l : listeners) {
			l.positionChanged(onCampus, insideOfBuilding);
		}
	}

	@Override
	public boolean isOnCampus() {
		return onCampus;
	}

	@Override
	public Building getInsideOfBuilding() {
		return insideOfBuilding;
	}
	
	@Override
	public Location getCurrentLocation() {
		return currentLocation;
	}

	private void initializeLastKnownPosition() {
		Location lastGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location lastNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		Location closest = null;

		if (lastGPSLocation != null && lastNetworkLocation == null) {
			closest = lastGPSLocation;
		} else if (lastGPSLocation == null && lastNetworkLocation != null) {
			closest = lastNetworkLocation;
		} else if (lastGPSLocation != null && lastNetworkLocation != null) {
			if (lastGPSLocation.getTime() > lastNetworkLocation.getTime()) {
				closest = lastGPSLocation;
			} else {
				closest = lastNetworkLocation;
			}
		}
		
		if (closest != null) {
			saveLocation(closest);
		}
	}

	private void saveLocation(Location location) {
		PointF position = new PointF((float) location.getLatitude(), (float) location.getLongitude());

		onCampus = zoneTrioletCampus.contains(position);

		if (onCampus) {
			for (Building b : buildings) {
				if (b.isInBuilding(position)) {
					insideOfBuilding = b;
					break;
				}
			}
		}
		
		currentLocation = location;
	}

	private static Polygon buildZoneTriolet() {
		Polygon p = new Polygon();
		p.addPoint(new PointF(43.631043f, 3.860621f));
		p.addPoint(new PointF(43.635066f, 3.859849f));
		p.addPoint(new PointF(43.635578f, 3.861523f));
		p.addPoint(new PointF(43.634056f, 3.863583f));
		p.addPoint(new PointF(43.634755f, 3.86811f));
		p.addPoint(new PointF(43.62963f, 3.869998f));
		p.addPoint(new PointF(43.629614f, 3.867531f));

		return p;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onDestroy() {
		locationManager.removeUpdates(locationListener);
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void showNotification(CharSequence title, CharSequence message) {
		Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);

		Notification notification = new Notification();
		notification.when = System.currentTimeMillis();
		notification.icon = R.drawable.ic_launcher;
		notification.setLatestEventInfo(getBaseContext(), title, message, contentIntent);

		// Récupération du Notification Manager
		NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(
				Context.NOTIFICATION_SERVICE);

		manager.notify(NOTIFICATION_ID, notification);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

	@Override
	public void addListener(GeolocationServiceListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(GeolocationServiceListener listener) {
		listeners.remove(listener);
	}

	private class Listener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			boolean oldOnCampus = onCampus;
			Building oldInsideOfBuilding = insideOfBuilding;

			saveLocation(location);

			if (oldOnCampus != onCampus || insideOfBuilding != oldInsideOfBuilding) {
				onPositionChanged(oldOnCampus, oldInsideOfBuilding);
			}
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
