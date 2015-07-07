package com.zulu.places.utils;

import com.zulu.places.service.DirectionService;

public class Constants {

	/**
	 * 
	 */
	public class AppSetting {
		
		public static final String FONT = "fonts/FreeSans.ttf";
		public static final String FONT_BOLD = "fonts/FreeSansBold.ttf";
	}
	
	public static class PlaceDetails {
		
		public static String LIST_PLACES = "listPlaces";
		public static String LIST_DISTANCE = "listDistance";
		
		public static String PLACE_DETAIL_OBJECT = "place_detail_object";
	}
	
	public static class DirectionDetails {
		
		public static String DIRECTION_SWAP = "direction_swap";
		public static String DIRECTION_LAT = "direction_lat";
		public static String DIRECTION_LNG = "direction_lng";
		public static String DIRECTION_MODE = "direction_mode";
		public static String DIRECTION_PLACE_NAME = "direction_place_name";
		public static String DIRECTION_PLACE_ADDRESS = "direction_place_address";
		public static String DIRECTION_PLACE_TYPE = "direction_place_type";
		
		public static String MODE_SAVED = DirectionService.MODE_DRIVING;
		public static boolean SWAP_SAVED  = false;
	}
	
	public static class PlaceTypes {
		
		public static String PLACE_TYPE = "place_type";
		public static String PLACE_TITLE = "place_title";
		
		public static String TYPE_PARK = "park|amusement_park|rv_park";
		public static String TYPE_RESTAURANT = "restaurant|liquor_store";
		public static String TYPE_CAFE = "cafe";
		public static String TYPE_ZOO = "zoo";
		public static String TYPE_SHOPPING = "shopping_mall|clothing_store|convenience_store|" +
				"department_store|electronics_store|furniture_store|" +
				"pet_store|jewelry_store|home_goods_store|shoe_store";
//		public static String TYPE_SHOPPING = "store";
		public static String TYPE_FOOD = "food|bakery";
		public static String TYPE_BAR = "bar";
		public static String TYPE_POST_OFFICE = "post_office";
		public static String TYPE_BANK = "bank|atm";
		public static String TYPE_HOSPITAL = "hospital|health|physiotherapist";
		public static String TYPE_PHARMACY = "pharmacy";
		public static String TYPE_SUPER_MARKET = "grocery_or_supermarket";
		public static String TYPE_MUSEUM = "museum";
		public static String TYPE_SCHOOL = "school";
		public static String TYPE_UNIVERSITY = "university";
		public static String TYPE_STADIUM = "stadium";
		public static String TYPE_GYM = "gym";
		public static String TYPE_NIGHT_CLUB = "night_club";
		public static String TYPE_MOVIE_THEATER = "movie_theater";
		public static String TYPE_BEAUTY_SALON = "beauty_salon|hair_care|spa";
		public static String TYPE_BUS_STATION = "bus_station|transit_station";
		public static String TYPE_GAS_STATION = "gas_station";
		
//		Beauty salon <> gas station <> hair care <> gym <> grocery or supermarket <> movie theater <> museum <> night club <>
//		post_office <> school <> spa <> stadium <> university <> bus_station
		
		// nha hang
		// so thu, dia diem du lich
		// cafe
		// mua sam, bar
		// an uong
		// ngan hang, atm, buu dien					post_office
		// cham soc suc khoe, nha thuoc
		// tram xe bus, tram xang					bus_station|transit_station			gas station	
		// sieu thi, bao tang						grocery or supermarket				museum
		// truong hoc, dai hoc						school								university
		// cham soc sac dep, tham mi vien:			Beauty salon|hair care|spa
		// san van dong, gym						stadium								gym
		// night club, rap phim						night club							movie theater
	}
	
	public static class PlaceHomeDisplayTypes {
		
		public static String KEY_SETTING = "prefCategoryList";
		
		public static String TYPE_RES = "Restaurant";
		public static String TYPE_PARK = "Park";
		public static String TYPE_CAFE = "Cafe";
		public static String TYPE_SHOPPING = "Shoping";
		public static String TYPE_FOOD = "Food";
		public static String TYPE_BANK = "Bank";
		public static String TYPE_MARKET = "Market";
		public static String TYPE_BEAUTY = "Beauty";
		public static String TYPE_BUS = "Bus";
		public static String TYPE_NIGHT = "Night";
		public static String TYPE_HEALTH = "Health";
		public static String TYPE_SCHOOL = "School";
		public static String TYPE_STADIUM = "Stadium";
	}
	
	public static class PrefCateTypes {
		
		public static String KEY_TYPE = "prefCateType";
		
		public static String METRO = "Metro";
		public static String LIST = "List";
	}
	
	/**
	 *
	 */
	public class PrefenceKey {
		public static final String PREF_REMEMBER_CHECKED = "rememberChecked";
		public static final String PREF_REMEMBER_USERNAME = "rememberUsername";
		public static final String PREF_REMEMBER_PASSWORD = "rememberPassword";
		public static final String PREF_TIME_REPEAT_SERVICE = "timeRepeatService";
		public static final String PREF_LINK_SERVER = "linkServer";
		public static final String PREF_ACRA_MAIL_TO = "acraMailTo";
		public static final String PREF_ACRA_FORM_KEY = "acraFormKey";
		
		// pref
		public static final String PREF_IS_FIRST_RUN = "isFirstRun";
		public static final String PREF_FIRST_MAIN_ACTIVITY = "isFirstMainActivity";
		public static final String PREF_FIRST_PLACE_DETAIL = "isFirstPlaceDetail";
		public static final String PREF_FIRST_DIRECTION = "isFirstDirection";
		public static final String PREF_FIRST_SWIPE_DISMISS = "isFirstSwipeDismiss";
	}
	
	/**
	 *
	 */
	public class TargetMode {
		public static final String PREF_TARGET = "prefTargetMode";
		
		public static final int CURRENT_LOCATION = 0;
		public static final int PHU_QUOC = 1;
		
		public static final double PQ_LAT = 10.2288308;
		public static final double PQ_LNG = 103.9573059;
		
		public static final double VT_LAT = 10.347713;
		public static final double VT_LNG = 107.098406;
	}
	
	/**
	 *
	 */
	public class MapScreenMode {
		public static final String MAP_MODE = "MAP_MODE";
		
		public static final String MAP_MODE_PLACE = "MAP_MODE_PLACE";
		public static final String MAP_MODE_TEXT = "MAP_MODE_TEXT";
		
		public static final String TEXT_SEARCH = "TEXT_SEARCH";
	}
	
	/**
	 * 
	 */
	public class ActionReceiver {
		public static final String ACTION_RECEIVER = "ACTION_RECEIVER";

		public static final String SHOW_TOTURIAL = "SHOW_TOTURIAL";
	}
}
