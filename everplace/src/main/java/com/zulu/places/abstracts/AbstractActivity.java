package com.zulu.places.abstracts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zulu.places.utils.GeneralUtils;

public abstract class AbstractActivity extends FragmentActivity implements ActivityHolderInterface{

	public Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		
		this.setContentView(getContentViewId());
		
		initView();
	}
	
	@Override
	public void onBackPressed() {
		GeneralUtils.finishActivityWithAnim(mContext);
	}
	
	@Override
	public abstract int getContentViewId();

	@Override
	public abstract void initView();
	
}
