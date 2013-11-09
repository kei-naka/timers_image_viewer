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

/**
 * FullScreenActivityで使用されるView
 * ピンチ操作による拡大・縮小と、ドラッグ操作による移動に対応している。
 * ピンチとドラッグは同時に可能（二本指でズームしながら移動など）
 * ピンチ操作の拡大縮小は、直前の距離と現在の距離の差分から倍率を求めている。
 * そのまま画像に反映させると、若干敏感すぎた（一気に拡大・縮小した）ので、重みを付けて若干ゆるやかにしている。
 * Bitmapで読み込む時に、ファイルが削除済みなどで読み込めない場合は、例外が発生する。
 * （本当は、ファイルのフルパスから再度読み込み、フルパスにない場合はダイアログを出して前画面に戻りたいが、時間がなく未実装）
 * @author keigo.nakamura
 *
 */
public class TimersImageViewer extends View {
	
	private Context context;
	private ScaleGestureDetector gestureDetector;
	private float scale = 1.0f;
	private final float WEIGHT_DECREASE = 1.05f;
	private final float WEIGHT_INCREASE = 0.95f;
	private float prev_span = 0.0f;
	private Bitmap bitmap = null;
	private Matrix matrix = new Matrix();
	private float pre_x, pre_y;
	
	public TimersImageViewer(Context context) {
		super(context);
		this.context = context;
		gestureDetector = new ScaleGestureDetector(context, listener);
	}
	
	/**
	 * Bitmapで画像を読みkもう
	 * @param file_name
	 */
	public void loadImageBitmap(String file_name) {
		InputStream in = null;
		try {
			in = context.openFileInput(file_name);
			bitmap = BitmapFactory.decodeStream(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// TODO load image data from file_path
			// if there is no original file,
			//  - throw exception,
			//  - show dialog to inform no image
			//  - delete record from database
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("TIV", "failed to close input file");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ピンチ操作用のマルチタップリスナー
	 */
	private SimpleOnScaleGestureListener listener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			prev_span = detector.getCurrentSpan();
			pre_x = detector.getFocusX();
			pre_y = detector.getFocusY();
			return true;
		}
		
		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			prev_span = detector.getCurrentSpan();
			pre_x = detector.getFocusX();
			pre_y = detector.getFocusY();
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scale = accumlateScale(detector.getCurrentSpan(), prev_span);
			matrix.postScale(scale,  scale);
			prev_span = detector.getCurrentSpan();
			return false;
		}
	};

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
	
	/**
	 * 倍率を計算する。
	 * 二点間の距離から求めた倍率をそのまま画像に反映させると、
	 * 若干敏感すぎた（一気に拡大・縮小した）ので、重みを付けて若干ゆるやかにしている。
	 * @param current
	 * @param previous
	 * @return
	 */
	private float accumlateScale(float current, float previous) {
		float scale = current / previous;
		if (scale < 1.0f) {
			scale *= WEIGHT_DECREASE;
			if (scale > 1.0f) {
				scale = current / prev_span;
			}
		} else {
			scale *= WEIGHT_INCREASE;
			if (scale < 1.0f) {
				scale = current / prev_span;
			}
		}
		return scale;
	}
}
