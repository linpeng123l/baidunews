package com.linpeng.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManage {
	protected Context context;
	private SQLiteDatabase readableDatabase;
	private SQLiteDatabase writeableDatabase;

	public DataBaseManage(Context context) {
		super();
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public SQLiteDatabase getReadableDatabase(){
		if(readableDatabase==null){
			readableDatabase = 	DataBaseHelper.getInstance(context).getReadableDatabase();
		}
		return readableDatabase;
	}

	public SQLiteDatabase getWritableDatabase(){
		if(writeableDatabase==null||!writeableDatabase.isOpen()){
			writeableDatabase = DataBaseHelper.getInstance(context).getWritableDatabase();
		}
		return writeableDatabase;
	}
	public void closeDB(){
		if(readableDatabase!=null){
			readableDatabase.close();
		}
		if(writeableDatabase!=null){
			writeableDatabase.close();
		}
	}
}
