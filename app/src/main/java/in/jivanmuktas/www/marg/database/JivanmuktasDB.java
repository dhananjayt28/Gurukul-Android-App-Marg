package in.jivanmuktas.www.marg.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JivanmuktasDB {
	/*private SQLiteOpenHelper openHelper;
	private SQLiteDatabase sqLiteDatabase;
	private static JivanmuktasDB instance;*/

	//***********************************************//
	/*private static final String Table_Name = "satsang_chapter";
	private static final String column0 = "CHAPTER_SYS_ID";
	private static final String column1 = "CHAPTER_NAME";
	private static final String column2 = "CHAPTER_DESC";
	private static final String column3 = "COUNTRY_ID";
	private static final String column4 = "CREATED_BY";
	private static final String column5 = "CREATED_DATE";
	private static final String column6 = "ISACTIVE";

	private JivanmuktasDB(Context context) { this.openHelper = new DatabaseHelper(context); }

	public String getTableName(){
		return Table_Name;
	}

	public static JivanmuktasDB getInstance(Context context) {
		if (instance == null) {
			instance = new JivanmuktasDB(context);
		}
		return instance;
	}

	/**
	 * Open the database connection.
	 */
	/*public void open() {
		this.sqLiteDatabase = openHelper.getWritableDatabase();
	}

	/**
	 * Close the database connection.
	 */
	/*public void close() {
		if (sqLiteDatabase != null) {
			this.sqLiteDatabase.close();
		}
	}

	public Cursor GetSatsangChapter(String countryId) {
		Cursor res;
		res = sqLiteDatabase.rawQuery("SELECT "+column1+" FROM " + Table_Name +" where "+column3+" = ?", new String[] {countryId});
		return res;
	}*/

	private SQLiteOpenHelper openHelper;
	private SQLiteDatabase sqLiteDatabase;
	private static JivanmuktasDB instance;

	private static final String Tb1_SyncConfigration = "SyncConfigration";

	private JivanmuktasDB(Context context) {
		this.openHelper = new DatabaseHelper(context);
	}

	public static JivanmuktasDB getInstance(Context context) {
		if (instance == null) {
			instance = new JivanmuktasDB(context);
		}
		return instance;
	}

	public void open() {
		this.sqLiteDatabase = openHelper.getWritableDatabase();
	}

	public void close() {
		if (sqLiteDatabase != null) {
			this.sqLiteDatabase.close();
		}
	}

	public boolean insertTime(String initialTime){
		ContentValues values = new ContentValues();
		values.put("LAST_SYNC_TIME",initialTime);
		values.put("INITIAL_SYNC",1);
		long checkers = sqLiteDatabase.insert(Tb1_SyncConfigration,null,values);
		if(checkers == -1) {return false;} else {return true;}
	}

	public boolean updateSync(String sync_time){
		ContentValues values = new ContentValues();
		values.put("LAST_SYNC_TIME",sync_time);
		values.put("INITIAL_SYNC",1);
		long checkers = sqLiteDatabase.update(Tb1_SyncConfigration,values,"SYNC_ID = ?", new String[] { "1" });
		if (checkers == -1) {return false;} else {return true;}
	}

	public String selectSync(){
		Cursor res = sqLiteDatabase.rawQuery(" SELECT INITIAL_SYNC FROM " + Tb1_SyncConfigration + " WHERE SYNC_ID =? " , new String[] { "1" });
		res.moveToFirst();
		return res.getString(0);

	}
	public String selectLastSyncStamp(){
		Cursor res = sqLiteDatabase.rawQuery(" SELECT LAST_SYNC_TIME FROM " + Tb1_SyncConfigration + " WHERE SYNC_ID =? " , new String[] { "1" });
		res.moveToFirst();
		return res.getString(0);

	}

}
