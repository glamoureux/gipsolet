package lamaro.gipsolet.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import lamaro.gipsolet.R;
import misc.CSVReader;
import misc.SQLFileParser;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Context context;

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Database.TABLE_BUILDINGS + " (" + Database.CE_ID + " INTEGER PRIMARY KEY,"
				+ Database.CE_ZONE + " TEXT," + Database.CE_P_LAT + " NUMERIC," + Database.CE_P_LON + " NUMERIC,"
				+ Database.CE_NUMBER + " NUMERIC," + Database.CE_LABEL + " TEXT);");

		db.execSQL("CREATE TABLE " + Database.TABLE_ROOMS + " (" + Database.CE_ID + " INTEGER PRIMARY KEY,"
				+ Database.CE_P_LAT + " NUMERIC," + Database.CE_P_LON + " NUMERIC," + Database.CE_BUILDING
				+ " NUMERIC," + Database.CE_FLOOR + " NUMERIC," + Database.CE_TYPE_ROOM + " NUMERIC," + Database.CE_LABEL
				+ " TEXT);");

		db.execSQL("CREATE TABLE " + Database.TABLE_SERVICES + " (" + Database.CE_ID + " INTEGER PRIMARY KEY,"
				+ Database.CE_P_LAT + " NUMERIC," + Database.CE_P_LON + " NUMERIC," + Database.CE_BUILDING
				+ " NUMERIC," + Database.CE_FLOOR + " NUMERIC," + Database.CE_LABEL + " TEXT);");

		db.execSQL("CREATE VIRTUAL TABLE " + Database.TABLE_KEYWORDS + " USING fts3 (" + Database.KEYWORDS_WORDS + ","
				+ Database.KEYWORDS_NAME + "," + Database.KEYWORDS_ICON + "," + Database.KEYWORDS_TYPE + "," + Database.KEYWORDS_ID + ");");

		addBuildings(db);
		addRooms(db);
		addServices(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_SERVICES);
		db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_ROOMS);
		db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_BUILDINGS);
		db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_KEYWORDS);

		onCreate(db);
	}

	private void addBuildings(SQLiteDatabase db) {
		CSVReader reader;
		try {
			reader = new CSVReader(new BufferedReader(new InputStreamReader(context.getResources().getAssets()
					.open("buildings.csv"))), ';', '"', 1);

			String[] line = null;
			ContentValues values = null;

			while (null != (line = reader.readNext())) {
				values = new ContentValues();
				values.put(Database.CE_ID, line[0]);
				values.put(Database.CE_ZONE, line[1]);
				values.put(Database.CE_P_LAT, line[2]);
				values.put(Database.CE_P_LON, line[3]);
				values.put(Database.CE_NUMBER, line[4]);
				values.put(Database.CE_LABEL, line[5]);
				db.insert(Database.TABLE_BUILDINGS, null, values);

				values = new ContentValues();
				values.put(Database.KEYWORDS_WORDS, line[6] + " " + line[4] + " " + line[5]);
				values.put(Database.KEYWORDS_NAME, line[5]);
				values.put(Database.KEYWORDS_ICON, R.drawable.building);
				values.put(Database.KEYWORDS_TYPE, "building");
				values.put(Database.KEYWORDS_ID, line[0]);
				db.insert(Database.TABLE_KEYWORDS, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addRooms(SQLiteDatabase db) {
		CSVReader reader;
		try {
			reader = new CSVReader(new BufferedReader(new InputStreamReader(context.getResources().getAssets()
					.open("rooms.csv"))), ';', '"', 1);

			String[] line = null;
			ContentValues values = null;

			while (null != (line = reader.readNext())) {
				values = new ContentValues();
				values.put(Database.CE_ID, line[0]);
				values.put(Database.CE_P_LAT, line[1]);
				values.put(Database.CE_P_LON, line[2]);
				values.put(Database.CE_BUILDING, line[3]);
				values.put(Database.CE_FLOOR, line[4]);
				values.put(Database.CE_TYPE_ROOM, line[5]);
				values.put(Database.CE_LABEL, line[6]);
				db.insert(Database.TABLE_ROOMS, null, values);

				values = new ContentValues();
				values.put(Database.KEYWORDS_WORDS, line[6].replace(".", " "));
				values.put(Database.KEYWORDS_NAME, line[6]);
				values.put(Database.KEYWORDS_ICON, R.drawable.room);
				values.put(Database.KEYWORDS_TYPE, "room");
				values.put(Database.KEYWORDS_ID, line[0]);
				db.insert(Database.TABLE_KEYWORDS, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addServices(SQLiteDatabase db) {
		CSVReader reader;
		try {
			reader = new CSVReader(new BufferedReader(new InputStreamReader(context.getResources().getAssets()
					.open("services.csv"))), ';', '"', 1);

			String[] line = null;
			ContentValues values = null;

			while (null != (line = reader.readNext())) {
				values = new ContentValues();
				values.put(Database.CE_ID, line[0]);
				values.put(Database.CE_P_LAT, line[1]);
				values.put(Database.CE_P_LON, line[2]);
				values.put(Database.CE_LABEL, line[3]);
				values.put(Database.CE_BUILDING, line[4]);
				values.put(Database.CE_FLOOR, line[5]);
				db.insert(Database.TABLE_SERVICES, null, values);

				values = new ContentValues();
				values.put(Database.KEYWORDS_WORDS, line[6]);
				values.put(Database.KEYWORDS_NAME, line[3]);
				values.put(Database.KEYWORDS_ICON, R.drawable.service);
				values.put(Database.KEYWORDS_TYPE, "service");
				values.put(Database.KEYWORDS_ID, line[0]);
				db.insert(Database.TABLE_KEYWORDS, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
