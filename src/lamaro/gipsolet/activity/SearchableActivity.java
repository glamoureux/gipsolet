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
		System.out.println("Je suis ici");


		setListAdapter(new CampusEntitiesAdapter(this, R.layout.campus_entity_search_item));

		System.out.println("ICI = " + super.getListAdapter());
		System.out.println("ICI MDR = " + getListAdapter());
		
		handleIntent(getIntent());

		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		System.out.println("je suis la");
		setIntent(intent);
		handleIntent(intent);
	}

	private void doMySearch(String query) {
		System.out.println("REQUETE = " + query);
		Database db = new Database(this);
		db.open();

		List<CampusEntity> result = db.search(query);
		System.out.println(getListAdapter());
		getListAdapter().set(result);

		db.close();
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
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
