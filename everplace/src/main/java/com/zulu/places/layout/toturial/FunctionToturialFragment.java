package com.zulu.places.layout.toturial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zulu.places.R;

public class FunctionToturialFragment extends Fragment {

	private TextView mTxtTitle, mTxtMess;
	private ImageView mImgDidac;
	
	public static final String TITLE = "TITLE";
	public static final String MESS = "MESS";
	public static final String POS_IMAGE = "RES_IMAGE";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_toturial_detail, null);
		
		initView(view);
		
		return view;
	}
	
	private void initView(View view) {
		mImgDidac = (ImageView) view.findViewById(R.id.img_didac);
		mTxtTitle = (TextView) view.findViewById(R.id.txt_didac_title);
		mTxtMess = (TextView) view.findViewById(R.id.txt_didac_mess);
		
		int pos = getArguments().getInt(POS_IMAGE);
		if(pos == ToturialActivity.POS_1) {
			mImgDidac.setImageResource(R.drawable.totu_category);
		} else if(pos == ToturialActivity.POS_2) {
			mImgDidac.setImageResource(R.drawable.totu_fast_search);
		} else if (pos == ToturialActivity.POS_3) {
			mImgDidac.setImageResource(R.drawable.totu_map_screen);
		} else if (pos == ToturialActivity.POS_4) {
			mImgDidac.setImageResource(R.drawable.totu_place_detail);
		} else if (pos == ToturialActivity.POS_5) {
			mImgDidac.setImageResource(R.drawable.totu_direction_detail);
		} else if (pos == ToturialActivity.POS_6) {
			mImgDidac.setImageResource(R.drawable.totu_navigate);
		} else if (pos == ToturialActivity.POS_7) {
			mImgDidac.setImageResource(R.drawable.totu_favorite_list);
		} else if (pos == ToturialActivity.POS_8) {
			mImgDidac.setImageResource(R.drawable.totu_history_list);
		}
		mTxtTitle.setText(getArguments().getString(TITLE));
		mTxtMess.setText(getArguments().getString(MESS));
	}
	
	public static FunctionToturialFragment newInstance(String title, String mess, int imgPos) {

		FunctionToturialFragment f = new FunctionToturialFragment();
		
		Bundle b = new Bundle();
	    b.putString(TITLE, title);
	    b.putString(MESS, mess);
	    b.putInt(POS_IMAGE, imgPos);
	    f.setArguments(b);

        return f;
    }
	
}
