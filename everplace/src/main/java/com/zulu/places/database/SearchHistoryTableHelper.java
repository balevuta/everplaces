package com.zulu.places.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class SearchHistoryTableHelper {

	private DatabaseHelper mDbHelper;

	public SearchHistoryTableHelper(DatabaseHelper dbHelper) {
		mDbHelper = dbHelper;
	}
	
	/**
	 * 
	 * @param placeDetailModel
	 * @return
	 */
	public boolean addSearchText(String text) {
		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mDbHelper.getWritableDatabase();
				try {

					db.beginTransaction();
					ContentValues values = new ContentValues();

					values.put(
							DatabaseDefinition.SearchHistory.COLUMN_TEXT_STRING, text);

					db.insertOrThrow(DatabaseDefinition.SearchHistory.TABLE,
							null, values);
					db.setTransactionSuccessful();
					Log.i(DatabaseManager.TAG, "Saved history: " + "text: " + text);
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
					
					db.delete(DatabaseDefinition.SearchHistory.TABLE, 
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
	public List<String> getAllHistorySearch() {

		List<String> lstText = new ArrayList<String>();

		SQLiteDatabase db = null;
		Cursor c = null;

		try {
			db = mDbHelper.getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(DatabaseDefinition.SearchHistory.TABLE);
			c = qb.query(
					db, // Database
					new String[] {
							DatabaseDefinition.SearchHistory.COLUMN_TEXT_STRING},
					null, null, // argument[]
					null, // groupBy
					null, // having
					DatabaseDefinition.SearchHistory.COLUMN_TEXT_ID + " ASC" // order
			);
			if (c.moveToFirst()) {
				do {
					String text = "";
					
					text = c.getString(c
							.getColumnIndex(DatabaseDefinition.SearchHistory.COLUMN_TEXT_STRING));
					
					lstText.add(text);
				} while (c.moveToNext());
			} else {
				lstText = null;
			}
		} finally {
			if (db != null) {
				db.close();
			}
			if (c != null) {
				c.close();
			}
		}

		return lstText;
	}
}
