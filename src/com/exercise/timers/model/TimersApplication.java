package com.exercise.timers.model;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class TimersApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.memoryCache(new LruMemoryCache(2 * 512 * 512))
			.memoryCacheSize(2 * 512 * 512)
			.build();
		ImageLoader.getInstance().init(config);
	}
}
