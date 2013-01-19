package lamaro.gipsolet.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import misc.Polygon;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

public class Database {

	private static final int DB_VERSION = 2;
	
	public static final String DB_NAME = "gipsolet";
	public static final String TABLE_BUILDINGS = "buildings";
	public static final String TABLE_ROOMS = "rooms";
	public static final String TABLE_SERVICES = "services";
	public static final String TABLE_KEYWORDS = "keywords";
	
	public static final String CE_ID = "id";
	public static final String CE_ZONE = "zone";
	public static final String CE_P_LAT = "p_lat";
	public static final String CE_P_LON = "p_lon";
	public static final String CE_NUMBER = "number";
	public static final String CE_LABEL = "label";
	public static final String CE_FLOOR = "floor";
	public static final String CE_TYPE_ROOM = "type";
	public static final String CE_BUILDING = "building_id";
	
	public static final String KEYWORDS_WORDS = "words";
	public static final String KEYWORDS_NAME = SearchManager.SUGGEST_COLUMN_TEXT_1;
	public static final String KEYWORDS_ICON = SearchManager.SUGGEST_COLUMN_ICON_1;
	public static final String KEYWORDS_TYPE = SearchManager.SUGGEST_COLUMN_INTENT_DATA;
	public static final String KEYWORDS_ID = SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID;

	private DatabaseHelper helper;
	private SQLiteDatabase db;
	private HashMap<String, String> columnsMap = buildColumnsMap();

	public Database(Context ctx) {
		helper = new DatabaseHelper(ctx, DB_NAME, null, DB_VERSION);
	}

	private static HashMap<String, String> buildColumnsMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(KEYWORDS_NAME, KEYWORDS_NAME);
		map.put(KEYWORDS_ICON, KEYWORDS_ICON);
		map.put(KEYWORDS_TYPE, KEYWORDS_TYPE);
		map.put(KEYWORDS_ID, KEYWORDS_ID);
		map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);

		return map;
	}

	/**
	 * Performs a database query.
	 * 
	 * @param selection
	 *            The selection clause
	 * @param selectionArgs
	 *            Selection arguments for "?" components in the selection
	 * @param columns
	 *            The columns to return
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor query(String selection, String[] selectionArgs, String[] columns) {
		/*
		 * The SQLiteBuilder provides a map for all possible columns requested
		 * to actual columns in the database, creating a simple column alias
		 * mechanism by which the ContentProvider does not need to know the real
		 * column names
		 */
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables("keywords");
		builder.setProjectionMap(columnsMap);

		Cursor cursor = builder.query(helper.getReadableDatabase(), columns, selection, selectionArgs, null, null, null);

		if (cursor == null) {
			System.out.println("pas de resultat 1");
			return null;
		} else if (!cursor.moveToFirst()) {
			System.out.println("pas de resultat 2");
			cursor.close();
			return null;
		}

		return cursor;
	}

	@SuppressLint("DefaultLocale")
	public Cursor searchCampusEntitiesMatches(String query) {
		String selection = "words MATCH ?";
		String[] selectionArgs = new String[] { query.trim().toLowerCase().replaceAll(" ", "* ") + "*" };

		return query(selection, selectionArgs, null);
	}

	public Building getBuildingById(int id) {
		Cursor c = db.query("buildings", null, "id = " + id, null, null, null, null);

		Building result = null;
		if (c.moveToFirst()) {
			result = cursorToBuilding(c);
		}
		c.close();

		return result;
	}

	public Room getRoomById(int id) {
		Cursor c = db.query("rooms", null, "id = " + id, null, null, null, null);

		Room result = null;
		if (c.moveToFirst()) {
			result = cursorToRoom(c);
		}
		c.close();

		return result;
	}

	public Service getServiceById(int id) {
		Cursor c = db.query("services", null, "id = " + id, null, null, null, null);

		Service result = null;
		if (c.moveToFirst()) {
			result = cursorToService(c);
		}
		c.close();

		return result;
	}

	public List<Building> getBuildings() {
		Cursor c = db.query("buildings", null, null, null, null, null, "id");

		List<Building> result = cursorToBuildings(c);
		c.close();

		return result;
	}

	public List<Room> getRoomsOfBuilding(Building b) {
		Cursor c = db.query("rooms", null, "building_id = " + b.id, null, null, null, "id");

		List<Room> result = cursorToRooms(c);
		c.close();

		return result;
	}

	public List<Service> getServicesOfBuilding(Building b) {
		Cursor c = db.query("services", null, "building_id = " + b.id, null, null, null, "id");

		List<Service> result = cursorToServices(c);
		c.close();

		return result;
	}

	private List<Service> cursorToServices(Cursor c) {
		List<Service> result = new ArrayList<Service>(c.getCount());

		while (c.moveToNext()) {
			result.add(cursorToService(c));
		}

		return result;
	}

	private Service cursorToService(Cursor c) {
		Service s = new Service();
		s.id = c.getInt(0);
		s.position.x = c.getFloat(1);
		s.position.y = c.getFloat(2);
		s.description = c.getString(3);
		s.building = getBuildingById(c.getInt(4));
		s.floor = c.getInt(5);
		s.keywords = c.getString(6);

		return s;
	}

	private List<Room> cursorToRooms(Cursor c) {
		List<Room> result = new ArrayList<Room>(c.getCount());

		while (c.moveToNext()) {
			result.add(cursorToRoom(c));
		}

		return result;
	}

	private Room cursorToRoom(Cursor c) {
		Room r = new Room();
		r.id = c.getInt(0);
		r.position.x = c.getFloat(1);
		r.position.y = c.getFloat(2);
		r.building = getBuildingById(c.getInt(3));
		r.floor = c.getInt(4);
		r.type = c.getInt(5);
		r.label = c.getString(6);

		return r;
	}

	private List<Building> cursorToBuildings(Cursor c) {
		List<Building> result = new ArrayList<Building>(c.getCount());

		while (c.moveToNext()) {
			result.add(cursorToBuilding(c));
		}

		return result;
	}

	private Building cursorToBuilding(Cursor c) {
		Building b = new Building();
		b.id = c.getInt(0);
		b.zone = Polygon.createFrom(c.getString(1));
		b.position.x = c.getFloat(2);
		b.position.y = c.getFloat(3);
		b.number = c.getInt(4);
		b.label = c.getString(5);
		b.keywords = c.getString(6);

		return b;
	}
}
