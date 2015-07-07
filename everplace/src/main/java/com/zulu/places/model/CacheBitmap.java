package com.zulu.places.model;

import android.graphics.Bitmap;

public class CacheBitmap {
	private String key;
	private Bitmap bitmap;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	};

}
