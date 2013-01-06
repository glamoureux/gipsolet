package lamaro.gipsolet.activity;

import java.util.List;

import lamaro.gipsolet.R;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import lamaro.gipsolet.sqlite.Database;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Creation
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Database db = new Database(this);
		db.open();
		List<Building> buildings = db.getBuildings();
		for (Building b: buildings) {
			System.out.println("_____");
			System.out.println(b);
			for (Room r: db.getRoomsOfBuilding(b)) {
				System.out.println(r);
			}
			
			for (Service s: db.getServicesOfBuilding(b)) {
				System.out.println(s);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
