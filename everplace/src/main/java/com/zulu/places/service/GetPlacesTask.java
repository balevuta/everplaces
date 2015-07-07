package com.zulu.places.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.zulu.places.R;
import com.zulu.places.extras.MapWrapperLayout;
import com.zulu.places.extras.OnInfoWindowElemTouchListener;
import com.zulu.places.layout.ListPlacesActivity;
import com.zulu.places.layout.MapActivity;
import com.zulu.places.model.Place;
import com.zulu.places.model.UserSetting;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;

public class GetPlacesTask extends AsyncTask<Void, Void, ArrayList<Place>> {

	public static int SEARCH_MODE_SINGLE = 0;
	public static int SEARCH_MODE_FILTER_KEYWORD = 1;
	public static int SEARCH_MODE_FILTER_RADIUS = 2;
	public static int SEARCH_MODE_FILTER_KEYWORD_RADIUS = 3;
	public static int SEARCH_MODE_TEXT = 4;
	
	private int SEARCH_MODE;
	
	private ProgressDialog dialog;
	private Context mContext;
	private String places;
	private String textSearch;
	
	private GoogleMap mMap;
	private Location loc;
	
	// custom info window with touchable event
	private MapWrapperLayout mWrapperLayout;
	private OnInfoWindowElemTouchListener mBtnDirectListener, mBtnGoDetailListener;
	private ViewGroup infoWindow;
    private TextView mTxtInfoTitle;
    private TextView mTxtInfoSnippet;
    private TextView mTxtInfoDistance;
    private LinearLayout mBtnDirect, mBtnGoDetail;
	
	private Polyline line;
	private String keyword;
	private int radius;
	
	private ArrayList<Place> lstPlacesResult;
	private List<Marker> lstMarker;
	
	private GetDetailsTask mGetDetailsTask;
	private int posPlace = 0;
	
	private PlacesService placeService;

	/**
	 * 
	 * @param context
	 * @param places
	 * @param mMap
	 * @param loc
	 */
	public GetPlacesTask(Context context, String textOrPlay, GoogleMap mMap, Location loc, MapWrapperLayout mWrapperLayout, boolean isSearch) {
		this.mContext = context;
		this.mMap = mMap;
		this.loc = loc;
		this.mWrapperLayout = mWrapperLayout;
		if(isSearch) {
			this.textSearch = textOrPlay;
			SEARCH_MODE = SEARCH_MODE_TEXT;
		} else {
			this.places = textOrPlay;
			SEARCH_MODE = SEARCH_MODE_SINGLE;
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param places
	 * @param mMap
	 * @param loc
	 * @param keyword
	 */
	public GetPlacesTask(Context context, String places, GoogleMap mMap, Location loc, String keyword, MapWrapperLayout mWrapperLayout) {
		this.mContext = context;
		this.places = places;
		this.mMap = mMap;
		this.loc = loc;
		this.keyword = keyword;
		this.mWrapperLayout = mWrapperLayout;
		SEARCH_MODE = SEARCH_MODE_FILTER_KEYWORD;
	}
	
	/**
	 * 
	 * @param context
	 * @param places
	 * @param mMap
	 * @param loc
	 * @param radius
	 */
	public GetPlacesTask(Context context, String places, GoogleMap mMap, Location loc, int radius, MapWrapperLayout mWrapperLayout) {
		this.mContext = context;
		this.places = places;
		this.mMap = mMap;
		this.loc = loc;
		this.radius = radius;
		this.mWrapperLayout = mWrapperLayout;
		SEARCH_MODE = SEARCH_MODE_FILTER_RADIUS;
	}
	
	/**
	 * 
	 * @param context
	 * @param places
	 * @param mMap
	 * @param loc
	 * @param keyword
	 * @param radius
	 */
	public GetPlacesTask(Context context, String places, GoogleMap mMap, Location loc, String keyword, int radius, MapWrapperLayout mWrapperLayout) {
		this.mContext = context;
		this.places = places;
		this.mMap = mMap;
		this.loc = loc;
		this.keyword = keyword;
		this.radius = radius;
		this.mWrapperLayout = mWrapperLayout;
		SEARCH_MODE = SEARCH_MODE_FILTER_KEYWORD_RADIUS;
	}

	@Override
	protected void onPostExecute(ArrayList<Place> result) {
		super.onPostExecute(result);
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
		if (result != null) {
			if(result.size() > 0) {
				GeneralUtils.showShortToast(mContext, mContext.getResources().
						getString(R.string.msg_no_results).replace("{0}", String.valueOf(result.size())));
				
				lstMarker = new ArrayList<Marker>();
				mMap.clear();
				for (int i = 0; i < result.size(); i++) {
					
	//				posPlace = i;
					
					final float[] disArr = new float[3];
					Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), result.get(i).getLatitude(), result.get(i).getLongitude(), disArr);
					
					Marker markerItem = mMap.addMarker(new MarkerOptions()
							.position(
									new LatLng(result.get(i).getLatitude(), result.get(i).getLongitude()))
							.title(result.get(i).getName())
	//						.icon(BitmapDescriptorFactory
	//								.fromResource(R.drawable.pin_restaurant))
	//						.icon(BitmapDescriptorFactory
	//								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
							.snippet(result.get(i).getVicinity() + "<;>" + GeneralUtils.convertToDisplayDistance(mContext, disArr[0]) + "<;>" + String.valueOf(i)));
					
					drawMarker(markerItem, places);
					
					drawTouchableInfoWindow(result);
			        
					lstMarker.add(markerItem);
				}
			} else {
				GeneralUtils.showLongToast(mContext, mContext.getResources().
						getString(R.string.msg_no_results_found));
			}
			
			if(lstMarker!=null) {
				if(lstMarker.size()>0) {
					lstMarker.get(0).showInfoWindow();
					
					LatLngBounds.Builder bounds = new LatLngBounds.Builder();
					for (Marker itemMarker : lstMarker) {
						LatLng itemLatLng = itemMarker.getPosition();
						bounds.include(itemLatLng);
					}
					mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 500, 500, 5));
				}
			}
		} else {
			GeneralUtils.showLongToast(mContext, mContext.getResources().
					getString(R.string.msg_connect_fail));
		}
		
		// send broadcash get done
		Intent i = new Intent(MapActivity.class.getName())
			.putExtra(Constants.ActionReceiver.ACTION_RECEIVER, Constants.ActionReceiver.SHOW_TOTURIAL);
		mContext.sendBroadcast(i);
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(mContext);
		dialog.setCancelable(true);
		dialog.setMessage(mContext.getResources().getString(R.string.text_loading));
		dialog.isIndeterminate();
		dialog.show();
	}

	@Override
	protected ArrayList<Place> doInBackground(Void... arg0) {
		placeService = new PlacesService(mContext.getString(R.string.api_place_key), mContext);
		
		ArrayList<Place> findPlaces = new ArrayList<Place>();
		try {
			if(loc!=null) {
				if(SEARCH_MODE == SEARCH_MODE_SINGLE) {
					findPlaces = placeService.findSinglePlaces(
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLatitude() : Constants.TargetMode.VT_LAT,
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLongitude() : Constants.TargetMode.VT_LNG, 
							places); 
				} else if (SEARCH_MODE == SEARCH_MODE_TEXT) {
					findPlaces = placeService.findSearchText(
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLatitude() : Constants.TargetMode.VT_LAT,
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLongitude() : Constants.TargetMode.VT_LNG, 
							textSearch); 
				} else if (SEARCH_MODE == SEARCH_MODE_FILTER_KEYWORD) {
					findPlaces = placeService.findPlacesFilterKeyword(
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLatitude() : Constants.TargetMode.PQ_LAT,
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLongitude() : Constants.TargetMode.PQ_LNG,
							places, keyword);
				} else if(SEARCH_MODE == SEARCH_MODE_FILTER_RADIUS) {
					findPlaces = placeService.findPlacesFilterRadius(
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLatitude() : Constants.TargetMode.PQ_LAT,
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLongitude() : Constants.TargetMode.PQ_LNG,
							places, radius);
				} else if (SEARCH_MODE == SEARCH_MODE_FILTER_KEYWORD_RADIUS) {
					findPlaces = placeService.findPlacesFilterKeywordRadius(
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLatitude() : Constants.TargetMode.PQ_LAT,
							UserSetting.getInstance().getTargetMode()==Constants.TargetMode.CURRENT_LOCATION ? loc.getLongitude() : Constants.TargetMode.PQ_LNG,
							places, keyword, radius);
				}
				lstPlacesResult = new ArrayList<Place>();
				lstPlacesResult = findPlaces;
			} else {
				dialog.dismiss();
				GeneralUtils.showShortToast(mContext, mContext.getResources().getString(R.string.msg_connect_fail));
			}
		} catch(Exception e) {
			dialog.dismiss();
//			GeneralUtils.showShortToast(mContext, mContext.getResources().getString(R.string.msg_error_not_sure));
			Log.i("GetPlacesTask", e.getMessage());
		}

//		for (int i = 0; i < findPlaces.size(); i++) {
//			Place placeDetail = findPlaces.get(i);
//			Log.e("GetPlacesTask", "places : " + placeDetail.getName());
//		}
		return findPlaces;
	}
	
	/**
	 * draw info window with touchable event
	 * @param result
	 */
	private void drawTouchableInfoWindow(final ArrayList<Place> result) {
		
		mWrapperLayout.init(mMap, GeneralUtils.getPixelsFromDp(mContext, 39 + 20));
		this.infoWindow = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.window_info_touchable, null);
        this.mTxtInfoTitle = (TextView) infoWindow.findViewById(R.id.title);
        this.mTxtInfoSnippet = (TextView) infoWindow.findViewById(R.id.snippet);
        this.mTxtInfoDistance = (TextView) infoWindow.findViewById(R.id.distance);
        this.mBtnDirect = (LinearLayout) infoWindow.findViewById(R.id.btn_direction);
        this.mBtnGoDetail = (LinearLayout) infoWindow.findViewById(R.id.btn_go_detail);
		
        this.mBtnDirectListener = new OnInfoWindowElemTouchListener(mBtnDirect,
        		R.drawable.shape_info_window_button, R.drawable.shape_info_window_button_selected) {
			
			@Override
			protected void onClickConfirmed(View v, Marker marker) {
				GeneralUtils.showDirectionOpionDialog(mContext, 
						mTxtInfoTitle.getText().toString(), mTxtInfoSnippet.getText().toString(), 
						marker.getPosition().latitude, marker.getPosition().longitude, places);
			}
		};
		
		this.mBtnDirect.setOnTouchListener(mBtnDirectListener);
		
		this.mBtnGoDetailListener = new OnInfoWindowElemTouchListener(mBtnGoDetail, 
				R.drawable.shape_info_window_button, R.drawable.shape_info_window_button_selected) {
			
			@Override
			protected void onClickConfirmed(View v, Marker marker) {
				searchAndStartPlaceDetails(result.get(posPlace).getReference());
				ListPlacesActivity.placesPos = posPlace;
			}
		};
		
		this.mBtnGoDetail.setOnTouchListener(mBtnGoDetailListener);
		
		mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker marker) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public View getInfoContents(Marker marker) {
				
				String[] textResult = marker.getSnippet().split("<;>");
				
				// set number of marker
				posPlace = Integer.valueOf(textResult[2]);
				
				// Setting up the infoWindow with current's marker info
                mTxtInfoTitle.setText(marker.getTitle());
                mTxtInfoSnippet.setText(textResult[0]);
                mTxtInfoDistance.setText(textResult[1]);
                mBtnDirectListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
			}
		});
	}
	
	/**
	 * draw marker base on place type
	 * @param marker
	 * @param places
	 */
	public static void drawMarker(Marker marker, String places) {
		if(places!=null && !places.isEmpty()) {
			if(places.equals(Constants.PlaceTypes.TYPE_RESTAURANT.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_restaurant_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_ZOO.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_zoo_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_PARK.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_park_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_CAFE.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_cafe_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_SHOPPING.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_shop_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_BAR.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bar_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_FOOD.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_food_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_POST_OFFICE.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_post_office_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_BANK.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_atm_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_SUPER_MARKET.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_super_market_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_MUSEUM.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_museum_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_BEAUTY_SALON.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_salon_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_BUS_STATION.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bus_stop_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_GAS_STATION.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gas_station_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_MOVIE_THEATER.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_movie_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_NIGHT_CLUB.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_night_club_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_HOSPITAL.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hospital_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_PHARMACY.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pharmacy_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_SCHOOL.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_school_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_UNIVERSITY.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_university_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_STADIUM.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_stadium_test));
			} else if(places.equals(Constants.PlaceTypes.TYPE_GYM.toLowerCase().replace("-", "_")
					.replace(" ", "_"))) {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gym_test));
			}
			else {
				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_search_default));
			}
		} else {
			marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_search_default));
		}
	}
	
	/**
	 * async task for get more 20 result 
	 * @param nextPageToken
	 */
	public void processGetMoreResults() {
		new AsyncTask<String, Void, ArrayList<Place>>() {

			@Override
			protected ArrayList<Place> doInBackground(String... arg0) {
				ArrayList<Place> findPlaces = new ArrayList<Place>();
				if(loc!=null) {
					if(!placeService.getNextPageToken().equals("")) {
						findPlaces = placeService.findMorePlaces(placeService.getNextPageToken());
					} else {
						dialog.dismiss();
						GeneralUtils.showShortToast(mContext, mContext.getResources().getString(R.string.msg_no_results_more));
						return null;
					}
				} else {
					dialog.dismiss();
					GeneralUtils.showShortToast(mContext, mContext.getResources().getString(R.string.msg_connect_fail));
				}
				return findPlaces;
			}

			@Override
			protected void onPostExecute(ArrayList<Place> result) {
				super.onPostExecute(result);
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				
				if(result != null) {
					for (Place place : result) {
						lstPlacesResult.add(place);
					}
					
					GeneralUtils.showShortToast(mContext, mContext.getResources().
							getString(R.string.msg_no_results).replace("{0}", String.valueOf(lstPlacesResult.size())));
					
					if (lstPlacesResult.size() > 0 && lstPlacesResult != null) {
						lstMarker = new ArrayList<Marker>();
						mMap.clear();
						for (int i = 0; i < lstPlacesResult.size(); i++) {
							
//							posPlace = i;
							
							float[] disArr = new float[3];
							Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), lstPlacesResult.get(i).getLatitude(), lstPlacesResult.get(i).getLongitude(), disArr);
							
							Marker markerItem = mMap.addMarker(new MarkerOptions()
									.title(lstPlacesResult.get(i).getName())
									.position(
											new LatLng(lstPlacesResult.get(i).getLatitude(), lstPlacesResult
													.get(i).getLongitude()))
//									.icon(BitmapDescriptorFactory
//											.fromResource(R.drawable.icon_location))
//									.icon(BitmapDescriptorFactory
//											.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
									.snippet(lstPlacesResult.get(i).getVicinity() + "<;>" + GeneralUtils.convertToDisplayDistance(mContext, disArr[0]) + "<;>" + String.valueOf(i)));
							
							drawMarker(markerItem, places);
							
							drawTouchableInfoWindow(lstPlacesResult);
							
							lstMarker.add(markerItem);
						}
						
						if(lstMarker.size()>0 && lstMarker!=null)
							lstMarker.get(0).showInfoWindow();
						
						LatLngBounds.Builder bounds = new LatLngBounds.Builder();
						for (Marker itemMarker : lstMarker) {
							LatLng itemLatLng = itemMarker.getPosition();
							bounds.include(itemLatLng);
						}
						mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 500, 500, 5));
					}
				}
				
			}
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = new ProgressDialog(mContext);
//				dialog.setCancelable(true);
				dialog.setMessage(mContext.getResources().getString(R.string.text_loading));
				dialog.isIndeterminate();
				dialog.show();
			}
			
		}.execute();
	}
	
	/**
	 * 
	 * @return
	 */
	public Polyline getLastPolyline() {
		return line;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Place> getListPlacesResult() {
		return lstPlacesResult;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Marker> getListMarker() {
		return lstMarker;
	}
	
	/**
	 * 
	 * @return
	 */
	public PlacesService getPlaceService() {
		return placeService;
	}
	
	/**
	 * 
	 * @author Than Banh
	 *
	 */
	class CustomInfoAdapter implements InfoWindowAdapter {

//		private View infoWindow;
		
		View infoWindow = LayoutInflater.from(mContext).inflate(R.layout.window_popup_location, null);
		
		public CustomInfoAdapter() {
//			this.infoWindow = infoWindow;
		}
		
		@Override
		public View getInfoContents(Marker marker) {
			String[] textResult = marker.getSnippet().split("<;>");
			
			TextView tvName = (TextView)infoWindow.findViewById(R.id.txt_name_info);
			TextView tvAddress = (TextView)infoWindow.findViewById(R.id.txt_address_info);
			TextView tvDistance = (TextView)infoWindow.findViewById(R.id.txt_distance);
			
			tvName.setText(marker.getTitle());
			tvAddress.setText(textResult[0]);
			tvDistance.setText(textResult[1]);
			
			posPlace = Integer.valueOf(textResult[2]);
			
			return infoWindow;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param placeReference
	 */
	private void searchAndStartPlaceDetails(String placeReference) {
		mGetDetailsTask = new GetDetailsTask(mContext, placeReference, places);
		mGetDetailsTask.execute();
	}

}
