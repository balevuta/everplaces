package com.zulu.places.layout;

import android.content.Intent;
import android.os.Handler;

import com.zulu.places.R;
import com.zulu.places.abstracts.AbstractActivity;
import com.zulu.places.utils.GeneralUtils;

public class SplashActivity extends AbstractActivity {

	@Override
	public int getContentViewId() {
		return R.layout.activity_splash;
	}

	@Override
	public void initView() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				GeneralUtils.finishActivityWithAnim(mContext);
				Intent intent = new Intent(SplashActivity.this, DrawerActivity.class);
				GeneralUtils.gotoActivityWithAnim(mContext, intent);
			}
		}, 2000);
	}

}
