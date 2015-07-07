package com.zulu.places.database;


public class DatabaseManager {
	
	public static final String TAG = "ZuluDatabase";
	
	private DatabaseHelper mDbHelper;
	private FavoriteTableHelper instancePlacesListHelper;
	private HistoryTableHelper instanceHistoryHelper;
	private SearchHistoryTableHelper instanceSearchHelper;
	
	private static DatabaseManager instance = null;
//	private static Context mContext = null;

//	public static synchronized DatabaseManager getInstance(Context context) {
//		if (instance == null) {
//			instance = new DatabaseManager(context);
//			mContext = context;
//		}
//		mContext = context;
//		return instance;
//	}
	
	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
//			mContext = context;
		}
//		mContext = context;
		return instance;
	}
	
//	public static synchronized DatabaseManager getInstance() {
//		if (instance == null) {
//			instance = new DatabaseManager();
//			mContext = context;
//		}
//		mContext = context;
//		return instance;
//	}

//	private DatabaseManager(Context context) {
//		this.mDbHelper = new DatabaseHelper(context);
//	}
	
	private DatabaseManager() {
		this.mDbHelper = new DatabaseHelper();
	}

	/**
	 * 
	 * @return
	 */
	public FavoriteTableHelper getFavoriteTableHelper() {
		if (instancePlacesListHelper == null)
			instancePlacesListHelper = new FavoriteTableHelper(mDbHelper);
		return instancePlacesListHelper;
	}
	
	/**
	 * 
	 * @return
	 */
	public HistoryTableHelper getHistoryTableHelper() {
		if (instanceHistoryHelper== null)
			instanceHistoryHelper = new HistoryTableHelper(mDbHelper);
		return instanceHistoryHelper;
	}
	
	/**
	 * 
	 * @return
	 */
	public SearchHistoryTableHelper getSearchTableHelper() {
		if (instanceSearchHelper== null)
			instanceSearchHelper = new SearchHistoryTableHelper(mDbHelper);
		return instanceSearchHelper;
	}
	
}
