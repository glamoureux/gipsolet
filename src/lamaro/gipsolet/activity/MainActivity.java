package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.service.GeolocationService;
import lamaro.gipsolet.service.GeolocationServiceBinder;
import lamaro.gipsolet.service.GeolocationServiceListener;
import lamaro.gipsolet.service.IGeolocationService;
import android.location.Location;
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

public class MainActivity extends Activity implements GeolocationServiceListener {
	
	ServiceConnection connection;
	IGeolocationService service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Creation
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		connection = new ServiceConnection() {
			
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
			inTriolet.setVisibility(View.GONE);
			notInTriolet.setVisibility(View.VISIBLE);
		}
	}
	
	public void goCampus(View view) {
		Intent goCampus = new Intent(this, GoCampusActivity.class);
		
		Location currentLocation = service.getCurrentLocation();
		System.out.println(currentLocation);
		goCampus.putExtra(GoCampusActivity.LATITUDE_KEY, (float) currentLocation.getLatitude());
		goCampus.putExtra(GoCampusActivity.LONGITUDE_KEY, (float) currentLocation.getLongitude());
		
		startActivity(goCampus);
	}
	
	@Override
	public void onDestroy() {
		unbindService(connection);
	}
}
