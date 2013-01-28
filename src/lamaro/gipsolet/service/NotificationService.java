package lamaro.gipsolet.service;

import lamaro.gipsolet.R;
import lamaro.gipsolet.activity.MainActivity;
import lamaro.gipsolet.geolocation.inside.IInside;
import lamaro.gipsolet.geolocation.inside.IInsideListener;
import lamaro.gipsolet.geolocation.inside.Inside;
import lamaro.gipsolet.model.Building;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class NotificationService extends IntentService implements IInsideListener {

	private static final int NOTIFICATION_ID = 0;
	private Binder binder = null;
	private IInside inside = null;
	private boolean lastOnCampus = false;
	private Building lastInsideOfBuilding = null;

	public NotificationService() {
		super("lamaro.gipsolet.service.NotificationService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		binder = new NotificationServiceBinder(this);
		inside = Inside.getInstance();
		inside.addListener(this);
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
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insideStateChanged(boolean onCampus, Building insideOfBuilding) {
		if(lastOnCampus != onCampus) {
			if(onCampus) {
				showNotification(getBaseContext().getString(R.string.location_changed),
						getBaseContext().getString(R.string.location_changed_enter_campus));
			} else {
				showNotification(getBaseContext().getString(R.string.location_changed),
						getBaseContext().getString(R.string.location_changed_quit_campus));
			}
		}
		lastOnCampus = onCampus;

		if(lastInsideOfBuilding != insideOfBuilding) {
			if(insideOfBuilding == null) {
				showNotification(
						getBaseContext().getString(R.string.location_changed),
						getBaseContext().getString(R.string.location_changed_quit_building,
								lastInsideOfBuilding.getName()));
			} else {
				showNotification(getBaseContext().getString(R.string.location_changed),
						getBaseContext()
								.getString(R.string.location_changed_enter_building, insideOfBuilding.getName()));
			}
		}
		lastInsideOfBuilding = insideOfBuilding;
	}
}
