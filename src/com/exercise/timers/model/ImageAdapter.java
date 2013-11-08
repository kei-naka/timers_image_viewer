package com.exercise.timers.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<String> file_names = new ArrayList<String>();
	private List<String> file_paths = new ArrayList<String>();
	
	public ImageAdapter(Context context) {
		this.context = context;
		TimersDBHelper helper = new TimersDBHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TimersDBHelper.getTblImages(), TimersDBHelper.getColumnNames(), null, null, null, null, null);
		if (cursor.getCount() > 0) {
			for (boolean next = cursor.moveToFirst(); next; next = cursor.moveToNext()) {
				Log.d("MA", "local file name: " + cursor.getString(0));
				Log.d("MA", "original file path: " + cursor.getString(1));
				file_names.add(cursor.getString(0));
				file_paths.add(cursor.getString(1));
			}
		}
		if(cursor != null) {
			cursor.close();
		}
		if (db != null) {
			db.close();
		}
	}
	
	@Override
	public int getCount() {
		return file_names.size();
	}

	@Override
	public Object getItem(int index) {
		return (String)file_names.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View convertedView, ViewGroup parent) {
		ImageView view = (ImageView)convertedView;
		if (view == null) {
			view = new ImageView(context);
			view.setLayoutParams(new GridView.LayoutParams(248, 248));
			view.setPadding(10, 10, 10, 10);
		}
		InputStream in = null;
		try {
			in = context.openFileInput(file_names.get(index));
			Bitmap data = BitmapFactory.decodeStream(in);
			view.setImageBitmap(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		view.setTag(com.exercise.timers.R.string.file_name, file_names.get(index));
		view.setTag(com.exercise.timers.R.string.file_path, file_paths.get(index));
		return view;
	}

}
