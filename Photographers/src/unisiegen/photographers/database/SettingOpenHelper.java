package unisiegen.photographers.database;

import java.util.ArrayList;

import unisiegen.photographers.model.Setting;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "pn_data";
													// Klasse?
	private static final int DATABASE_VERSION = 1;

	private static final String GEARSET_TABLE_DROP = "DROP TABLE [GearSet];";
	private static final String SETTING_TABLE_DROP = "DROP TABLE [Setting];";
	
	private static final String GEARSET_TABLE_CREATE = "CREATE TABLE [GearSet] ([ID] integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, [SetName] text, [SetDescription] text);";
	private static final String SETTING_TABLE_CREATE = "CREATE TABLE [Setting] ([ID] integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, [GearSetID] integer NOT NULL, [SettingType] text, [SettingName] text, [IsDisplayed] integer, [IsDefaultSelection] integer);";
	private Context context;
	private SQLiteDatabase db;

	public SettingOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		this.db = db;
		
//		db.execSQL(SETTING_TABLE_DROP);
//		db.execSQL(GEARSET_TABLE_DROP);		
				
		db.execSQL(GEARSET_TABLE_CREATE);
		db.execSQL(SETTING_TABLE_CREATE);
		
		// check for legacy upgrade
		if(legacyDataExist()){
			// import legacy data
			importLegacyData();
		} else {
			// establish new default data for settings
		}
	}

	private void importLegacyData() {
		
		// import all data from different tables into a new one
		
		//create sets
		createSet(DB.MY_DB_SET);
		createSet(DB.MY_DB_SET1);
		createSet(DB.MY_DB_SET2);
		createSet(DB.MY_DB_SET3);
		
		copySettingsForSet(DB.MY_DB_SET);
		copySettingsForSet(DB.MY_DB_SET1);
		copySettingsForSet(DB.MY_DB_SET2);
		copySettingsForSet(DB.MY_DB_SET3);
	}

	private void createSet(String myDbSet) {
		
		ContentValues cv = new ContentValues();
		//cv.put("ID", "");
		cv.put("SetName", myDbSet);
		cv.put("SetDescription", "Imported Set. Change this to your liking.");						
		db.insert("GearSet", null, cv);	
	}

	private void copySettingsForSet(String myDbSet) {

		Cursor gearset = db.query("GearSet", null, "SetName like ?", new String [] {myDbSet}, null, null, null);
		gearset.moveToFirst();
		int gearSetID = gearset.getInt(0);
		
		SQLiteDatabase legacyDB = context.openOrCreateDatabase(myDbSet, Context.MODE_PRIVATE, null);
		for(String tableName : DB.tableNames){
			Cursor c = legacyDB.rawQuery("SELECT name, value, def FROM " + tableName, null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						
						ContentValues cv = new ContentValues();
						//cv.put("ID", "");
						cv.put("GearSetID", gearSetID);
						cv.put("SettingType", tableName);
						cv.put("SettingName", c.getString(c.getColumnIndex("name")));
						cv.put("IsDisplayed", c.getColumnIndex("value"));
						cv.put("IsDefaultSelection", c.getInt(c.getColumnIndex("def")));
						
						db.insert("Setting", null, cv);						
	
					} while (c.moveToNext());
				}
			}
			c.close();
		}
		legacyDB.close();
	}

	private boolean legacyDataExist() {

		try {
			SQLiteDatabase myDBSet = context.openOrCreateDatabase(DB.MY_DB_SET, Context.MODE_PRIVATE, null);
			
			if(myDBSet != null){
				Cursor c = myDBSet.rawQuery("SELECT * FROM " + DB.MY_DB_TABLE_SETCAM + ";", null);
				if(c != null && c.getCount() > 0){
					return true;
				}
			}
				
				
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		System.out.println();
	}
	
	public Object getFoo(){
		
		return null;
	}

}
