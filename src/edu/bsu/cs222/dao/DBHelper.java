package edu.bsu.cs222.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "word.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS book (id integer PRIMARY KEY autoincrement, name text)");
		db.execSQL("CREATE TABLE IF NOT EXISTS word (id integer PRIMARY KEY autoincrement, bookId Integer,name text, comment text,flag int)");
		
		db.execSQL("insert into book values(null,'"+BookDao.UNKNOWN+"')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
