package com.zulu.places.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zulu.places.R;
import com.zulu.places.model.ReviewDetail;
import com.zulu.places.utils.DateTimeUtils;

public class ListReviewAdapter extends BaseAdapter {

	private List<ReviewDetail> lstReviews;
	private Context mContext;
	
	public ListReviewAdapter(Context mContext, List<ReviewDetail> lstReviews) {
		this.lstReviews = lstReviews;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return lstReviews.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lstReviews.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.item_review_detail, null);
		
		TextView tvAuthorName = (TextView)rowView.findViewById(R.id.txt_author_name);
		TextView tvTime = (TextView)rowView.findViewById(R.id.txt_time);
		TextView tvText = (TextView)rowView.findViewById(R.id.txt_review_text);
		RatingBar ratingBar = (RatingBar)rowView.findViewById(R.id.rating_review);
		
		tvAuthorName.setText(lstReviews.get(pos).getAuthorName());
		tvText.setText(lstReviews.get(pos).getText());
		tvTime.setText(DateTimeUtils.formatStandardDate(lstReviews.get(pos).getTime()));
		ratingBar.setRating(lstReviews.get(pos).getRating());
		
		return rowView;
	}

}
