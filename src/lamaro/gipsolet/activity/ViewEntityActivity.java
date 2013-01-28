package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.CampusEntity;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewEntityActivity extends Activity {
	private CampusEntity entity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.go_home, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.go_home:
			startActivity(new Intent(this, MainActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
