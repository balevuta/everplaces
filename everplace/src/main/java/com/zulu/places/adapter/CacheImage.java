package com.zulu.places.adapter;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class CacheImage {

	private LruCache<String, Bitmap> mMemoryCache;
	public static final String DEFAULT_LOGO = "default_image";

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			getmMemoryCache().put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return getmMemoryCache().get(key);
	}

	public LruCache<String, Bitmap> getmMemoryCache() {
		return mMemoryCache;
	}

	public void setmMemoryCache(LruCache<String, Bitmap> mMemoryCache) {
		this.mMemoryCache = mMemoryCache;
	}

}
