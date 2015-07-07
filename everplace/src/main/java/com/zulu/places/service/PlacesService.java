package com.zulu.places.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.zulu.places.R;
import com.zulu.places.model.Place;
import com.zulu.places.model.PlaceDetail;

/**
 *  Create request for Places API.
 * 
 * @author Karn Shah
 * @Date   10/3/2013
 *
 */
public class PlacesService {

	private String API_KEY;
	
	private String nextPageToken;
	
	private Context mContext;

	/**
	 * get next page token
	 * @return
	 */
	public String getNextPageToken() {
		return nextPageToken;
	}

	/**
	 * set next page token
	 * @param nextPageToken
	 */
	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	/**
	 * 
	 * @param apikey
	 */
	public PlacesService(String apikey, Context context) {
		this.API_KEY = apikey;
		this.mContext = context;
	}

	/**
	 * 
	 * @param apikey
	 */
	public void setApiKey(String apikey) {
		this.API_KEY = apikey;
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param placeSpacification
	 * @return
	 */
	public ArrayList<Place> findSinglePlaces(double latitude, double longitude, String placeSpacification) {

		String urlString = makeSingleUrl(latitude, longitude, placeSpacification);
		Log.i("PLacesService", "urlString: " + urlString);
		try {
			String json = getUrlContents(urlString);
			Log.i("PLacesService", json);
			System.out.println(json);
			if(json != null) {
				JSONObject object = new JSONObject(json);
				
				if(!object.isNull("next_page_token")) {
					setNextPageToken(object.getString("next_page_token"));
				} else {
					setNextPageToken("");
				}
				
				if(!object.isNull("results")) {
					JSONArray array = object.getJSONArray("results");
		
					ArrayList<Place> arrayList = new ArrayList<Place>();
					for (int i = 0; i < array.length(); i++) {
						try {
							Place place = Place
									.jsonToPontoReferencia((JSONObject) array.get(i));
							Log.v("Places Services ", "" + place);
							arrayList.add(place);
						} catch (Exception e) {
						}
					}
	//				String detailString = makePlaceDetailUrl(arrayList.get(0).getReference());
					return arrayList;
				}
			} else {
				
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param placeSpacification
	 * @return
	 */
	public ArrayList<Place> findSearchText(double latitude, double longitude, String textSearch) {

		String urlString = makeSearchTextUrl(latitude, longitude, textSearch);
		Log.i("PLacesService", "urlString: " + urlString);
		try {
			String json = getUrlContents(urlString);
			Log.i("PLacesService", json);
			System.out.println(json);
			if(json != null) {
				JSONObject object = new JSONObject(json);
				
				if(!object.isNull("next_page_token")) {
					setNextPageToken(object.getString("next_page_token"));
				} else {
					setNextPageToken("");
				}
				
				if(!object.isNull("results")) {
					JSONArray array = object.getJSONArray("results");
		
					ArrayList<Place> arrayList = new ArrayList<Place>();
					for (int i = 0; i < array.length(); i++) {
						try {
							Place place = Place
									.jsonToPontoReferencia((JSONObject) array.get(i));
							Log.v("Places Services ", "" + place);
							arrayList.add(place);
						} catch (Exception e) {
						}
					}
	//				String detailString = makePlaceDetailUrl(arrayList.get(0).getReference());
					return arrayList;
				}
			} else {
				
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * get more 20 places with new next page token if any
	 * @param nextPageToken
	 * @return
	 */
	public ArrayList<Place> findMorePlaces(String nextPageToken) {

		String urlString = makeGetMorePlacesUrl(nextPageToken);

		try {
			String json = getUrlContents(urlString);
			Log.i("PLacesService", json);
			System.out.println(json);
			JSONObject object = new JSONObject(json);
			
			if(!object.isNull("next_page_token")) {
				setNextPageToken(object.getString("next_page_token"));
			} else {
				setNextPageToken("");
			}
			
			if(!object.isNull("results")) {
				JSONArray array = object.getJSONArray("results");
	
				ArrayList<Place> arrayList = new ArrayList<Place>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Place place = Place
								.jsonToPontoReferencia((JSONObject) array.get(i));
						Log.v("Places Services ", "" + place);
						arrayList.add(place);
					} catch (Exception e) {
					}
				}
//				String detailString = makePlaceDetailUrl(arrayList.get(0).getReference());
				return arrayList;
			}
		} catch (JSONException ex) {
			Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param placeSpacification
	 * @param keyword
	 * @return
	 */
	public ArrayList<Place> findPlacesFilterKeyword(double latitude, double longitude, String placeSpacification, String keyword) {

		String urlString = makeUrlFilterKeyword(latitude, longitude, placeSpacification, keyword);

		try {
			String json = getUrlContents(urlString);
			Log.i("PLacesService", json);
			System.out.println(json);
			JSONObject object = new JSONObject(json);
			
			if(!object.isNull("next_page_token")) {
				setNextPageToken(object.getString("next_page_token"));
			} else {
				setNextPageToken("");
			}
			
			if(!object.isNull("results")) {
				JSONArray array = object.getJSONArray("results");
	
				ArrayList<Place> arrayList = new ArrayList<Place>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Place place = Place
								.jsonToPontoReferencia((JSONObject) array.get(i));
						Log.v("Places Services ", "" + place);
						arrayList.add(place);
					} catch (Exception e) {
					}
				}
				return arrayList;
			}
		} catch (JSONException ex) {
			Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param placeSpacification
	 * @param radius
	 * @return
	 */
	public ArrayList<Place> findPlacesFilterRadius(double latitude, double longitude, String placeSpacification, int radius) {

		String urlString = makeUrlFilterRadius(latitude, longitude, placeSpacification, radius);

		try {
			String json = getUrlContents(urlString);
			Log.i("PLacesService", json);
			System.out.println(json);
			JSONObject object = new JSONObject(json);
			
			if(!object.isNull("next_page_token")) {
				setNextPageToken(object.getString("next_page_token"));
			} else {
				setNextPageToken("");
			}
			
			if(!object.isNull("results")) {
				JSONArray array = object.getJSONArray("results");
	
				ArrayList<Place> arrayList = new ArrayList<Place>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Place place = Place
								.jsonToPontoReferencia((JSONObject) array.get(i));
						Log.v("Places Services ", "" + place);
						arrayList.add(place);
					} catch (Exception e) {
					}
				}
				return arrayList;
			}
		} catch (JSONException ex) {
			Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param placeSpacification
	 * @param keyword
	 * @param radius
	 * @return
	 */
	public ArrayList<Place> findPlacesFilterKeywordRadius(double latitude, double longitude, String placeSpacification, String keyword, int radius) {

		String urlString = makeUrlFilterKeywordRadius(latitude, longitude, placeSpacification, keyword, radius);

		try {
			String json = getUrlContents(urlString);
			Log.i("PLacesService", json);
			System.out.println(json);
			JSONObject object = new JSONObject(json);
			
			if(!object.isNull("next_page_token")) {
				setNextPageToken(object.getString("next_page_token"));
			} else {
				setNextPageToken("");
			}
			
			if(!object.isNull("results")) {
				JSONArray array = object.getJSONArray("results");
	
				ArrayList<Place> arrayList = new ArrayList<Place>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Place place = Place
								.jsonToPontoReferencia((JSONObject) array.get(i));
						Log.v("Places Services ", "" + place);
						arrayList.add(place);
					} catch (Exception e) {
					}
				}
				return arrayList;
			}
		} catch (JSONException ex) {
			Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;
	}
	
	/**
	 * 
	 * @param placeReference
	 * @return
	 */
	public PlaceDetail findPlaceDetails(String placeReference, String placeCategory) {

		String urlString = makePlaceDetailUrl(placeReference);

		try {
			String json = getUrlContents(urlString);
			Log.i("PLacesService", json);
			System.out.println(json);
			JSONObject object = new JSONObject(json);
			PlaceDetail detail = new PlaceDetail();
			if(!object.isNull("result")) {
				JSONObject detailObj = object.getJSONObject("result");
	
				try {
					detail = PlaceDetail
							.jsonToDetailReferencia((JSONObject) detailObj, mContext);
					detail.setCategory(placeCategory);
					Log.i("Place Details", "" + detailObj);
				} catch (Exception e) {
					Log.i("Place Details", "Exception: " + e.getMessage());
				}
				return detail;
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
			Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	/**
	 * 
	 * @param photoReference
	 * @return
	 */
	public String getPhotoResponseUrl(String photoReference) {
		StringBuilder urlString = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/photo?maxwidth=400");

		if (photoReference.equals("")) {
//			urlString.append("&location=");
//			urlString.append(Double.toString(latitude));
//			urlString.append(",");
//			urlString.append(Double.toString(longitude));
//			urlString.append("&radius=10000");
			// urlString.append("&types="+place);
//			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&photoreference=" + photoReference);
			urlString.append("&sensor=false&key=" + API_KEY);
		}
		return urlString.toString();
	}
	
	/**
	 * 
	 * @param reference
	 * @return
	 */
	private String makePlaceDetailUrl(String placeReference) {
		StringBuilder urlString = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/details/json?");

		if (placeReference.equals("")) {
//			urlString.append("&location=");
//			urlString.append(Double.toString(latitude));
//			urlString.append(",");
//			urlString.append(Double.toString(longitude));
//			urlString.append("&radius=10000");
			// urlString.append("&types="+place);
//			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&reference=" + placeReference);
			urlString.append("&sensor=false&key=" + API_KEY);
		}
		return urlString.toString();
	}

	// https://maps.googleapis.com/maps/api/place/search/json?location=28.632808,77.218276&radius=500&types=atm&sensor=false&key=apikey
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param place
	 * @return
	 */
	private String makeSingleUrl(double latitude, double longitude, String place) {
		StringBuilder urlString = new StringBuilder(mContext.getString(R.string.api_place_search_url));

		if (place.equals("")) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=10000");
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&rankby=distance");
			urlString.append("&types=" + place);
			urlString.append("&sensor=false&key=" + API_KEY);
		}
		return urlString.toString();
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param textSearch
	 * @return
	 */
	private String makeSearchTextUrl(double latitude, double longitude, String textSearch) {
		StringBuilder urlString = new StringBuilder(mContext.getString(R.string.api_place_text_search_url));

		if (textSearch.equals("")) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=10000");
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
//			urlString.append("&rankby=distance");
			urlString.append("query=" + textSearch);
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=5000");
			urlString.append("&key=" + API_KEY);
		}
		return urlString.toString();
	}
	
	/**
	 * 
	 * @param nextPageToken
	 * @return
	 */
	private String makeGetMorePlacesUrl(String nextPageToken) {
		StringBuilder urlString = new StringBuilder(mContext.getString(R.string.api_place_search_url));

		if (nextPageToken.equals("")) {
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&pagetoken=" + nextPageToken);
			urlString.append("&sensor=false&key=" + API_KEY);
		}
		return urlString.toString();
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param place
	 * @param keyword
	 * @return
	 */
	private String makeUrlFilterKeyword(double latitude, double longitude, String place, String keyword) {
		StringBuilder urlString = new StringBuilder(mContext.getString(R.string.api_place_search_url));

		if (place.equals("")) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=10000");
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&rankby=distance");
			urlString.append("&types=" + place);
			urlString.append("&keyword=" + keyword);
			urlString.append("&sensor=false&key=" + API_KEY);
		}
		return urlString.toString();
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param place
	 * @param radius
	 * @return
	 */
	private String makeUrlFilterRadius(double latitude, double longitude, String place, int radius) {
		StringBuilder urlString = new StringBuilder(mContext.getString(R.string.api_place_search_url));

		if (place.equals("")) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=10000");
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=" + radius);
			urlString.append("&types=" + place);
			urlString.append("&sensor=false&key=" + API_KEY);
		}
		return urlString.toString();
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param place
	 * @param keyword
	 * @param radius
	 * @return
	 */
	private String makeUrlFilterKeywordRadius(double latitude, double longitude, String place, String keyword, int radius) {
		StringBuilder urlString = new StringBuilder(mContext.getString(R.string.api_place_search_url));

		if (place.equals("")) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=10000");
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&types=" + place);
			urlString.append("&radius=" + radius);
			urlString.append("&keyword=" + keyword);
			urlString.append("&sensor=false&key=" + API_KEY);
		}
		return urlString.toString();
	}

	/**
	 * 
	 * @param theUrl
	 * @return
	 */
	private String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setReadTimeout(1000);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()), 8);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}