package in.jivanmuktas.www.marg.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JivanmuktasDB {
	private SQLiteOpenHelper openHelper;
	private SQLiteDatabase sqLiteDatabase;
	private static JivanmuktasDB instance;

	//***********************************************//
	private static final String Table_Name = "satsang_chapter";
	private static final String column0 = "CHAPTER_SYS_ID";
	private static final String column1 = "CHAPTER_NAME";
	private static final String column2 = "CHAPTER_DESC";
	private static final String column3 = "COUNTRY_ID";
	private static final String column4 = "CREATED_BY";
	private static final String column5 = "CREATED_DATE";
	private static final String column6 = "ISACTIVE";

	private JivanmuktasDB(Context context) {
		this.openHelper = new DatabaseHelper(context);

	}

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
	public void open() {
		this.sqLiteDatabase = openHelper.getWritableDatabase();
	}

	/**
	 * Close the database connection.
	 */
	public void close() {
		if (sqLiteDatabase != null) {
			this.sqLiteDatabase.close();
		}
	}

	public Cursor GetSatsangChapter(String countryId) {
		Cursor res;
		res = sqLiteDatabase.rawQuery("SELECT "+column1+" FROM " + Table_Name +" where "+column3+" = ?", new String[] {countryId});
		return res;
	}

}
