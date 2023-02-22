package com.codinginflow.sqliterecyclerview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codinginflow.sqliterecyclerview.GroceryContract.*;

import androidx.annotation.Nullable;

class GroceryDBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "groceryList.db";
	public static final int DATABASE_VERSION = 1;

	public GroceryDBHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		final String SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
				GroceryEntry.TABLE_NAME + " (" +
				GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
				GroceryEntry.COLUMN_AMOUNT + " INTEGER NOT NULL, " +
				GroceryEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
				");";

		sqLiteDatabase.execSQL(SQL_CREATE_GROCERYLIST_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME);
		onCreate(sqLiteDatabase);
	}
}
