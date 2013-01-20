package lamaro.gipsolet.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import mandarine.boussole.RoutingResultHandler;
import mandarine.boussole.RoutingTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements RoutingResultHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Creation
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Tests !
//		Database db = new Database(this);
//		db.open();
//
//		List<Building> buildings = db.getBuildings();
//
//		PointF p = new PointF(43.634654f, 3.862047f);
//		for (Building b : buildings) {
//			System.out.println(b.id + " " + b.isInBuilding(p));
//		}
//
//		RoutingTask r = new RoutingTask(this);
//		r.execute(new PointF(43.614799f, 3.886697f), new PointF(43.631983f, 3.861178f));
//		
//		db.close();
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
}
