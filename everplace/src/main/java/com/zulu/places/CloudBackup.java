package com.zulu.places;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

public class CloudBackup extends BackupAgentHelper {
	
	private String PREFS_DBBACKUP_KEY = "com.zulu.places.db";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		addHelper(PREFS_DBBACKUP_KEY, new FileBackupHelper(this, "../databases/places.db"));
	}

}
