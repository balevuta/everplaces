package com.zulu.places.layout;

import zulu.app.libraries.ldrawer.ActionBarDrawerToggle;
import zulu.app.libraries.ldrawer.DrawerArrowDrawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.support.v4.util.LruCache;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.zulu.places.R;
import com.zulu.places.adapter.CacheImage;
import com.zulu.places.adapter.MySuggestionProvider;
import com.zulu.places.layout.fragments.AboutUsFragment;
import com.zulu.places.layout.fragments.CategoryFragment;
import com.zulu.places.layout.fragments.FavoriteFragment;
import com.zulu.places.layout.fragments.HistoryFragment;
import com.zulu.places.utils.ConnectionUtils;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.FontSetting;
import com.zulu.places.utils.GeneralUtils;

public class DrawerActivity extends Activity implements OnClickListener {

	private Context mContext;
	
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerArrowDrawable drawerArrow;
	private String title;
	
	// side menu
	private TextView mSideCategory, mSideHistory, mSideFavorite, mTxtVersion;
	private LinearLayout mSideBtnAbout, mSideBtnHelp;
	
	// Image cache
	public static CacheImage mCache;
	private LruCache<String, Bitmap> mMemoryCache;
	
	private boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		mContext = this;
		
		checkConnectionStatus();
		
		initActionBar();
		
		setUpDrawerLayout();
		
		showFragmentCategory();
		
		initImageCatchMem();
		
		// Admob
		ViewGroup ad = (ViewGroup) findViewById(R.id.lnAdmob);
		GeneralUtils.addAdMod(this, ad);
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    	String query = intent.getStringExtra(SearchManager.QUERY);
	    	
	    	SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
	                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
	    	suggestions.saveRecentQuery(query, null);
	    	
	    	startMapScreenWithTextSearch(query);
	    }
	}
	
	private void startMapScreenWithTextSearch(String text) {
		if(text!=null) {
			if(GeneralUtils.checkPlayServices(this)) {
				Intent intent = new Intent(mContext, MapActivity.class);
				intent.putExtra(Constants.MapScreenMode.MAP_MODE, Constants.MapScreenMode.MAP_MODE_TEXT);
				intent.putExtra(Constants.MapScreenMode.TEXT_SEARCH, text.replaceAll(" ", "+"));
				intent.putExtra(Constants.PlaceTypes.PLACE_TITLE, text);
				GeneralUtils.gotoActivityWithAnim(mContext, intent);
			}
		} else {
			GeneralUtils.showShortToast(mContext, getString(R.string.msg_error_not_sure));
		}
	}
	
	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_back_again_to_exit));
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}
	
	@Override
	protected void onDestroy() {
		ConnectionUtils.unregisterCheckStateInternetConnection(mContext);
		
		if (mCache != null && mCache.getmMemoryCache() != null) {
			Log.i("Before Eviction -- Cache's Size : ", mCache
					.getmMemoryCache().size() + "");
			mCache.getmMemoryCache().evictAll();
			Log.i("Cache's Size : ", mCache.getmMemoryCache()
					.size() + "");
		}
		
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	/**
	 * init Image Catch Memory
	 */
	private void initImageCatchMem() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;
		mCache = new CacheImage();
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};

		mCache.setmMemoryCache(mMemoryCache);
	}
	
	/**
	 * 
	 */
	private void checkConnectionStatus() {
		ConnectionUtils.checkConnectInternet(mContext);
		ConnectionUtils.registerCheckStateInternetConnection(mContext);
	}
	
	/**
	 * 
	 */
	private void setUpDrawerLayout() {
		title = getActionBar().getTitle().toString();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mSideCategory = (TextView) findViewById(R.id.txt_side_menu_category);
		mSideHistory = (TextView) findViewById(R.id.txt_side_menu_history);
		mSideFavorite = (TextView) findViewById(R.id.txt_side_menu_favorite);
		mTxtVersion = (TextView) findViewById(R.id.txt_app_version);
		mTxtVersion.setText(getString(R.string.text_app_version)
				.replace("{0}", GeneralUtils.getAppVersionName(mContext)));

		mSideBtnAbout = (LinearLayout) findViewById(R.id.btn_side_about);
		mSideBtnHelp = (LinearLayout) findViewById(R.id.btn_side_help);
		
		mSideCategory.setOnClickListener(this);
		mSideHistory.setOnClickListener(this);
		mSideFavorite.setOnClickListener(this);
		
		mSideBtnAbout.setOnClickListener(this);
		mSideBtnHelp.setOnClickListener(this);
		
		drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, drawerArrow,
				R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */

			public void onDrawerClosed(View view) {
				getActionBar().setTitle(title);
			}

			/** Called when a drawer has settled in a completely open state. */

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(R.string.drawer_open);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.app_title);
//		Drawable myBg = getResources().getDrawable(R.drawable.action_bar);
//		getActionBar().setBackgroundDrawable(myBg);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if(ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
			inflater.inflate(R.menu.main_category_menu_icon, menu);
		} else {
			inflater.inflate(R.menu.main_category_menu, menu);
		}
		
	    // Associate searchable configuration with the SearchView
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		switch (item.getItemId()) {
		case R.id.help:
			GeneralUtils.startHelpActivity(mContext);
			break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 */
	private void showFragmentAboutUs() {
		Fragment fragment = new AboutUsFragment();
		
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
//				.setCustomAnimations(R.anim.zoom_in_half, R.anim.zoom_out_half)
				.replace(R.id.content_frame, fragment)
				.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit();
		title = getResources().getString(R.string.title_about_us);
	}
	
	/**
	 * 
	 */
	private void showFragmentFavorite() {
		Fragment fragment = new FavoriteFragment();
//		Bundle args = new Bundle();
//		args.putInt(OpertingSystemFragment.ARG_OS, position);
//		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
//				.setCustomAnimations(R.anim.zoom_in_half, R.anim.zoom_out_half)
				.replace(R.id.content_frame, fragment)
				.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit();
		title = getResources().getString(R.string.title_favorite);
		
		mSideCategory.setTypeface(FontSetting.getInstance().getTypeface(mContext));
		mSideFavorite.setTypeface(FontSetting.getInstance().getBoldTypeface(mContext));
		mSideHistory.setTypeface(FontSetting.getInstance().getTypeface(mContext));
	}
	
	/**
	 * 
	 */
	private void showFragmentHistory() {
		Fragment fragment = new HistoryFragment();
//		Bundle args = new Bundle();
//		args.putInt(OpertingSystemFragment.ARG_OS, position);
//		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
//				.setCustomAnimations(R.anim.zoom_in_half, R.anim.zoom_out_half)
				.replace(R.id.content_frame, fragment)
				.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit();
		title = getResources().getString(R.string.title_history);
		
		mSideCategory.setTypeface(FontSetting.getInstance().getTypeface(mContext));
		mSideFavorite.setTypeface(FontSetting.getInstance().getTypeface(mContext));
		mSideHistory.setTypeface(FontSetting.getInstance().getBoldTypeface(mContext));
	}
	
	private void showFragmentCategory() {
		Fragment fragment = new CategoryFragment();
//		Bundle args = new Bundle();
//		args.putInt(OpertingSystemFragment.ARG_OS, position);
//		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit();
		title = getResources().getString(R.string.app_title);
		
		mSideCategory.setTypeface(FontSetting.getInstance().getBoldTypeface(mContext));
		mSideFavorite.setTypeface(FontSetting.getInstance().getTypeface(mContext));
		mSideHistory.setTypeface(FontSetting.getInstance().getTypeface(mContext));
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.txt_side_menu_category:
			showFragmentCategory();
			break;
		case R.id.txt_side_menu_history:
			showFragmentHistory();
			break;
		case R.id.txt_side_menu_favorite:
			showFragmentFavorite();
			break;
		case R.id.btn_side_about:
			showFragmentAboutUs();
			break;
		case R.id.btn_side_help:
			GeneralUtils.startHelpActivity(mContext);
			break;

		default:
			break;
		}
		
		mDrawerLayout.closeDrawers();
	}

}
