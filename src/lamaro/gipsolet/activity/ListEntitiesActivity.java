package lamaro.gipsolet.activity;

import java.util.List;
import lamaro.gipsolet.R;
import lamaro.gipsolet.data.CEAdapter;
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
				listEntities(db.getBuildings(), Database.TABLE_BUILDINGS, 0);
			} else if (extra.contains(Database.TABLE_ROOMS)) {
				listEntities(db.getRooms(), Database.TABLE_ROOMS, 0);
			} else if (extra.contains(Database.TABLE_SERVICES)) {
				listEntities(db.getServices(), Database.TABLE_SERVICES, 0);
			}
		} else if (extras.containsKey("buildingID") && extras.containsKey("type")) {
			int id = extras.getInt("buildingID");
			String type = extras.getString("type");
			if (type.equals("room"))
				listEntities(db.getRoomsOfBuilding(db.getBuildingById(id)), Database.TABLE_ROOMS, id);
			if (type.equals("service"))
				listEntities(db.getServicesOfBuilding(db.getBuildingById(id)), Database.TABLE_SERVICES, id);
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

	private void listEntities(List<? extends CampusEntity> ces, String type, int id) {
		TextView resultText = (TextView) findViewById(R.id.text_result);
		if (ces == null && !type.equals("")) {
			resultText.setText(getString(R.string.no_results, new Object[] { type }));
		} else {
			if (type.equals(""))
				resultText.setVisibility(View.GONE);
			else {
				if (id == 0) {
					if (type.equals(Database.TABLE_ROOMS)) {
						resultText.setText(getString(R.string.rooms_list));
					} else if (type.equals(Database.TABLE_SERVICES)) {
						resultText.setText(getString(R.string.services_list));
					} else if (type.equals(Database.TABLE_BUILDINGS)) {
						resultText.setText(getString(R.string.buildings_list));
					}
				} else {
					if (type.equals(Database.TABLE_ROOMS)) {
						resultText.setText(getString(R.string.buildingRooms, new Object[] { id }));
					} else if (type.equals(Database.TABLE_SERVICES)) {
						resultText.setText(getString(R.string.buildingServices, new Object[] { id }));
					}
				}
			}

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			ListAdapter adapter = new CEAdapter(this, ces);
			setListAdapter(adapter);
		}
	}
}
