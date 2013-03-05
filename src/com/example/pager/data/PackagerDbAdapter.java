package com.example.pager.data;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PackagerDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String NAME = "name";
	
	private static final String DATABASE_TABLE = "packager";
	
	/** @see getInstance(Context) */
	private PackagerDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static PackagerDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static PackagerDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new PackagerDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new packager. If the packager is successfully created return the
	 * new rowId for that packager, otherwise return a -1 to indicate failure.
	 * 
	 * @param name
	 * @return rowId or -1 if failed
	 */
	public long createPackager(String name)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void createPackagers(List<String> packagers)
	{
		db.beginTransaction();
		try
		{
			ContentValues initialValues = new ContentValues();
			for (String packager : packagers)
			{
				initialValues.clear();
				initialValues.put(NAME, packager);
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
	 * Delete the packager with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePackager(long rowId)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete the packager with the given name
	 * 
	 * @param name
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePackager(String name)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "='" + name + "'", null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all packagers in the database
	 * 
	 * @return Cursor over all packagers
	 */
	public Cursor getAllPackagers()
	{
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, NAME }, null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor positioned at the packager that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching packager, if found
	 * @throws SQLException
	 *             if packager could not be found/retrieved
	 */
	public Cursor getPackager(long rowId) throws SQLException
	{
		
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the packager that matches the given name
	 * 
	 * @param name
	 * @return Cursor positioned to matching packager, if found
	 * @throws SQLException
	 *             if packager could not be found/retrieved
	 */
	public Cursor getPackager(String name) throws SQLException
	{
		
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, ROW_ID + "='" + name + "'", null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the packager.
	 * 
	 * @param rowId
	 * @param name
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updatePackager(long rowId, String name)
	{
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}
	
}
