package com.exercise.timers.activity;

import com.exercise.view.TimersImageViewer;

import android.os.Bundle;
import android.app.Activity;
import android.view.ViewGroup.LayoutParams;

/**
 * 初期画面で各画像をタップした時に表示される、全画面表示用のアクティビティ
 * @author keigo.nakamura
 *
 */
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
}
