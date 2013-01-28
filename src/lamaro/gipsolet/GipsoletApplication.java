package lamaro.gipsolet;

import android.app.Application;
import android.content.Context;

public class GipsoletApplication extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		GipsoletApplication.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return GipsoletApplication.context;
	}

}
