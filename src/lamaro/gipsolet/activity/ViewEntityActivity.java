package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.CEAdapter;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.CampusEntity;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewEntityActivity extends Activity {
	private CampusEntity entity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		Database db = new Database(this);
		String extra = intent.getStringExtra("id");

		String[] entityType = extra.split("/");
		entity = db.getEntityByTypeId(entityType[0], Integer.parseInt(entityType[1]));
		ListAdapter adapt = null;
		if (entity instanceof Room) {
			setContentView(R.layout.activity_view_room);
			Room room = (Room) entity;
			TextView entityLabel = (TextView) findViewById(R.id.entityLabel);
			entityLabel.setText(room.getName());
			TextView typeRoom = (TextView) findViewById(R.id.typeRoom);
			typeRoom.setText(room.getType());
			TextView buildingAndFloor = (TextView) findViewById(R.id.buidingAndFloor);
			buildingAndFloor.setText(room.getBuilding().getName() + " - " + room.getFloor());
		} else if (entity instanceof Building) {
			setContentView(R.layout.activity_view_building);
			TextView entityLabel = (TextView) findViewById(R.id.entityLabel);
			Building building = (Building) entity;
			entityLabel.setText(building.getName());
		} else if (entity instanceof Service) {
			setContentView(R.layout.activity_view_service);
			Service service = (Service) entity;
			TextView entityLabel = (TextView) findViewById(R.id.entityLabel);
			entityLabel.setText(service.getName());
			TextView buildingAndFloor = (TextView) findViewById(R.id.buidingAndFloor);
			buildingAndFloor.setText(service.getBuilding().getName() + " - " + service.getFloor());
			
//			res += getString(R.string.service) + " " + entity.getName() + "\n";
//			res += getString(R.string.descr) + " " + ((Service) entity).label + "\n";
//			if (((Service) entity).building != null)
//				res += getString(R.string.building) + " " + ((Service) entity).building.getName() + "\n";
//			res += getString(R.string.floor) + " " + ((Service) entity).floor + "\n";
//			if (((Service) entity).keywords != null)
//				res += getString(R.string.assKeywords) + " " + ((Service) entity).keywords + "\n";
		}
	}

	public void onClickList(View v) {
		Intent intent = new Intent(this, ListEntitiesActivity.class);

		if (v.getId() == R.id.menuButtonBuildingRooms) {
			intent.putExtra("buildingID", entity.getId());
			intent.putExtra("type", "room");
		}
		if (v.getId() == R.id.menuButtonBuildingServices) {
			intent.putExtra("buildingID", entity.getId());
			intent.putExtra("type", "service");
		}

		startActivity(intent);
	}

	public void viewOnMap(View v) {
		Intent intent = new Intent(this, MapActivity.class);

		if (entity instanceof Building) {
			intent.putExtra("building", entity.getId());
		} else if (entity instanceof Room) {
			intent.putExtra("room", entity.getId());
		} else if (entity instanceof Service) {
			intent.putExtra("service", entity.getId());
		}

		startActivity(intent);
	}
	
	public void viewBuilding(View v) {
		Intent intent = new Intent(this, ViewEntityActivity.class);
		intent.putExtra("id", "building/" + entity.getBuilding().getId());
		
		startActivity(intent);
	}
}
