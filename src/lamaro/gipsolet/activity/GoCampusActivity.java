package lamaro.gipsolet.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import lamaro.gipsolet.R;
import mandarine.boussole.RoutingResultHandler;
import mandarine.boussole.RoutingTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;

public class GoCampusActivity extends Activity implements RoutingResultHandler {

	public static final String LATITUDE_KEY = "latitude";
	public static final String LONGITUDE_KEY = "longitude";
	
	private static final PointF CAMPUS_POSITION = new PointF(43.631362f, 3.861136f);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.go_campus);
		
		Intent starterIntent = getIntent();
		float startLatitude = starterIntent.getExtras().getFloat(GoCampusActivity.LATITUDE_KEY);
		float startLongitude= starterIntent.getExtras().getFloat(GoCampusActivity.LONGITUDE_KEY);

		new RoutingTask(this).execute(new PointF(startLatitude, startLongitude), CAMPUS_POSITION);
	}

	@Override
	public void processGeoJSONResult(JSONObject json) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processRoutingResult(JSONArray routes) {
		System.out.println(routes);
	}
}
