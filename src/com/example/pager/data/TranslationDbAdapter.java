package com.example.pager.data;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.pager.model.TranslationStruct;

public class TranslationDbAdapter extends DbAdapter
{
	public static final String SHORT_TEXT = "shortText";
	public static final String LANGUAGE_ID = "languageCodeId";
	public static final String TRANSLATED_TEXT = "translatedText";
	
	private static final String DATABASE_TABLE = "translation";
	
	/** @see getInstance(Context) */
	private TranslationDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static TranslationDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static TranslationDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new TranslationDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new translation. If the translation is successfully created
	 * return the new rowId for that translation, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param languageCodeId
	 * @param languageCode
	 * @param translatedText
	 * @return rowId or -1 if failed
	 */
	public long createTranslation(String shortText, long languageCodeId, String translatedText)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(SHORT_TEXT, shortText);
		initialValues.put(LANGUAGE_ID, languageCodeId);
		initialValues.put(TRANSLATED_TEXT, translatedText);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void createTranslations(List<TranslationStruct> translations)
	{
		db.beginTransaction();
		try
		{
			ContentValues initialValues = new ContentValues();
			for (TranslationStruct translationStruct : translations)
			{
				initialValues.clear();
				initialValues.put(SHORT_TEXT, translationStruct.shortText);
				initialValues.put(LANGUAGE_ID, translationStruct.languageCodeId);
				initialValues.put(TRANSLATED_TEXT, translationStruct.translatedText);
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
	 * Delete the translation with the given rowId
	 * 
	 * @param shortText
	 * @param languageCodeId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteTranslation(String shortText, long languageCodeId)
	{
		return this.db.delete(DATABASE_TABLE, SHORT_TEXT + "=" + shortText + " AND " + LANGUAGE_ID + "=" + languageCodeId, null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all translations in the database
	 * 
	 * @return Cursor over all translations
	 */
	public Cursor getAllTranslations()
	{
		
		return this.db.query(DATABASE_TABLE, new String[] { SHORT_TEXT, LANGUAGE_ID, TRANSLATED_TEXT }, null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor positioned at the translation that matches the given
	 * rowId
	 * 
	 * @param shortText
	 * @param languageCodeId
	 * @return Cursor positioned to matching translation, if found
	 * @throws SQLException
	 *             if translation could not be found/retrieved
	 */
	public Cursor getTranslations(String shortText, long languageCodeId) throws SQLException
	{
		
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { SHORT_TEXT, LANGUAGE_ID, TRANSLATED_TEXT }, SHORT_TEXT + "=" + shortText + " AND "
		        + LANGUAGE_ID + "=" + languageCodeId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the translation.
	 * 
	 * @param shortText
	 * @param languageCodeId
	 * @param translatedText
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateItem(String shortText, long languageCodeId, String translatedText)
	{
		ContentValues args = new ContentValues();
		args.put(SHORT_TEXT, shortText);
		args.put(LANGUAGE_ID, languageCodeId);
		args.put(TRANSLATED_TEXT, translatedText);
		
		return this.db.update(DATABASE_TABLE, args, SHORT_TEXT + "=" + shortText + " AND " + LANGUAGE_ID + "=" + languageCodeId, null) > 0;
	}
	
}
