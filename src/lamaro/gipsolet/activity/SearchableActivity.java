package lamaro.gipsolet.activity;

import java.util.List;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Adapter;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.model.CampusEntity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;

public class SearchableActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new Adapter(this, R.layout.campus_entity_search_item));
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void doMySearch(String query) {
//		Database db = new Database(this);
//		db.open();
//
//		List<CampusEntity> result = db.search(query);
//		System.out.println(getListAdapter());
//		getListAdapter().set(result);
//
//		db.close();
	}

	private void handleIntent(Intent intent) {
		System.out.println(intent);
		System.out.println(intent.getAction());
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			System.out.println("DATA = " + intent.getDataString());
		}
	}

	@Override
	public Adapter getListAdapter() {
		return (Adapter) super.getListAdapter();
	}

	@Override
	public void setListAdapter(ListAdapter adapter) {
		super.setListAdapter((Adapter) adapter);
	}
}
