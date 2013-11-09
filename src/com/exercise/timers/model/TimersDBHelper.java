package com.exercise.timers.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * このアプリで使用するデータベース設定
 * @author keigo
 *
 */
public class TimersDBHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "TimersDB";
	private static int DB_VERSION = 1;
	
	private static final String TBL_IMAGES = "images";
	private static final String[] COLUMN_NAMES= {"local_file_name", "org_file_path"};
	
	public TimersDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public static String getTblImages() {
		return TBL_IMAGES;
	}
	
	public static String[] getColumnNames() {
		return COLUMN_NAMES;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE ");
		sql.append(TBL_IMAGES);
		sql.append(" (");
		sql.append("id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");
		sql.append(", local_file_name TEXT NOT NULL");
		sql.append(", org_file_path TEXT NOT NULL");
		sql.append(")");
		db.execSQL(sql.toString());
		Log.d("TDBH", "sql: " + sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
