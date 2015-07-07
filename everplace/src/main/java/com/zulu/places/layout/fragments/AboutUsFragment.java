package com.zulu.places.layout.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zulu.places.R;
import com.zulu.places.utils.GeneralUtils;

public class AboutUsFragment extends Fragment {

	private LinearLayout mBoxAboutUs;
	private Context mContext;
	
	private TextView mTxtVersion;
	private ImageView mImgWebsite, mImgEmail, mImgPhone, mImgAddress;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_about_us, null);
		
		mContext = getActivity();
		
		initActionBar();
		initComponant(view);
		
		return view;
	}
	
	private void initComponant(View view) {
		mBoxAboutUs = (LinearLayout) view.findViewById(R.id.box_about_us);
		GeneralUtils.setViewApprearAnim(mBoxAboutUs, mContext);
		
		mTxtVersion = (TextView) view.findViewById(R.id.txt_version);
		mImgAddress = (ImageView) view.findViewById(R.id.img_address);
		mImgWebsite = (ImageView) view.findViewById(R.id.img_website);
		mImgEmail = (ImageView) view.findViewById(R.id.img_email);
		mImgPhone = (ImageView) view.findViewById(R.id.img_phone);
		
		mTxtVersion.setText(getString(R.string.text_app_version).replace("{0}", GeneralUtils.getAppVersionName(mContext)));
		mImgAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GeneralUtils.viewOnMap(mContext, "1 Bach Dang Street, Ward 2, Tan Binh Dist., Ho Chi Minh City, Vietnam");
			}
		});
		mImgWebsite.setOnClickListener(new OnClickListener() {
					
				@Override
				public void onClick(View v) {
					GeneralUtils.doWebsiteClick(mContext, "http://zulu-solutions.com/");
				}
			});
		mImgEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GeneralUtils.doEmailClick(mContext, "contact@zulu-solutions.com");
			}
		});
		mImgPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GeneralUtils.doCallPhoneClick(mContext, "+84 984 367 830");
			}
		});
	}

	private void initActionBar() {
		getActivity().getActionBar().setTitle(R.string.title_about_us);
		setHasOptionsMenu(true);
	}
}
