
package com.exercise.timers.model;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

 import android.app.Application;

/**
 * ImageLoader‚ğg‚¤‚Ì‚É•K—v‚È‰Šú‰»ˆ—
 * @author keigo.nakmaura
 *
 */
public class TimersApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Builder options = new DisplayImageOptions.Builder()
			.showImageOnFail(com.exercise.timers.R.drawable.image_load_failed);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.memoryCache(new LruMemoryCache(2 * 512 * 512))
			.memoryCacheSize(2 * 512 * 512)
			.defaultDisplayImageOptions(options.build())
			.build();
		ImageLoader.getInstance().init(config);
	}
}
