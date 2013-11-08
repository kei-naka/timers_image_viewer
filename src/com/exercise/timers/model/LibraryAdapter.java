package com.exercise.timers.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.exercise.timers.activity.LibraryActivity;
import com.exercise.timers.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class LibraryAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> thumbs;
	
	public LibraryAdapter(Context context, ArrayList<String> thumb_paths) {
		this.context = context;
		thumbs = thumb_paths;
	}
	
	@Override
	public int getCount() {
		return thumbs.size();
	}

	@Override
	public Object getItem(int index) {
		return thumbs.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(final int index, View convertedView, ViewGroup parent) {
		ImageView view = (ImageView)convertedView;
		if (view == null) {
			view = new ImageView(context);
			view.setLayoutParams(new GridView.LayoutParams(248, 248));
			view.setPadding(10, 10, 10, 10);
		}
		final String imageUrl = "file://" + thumbs.get(index);
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(imageUrl, view);
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle(com.exercise.timers.R.string.save_image_title);
				dialog.setMessage(com.exercise.timers.R.string.save_image_message);
				dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface di, int which) {
						String file_name = new File(imageUrl).getName();
						TimersDBHelper helper = new TimersDBHelper(context);
						SQLiteDatabase db = helper.getWritableDatabase();
						ContentValues values = new ContentValues();
						values.put("local_file_name", file_name);
						values.put("org_file_path", imageUrl);
						db.insert(TimersDBHelper.getTblImages(), "0", values);
						db.close();
						helper.close();
						
						BitmapFactory.Options imageOptions = new BitmapFactory.Options();
						imageOptions.inJustDecodeBounds = true;
						InputStream in = null;
						FileOutputStream out = null;
						Bitmap bitmap;
						try {
							bitmap = BitmapFactory.decodeFile(thumbs.get(index));
							out = context.openFileOutput(file_name, Context.MODE_PRIVATE);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} finally {
							if (in != null) {
								try {
									in.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							if (out != null) {
								try {
									out.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						Intent intent = new Intent(context, MainActivity.class);
						context.startActivity(intent);
					}
				});
				dialog.setNegativeButton(R.string.cancel, null);
				dialog.create();
				dialog.show();
			}
		});
		
		return view;
	}
}
