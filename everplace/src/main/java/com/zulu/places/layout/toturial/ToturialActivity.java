package com.zulu.places.layout.toturial;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zulu.places.R;
import com.zulu.places.adapter.FragmentAdapter;
import com.zulu.places.utils.GeneralUtils;

public class ToturialActivity extends FragmentActivity {

	private Context mContext;
	
	private ViewPager mViewPager;
	private ImageView mBtnClose;
	private ImageView mImgIndicator;
	private FragmentAdapter fragmentAdapter;
	
	private int[] lstResIndicator;
	private List<Fragment> lstFragment;
	
	public static int POS_1 = 1;
	public static int POS_2 = 2;
	public static int POS_3 = 3;
	public static int POS_4 = 4;
	public static int POS_5 = 5;
	public static int POS_6 = 6;
	public static int POS_7 = 7;
	public static int POS_8 = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toturial);
		
		mContext = this;
		
		initView();
	}
	
	@Override
	public void onBackPressed() {
		GeneralUtils.finishActivityWithAnim(mContext);
	}
	
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mBtnClose = (ImageView) findViewById(R.id.btn_close);
		mImgIndicator = (ImageView) findViewById(R.id.img_indicator);
		
		initViewPager();
		
		mBtnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GeneralUtils.finishActivityWithAnim(mContext);
			}
		});
	}
	
	/**
	 * init view pager for screen
	 */
	private void initViewPager() {
		
		lstResIndicator = new int[]{R.drawable.indi_1, R.drawable.indi_2, 
				R.drawable.indi_3, R.drawable.indi_4, R.drawable.indi_5, 
				R.drawable.indi_6, R.drawable.indi_7, R.drawable.indi_8 };
		
		lstFragment = new ArrayList<Fragment>();
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_category), getString(R.string.help_mess_category), POS_1));
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_fast_search), getString(R.string.help_mess_fast_search), POS_2));
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_maps), getString(R.string.help_mess_maps), POS_3));
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_place_detail), getString(R.string.help_mess_place_detail), POS_4));
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_direction), getString(R.string.help_mess_direction), POS_5));
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_navigation), getString(R.string.help_mess_navigation), POS_6));
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_favorite), getString(R.string.help_mess_favorite), POS_7));
		
		lstFragment.add(FunctionToturialFragment.newInstance(
				getString(R.string.help_title_history), getString(R.string.help_mess_history), POS_8));
		
		fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), lstFragment);
		
		mViewPager.setAdapter(fragmentAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				mImgIndicator.setImageResource(lstResIndicator[pos]);
			}
			
			@Override
			public void onPageScrolled(int pos, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int pos) {
				
			}
		});
	}

}
