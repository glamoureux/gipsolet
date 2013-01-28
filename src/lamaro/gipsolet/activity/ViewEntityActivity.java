package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.CampusEntityAdapter;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewEntityActivity extends Activity {
	private CampusEntity entity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_view_entity);
		
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
		TextView tv = null;
		
		String[] entityType = extra.split("/");
		entity = db.getEntityByTypeId(entityType[0], Integer.parseInt(entityType[1]));
		String res = "";
		ListAdapter adapt = null;
		if (entity instanceof Room) {
			setContentView(R.layout.activity_view_room);
			Room room = (Room) entity;
			findViewById(R.id.buildingEntitiesList).setVisibility(View.GONE);
			entity = db.getRoomById(Integer.parseInt(entityType[1]));
			switch (((Room) entity).type) {
			case 0:
				res += getString(R.string.amphi) + " " + entity.getName() + "\n";
				break;
			default:
				res += getString(R.string.room) + " " + entity.getName() + "\n";
				break;
			}
			if (!((Room) entity).label.equals(""))
				res += getString(R.string.label) + " : " + ((Room) entity).label + "\n";
			res += getString(R.string.building) + " " + ((Room) entity).building.getName() + "\n";
			res += getString(R.string.floor) + " " + ((Room) entity).floor + "\n";
			
		 } else if (entity instanceof Building) {
			setContentView(R.layout.activity_view_building);
			tv = (TextView) findViewById(R.id.entityLabel);
			Building building = (Building) entity;
			res += getString(R.string.building) + " " + building.getName() + "\n";
//			if (!((Building) entity).label.equals(""))
//				res += getString(R.string.label) + " " + ((Building) entity).label + "\n";
//			if (((Building) entity).keywords != null)
//				res += getString(R.string.assKeywords) + " " + ((Building) entity).keywords + "\n";
			
		} else if (entity instanceof Service){
			setContentView(R.layout.activity_view_service);
			Service service = (Service) entity;
			findViewById(R.id.buildingEntitiesList).setVisibility(View.GONE);
			entity = db.getServiceById(Integer.parseInt(entityType[1]));
			res += getString(R.string.service) + " " + entity.getName() + "\n";
			res += getString(R.string.descr) + " " + ((Service) entity).label + "\n";
			if (((Service) entity).building != null)
				res += getString(R.string.building) + " " + ((Service) entity).building.getName() + "\n";
			res += getString(R.string.floor) + " " + ((Service) entity).floor + "\n";
			if (((Service) entity).keywords != null)
				res += getString(R.string.assKeywords) + " " + ((Service) entity).keywords + "\n";
		}
		tv.setText(res);		
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
}
