package lamaro.gipsolet.service;

import android.os.Binder;

public class NotificationServiceBinder extends Binder {
	private NotificationService notificationService = null;

	public NotificationServiceBinder(NotificationService notificationService) {
		super();
		
		this.notificationService = notificationService;
	}

	public NotificationService getService() {
		return notificationService;
	}
}
