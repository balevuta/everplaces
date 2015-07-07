package com.zulu.places.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zulu.places.R;
import com.zulu.places.model.UserSetting;

public class ConnectionUtils {

	/**
	 * 
	 * @param context
	 */
	public static void registerCheckStateInternetConnection(Context context) {
		String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
		IntentFilter filter = new IntentFilter(ACTION);
		context.registerReceiver(mConnectivityCheckReceiver, filter);
	}

	/**
	 * @param context
	 */
	public static void unregisterCheckStateInternetConnection(Context context) {
		context.unregisterReceiver(mConnectivityCheckReceiver);
	}

	/**
	 * mConnectivityCheckReceiver
	 */
	private static BroadcastReceiver mConnectivityCheckReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			boolean isNoConnectivity = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

			if (isNoConnectivity) {
				UserSetting.getInstance().isConnectInternet = false;
			} else {
				UserSetting.getInstance().isConnectInternet = true;
				if (UserSetting.getInstance().isLoggedIn()) {
					// Do...
				}
			}
		}
	};

	/**
	 * 
	 * @param context
	 */
	public static void checkConnectInternet(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conManager.getActiveNetworkInfo();
		if ((i == null) || (!i.isConnected()) || (!i.isAvailable())) {
			UserSetting.getInstance().isConnectInternet = false;
			GeneralUtils.showLongToast(context, context.getResources().getString(R.string.msg_no_internet_access));
		} else {
			UserSetting.getInstance().isConnectInternet = true;
		}
	}
	
	/**
	 * 
	 * @param context
	 */
	public static boolean isConnectInternet(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conManager.getActiveNetworkInfo();
		if ((i == null) || (!i.isConnected()) || (!i.isAvailable())) {
			return false;
		} else {
			return true;
		}
	}

}
