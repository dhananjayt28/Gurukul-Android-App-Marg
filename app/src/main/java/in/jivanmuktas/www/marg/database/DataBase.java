package in.jivanmuktas.www.marg.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "myDatabase.db";
	private static final String TableGeneral = "General";
	private static final String TableHOD = "HOD";

	private static final String column0 = "ID";
	private static final String column1 = "NOTIFICATION";
	private static final String column2 = "DATE";
	private static final String column3 = "MOBILE";
	private static final String column4 = "EMAIL";

	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query;
		query = "CREATE TABLE " + TableGeneral + "(" + column0 + " INTEGER PRIMARY KEY AUTOINCREMENT," + column1 + " TEXT,"+ column2 + " TEXT)";
		db.execSQL(query);
		query = "CREATE TABLE " + TableHOD + "(" + column0 + " INTEGER PRIMARY KEY AUTOINCREMENT," + column1 + " TEXT,"+ column2 + " TEXT,"+ column3 + " TEXT,"+ column4 + " TEXT)";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TableGeneral);
		onCreate(db);
	}

	public boolean addToGeneral(String notification, String date) {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		//contentValues.put(column0, ui);
		contentValues.put(column1, notification);
		contentValues.put(column2, date);

		long ckeckers = sqLiteDatabase.insert(TableGeneral, null, contentValues);

		if (ckeckers == -1) {
			return false;
		} else {
			return true;
		}
	}
	public boolean addToHOD(String notification, String date,String moblie, String email) {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		//contentValues.put(column0, ui);
		contentValues.put(column1, notification);
		contentValues.put(column2, date);
		contentValues.put(column3, moblie);
		contentValues.put(column4, email);
		long ckeckers = sqLiteDatabase.insert(TableHOD, null, contentValues);

		if (ckeckers == -1) {
			return false;
		} else {
			return true;
		}
	}
	public Cursor displayGeneral() {
		SQLiteDatabase sqLiteDatabase = getReadableDatabase();
		Cursor res;
		res = sqLiteDatabase.rawQuery("SELECT * FROM " + TableGeneral, null);
		return res;
	}
	public Cursor displayHOD() {
		SQLiteDatabase sqLiteDatabase = getReadableDatabase();
		Cursor res;
		res = sqLiteDatabase.rawQuery("SELECT * FROM " + TableHOD, null);
		return res;
	}
	public int deleteGenRow(String id) {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		return sqLiteDatabase.delete(TableGeneral, "ID = ?", new String[] { id });
	}
	public int deleteHODRow(String id) {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		return sqLiteDatabase.delete(TableHOD, "ID = ?", new String[] { id });
	}

	public void deleteGeneral() {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		//sqLiteDatabase.execSQL("DELETE * FROM "+TableGeneral);
		sqLiteDatabase.delete(TableGeneral, null, null);
	}
	public void deleteHOD() {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		//sqLiteDatabase.execSQL("DELETE * FROM "+TableGeneral);
		sqLiteDatabase.delete(TableHOD, null, null);
	}
}
