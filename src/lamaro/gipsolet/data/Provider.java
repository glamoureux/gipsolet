package lamaro.gipsolet.data;

import lamaro.gipsolet.R;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.CampusEntity;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class Provider extends ContentProvider {
	public static final String KEY_TITLE = SearchManager.SUGGEST_COLUMN_TEXT_1;
	public static final String KEY_ICON_TYPE = SearchManager.SUGGEST_COLUMN_ICON_1;
	public static final String KEY_ENTITY_ID = SearchManager.SUGGEST_COLUMN_INTENT_DATA;

	private Database db;

	@Override
	public boolean onCreate() {
		db = new Database(getContext());

		return true;
	}

	/**
	 * Handles all the dictionary searches and suggestion queries from the
	 * Search Manager. When requesting a specific word, the uri alone is
	 * required. When searching all of the dictionary for matches, the
	 * selectionArgs argument must carry the search query as the first element.
	 * All other arguments are ignored.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String query = uri.getLastPathSegment();
		if (query.length() < 3) {
			return null;
		}

		db.open();
		MatrixCursor result = new MatrixCursor(new String[] { BaseColumns._ID, KEY_TITLE, KEY_ICON_TYPE, KEY_ENTITY_ID });
		int cpt = 0;
		for (CampusEntity ce : db.search(query)) {
			int icon = 0;
			String id = "";

			if (ce instanceof Building) {
				icon = R.drawable.building;
				id = "building/" + ce.getId();
			} else if (ce instanceof Room) {
				icon = R.drawable.room;
				id = "room/" + ce.getId();
			} else if (ce instanceof Service) {
				icon = R.drawable.service;
				id = "service/" + ce.getId();
			}

			result.addRow(new Object[] { ce.getId(), ce.getName(), icon, id });

			if (cpt >= 10) {
				break;
			}

			cpt++;
		}

		return result;
	}

	/**
	 * This method is required in order to query the supported types. It's also
	 * useful in our own query() method to determine the type of Uri received.
	 */
	@Override
	public String getType(Uri uri) {
		return "";
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}
}
