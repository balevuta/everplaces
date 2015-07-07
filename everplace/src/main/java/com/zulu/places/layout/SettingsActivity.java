package com.zulu.places.layout;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.zulu.places.R;
import com.zulu.places.utils.GeneralUtils;
import com.zulu.places.widget.CustomAlertDialog;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {

	private Context mContext;
	private ListPreference mPrefLanguage;
	
	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;

		PreferenceManager.setDefaultValues(mContext, R.xml.settings, false);

		addPreferencesFromResource(R.xml.settings);
		
		getListView().setBackgroundColor(getResources().getColor(R.color.white));

		initActionBar();

		if(isFirst) {
			initPrefs();
			isFirst = false;
		}
	}
	
	/**
	 * init action bar
	 */
	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		Drawable myBg = getResources().getDrawable(R.drawable.action_bar);
		getActionBar().setBackgroundDrawable(myBg);
		getActionBar().setTitle(R.string.text_setting);
	}

	private void initPrefs() {
		mPrefLanguage = (ListPreference) findPreference("prefLanguageList");
		mPrefLanguage.setOnPreferenceChangeListener(this);
		mPrefLanguage.setValueIndex(getLanguageIndex(getResources().getConfiguration().locale));
		mPrefLanguage.setSummary(getLanguageDisplay(getResources().getConfiguration().locale));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			GeneralUtils.finishActivityWithAnim(mContext);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		GeneralUtils.finishActivityWithAnim(mContext);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		if (preference.getKey().equals("prefHelp")) {
			GeneralUtils.startHelpActivity(mContext);
//			showDialogHelp();
		} else if (preference.getKey().equals("prefAbout")) {
			showDialogAbout();
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.getKey().equals("prefLanguageList")) {
			if(newValue.toString().equals("US") && !getResources().getConfiguration().locale.equals(Locale.US)) {
				changeLocalLanguage(Locale.US);
				mPrefLanguage.setValueIndex(0);
			} else if(newValue.toString().equals("VN") && !getResources().getConfiguration().locale.toString().equals("vi_vn")) {
				changeLocalLanguage(new Locale("vi_vn"));
				mPrefLanguage.setValueIndex(1);
			}
			mPrefLanguage.setSummary(getLanguageDisplay(getResources().getConfiguration().locale));
		}
		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Set default Language
		Locale.setDefault(newConfig.locale);
		getBaseContext().getResources().updateConfiguration(newConfig,
				getResources().getDisplayMetrics());

		// Refresh UI
		recreate();

		// Inform
		GeneralUtils.showShortToast(mContext, getResources().getString(R.string.msg_change_language_successfully));
	}

	/**
	 * Change Local Language
	 */
	private void changeLocalLanguage(Locale locale) {
		Configuration newConfig = new Configuration();
		newConfig.locale = locale;
		onConfigurationChanged(newConfig);
	}
	
	/**
	 * 
	 * @param locale
	 * @return
	 */
	private int getLanguageIndex(Locale locale) {
		if(locale.equals(Locale.US)) {
			return 0;
		} else if(locale.toString().equals("vi_vn")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param locale
	 * @return
	 */
	private String getLanguageDisplay(Locale locale) {
		if(locale.equals(Locale.US)) {
			return getResources().getStringArray(R.array.languagesEntries)[0];
		} else if(locale.toString().equals("vi_vn")) {
			return getResources().getStringArray(R.array.languagesEntries)[1];
		}
		return getResources().getStringArray(R.array.languagesEntries)[0];
	}

	/**
	 * 
	 */
	private void showDialogAbout() {

		View customLayout = getLayoutInflater().inflate(
				R.layout.dialog_about_us, null);
		final CustomAlertDialog aboutDg = new CustomAlertDialog(mContext,
				customLayout);
		aboutDg.setTitle(getResources().getString(R.string.pref_about));
		aboutDg.setOnlyButton(getResources().getString(R.string.btn_ok),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						aboutDg.hideDialog();
					}
				});
		aboutDg.showDialog();
	}
	
	/**
	 * 
	 */
	private void showDialogHelp() {

		View customLayout = getLayoutInflater().inflate(R.layout.dialog_help,
				null);
		final CustomAlertDialog helpDg = new CustomAlertDialog(mContext,
				customLayout);
		helpDg.setTitle(getResources().getString(R.string.pref_help));
		helpDg.setOnlyButton(getResources().getString(R.string.btn_ok),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						helpDg.hideDialog();
					}
				});
		helpDg.showDialog();
	}

}
