package com.example.pager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItemDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String NAME = "name";
	public static final String HEIGHT = "height";
	public static final String WIDTH = "width";
	public static final String LENGTH = "length";
	public static final String SERIAL_NUMBER = "serialNumber";
	public static final String NOTES = "notes";
	public static final String CONDITION_ID = "conditionId";
	public static final String CATEGORY_ID = "categoryId";
	
	private static final String DATABASE_TABLE = "item";
	
	/** @see getInstance(Context) */
	private ItemDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static ItemDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static ItemDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new ItemDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new item. If the item is successfully created return the new
	 * rowId for that item, otherwise return a -1 to indicate failure.
	 * 
	 * @param name
	 * @param height
	 * @param width
	 * @param length
	 * @param serialNumber
	 * @param notes
	 * @param conditionId
	 * @param categoryId
	 * @return rowId or -1 if failed
	 */
	public long createItem(String name, double height, double width, double length, String serialNumber, String notes, long conditionId,
	        long categoryId)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		initialValues.put(HEIGHT, height);
		initialValues.put(WIDTH, width);
		initialValues.put(LENGTH, length);
		initialValues.put(SERIAL_NUMBER, serialNumber);
		initialValues.put(NOTES, notes);
		initialValues.put(CONDITION_ID, conditionId);
		initialValues.put(CATEGORY_ID, categoryId);
		return this.db.insertWithOnConflict(DATABASE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
/*	public void createItems(List<Item> items)
	{
		db.beginTransaction();
		try
		{
//			ContentValues initialValues = new ContentValues();
			Dimension itemDimension;
			StringBuilder sb = new StringBuilder();
			for (Item item : items)
			{
				itemDimension = item.getItemDimension() == null ? new Dimension() : item.getItemDimension();
				sb.setLength(0);
				
				sb.append("INSERT INTO item (")
				.append(NAME).append(", ")
				.append(HEIGHT).append(", ")
				.append(WIDTH).append(", ")
				.append(LENGTH).append(", ")
				.append(SERIAL_NUMBER).append(", ")
				.append(NOTES).append(")")
				.append("VALUES (")
				.append(item.getName())
				.append(itemDimension.getHeight())
				.append(itemDimension.getWidth())
				.append(itemDimension.getLength())
				.append(item.getSerialNumber())
				.append(item.getNotes());
				db.execSQL(sb.toString());
				initialValues.clear();
				initialValues.put(NAME, item.getName());
				initialValues.put(HEIGHT, item.getItemDimension().getHeight());
				initialValues.put(WIDTH, item.getItemDimension().getWidth());
				initialValues.put(LENGTH, item.getItemDimension().getLength());
				initialValues.put(SERIAL_NUMBER, item.getSerialNumber());
				initialValues.put(NOTES, item.getNotes());
				ConditionDbAdapter.getInstance(this.context).getCondition(item.getCondition());
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
	}*/
	
	/**
	 * Delete the item with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteItem(long rowId)
	{
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Delete the item with the given name
	 * 
	 * @param name
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteItem(String name)
	{
		return this.db.delete(DATABASE_TABLE, NAME + "='" + name + "'", null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all items in the database
	 * 
	 * @return Cursor over all items
	 */
	public Cursor getAllItems()
	{
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, NAME, HEIGHT, WIDTH, LENGTH, SERIAL_NUMBER, NOTES, CONDITION_ID, CATEGORY_ID },
		        null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor positioned at the item that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching item, if found
	 * @throws SQLException
	 *             if item could not be found/retrieved
	 */
	public Cursor getItem(long rowId) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME, HEIGHT, WIDTH, LENGTH, SERIAL_NUMBER, NOTES, CONDITION_ID,
		        CATEGORY_ID }, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the item that matches the given name
	 * 
	 * @param name
	 * @return Cursor positioned to matching item, if found
	 * @throws SQLException
	 *             if item could not be found/retrieved
	 */
	public Cursor getItem(String name) throws SQLException
	{
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, NAME, HEIGHT, WIDTH, LENGTH, SERIAL_NUMBER, NOTES, CONDITION_ID,
		        CATEGORY_ID }, NAME + "='" + name + "'", null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the item.
	 * 
	 * @param rowId
	 * @param name
	 * @param height
	 * @param width
	 * @param length
	 * @param serialNumber
	 * @param notes
	 * @param conditionId
	 * @param categoryId
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateItem(long rowId, String name, double height, double width, double length, String serialNumber, String notes,
	        long conditionId, long categoryId)
	{
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		args.put(HEIGHT, height);
		args.put(WIDTH, width);
		args.put(LENGTH, length);
		args.put(SERIAL_NUMBER, serialNumber);
		args.put(NOTES, notes);
		args.put(CONDITION_ID, conditionId);
		args.put(CATEGORY_ID, categoryId);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}
	
}
