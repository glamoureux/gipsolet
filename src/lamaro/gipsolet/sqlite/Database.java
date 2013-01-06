package lamaro.gipsolet.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import misc.Polygon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "gipsolet";

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public Database(Context ctx) {
		dbHelper = new DatabaseHelper(ctx, DB_NAME, null, DB_VERSION);
	}

	public void open() {
		db = dbHelper.getWritableDatabase();
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public void close() {
		db.close();
	}

	public Set<Object> search(String query) {
		Set<Object> result = new HashSet<Object>();
		HashMap<Object, Integer> partialResults = new HashMap<Object, Integer>();
		String[] queryPieces = query.split(" ");
		
		partialResults.clear();
		for (String piece : queryPieces) {
			for (String column : new String[] { "number", "keywords", "label" }) {
				Cursor c = db.query("buildings", null, column + " LIKE '%" + piece + "%'", null, null, null, null);

				for (Building b: cursorToBuildings(c)) {
					if (!partialResults.containsKey(b)) {
						partialResults.put(b, 0);
					}
					partialResults.put(b, partialResults.get(b) + 1);
				}
				c.close();
			}
		}
		

		for (String piece : queryPieces) {
			for (String column : new String[] { "label", "building_id" }) {
				Cursor c = db.query("rooms", null, column + " LIKE '%" + piece + "%'", null, null, null, null);
				
				for (Room r: cursorToRooms(c)) {
					if (!partialResults.containsKey(r)) {
						partialResults.put(r, 0);
					}
					partialResults.put(r, partialResults.get(r) + 1);
				}
				c.close();
			}
		}
		
		for (String piece : queryPieces) {
			for (String column : new String[] { "description", "building_id", "keywords" }) {
				Cursor c = db.query("services", null, column + " LIKE '%" + piece + "%'", null, null, null, null);
				
				for (Service s: cursorToServices(c)) {
					if (!partialResults.containsKey(s)) {
						partialResults.put(s, 0);
					}
					partialResults.put(s, partialResults.get(s) + 1);
				}
				c.close();
			}
		}
		
		for (Entry<Object, Integer> entry: partialResults.entrySet()) {
			if (entry.getValue() >= queryPieces.length) {
				result.add(entry.getKey());
			}
		}
		
		return result;
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
