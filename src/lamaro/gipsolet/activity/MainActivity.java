package lamaro.gipsolet.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import lamaro.gipsolet.service.GeolocationService;
import lamaro.gipsolet.service.GeolocationServiceBinder;
import lamaro.gipsolet.service.GeolocationServiceListener;
import lamaro.gipsolet.service.IGeolocationService;
import mandarine.boussole.RoutingResultHandler;
import mandarine.boussole.RoutingTask;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements RoutingResultHandler, GeolocationServiceListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Creation
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ServiceConnection connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				IGeolocationService service = ((GeolocationServiceBinder) binder).getService();
				service.addListener(MainActivity.this);
			}
		};

		startService(new Intent(this, GeolocationService.class));
		bindService(new Intent(this, GeolocationService.class), connection, Context.BIND_AUTO_CREATE);
	}

	public void onClickSearchEditText(View v) {
		onSearchRequested();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void processGeoJSONResult(JSONObject json) {
		System.out.println(json.toString());
	}

	@Override
	public void processRoutingResult(JSONArray routes) {
		System.out.println(routes.toString());
	}

	@Override
	public void positionChanged(boolean inCampus, Building insideOfBuilding) {
		System.out.println(inCampus);
	}
}
