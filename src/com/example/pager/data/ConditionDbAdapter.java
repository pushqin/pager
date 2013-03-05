package com.example.pager.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ConditionDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String NAME = "name";
	
	private static final String DATABASE_TABLE = "condition";
	
	/** @see getInstance(Context) */
	private ConditionDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static ConditionDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static ConditionDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new ConditionDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new condition. If the condition is successfully created return
	 * the new rowId for that condition, otherwise return a -1 to indicate
	 * failure.
	 * 
	 * @param name
	 * @return rowId or -1 if failed
	 */
	public long createCondition(String name)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void createConditions(List<String> conditions)
	{
		db.beginTransaction();
		try
		{
			ContentValues initialValues = new ContentValues();
			for (String condition : conditions)
			{
				initialValues.clear();
				initialValues.put(NAME, condition);
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
	 * Delete the condition with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteCondition(long rowId)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete the condition with the given name
	 * 
	 * @param name
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteCondition(String name)
	{
		return this.db.delete(DATABASE_TABLE, NAME + "='" + name + "'", null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all conditions in the database
	 * 
	 * @return Cursor over all conditions
	 */
	public Cursor getAllConditions()
	{
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, NAME }, null, null, null, null, null);
	}
	
	public Map<String, Long> getAllConditionNames(String language)
	{
		Map<String, Long> result = new HashMap<String, Long>();
		StringBuilder sb = new StringBuilder();
		if (language == null || language.isEmpty())
		{
			language = "eng";
		}
		/*
		 * select translation.translatedText, condition._id from condition left join
		 * translation on condition.name = translation.shortText
		 * left join language on translation.languageCodeId = language._id where
		 * language.languageCode = 'eng'
		 */
		sb.append("SELECT translation.translatedText, condition._id ").append("FROM condition ")
		        .append("LEFT JOIN translation ON condition.name = translation.shortText ")
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
	 * Return a Cursor positioned at the condition that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching condition, if found
	 * @throws SQLException
	 *             if condition could not be found/retrieved
	 */
	public Cursor getCondition(long rowId) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the condition that matches the given name
	 * 
	 * @param name
	 * @return Cursor positioned to matching condition, if found
	 * @throws SQLException
	 *             if condition could not be found/retrieved
	 */
	public Cursor getCondition(String name) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, NAME + "='" + name + "'", null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the condition.
	 * 
	 * @param rowId
	 * @param name
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateCondition(long rowId, String name)
	{
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}
	
}
