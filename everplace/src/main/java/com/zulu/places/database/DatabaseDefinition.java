package com.zulu.places.database;

public class DatabaseDefinition {
	public static final String CREATE_SQL = "createTable";
	public static final String UPDATE_SQL = "updateTable";

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "zuluplaces.db";

	private final static String CREATE_TABLE = "CREATE TABLE ";
//	private final static String T_LONG = " LONG ";
//	private final static String T_DOUBLE = " DOUBLE ";
	private final static String T_INTEGER = " INTEGER ";

	private final static String T_TEXT = " TEXT ";
//	private final static String T_PRIMARY_KEY = " PRIMARY KEY  ";
	private final static String T_PRIMARY_KEY_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT ";

	/** tables in dabatase */
	public static final Class<?>[] Tables = { PlacesListFavorite.class , PlacesListHistory.class, SearchHistory.class};

	/**
	 * 
	 * @author ThanBanh
	 *
	 */
	public static final class PlacesListFavorite {
		public static final String TABLE = "tbl_places_favorite";
		public static final String COLUMN_PLACE_ID = "c_place_id";
		public static final String COLUMN_PLACE_LAT = "c_place_lat";
		public static final String COLUMN_PLACE_LNG = "c_place_lng";
		public static final String COLUMN_PLACE_NAME = "c_place_name";
		public static final String COLUMN_PLACE_ADDRESS = "c_place_address";
		public static final String COLUMN_PLACE_PHONE = "c_place_phone";
		public static final String COLUMN_PLACE_RATING = "c_place_rating";
		public static final String COLUMN_PLACE_NUM_RATING = "c_place_no_rating";
		public static final String COLUMN_PLACE_EMAIL = "c_place_email";
		public static final String COLUMN_PLACE_WEBSITE = "c_place_website";
		public static final String COLUMN_PLACE_CATEGORY = "c_place_category";
		public static final String COLUMN_PLACE_ARR_PHOTO = "c_place_arr_photo";
		public static final String COLUMN_PLACE_REVIEW_JSON = "c_place_review_json";

		/**
		 * build a sql string to create table MySongList
		 */
		public static String createTable() {
			return CREATE_TABLE + TABLE + " (" + COLUMN_PLACE_ID
					+ T_INTEGER + T_PRIMARY_KEY_AUTOINCREMENT + ","
					+ COLUMN_PLACE_LAT + T_TEXT + ","
					+ COLUMN_PLACE_LNG + T_TEXT + ","
					+ COLUMN_PLACE_NAME + T_TEXT + ","
					+ COLUMN_PLACE_ADDRESS + T_TEXT + ","
					+ COLUMN_PLACE_PHONE + T_TEXT + ","
					+ COLUMN_PLACE_RATING + T_TEXT + ","
					+ COLUMN_PLACE_NUM_RATING + T_TEXT + ","
					+ COLUMN_PLACE_EMAIL + T_TEXT + ","
					+ COLUMN_PLACE_WEBSITE + T_TEXT + "," 
					+ COLUMN_PLACE_CATEGORY + T_TEXT + "," 
					+ COLUMN_PLACE_ARR_PHOTO + T_TEXT + ","
					+ COLUMN_PLACE_REVIEW_JSON + T_TEXT + ");";
		}

		/**
		 * build a sql string to update table MySongList
		 */
		public static String updateTable() {
			return "DROP TABLE IF EXISTS " + TABLE;
		}
	}
	
	/**
	 * 
	 * @author ThanBanh
	 *
	 */
	public static final class PlacesListHistory {
		public static final String TABLE = "tbl_places_history";
		public static final String COLUMN_PLACE_ID = "c_history_id";
		public static final String COLUMN_PLACE_LAT = "c_history_lat";
		public static final String COLUMN_PLACE_LNG = "c_history_lng";
		public static final String COLUMN_PLACE_NAME = "c_history_name";
		public static final String COLUMN_PLACE_ADDRESS = "c_history_address";
		public static final String COLUMN_PLACE_PHONE = "c_history_phone";
		public static final String COLUMN_PLACE_RATING = "c_history_rating";
		public static final String COLUMN_PLACE_NUM_RATING = "c_history_no_rating";
		public static final String COLUMN_PLACE_EMAIL = "c_history_email";
		public static final String COLUMN_PLACE_WEBSITE = "c_history_website";
		public static final String COLUMN_PLACE_CATEGORY = "c_history_category";
		public static final String COLUMN_PLACE_ARR_PHOTO = "c_history_arr_photo";
		public static final String COLUMN_PLACE_REVIEW_JSON = "c_history_review_json";

		/**
		 * build a sql string to create table MySongList
		 */
		public static String createTable() {
			return CREATE_TABLE + TABLE + " (" + COLUMN_PLACE_ID
					+ T_INTEGER + T_PRIMARY_KEY_AUTOINCREMENT + ","
					+ COLUMN_PLACE_LAT + T_TEXT + ","
					+ COLUMN_PLACE_LNG + T_TEXT + ","
					+ COLUMN_PLACE_NAME + T_TEXT + ","
					+ COLUMN_PLACE_ADDRESS + T_TEXT + ","
					+ COLUMN_PLACE_PHONE + T_TEXT + ","
					+ COLUMN_PLACE_RATING + T_TEXT + ","
					+ COLUMN_PLACE_NUM_RATING + T_TEXT + ","
					+ COLUMN_PLACE_EMAIL + T_TEXT + ","
					+ COLUMN_PLACE_WEBSITE + T_TEXT + "," 
					+ COLUMN_PLACE_CATEGORY + T_TEXT + "," 
					+ COLUMN_PLACE_ARR_PHOTO + T_TEXT + ","
					+ COLUMN_PLACE_REVIEW_JSON + T_TEXT + ");";
		}

		/**
		 * build a sql string to update table MySongList
		 */
		public static String updateTable() {
			return "DROP TABLE IF EXISTS " + TABLE;
		}
	}
	
	/**
	 * 
	 * @author ThanBanh
	 *
	 */
	public static final class SearchHistory {
		public static final String TABLE = "tbl_search_history";
		public static final String COLUMN_TEXT_ID = "c_search_id";
		public static final String COLUMN_TEXT_STRING = "c_search_string";

		/**
		 * build a sql string to create table MySongList
		 */
		public static String createTable() {
			return CREATE_TABLE + TABLE + " (" + COLUMN_TEXT_ID
					+ T_INTEGER + T_PRIMARY_KEY_AUTOINCREMENT + ","
					+ COLUMN_TEXT_STRING + T_TEXT + ");";
		}

		/**
		 * build a sql string to update table MySongList
		 */
		public static String updateTable() {
			return "DROP TABLE IF EXISTS " + TABLE;
		}
	}

}

