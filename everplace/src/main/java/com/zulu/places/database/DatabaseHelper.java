package com.zulu.places.database;

import java.lang.reflect.Method;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zulu.places.ZuluApplication;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	//private Context mContext;
//	private static SQLiteDatabase mDataBase;
	
//    public DatabaseHelper(Context context) {
//    	super(context, DatabaseDefinition.DATABASE_NAME, null, DatabaseDefinition.DATABASE_VERSION);
//    }
    
    public DatabaseHelper() {
    	super(ZuluApplication.getContext(), DatabaseDefinition.DATABASE_NAME, null, DatabaseDefinition.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {      
    	for(Class<?> cls : DatabaseDefinition.Tables){
            try {
                Method method = cls.getMethod(DatabaseDefinition.CREATE_SQL);
                String sql = (String) method.invoke(null);    // static method
                if(sql != null) {
                    db.execSQL(sql);
                    Log.i("ZuluDatabase", "Table created: " + cls.getName());
                }
            } catch(Exception e) {
            	e.printStackTrace();
            	Log.i("ZuluDatabase", "Wrong create: " + e.getMessage());
            }
        }       
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	for(Class<?> cls : DatabaseDefinition.Tables){
            try {
                Method method = cls.getMethod(DatabaseDefinition.UPDATE_SQL);
                String sql = (String) method.invoke(null);    // static method
                if(sql != null) {
                	db.execSQL(sql);
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    	
		onCreate(db);
    }

}