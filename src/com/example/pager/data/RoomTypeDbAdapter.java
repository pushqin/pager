package com.example.pager.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RoomTypeDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String NAME = "name";
	
	private static final String DATABASE_TABLE = "roomType";
	
	/** @see getInstance(Context) */
	private RoomTypeDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static RoomTypeDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static RoomTypeDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new RoomTypeDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new roomType. If the roomType is successfully created return the
	 * new rowId for that roomType, otherwise return a -1 to indicate failure.
	 * 
	 * @param name
	 * @return rowId or -1 if failed
	 */
	public long createRoomType(String name)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void createRoomTypes(List<String> rooms)
	{
		db.beginTransaction();
		try
		{
			ContentValues initialValues = new ContentValues();
			for (String room : rooms)
			{
				initialValues.clear();
				initialValues.put(NAME, room);
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
	 * Delete the roomType with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteRoomType(long rowId)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete the roomType with the given name
	 * 
	 * @param name
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteRoomType(String name)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "='" + name + "'", null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all roomTypes in the database
	 * 
	 * @return Cursor over all roomTypes
	 */
	public Cursor getAllRoomTypes()
	{
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, NAME }, null, null, null, null, null);
	}
	
	public List<String> getAllRoomTypeNames(String language)
	{
		List<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		if (language == null || language.isEmpty())
		{
			language = "eng";
		}
		/*
		 * select translation.translatedText from roomType left join
		 * translation on roomType.name = translation.shortText
		 * left join language on translation.languageCodeId = language._id where
		 * language.languageCode = 'eng'
		 */
		sb.append("SELECT translation.translatedText ").append("FROM roomType ")
		        .append("LEFT JOIN translation ON roomType.name = translation.shortText ")
		        .append("LEFT JOIN language ON translation.languageCodeId = language._id ").append("WHERE language.languageCode = '")
		        .append(language).append("'");
		Cursor cursor = this.db.rawQuery(sb.toString(), null);
		if (cursor != null)
		{
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			{
				// The Cursor is now set to the right position
				result.add(cursor.getString(0));
			}
		}
		return result;
	}
	
	/**
	 * Return a Cursor positioned at the roomType that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching roomType, if found
	 * @throws SQLException
	 *             if roomType could not be found/retrieved
	 */
	public Cursor getRoomType(long rowId) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the roomType that matches the given name
	 * 
	 * @param name
	 * @return Cursor positioned to matching roomType, if found
	 * @throws SQLException
	 *             if roomType could not be found/retrieved
	 */
	public Cursor getRoomType(String name) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME }, ROW_ID + "='" + name + "'", null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the roomType.
	 * 
	 * @param rowId
	 * @param name
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateCar(long rowId, String name)
	{
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}
	
}
