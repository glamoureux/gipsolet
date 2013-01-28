package lamaro.gipsolet.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.CampusEntity;
import lamaro.gipsolet.model.ContainerEntity;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import misc.StringUtils;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

public class Database {

	public static final int DB_VERSION = 512;
	
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
	private HashMap<String, String> columnsMap = buildColumnsMap();

	public Database(Context ctx) {
		helper = DatabaseHelper.getInstance(ctx);
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
		 * to actual columns in the database, creating a simple column aliadesign vue d'un bâtiment
		 * mechanism by which the ContentProvider does not need to know the real
		 * column names
		 */
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TABLE_KEYWORDS);
		builder.setProjectionMap(columnsMap);

		Cursor cursor = builder.query(helper.getReadableDatabase(), columns, selection, selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	@SuppressLint("DefaultLocale")
	public Cursor searchCampusEntitiesMatches(String query) {
		String selection = "words MATCH ?";
		String[] selectionArgs = new String[] { StringUtils.removeAccents(query.trim().toLowerCase()).replaceAll(" ", "* ") + "*" };

		return query(selection, selectionArgs, null);
	}
	
	public CampusEntity getEntityByTypeId(String type, int id) {
		if (type.equals("building")) {
			return getBuildingById(id);
		} else if (type.equals("room")) {
			return getRoomById(id);
		} else if (type.equals("service")) {
			return getServiceById(id);
		} else {
			return null;
		}
	}

	public Building getBuildingById(int id) {
		Cursor c = helper.getReadableDatabase().query("buildings", null, "id = " + id, null, null, null, null);

		Building result = null;
		if (c.moveToFirst()) {
			result = cursorToBuilding(c);
		}
		c.close();

		return result;
	}

	public Room getRoomById(int id) {
		Cursor c = helper.getReadableDatabase().query("rooms", null, "id = " + id, null, null, null, null);

		Room result = null;
		if (c.moveToFirst()) {
			result = cursorToRoom(c);
		}
		c.close();

		return result;
	}

	public Service getServiceById(int id) {
		Cursor c = helper.getReadableDatabase().query("services", null, "id = " + id, null, null, null, null);

		Service result = null;
		if (c.moveToFirst()) {
			result = cursorToService(c);
		}
		c.close();

		return result;
	}

	public Cursor getCursorBuildings() {
		return helper.getReadableDatabase().query("buildings", null, null, null, null, null, "id");
	}
	
	public List<Building> getBuildings() {
		Cursor c = getCursorBuildings();

		List<Building> result = cursorToBuildings(c);
		c.close();

		return result;
	}

	public List<Room> getRoomsOfBuilding(Building b) {
		Cursor c = helper.getReadableDatabase().query("rooms", null, "building_id = " + b.id, null, null, null, "id");

		List<Room> result = cursorToRooms(c);
		c.close();

		return result;
	}

	public List<Service> getServicesOfBuilding(Building b) {
		Cursor c = helper.getReadableDatabase().query("services", null, "building_id = " + b.id, null, null, null, "id");

		List<Service> result = cursorToServices(c);
		c.close();

		return result;
	}
	
	public Cursor getCursorRooms() {
		return helper.getReadableDatabase().query("rooms", null, null, null, null, null, "id");
	}
	
	public List<Room> getRooms() {
		Cursor c = getCursorRooms();

		List<Room> result = cursorToRooms(c);
		c.close();

		return result;
	}
	
	public Cursor getCursorServices() {
		return helper.getReadableDatabase().query("services", null, null, null, null, null, "id");
	}
	
	public List<Service> getServices() {
		Cursor c = getCursorServices();

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
		s.latlng = new LatLng((double)c.getFloat(1), (double)c.getFloat(2));
		s.building = getBuildingById(c.getInt(3));
		s.floor = c.getInt(4);
		s.label = c.getString(5);

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
		r.latlng = new LatLng((double)c.getFloat(1), (double)c.getFloat(2));
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
		b.shape = ContainerEntity.stringToShape(c.getString(1));
		b.latlng = new LatLng((double)c.getFloat(2), (double)c.getFloat(3));
		b.number = c.getInt(4);
		b.label = c.getString(5);

		return b;
	}
}
