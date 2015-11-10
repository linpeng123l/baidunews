package com.linpeng.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoveNewsManage extends DataBaseManage{

	public LoveNewsManage(Context context) {
		super(context);
	}

	public List<String> getAllNewsTitles(){
		SQLiteDatabase sqLiteDatabase = getReadableDatabase();
		List<String> titles = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query("loveNews", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			titles.add(cursor.getString(cursor.getColumnIndex("title")));
		}
		return titles;
	}

	public void addLoveNews(String title){
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		if(sqLiteDatabase!=null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("title", title);
			sqLiteDatabase.insert("loveNews", null, contentValues);
		}
		sqLiteDatabase.close();
	}

}
