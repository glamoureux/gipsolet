package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.geolocation.Geolocation;
import lamaro.gipsolet.geolocation.IGeolocation;
import lamaro.gipsolet.geolocation.inside.IInside;
import lamaro.gipsolet.geolocation.inside.IInsideListener;
import lamaro.gipsolet.geolocation.inside.Inside;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.service.NotificationService;
//import lamaro.gipsolet.service.NotificationServiceBinder;
import android.location.LocationManager;
import android.net.Uri;
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


public class MainActivity extends Activity implements IInsideListener {
	
	private IGeolocation geolocation = null;
	private IInside inside = null;

	//private NotificationService service;
	private ServiceConnection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		geolocation = Geolocation.getInstance();
		geolocation.setLocationManager((LocationManager) getSystemService(Context.LOCATION_SERVICE));
		inside = Inside.getInstance();
		inside.setContext(getBaseContext());

		connection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				//service = ((NotificationServiceBinder)binder).getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				
			}
		};

		startService(new Intent(this, NotificationService.class));
		bindService(new Intent(this, NotificationService.class), connection, Context.BIND_AUTO_CREATE);

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
	
	public void onClickButtonMap(View v) {
		if (v.getId() == R.id.menuButtonMap) {
			Intent intent = new Intent(this, MapActivity.class);
			
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void goCampus(View view) {
//		Intent goCampus = new Intent(this, GoCampusActivity.class);
//		
//		Location currentLocation = service.getCurrentLocation();
//
//		goCampus.putExtra(GoCampusActivity.LATITUDE_KEY, (float) currentLocation.getLatitude());
//		goCampus.putExtra(GoCampusActivity.LONGITUDE_KEY, (float) currentLocation.getLongitude());
//		
//		startActivity(goCampus);
		double campusLatitude = 43.631362;
		double campusLongitude = 3.861136;
		
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + campusLatitude + "," + campusLongitude)));
	}
	
	@Override
	public void onDestroy() {
		unbindService(connection);
		super.onDestroy();
	}

	@Override
	public void insideStateChanged(boolean onCampus, Building insideOfBuilding) {
		Button inTriolet = (Button) findViewById(R.id.in_triolet);
		Button notInTriolet = (Button) findViewById(R.id.not_in_triolet);
		
		if(onCampus) {
			inTriolet.setVisibility(View.VISIBLE);
			notInTriolet.setVisibility(View.GONE);
			
			if(insideOfBuilding != null) {
				inTriolet.setText(getString(R.string.current_location, insideOfBuilding.getName()));
			} else {
				inTriolet.setText(getString(R.string.in_triolet));
			}
 		} else {
			inTriolet.setVisibility(View.GONE);
			notInTriolet.setVisibility(View.VISIBLE);
		}
	}
}
