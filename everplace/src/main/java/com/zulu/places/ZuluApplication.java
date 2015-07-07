package com.zulu.places;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.zulu.places.R;

@ReportsCrashes(
		formKey = "",
		mailTo = "banhlevuthan@gmail.com", // my email here. Cannot empty. It is replaced on your custom configuration.
		customReportContent = { 
				ReportField.USER_COMMENT,  
				ReportField.APP_VERSION_NAME,
				ReportField.APP_VERSION_CODE,
				ReportField.ANDROID_VERSION, 
				ReportField.PHONE_MODEL,
				ReportField.USER_CRASH_DATE, 
				ReportField.STACK_TRACE, 
				ReportField.LOGCAT },	
		mode = ReportingInteractionMode.DIALOG,
		resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
		resDialogText = R.string.crash_dialog_text,
		resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
		resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
		resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
		resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
	)

public class ZuluApplication extends Application{

	private static Context sContext;
	private static ZuluApplication instance;
	private BackupManager mBackupManager;
	
	public static ZuluApplication getInstance(){
		if(instance == null) {
			instance = new ZuluApplication();
		}
		return instance;
	}
	
	@Override
	public void onCreate() {
		
		sContext = getApplicationContext();
		mBackupManager = new BackupManager(sContext);
		Log.i("ZuluApplication", "onCreate");
		
		// init ARCA
//		ACRA.init(this);
//        
//        // Configuration
//        ACRAConfiguration configuration = ACRA.getNewDefaultConfig(this);
//        configuration.setMailTo(PreferenceUtils.getString(getApplicationContext(), 
//        		Constants.PrefenceKey.PREF_ACRA_MAIL_TO, "banhlevuthan@gmail.com"));
//        ACRA.setConfig(configuration);
        
        super.onCreate();
	}
	
	@Override
	public void onTerminate() {
		Log.i("ZuluApplication", "onTerminate");
		super.onTerminate();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Context getContext() {
		return sContext;
	}
	
	/**
	 * 
	 */
	public void dataChanged() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mBackupManager.dataChanged();
		}
	}
	
	/**
	 * 
	 */
	public void requestRestore() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mBackupManager.requestRestore(new RestoreObserver() {
				
				@Override
				public void onUpdate(int nowBeingRestored, String currentPackage) {
					super.onUpdate(nowBeingRestored, currentPackage);
				}
				
				@Override
				public void restoreFinished(int error) {
					super.restoreFinished(error);
				}
				
				@Override
				public void restoreStarting(int numPackages) {
					super.restoreStarting(numPackages);
				}
			});
		}
	}
	
}
