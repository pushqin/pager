package com.example.pager.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.pager.model.TranslationStruct;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter
{
	// this is a private object extended/defined later
	private DatabaseHelper dbHelper;
	
	// create a SQLiteDatabase object
	protected SQLiteDatabase db;
	
	// constant for the context (for the SQLiteOpenHelper)
	protected final Context context;
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		private static final String DATABASE_NAME = "packageList.db";
		
		private static final int DATABASE_VERSION = 1;
		
		private static final String CREATE_TABLE_LANGUAGE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS language")
		        .append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT,").append(" languageCode TEXT NOT NULL,")
		        .append(" UNIQUE (languageCode) ON CONFLICT IGNORE);").toString();
		
		private static final String CREATE_TABLE_TRANSLATION = new StringBuilder().append("CREATE TABLE IF NOT EXISTS translation ")
		        .append("(shortText TEXT  NOT NULL, ").append("languageCodeId INTEGER NOT NULL, ").append("translatedText TEXT NOT NULL, ")
		        .append("PRIMARY KEY (shortText, languageCodeId));").toString();
		
		private static final String CREATE_TABLE_CONDITION = new StringBuilder().append("CREATE TABLE IF NOT EXISTS condition ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append(" UNIQUE (name) ON CONFLICT IGNORE);")
		        .toString();
		
		private static final String CREATE_TABLE_CATEGORY = new StringBuilder().append("CREATE TABLE IF NOT EXISTS category ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append(" UNIQUE (name) ON CONFLICT IGNORE);")
		        .toString();
		
		private static final String CREATE_TABLE_PACKAGE_TYPE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS packageType ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append(" UNIQUE (name) ON CONFLICT IGNORE);")
		        .toString();
		
		private static final String CREATE_TABLE_ROOM_TYPE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS roomType ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append(" UNIQUE (name) ON CONFLICT IGNORE);")
		        .toString();
		
		private static final String CREATE_TABLE_PACKAGER = new StringBuilder().append("CREATE TABLE IF NOT EXISTS packager ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append(" UNIQUE (name) ON CONFLICT IGNORE);")
		        .toString();
		
		private static final String CREATE_TABLE_DEFAULT_ITEM = new StringBuilder().append("CREATE TABLE IF NOT EXISTS defaultItem ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append("categoryId INTEGER NOT NULL, ")
		        .append("FOREIGN KEY(categoryId) REFERENCES category(_id) ON DELETE CASCADE, ").append(" UNIQUE (name) ON CONFLICT IGNORE);")
		        .toString();
		
		private static final String CREATE_TABLE_PACKAGE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS package ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append("packageTypeId INTEGER NOT NULL, ")
		        .append("roomTypeId INTEGER NOT NULL, ").append("packagerId INTEGER NOT NULL, ")
		        .append("FOREIGN KEY(packageTypeId) REFERENCES packageType(_id) ON DELETE CASCADE, ")
		        .append("FOREIGN KEY(roomTypeId) REFERENCES roomType(_id) ON DELETE CASCADE, ")
		        .append("FOREIGN KEY(packagerId) REFERENCES packager(_id) ON DELETE CASCADE);").toString();
		
		private static final String CREATE_TABLE_ITEM = new StringBuilder().append("CREATE TABLE IF NOT EXISTS item ")
		        .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NOT NULL, ").append("height REAL, ").append("width REAL, ")
		        .append("length REAL, ").append("serialNumber TEXT, ").append("notes TEXT, ").append("defaultItemId INTEGER NOT NULL, ")
		        .append("conditionId INTEGER NOT NULL, ").append("packageId INTEGER NOT NULL, ")
		        .append("FOREIGN KEY(defaultItemId) REFERENCES defaultItem(_id) ON DELETE CASCADE, ")
		        .append("FOREIGN KEY(conditionId) REFERENCES condition(_id) ON DELETE CASCADE, ")
		        .append("FOREIGN KEY(packageId) REFERENCES package(_id) ON DELETE CASCADE);").toString();
		
		/**
		 * Constructor - takes the context to allow the database to be
		 * opened/created
		 * 
		 * @param ctx
		 *            the Context within which to work
		 */
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(CREATE_TABLE_LANGUAGE);
			db.execSQL(CREATE_TABLE_TRANSLATION);
			db.execSQL(CREATE_TABLE_CONDITION);
			db.execSQL(CREATE_TABLE_CATEGORY);
			db.execSQL(CREATE_TABLE_PACKAGE_TYPE);
			db.execSQL(CREATE_TABLE_ROOM_TYPE);
			db.execSQL(CREATE_TABLE_PACKAGER);
			db.execSQL(CREATE_TABLE_DEFAULT_ITEM);
			db.execSQL(CREATE_TABLE_PACKAGE);
			db.execSQL(CREATE_TABLE_ITEM);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS \"language\"");
			db.execSQL("DROP TABLE IF EXISTS \"translation\"");
			db.execSQL("DROP TABLE IF EXISTS \"condition\"");
			db.execSQL("DROP TABLE IF EXISTS \"category\"");
			db.execSQL("DROP TABLE IF EXISTS \"packageType\"");
			db.execSQL("DROP TABLE IF EXISTS \"roomType\"");
			db.execSQL("DROP TABLE IF EXISTS \"packager\"");
			db.execSQL("DROP TABLE IF EXISTS \"defaultItem\"");
			db.execSQL("DROP TABLE IF EXISTS \"package\"");
			db.execSQL("DROP TABLE IF EXISTS \"item\"");
			onCreate(db);
		}
	}
	
	public DbAdapter(Context ctx)
	{
		this.context = ctx;
		this.dbHelper = new DatabaseHelper(this.context);
		this.db = this.dbHelper.getWritableDatabase();
	}
	
	protected void finalize() throws Throwable
	{
		this.close();
		super.finalize();
	}
	
	/**
	 * Opens the database. If it cannot be opened, try to create a new instance
	 * of the database. If it cannot be created, throw an exception to signal
	 * the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public void open() throws SQLException
	{
		this.fillDataBase();
	}
	
	/**
	 * Closes the database
	 */
	public void close()
	{
		this.db.close();
		this.dbHelper.close();
	}
	
	private Map<String, List<String>> engTexts = new HashMap<String, List<String>>();
	private Map<String, List<String>> hebTexts = new HashMap<String, List<String>>();
	private Map<String, List<String>> shortTexts = new HashMap<String, List<String>>();
	
	public void fillDataBase()
	{
		LanguageDbAdapter languageDB = LanguageDbAdapter.getInstance(this.context);
		Cursor cursor;
		long engIndex = languageDB.createLanguage("eng");
		if (engIndex == -1)
		{
			cursor = languageDB.getLanguage("eng");
			cursor.moveToFirst();
			engIndex = cursor.getLong(0);
		}
		long hebIndex = languageDB.createLanguage("heb");
		if (hebIndex == -1)
		{
			cursor = languageDB.getLanguage("heb");
			cursor.moveToFirst();
			hebIndex = cursor.getLong(0);
		}
		
		createEnglishDictionary();
		createHebrewDictionary();
		createShortWordsDictionary();
		
		TranslationDbAdapter translationDB = TranslationDbAdapter.getInstance(this.context);
		List<TranslationStruct> translationList = new ArrayList<TranslationStruct>();
		String key;
		List<String> shortTextValues = new ArrayList<String>();
		List<String> engTextValues = new ArrayList<String>();
		List<String> hebTextValues = new ArrayList<String>();
		int shortSize;
		int engSize;
		int hebSize;
		for (Map.Entry<String, List<String>> entry : this.shortTexts.entrySet())
		{
			key = entry.getKey();
			shortTextValues = entry.getValue();
			engTextValues = this.engTexts.get(key);
			hebTextValues = this.hebTexts.get(key);
			
			shortSize = shortTextValues.size();
			engSize = engTextValues.size();
			hebSize = hebTextValues.size();
			if (shortSize != engSize || shortSize != hebSize)
			{
				System.out.println("Sizes are not equals");
			}
			// Check that the length of all lists are equals
			for (int i = 0; i < shortTextValues.size(); i++)
			{
				translationList.add(new TranslationStruct(shortTextValues.get(i), engIndex, engTextValues.get(i)));
				translationList.add(new TranslationStruct(shortTextValues.get(i), hebIndex, hebTextValues.get(i)));
			}
		}
		translationDB.createTranslations(translationList);
		
		ConditionDbAdapter conditionDB = ConditionDbAdapter.getInstance(this.context);
		List<String> objectList = new ArrayList<String>();
		objectList.add("cond_fine");
		objectList.add("cond_broken");
		objectList.add("cond_cracked");
		objectList.add("cond_scratched");
		objectList.add("cond_stained");
		objectList.add("cond_torn");
		objectList.add("cond_rusty");
		objectList.add("cond_peeledColor");
		objectList.add("cond_used");
		conditionDB.createConditions(objectList);
		
		CategoryDbAdapter categoryDB = CategoryDbAdapter.getInstance(this.context);
		objectList.clear();
		objectList.add("categ_electronics");
		objectList.add("categ_largeAppl");
		objectList.add("categ_smallAppl");
		objectList.add("categ_furniture");
		objectList.add("categ_bedrooms");
		objectList.add("categ_nursery");
		objectList.add("categ_tablesChairs");
		objectList.add("categ_misc");
		objectList.add("categ_kitchen");
		objectList.add("categ_cloth");
		objectList.add("categ_ornsAndExpens");
		objectList.add("categ_textile");
		objectList.add("categ_sport");
		objectList.add("categ_garden");
		objectList.add("categ_musical");
		categoryDB.createCategories(objectList);
		
		PackagerDbAdapter packagerDB = PackagerDbAdapter.getInstance(this.context);
		objectList.clear();
		objectList.add("Packager 1");
		objectList.add("Packager 2");
		objectList.add("Packager 3");
		objectList.add("Packager 4");
		objectList.add("Packager 5");
		objectList.add("Packager 6");
		packagerDB.createPackagers(objectList);
		
		RoomTypeDbAdapter roomTypeDB = RoomTypeDbAdapter.getInstance(this.context);
		objectList.clear();
		objectList.add("room_general");
		objectList.add("room_washroom");
		objectList.add("room_kitchen");
		objectList.add("room_bedroom");
		objectList.add("room_kids1");
		objectList.add("room_kids2");
		objectList.add("room_salon");
		objectList.add("room_nursery");
		objectList.add("room_bathroom");
		roomTypeDB.createRoomTypes(objectList);
		
		PackageTypeDbAdapter packageTypeDB = PackageTypeDbAdapter.getInstance(this.context);
		objectList.clear();
		objectList.add("packType_general");
		objectList.add("packType_glass");
		objectList.add("packType_clothes");
		objectList.add("packType_books");
		packageTypeDB.createPackageTypes(objectList);
		
		DefaultItemDbAdapter defaultItemDB = DefaultItemDbAdapter.getInstance(this.context);
		Map<String, Long> objectMap = new HashMap<String, Long>();
		
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_electronics\"", null, null, null, null, null);
		cursor.moveToFirst();
		long index = cursor.getLong(0);
		objectMap.put("catEl_general", index);
		objectMap.put("catEl_tvLarge", index);
		objectMap.put("catEl_tvSmall", index);
		objectMap.put("catEl_plasma", index);
		objectMap.put("catEl_movieProj", index);
		objectMap.put("catEl_slideProj", index);
		objectMap.put("catEl_reciever", index);
		objectMap.put("catEl_stereo", index);
		objectMap.put("catEl_recordPlayer", index);
		objectMap.put("catEl_compactDisk", index);
		objectMap.put("catEl_tapeCassette", index);
		objectMap.put("catEl_Radio", index);
		objectMap.put("catEl_speakersSet", index);
		objectMap.put("catEl_surroundSystem", index);
		objectMap.put("catEl_stereoApml", index);
		objectMap.put("catEl_tuner", index);
		objectMap.put("catEl_headphones", index);
		objectMap.put("catEl_vcr", index);
		objectMap.put("catEl_dvdPlayer", index);
		objectMap.put("catEl_records", index);
		objectMap.put("catEl_cassette", index);
		objectMap.put("catEl_cd", index);
		objectMap.put("catEl_dvd", index);
		objectMap.put("catEl_tvGames", index);
		objectMap.put("catEl_books", index);
		objectMap.put("catEl_computer", index);
		objectMap.put("catEl_monitor", index);
		objectMap.put("catEl_keyboard", index);
		objectMap.put("catEl_printer", index);
		objectMap.put("catEl_scanner", index);
		objectMap.put("catEl_fax", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_largeAppl\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catLargeAppl_general", index);
		objectMap.put("catLargeAppl_refrigirator", index);
		objectMap.put("catLargeAppl_deepFreezer", index);
		objectMap.put("catLargeAppl_dishwasher", index);
		objectMap.put("catLargeAppl_washingMachine", index);
		objectMap.put("catLargeAppl_clothesDryer", index);
		objectMap.put("catLargeAppl_stove/range", index);
		objectMap.put("catLargeAppl_stoveTop", index);
		objectMap.put("catLargeAppl_extractorFan", index);
		objectMap.put("catLargeAppl_microWaveOven", index);
		objectMap.put("catLargeAppl_airConditioner", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_smallAppl\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catSmallAppl_general", index);
		objectMap.put("catSmallAppl_vacuumCleaner", index);
		objectMap.put("catSmallAppl_blender", index);
		objectMap.put("catSmallAppl_coffeMaker", index);
		objectMap.put("catSmallAppl_electricKettle", index);
		objectMap.put("catSmallAppl_foodProcessor", index);
		objectMap.put("catSmallAppl_juiceExtractor", index);
		objectMap.put("catSmallAppl_mixer", index);
		objectMap.put("catSmallAppl_toastOven", index);
		objectMap.put("catSmallAppl_toaster", index);
		objectMap.put("catSmallAppl_drill", index);
		objectMap.put("catSmallAppl_iron", index);
		objectMap.put("catSmallAppl_fan/ventilator", index);
		objectMap.put("catSmallAppl_heater", index);
		objectMap.put("catSmallAppl_sewingMachine", index);
		objectMap.put("catSmallAppl_typewriter", index);
		objectMap.put("catSmallAppl_hairDryer", index);
		objectMap.put("catSmallAppl_telephone", index);
		objectMap.put("catSmallAppl_clockRadio", index);
		objectMap.put("catSmallAppl_grandfatherClock", index);
		objectMap.put("catSmallAppl_wallClock", index);
		objectMap.put("catSmallAppl_electricBlanket", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_furniture\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catFurn_general", index);
		objectMap.put("catFurn_armchair", index);
		objectMap.put("catFurn_sofa2Seat", index);
		objectMap.put("catFurn_sofa3Seat", index);
		objectMap.put("catFurn_sofa", index);
		objectMap.put("catFurn_stool/footrest", index);
		objectMap.put("catFurn_rockingChair", index);
		objectMap.put("catFurn_bookcase", index);
		objectMap.put("catFurn_shelves", index);
		objectMap.put("catFurn_buffet", index);
		objectMap.put("catFurn_cornerCabinet", index);
		objectMap.put("catFurn_chinaCloset", index);
		objectMap.put("catFurn_standingLamp", index);
		objectMap.put("catFurn_lamp", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_tablesChairs\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catTables_general", index);
		objectMap.put("catTables_coffeeTable", index);
		objectMap.put("catTables_desk", index);
		objectMap.put("catTables_deskChair", index);
		objectMap.put("catTables_diningTable", index);
		objectMap.put("catTables_diningTableChairs", index);
		objectMap.put("catTables_tvCabinet", index);
		objectMap.put("catTables_stereoCabinet", index);
		objectMap.put("catTables_cornerTable", index);
		objectMap.put("catTables_kitchenTable", index);
		objectMap.put("catTables_kitchenChairs", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_bedrooms\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catBed_general", index);
		objectMap.put("catBed_doubleBed", index);
		objectMap.put("catBed_doubleMattress", index);
		objectMap.put("catBed_dressingNightTable", index);
		objectMap.put("catBed_chestOfDrawers", index);
		objectMap.put("catBed_dressingTable", index);
		objectMap.put("catBed_mirror", index);
		objectMap.put("catBed_closet", index);
		objectMap.put("catBed_childrensBed", index);
		objectMap.put("catBed_single", index);
		objectMap.put("catBed_singleMattress", index);
		objectMap.put("catBed_sofaBed", index);
		objectMap.put("catBed_wardrobe/closet", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_nursery\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catNurs_general", index);
		objectMap.put("catNurs_playPen", index);
		objectMap.put("catNurs_pram", index);
		objectMap.put("catNurs_carSafetySeat", index);
		objectMap.put("catNurs_highChair", index);
		objectMap.put("catNurs_crib", index);
		objectMap.put("catNurs_mattressBaby", index);
		objectMap.put("catNurs_babyDresser", index);
		objectMap.put("catNurs_humidifier", index);
		objectMap.put("catNurs_toys", index);
		objectMap.put("catNurs_childrenClothing", index);
		objectMap.put("catNurs_childrenBlankets", index);
		objectMap.put("catNurs_childrenLinens", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_misc\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catMisc_general", index);
		objectMap.put("catMisc_ironingBoard", index);
		objectMap.put("catMisc_shoeCabinet", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_kitchen\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catKitch_general", index);
		objectMap.put("catKitch_potspans", index);
		objectMap.put("catKitch_dishes", index);
		objectMap.put("catKitch_utensils", index);
		objectMap.put("catKitch_glassware", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_ornsAndExpens\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catOrnsAndExp_general", index);
		objectMap.put("catOrnsAndExp_ornaments/bricABrac", index);
		objectMap.put("catOrnsAndExp_rugs", index);
		objectMap.put("catOrnsAndExp_pictures", index);
		objectMap.put("catOrnsAndExp_chinaware", index);
		objectMap.put("catOrnsAndExp_crystalware", index);
		objectMap.put("catOrnsAndExp_painting", index);
		objectMap.put("catOrnsAndExp_statue", index);
		objectMap.put("catOrnsAndExp_videoCamera", index);
		objectMap.put("catOrnsAndExp_digitalCamera", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_textile\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catTextile_general", index);
		objectMap.put("catTextile_tableCloths", index);
		objectMap.put("catTextile_towels", index);
		objectMap.put("catTextile_pilloes", index);
		objectMap.put("catTextile_blankets", index);
		objectMap.put("catTextile_sheets", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_cloth\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catCloth_general", index);
		objectMap.put("catCloth_womenClothing", index);
		objectMap.put("catCloth_womenShoes", index);
		objectMap.put("catCloth_womenSuits", index);
		objectMap.put("catCloth_womenCoats", index);
		objectMap.put("catCloth_womenLeatherCoats", index);
		objectMap.put("catCloth_womenFurCoats", index);
		objectMap.put("catCloth_menClothing", index);
		objectMap.put("catCloth_menShoes", index);
		objectMap.put("catCloth_menSuits", index);
		objectMap.put("catCloth_menCoats", index);
		objectMap.put("catCloth_menLeatherCoats", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_sport\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catSport_general", index);
		objectMap.put("catSport_exerciseBicycle", index);
		objectMap.put("catSport_treadmill", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_garden\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catGerden_general", index);
		objectMap.put("catGerden_table", index);
		objectMap.put("catGerden_chairs", index);
		objectMap.put("catGerden_ladder", index);
		objectMap.put("catGerden_tools", index);
		objectMap.put("catGerden_serviceCabinet", index);
		objectMap.put("catGerden_barbecueGrill", index);
		defaultItemDB.createDefaultItems(objectMap);
		
		objectMap.clear();
		cursor = db.query(true, "category", new String[] { "_id" }, "name=\"categ_musical\"", null, null, null, null, null);
		cursor.moveToFirst();
		index = cursor.getLong(0);
		objectMap.put("catMusic_general", index);
		objectMap.put("catMusic_piano", index);
		objectMap.put("catMusic_electricKeyboard", index);
		objectMap.put("catMusic_guitar", index);
		defaultItemDB.createDefaultItems(objectMap);
	}
	
	private void createShortWordsDictionary()
	{
		List<String> objectList = new ArrayList<String>();
		objectList = Arrays.asList("cond_fine", "cond_broken", "cond_cracked", "cond_scratched", "cond_stained", "cond_torn", "cond_rusty",
		        "cond_peeledColor", "cond_used");
		shortTexts.put("conditions", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("categ_electronics", "categ_largeAppl", "categ_smallAppl", "categ_furniture", "categ_bedrooms", "categ_nursery",
		        "categ_tablesChairs", "categ_misc", "categ_kitchen", "categ_cloth", "categ_ornsAndExpens", "categ_textile", "categ_sport",
		        "categ_garden", "categ_musical");
		shortTexts.put("categories", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("room_general", "room_washroom", "room_kitchen", "room_bedroom", "room_kids1", "room_kids2", "room_salon",
		        "room_nursery", "room_bathroom");
		shortTexts.put("roomTypes", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("packType_general", "packType_glass", "packType_clothes", "packType_books");
		shortTexts.put("packageTypes", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catEl_general", "catEl_tvLarge", "catEl_tvSmall", "catEl_plasma", "catEl_movieProj", "catEl_slideProj",
		        "catEl_reciever", "catEl_stereo", "catEl_recordPlayer", "catEl_compactDisk", "catEl_tapeCassette", "catEl_Radio",
		        "catEl_speakersSet", "catEl_surroundSystem", "catEl_stereoApml", "catEl_tuner", "catEl_headphones", "catEl_vcr", "catEl_dvdPlayer",
		        "catEl_records", "catEl_cassette", "catEl_cd", "catEl_dvd", "catEl_tvGames", "catEl_books", "catEl_computer", "catEl_monitor",
		        "catEl_keyboard", "catEl_printer", "catEl_scanner", "catEl_fax");
		shortTexts.put("category_electronics", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catLargeAppl_general", "catLargeAppl_refrigirator", "catLargeAppl_deepFreezer", "catLargeAppl_dishwasher",
		        "catLargeAppl_washingMachine", "catLargeAppl_clothesDryer", "catLargeAppl_stove/range", "catLargeAppl_stoveTop",
		        "catLargeAppl_extractorFan", "catLargeAppl_microWaveOven", "catLargeAppl_airConditioner");
		shortTexts.put("category_largeAppliances", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catSmallAppl_general", "catSmallAppl_vacuumCleaner", "catSmallAppl_blender", "catSmallAppl_coffeMaker",
		        "catSmallAppl_electricKettle", "catSmallAppl_foodProcessor", "catSmallAppl_juiceExtractor", "catSmallAppl_mixer",
		        "catSmallAppl_toastOven", "catSmallAppl_toaster", "catSmallAppl_drill", "catSmallAppl_iron", "catSmallAppl_fan/ventilator",
		        "catSmallAppl_heater", "catSmallAppl_sewingMachine", "catSmallAppl_typewriter", "catSmallAppl_hairDryer", "catSmallAppl_telephone",
		        "catSmallAppl_clockRadio", "catSmallAppl_grandfatherClock", "catSmallAppl_wallClock", "catSmallAppl_electricBlanket");
		shortTexts.put("category_smallAppliances", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catFurn_general", "catFurn_armchair", "catFurn_sofa2Seat", "catFurn_sofa3Seat", "catFurn_sofa",
		        "catFurn_stool/footrest", "catFurn_rockingChair", "catFurn_bookcase", "catFurn_shelves", "catFurn_buffet", "catFurn_cornerCabinet",
		        "catFurn_chinaCloset", "catFurn_standingLamp", "catFurn_lamp");
		shortTexts.put("category_furniture", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catTables_general", "catTables_coffeeTable", "catTables_desk", "catTables_deskChair", "catTables_diningTable",
		        "catTables_diningTableChairs", "catTables_tvCabinet", "catTables_stereoCabinet", "catTables_cornerTable", "catTables_kitchenTable",
		        "catTables_kitchenChairs");
		shortTexts.put("category_tablesAndChairs", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catBed_general", "catBed_doubleBed", "catBed_doubleMattress", "catBed_dressingNightTable",
		        "catBed_chestOfDrawers", "catBed_dressingTable", "catBed_mirror", "catBed_closet", "catBed_childrensBed", "catBed_single Bed",
		        "catBed_singleMattress", "catBed_sofaBed", "catBed_wardrobe/closet");
		shortTexts.put("category_bedrooms", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catNurs_general", "catNurs_playPen", "catNurs_pram", "catNurs_carSafetySeat", "catNurs_highChair",
		        "catNurs_crib", "catNurs_mattressBaby", "catNurs_babyDresser", "catNurs_humidifier", "catNurs_toys", "catNurs_childrenClothing",
		        "catNurs_childrenBlankets", "catNurs_childrenLinens");
		shortTexts.put("category_nursery", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catMisc_general", "catMisc_ironingBoard", "catMisc_shoeCabinet");
		shortTexts.put("category_miscellaneous", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catKitch_general", "catKitch_potspans", "catKitch_dishes", "catKitch_utensils", "catKitch_glassware");
		shortTexts.put("category_kitchenware", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catCloth_general", "catCloth_womenClothing", "catCloth_womenShoes", "catCloth_womenSuits", "catCloth_womenCoats",
		        "catCloth_womenLeatherCoats", "catCloth_womenFurCoats", "catCloth_menClothing", "catCloth_menShoes", "catCloth_menSuits",
		        "catCloth_menCoats", "catCloth_menLeatherCoats");
		shortTexts.put("category_clothings", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catOrnsAndExp_general", "catOrnsAndExp_ornaments/bricABrac", "catOrnsAndExp_rugs", "catOrnsAndExp_pictures",
		        "catOrnsAndExp_chinaware", "catOrnsAndExp_crystalware", "catOrnsAndExp_painting", "catOrnsAndExp_statue",
		        "catOrnsAndExp_videoCamera", "catOrnsAndExp_digitalCamera");
		shortTexts.put("category_ornamentsAndExpensive", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catTextile_general", "catTextile_tableCloths", "catTextile_towels", "catTextile_pilloes", "catTextile_blankets",
		        "catTextile_sheets");
		shortTexts.put("category_textile", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catSport_general", "catSport_exerciseBicycle", "catSport_treadmill");
		shortTexts.put("category_sport", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catGerden_general", "catGerden_table", "catGerden_chairs", "catGerden_ladder", "catGerden_tools",
		        "catGerden_serviceCabinet", "catGerden_barbecueGrill");
		shortTexts.put("category_garden", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("catMusic_general", "catMusic_piano", "catMusic_electricKeyboard", "catMusic_guitar");
		shortTexts.put("category_musical", objectList);
	}
	
	private void createHebrewDictionary()
	{
		List<String> objectList = new ArrayList<String>();
		objectList = Arrays.asList("אוקיי", "שבור", "סדוק", "סרוט", "מוכתם", "קרוע", "חלוד", "צבע מקולף", "משומש");
		hebTexts.put("conditions", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("מוצרי אלקטרוניקה", "מוצרי חשמל גדולים", "מוצרי חשמל קטנים", "רהיטים", "שולחנות וכסאות", "חדרי שינה",
		        "חדר תינוקות", "שונות", "כלי מטבח", "מוצרי נוי ויקרים", "מוצרי טקסטיל", "ביגוד", "מוצרי ספורט", "אביזרים ורהיטי גן", "כלי נגינה");
		hebTexts.put("categories", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "חדר שרותים", "מטבח", "חדר שינה", "חדר שינה ילדים 1", "חדר שינה ילדים 2", "סלון", "חדר תינוקות",
		        "חדר אמבטיה");
		hebTexts.put("roomTypes", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "זכוכית", "בגדים", "ספרים");
		hebTexts.put("packageTypes", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "טלויזיה גדולה", "טלויזיה קטנה", "טלויזה", "מקרן סרטים", "מקרן שקופיות", "(מקלט(רסיבר", "מערכת סטריאו",
		        "פטיפון", "קומפקט דיסק", "טייפ קסטות", "רדיו", "זוג רמקולים", "מערכת סראונד", "מגבר", "טיונר", "אוזניות", "וידאו", "מכשיר די.וי.די",
		        "תקליטים", "קלטות", "תקליטורים", "דיסקים", "משחק טלויזה", "ספרים", "מחשב", "מסך", "מקלדת", "מדפסת", "סורק", "פקס");
		hebTexts.put("category_electronics", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "מקרר", "מקפיא", "מדיח כלים", "מכונת כביסה", "מייבש כביסה", "תנור אפיה עומד", "כיריים", "קולט אדים",
		        "מיקרוגל", "מזגן");
		hebTexts.put("category_largeAppliances", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "שואב אבק", "מערבל", "מכונת קפה", "קומקום", "מערבל מזון", "מסחטת מיץ", "מיקסר", "מצנם תנור", "מצנם",
		        "מקדחה", "מגהץ", "מאורר", "תנור חימום", "מכונת תפירה", "מכונת כתיבה", "מייבש שיער", "טלפון", "שעון רדיו", "שעון עומד", "שעון קיר",
		        "סדין חשמלי");
		hebTexts.put("category_smallAppliances", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "כורסא", "ספה דו מושבית", "ספה תלת מושבית", "ספה", "הדום", "כסא נדנדה", "כוננית ספרים", "מדפים", "מזנון",
		        "ארון פינתי", "מזנון גבוהה", "מנורה עומדת", "מנורה");
		hebTexts.put("category_furniture", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "שולחן סלון", "שולחן כתיבה", "כסא לשולחן כתיבה", "שולחן אוכל", "כסאות שולחן אוכל", "שולחן טלויזיה",
		        "שולחן סטריאו", "שולחן פינתי", "שולחן מטבח", "כסא לשולחן מטבח");
		hebTexts.put("category_tablesAndChairs", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "מיטה זוגית", "מזרן זוגי", "שידות לילה", "שידת מגירות", "שולחן איפור", "מראה", "ארון בגדים", "מיטת נוער",
		        "מיטת יחיד", "מזרון יחיד", "ספת שינה", "ארון בגדים");
		hebTexts.put("category_bedrooms", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "לול", "עגלת תינוק", "כסא בטיחות לרכב", "כסא אוכל לתינוק", "מיטת תינוק", "מזרן למיטת תינוק", "שידת תינוק",
		        "מכשיר אדים", "צעצועים", "בגדי ילדים", "שמיכות", "מצעים");
		hebTexts.put("category_nursery", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "שולחן גיהוץ", "ארון נעליים");
		hebTexts.put("category_miscellaneous", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "סירים ומחבתות", "מערכת כלי אוכל", "סכום", "כלי זכוכית");
		hebTexts.put("category_kitchenware", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "בגדי נשים", "נעלי נשים", "חליפות נשים", "מעילי גשם", "מעילי עור נשים", "מעילי פרווה נשים", "בגדי גברים",
		        "נעלי גברים", "חליפןת גברים", "מעילי גברים", "מעילי עור גברים");
		hebTexts.put("category_clothings", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays
		        .asList("כללי", "חפצי נוי,חן", "שטיחים", "תמונות", "כלי חרסינה", "קריסטל", "תמונה", "פסל", "מצלמת וידאו", "מצלמה דיגיטלית");
		hebTexts.put("category_ornamentsAndExpensive", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "מפות", "מגבות", "כריות", "סמיכות", "מצעים");
		hebTexts.put("category_textile", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "אופני כושר", "הליכון");
		hebTexts.put("category_sport", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "שולחן", "כסאות", "סולם", "כלי עבודה", "ארונית שרות", "בר בי קיו");
		hebTexts.put("category_garden", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("כללי", "פסנתר", "אורגנית", "גיטרה");
		hebTexts.put("category_musical", objectList);
	}
	
	private void createEnglishDictionary()
	{
		List<String> objectList = new ArrayList<String>();
		objectList = Arrays.asList("Fine", "Broken", "Cracked", "Scratched", "Stained", "Torn", "Rusty", "Peeled Color", "Used");
		engTexts.put("conditions", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("Electronics", "Large Appliances", "Small Appliances", "Furniture", "Bedrooms", "Nursery", "Tables And Chairs",
		        "Miscellaneous", "Kitchenware", "Clothings", "Ornaments And Expensive Items", "Textile", "Sport Equipment",
		        "Garden Furniture And Equipment", "Musical Instruments");
		engTexts.put("categories", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Washroom", "Kitchen", "Bed Room", "Kids Room 1", "Kids Room 2", "Salon", "Nursery", "BathRoom");
		engTexts.put("roomTypes", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Glass", "Clothes", "Books");
		engTexts.put("packageTypes", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "T.V - Large", "T.V - Small", "Plasma", "Movie Projector", "Slide Projector", "Reciever",
		        "Stereo System", "Record Player", "Compact Disk", "Tape Cassette", "Radio", "Speakers Set", "Surround System", "Stereo Apmlifier",
		        "Tuner", "Headphones", "VCR", "DVD Player", "Records", "Cassette", "CD's", "DVD's", "T.V Games", "Books", "Computer", "Monitor",
		        "Keyboard", "Printer", "Scanner", "Fax");
		engTexts.put("category_electronics", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Refrigirator", "Deep Freezer", "Dishwasher", "Washing Machine", "Clothes Dryer", "Stove/Range",
		        "Stove Top", "Extractor Fan", "MicroWave Oven", "Air Conditioner");
		engTexts.put("category_largeAppliances", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Vacuum Cleaner", "Blender", "Coffe Maker", "Electric Kettle", "Food Processor", "Juice Extractor",
		        "Mixer", "Toast Oven", "Toaster", "Drill", "Iron", "Fan/Ventilator", "Heater", "Sewing Machine", "Typewriter", "Hair Dryer",
		        "Telephone", "Clock Radio", "Grandfather Clock", "Wall Clock", "Electric Blanket");
		engTexts.put("category_smallAppliances", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Armchair", "Sofa 2 Seat", "Sofa 3 Seat", "Sofa", "Stool/Footrest", "Rocking Chair", "Bookcase",
		        "Shelves", "Buffet", "Corner Cabinet", "China Closet", "Standing Lamp", "Lamp");
		engTexts.put("category_furniture", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Coffee Table", "Desk", "Desk Chair", "Dining Table", "Dining Table Chairs", "T.V Cabinet",
		        "Stereo Cabinet", "Corner Table", "Kitchen Table", "Kitchen Chairs");
		engTexts.put("category_tablesAndChairs", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Double Bed", "Double Mattress", "Dressing Night Table", "Chest Of Drawers", "Dressing Table",
		        "Mirror", "Closet", "Childrens Bed", "Single Bed", "Single Mattress", "Sofa Bed", "Wardrobe/Closet");
		engTexts.put("category_bedrooms", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Play Pen", "Pram", "Car Safety Seat", "High Chair", "Crib", "Mattress baby", "Baby Dresser",
		        "Humidifier", "Toys", "Children Clothing", "Children Blankets", "Children Linens");
		engTexts.put("category_nursery", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Ironing Board", "Shoe Cabinet");
		engTexts.put("category_miscellaneous", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Pots & Pans", "Set Of Dishes", "Utensils", "Glassware");
		engTexts.put("category_kitchenware", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Women Clothing", "Women Shoes", "Women Suits", "Women Coats", "Women Leather Coats",
		        "Women Fur Coats", "Men Clothing", "Men Shoes", "Men Suits", "Men Coats", "Men Leather Coats");
		engTexts.put("category_clothings", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Ornaments/Bric A Brac", "Rugs", "Pictures", "Chinaware", "Crystalware", "Painting", "Statue",
		        "Video Camera", "Digital Camera");
		engTexts.put("category_ornamentsAndExpensive", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "TableCloths", "Towels", "Pilloes", "Blankets", "Sheets");
		engTexts.put("category_textile", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Exercise Bicycle", "Treadmill");
		engTexts.put("category_sport", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Table", "Chairs", "Ladder", "Tools", "Service Cabinet", "Barbecue Grill");
		engTexts.put("category_garden", objectList);
		
		objectList = new ArrayList<String>();
		objectList = Arrays.asList("General", "Piano", "Electric Keyboard", "Guitar");
		engTexts.put("category_musical", objectList);
	}
}
