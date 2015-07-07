package com.zulu.places.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontSetting {

	private static FontSetting instance;

	public static FontSetting getInstance() {
		if (instance == null)
			instance = new FontSetting();
		return instance;
	}

	public Typeface getTypeface(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				Constants.AppSetting.FONT);
	}
	
	public Typeface getBoldTypeface(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				Constants.AppSetting.FONT_BOLD);
	}

}
