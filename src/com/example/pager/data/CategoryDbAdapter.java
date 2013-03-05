package com.example.pager.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String NAME = "name";
	
	private static final String DATABASE_TABLE = "category";
	
	/** @see getInstance(Context) */
	private CategoryDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static CategoryDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static CategoryDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new CategoryDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new category. If the category is successfully created return the
	 * new rowId for that category, otherwise return a -1 to indicate failure.
	 * 
	 * @param name
	 * @return rowId or -1 if failed
	 */
	public long createCategory(String name)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void createCategories(List<String> categories)
	{
		db.beginTransaction();
		try
		{
			ContentValues initialValues = new ContentValues();
			for (String category : categories)
			{
				initialValues.clear();
				initialValues.put(NAME, category);
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
	 * Delete the category with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteCategory(long rowId)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete the category with the given name
	 * 
	 * @param name
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteCategory(String name)
	{
		return this.db.delete(DATABASE_TABLE, NAME + "='" + name + "'", null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all categories in the database
	 * 
	 * @return Cursor over all categories
	 */
	public Cursor getAllCategories()
	{
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, NAME }, null, null, null, null, null);
	}
	
	public Map<String, Long> getAllCategoryNames(String language)
	{
		Map<String, Long> result = new HashMap<String, Long>();
		StringBuilder sb = new StringBuilder();
		if (language == null || language.isEmpty())
		{
			language = "eng";
		}
		/*
		 * select translation.translatedText, category._id from category left join
		 * translation on category.name = translation.shortText
		 * left join language on translation.languageCodeId = language._id where
		 * language.languageCode = 'eng'
		 */
		sb.append("SELECT translation.translatedText, category._id ").append("FROM category ")
		        .append("LEFT JOIN translation ON category.name = translation.shortText ")
		        .append("LEFT JOIN language ON translation.languageCodeId = language._id ").append("WHERE language.languageCode = '")
		        .append(language).append("'");
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
	 * Return a Cursor positioned at the category that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching category, if found
	 * @throws SQLException
	 *             if category could not be found/retrieved
	 */
	public Cursor getCategory(long rowId) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the category that matches the given name
	 * 
	 * @param name
	 * @return Cursor positioned to matching category, if found
	 * @throws SQLException
	 *             if category could not be found/retrieved
	 */
	public Cursor getCategory(String name) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, NAME + "='" + name + "'", null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the category.
	 * 
	 * @param rowId
	 * @param name
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateCategory(long rowId, String name)
	{
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}	
}
