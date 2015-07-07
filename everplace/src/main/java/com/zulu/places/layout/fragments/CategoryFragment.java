package com.zulu.places.layout.fragments;

import java.util.Set;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.gc.materialdesign.views.LayoutRipple;
import com.nineoldandroids.view.ViewHelper;
import com.zulu.places.R;
import com.zulu.places.layout.MapActivity;
import com.zulu.places.model.UserSetting;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;

public class CategoryFragment extends Fragment implements OnClickListener {

	private LayoutRipple mBtnRestaurant, mBtnCafe, mBtnZoo, mBtnShopping, mBtnFood, mBtnPark, mBtnBar, 
			mBtnSuperMarket, mBtnMuseum, mBtnBeautySalon, mBtnNightClub, mBtnMovieTheater,
			mBtnSchool, mBtnUniversity, mbtnStadium, mBtnGym, 
			mBtnPostOffice, mBtnBank, mBtnHospital, mBtnPharmacy, mBtnBusStation, mBtnGasStation;
	
	private TableLayout mTableCategories;
	
	private LinearLayout 
			mBoxPark, mBoxShoping, 
			mBoxNight, mBoxMarket;
//			mBoxBank, mBoxBus, mBoxSchool, mBoxStadium, mBoxHealth;
	
	private Set<String> mSetCateResult;
	
	private String mPrefCateType = "";
	
	private Context mContext;
	
	private View fragmentView;
	
	private SharedPreferences settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mContext = getActivity();
		
		getSettingValue();
		
//		if(mPrefCateType.equals(Constants.PrefCateTypes.METRO)) {
//			fragmentView = inflater.inflate(R.layout.fragment_category_metro, null);
//		} else if (mPrefCateType.equals(Constants.PrefCateTypes.LIST)) {
//			fragmentView = inflater.inflate(R.layout.fragment_category_list, null);
//		}
		
		fragmentView = inflater.inflate(R.layout.fragment_category_table, null);
		
		initActionBar();
		
		initView(fragmentView);
		
//		applyCateHomeDisplay();
		
		return fragmentView;
	}
	
	private void getSettingValue() {
		
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		mSetCateResult = settings.getStringSet(Constants.PlaceHomeDisplayTypes.KEY_SETTING, null);
		mPrefCateType = settings.getString(Constants.PrefCateTypes.KEY_TYPE, Constants.PrefCateTypes.LIST);
	}
	
	private void applyCateHomeDisplay() {
		
		if(mSetCateResult!=null) {
			clearAllItems();
			scanHomeItems(mSetCateResult);
		}
	}
	
	/**
	 * scan items for display in home screen
	 * @param result
	 */
	private void scanHomeItems(Set<String> result) {
		for (String item : result) {
			checkHomeItem(item);
		}
	}
	
	/**
	 * check and show items
	 * @param item
	 */
	private void checkHomeItem(String item) {
		
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_RES)) {
			mBtnRestaurant.setVisibility(View.VISIBLE);
		}
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_PARK)) {
			if(mPrefCateType.equals(Constants.PrefCateTypes.METRO)) {
				mBoxPark.setVisibility(View.VISIBLE);
			} else if (mPrefCateType.equals(Constants.PrefCateTypes.LIST)) {
				mBtnZoo.setVisibility(View.VISIBLE);
				mBtnPark.setVisibility(View.VISIBLE);
			}
		}
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_CAFE)) {
			mBtnCafe.setVisibility(View.VISIBLE);
		}
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_SHOPPING)) {
			if(mPrefCateType.equals(Constants.PrefCateTypes.METRO)) {
				mBoxShoping.setVisibility(View.VISIBLE);
			} else if (mPrefCateType.equals(Constants.PrefCateTypes.LIST)) {
				mBtnShopping.setVisibility(View.VISIBLE);
				mBtnBar.setVisibility(View.VISIBLE);
			}
		}
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_FOOD)) {
			mBtnFood.setVisibility(View.VISIBLE);
		}
//		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_BANK)) {
//			mBoxBank.setVisibility(View.VISIBLE);
//		}
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_MARKET)) {
			if(mPrefCateType.equals(Constants.PrefCateTypes.METRO)) {
				mBoxMarket.setVisibility(View.VISIBLE);
			} else if (mPrefCateType.equals(Constants.PrefCateTypes.LIST)) {
				mBtnSuperMarket.setVisibility(View.VISIBLE);
				mBtnMuseum.setVisibility(View.VISIBLE);
			}
		}
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_BEAUTY)) {
			mBtnBeautySalon.setVisibility(View.VISIBLE);
		}
//		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_BUS)) {
//			mBoxBus.setVisibility(View.VISIBLE);
//		}
		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_NIGHT)) {
			if(mPrefCateType.equals(Constants.PrefCateTypes.METRO)) {
				mBoxNight.setVisibility(View.VISIBLE);
			} else if (mPrefCateType.equals(Constants.PrefCateTypes.LIST)) {
				mBtnMovieTheater.setVisibility(View.VISIBLE);
				mBtnNightClub.setVisibility(View.VISIBLE);
			}
		}
//		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_HEALTH)) {
//			mBoxHealth.setVisibility(View.VISIBLE);
//		}
//		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_SCHOOL)) {
//			mBoxSchool.setVisibility(View.VISIBLE);
//		}
//		if(item.equals(Constants.PlaceHomeDisplayTypes.TYPE_STADIUM)) {
//			mBoxStadium.setVisibility(View.VISIBLE);
//		}
	}
	
	/**
	 * clear all home item categories
	 */
	private void clearAllItems() {
		mBtnRestaurant.setVisibility(View.GONE);
		mBtnCafe.setVisibility(View.GONE);
		mBtnFood.setVisibility(View.GONE);
//		mBoxBank.setVisibility(View.GONE);
		mBtnBeautySalon.setVisibility(View.GONE);
//		mBoxBus.setVisibility(View.GONE);
//		mBoxHealth.setVisibility(View.GONE);
//		mBoxSchool.setVisibility(View.GONE);
//		mBoxStadium.setVisibility(View.GONE);
		
		if(mPrefCateType.equals(Constants.PrefCateTypes.METRO)) {
			mBoxPark.setVisibility(View.GONE);
			mBoxShoping.setVisibility(View.GONE);
			mBoxMarket.setVisibility(View.GONE);
			mBoxNight.setVisibility(View.GONE);
		} else if (mPrefCateType.equals(Constants.PrefCateTypes.LIST)) {
			mBtnZoo.setVisibility(View.GONE);
			mBtnPark.setVisibility(View.GONE);
			mBtnShopping.setVisibility(View.GONE);
			mBtnBar.setVisibility(View.GONE);
			mBtnSuperMarket.setVisibility(View.GONE);
			mBtnMuseum.setVisibility(View.GONE);
			mBtnMovieTheater.setVisibility(View.GONE);
			mBtnNightClub.setVisibility(View.GONE);
		}
	}

	private void initView(View view) {
		
		mTableCategories = (TableLayout) view.findViewById(R.id.table_categories);
		GeneralUtils.setViewApprearAnim(mTableCategories, mContext);
		
		mBtnRestaurant = (LayoutRipple) view.findViewById(R.id.btn_restaurant);
		mBtnCafe = (LayoutRipple) view.findViewById(R.id.btn_cafe);
		mBtnZoo = (LayoutRipple) view.findViewById(R.id.btn_zoo);
		mBtnShopping = (LayoutRipple) view.findViewById(R.id.btn_shopping);
		mBtnFood = (LayoutRipple) view.findViewById(R.id.btn_food);
		mBtnPark = (LayoutRipple) view.findViewById(R.id.btn_park);
		mBtnBar = (LayoutRipple) view.findViewById(R.id.btn_bar);
		mBtnPostOffice = (LayoutRipple) view.findViewById(R.id.btn_post_office);
		mBtnBank = (LayoutRipple) view.findViewById(R.id.btn_bank);
		mBtnHospital = (LayoutRipple) view.findViewById(R.id.btn_hospital);
		mBtnPharmacy = (LayoutRipple) view.findViewById(R.id.btn_pharmacy);
		
		mBtnSuperMarket = (LayoutRipple) view.findViewById(R.id.btn_super_market);
		mBtnMuseum = (LayoutRipple) view.findViewById(R.id.btn_museum);
		mBtnBeautySalon = (LayoutRipple) view.findViewById(R.id.btn_beauty_salon);
		mBtnBusStation = (LayoutRipple) view.findViewById(R.id.btn_bus_station);
		mBtnGasStation  = (LayoutRipple) view.findViewById(R.id.btn_gas_station);
		mBtnNightClub = (LayoutRipple) view.findViewById(R.id.btn_night_club);
		mBtnMovieTheater = (LayoutRipple) view.findViewById(R.id.btn_movie_theater);
		mBtnSchool = (LayoutRipple) view.findViewById(R.id.btn_school);
		mBtnUniversity = (LayoutRipple) view.findViewById(R.id.btn_university);
		mbtnStadium = (LayoutRipple) view.findViewById(R.id.btn_stadium);
		mBtnGym = (LayoutRipple) view.findViewById(R.id.btn_gym);
		
		setOriginRiple(mBtnRestaurant);
		setOriginRiple(mBtnCafe);
		setOriginRiple(mBtnZoo);
		setOriginRiple(mBtnShopping);
		setOriginRiple(mBtnFood);
		setOriginRiple(mBtnPark);
		setOriginRiple(mBtnBar);
		setOriginRiple(mBtnPostOffice);
		setOriginRiple(mBtnBank);
		setOriginRiple(mBtnHospital);
		setOriginRiple(mBtnPharmacy);
		setOriginRiple(mBtnSuperMarket);
		setOriginRiple(mBtnMuseum);
		setOriginRiple(mBtnBeautySalon);
		setOriginRiple(mBtnBusStation);
		setOriginRiple(mBtnGasStation);
		setOriginRiple(mBtnNightClub);
		setOriginRiple(mBtnMovieTheater);
		setOriginRiple(mBtnSchool);
		setOriginRiple(mBtnUniversity);
		setOriginRiple(mbtnStadium);
		setOriginRiple(mBtnGym);
		
		mBoxPark = (LinearLayout) view.findViewById(R.id.box_home_park);
		mBoxShoping = (LinearLayout) view.findViewById(R.id.box_home_shopping);
//		mBoxBank = (LinearLayout) view.findViewById(R.id.box_home_bank);
		mBoxMarket = (LinearLayout) view.findViewById(R.id.box_home_market);
//		mBoxBus = (LinearLayout) view.findViewById(R.id.box_home_bus);
		mBoxNight = (LinearLayout) view.findViewById(R.id.box_home_night);
//		mBoxHealth = (LinearLayout) view.findViewById(R.id.box_home_health);
//		mBoxSchool = (LinearLayout) view.findViewById(R.id.box_home_school);
//		mBoxStadium = (LinearLayout) view.findViewById(R.id.box_home_stadium);

		mBtnRestaurant.setOnClickListener(this);
		mBtnCafe.setOnClickListener(this);
		mBtnZoo.setOnClickListener(this);
		mBtnShopping.setOnClickListener(this);
		mBtnFood.setOnClickListener(this);
		mBtnPark.setOnClickListener(this);
		mBtnBar.setOnClickListener(this);
		mBtnPostOffice.setOnClickListener(this);
		mBtnBank.setOnClickListener(this);
		mBtnHospital.setOnClickListener(this);
		mBtnPharmacy.setOnClickListener(this);
		
		mBtnSuperMarket.setOnClickListener(this);
		mBtnMuseum.setOnClickListener(this);
		mBtnBeautySalon.setOnClickListener(this);
		mBtnBusStation.setOnClickListener(this);
		mBtnGasStation.setOnClickListener(this);
		mBtnNightClub.setOnClickListener(this);
		mBtnMovieTheater.setOnClickListener(this);
		mBtnSchool.setOnClickListener(this);
		mBtnUniversity.setOnClickListener(this);
		mbtnStadium.setOnClickListener(this);
		mBtnGym.setOnClickListener(this);
	}
	
	private void initActionBar() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActivity().getActionBar().setTitle(Html.fromHtml("<font color=\"red\">" + getString(R.string.app_title) + "</font>"));
		getActivity().getActionBar().setTitle(getString(R.string.app_title));
//		Drawable myBg = getResources().getDrawable(R.drawable.action_bar);
//		getActivity().getActionBar().setBackgroundDrawable(myBg);
	}

	@Override
	public void onClick(View v) {
		
		if(UserSetting.getInstance().isConnectInternet) {
			switch (v.getId()) {
			case R.id.btn_restaurant:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_RESTAURANT, 
						getResources().getString(R.string.home_restaurant));
				break;
			case R.id.btn_cafe:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_CAFE,
						getResources().getString(R.string.home_cafe));
				break;
			case R.id.btn_zoo:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_ZOO, 
						getResources().getString(R.string.home_zoo));
				break;
			case R.id.btn_shopping:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_SHOPPING,
						getResources().getString(R.string.home_shopping));
				break;
			case R.id.btn_food:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_FOOD,
						getResources().getString(R.string.home_food));
				break;
			case R.id.btn_park:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_PARK,
						getResources().getString(R.string.home_park));
				break;
			case R.id.btn_bar:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_BAR,
						getResources().getString(R.string.home_bar));
				break;
			case R.id.btn_post_office:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_POST_OFFICE,
						getResources().getString(R.string.home_post_office));
				break;
			case R.id.btn_bank:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_BANK,
						getResources().getString(R.string.home_bank));
				break;
			case R.id.btn_hospital:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_HOSPITAL,
						getResources().getString(R.string.home_hospital));
				break;
			case R.id.btn_pharmacy:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_PHARMACY,
						getResources().getString(R.string.home_pharmacy));
				break;
				
			case R.id.btn_super_market:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_SUPER_MARKET,
						getResources().getString(R.string.home_super_market));
				break;
			case R.id.btn_museum:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_MUSEUM,
						getResources().getString(R.string.home_museum));
				break;
			case R.id.btn_beauty_salon:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_BEAUTY_SALON,
						getResources().getString(R.string.home_beauty_salon));
				break;
			case R.id.btn_bus_station:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_BUS_STATION,
						getResources().getString(R.string.home_bus_station));
				break;
			case R.id.btn_gas_station:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_GAS_STATION,
						getResources().getString(R.string.home_gas_station));
				break;
			case R.id.btn_night_club:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_NIGHT_CLUB,
						getResources().getString(R.string.home_night_club));
				break;
			case R.id.btn_movie_theater:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_MOVIE_THEATER,
						getResources().getString(R.string.home_movie_theater));
				break;
			case R.id.btn_school:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_SCHOOL,
						getResources().getString(R.string.home_school));
				break;
			case R.id.btn_university:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_UNIVERSITY,
						getResources().getString(R.string.home_university));
				break;
			case R.id.btn_stadium:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_STADIUM,
						getResources().getString(R.string.home_stadium));
				break;
			case R.id.btn_gym:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_GYM,
						getResources().getString(R.string.home_gym));
				break;
				
			default:
				startMapActivity(mContext, Constants.PlaceTypes.TYPE_RESTAURANT,
						getResources().getString(R.string.home_restaurant));
				break;
			}
		} else {
			GeneralUtils.showLongToast(mContext, getResources().getString(R.string.msg_no_internet_access));
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	private void startMapActivity(Context mContext, String type, String title) {
		if(GeneralUtils.checkPlayServices(getActivity())) {
			Intent intent = new Intent(mContext, MapActivity.class);
			intent.putExtra(Constants.MapScreenMode.MAP_MODE, Constants.MapScreenMode.MAP_MODE_PLACE);
			intent.putExtra(Constants.PlaceTypes.PLACE_TYPE, type);
			intent.putExtra(Constants.PlaceTypes.PLACE_TITLE, title);
			GeneralUtils.gotoActivityWithAnim(mContext, intent);
		}
	}
	
	private void setOriginRiple(final LayoutRipple layoutRipple){
    	
    	layoutRipple.post(new Runnable() {
			
			@Override
			public void run() {
				View v = layoutRipple.getChildAt(0);
		    	layoutRipple.setxRippleOrigin(ViewHelper.getX(v)+v.getWidth()/2);
		    	layoutRipple.setyRippleOrigin(ViewHelper.getY(v)+v.getHeight()/2);
		    	
		    	layoutRipple.setRippleColor(getResources().getColor(R.color.holo_blue_light));
		    	
		    	layoutRipple.setRippleSpeed(30);
			}
		});
    }

}
