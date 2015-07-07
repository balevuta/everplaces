package com.zulu.places.layout;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zulu.places.R;
import com.zulu.places.abstracts.AbstractActivity;
import com.zulu.places.service.DirectionService;
import com.zulu.places.service.GetPlacesTask;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;
import com.zulu.places.utils.PreferenceUtils;

public class DirectionDetailsActivity extends AbstractActivity implements 
				ConnectionCallbacks, OnConnectionFailedListener, 
				OnClickListener, OnTouchListener {
	private GoogleMap mMap;
	private TextView mTVPlaceName, mTVTimeDistance;
	private CheckBox mCbxNavigate;
	
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	
//	private LocationClient mLocationClient;
	private GoogleApiClient mGoogleClientApi;
	
	private SensorManager mSensorManager;
	private SensorEventListener mSensorListener;
	private float mOrientation;
	private float zoomIndex = 16f;
	private int zoomValueInt = 4;
	
	private Location loc;
	private double lat, lng;
	private boolean swap;
	private String mode, placeName, placeAddress, distance, duration, placeCategory;
	
	private MenuItem carItem, walkingItem;
	private LinearLayout mBtnZoomOut, mBtnZoomIn, mBoxZoom, mBtnCurrentToPlace, mBtnPlaceToCurrent;
	
	private LatLng targetLatlng;
	private Polyline line;
	
	private Marker currMarker;
	
	// first toturial
	private RelativeLayout mBoxTotorial;
	private boolean isFirst;
	private final int DEPLAY_SHOW_TOTURIAL = 1500;
	
	@Override
	protected void onResume() {
		
		initLocationListener();
		initSensorListener();
		
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
		removeFirstToturialView();
		super.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		if (mGoogleClientApi != null) {
			mGoogleClientApi.disconnect();
		}
		if (mLocationListener != null && mLocationManager!=null) {
			mLocationManager.removeUpdates(mLocationListener);
		}
		if (mSensorListener != null && mSensorManager != null) {
			mSensorManager.unregisterListener(mSensorListener);
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if (mGoogleClientApi != null) {
			mGoogleClientApi.disconnect();
		}
		if (mLocationListener != null && mLocationManager!=null) {
			mLocationManager.removeUpdates(mLocationListener);
		}
		if (mSensorListener != null && mSensorManager != null) {
			mSensorManager.unregisterListener(mSensorListener);
		}
		super.onDestroy();
	}
	
//	private void setUpLocationClientIfNeeded() {
//		if (mLocationClient == null) {
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
	 * check show first toturial for this screen
	 */
	private void checkShowFirstToturial() {
		isFirst = PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_DIRECTION, true);
		if(isFirst) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mBoxTotorial.setVisibility(View.VISIBLE);
					Animation startAnim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
					mBoxTotorial.startAnimation(startAnim);
				}
			}, DEPLAY_SHOW_TOTURIAL);
		}
	}
	
	/**
	 * remove first toturial view
	 */
	private void removeFirstToturialView() {
		if(PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_DIRECTION, true)) {
			PreferenceUtils.saveBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_DIRECTION, false);
			mBoxTotorial.setVisibility(View.GONE);
			Animation startAnim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
			mBoxTotorial.startAnimation(startAnim);
		}
	}
	
	/**
	 * 
	 */
	private void moveToMyLocation() {

		checkGPSStatus();

		// myLat = mLocationClient.getLastLocation().getLatitude();
		// myLng = mLocationClient.getLastLocation().getLongitude();

		if (mGoogleClientApi != null
				&& LocationServices.FusedLocationApi.getLastLocation(mGoogleClientApi) != null) {
			
			loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleClientApi);
			
			LatLng coor = new LatLng(loc.getLatitude(), loc.getLongitude());

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coor, 20));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

			setupSensorManager();
			
			checkAndShowDirection();
			
		} else {
			GeneralUtils.showLongToast(mContext, getResources().getString(R.string.msg_restart_app));
		}
	}
	
	/**
	 * 
	 */
	private void checkAndShowDirection() {
		if(!swap) {
			showDirectionAndDetails(new LatLng(loc.getLatitude(), loc.getLongitude()), targetLatlng, mode);
		} else {
			showDirectionAndDetails(targetLatlng, new LatLng(loc.getLatitude(), loc.getLongitude()), mode);
		}
	}
	
	/**
	 * 
	 */
	private void checkGPSStatus() {
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			GeneralUtils.showShortToast(mContext,
					getResources().getString(R.string.msg_turn_gps_on));
		}
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

		Log.d("Direction Details", "SensorManager setup");
	}
	
	/**
	 * 
	 */
	private void initLocationListener() {
		mLocationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
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
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, mLocationListener);
	}
	
	/**
	 * khoi tao cam bien dieu huong
	 */
	private void initSensorListener() {
		mSensorListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				mOrientation = event.values[0];
				Log.d("Direction Details", "Phone Moved " + mOrientation);
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
	private void updateCamera(float bearing) {
		CameraPosition currentPlace = new CameraPosition.Builder()
				.target(new LatLng(loc.getLatitude(), loc.getLongitude()))
				.bearing(bearing).tilt(30).zoom(zoomIndex).build();
		mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
	}
	
	/**
	 * 
	 */
	private void initFirstData() {
		
		setUpGoogleClientApi();
		
		lat = getIntent().getDoubleExtra(Constants.DirectionDetails.DIRECTION_LAT, 0);
		lng = getIntent().getDoubleExtra(Constants.DirectionDetails.DIRECTION_LNG, 0);
		swap = getIntent().getBooleanExtra(Constants.DirectionDetails.DIRECTION_SWAP, false);
		mode = getIntent().getStringExtra(Constants.DirectionDetails.DIRECTION_MODE);
		placeName = getIntent().getStringExtra(Constants.DirectionDetails.DIRECTION_PLACE_NAME);
		placeAddress = getIntent().getStringExtra(Constants.DirectionDetails.DIRECTION_PLACE_ADDRESS);
		placeCategory = getIntent().getStringExtra(Constants.DirectionDetails.DIRECTION_PLACE_TYPE);
		
		mTVPlaceName.setText(placeName);
		
		targetLatlng = new LatLng(lat, lng);
		
		Marker marker = mMap.addMarker(new MarkerOptions()
						.title(placeName)
						.position(targetLatlng)
//						.icon(BitmapDescriptorFactory
//								.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
						.snippet(placeAddress));
		GetPlacesTask.drawMarker(marker, placeCategory);
		
		if(!swap) {
			mBtnCurrentToPlace.setBackgroundResource(R.drawable.shape_detail_button_selected);
			mBtnCurrentToPlace.setEnabled(false);
		} else {
			mBtnPlaceToCurrent.setBackgroundResource(R.drawable.shape_detail_button_selected);
			mBtnPlaceToCurrent.setEnabled(false);
		}
	}
	
	/**
	 * 
	 */
	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(placeName);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.direction, menu);
		carItem = menu.getItem(0);
		walkingItem= menu.getItem(1);
		if(mode.equals(DirectionService.MODE_DRIVING)) {
			carItem.setEnabled(false);
			carItem.setIcon(R.drawable.icon_car_32_actived);
		} else {
			walkingItem.setEnabled(false);
			walkingItem.setIcon(R.drawable.icon_walking_32_actived);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.zoom_in_half, R.anim.move_left_out);
			break;
		case R.id.action_direction_driving:
			mode = DirectionService.MODE_DRIVING;
			checkAndShowDirection();
			carItem.setEnabled(false);
			carItem.setIcon(R.drawable.icon_car_32_actived);
			walkingItem.setEnabled(true);
			walkingItem.setIcon(R.drawable.icon_walking_32);
			break;
		case R.id.action_direction_walking:
			mode = DirectionService.MODE_WALKING;
			checkAndShowDirection();
			carItem.setEnabled(true);
			carItem.setIcon(R.drawable.icon_car_32);
			walkingItem.setEnabled(false);
			walkingItem.setIcon(R.drawable.icon_walking_32_actived);
			break;
		default:
			break;
		}
		
		removeFirstToturialView();
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 
	 * @param fromPosition
	 * @param toPosition
	 */
	private void showDirectionAndDetails(final LatLng fromPosition, final LatLng toPosition, final String mode) {

		new AsyncTask<String, Void, Document>() {
			
			DirectionService md = new DirectionService();
			Document doc;
			ProgressDialog dialog;
			
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

				if(line!=null) {
					line.remove();
				}
				line = mMap.addPolyline(rectLine);
				LatLngBounds bounds = new LatLngBounds(md.getSouthWestBound(doc), md.getNorthEastBound(doc));
				mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
				
				distance = md.getDistanceText(doc);
				duration = md.getDurationText(doc);
				
				mTVTimeDistance.setText(duration + ", " + distance);
				
				checkShowFirstToturial();
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = new ProgressDialog(mContext);
				dialog.setCancelable(true);
				dialog.setMessage(mContext.getResources().getString(R.string.text_searching));
				dialog.isIndeterminate();
				dialog.show();
			}
			
		}.execute();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		GeneralUtils.showShortToast(mContext,
				getResources().getString(R.string.msg_connect_fail));
	}
	
	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		moveToMyLocation();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_current_to_place:
			mBtnCurrentToPlace.setBackgroundResource(R.drawable.shape_detail_button_selected);
			mBtnCurrentToPlace.setEnabled(false);
			mBtnPlaceToCurrent.setBackgroundResource(R.drawable.shape_direction_swap);
			mBtnPlaceToCurrent.setEnabled(true);
			swap = false;
			checkAndShowDirection();
			break;
			
		case R.id.btn_place_to_current:
			mBtnPlaceToCurrent.setBackgroundResource(R.drawable.shape_detail_button_selected);
			mBtnPlaceToCurrent.setEnabled(false);
			mBtnCurrentToPlace.setBackgroundResource(R.drawable.shape_direction_swap);
			mBtnCurrentToPlace.setEnabled(true);
			swap = true;
			checkAndShowDirection();
			break;
		
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
			
		default:
			break;
		}
		
		removeFirstToturialView();
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
		if(v.equals(mBoxTotorial) || mBoxTotorial.getVisibility() == View.VISIBLE) {
			removeFirstToturialView();
		}
		return false;
	}

	@Override
	public int getContentViewId() {
		return R.layout.activity_direction_details;
	}

	@Override
	public void initView() {
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_direction)).getMap();
		mMap.setMyLocationEnabled(true);
		
		mTVPlaceName = (TextView) findViewById(R.id.txt_direction_target);
		mTVTimeDistance = (TextView) findViewById(R.id.txt_direction_time_distance);
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		mBtnZoomOut = (LinearLayout) findViewById(R.id.btn_direction_zoom_out);
		mBtnZoomIn = (LinearLayout) findViewById(R.id.btn_direction_zoom_in);
		mBtnZoomOut.setOnClickListener(this);
		mBtnZoomIn.setOnClickListener(this);
		
		mBoxZoom = (LinearLayout) findViewById(R.id.box_direction_zoom);
		mBoxZoom.setVisibility(View.GONE);
		
		mBtnCurrentToPlace = (LinearLayout) findViewById(R.id.btn_current_to_place);
		mBtnPlaceToCurrent = (LinearLayout) findViewById(R.id.btn_place_to_current);
		mBtnCurrentToPlace.setOnClickListener(this);
		mBtnPlaceToCurrent.setOnClickListener(this);
		
		mCbxNavigate = (CheckBox) findViewById(R.id.cbx_navigate);
		mCbxNavigate.setOncheckListener(new OnCheckListener() {
			
			@Override
			public void onCheck(boolean isChecked) {
				if(mSensorManager!=null) {
					if (isChecked) {
						mSensorManager.registerListener(mSensorListener,
								mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
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
//		mCbxNavigate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(mSensorManager!=null) {
//					if (isChecked) {
//						mSensorManager.registerListener(mSensorListener,
//								mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//								SensorManager.SENSOR_DELAY_UI);
//						mBoxZoom.setVisibility(View.VISIBLE);
//					} else {
//						mSensorManager.unregisterListener(mSensorListener);
//						mBoxZoom.setVisibility(View.GONE);
//					}
//				} else {
//					GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_connect_fail));
//				}
//			}
//		});
		
		mBoxTotorial = (RelativeLayout) findViewById(R.id.box_toturial_direction);
		mBoxTotorial.setOnTouchListener(this);
		
		initFirstData();
		initActionBar();
		mGoogleClientApi.connect();
	}

}
