package com.exercise.timers.activity;

import com.exercise.timers.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class FullScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int index = bundle.getInt("index");
			Log.d("FSA", "index: " + index);
			ImageView imageView = (ImageView) findViewById(R.id.imageView);
			imageView.setImageResource(R.drawable.sample_1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
