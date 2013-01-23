package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.CampusEntity;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ViewEntityActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_entity);
		
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {		
		
		String extra = intent.getStringExtra("id");
		TextView tv = (TextView) findViewById(R.id.entityLabel);
		
		Database db = new Database(this);
		String[] entityType = extra.split("/");
		CampusEntity entity;
		String res = "";
		if (entityType[0].equals("room")) {
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
				res += getString(R.string.label) + " " + ((Room) entity).label + "\n";
			res += getString(R.string.building) + " " + ((Room) entity).building.getName() + "\n";
			res += getString(R.string.floor) + " " + ((Room) entity).floor + "\n";
			
		} else if (entityType[0].equals("building")) {
			entity = db.getBuildingById(Integer.parseInt(entityType[1]));
			res += getString(R.string.building) + " " + entity.getName() + "\n";
			if (!((Building) entity).label.equals(""))
				res += getString(R.string.label) + " " + ((Building) entity).label + "\n";
			if (((Building) entity).keywords != null)
				res += getString(R.string.assKeywords) + " " + ((Building) entity).keywords + "\n";
			
		} else if (entityType[0].equals("service")){
			entity = db.getServiceById(Integer.parseInt(entityType[1]));
			res += getString(R.string.service) + " " + entity.getName() + "\n";
			res += getString(R.string.descr) + " " + ((Service) entity).description + "\n";
			if (((Service) entity).building != null)
				res += getString(R.string.building) + " " + ((Service) entity).building.getName() + "\n";
			res += getString(R.string.floor) + " " + ((Service) entity).floor + "\n";
			if (((Service) entity).keywords != null)
				res += getString(R.string.assKeywords) + " " + ((Service) entity).keywords + "\n";
		}
		tv.setText(res);		
	}
}
