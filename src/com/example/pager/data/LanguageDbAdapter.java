package com.example.pager.data;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LanguageDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String CODE = "languageCode";
	
	private static final String DATABASE_TABLE = "language";
	
	/** @see getInstance(Context) */
	private LanguageDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static LanguageDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static LanguageDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new LanguageDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new language. If the language is successfully created return the
	 * new rowId for that language, otherwise return a -1 to indicate failure.
	 * 
	 * @param code
	 * @return rowId or -1 if failed
	 */
	public long createLanguage(String code)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(CODE, code);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void createLanguages(List<String> codes)
	{
		db.beginTransaction();
		try
		{
			ContentValues initialValues = new ContentValues();
			for (String code : codes)
			{
				initialValues.clear();
				initialValues.put(CODE, code);
				this.db.insert(DATABASE_TABLE, null, initialValues);
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
	 * Delete the language with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteLanguage(long rowId)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete the language with the given code
	 * 
	 * @param code
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteLanguage(String code)
	{
		return this.db.delete(DATABASE_TABLE, CODE + "='" + code + "'", null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all languages in the database
	 * 
	 * @return Cursor over all languages
	 */
	public Cursor getAllLanguages()
	{
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, CODE }, null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor positioned at the language that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching language, if found
	 * @throws SQLException
	 *             if language could not be found/retrieved
	 */
	public Cursor getLanguage(long rowId) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, CODE }, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the language that matches the given code
	 * 
	 * @param code
	 * @return Cursor positioned to matching language, if found
	 * @throws SQLException
	 *             if language could not be found/retrieved
	 */
	public Cursor getLanguage(String code) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, CODE }, CODE + "='" + code + "'", null, null, null, null, null);
		
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the language.
	 * 
	 * @param rowId
	 * @param code
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateCategory(long rowId, String code)
	{
		ContentValues args = new ContentValues();
		args.put(CODE, code);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}
}
