package com.exercise.timers.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimersDBHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "TimersDB";
	private static int DB_VERSION = 1;
	
	private static final String TBL_IMAGES = "images";
	private static final String[] COLUMN_NAMES= {"id", "resource_id"}; 
	
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
		sql.append(", resource_id TEXT");
		sql.append(")");
		db.execSQL(sql.toString());
		Log.d("TDBH", "sql: " + sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

}
