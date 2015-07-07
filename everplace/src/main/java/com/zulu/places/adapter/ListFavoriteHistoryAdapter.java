package com.zulu.places.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zulu.places.R;
import com.zulu.places.model.PlaceDetail;
import com.zulu.places.utils.Constants;

public class ListFavoriteHistoryAdapter extends BaseAdapter{

	private ArrayList<PlaceDetail> lstPlaces;
	private Context mContext;
	private View emptyView;
	
	public ListFavoriteHistoryAdapter(Context mContext, ArrayList<PlaceDetail> lstPlaces) {
		this.lstPlaces = lstPlaces;
		this.mContext = mContext;
	}
	
	@Override
	public int getCount() {
		return (lstPlaces!=null) ? lstPlaces.size() : 1;
	}

	@Override
	public Object getItem(int arg0) {
		return (lstPlaces!=null) ? lstPlaces.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View arg1, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.item_favorite_place, parent, false);
		
		TextView tvName = (TextView)rowView.findViewById(R.id.txt_favorite_place_name);
		TextView tvAddress = (TextView)rowView.findViewById(R.id.txt_favorite_place_address);
		ImageView imgType = (ImageView)rowView.findViewById(R.id.img_favorite_item);
		
		if(lstPlaces==null || lstPlaces.size()==0) {
			emptyView = inflater.inflate(R.layout.item_favorite_place_empty, parent, false);
			return emptyView;
		} else {
			tvName.setText(lstPlaces.get(pos).getName());
			tvAddress.setText(lstPlaces.get(pos).getAddress());
			if(lstPlaces.get(pos).getCategory()!=null) {
				setImageCategory(imgType, lstPlaces.get(pos).getCategory());
			} else {
				imgType.setImageResource(R.drawable.cate_icon_default);
			}
		}
		return rowView;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
//		GeneralUtils.showLongToast(mContext.getResources().getString(R.string.text_removed_favorite));
	}

	/**
	 * set image type for list favorite item
	 * @param imgType
	 * @param category
	 */
	private void setImageCategory(ImageView imgType, String category) {
		if(category.equals(Constants.PlaceTypes.TYPE_BANK)) {
			imgType.setImageResource(R.drawable.cate_icon_bank);
		} else if(category.equals(Constants.PlaceTypes.TYPE_BAR)) {
			imgType.setImageResource(R.drawable.cate_icon_bar_2);
		} else if(category.equals(Constants.PlaceTypes.TYPE_BEAUTY_SALON)) {
			imgType.setImageResource(R.drawable.cate_icon_beauty_salon);
		} else if(category.equals(Constants.PlaceTypes.TYPE_BUS_STATION)) {
			imgType.setImageResource(R.drawable.cate_icon_bus_station_1);
		} else if(category.equals(Constants.PlaceTypes.TYPE_CAFE)) {
			imgType.setImageResource(R.drawable.cate_icon_cafe);
		} else if(category.equals(Constants.PlaceTypes.TYPE_FOOD)) {
			imgType.setImageResource(R.drawable.cate_icon_food);
		} else if(category.equals(Constants.PlaceTypes.TYPE_GAS_STATION)) {
			imgType.setImageResource(R.drawable.cate_icon_gas_station);
		} else if(category.equals(Constants.PlaceTypes.TYPE_GYM)) {
			imgType.setImageResource(R.drawable.cate_icon_gym);
		} else if(category.equals(Constants.PlaceTypes.TYPE_HOSPITAL)) {
			imgType.setImageResource(R.drawable.cate_icon_hospital);
		} else if(category.equals(Constants.PlaceTypes.TYPE_MOVIE_THEATER)) {
			imgType.setImageResource(R.drawable.cate_icon_movie);
		} else if(category.equals(Constants.PlaceTypes.TYPE_MUSEUM)) {
			imgType.setImageResource(R.drawable.cate_icon_museum);
		} else if(category.equals(Constants.PlaceTypes.TYPE_NIGHT_CLUB)) {
			imgType.setImageResource(R.drawable.cate_icon_night_club);
		} else if(category.equals(Constants.PlaceTypes.TYPE_PARK)) {
			imgType.setImageResource(R.drawable.cate_icon_park);
		} else if(category.equals(Constants.PlaceTypes.TYPE_PHARMACY)) {
			imgType.setImageResource(R.drawable.cate_icon_pharmacy);
		} else if(category.equals(Constants.PlaceTypes.TYPE_POST_OFFICE)) {
			imgType.setImageResource(R.drawable.cate_icon_post_office);
		} else if(category.equals(Constants.PlaceTypes.TYPE_RESTAURANT)) {
			imgType.setImageResource(R.drawable.cate_icon_restaurant);
		} else if(category.equals(Constants.PlaceTypes.TYPE_SCHOOL)) {
			imgType.setImageResource(R.drawable.cate_icon_school);
		} else if(category.equals(Constants.PlaceTypes.TYPE_SHOPPING)) {
			imgType.setImageResource(R.drawable.cate_icon_shopping);
		} else if(category.equals(Constants.PlaceTypes.TYPE_STADIUM)) {
			imgType.setImageResource(R.drawable.cate_icon_stadium);
		} else if(category.equals(Constants.PlaceTypes.TYPE_SUPER_MARKET)) {
			imgType.setImageResource(R.drawable.cate_icon_super_market);
		} else if(category.equals(Constants.PlaceTypes.TYPE_UNIVERSITY)) {
			imgType.setImageResource(R.drawable.cate_icon_university);
		} else if(category.equals(Constants.PlaceTypes.TYPE_ZOO)) {
			imgType.setImageResource(R.drawable.cate_icon_zoo_elep);
		} else {
			imgType.setImageResource(R.drawable.cate_icon_restaurant);
		}
	}

}
