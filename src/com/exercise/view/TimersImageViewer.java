package com.exercise.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;

public class TimersImageViewer extends View {
	
	private Context context;
	private ScaleGestureDetector gestureDetector;
	private float scale = 1.0f;
	private float prev_span = 0.0f;
	private Bitmap bitmap = null;
	private Matrix matrix = new Matrix();
	private float pre_x, pre_y;
	
	private SimpleOnScaleGestureListener listener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			invalidate();
			prev_span = detector.getCurrentSpan();
			pre_x = detector.getFocusX();
			pre_y = detector.getFocusY();
			return super.onScaleBegin(detector);
		}
		
		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			prev_span = detector.getCurrentSpan();
			pre_x = detector.getFocusX();
			pre_y = detector.getFocusY();
			invalidate();
			super.onScaleEnd(detector);
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scale = detector.getCurrentSpan() / prev_span;
			prev_span = detector.getCurrentSpan();
			matrix.postScale(scale,  scale);
			invalidate();
			return false;
		}
	};

	public TimersImageViewer(Context context) {
		super(context);
		this.context = context;
		gestureDetector = new ScaleGestureDetector(context, listener);
	}
	
	public void loadImageBitmap(String file_name) {
		InputStream in = null;
		try {
			in = context.openFileInput(file_name);
			bitmap = BitmapFactory.decodeStream(in);
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
		// image.setBounds(0,  0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, matrix, null);
		canvas.save();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			pre_x = ev.getX();
			pre_y = ev.getY();
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
				matrix.postTranslate(ev.getX() - pre_x, ev.getY() - pre_y);
				pre_x = ev.getX();
				pre_y = ev.getY();
				invalidate();
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			pre_x = ev.getX();
			pre_y = ev.getY();
		}
		return true;
	}
}
