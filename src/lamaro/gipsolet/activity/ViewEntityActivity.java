package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.CampusEntity;
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
		/*
		Database db = new Database(this);
		String[] entityType = extra.split("/");
		CampusEntity entity;
		if (entityType[0].equals("room")) {
			entity = db.getRoomById(Integer.parseInt(entityType[1]));
		} else if (entityType[0].equals("building")) {
			entity = db.getBuildingById(Integer.parseInt(entityType[1]));
		} else {
			entity = db.getServiceById(Integer.parseInt(entityType[1]));
		}
		*/
		tv.setText(extra);//entity.toString());		
	}
}
