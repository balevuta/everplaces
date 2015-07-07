package com.zulu.places.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.w3c.dom.Document;

import zulu.app.libraries.supertoasts.SuperActivityToast;
import zulu.app.libraries.supertoasts.SuperToast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zulu.places.R;
import com.zulu.places.layout.DirectionDetailsActivity;
import com.zulu.places.layout.toturial.ToturialActivity;
import com.zulu.places.model.CacheBitmap;
import com.zulu.places.service.DirectionService;
import com.zulu.places.widget.CustomAlertDialog;

public class GeneralUtils {
	
	public static final String MY_AD_UNIT_ID = "ca-app-pub-5560218498190649/9641186810";
	
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	/**
	 * 
	 * @param context
	 * @param text
	 */
	public static void showShortToast(Context context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		SuperActivityToast toast = new SuperActivityToast((Activity)context, SuperToast.Type.STANDARD);
		toast.setAnimations(SuperToast.Animations.SCALE);
		toast.setBackground(SuperToast.Background.BLUE);
		toast.setTextSize(SuperToast.TextSize.MEDIUM);
		toast.setDuration(SuperToast.Duration.SHORT);
		toast.setText(text);
		toast.show();
	}
	
	/**
	 * 
	 * @param context
	 * @param text
	 */
	public static void showLongToast(Context context, String text) {
//		Toast.makeText(ZuluApplication.getContext(), text, Toast.LENGTH_LONG).show();
		SuperActivityToast toast = new SuperActivityToast((Activity)context, SuperToast.Type.STANDARD);
		toast.setAnimations(SuperToast.Animations.SCALE);
		toast.setBackground(SuperToast.Background.BLUE);
		toast.setTextSize(SuperToast.TextSize.MEDIUM);
		toast.setDuration(SuperToast.Duration.LONG);
		toast.setText(text);
		toast.show();
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String convertToDisplayDistance(Context context, float value) {
		String result = "";
		float kmValue = 0;
		DecimalFormat df = new DecimalFormat();
		if (value > 1000) {
			kmValue = value/1000;
			df.setMaximumFractionDigits(1);
			result = String.valueOf(df.format(kmValue)) + " km";
		} else {
			df.setMaximumFractionDigits(0);
			result = String.valueOf(df.format(value)) + " m";
		}
		return result;
	}
	
	/**
	 * Converting objects to byte arrays
	 */
	static public byte[] object2Bytes(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		return baos.toByteArray();
	}
	
	/**
	 * Converting byte arrays to objects
	 */
	static public Object bytes2Object(byte raw[]) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(raw);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		return o;
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEncoded(String text){
	    Charset charset = Charset.forName("US-ASCII");
	    String checked=new String(text.getBytes(charset),charset);
	    return !checked.equals(text);
	}
	
	public static Bitmap downloadImage(String url) {
		Bitmap bitmap = null;
		InputStream stream = null;
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;

		try {
			stream = getHttpConnection(url);
			bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public static CacheBitmap downloadImageWithCache(Context context, String url) {
		Bitmap bitmap = null;
		InputStream stream = null;
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		CacheBitmap tmp = new CacheBitmap();

		try {
			stream = getHttpConnection(url);
			if(stream!=null) {
				bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
				tmp.setKey(url);
				tmp.setBitmap(bitmap);
			} else {
				Bitmap defaultLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_not_found);
				tmp.setKey(url);
				tmp.setBitmap(defaultLogo);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(stream!=null)
					stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tmp;
	}
	
	public static InputStream getHttpConnection(String urlString)
			throws IOException {
		InputStream stream = null;
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				stream = httpConnection.getInputStream();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return stream;
	}
	
	/**
	 * 
	 * @param mContext
	 * @param fromPosition
	 * @param toPosition
	 * @param mMap
	 * @param mode
	 */
	public static void drawDirection(final Context mContext, final LatLng fromPosition, final LatLng toPosition, 
			final GoogleMap mMap, final String mode) {

		new AsyncTask<String, Void, Document>() {
			
			ProgressDialog dialog;
			DirectionService md = new DirectionService();
			Document doc;
			
			@Override
			protected Document doInBackground(String... params) {
				doc = md.getDocument(fromPosition, toPosition, mode);
				return doc;
			}

			@Override
			protected void onPostExecute(Document result) {
				super.onPostExecute(result);
				
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				
				ArrayList<LatLng> directionPoint = md.getDirection(result);
				PolylineOptions rectLine = new PolylineOptions().width(10).color(
						mContext.getResources().getColor(R.color.DarkGreen));

				for (int i = 0; i < directionPoint.size(); i++) {
					rectLine.add(directionPoint.get(i));
				}

//				if(line!=null) {
//					line.remove();
//				}
				mMap.addPolyline(rectLine);
				LatLngBounds bounds = new LatLngBounds(md.getSouthWestBound(doc), md.getNorthEastBound(doc));
				mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = new ProgressDialog(mContext);
				dialog.setCancelable(false);
				dialog.setMessage(mContext.getResources().getString(R.string.text_searching));
				dialog.isIndeterminate();
				dialog.show();
			}
			
		}.execute();
		
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startExpandFromCenterAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0.5f, 1f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startCollapseFromCenterAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 2f, 1f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startCollapseFromBottomAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 1f, 0f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startCollapseFromBottomHalfAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 2f, 1f, 
				ScaleAnimation.RELATIVE_TO_SELF, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, 0f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startCollapseFromTopAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0.7f, 0f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 1f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startExpandFromBottomAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 1f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startExpandFromTopAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * 
	 * @param view
	 */
	public static void startExpandFromTopHalfAnimation(View view) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0.5f, 1f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0f);
		scaleAnimation.setDuration(600);
		view.startAnimation(scaleAnimation);
	}
	
	/**
	 * set background for other side menu button
	 * @param color
	 * @param params
	 */
	public static void setSideButtonBackground(int background, View... params) {

		for (View view : params) {
			if (view == null) {
				continue;
			}
			if (view instanceof LinearLayout) {
				((LinearLayout) view).setBackgroundResource(background);
			}
		}
	}
	
	/**
	 * set background for other side menu button
	 * @param color
	 * @param params
	 */
	public static void setVisibility(int visibility, View... params) {

		for (View view : params) {
			if (view == null) {
				continue;
			}
			if (view instanceof ImageView) {
				((ImageView) view).setVisibility(visibility);
			}
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static Boolean isMobileAvailable(Context context) {       
	    TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    if(tel.getPhoneType()!=TelephonyManager.PHONE_TYPE_NONE) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	/**
	 * start activity animation
	 * 
	 * @param activity
	 */
	public static void beginStartActivityAnimation(Context context) {
		((Activity) context).overridePendingTransition(R.anim.move_right_in, R.anim.zoom_out_half);
	}

	/**
	 * finish activity animation
	 * 
	 * @param activity
	 */
	public static void beginFinishActivityAnimation(Context context) {
		((Activity) context).overridePendingTransition(R.anim.zoom_in_half, R.anim.move_left_out);
	}
	
	/**
	 * 
	 * @param context
	 * @param myClass
	 */
	public static void gotoActivityWithAnim(Context context, Intent intent) {
		context.startActivity(intent);
		beginStartActivityAnimation(context);
	}

	/**
	 * 
	 * @param context
	 */
	public static void finishActivityWithAnim(Context context) {
		((Activity) context).finish();
		beginFinishActivityAnimation(context);
	}
	
	/**
	 * show direction option dialog
	 */
	public static void showDirectionOpionDialog(final Context mContext, final String placeName,
			final String placeAddress, final double lat, final double lng, final String placeCategory) {
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View customLayout = inflater.inflate(R.layout.dialog_direction_option, null);
		final CustomAlertDialog optionDg = new CustomAlertDialog(mContext, customLayout);
		optionDg.setTitle(mContext.getResources().getString(R.string.title_direction_option));
		
		LinearLayout btnWrap = (LinearLayout) customLayout.findViewById(R.id.btn_wrap_position);
		final LinearLayout btnDriving = (LinearLayout) customLayout.findViewById(R.id.btn_mode_driving);
		final LinearLayout btnWalking = (LinearLayout) customLayout.findViewById(R.id.btn_mode_walking);
		final TextView txtDriving = (TextView) customLayout.findViewById(R.id.txt_mode_driving);
		final TextView txtWalking = (TextView) customLayout.findViewById(R.id.txt_mode_walking);
		final TextView edtStart = (TextView) customLayout.findViewById(R.id.edt_start_location);
		final TextView edtEnd = (TextView) customLayout.findViewById(R.id.edt_end_location);
		
		edtStart.setText(mContext.getResources().getString(R.string.text_current_location));
		edtEnd.setText(placeName);
		
		// set default is driving
		btnDriving.setBackgroundResource(R.drawable.shape_detail_button_selected);
		txtDriving.setTextColor(mContext.getResources().getColor(R.color.White));
		btnWalking.setBackgroundResource(R.drawable.shape_detail_button);
		txtWalking.setTextColor(mContext.getResources().getColor(R.color.TextColor));
		Constants.DirectionDetails.MODE_SAVED = DirectionService.MODE_DRIVING;
		
		btnDriving.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnDriving.setBackgroundResource(R.drawable.shape_detail_button_selected);
				txtDriving.setTextColor(mContext.getResources().getColor(R.color.White));
				btnWalking.setBackgroundResource(R.drawable.shape_detail_button);
				txtWalking.setTextColor(mContext.getResources().getColor(R.color.TextColor));
				Constants.DirectionDetails.MODE_SAVED = DirectionService.MODE_DRIVING;
			}
		});
		
		btnWalking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnWalking.setBackgroundResource(R.drawable.shape_detail_button_selected);
				txtWalking.setTextColor(mContext.getResources().getColor(R.color.White));
				btnDriving.setBackgroundResource(R.drawable.shape_detail_button);
				txtDriving.setTextColor(mContext.getResources().getColor(R.color.TextColor));
				Constants.DirectionDetails.MODE_SAVED = DirectionService.MODE_WALKING;
			}
		});
		
		btnWrap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchPositionEditText(edtStart, edtEnd);
				if(edtStart.getText().toString().equals(mContext.getResources().getString(R.string.text_current_location))) {
					Constants.DirectionDetails.SWAP_SAVED = false;
				} else {
					Constants.DirectionDetails.SWAP_SAVED = true;
				}
			}
		});
		
		optionDg.setLeftButton(mContext.getResources().getString(R.string.btn_search), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DirectionDetailsActivity.class);
				intent.putExtra(Constants.DirectionDetails.DIRECTION_SWAP, Constants.DirectionDetails.SWAP_SAVED);
				intent.putExtra(Constants.DirectionDetails.DIRECTION_LAT, lat);
				intent.putExtra(Constants.DirectionDetails.DIRECTION_LNG, lng);
				intent.putExtra(Constants.DirectionDetails.DIRECTION_MODE, Constants.DirectionDetails.MODE_SAVED);
				intent.putExtra(Constants.DirectionDetails.DIRECTION_PLACE_NAME, placeName);
				intent.putExtra(Constants.DirectionDetails.DIRECTION_PLACE_ADDRESS, placeAddress);
				intent.putExtra(Constants.DirectionDetails.DIRECTION_PLACE_TYPE, placeCategory);
				mContext.startActivity(intent);
				
				optionDg.hideDialog();
				((Activity) mContext).overridePendingTransition(R.anim.move_right_in, R.anim.zoom_out_half);
			}
		});
		optionDg.setRightButton(mContext.getResources().getString(R.string.btn_cancel), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				optionDg.hideDialog();
			}
		});
		optionDg.showDialog();
	}
	
	public static void switchPositionEditText(TextView edtStart, TextView edtEnd) {
		String end = edtEnd.getText().toString();
		String start = edtStart.getText().toString();
		edtStart.setText(end);
		edtEnd.setText(start);
	}
	
	public static void setViewApprearAnim(View view, Context mContext) {
		Animation startAnim = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
		view.startAnimation(startAnim);
	}
	
	public static void startHelpActivity(Context mContext) {
		Intent intent = new Intent(mContext, ToturialActivity.class);
		GeneralUtils.gotoActivityWithAnim(mContext, intent);
	}
	
	/**
	 * get version code from AndroidManifest
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * get version name from AndroidManifest
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static AdView addAdMod(Activity act, ViewGroup group) {
//		if (ReferenceControl.getPurchase(act)) {
//			((ViewGroup) group.getParent()).setVisibility(View.GONE);
//			return null;
//		}

		if (group == null) {
			return null;
		}
		if (!ConnectionUtils.isConnectInternet(act) 
				|| !act.getResources().getBoolean(R.bool.is_show_ads)) {
			((ViewGroup) group.getParent()).setVisibility(View.GONE);
			return null;
		}

		group.removeAllViews();

		// Create the adView
		AdView adView = new AdView(act);
	    adView.setAdSize(AdSize.SMART_BANNER);
	    adView.setAdUnitId(MY_AD_UNIT_ID);

		ViewGroup.LayoutParams param = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		adView.setLayoutParams(param);

		group.addView(adView);

	    // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder()
	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .addTestDevice("DEBBA2F2463F77BCAB80F2045DD31AC6")
	        .addTestDevice("9AD66FF6B0953B6570B9C41D9C8C0913")
	        .addTestDevice("4D2F0CF04A262AAA1839313443929F52")
	        .addTestDevice("2B3529A848975DFABD00508171CB0CC0")

	        .build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

	    return adView;
	}
	
	/**
	 * check if google play service avaiable or not
	 * @return
	 */
	public static boolean checkPlayServices(Activity activity) {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            
	        }
	        return false;
	    } 
	    return true;
	}
	
	/**
	 * 
	 * @param context
	 * @param phone
	 */
	public static void doCallPhoneClick(Context context, String phone) {
		if(GeneralUtils.isMobileAvailable(context)) {
			String uri = "tel:" + phone.trim() ;
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse(uri));
			context.startActivity(intent);
		} else {
			GeneralUtils.showShortToast(context, context.getResources().getString(R.string.msg_telephony_not_avaiable));
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param email
	 * @param subject
	 * @param body
	 */
	public static void doEmailClick(Context context, String email) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//		i.putExtra(Intent.EXTRA_SUBJECT, subject);
//		i.putExtra(Intent.EXTRA_TEXT   , body);
		i.setType("message/rfc822");
		try {
			context.startActivity(Intent.createChooser(i, context.getResources().getString(R.string.title_choose_email)));
		} catch (android.content.ActivityNotFoundException ex) {
		    GeneralUtils.showShortToast(context, context.getString(R.string.msg_no_email_app));
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param email
	 * @param subject
	 * @param body
	 */
	public static void doWebsiteClick(Context context, String website) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(website));
		try {
			context.startActivity(i);
		} catch (Exception e) {
			GeneralUtils.showShortToast(context, context.getString(R.string.msg_url_fail));
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param email
	 * @param subject
	 * @param body
	 */
	public static void doOpenContactClick(Context context, long contactId) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
	    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
	    intent.setData(uri);
	    context.startActivity(intent);
	}
	
	/**
	 * 
	 * @param context
	 * @param shareSubject
	 * @param shareBody
	 */
	public static void doChatClick(Context context) {
	    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.title_choose_email)));
	}
	
	/**
	 * 
	 * @param context
	 * @param name
	 * @param phone
	 * @param email
	 * @param jobTitle
	 */
	public static void doAddContactClick(Context context, String name, String phone, String email, String jobTitle) {
		Intent addContactIntent = new Intent(ContactsContract.Intents.Insert.ACTION, ContactsContract.Contacts.CONTENT_URI);
		addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, name); // an example, there is other data available
		addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
		addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
		addContactIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, jobTitle);
		addContactIntent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
		
		context.startActivity(addContactIntent);
	}
	
	/**
	 * 
	 * @param context
	 */
	public static void doAddNewContactClick(Context context) {
		Intent addContactIntent = new Intent(ContactsContract.Intents.Insert.ACTION, ContactsContract.Contacts.CONTENT_URI);
		context.startActivity(addContactIntent);
	}
	
	public static void viewOnMap(Context mContext, String address) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
			                  Uri.parse(String.format("geo:0,0?q=%s", URLEncoder.encode(address, "UTF-8"))));
			mContext.startActivity(intent);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
	    	GeneralUtils.showShortToast(mContext, mContext.getString(R.string.msg_map_not_found));
	    }
	}
	
	public static void gotoMarket(Context context, String gotoPack) {
		Uri uri;
		try {
			uri = Uri.parse("market://details?id=" + gotoPack);
			context.startActivity(new Intent(Intent.ACTION_VIEW, uri)
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		} catch (Exception anfe) {
			try {
				uri = Uri
						.parse("http://play.google.com/store/apps/details?id="
								+ gotoPack);
				context.startActivity(new Intent(Intent.ACTION_VIEW, uri)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
