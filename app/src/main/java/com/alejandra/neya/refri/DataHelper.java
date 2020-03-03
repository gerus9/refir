package com.alejandra.neya.refri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "refri_db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_ITEMS = "items";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_WEIGTH = "weigth";
	private static final String KEY_DATE = "date";

	public DataHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Define a primary key
		String CREATE_POSTS_TABLE = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY,%s TEXT ,%s TEXT ,%s TEXT)", TABLE_ITEMS, KEY_ID, KEY_NAME, KEY_WEIGTH,
												  KEY_DATE);

		db.execSQL(CREATE_POSTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion != newVersion) {
			// Simplest implementation is to drop all old tables and recreate them
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
			onCreate(db);
		}
	}

	void insert(Item item) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, item.getId());
		values.put(KEY_NAME, item.getName());
		values.put(KEY_WEIGTH, item.getWeigth());
		values.put(KEY_DATE, item.getDate());

		db.insert(TABLE_ITEMS, null, values);
	}

	void remove(Item item) {
		SQLiteDatabase db = getWritableDatabase();

		String selection = KEY_ID + " LIKE ?";
		String[] selectionArgs = {item.getId()};

		db.delete(TABLE_ITEMS, selection, selectionArgs);
	}

	public List<Item> getListItems() {

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);


		List arrayList = new ArrayList<>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
				String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
				String weigth = cursor.getString(cursor.getColumnIndex(KEY_WEIGTH));
				String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));

				Item item = new Item();
				item.setId(id);
				item.setName(name);
				item.setWeigth(weigth);
				item.setDate(Long.parseLong(date));

				arrayList.add(item);
			}
			cursor.close();
		}

		return arrayList;
	}
}
