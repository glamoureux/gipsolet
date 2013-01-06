package lamaro.gipsolet.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import boussole.mandarine.RoutingTask;
import boussole.mandarine.RoutingResultHandler;

import lamaro.gipsolet.R;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import lamaro.gipsolet.sqlite.Database;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.PointF;
import android.view.Menu;

public class MainActivity extends Activity implements RoutingResultHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Creation
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Database db = new Database(this);
		// db.open();

		// List<Building> buildings = db.getBuildings();
		//
		// PointF p = new PointF(43.634654f,3.862047f);
		// for (Building b: buildings) {
		// System.out.println(b.id + " " + b.isInBuilding(p));
		// }
		// db.close();

		RoutingTask r = new RoutingTask(this);
		r.execute(new PointF(43.614799f, 3.886697f), new PointF(43.631983f, 3.861178f));
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
}
