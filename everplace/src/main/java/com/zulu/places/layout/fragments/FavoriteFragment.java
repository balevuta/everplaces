package com.zulu.places.layout.fragments;

import java.io.IOException;
import java.util.ArrayList;

import zulu.app.libraries.supertoasts.SuperActivityToast;
import zulu.app.libraries.supertoasts.SuperToast;
import zulu.app.libraries.supertoasts.util.OnClickWrapper;
import zulu.app.libraries.supertoasts.util.OnDismissWrapper;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zulu.places.R;
import com.zulu.places.adapter.ListFavoriteHistoryAdapter;
import com.zulu.places.database.DatabaseManager;
import com.zulu.places.database.FavoriteTableHelper;
import com.zulu.places.extras.SwipeDismissListViewTouchListener;
import com.zulu.places.extras.SwipeDismissListViewTouchListener.DismissCallbacks;
import com.zulu.places.layout.PlaceDetailActivity;
import com.zulu.places.model.PlaceDetail;
import com.zulu.places.model.UserSetting;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;
import com.zulu.places.utils.PreferenceUtils;
import com.zulu.places.widget.CustomAlertDialog;

public class FavoriteFragment extends Fragment implements OnTouchListener {

	private ListView mLstViewFavorite;
	private ListFavoriteHistoryAdapter mFavoriteAdapter;
	private Context mContext;
	private FavoriteTableHelper mTableHelperPlaces;
	private ArrayList<PlaceDetail> lstModels;
	
	// first toturial
	private RelativeLayout mBoxToturial;
	private boolean isFirst;
	private final int DEPLAY_SHOW_TOTURIAL = 500;
	
	private int currPos = 0;
	private boolean clickUndo = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_favorite, null);
		
		initActionBar();
		initComponant(view);
		
		return view;
	}
	
	@Override
	public void onResume() {
		initData(true);
		checkShowFirstToturial();
		super.onResume();
	}
	
	private void checkShowFirstToturial() {
		isFirst = PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_SWIPE_DISMISS, true)
				&& (lstModels != null);
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
	
	/**
	 * remove first toturial screen
	 */
	private void removeFirstToturialView() {
		if(PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_SWIPE_DISMISS, true)) {
			PreferenceUtils.saveBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_SWIPE_DISMISS, false);
			mBoxToturial.setVisibility(View.GONE);
			Animation startAnim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
			mBoxToturial.startAnimation(startAnim);
		}
	}
	
	private void initComponant(View view) {
		mContext = getActivity();
		mLstViewFavorite = (ListView) view.findViewById(R.id.lst_favorite);
		
		mTableHelperPlaces = DatabaseManager.getInstance().getFavoriteTableHelper();
		
		mBoxToturial = (RelativeLayout) view.findViewById(R.id.box_toturial_favorite);
		mBoxToturial.setOnTouchListener(this);
	}

	private void initActionBar() {
		getActivity().getActionBar().setTitle(R.string.title_favorite);
		setHasOptionsMenu(true);
	}
	
	private void initData(boolean isAnim) {
		lstModels = new ArrayList<PlaceDetail>();
		lstModels = mTableHelperPlaces.getAllFavoritePlaces();
		
		mFavoriteAdapter = new ListFavoriteHistoryAdapter(mContext, lstModels);
		
		mLstViewFavorite.setAdapter(mFavoriteAdapter);
		
		if(isAnim) {
			GeneralUtils.setViewApprearAnim(mLstViewFavorite, mContext);
		}
		
		initListViewOnClick();
		
		initListViewSwipeDismiss();
	}
	
	/**
	 * set on list view item click listener
	 * @param lstModels list favorite
	 */
	private void initListViewOnClick() {
		mLstViewFavorite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
//				GeneralUtils.showShortToast(mContext, lstModels.get(pos).getCategory());
				if(UserSetting.getInstance().isConnectInternet) {
					startPlaceDetailActivity(pos);
				} else {
					GeneralUtils.showLongToast(mContext, 
							getResources().getString(R.string.msg_no_internet_access));
				}
			}
		});
	}
	
	private void startPlaceDetailActivity(int pos) {
		Intent intent = new Intent(mContext, PlaceDetailActivity.class);
		PlaceDetail result = lstModels.get(pos);
		
		try {
			intent.putExtra(Constants.PlaceDetails.PLACE_DETAIL_OBJECT, GeneralUtils.object2Bytes(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeneralUtils.gotoActivityWithAnim(mContext, intent);
	}
	
	/**
	 * init ListView swipe dismiss
	 * @param lstModels
	 */
	private void initListViewSwipeDismiss() {
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(mLstViewFavorite, new DismissCallbacks() {
			
			@Override
			public void onDismiss(ListView c, int[] reverseSortedPositions) {
				for (int position : reverseSortedPositions) {
					currPos = position;
					lstModels.remove(position);
                }
				showUndoSuperToast();
				mFavoriteAdapter.notifyDataSetChanged();
			}
			
			@Override
			public boolean canDismiss(int position) {
				return true;
			}
		});
		
		mLstViewFavorite.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
		mLstViewFavorite.setOnScrollListener(touchListener.makeScrollListener());
	}
	
	private void showUndoSuperToast() {
		
		SuperActivityToast undoToast = new SuperActivityToast(getActivity(), SuperToast.Type.BUTTON);
		undoToast.setText(mContext.getString(R.string.msg_done_for_now));
		undoToast.setAnimations(SuperToast.Animations.SCALE);
		undoToast.setBackground(SuperToast.Background.BLUE);
		undoToast.setTextSize(SuperToast.TextSize.MEDIUM);
		undoToast.setDuration(SuperToast.Duration.LONG);
		undoToast.setOnDismissWrapper(onDismissWrapper);
		undoToast.setOnClickWrapper(onClickUndo);
		undoToast.show();
	}
	
	private OnDismissWrapper onDismissWrapper = new OnDismissWrapper("ondismisswrapper_one", new SuperToast.OnDismissListener() {

        @Override
        public void onDismiss(View view) {

        	if(!clickUndo) {
        		lstModels.clear();
            	lstModels = mTableHelperPlaces.getAllFavoritePlaces();
	        	mTableHelperPlaces.deletePlace(lstModels.get(currPos));
				initData(false);
        	}
        	clickUndo = false;
        }
    });
	
	private OnClickWrapper onClickUndo = new OnClickWrapper("onclickwrapper_one", new SuperToast.OnClickListener() {

        @Override
        public void onClick(View v, Parcelable token) {
        	initData(false);
        	clickUndo = true;
        }

    });
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if(ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
			inflater.inflate(R.menu.list_db_icon, menu);
		} else {
			inflater.inflate(R.menu.list_db, menu);
		}
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.action_clear_all && lstModels!=null) {
			showDialogClearAllConfirm();
		}
		
		if(isFirst) {
			removeFirstToturialView();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * show dialog clear all confirm
	 */
	private void showDialogClearAllConfirm() {
		final CustomAlertDialog dialog = new CustomAlertDialog(mContext, 
				getResources().getString(R.string.msg_clear_confirm));
		dialog.setLeftButton(getResources().getString(R.string.btn_ok), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTableHelperPlaces.deleteAllPlaces();
				initData(true);
				dialog.hideDialog();
			}
		});
		
		dialog.setRightButton(getResources().getString(R.string.btn_cancel), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.hideDialog();
			}
		});
		
		dialog.setTitle(getResources().getString(R.string.action_clear_all));
		
		dialog.showDialog();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.equals(mBoxToturial) && mBoxToturial.getVisibility()==View.VISIBLE) {
			removeFirstToturialView();
		}
		return false;
	}
}
