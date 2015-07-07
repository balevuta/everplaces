package com.zulu.places.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zulu.places.R;
import com.zulu.places.model.Place;

public class ListPlacesAdapter extends BaseAdapter{

	private ArrayList<Place> lstPlaces;
	private ArrayList<String> lstDistance;
	private Context mContext;
	
	public ListPlacesAdapter(Context mContext, ArrayList<Place> lstPlaces, ArrayList<String> lstDistance) {
		this.lstPlaces = lstPlaces;
		this.mContext = mContext;
		this.lstDistance = lstDistance;
	}

	@Override
	public int getCount() {
		return lstPlaces.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lstPlaces.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.item_places_list, null);
		
		TextView tvName = (TextView)rowView.findViewById(R.id.txt_list_name);
		TextView tvAddress = (TextView)rowView.findViewById(R.id.txt_list_address);
		TextView tvDistance = (TextView)rowView.findViewById(R.id.txt_list_distance);
		
		tvName.setText(lstPlaces.get(pos).getName());
		tvAddress.setText(lstPlaces.get(pos).getVicinity());
		tvDistance.setText(lstDistance.get(pos));
		
		return rowView;
	}

}
