package com.example.pager.data;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DefaultItemDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String NAME = "name";
	public static final String CATEGORY_ID = "categoryId";
	
	private static final String DATABASE_TABLE = "defaultItem";
	
	/** @see getInstance(Context) */
	private DefaultItemDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static DefaultItemDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static DefaultItemDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new DefaultItemDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new defaultItem. If the defaultItem is successfully created
	 * return the new rowId for that defaultItem, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param name
	 * @param categoryId
	 * @return rowId or -1 if failed
	 */
	public long createDefaultItem(String name, long categoryId)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		initialValues.put(CATEGORY_ID, categoryId);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void createDefaultItems(Map<String, Long> defaultItems)
	{
		db.beginTransaction();
		try
		{
			ContentValues initialValues = new ContentValues();
			for (Map.Entry<String, Long> entry : defaultItems.entrySet())
			{
				initialValues.clear();
				initialValues.put(NAME, entry.getKey());
				initialValues.put(CATEGORY_ID, entry.getValue());
				this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
			}
			db.setTransactionSuccessful();
		}
		catch (Exception ex)
		{	
			
		}
		finally
		{
			this.db.endTransaction();
		}
	}
	
	/**
	 * Delete the defaultItem with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteDefaultItem(long rowId)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete the defaultItem with the given name
	 * 
	 * @param name
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteDefaultItem(String name)
	{
		return this.db.delete(DATABASE_TABLE, NAME + "='" + name + "'", null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all defaultItems in the database
	 * 
	 * @return Cursor over all defaultItems
	 */
	public Cursor getAllDefaultItems()
	{
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, NAME, CATEGORY_ID }, null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor positioned at the defaultItem that matches the given
	 * rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching defaultItem, if found
	 * @throws SQLException
	 *             if defaultItem could not be found/retrieved
	 */
	public Cursor getDefaultItem(long rowId) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME, CATEGORY_ID }, ROW_ID + "=" + rowId, null, null, null,
		        null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the defaultItem that matches the given name
	 * 
	 * @param name
	 * @return Cursor positioned to matching defaultItem, if found
	 * @throws SQLException
	 *             if defaultItem could not be found/retrieved
	 */
	public Cursor getDefaultItem(String name) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME, CATEGORY_ID }, NAME + "='" + name + "'", null, null,
		        null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the defaultItems that matches the given
	 * categoryId
	 * 
	 * @param categoryId
	 * @return Cursor positioned to matching defaultItems, if found
	 * @throws SQLException
	 *             if defaultItems could not be found/retrieved
	 */
	public Cursor getDefaultItems(long categoryId) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME, CATEGORY_ID }, CATEGORY_ID + "=" + categoryId, null, null,
		        null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Map<String, Long> getDefaultItems(String language, long categoryId)
	{
		Map<String, Long> result = new HashMap<String, Long>();
		StringBuilder sb = new StringBuilder();
		if (language == null || language.isEmpty())
		{
			language = "eng";
		}
		/*
		 * select translation.translatedText, defaultItem._id from defaultItem left join
		 * translation on defaultItem.name = translation.shortText
		 * left join language on translation.languageCodeId = language._id where
		 * language.languageCode = 'eng' and defaultItem.categoryId = categoryId
		 */
		sb.append("SELECT translation.translatedText, defaultItem._id ").append("FROM defaultItem ")
		        .append("LEFT JOIN translation ON condition.name = translation.shortText ")
		        .append("LEFT JOIN language ON translation.languageCodeId = language._id ").append("WHERE language.languageCode = '")
		        .append(language).append("' AND defaultItem.categoryId = ").append(categoryId);
		Cursor cursor = this.db.rawQuery(sb.toString(), null);
		if (cursor != null)
		{
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				// The Cursor is now set to the right position
				result.put(cursor.getString(0), cursor.getLong(1));
			}
		}
		return result;
	}
	
	/**
	 * Update the defaultItem.
	 * 
	 * @param rowId
	 * @param name
	 * @param categoryId
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateItem(long rowId, String name, long categoryId)
	{
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		args.put(CATEGORY_ID, categoryId);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}
	
}
