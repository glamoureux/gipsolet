package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.CEAdapter;
import lamaro.gipsolet.data.Provider;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchableActivity extends ListActivity {

	private TextView resultText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ce_search);
		
		resultText = (TextView) findViewById(R.id.text_result);
		
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void doMySearch(String query) {
		CursorLoader loader = new CursorLoader(this, Provider.CONTENT_URI, null, null, new String[] { query }, null);
		Cursor cursor = loader.loadInBackground();

		if (cursor == null) {
			resultText.setText(getString(R.string.no_results, new Object[] { query }));
		} else {
			// Display the number of results
			int count = cursor.getCount();
			String countString = getResources().getQuantityString(R.plurals.search_results, count,
					new Object[] { count, query });
			resultText.setText(countString);

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			ListAdapter adapter = new CEAdapter(this, R.layout.ce_search_item, cursor, 0);
			setListAdapter(adapter);
		}
	}

	private void handleIntent(Intent intent) {
		System.out.println(intent);
		System.out.println(intent.getAction());
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			System.out.println("DATA = " + intent.getDataString());
			
			Intent intent1 = new Intent(this, ViewEntityActivity.class);
			intent1.putExtra("id", intent.getDataString());
			startActivity(intent1);
		}
	}
}
