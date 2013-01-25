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
		
		String extra = intent.getStringExtra("entityType");
		//listEntities(extra);
		
		Database db = new Database(this);
		if (extra.contains(Database.TABLE_BUILDINGS)) {
			listEntities(db.getBuildings());
		} else if (extra.contains(Database.TABLE_ROOMS)) {
			listEntities(db.getRooms());
		} else if (extra.contains(Database.TABLE_SERVICES)) {
			listEntities(db.getServices());
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
	
	private void listEntities(List<? extends CampusEntity> ces) {
		TextView resultText = (TextView) findViewById(R.id.text_result);
		if (ces == null) {
			resultText.setText(getString(R.string.no_results, new Object[] { getString(R.string.entity) }));
		} else {
			// Display the number of results
			int count = ces.size();
			String countString = getResources().getQuantityString(R.plurals.search_results, count,
					new Object[] { count, getString(R.string.entity) });
			resultText.setText(countString);

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			ListAdapter adapter = new CampusEntityAdapter(this, ces);
			setListAdapter(adapter);
		}		
	}
}
