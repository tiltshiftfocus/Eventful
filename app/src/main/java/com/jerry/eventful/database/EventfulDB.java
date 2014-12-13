package com.jerry.eventful.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class EventfulDB {
	private static final String DATABASE_CREATE = "create table events (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,when_db INTEGER NOT NULL);";
	private static final String DATABASE_NAME = "Eventful";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "events";
	private DatabaseHelper DBHelper;
	private Context context;
	private SQLiteDatabase db;

	public EventfulDB(Context paramContext) {
		context = paramContext;
		DBHelper = new DatabaseHelper(paramContext);
	}

	public boolean addItem(ContentValues paramContentValues) {
		if (paramContentValues == null) {
			return false;
		}
		return db.insert("events", null, paramContentValues) > 0L;
	}

	public Cursor all() {
		return db.rawQuery("SELECT * FROM events ORDER BY when_db desc", null);
	}

	public void clear() {
		db.execSQL("DROP TABLE IF EXISTS events");
		db.execSQL("create table events (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,when_db INTEGER NOT NULL);");
	}

	public void close() {
		DBHelper.close();
		db = null;
	}

	public void deleteItem(int paramInt) {
		SQLiteDatabase localSQLiteDatabase = db;
		String[] arrayOfString = new String[1];
		Integer value = paramInt;
		arrayOfString[0] = value.toString();
		localSQLiteDatabase.delete("events", "id=?", arrayOfString);
	}

	public EventfulDB open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public boolean updateItem(ContentValues paramContentValues, int paramInt){
		if (paramContentValues == null) {
			return false;
		}
		SQLiteDatabase localSQLiteDatabase = db;
		String[] arrayOfString = new String[1];
		Integer value = paramInt;
		arrayOfString[0] = value.toString();
		return localSQLiteDatabase.update("events", paramContentValues, "id=?", arrayOfString) > 0L;
	}


}