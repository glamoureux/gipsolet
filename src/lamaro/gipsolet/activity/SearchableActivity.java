package lamaro.gipsolet.activity;

import java.util.List;

import lamaro.gipsolet.R;
import lamaro.gipsolet.model.CampusEntity;
import lamaro.gipsolet.sqlite.CampusEntitiesAdapter;
import lamaro.gipsolet.sqlite.Database;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;

public class SearchableActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      doMySearch(query);
	    }
	    
	    setListAdapter(new CampusEntitiesAdapter(this, R.layout.campus_entity_search_item));
	}

	private void doMySearch(String query) {
		Database db = new Database(this);
		db.open();

		List<CampusEntity> result = db.search(query);
		getListAdapter().set(result);
		
		db.close();
	}
	
	@Override
	public CampusEntitiesAdapter getListAdapter() {
		return (CampusEntitiesAdapter) super.getListAdapter();
	}
	
	@Override
	public void setListAdapter(ListAdapter adapter) {
		super.setListAdapter((CampusEntitiesAdapter) adapter);
	}
}
