package lamaro.gipsolet.activity;

import org.json.JSONArray;
import org.json.JSONObject;
import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.service.GeolocationService;
import lamaro.gipsolet.service.GeolocationServiceBinder;
import lamaro.gipsolet.service.GeolocationServiceListener;
import lamaro.gipsolet.service.IGeolocationService;
import mandarine.boussole.RoutingResultHandler;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements RoutingResultHandler, GeolocationServiceListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Creation
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ServiceConnection connection = new ServiceConnection() {
			IGeolocationService service;
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				service.removeListener(MainActivity.this);
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				service = ((GeolocationServiceBinder) binder).getService();
				service.addListener(MainActivity.this);
				
				positionChanged(service.isOnCampus(), service.getInsideOfBuilding());
			}
		};

		startService(new Intent(this, GeolocationService.class));
		bindService(new Intent(this, GeolocationService.class), connection, Context.BIND_AUTO_CREATE);
	}

	public void onClickSearchEditText(View v) {
		onSearchRequested();
	}
	
	public void onClickList(View v) {
		Intent intent = new Intent(this, ListEntitiesActivity.class);
		if (v.getId() == R.id.menuButtonBuildings)
			intent.putExtra("entityType", Database.TABLE_BUILDINGS);
		if (v.getId() == R.id.menuButtonRooms)
			intent.putExtra("entityType", Database.TABLE_ROOMS);
		if (v.getId() == R.id.menuButtonServices)
			intent.putExtra("entityType", Database.TABLE_SERVICES);
		
		startActivity(intent);
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
		Button inTriolet = (Button) findViewById(R.id.in_triolet);
		Button notInTriolet = (Button) findViewById(R.id.not_in_triolet);
		
		if (inCampus) {
			inTriolet.setVisibility(View.VISIBLE);
			notInTriolet.setVisibility(View.GONE);
			
			
			if (insideOfBuilding != null) {
				inTriolet.setText(getString(R.string.current_location, insideOfBuilding.getName()));
			} else {
				inTriolet.setText(getString(R.string.in_triolet));
			}
 		} else {
 			System.out.println("la");
			inTriolet.setVisibility(View.GONE);
			notInTriolet.setVisibility(View.VISIBLE);
		}
	}
}
