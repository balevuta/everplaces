package com.zulu.places.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zulu.places.R;

public class CustomAlertDialog {

	private Button leftButton, rightButton, onlyButton;
	private Dialog mCustomDialog;
	
	public CustomAlertDialog(Context mContext, View contentView) {
		View customLayout = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_layout, null);
        FrameLayout layoutContent = (FrameLayout)customLayout.findViewById(R.id.layout_content);
        leftButton = (Button)customLayout.findViewById(R.id.btn_left);
        rightButton = (Button)customLayout.findViewById(R.id.btn_right);
        onlyButton = (Button)customLayout.findViewById(R.id.btn_only);
        leftButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);
        onlyButton.setVisibility(View.GONE);
        layoutContent.addView(contentView);
        mCustomDialog = new Dialog(mContext, R.style.CustomDialogTheme);
//        mCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCustomDialog.setContentView(customLayout);
        mCustomDialog.setCanceledOnTouchOutside(true);
        mCustomDialog.getWindow().getAttributes().windowAnimations = R.style.Animations_Window;
	}
	
	public CustomAlertDialog(Context mContext, String message) {
		View customLayout = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_layout, null);
        FrameLayout layoutContent = (FrameLayout)customLayout.findViewById(R.id.layout_content);
        leftButton = (Button)customLayout.findViewById(R.id.btn_left);
        rightButton = (Button)customLayout.findViewById(R.id.btn_right);
        onlyButton = (Button)customLayout.findViewById(R.id.btn_only);
        leftButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);
        onlyButton.setVisibility(View.GONE);
        
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.simple_dialog_layout, null);
        TextView txtMessage = (TextView) contentView.findViewById(R.id.txt_dialog_message);
        txtMessage.setText(message);
        layoutContent.addView(contentView);
        
        mCustomDialog = new Dialog(mContext, R.style.CustomDialogTheme);
//        mCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCustomDialog.setContentView(customLayout);
        mCustomDialog.setCanceledOnTouchOutside(true);
        mCustomDialog.getWindow().getAttributes().windowAnimations = R.style.Animations_Window;
	}
	
	public void showDialog() {
		if(mCustomDialog != null && !mCustomDialog.isShowing()){
			mCustomDialog.show();
		}
	}
	
	public void hideDialog() {
		if(mCustomDialog != null && mCustomDialog.isShowing()){
			mCustomDialog.dismiss();
		}
	}
	
	public void setLeftButton(String title, View.OnClickListener onClick) {
		leftButton.setVisibility(View.VISIBLE);
		leftButton.setText(title);
		leftButton.setOnClickListener(onClick);
	}
	
	public void setRightButton(String title, View.OnClickListener onClick) {
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setText(title);
		rightButton.setOnClickListener(onClick);
	}
	
	public void setOnlyButton(String title, View.OnClickListener onClick) {
		onlyButton.setVisibility(View.VISIBLE);
		onlyButton.setText(title);
		onlyButton.setOnClickListener(onClick);
	}
	
	public void setTitle(String title) {
		mCustomDialog.setTitle(title);
	}
	
}
