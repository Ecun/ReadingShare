package edu.bsu.cs222.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDao {
	private DBHelper helper;
	protected SQLiteDatabase db;
	
	public BaseDao(Context context){
		helper=new DBHelper(context);
		db=helper.getWritableDatabase();
	}
	
	public void closeDB(){
		db.close();
	}
}
