package com.exercise.timers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	private Context context;
	
	private Integer[] thumbs = {
			R.drawable.sample_0
			, R.drawable.sample_1
			, R.drawable.sample_2
			, R.drawable.sample_3
			, R.drawable.sample_4
			, R.drawable.sample_5
			, R.drawable.sample_6
			, R.drawable.sample_7
	};
	
	public ImageAdapter(Context c) {
		this.context = c;
	}

	@Override
	public int getCount() {
		return thumbs.length;
	}

	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return null;
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
			view.setLayoutParams(new GridView.LayoutParams(85, 85));
		}
		view.setImageResource(thumbs[index]);
		return view;
	}

}
