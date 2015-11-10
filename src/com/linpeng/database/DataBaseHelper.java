package com.linpeng.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{


	private final static String DATABASE_NAME = "lange"; 
	public synchronized static DataBaseHelper getInstance(Context context){
		return new DataBaseHelper(context);
	}


	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists loveNews(id integer primary key autoincrement,title varchar(100))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
