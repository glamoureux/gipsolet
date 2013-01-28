package lamaro.gipsolet.activity;

import java.util.List;
import lamaro.gipsolet.R;
import lamaro.gipsolet.data.CampusEntityAdapter;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.CampusEntity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListEntitiesActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ce_search);
		
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		Database db = new Database(this);
		if (extras.containsKey("entityType")) {
			String extra = extras.getString("entityType");	
			if (extra.contains(Database.TABLE_BUILDINGS)) {
				listEntities((List) db.getBuildings(), Database.TABLE_BUILDINGS, "");
			} else if (extra.contains(Database.TABLE_ROOMS)) {
				listEntities((List) db.getRooms(), Database.TABLE_ROOMS, "");
			} else if (extra.contains(Database.TABLE_SERVICES)) {
				listEntities((List) db.getServices(), Database.TABLE_SERVICES, "");
			}		
		} else if (extras.containsKey("buildingID") && extras.containsKey("type")) {
			String id = extras.getString("buildingID");
			String type = extras.getString("type");
			if (type.equals("room"))
				listEntities((List) db.getRoomsOfBuilding(db.getBuildingById(Integer.parseInt(id))), Database.TABLE_ROOMS, id);
			if (type.equals("service"))
				listEntities((List) db.getServicesOfBuilding(db.getBuildingById(Integer.parseInt(id))), Database.TABLE_SERVICES, id);
		}		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		TextView obj = (TextView) v.findViewById(R.id.hideLabel);
		Intent intent = new Intent(this, ViewEntityActivity.class);
		intent.putExtra("id", obj.getText());
		startActivity(intent);
	}
	
	private void listEntities(List<CampusEntity> ces, String type, String id) {
		TextView resultText = (TextView) findViewById(R.id.text_result);
		if (ces == null && !type.equals("")) {
			resultText.setText(getString(R.string.no_results, new Object[] { type }));
		} else {
			if (type.equals(""))
				resultText.setVisibility(View.GONE);
			else {
				if (type.equals(Database.TABLE_ROOMS))
					resultText.setText(getString(R.string.buildingRooms, new Object[] { id }));
				if (type.equals(Database.TABLE_SERVICES))
					resultText.setText(getString(R.string.buildingServices, new Object[] { id }));
			}
				
			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			ListAdapter adapter = new CampusEntityAdapter(this, ces);
			setListAdapter(adapter);
		}		
	}
}
