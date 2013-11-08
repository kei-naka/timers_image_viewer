package com.exercise.timers.activity;

import java.io.File;
import java.util.ArrayList;

import com.exercise.timers.R;
import com.exercise.timers.model.ImageAdapter;
import com.exercise.timers.model.LibraryAdapter;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class LibraryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library);
		
		ArrayList<String> image_path = new ArrayList<String>();
		String sdcard_path = Environment.getExternalStorageDirectory().getPath();
		Log.d("MA", "ssdcard_path: " + sdcard_path);
		image_path = getSubFiles(sdcard_path);
		for (String file_path : image_path) {
			Log.d("MA", "file path: " + file_path);
		}
		
		GridView gridView = (GridView) findViewById(R.id.gridViewLibrary);
		gridView.setAdapter(new LibraryAdapter(this, image_path));
	}
	
	private ArrayList<String> getSubFiles(String path) {
		Log.d("MA", "scan " + path);
		
		File file_handler = new File(path);
		String file_cands[] = file_handler.list();
		ArrayList<String> file_names = new ArrayList<String>();
		
		if (file_cands == null || file_cands.length <= 0) {
			return file_names;
		}
		
		for (String file_candidate : file_cands) {
			if (file_candidate.startsWith(".")) {
				continue;
			}
			String sub_path = path + "/" + file_candidate;
			File sub_file_cands = new File(sub_path);
			if (sub_file_cands.isDirectory()) {
				ArrayList<String> sub_files = getSubFiles(sub_path);
				if (sub_files == null || sub_files.size() <= 0) {
					continue;
				}
				for (String sub_file : sub_files) {
					if (isImage(sub_file)) {
						file_names.add(sub_file);
					}
				}
			} else {
				if (isImage(file_candidate)) {
					file_names.add(sub_path);
				}
			}
		}
		return file_names;
	}
	
	private boolean isImage(String name) {
		String[] exts = {"jpg", "JPG", "jpeg", "png", "bmp", "tiff", "gif"};
		if (name.isEmpty())
			return false;
		if (name.startsWith(".")) {
			return false;
		}
		for (String ext : exts) {
			if (name.endsWith(ext))
				return true;
		}
		return false;
	}
}
