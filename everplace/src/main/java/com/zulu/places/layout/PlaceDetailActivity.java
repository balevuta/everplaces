package com.zulu.places.layout;

import java.io.IOException;
import java.util.ArrayList;

import zulu.app.libraries.dynamicshare.DynamicShareActionProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zulu.places.R;
import com.zulu.places.abstracts.AbstractActivity;
import com.zulu.places.adapter.ListReviewAdapter;
import com.zulu.places.adapter.PlacePhotoAdapter;
import com.zulu.places.database.DatabaseManager;
import com.zulu.places.database.FavoriteTableHelper;
import com.zulu.places.database.HistoryTableHelper;
import com.zulu.places.model.PlaceDetail;
import com.zulu.places.service.GetPlacesTask;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;
import com.zulu.places.utils.PreferenceUtils;
import com.zulu.places.widget.CustomAlertDialog;

public class PlaceDetailActivity extends AbstractActivity implements OnClickListener, OnTouchListener {

	private TextView mTVRating, mTVPlaceAddress, mTVNoOfReviews, mTVWebsite, mTVEmail;
	private LinearLayout mLineWebsite, mLineEmail;
	private LinearLayout mBtnPhone, mBtnFavourite, mBtnDirection, mBtnControl;
	
	private LinearLayout mBoxTextInfo, mBoxMapPreview;
	
	private RatingBar mRatingBar;
	
	private TextView mTxtPhone, mTxtFavourite, mTxtMapControl;
	private ImageView mImgPhone, mImgFavourite, mImgMapControl;
	private boolean mapExpanded = false;
	
	private ViewPager mPagerPhotos;
	private PlacePhotoAdapter mPhotosAdapter;
	
	private GoogleMap mMap;
	
	// database variables
	private FavoriteTableHelper mTableHelperFavorite;
	private HistoryTableHelper mTableHelperHistory;
	private PlaceDetail mPlaceModel;
	
	// first toturial
	private boolean isFirst;
	private RelativeLayout mBoxTotuDetail;
	private final int DEPLAY_SHOW_TOTURIAL = 1000;
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		removeFirstToturialView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
			getMenuInflater().inflate(R.menu.place_detail_icon, menu);
		} else {
			getMenuInflater().inflate(R.menu.place_detail, menu);
		}
		
		MenuItem itemShare = menu.findItem(R.id.action_share);
		
		DynamicShareActionProvider provider = (DynamicShareActionProvider) itemShare.getActionProvider();
        provider.setShareDataType("text/plain");
        provider.setOnShareIntentUpdateListener(new DynamicShareActionProvider.OnShareIntentUpdateListener() {

            @Override
            public Bundle onShareIntentExtrasUpdate() {
                Bundle extras = new Bundle();
                extras.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.text_place_sharing) + " " + 
                		mPlaceModel.getName() + "; " + mPlaceModel.getAddress());
                extras.putString(Intent.EXTRA_SUBJECT, getResources().getString(R.string.text_subject_sharing));
                return extras;
            }

        });
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			GeneralUtils.finishActivityWithAnim(mContext);
			break;

//		case R.id.action_share:
//			showShareDialog();
//			break;
			
		case R.id.help:
			GeneralUtils.startHelpActivity(mContext);
			break;
		default:
			break;
		}
		removeFirstToturialView();
		return super.onOptionsItemSelected(item);
	}
	
	private void showShareDialog() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.text_place_sharing) + " " + 
        		mPlaceModel.getName() + "; " + mPlaceModel.getAddress());
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.text_subject_sharing));

        try {
        	startActivity(Intent.createChooser(intent, getResources().getString(R.string.action_share)));
        	overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out_half);
        } catch (ActivityNotFoundException ex) {
        	 		
        }
	}
	
	/**
	 * check show first toturial for this screen
	 */
	private void checkShowFirstToturial() {
		isFirst = PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_PLACE_DETAIL, true);
		if(isFirst) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mBoxTotuDetail.setVisibility(View.VISIBLE);
					Animation startAnim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
					mBoxTotuDetail.startAnimation(startAnim);
				}
			}, DEPLAY_SHOW_TOTURIAL);
		}
	}
	
	/**
	 * remove first toturial screen
	 */
	private void removeFirstToturialView() {
		if(PreferenceUtils.getBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_PLACE_DETAIL, true)) {
			PreferenceUtils.saveBoolean(mContext, Constants.PrefenceKey.PREF_FIRST_PLACE_DETAIL, false);
			mBoxTotuDetail.setVisibility(View.GONE);
			Animation startAnim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
			mBoxTotuDetail.startAnimation(startAnim);
		}
	}
	
	/**
	 * 
	 */
	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		Drawable myBg = getResources().getDrawable(R.drawable.transp_black_25);
		getActionBar().setBackgroundDrawable(myBg);
		getActionBar().setTitle(mPlaceModel.getName());
	}
	
	/**
	 * 
	 */
	private void initViewPagerPhotos() {
		if(mPlaceModel.getPhotoURL()!=null) {
			mPhotosAdapter = new PlacePhotoAdapter(mPlaceModel.getPhotoURL(), mContext);
			mPagerPhotos.setOffscreenPageLimit(3);
			mPagerPhotos.setAdapter(mPhotosAdapter);
		} else {
			mPagerPhotos.setVisibility(View.GONE);
//			mBtnControl.setEnabled(false);
//			mTxtMapControl.setVisibility(View.GONE);
//			mImgMapControl.setImageResource(R.drawable.icon_expand_disable);
			mBtnControl.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * @param bundle
	 */
	private void initFirstData() {
		
		mTableHelperFavorite = DatabaseManager.getInstance().getFavoriteTableHelper();
		mTableHelperHistory = DatabaseManager.getInstance().getHistoryTableHelper();
		try {
			mPlaceModel = (PlaceDetail) GeneralUtils.bytes2Object(getIntent().getExtras().getByteArray(Constants.PlaceDetails.PLACE_DETAIL_OBJECT));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		mTVPlaceAddress.setText(mPlaceModel.getAddress());
		mTVRating.setText(String.valueOf(mPlaceModel.getRating()));
		
		mRatingBar.setRating(Float.valueOf(String.valueOf(mPlaceModel.getRating())));
		
		mTVNoOfReviews.setText(Html.fromHtml(getResources().getString(R.string.text_no_of_reviews).
				replace("{0}", String.valueOf(mPlaceModel.getNoOfReviews()))));
		if(mPlaceModel.getNoOfReviews()>0) {
			mTVNoOfReviews.setTypeface(null, Typeface.BOLD);
			mTVNoOfReviews.setTextSize(15);
		}
		
		// Check website and email fields
		if(mPlaceModel.getWebsite()!=null) {
			mLineWebsite.setVisibility(View.VISIBLE);
			mTVWebsite.setText(Html.fromHtml(getUnderlineText(mPlaceModel.getWebsite())));
			mTVWebsite.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GeneralUtils.doWebsiteClick(mContext, mPlaceModel.getWebsite());
				}
			});
		} else {
			mLineWebsite.setVisibility(View.GONE);
		}
		if(mPlaceModel.getEmail()!=null) {
			mLineEmail.setVisibility(View.VISIBLE);
			mTVEmail.setText(Html.fromHtml(getUnderlineText(mPlaceModel.getEmail())));
			mTVEmail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GeneralUtils.doEmailClick(mContext, mPlaceModel.getEmail());
				}
			});
		} else {
			mLineEmail.setVisibility(View.GONE);
		}
		
		if(mPlaceModel.getPhone()!=null) {
			mImgPhone.setImageResource(R.drawable.icon_phone_enable);
			mTxtPhone.setText(mPlaceModel.getPhone());
			mBtnPhone.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GeneralUtils.doCallPhoneClick(mContext, mPlaceModel.getPhone());
				}
			});
		} else {
			mBtnPhone.setVisibility(View.GONE);
		}
		
		parseSaveArrPhotoToDB();
	}
	
	/**
	 * save array photo to DB
	 */
	private void parseSaveArrPhotoToDB() {
		StringBuffer buffer = new StringBuffer();
		if(mPlaceModel.getPhotoURL()!=null) {
			for (String photoURL : mPlaceModel.getPhotoURL()) {
				buffer.append(photoURL + "<;>");
			}
			mPlaceModel.setArrPhotoDB(buffer.toString());
			Log.i("PlaceDetail", buffer.toString());
		}
	}
	
	/**
	 * 
	 */
	private void displayMap() {
		Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(mPlaceModel.getLatitude(), mPlaceModel.getLongitude()))
				.title(mPlaceModel.getName())
				.snippet(mTVPlaceAddress.getText().toString()));
				// .rotation(mOrientation)
				// .icon(BitmapDescriptorFactory
				// .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//				.icon(BitmapDescriptorFactory
//						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		
		GetPlacesTask.drawMarker(marker, mPlaceModel.getCategory());
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(mPlaceModel.getLatitude(), mPlaceModel.getLongitude())) // Sets the center of
											// the map to
				// Mountain View
				.zoom(15) // Sets the zoom
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	private String getUnderlineText(String text) {
		String str="<html><body><u>" + text + "</u></body></html>";
		return str;
	}
	
	private void controlPlaceDetailMap() {
		if(!mapExpanded) {
			mBoxTextInfo.setVisibility(View.GONE);
			mPagerPhotos.setVisibility(View.GONE);
			
			GeneralUtils.startExpandFromTopHalfAnimation(mBoxMapPreview);
			GeneralUtils.startCollapseFromBottomAnimation(mBoxTextInfo);
			GeneralUtils.startCollapseFromTopAnimation(mPagerPhotos);
			
			mTxtMapControl.setText(getResources().getString(R.string.text_collapse_map));
			mImgMapControl.setImageResource(R.drawable.icon_detail_collapse_basic);
			mapExpanded = true;
		} else {
			mBoxTextInfo.setVisibility(View.VISIBLE);
			mPagerPhotos.setVisibility(View.VISIBLE);
			
			GeneralUtils.startCollapseFromBottomHalfAnimation(mBoxMapPreview);
			GeneralUtils.startExpandFromBottomAnimation(mPagerPhotos);
			GeneralUtils.startExpandFromTopAnimation(mBoxTextInfo);
			
			mTxtMapControl.setText(getResources().getString(R.string.text_expand_map));
			mImgMapControl.setImageResource(R.drawable.icon_detail_expand_basic);
			mapExpanded = false;
		}
	}
	
	/**
	 * check init favorite icon
	 */
	private void checkInitFavoriteIcon() {
		if(!isInFavorite()) {
			mImgFavourite.setImageResource(R.drawable.icon_favorites_empty);
			mTxtFavourite.setText(getResources().getString(R.string.text_not_favourite));
		} else {
			mImgFavourite.setImageResource(R.drawable.icon_favorites);
			mTxtFavourite.setText(getResources().getString(R.string.text_in_favourite));
		}
	}
	
	/**
	 * save or remove places in favorite
	 */
	private void saveOrRemovePlacesInFavorite() {
		if(!isInFavorite()) {
			boolean saveSucceed = mTableHelperFavorite.addPlace(mPlaceModel);
			GeneralUtils.showShortToast(mContext, saveSucceed ? getResources().getString(R.string.text_saved_favorite) : 
				getResources().getString(R.string.text_favorite_db_fail));
		} else {
			boolean removedSecceed = mTableHelperFavorite.deletePlace(mPlaceModel);
			GeneralUtils.showShortToast(mContext, removedSecceed ? getResources().getString(R.string.text_removed_favorite) : 
				getResources().getString(R.string.text_favorite_db_fail));
		}
		checkInitFavoriteIcon();
	}
	
	private void checkAndSavePlacesInHistory() {
		if(!isInHistory()) {
			boolean saveSucceed = mTableHelperHistory.addPlace(mPlaceModel);
			Log.i(DatabaseManager.TAG, saveSucceed ? "saved history succeed" : "save history failed");
		}
	}
	
	/**
	 * check if place in favorite database
	 * @return
	 */
	private boolean isInFavorite() {
		ArrayList<PlaceDetail> lstFavorite = mTableHelperFavorite.getAllFavoritePlaces();
		if(lstFavorite!=null && lstFavorite.size()>0) {
			for (PlaceDetail place : lstFavorite) {
				if(mPlaceModel.getLatitude()==place.getLatitude() 
						&& mPlaceModel.getLongitude()==place.getLongitude()) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	/**
	 * check if place in history database
	 * @return
	 */
	private boolean isInHistory() {
		ArrayList<PlaceDetail> lstHistory = mTableHelperHistory.getAllHistoryPlaces();
		if(lstHistory!=null && lstHistory.size()>0) {
			for (PlaceDetail place : lstHistory) {
				if(mPlaceModel.getLatitude()==place.getLatitude() 
						&& mPlaceModel.getLongitude()==place.getLongitude()) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	private void showListReviewDialog() {
		
		if(mPlaceModel.getLstReviews()!=null && mPlaceModel.getLstReviews().size() > 0) {

			LayoutInflater inflater = LayoutInflater.from(mContext);
			View customLayout = inflater.inflate(R.layout.dialog_reviews_list, null);
			
			ListView lstViewReviews = (ListView) customLayout.findViewById(R.id.lst_reviews);
			
			ListReviewAdapter reviewAdapter = new ListReviewAdapter(mContext, mPlaceModel.getLstReviews());
			lstViewReviews.setAdapter(reviewAdapter);
			
			int height = (int) getResources().getDimension(R.dimen.dialog_height_review); 
			
			if(reviewAdapter.getCount() > 3){
		        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		        lstViewReviews.setLayoutParams(params);
			} else {
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height*2/3);
				lstViewReviews.setLayoutParams(params);
			}
			
//			if(CategoryTableHelper.getAllCategoriesArr(mContext).size()>5) {
//				try {
//					View item = mAdapter.getView(0, null, lvCategories);
//			        item.measure(0, 0);         
//			        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (6 * item.getMeasuredHeight()));
//			        lvCategories.setLayoutParams(params);
//				} catch(Exception e) {
//					
//				}
//			} else {
//				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		        lvCategories.setLayoutParams(params);
//			}
			
			final CustomAlertDialog filterDg = new CustomAlertDialog(mContext, customLayout);
			filterDg.setTitle(mContext.getResources().getString(R.string.title_reviews_list));
			
			filterDg.setOnlyButton(getResources().getString(R.string.btn_cancel), new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					filterDg.hideDialog();
				}
			});
			
			filterDg.showDialog();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_detail_directions:
			GeneralUtils.showDirectionOpionDialog(mContext, mPlaceModel.getName(), mTVPlaceAddress.getText().toString(), 
					mPlaceModel.getLatitude(), mPlaceModel.getLongitude(), mPlaceModel.getCategory());
			break;
		case R.id.btn_detail_map_control:
			controlPlaceDetailMap();
			break;
		case R.id.txt_detail_number_rating:
			showListReviewDialog();
			break;
		case R.id.btn_detail_favourite:
			saveOrRemovePlacesInFavorite();
			break;

		default:
			break;
		}
		
		removeFirstToturialView();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.equals(mBoxTotuDetail) && mBoxTotuDetail.getVisibility() == View.VISIBLE) {
			removeFirstToturialView();
		}
		return false;
	}

	@Override
	public int getContentViewId() {
		return R.layout.activity_place_detail;
	}

	@Override
	public void initView() {
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_detail)).getMap();

		mTVPlaceAddress = (TextView) findViewById(R.id.txt_detail_address);
		mTVRating = (TextView) findViewById(R.id.txt_detail_rating);
		mTVNoOfReviews = (TextView) findViewById(R.id.txt_detail_number_rating);
		mTVNoOfReviews.setOnClickListener(this);
		
		mTVWebsite = (TextView) findViewById(R.id.txt_detail_website);
		mTVEmail = (TextView) findViewById(R.id.txt_detail_email);
		mPagerPhotos = (ViewPager) findViewById(R.id.pager_place_photos);
		
		mLineWebsite = (LinearLayout) findViewById(R.id.line_detail_website);
		mLineEmail = (LinearLayout) findViewById(R.id.line_detail_email);
		
		mBoxTextInfo = (LinearLayout) findViewById(R.id.box_detail_text_info);
		mBoxMapPreview = (LinearLayout) findViewById(R.id.box_detail_map_review);
		
		mBtnPhone = (LinearLayout) findViewById(R.id.btn_detail_phone);
		mBtnFavourite = (LinearLayout) findViewById(R.id.btn_detail_favourite);
		mBtnDirection = (LinearLayout) findViewById(R.id.btn_detail_directions);
		mBtnControl = (LinearLayout) findViewById(R.id.btn_detail_map_control);
		mBtnDirection.setOnClickListener(this);
		mBtnControl.setOnClickListener(this);
		mBtnFavourite.setOnClickListener(this);
		
		
		mImgPhone = (ImageView) findViewById(R.id.img_detail_phone);
		mImgFavourite = (ImageView) findViewById(R.id.img_detail_favourite);
		mImgMapControl = (ImageView) findViewById(R.id.img_detail_map_control);
		
		mTxtPhone = (TextView) findViewById(R.id.txt_detail_phone);
		mTxtFavourite = (TextView) findViewById(R.id.txt_detail_favourite);
		mTxtMapControl = (TextView) findViewById(R.id.txt_detail_map_control);

		mTVEmail.setText(Html.fromHtml(getUnderlineText("banhlevuthan@gmail.com")));
		mTVWebsite.setText(Html.fromHtml(getUnderlineText("https://www.facebook.com/ZuluSolutions")));
		
		mRatingBar = (RatingBar) findViewById(R.id.rating_bar);
		LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.DarkGreen), 
				PorterDuff.Mode.SRC_ATOP);
		
		mBoxTotuDetail = (RelativeLayout) findViewById(R.id.box_toturial_place_detail);
		mBoxTotuDetail.setOnTouchListener(this);
		
		initFirstData();
		initActionBar();
		initViewPagerPhotos();
		displayMap();
		checkAndSavePlacesInHistory();
		checkInitFavoriteIcon();
		checkShowFirstToturial();
	}

}
