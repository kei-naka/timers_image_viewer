package com.exercise.timers.activity;

import com.exercise.timers.R;
import com.exercise.view.TimersImageViewer;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;

public class FullScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String file_name = bundle.getString("file_name");
			TimersImageViewer imageViewer = new TimersImageViewer(this);
			imageViewer.loadImageBitmap(file_name);
			imageViewer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			setContentView(imageViewer);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
