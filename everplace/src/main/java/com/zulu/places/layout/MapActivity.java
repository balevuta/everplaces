package com.zulu.places.layout;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zulu.places.R;
import com.zulu.places.abstracts.AbstractActivity;
import com.zulu.places.extras.MapWrapperLayout;
import com.zulu.places.model.UserSetting;
import com.zulu.places.service.GetPlacesTask;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;
import com.zulu.places.utils.PreferenceUtils;
import com.zulu.places.widget.CustomAlertDialog;

public class MapActivity extends AbstractActivity implements OnClickListener, OnTouchListener,
								ConnectionCallbacks, OnConnectionFailedListener {

	private final String TAG = getClass().getSimpleName();
	
	private GoogleMap mMap;
	private MapWrapperLayout mWrapperLayout;
	
//	private String[] places;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Location loc;

//	private LocationClient mLocationClient;
	private GoogleApiClient mGoogleClientApi;
	private Marker currMarker;
	
	// Image cache
//	public static CacheImage mCache;
//	private LruCache<String, Bitmap> mMemoryCache;

	// View of content
	private CheckBox cbxNavite;
	private LinearLayout mBtnZoomOut, mBtnZoomIn, mBoxZoom;
	
	private ButtonFloat mBtnMore, mBtnSearch, mBtnList;
	
	// View of search details
	private TextView mTvSearchDistance, mTvSearchKeyword;

	// Orientation sensor
	private float mOrientation;
	private SensorManager mSensorManager;
	private SensorEventListener mSensorListener;
	private float zoomIndex = 16f;
	private int zoomValueInt = 4;
	
	// GetPlacesTask GetDetailsTask
	private GetPlacesTask mGetPlacesTask;
	private int numberMoreRequest = 0;
	private MenuItem menuRequestItem;
	
	private String placeType;
	private String placeTitle;
	private String textSearch;
	
	// first toturial
	private RelativeLayout mBoxToturial;
	private final int DEPLAY_SHOW_TOTURIAL = 2000;
	private boolean isFirst;

	@Override
	protected void onResume() {
		// setupSensorManager();
		initLocationListener();
		initSensorListener();
		if(mGetPlacesTask!=null) {
			if(mGetPlacesTask.getListMarker()!=null && mGetPlacesTask.getListMarker().size()>=ListPlacesActivity.placesPos) {
				mGetPlacesTask.getListMarker().get(ListPlacesActivity.placesPos).showInfoWindow();
				mMap.moveCamera(CameraUpdateFactory.newLatLng(mGetPlacesTask.getListMarker().get(ListPlacesActivity.placesPos).getPosition()));
			}
		}
		
		IntentFilter intentFilter = new IntentFilter(MapActivity.class.getName());
		registerReceiver(mToturialReceiver, intentFilter);
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (locationListener != null)
			locationManager.removeUpdates(locationListener);
		if (mGoogleClientApi != null) {
			mGoogleClientApi.disconnect();
		}
		if (mSensorListener != null && mSensorManager != null) {
			mSensorManager.unregisterListener(mSensorListener);
		}
		unregisterReceiver(mToturialReceiver);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mSensorListener != null && mSensorManager != null) {
			mSensorManager.unregisterListener(mSensorListener);
		}
		mGoogleClientApi.disconnect();
		super.onDestroy();
	}
	
	/**
	 * 
	 */
	private BroadcastReceiver mToturialReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getStringExtra(Constants.ActionReceiver.ACTION_RECEIVER);
			if(action.equals(Constants.ActionReceiver.SHOW_TOTURIAL)) {
				checkShowFirstToturial();
			}
		}
	};
	
	/**
	 * check show first toturial for this screen
	 */
	private void checkShowFirstToturial() {
		isFirst = PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_MAIN_ACTIVITY, true);
		if(isFirst) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mBoxToturial.setVisibility(View.VISIBLE);
					Animation startAnim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
					mBoxToturial.startAnimation(startAnim);
				}
			}, DEPLAY_SHOW_TOTURIAL);
		}
	}

//	private void setUpLocationClientIfNeeded() {
//		if (mGoogleClientApi == null) {
//			mLocationClient = new LocationClient(mContext, this, // ConnectionCallbacks
//					this); // OnConnectionFailedListener
//		}
//	}
	
	private void setUpGoogleClientApi() {
		mGoogleClientApi = new GoogleApiClient.Builder(mContext)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)
			.addApi(LocationServices.API)
			.build();
		
		mGoogleClientApi.connect();
		
		Log.i(MapActivity.class.getSimpleName(), "connected begin call");
	}

	/**
	 * 
	 */
	private void moveToMyLocation() {

//		checkGPSStatus();

		if (mGoogleClientApi != null 
				&& LocationServices.FusedLocationApi.getLastLocation(mGoogleClientApi) != null) {
			
			loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleClientApi);
			
			LatLng coor = new LatLng(loc.getLatitude(), loc.getLongitude());

			if(UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION)
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor, 20));
			else 
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Constants.TargetMode.PQ_LAT, Constants.TargetMode.PQ_LNG), 20));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

			setupSensorManager();
		} else {
			showEnableWirelessGPSDialog();
		}
	}
	
	/**
	 * show dialog to enable gps
	 */
	private void showEnableWirelessGPSDialog() {
		final CustomAlertDialog dialog = new CustomAlertDialog(mContext, 
				getResources().getString(R.string.msg_open_wireless_gps));
		dialog.setLeftButton(getResources().getString(R.string.btn_ok), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		
		dialog.setRightButton(getResources().getString(R.string.btn_cancel), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.hideDialog();
			}
		});
		
		dialog.setTitle(getResources().getString(R.string.action_check_setting));
		
		dialog.showDialog();
	}


	/**
	 * 
	 */
	private void initFirstData() {
//		places = getResources().getStringArray(R.array.places);
		// currentLocation();

		setUpGoogleClientApi();

		mGoogleClientApi.connect();

		initViewAndAction();
		
		if(getIntent().getExtras()!=null) {
			if(getIntent().getStringExtra(Constants.MapScreenMode.MAP_MODE)
					.equals(Constants.MapScreenMode.MAP_MODE_TEXT)) {
				textSearch = getIntent().getStringExtra(Constants.MapScreenMode.TEXT_SEARCH);
			} else {
				placeType = getIntent().getStringExtra(Constants.PlaceTypes.PLACE_TYPE);
			}
			placeTitle = getIntent().getStringExtra(Constants.PlaceTypes.PLACE_TITLE);
		} else {
			placeType = Constants.PlaceTypes.TYPE_RESTAURANT;
			placeTitle = getResources().getString(R.string.home_restaurant);
		}
	}

	/**
	 * 
	 */
	private void initViewAndAction() {
		cbxNavite = (CheckBox) findViewById(R.id.cbx_navigate);
		cbxNavite.setOncheckListener(new OnCheckListener() {
			
			@Override
			public void onCheck(boolean isChecked) {
				if(mSensorManager!=null) {
					if (isChecked) {
						mSensorManager.registerListener(mSensorListener,
								mSensorManager
										.getDefaultSensor(Sensor.TYPE_ORIENTATION),
								SensorManager.SENSOR_DELAY_UI);
						mBoxZoom.setVisibility(View.VISIBLE);
					} else {
						mSensorManager.unregisterListener(mSensorListener);
						mBoxZoom.setVisibility(View.GONE);
					}
				} else {
					GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_connect_fail));
				}
			}
		});
	}

	/**
	 * 
	 */
	private void initLocationListener() {
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// Toast.makeText(getApplicationContext(),
				// location.getLatitude() + " " + location.getLongitude(),
				// Toast.LENGTH_LONG).show();
				if(currMarker!=null) {
					currMarker.remove();
				}
				currMarker = mMap.addMarker(new MarkerOptions()
						.position(
								new LatLng(location.getLatitude(), location
										.getLongitude()))
						.title("my position")
//						.rotation(mOrientation)
//						.icon(BitmapDescriptorFactory
//								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
						.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.navigation_icon)));

//				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//						new LatLng(location.getLatitude(), location
//								.getLongitude()), 15.0f));
				Log.i("Location", "location: " + location.getLatitude() + ":"
						+ location.getLongitude());

			}

			@Override
			public void onProviderDisabled(String arg0) {
			}

			@Override
			public void onProviderEnabled(String arg0) {
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}

		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
	}

	/**
	 * 
	 */
	private void initActionBar() {
		final ActionBar actionBar = getActionBar();
		
//		Drawable myBg = getResources().getDrawable(R.drawable.action_bar);
//		actionBar.setBackgroundDrawable(myBg);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
//		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(placeTitle);
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		removeFirstToturial();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(getIntent().getStringExtra(Constants.MapScreenMode.MAP_MODE).equals(Constants.MapScreenMode.MAP_MODE_PLACE)){
			if(ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
				getMenuInflater().inflate(R.menu.map_menu_place_icon, menu);
			} else {
				getMenuInflater().inflate(R.menu.map_menu_place, menu);
			}
		} else {
			if(ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
				getMenuInflater().inflate(R.menu.map_menu_search_icon, menu);
			} else {
				getMenuInflater().inflate(R.menu.map_menu_search, menu);
			}
		}
		menuRequestItem = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			GeneralUtils.finishActivityWithAnim(mContext);
			break;
		case R.id.help:
			GeneralUtils.startHelpActivity(mContext);
			break;
		
		case R.id.action_filter:
			showFilterDialog();
			break;

//		case R.id.action_settings:
//			Intent i = new Intent(this, SettingsActivity.class);
//			startActivityForResult(i, 1);
//			overridePendingTransition(R.anim.move_right_in,
//					R.anim.zoom_out_half);
//			break;
			
//		case R.id.action_remove_line:
//			if(mGetPlacesTask!=null && mGetPlacesTask.getLastPolyline()!=null)
//				mGetPlacesTask.getLastPolyline().remove();
//			break;
			
		case R.id.action_show_list:
			showListPlacesActivity();
			break;
			
		case R.id.action_download_more:
			if(loc!=null && mGetPlacesTask!=null 
					&& mGetPlacesTask.getPlaceService().getNextPageToken()!=null) {
				if(!mGetPlacesTask.getPlaceService().getNextPageToken().equals("") && numberMoreRequest < 3) {
					mGetPlacesTask.processGetMoreResults();
					numberMoreRequest+=1;
				}
				if(numberMoreRequest==3 || mGetPlacesTask.getPlaceService().getNextPageToken().equals("")) {
					menuRequestItem.setEnabled(false);
					GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_no_results_more));
				}
			} else {
				GeneralUtils.showShortToast(mContext, mContext.getResources().getString(R.string.msg_connect_fail));
			}
			break;

		default:
			break;
		}
		removeFirstToturial();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		GeneralUtils.showShortToast(mContext,
				getResources().getString(R.string.msg_connect_fail));
	}
	
	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle arg0) {
		moveToMyLocation();
		if(getIntent().getStringExtra(Constants.MapScreenMode.MAP_MODE).equals(Constants.MapScreenMode.MAP_MODE_PLACE)) {
			searchSinglePlace();
		} else {
			searchWithText();
		}
	}

	/**
	 * 
	 */
	private void showListPlacesActivity() {
		if(mGetPlacesTask!=null && mGetPlacesTask.getListPlacesResult()!= null) {
			Intent intent = new Intent(mContext, ListPlacesActivity.class);
			
//			String[] arrDistance = new String[mGetPlacesTask.getListPlacesResult().size()];
			
			ArrayList<String> lstDistance = new ArrayList<String>();
			
			for(int i=0;i<mGetPlacesTask.getListPlacesResult().size();i++) {
				float[] disArr = new float[3];
				Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), 
						mGetPlacesTask.getListPlacesResult().get(i).getLatitude(), 
						mGetPlacesTask.getListPlacesResult().get(i).getLongitude(), disArr);
//				arrDistance[i] = GeneralUtils.convertToDisplayDistance(mContext, disArr[0]);
				lstDistance.add(GeneralUtils.convertToDisplayDistance(mContext, disArr[0]));
			}
			
//			intent.putExtra(Constants.PlaceDetails.LIST_DISTANCE, arrDistance);
			intent.putStringArrayListExtra(Constants.PlaceDetails.LIST_DISTANCE, lstDistance);
			
			try {
				intent.putExtra(
						Constants.PlaceDetails.LIST_PLACES,
						GeneralUtils.object2Bytes(mGetPlacesTask.getListPlacesResult()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			GeneralUtils.gotoActivityWithAnim(mContext, intent);
		} else {
			GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_connect_fail));
		}
		
	}

	/**
	 * khoi tao cam bien dieu huong
	 */
	private void initSensorListener() {
		mSensorListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				mOrientation = event.values[0];
				Log.d(TAG, "Phone Moved " + mOrientation);
				// draw(mOrientation);
				// Toast.makeText(mContext, mOrientation + " ",
				// Toast.LENGTH_SHORT)
				// .show();
				updateCamera(mOrientation);

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
	}

	/**
	 * 
	 * @param bearing
	 */
	public void updateCamera(float bearing) {
		CameraPosition currentPlace = new CameraPosition.Builder()
				.target(new LatLng(loc.getLatitude(), loc.getLongitude()))
				.bearing(bearing).tilt(30).zoom(zoomIndex).build();
		mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
	}

	/**
	 * 
	 */
	private void setupSensorManager() {
		mSensorManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		// mSensorManager.registerListener(mSensorListener,
		// mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
		// SensorManager.SENSOR_DELAY_UI);

		Log.d(TAG, "SensorManager setup");
	}

	/**
	 * 
	 */
	private void showFilterDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View customLayout = inflater.inflate(R.layout.dialog_filter, null);
		
		final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		final EditText edtRadius = (EditText)customLayout.findViewById(R.id.edt_radius);
		RadioGroup groupMethod = (RadioGroup)customLayout.findViewById(R.id.group_search_method);
		
		groupMethod.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.radioDistance) {
					edtRadius.setEnabled(false);
				} else {
					edtRadius.setEnabled(true);
					edtRadius.requestFocus();
					imm.showSoftInput(edtRadius, InputMethodManager.SHOW_IMPLICIT);
				}
			}
		});
		
		CheckBox cbxKeyword = (CheckBox)customLayout.findViewById(R.id.cbx_with_keyword);
		
		final EditText edtKeyword = (EditText)customLayout.findViewById(R.id.edt_keyword);
		
		cbxKeyword.setOncheckListener(new OnCheckListener() {
			
			@Override
			public void onCheck(boolean isChecked) {
				if(isChecked) {
					edtKeyword.setEnabled(true);
					edtKeyword.requestFocus();
					imm.showSoftInput(edtKeyword, InputMethodManager.SHOW_IMPLICIT);
				} else {
					edtKeyword.setEnabled(false);
				}
			}
		});
		
//		cbxKeyword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(isChecked) {
//					edtKeyword.setEnabled(true);
//					edtKeyword.requestFocus();
//					imm.showSoftInput(edtKeyword, InputMethodManager.SHOW_IMPLICIT);
//				} else {
//					edtKeyword.setEnabled(false);
//				}
//			}
//		});
		
		final CustomAlertDialog filterDg = new CustomAlertDialog(mContext, customLayout);
		filterDg.setTitle(mContext.getResources().getString(R.string.title_search_filter));
		filterDg.setLeftButton(getResources().getString(R.string.btn_search), new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(mGetPlacesTask!=null) {
					
					if(!edtRadius.isEnabled() && !edtKeyword.isEnabled()) {
						searchSinglePlace();
						filterDg.hideDialog();
					} else {
						String keyword = edtKeyword.getText().toString().replaceAll("\\s","");
						String radius = edtRadius.getText().toString().replaceAll("\\s","");
						
						if(edtKeyword.isEnabled() && !edtRadius.isEnabled()) {
							if(keyword.equals("")) {
								GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_fill_keyword));
							} else {
								searchFilterKeyword(keyword, filterDg);
							}
						} else if(!edtKeyword.isEnabled() && edtRadius.isEnabled()) {
							if(radius.equals("")) {
								GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_fill_radius));
							} else {
								int radiusValue = Integer.valueOf(radius);
								if(radiusValue > 50) {
									GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_radius_less_50));
								} else {
									radiusValue *= 1000; 
									searchFilterRadius(radiusValue, filterDg);
								}
							}
						} else if(edtKeyword.isEnabled() && edtRadius.isEnabled()) {
							if(radius.equals("") || keyword.equals("")) {
								GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_fill_all_fields));
							} else {
								int radiusValue = Integer.valueOf(radius);
								if(radiusValue > 50) {
									GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_radius_less_50));
								} else {
									radiusValue *= 1000;
									searchFiterKeywordRadius(keyword, radiusValue, filterDg);
								}
							}
						}
					}
					
					numberMoreRequest = 0;
					menuRequestItem.setEnabled(true);
					
				} else {
					GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_connect_fail));
				}
			}
		});
		filterDg.setRightButton(getResources().getString(R.string.btn_cancel), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				filterDg.hideDialog();
			}
		});
		filterDg.showDialog();
	}
	
	/**
	 * 
	 * @param itemPosition
	 */
	private void searchSinglePlace() {
		mMap.clear();
		mGetPlacesTask = new GetPlacesTask(MapActivity.this,
				placeType.toLowerCase().replace("-", "_")
						.replace(" ", "_"), mMap, loc, mWrapperLayout, false);
		mGetPlacesTask.execute();
		
		mTvSearchDistance.setText(getResources().getString(R.string.text_main_search_nearby));
		mTvSearchKeyword.setVisibility(View.GONE);
	}
	
	/**
	 * 
	 * @param itemPosition
	 */
	private void searchWithText() {
		mMap.clear();
		mGetPlacesTask = new GetPlacesTask(MapActivity.this,
				textSearch.toLowerCase().replace("-", "_")
						.replace(" ", "_"), mMap, loc, mWrapperLayout, true);
		mGetPlacesTask.execute();
		
		mTvSearchDistance.setText(getResources().getString(R.string.text_main_search_nearby));
		mTvSearchKeyword.setVisibility(View.GONE);
	}
	
	/**
	 * 
	 * @param keyword
	 */
	private void searchFilterKeyword(String keyword, CustomAlertDialog filterDg) {
		if(!GeneralUtils.isEncoded(keyword)) {
			mMap.clear();
			mGetPlacesTask = new GetPlacesTask(MapActivity.this,
					placeType.toLowerCase().replace("-", "_")
							.replace(" ", "_"), mMap, loc, keyword, mWrapperLayout);
			mGetPlacesTask.execute();
			filterDg.hideDialog();
		} else {
			GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_keyword_encoded));
		}
		
		mTvSearchDistance.setText(getResources().getString(R.string.text_main_search_nearby));
		mTvSearchKeyword.setVisibility(View.VISIBLE);
		mTvSearchKeyword.setText(getResources().getString(R.string.text_main_search_keyword).replace("{0}", "'" + keyword + "'"));
	}
	
	/**
	 * 
	 * @param radius
	 */
	private void searchFilterRadius(int radius, CustomAlertDialog filterDg) {
		mMap.clear();
		mGetPlacesTask = new GetPlacesTask(MapActivity.this,
				placeType.toLowerCase().replace("-", "_")
						.replace(" ", "_"), mMap, loc, radius, mWrapperLayout);
		mGetPlacesTask.execute();
		filterDg.hideDialog();
		
		mTvSearchDistance.setText(getResources().getString(R.string.text_main_search_radius).replace("{0}", String.valueOf(radius/1000)));
		mTvSearchKeyword.setVisibility(View.GONE);
	}
	
	/**
	 * 
	 * @param keyword
	 * @param radius
	 */
	private void searchFiterKeywordRadius(String keyword, int radius, CustomAlertDialog filterDg) {
		if(!GeneralUtils.isEncoded(keyword)) {
			mMap.clear();
			mGetPlacesTask = new GetPlacesTask(MapActivity.this,
					placeType.toLowerCase().replace("-", "_")
							.replace(" ", "_"), mMap, loc, keyword, radius, mWrapperLayout);
			mGetPlacesTask.execute();
			filterDg.hideDialog();
		} else {
			GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_keyword_encoded));
		}
		
		mTvSearchDistance.setText(getResources().getString(R.string.text_main_search_radius).replace("{0}", String.valueOf(radius/1000)));
		mTvSearchKeyword.setVisibility(View.VISIBLE);
		mTvSearchKeyword.setText(getResources().getString(R.string.text_main_search_keyword).replace("{0}", "'" + keyword + "'"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_direction_zoom_out:
			zoomValueInt--;
			getZoomIndex(zoomValueInt);
			if(zoomValueInt==0) {
				mBtnZoomOut.setEnabled(false);
			} else if(zoomValueInt==7) {
				mBtnZoomIn.setEnabled(true);
			} 
			break;
			
		case R.id.btn_direction_zoom_in:
			zoomValueInt++;
			getZoomIndex(zoomValueInt);
			if(zoomValueInt==8) {
				mBtnZoomIn.setEnabled(false);
			} else if(zoomValueInt==1) {
				mBtnZoomOut.setEnabled(true);
			}
			break;
			
		case R.id.btn_fl_more:
			if(loc!=null && mGetPlacesTask!=null 
				&& mGetPlacesTask.getPlaceService().getNextPageToken()!=null) {
				if(!mGetPlacesTask.getPlaceService().getNextPageToken().equals("") && numberMoreRequest < 3) {
					mGetPlacesTask.processGetMoreResults();
					numberMoreRequest+=1;
				}
				if(numberMoreRequest==3 || mGetPlacesTask.getPlaceService().getNextPageToken().equals("")) {
					menuRequestItem.setEnabled(false);
					GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_no_results_more));
				}
			} else {
				GeneralUtils.showShortToast(mContext, mContext.getResources().getString(R.string.msg_connect_fail));
			}
			break;
		case R.id.btn_fl_search:
			showFilterDialog();
			break;
		case R.id.btn_fl_list:
			showListPlacesActivity();
			break;

		default:
			break;
		}
		
		removeFirstToturial();
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	private float getZoomIndex(int index) {
		switch (index) {
		case 0:
			zoomIndex = 12f;
			break;
		case 1:
			zoomIndex = 13f;
			break;
		case 2:
			zoomIndex = 14f;
			break;
		case 3:
			zoomIndex = 15f;
			break;
		case 4:
			zoomIndex = 16f;
			break;
		case 5:
			zoomIndex = 17f;
			break;
		case 6:
			zoomIndex = 18f;
			break;
		case 7:
			zoomIndex = 19f;
			break;
		case 8:
			zoomIndex = 20f;
			break;
		default:
			zoomIndex = 16f;
			break;
		}
		return zoomIndex;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.equals(mBoxToturial) && mBoxToturial.getVisibility() == View.VISIBLE) {
			removeFirstToturial();
		}
		return false;
	}
	
	private void removeFirstToturial() {
		if(PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_MAIN_ACTIVITY, true)) {
			PreferenceUtils.saveBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_MAIN_ACTIVITY, false);
			mBoxToturial.setVisibility(View.GONE);
			Animation startAnim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
			mBoxToturial.startAnimation(startAnim);
		}
	}

	@Override
	public int getContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.activity_main;
	}

	@Override
	public void initView() {
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		mWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
		
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		mTvSearchDistance = (TextView) findViewById(R.id.txt_main_search_distance);
		mTvSearchKeyword = (TextView) findViewById(R.id.txt_main_search_keyword);
		mTvSearchKeyword.setVisibility(View.GONE);
		
		mBtnZoomOut = (LinearLayout) findViewById(R.id.btn_direction_zoom_out);
		mBtnZoomIn = (LinearLayout) findViewById(R.id.btn_direction_zoom_in);
		mBtnZoomOut.setOnClickListener(this);
		mBtnZoomIn.setOnClickListener(this);
		
		mBtnMore = (ButtonFloat) findViewById(R.id.btn_fl_more);
		mBtnSearch = (ButtonFloat) findViewById(R.id.btn_fl_search);
		mBtnList = (ButtonFloat) findViewById(R.id.btn_fl_list);
		mBtnMore.setOnClickListener(this);
		mBtnSearch.setOnClickListener(this);
		mBtnList.setOnClickListener(this);
		
		mBoxZoom = (LinearLayout) findViewById(R.id.box_direction_zoom);
		mBoxZoom.setVisibility(View.GONE);
		
		mBoxToturial = (RelativeLayout) findViewById(R.id.box_toturial_main_results);
		mBoxToturial.setOnTouchListener(this);
		
		initFirstData();
		initActionBar();
		
		// Admob
		ViewGroup ad = (ViewGroup) findViewById(R.id.lnAdmob);
		GeneralUtils.addAdMod(this, ad);
	}

}
