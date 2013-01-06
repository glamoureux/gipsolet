package lamaro.gipsolet.sqlite;

import java.util.ArrayList;
import java.util.List;

import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import misc.Polygon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {

	private static final int DB_VERSION = 2;
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
		s.latitude = c.getFloat(1);
		s.longitude = c.getFloat(2);
		s.description = c.getString(3);
		// s.building = getBuildingById(c.getInt(4));
		s.floor = c.getInt(5);

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
		r.latitude = c.getFloat(1);
		r.longitude = c.getFloat(2);
		// r.building = getBuildingById(c.getInt(3));
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
		b.latitude = c.getFloat(2);
		b.longitude = c.getFloat(3);
		b.number = c.getInt(4);
		b.label = c.getString(5);
		b.keywords = c.getString(6);

		return b;
	}
}
