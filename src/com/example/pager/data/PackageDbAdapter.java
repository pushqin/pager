package com.example.pager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class PackageDbAdapter extends DbAdapter
{
	public static final String ROW_ID = "_id";
	public static final String NAME = "name";
	public static final String PACKAGE_TYPE_ID = "packageTypeId";
	public static final String ROOM_TYPE_ID = "roomTypeId";
	public static final String PACKAGER_ID = "packagerId";
	
	private static final String DATABASE_TABLE = "package";
	
	/** @see getInstance(Context) */
	private PackageDbAdapter(Context ctx)
	{
		super(ctx);
	}
	
	private static PackageDbAdapter instance = null;
	private static final String lock = "";
	
	/** Singleton instance fetcher. */
	public static PackageDbAdapter getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (lock)
			{
				if (instance == null)
				{
					instance = new PackageDbAdapter(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * Create a new package. If the package is successfully created return the
	 * new rowId for that package, otherwise return a -1 to indicate failure.
	 * 
	 * @param name
	 * @param packageTypeId
	 * @param roomTypeId
	 * @param packagerId
	 * @return rowId or -1 if failed
	 */
	public long createPackage(String name, long packageTypeId, long roomTypeId, long packagerId)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(NAME, name);
		initialValues.put(PACKAGE_TYPE_ID, packageTypeId);
		initialValues.put(ROOM_TYPE_ID, roomTypeId);
		initialValues.put(PACKAGER_ID, packagerId);
		return this.db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	/**
	 * Delete the package with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePackage(long rowId)
	{
		
		return this.db.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Return a Cursor over the list of all packages in the database
	 * 
	 * @return Cursor over all packages
	 */
	public Cursor getAllPackages()
	{
		
		return this.db.query(DATABASE_TABLE, new String[] { ROW_ID, PACKAGE_TYPE_ID, ROOM_TYPE_ID, PACKAGER_ID }, null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor positioned at the package that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching packages\, if found
	 * @throws SQLException
	 *             if package could not be found/retrieved
	 */
	public Cursor getPackage(long rowId) throws SQLException
	{
		
		Cursor mCursor = this.db.query(true, DATABASE_TABLE, new String[] { ROW_ID, PACKAGE_TYPE_ID, ROOM_TYPE_ID, PACKAGER_ID }, ROW_ID + "="
		        + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update the package.
	 * 
	 * @param rowId
	 * @param name
	 * @param packageTypeId
	 * @param roomTypeId
	 * @param packagerId
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updatePackage(long rowId, String name, long packageTypeId, long roomTypeId, long packagerId)
	{
		ContentValues args = new ContentValues();
		args.put(NAME, name);
		args.put(PACKAGE_TYPE_ID, packageTypeId);
		args.put(ROOM_TYPE_ID, roomTypeId);
		args.put(PACKAGER_ID, packagerId);
		
		return this.db.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
	}
	
}
