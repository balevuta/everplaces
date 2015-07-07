package com.zulu.places.database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.zulu.places.model.PlaceDetail;
import com.zulu.places.model.ReviewDetail;

public class HistoryTableHelper {

	private DatabaseHelper mDbHelper;

	public HistoryTableHelper(DatabaseHelper dbHelper) {
		mDbHelper = dbHelper;
	}
	
	/**
	 * 
	 * @param placeDetailModel
	 * @return
	 */
	public boolean addPlace(PlaceDetail placeDetailModel) {
		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mDbHelper.getWritableDatabase();
				try {

					db.beginTransaction();
					ContentValues values = new ContentValues();

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_NAME,
							placeDetailModel.getName());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_ADDRESS,
							placeDetailModel.getAddress());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LAT,
							placeDetailModel.getLatitude());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LNG,
							placeDetailModel.getLongitude());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_RATING,
							placeDetailModel.getRating());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_NUM_RATING,
							placeDetailModel.getNoOfReviews());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_EMAIL,
							placeDetailModel.getEmail());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_WEBSITE,
							placeDetailModel.getWebsite());

					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_PHONE,
							placeDetailModel.getPhone());
					
					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_CATEGORY,
							placeDetailModel.getCategory());
					
					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_ARR_PHOTO,
							placeDetailModel.getArrPhotoDB());
					
					values.put(
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_REVIEW_JSON,
							placeDetailModel.getReviewJson());

					db.insertOrThrow(DatabaseDefinition.PlacesListHistory.TABLE,
							null, values);
					db.setTransactionSuccessful();
					Log.i(DatabaseManager.TAG, "Saved history: " + "Name: " + placeDetailModel.getName() + " - Category: " + placeDetailModel.getCategory());
					return true;
				} catch (SQLException e) {

					e.printStackTrace();
					Log.i(DatabaseManager.TAG, "SQLException: " + e.getMessage());
					return false;
				} catch (Exception e) {

					e.printStackTrace();
					Log.i(DatabaseManager.TAG, "Exception" +
							": " + e.getMessage());
					return false;
				}
				
				finally {
					db.endTransaction();
				}
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}
	
	/**
	 * delete a place from database favorite
	 * @param place
	 * @return
	 */
	public boolean deletePlace(PlaceDetail place) {
		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mDbHelper.getWritableDatabase();
				try {

					db.beginTransaction();
					
					db.delete(DatabaseDefinition.PlacesListHistory.TABLE, 
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LAT + "=? and " +
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LNG + "=?", 
							new String[] {String.valueOf(place.getLatitude()),
											String.valueOf(place.getLongitude())});
					
					db.setTransactionSuccessful();
					Log.i(DatabaseManager.TAG, "Removed history: " + place.getName());
					return true;
				} catch (SQLException e) {

					e.printStackTrace();
					Log.i(DatabaseManager.TAG, "SQLException: " + e.getMessage());
					return false;
				} finally {
					db.endTransaction();
				}
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}
	
	/**
	 * delete all places from database history
	 * @param place
	 * @return
	 */
	public boolean deleteAllPlaces() {
		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mDbHelper.getWritableDatabase();
				try {

					db.beginTransaction();
					
					db.delete(DatabaseDefinition.PlacesListHistory.TABLE, 
							"1", null);
					
					db.setTransactionSuccessful();
					Log.i(DatabaseManager.TAG, "Removed all history places");
					return true;
				} catch (SQLException e) {

					e.printStackTrace();
					Log.i(DatabaseManager.TAG, "SQLException: " + e.getMessage());
					return false;
				} finally {
					db.endTransaction();
				}
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<PlaceDetail> getAllHistoryPlaces() {

		ArrayList<PlaceDetail> lstPlaces = new ArrayList<PlaceDetail>();

		SQLiteDatabase db = null;
		Cursor c = null;

		try {
			db = mDbHelper.getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(DatabaseDefinition.PlacesListHistory.TABLE);
			c = qb.query(
					db, // Database
					new String[] {
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_NAME,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_ADDRESS,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LAT,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LNG,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_RATING,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_NUM_RATING,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_PHONE,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_EMAIL,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_WEBSITE,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_CATEGORY,
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_ARR_PHOTO, 
							DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_REVIEW_JSON},
					null, null, // argument[]
					null, // groupBy
					null, // having
					DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_NAME + " ASC" // order
			);
			if (c.moveToFirst()) {
				do {
					PlaceDetail placeModel = new PlaceDetail();

					placeModel
							.setName(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_NAME)));
					placeModel
							.setAddress(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_ADDRESS)));
					placeModel
							.setLatitude(Double.valueOf(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LAT))));
					placeModel
							.setLongitude(Double.valueOf(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_LNG))));
					placeModel
							.setRating(Double.valueOf(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_RATING))));
					placeModel
							.setNoOfReviews(Integer.valueOf(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_NUM_RATING))));
					placeModel
							.setPhone(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_PHONE)));
					placeModel
							.setEmail(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_EMAIL)));
					placeModel
							.setWebsite(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_WEBSITE)));
					placeModel
							.setCategory(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_CATEGORY)));
					placeModel
							.setArrPhotoDB(c.getString(c
									.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_ARR_PHOTO)));
					
					String reviewJson = c.getString(c
							.getColumnIndex(DatabaseDefinition.PlacesListHistory.COLUMN_PLACE_REVIEW_JSON));
					if(reviewJson!=null) {
						try {
							JSONArray reviewArr = new JSONArray(reviewJson);
						
							List<ReviewDetail> lstReviews = new ArrayList<ReviewDetail>();
							
							if(reviewArr.length()>0) {
					            for(int k=0;k<reviewArr.length();k++) {
					            	JSONObject objReview = reviewArr.getJSONObject(k);
					            	ReviewDetail reviewModel = new ReviewDetail();
					            	if(!objReview.isNull("author_name")) {
					            		reviewModel.setAuthorName(objReview.getString("author_name"));
					            	}
					            	if(!objReview.isNull("author_url")) {
					            		reviewModel.setAuthorURL(objReview.getString("author_url"));
					            	}
					            	if(!objReview.isNull("text")) {
					            		reviewModel.setText(objReview.getString("text"));
					            	}
					            	if(!objReview.isNull("rating")) {
					            		reviewModel.setRating(objReview.getInt("rating"));
					            	}
					            	if(!objReview.isNull("time")) {
					            		reviewModel.setTime(objReview.getLong("time"));
					            	}
					            	lstReviews.add(reviewModel);
					            }
					            placeModel.setLstReviews(lstReviews);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					lstPlaces.add(placeModel);
				} while (c.moveToNext());
			} else {
				lstPlaces = null;
			}
		} finally {
			if (db != null) {
				db.close();
			}
			if (c != null) {
				c.close();
			}
		}

		return lstPlaces;
	}
}
