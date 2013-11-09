package com.exercise.timers.model;

import java.util.ArrayList;
import java.util.List;

// import com.exercise.timers.R;
import com.exercise.timers.activity.FullScreenActivity;
// import com.exercise.timers.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
// import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 初期画面のGridViewに使用するAdapter。
 * ImageLoaderライブラリを使って、非同期で表示する。
 * タッチするとFullScreenActivityに遷移する。
 * 長押しすると削除ダイアログが出る。
 * @author keigo.nakamura
 *
 */
public class ImageAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<String> file_names = null;
	private List<String> file_paths = null;
	private ImageLoader loader = null;
	
	public ImageAdapter(Context context) {
		this.context = context;
		
		if (file_names == null) {
			file_names = new ArrayList<String>();
		}
		if (file_paths == null) {
			file_paths = new ArrayList<String>();
		}
		if (loader == null) {
			loader = ImageLoader.getInstance();
		}
		
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
	public View getView(final int index, View convertedView, ViewGroup parent) {
		ImageView view = (ImageView)convertedView;
		if (view == null) {
			view = new ImageView(context);
			view.setLayoutParams(new GridView.LayoutParams(248, 248));
			view.setPadding(10, 10, 10, 10);
		}
		loader.displayImage("file://" + context.getFilesDir() + "/" + file_names.get(index), view); 
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, FullScreenActivity.class);
				intent.putExtra("file_name", file_names.get(index));
				intent.putExtra("file_path", file_paths.get(index));
				context.startActivity(intent);
			}
		});
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle(com.exercise.timers.R.string.remove_image_title);
				dialog.setMessage(com.exercise.timers.R.string.remove_image_message);
				dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface di, int which) {
						deleteLocalFile(index);
					}
				});
				dialog.setNegativeButton(android.R.string.cancel, null);
				dialog.create();
				dialog.show();
				return false;
			}
		});
		
		return view;
	}
	
	/**
	 * アプリに保存した画像を削除する。
	 * データベースからも削除し、Adapterからも削除する。
	 * 画像そのものは消さない。
	 * @param index
	 * @return
	 */
	private boolean deleteLocalFile(int index) {
		boolean result = false;
		
		context.deleteFile(file_names.get(index));
		
		TimersDBHelper helper = new TimersDBHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		int num = db.delete(TimersDBHelper.getTblImages(), "local_file_name = ?", new String[] {file_names.get(index)});
		if (db != null) {
			db.close();
		}
		if (helper != null) {
			helper.close();
		}
		
		AlertDialog.Builder dialog;
		if (num == 1) {
			dialog = createDialog(com.exercise.timers.R.string.remove_image_success);
			dialog.show();
			file_names.remove(index);
			file_paths.remove(index);
			notifyDataSetChanged();
			result = true;
		} else {
			dialog = createDialog(com.exercise.timers.R.string.remove_image_failed);
			dialog.show();
		}

		return result;
	}
	
	/**
	 * ダイアログを作成する
	 * @param messageId
	 * @return
	 */
	private AlertDialog.Builder createDialog(int messageId) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setPositiveButton(android.R.string.ok, null);
		dialog.setMessage(messageId);
		dialog.create();
		return dialog;
	}
}
