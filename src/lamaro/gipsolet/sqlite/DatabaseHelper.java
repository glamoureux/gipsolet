package lamaro.gipsolet.sqlite;

import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Context context;
	
	public DatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
	         InputStream is = context.getResources().getAssets().open("gipsolet.sql");
	        
	         String[] statements = FileHelper.parseSqlFile(is);
	        
	         for (String statement : statements) {
	           db.execSQL(statement);
	         }
	     } catch (Exception ex) {
	       ex.printStackTrace();
	     }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS services");
		db.execSQL("DROP TABLE IF EXISTS rooms");
		db.execSQL("DROP TABLE IF EXISTS buildings");
		
		onCreate(db);
	}

}
