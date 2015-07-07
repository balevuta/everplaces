package com.zulu.places.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zulu.places.R;
import com.zulu.places.layout.DrawerActivity;
import com.zulu.places.model.CacheBitmap;
import com.zulu.places.utils.GeneralUtils;

public class PlacePhotoAdapter extends PagerAdapter {

	private ArrayList<String> lstPhotoURL;
	private Context mContext;
	
	public PlacePhotoAdapter(ArrayList<String> lstPhotoURL, Context mContext) {
		super();
		this.lstPhotoURL = lstPhotoURL;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(lstPhotoURL!=null)
			return lstPhotoURL.size();
		else 
			return 1;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == ((ImageView) object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(mContext);
//		int padding = mContext.getResources().getDimensionPixelSize(R.dimen.padding_medium);
		imageView.setPadding(10, 10, 10, 10);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
//		imageView.setImageResource(R.drawable.image_not_found);
		downloadPlacePhoto(imageView, position);
		
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}
	
	private void downloadPlacePhoto(final ImageView imageView, final int pos) {
		if(lstPhotoURL!=null) {
			Bitmap bitmap = DrawerActivity.mCache
					.getBitmapFromMemCache(lstPhotoURL.get(pos));
			imageView.setImageBitmap(bitmap);
	
			AsyncTask<Void, Void, CacheBitmap> task = new AsyncTask<Void, Void, CacheBitmap>() {
	
				@Override
				protected CacheBitmap doInBackground(Void... params) {
	
					Bitmap tmp = DrawerActivity.mCache
							.getBitmapFromMemCache(lstPhotoURL.get(pos));
					if (tmp != null) {
						CacheBitmap bt = new CacheBitmap();
						bt.setKey(lstPhotoURL.get(pos));
						bt.setBitmap(tmp);
						Log.d("Using Image Memory Cache :", "FYI");
						return bt;
					} else {
						Log.d("Downloading Images :", "FYI");
						return GeneralUtils.downloadImageWithCache(mContext, lstPhotoURL.get(pos));
					}
				}
	
				@Override
				protected void onPostExecute(CacheBitmap result) {
					if (result != null) {
						DrawerActivity.mCache.addBitmapToMemoryCache(result.getKey(),
								result.getBitmap());
						imageView.setImageBitmap(result.getBitmap());
					}
				}
			};
	
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				task.execute();
			}
		} else {
			imageView.setImageResource(R.drawable.image_not_found);
		}
	}

}
