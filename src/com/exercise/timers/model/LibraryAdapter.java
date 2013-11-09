package com.exercise.timers.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.exercise.timers.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

/**
 * LibraryActivity��GridView�Ɏg�p����Adapter�B
 * ImageLoader���C�u�������g���āA�񓯊��ŕ\������B
 * �摜���N���b�N����ƁA�k�������ɃA�v���ɕۑ��A���k���ăA�v���ɕۑ����I�ׂ�B
 * @author keigo.nakamura
 *
 */
public class LibraryAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> file_paths = null;
	private static final float MAX_LENGTH = 960f;
	public static Intent intent = null;
	private ImageLoader loader = null;
	// TODO file_name �� imageUrl���C���X�^���X�ϐ��ɂ���
	
	public LibraryAdapter(Context context, ArrayList<String> thumb_paths) {
		this.context = context;
		file_paths = thumb_paths;
		if (loader == null) {
			loader = ImageLoader.getInstance();
		}
	}
	
	@Override
	public int getCount() {
		if (file_paths == null) {
			return 0;
		}
		return file_paths.size();
	}

	@Override
	public Object getItem(int index) {
		if (file_paths == null) {
			return null;
		}
		return file_paths.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(final int index, View convertedView, ViewGroup parent) {
		if (file_paths == null) {
			return null;
		}
		ImageView view = (ImageView)convertedView;
		if (view == null) {
			view = new ImageView(context);
			view.setLayoutParams(new GridView.LayoutParams(248, 248));
			view.setPadding(10, 10, 10, 10);
		}
		final String imageUrl = getImageUrl(file_paths.get(index));
		loader.displayImage(imageUrl, view);
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (canSaved(file_paths.get(index))) {
					AlertDialog.Builder dialog = createDialog(file_paths.get(index));
					dialog.show();
				} else {
					showErrorDialog();
				}
			}
		});
		
		return view;
	}
	
	/**
	 * �摜��ۑ�����_�C�A���O���쐬����B
	 * �V�K�ŕۑ����鎞�́A�f�[�^�x�[�X�ɂ��o�^����B
	 * ���ɓo�^����Ă��鎞�́A�����̃t�@�C�����폜������㏑������B
	 * �k�����邵�Ȃ��Ɋւ�炸�A�ۑ��������͏�����ʂɑJ�ڂ���B
	 * @param file_path
	 * @return
	 */
	private AlertDialog.Builder createDialog(final String file_path) {
		final String imageUrl = getImageUrl(file_path);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(com.exercise.timers.R.string.save_image_title);
		dialog.setMessage(com.exercise.timers.R.string.save_image_message);
		dialog.setPositiveButton(com.exercise.timers.R.string.raw_save, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface di, int which) {
				if (! isRegistered(imageUrl)) {
					saveImage(file_path, which, false);
					insertImageInfoToDB(imageUrl);
				} else {
					saveImage(file_path, which, true);
				}
				intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
			}
		});
		dialog.setNeutralButton(com.exercise.timers.R.string.compress_save, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (! isRegistered(imageUrl)) {
					saveImage(file_path, which, false);
					insertImageInfoToDB(imageUrl);
				} else {
					saveImage(file_path, which, true);
				}
				intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
			}
		});
		dialog.setNegativeButton(android.R.string.cancel, null);
		dialog.create();
		return dialog;
	}
	
	/**
	 * ����`�̃t�H�[�}�b�g�A�t�@�C�����Ȃǂ̓s���ŕ\���ł��Ȃ������摜��ۑ����悤�Ƃ������ɏo��
	 * �G���[�p�̃_�C�A���O��\������B
	 */
	private void showErrorDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(com.exercise.timers.R.string.failed_save_image_title);
		dialog.setMessage(com.exercise.timers.R.string.failed_save_image_message);
		dialog.setPositiveButton(android.R.string.ok, null);
		dialog.create();
		dialog.show();
	}
	
	/**
	 * �V�K�o�^����ہA�f�[�^�x�[�X�ɓo�^���郁�\�b�h�B
	 * ���[�J���̃t�@�C�����ƒ[�����̃t�@�C��URL��ݒ肷��B
	 * @param imageUrl
	 */
	private void insertImageInfoToDB(String imageUrl) {
		String file_name = new File(imageUrl).getName();
		TimersDBHelper helper = new TimersDBHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("local_file_name", file_name);
		values.put("org_file_path", imageUrl);
		db.insert(TimersDBHelper.getTblImages(), "0", values);
		values.clear();
		db.close();
		helper.close();
	}
	
	/**
	 * file_path�̉摜���A�v���ɕۑ�����B
	 * �摜�̓��[�J���t�@�C���Ƃ��ĕێ�����B
	 * BUTTON_NEUTRAL�������ꂽ���́A960px������l�Ƃ��ďk�����Ď�荞�ށB
	 * �������A�v�Z�������ł͂Ȃ��̂ŁA���ӂ�960�Ŋ����Ďl�̌ܓ�����0�ɂȂ鎞�́A960px�𒴂��ĂĂ��k�����Ȃ��B
	 * @param file_path
	 * @param button
	 * @param overwrite
	 */
	private void saveImage(String file_path, int button, boolean overwrite) {
		BitmapFactory.Options imageOptions = new BitmapFactory.Options();
		imageOptions.inSampleSize = 1;
		
		if (button == DialogInterface.BUTTON_NEUTRAL) {
			imageOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file_path, imageOptions);
			float longer_length = Math.max(imageOptions.outWidth, imageOptions.outHeight);
			int scale = Math.round(longer_length / MAX_LENGTH);
			if (scale <= 0) {
				scale = 1;
			}
			imageOptions.inSampleSize = scale;
			Log.d("LA", "scale: " + scale);
			Log.d("LA", "width: " + imageOptions.outWidth + " -> " + (double)imageOptions.outWidth / scale);
			Log.d("LA", "height: " + imageOptions.outHeight + " -> " + (double)imageOptions.outHeight / scale);
		}
		
		imageOptions.inJustDecodeBounds = false;
		_saveImage(file_path, imageOptions, overwrite);
	}
	
	/**
	 * ���ۂɃ��[�J���t�@�C���Ƃ��ĕۑ�����
	 * @param file_path
	 * @param options
	 * @param overwrite
	 */
	private void _saveImage(String file_path, BitmapFactory.Options options, boolean overwrite) {
		if (overwrite) {
			context.deleteFile(getImageName(file_path));
		}
		InputStream in = null;
		FileOutputStream out = null;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(file_path, options);
			out = context.openFileOutput(getImageName(file_path), Context.MODE_PRIVATE);
			if (bitmap != null && out != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} else {
				showErrorDialog();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
			if (bitmap != null) {
				try {
					bitmap.recycle();
				} catch (Exception e){}
			}
		}
	}
	
	/**
	 * ���ɃA�v���ɕۑ��ς݂��ǂ������A�f�[�^�x�[�X�ɓo�^����Ă��邩�ǂ����Œ��ׂ�B
	 * @param imageUrl
	 * @return
	 */
	private boolean isRegistered(String imageUrl) {
		boolean result = true;
		
		TimersDBHelper helper = new TimersDBHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(
				TimersDBHelper.getTblImages()
				, new String[] {"org_file_path"}
				, "org_file_path = ?"
				, new String[] {imageUrl}
				, null, null, null);
		if (c.getCount() == 0) {
			result = false;
		}
		c.close();
		db.close();
		helper.close();
		
		return result;
	}
	
	/**
	 * �ۑ��\���ǂ����`�F�b�N����B
	 * Bitmap�Ńf�R�[�h�ł��Ȃ����̂͑ΏۊO�Ƃ��Ă���B
	 * @param file_path
	 * @return
	 */
	private boolean canSaved(String file_path) {
		boolean result = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file_path);
		if (bitmap == null) {
			Log.d("LA", "cannot save image");
			result = false;
		} else {
			bitmap.recycle();
		}
		return result;
	}
	
	private String getImageUrl(String path) {
		return "file://" + path;
	}
	
	private String getImageName(String path) {
		return new File(path).getName();
	}
}
