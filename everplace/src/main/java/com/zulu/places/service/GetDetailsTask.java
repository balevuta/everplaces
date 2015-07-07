package com.zulu.places.service;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.zulu.places.R;
import com.zulu.places.layout.PlaceDetailActivity;
import com.zulu.places.model.PlaceDetail;
import com.zulu.places.utils.Constants;
import com.zulu.places.utils.GeneralUtils;

public class GetDetailsTask extends AsyncTask<Void, Void, PlaceDetail> {

	private ProgressDialog dialog;
	private Context mContext;
	private String placeReference;
	private String placeCategory;
	
	public GetDetailsTask(Context context, String placeReference, String placeCategory) {
		this.mContext = context;
		this.placeReference = placeReference;
		this.placeCategory = placeCategory;
	}
	
	@Override
	protected void onPostExecute(PlaceDetail result) {
		super.onPostExecute(result);
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
		// Do after find details
		if(result!=null) {
			Intent intent = new Intent(mContext, PlaceDetailActivity.class);
			try {
				intent.putExtra(Constants.PlaceDetails.PLACE_DETAIL_OBJECT, GeneralUtils.object2Bytes(result));
			} catch (IOException e) {
				e.printStackTrace();
			}
			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(R.anim.move_right_in, R.anim.zoom_out_half);
		} else {
			GeneralUtils.showShortToast(mContext, mContext.getString(R.string.msg_connect_fail));
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(mContext);
		dialog.setCancelable(true);
		dialog.setMessage(mContext.getResources().getString(R.string.text_loading));
		dialog.isIndeterminate();
		dialog.show();
	}

	@Override
	protected PlaceDetail doInBackground(Void... arg0) {
		PlacesService service = new PlacesService(mContext.getString(R.string.api_place_key), mContext);
		
		PlaceDetail detailsPlace = new PlaceDetail();
		
		detailsPlace = service.findPlaceDetails(placeReference, placeCategory);
		
		if(detailsPlace.getPhotoReference()!=null) {
			ArrayList<String> lstPhotoURL = new ArrayList<String>();
			for(int i=0;i<detailsPlace.getPhotoReference().size();i++) {
				String photoUrl = service.getPhotoResponseUrl(detailsPlace.getPhotoReference().get(i));
				lstPhotoURL.add(photoUrl);
			}
			detailsPlace.setPhotoURL(lstPhotoURL);
		} else {
			dialog.dismiss();
		}
	
		return detailsPlace;
	}
	
}
