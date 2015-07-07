package com.zulu.places.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

	private static final String PREF_NAME = "ZuluPref";

	public static void saveString(Context context, String key, String value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(Context context, String key, String defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		return settings.getString(key, defValue);
	}

	public static void saveLong(Context context, String key, long value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static long getLong(Context context, String key, long defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		return settings.getLong(key, defValue);
	}

	public static void saveInt(Context context, String key, int value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(Context context, String key, int defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		return settings.getInt(key, defValue);
	}

	public static void saveBoolean(Context context, String key, boolean value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */
				0);
		return settings.getBoolean(key, defValue);
	}
}
