package com.zulu.places.layout;

import java.io.IOException;
import java.util.ArrayList;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zulu.places.R;
import com.zulu.places.abstracts.AbstractActivity;
import com.zulu.places.adapter.ListPlacesAdapter;
import com.zulu.places.model.Place;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;

public class ListPlacesActivity extends AbstractActivity {
	
	private ListView mLstPlacesView;
	private ArrayList<Place> mLstPlacesResult;
//	private String[] arrDistance;
	private ArrayList<String> lstDistance;
	
	public static int placesPos = 0;
	
	private void initListView() {
		ListPlacesAdapter adapter = new ListPlacesAdapter(mContext, mLstPlacesResult, lstDistance);
		mLstPlacesView.setAdapter(adapter);
		mLstPlacesView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				placesPos = pos;
				finish();
				overridePendingTransition(R.anim.zoom_in_half, R.anim.move_left_out);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
		mLstPlacesResult = new ArrayList<Place>();
		try {
			mLstPlacesResult = (ArrayList<Place>) GeneralUtils
					.bytes2Object(getIntent().getByteArrayExtra(
							Constants.PlaceDetails.LIST_PLACES));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		lstDistance = getIntent().getStringArrayListExtra(Constants.PlaceDetails.LIST_DISTANCE);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==android.R.id.home) {
			finish();
			overridePendingTransition(R.anim.zoom_in_half, R.anim.move_left_out);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public int getContentViewId() {
		return R.layout.activity_list_places;
	}

	@Override
	public void initView() {
		
		initData();
		
		mLstPlacesView = (ListView)findViewById(R.id.lst_places);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(getResources().getString(R.string.text_list_places).replace("{0}", 
				String.valueOf(mLstPlacesResult.size())));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initListView();
	}

}
