/* Copyright (C) 2012 Sebastian Draxler, Alexander Boden, Christian Woehrl (Committers)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unisiegen.photographers.database;

import java.util.ArrayList;
import java.util.Collections;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.helper.SettingsComparator;
import unisiegen.photographers.model.Camera;
import unisiegen.photographers.model.Equipment;
import unisiegen.photographers.model.Lens;
import unisiegen.photographers.model.Setting;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * This class is able to create new databases for Settings and Films.
 * Furthermore, existing Databases can be reset to their default values, which
 * is a set of useful values for your gear and an empty film library.
 */
public class DB {

	private static DB instance;

	public final static String MY_DB_NUMMER = "Nummern";
	public static final String MY_DB_FILM = "Filme";

	public final static String MY_DB_TABLE_NUMMER = "Nummer";
	public final static String MY_DB_FILM_TABLE = "Film";

	public final static String MY_DB_SET = "Foto";
	public final static String MY_DB_SET1 = "FotoSettingsOne";
	public final static String MY_DB_SET2 = "FotoSettingsTwo";
	public final static String MY_DB_SET3 = "FotoSettingsThree";

	public final static String MY_DB_TABLE_SETCAM = "SettingsCamera";
	public final static String MY_DB_TABLE_SETCAMBW = "SettingsCameraBW";
	public final static String MY_DB_TABLE_SETFF = "SettingsFilmFormat";
	public final static String MY_DB_TABLE_SETEMP = "SettingsFilmEmpf";
	public final static String MY_DB_TABLE_SETBW = "SettingsBrennweite";
	public final static String MY_DB_TABLE_SETNM = "SettingsNahzubehor";
	public final static String MY_DB_TABLE_SETFIL = "SettingsFilter";
	public final static String MY_DB_TABLE_SETBLI = "SettingsBlitz";
	public final static String MY_DB_TABLE_SETSON = "SettingsSonder";
	public final static String MY_DB_TABLE_SETFOK = "SettingsFokus";
	public final static String MY_DB_TABLE_SETBLE = "SettingsBlende";
	public final static String MY_DB_TABLE_SETZEI = "SettingsZeit";
	public final static String MY_DB_TABLE_SETMES = "SettingsMessung";
	public final static String MY_DB_TABLE_SETPLU = "SettingsPlusMinus";
	public final static String MY_DB_TABLE_SETMAK = "SettingsMakro";
	public final static String MY_DB_TABLE_SETMVF = "SettingsMakroVF";
	public final static String MY_DB_TABLE_SETFVF = "SettingsFilterVF";
	public final static String MY_DB_TABLE_SETMVF2 = "SettingsMakroVF2";
	public final static String MY_DB_TABLE_SETFVF2 = "SettingsFilterVF2";
	public final static String MY_DB_TABLE_SETKOR = "SettingsBlitzKorr";
	public final static String MY_DB_TABLE_SETTYP = "SettingsFilmTyp";

	public final static ArrayList<String> tableNames = new ArrayList<String>();

	static {
		tableNames.add(MY_DB_TABLE_SETCAM);
		tableNames.add(MY_DB_TABLE_SETFF);
		tableNames.add(MY_DB_TABLE_SETEMP);
		tableNames.add(MY_DB_TABLE_SETBW);
		tableNames.add(MY_DB_TABLE_SETNM);
		tableNames.add(MY_DB_TABLE_SETFIL);
		tableNames.add(MY_DB_TABLE_SETBLI);
		tableNames.add(MY_DB_TABLE_SETSON);
		tableNames.add(MY_DB_TABLE_SETFOK);
		tableNames.add(MY_DB_TABLE_SETBLE);
		tableNames.add(MY_DB_TABLE_SETZEI);
		tableNames.add(MY_DB_TABLE_SETMES);
		tableNames.add(MY_DB_TABLE_SETPLU);
		tableNames.add(MY_DB_TABLE_SETMAK);
		tableNames.add(MY_DB_TABLE_SETMVF);
		tableNames.add(MY_DB_TABLE_SETFVF);
		tableNames.add(MY_DB_TABLE_SETMVF2);
		tableNames.add(MY_DB_TABLE_SETFVF2);
		tableNames.add(MY_DB_TABLE_SETKOR);
		tableNames.add(MY_DB_TABLE_SETTYP);
	}

	private DB() {
	}

	public static DB getDB() {
		if (instance == null) {
			instance = new DB();
		}
		return instance;
	}

	
	/*
	 * Figure out how to save information on the selected set!
	 * * implement it
	 * * use it here
	 * * migrate to DataSource...
	 */
	private String getDBName(Context context) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String dbName = prefs.getString("SettingsTable", DB.MY_DB_SET);

		return dbName;
	}

//	public void createOrRebuildSettingsTable(Context context) throws Exception {
//
//		String database = getDBName(context);
//		createOrRebuildSettingsTable(context, database);
//	}
//
//	public void createOrRebuildSettingsTable(Context context, String database)
//			throws Exception {
//
//		Log.v("DatabaseCreator", "rebuildSettings() was called...");
//
//		SQLiteDatabase myDBSet = context.openOrCreateDatabase(database,
//				Context.MODE_PRIVATE, null);
//
//		myDBSet.beginTransaction();
//		// needs special care
//		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ MY_DB_TABLE_SETCAMBW
//				+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
//				+ ";");
//        myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETCAMBW);
//
//		// all other tables are the same...
//		for (String tableName : tableNames) {
//			StringBuffer buf = new StringBuffer();
//			buf.append("CREATE TABLE IF NOT EXISTS ");
//			buf.append(tableName);
//			buf.append(" (_id integer primary key autoincrement, name varchar(100), value integer, def integer);");
//			myDBSet.execSQL(buf.toString());
//
//			buf = new StringBuffer();
//			buf.append("DELETE FROM ");
//			buf.append(tableName);
//			myDBSet.execSQL(buf.toString());
//		}
//
//		Resources res = context.getResources();
//
//		setDefaultSettings(myDBSet, res, R.array.setff, MY_DB_TABLE_SETFF);
//		setDefaultSettings(myDBSet, res, R.array.setemp, MY_DB_TABLE_SETEMP);
//		setDefaultSettings(myDBSet, res, R.array.settyp, MY_DB_TABLE_SETTYP);
//		setDefaultSettings(myDBSet, res, R.array.setnm, MY_DB_TABLE_SETNM);
//		setDefaultSettings(myDBSet, res, R.array.setfil, MY_DB_TABLE_SETFIL);
//		setDefaultSettings(myDBSet, res, R.array.setbli, MY_DB_TABLE_SETBLI);
//		setDefaultSettings(myDBSet, res, R.array.setson, MY_DB_TABLE_SETSON);
//		setDefaultSettings(myDBSet, res, R.array.setfok, MY_DB_TABLE_SETFOK);
//		setDefaultSettings(myDBSet, res, R.array.setble, MY_DB_TABLE_SETBLE);
//		setDefaultSettings(myDBSet, res, R.array.setzei, MY_DB_TABLE_SETZEI);
//		setDefaultSettings(myDBSet, res, R.array.setmes, MY_DB_TABLE_SETMES);
//		setDefaultSettings(myDBSet, res, R.array.setplu, MY_DB_TABLE_SETPLU);
//		setDefaultSettings(myDBSet, res, R.array.setkor, MY_DB_TABLE_SETKOR);
//		setDefaultSettings(myDBSet, res, R.array.setmvf, MY_DB_TABLE_SETMVF);
//		setDefaultSettings(myDBSet, res, R.array.setmvf2, MY_DB_TABLE_SETMVF2);
//		setDefaultSettings(myDBSet, res, R.array.setfvf, MY_DB_TABLE_SETFVF);
//		setDefaultSettings(myDBSet, res, R.array.setfvf2, MY_DB_TABLE_SETFVF2);
//		myDBSet.setTransactionSuccessful();
//		myDBSet.endTransaction();
//
//		myDBSet.close();
//
//		setDefaultVal(context, MY_DB_TABLE_SETKOR, "0");
//		setDefaultVal(context, MY_DB_TABLE_SETPLU, "0");
//	}
//
//	private void setDefaultSettings(SQLiteDatabase database, Resources res, int stringArrayName, String tableName) {
//		
//		String[] valueArray = res.getStringArray(stringArrayName);
//		if (valueArray != null) {
//			for (String value : valueArray) {
//				StringBuffer buf = new StringBuffer();
//				buf.append("INSERT INTO ");
//				buf.append(tableName);
//				buf.append(" Values (null,'");
//				buf.append(value);
//				buf.append("','1', '0');");
//				database.execSQL(buf.toString());
//			}
//		}
//	}
//
    public void createSettingsTableFromEquipmentImport(Context context, Equipment equipment) throws Exception {
 
        Log.v("DatabaseCreator", "Writing imported settings to database.");

        String database = getDBName(context);

        SQLiteDatabase myDBSet = context.openOrCreateDatabase(database,
                Context.MODE_PRIVATE, null);

        myDBSet.beginTransaction();
        // needs special care
        myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
                + MY_DB_TABLE_SETCAMBW
                + " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
                + ";");
        myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETCAMBW + ";");

        // all other tables are the same...
        for (String tableName : tableNames) {
            StringBuffer buf = new StringBuffer();
            buf.append("CREATE TABLE IF NOT EXISTS ");
            buf.append(tableName);
            buf.append(" (_id integer primary key autoincrement, name varchar(100), value integer, def integer);");
            myDBSet.execSQL(buf.toString());
            Log.v("Check", buf.toString());

            buf = new StringBuffer();
            buf.append("DELETE FROM ");
            buf.append(tableName);
            buf.append(";");
            myDBSet.execSQL(buf.toString());
            Log.v("Check", buf.toString());
        }

        for (Camera camera : equipment.cameras) {
            StringBuffer buf = new StringBuffer();
            buf.append("INSERT INTO ");
            buf.append(MY_DB_TABLE_SETCAM);
            buf.append(" (_id, name, value, def) VALUES (null, '");
            buf.append(camera.name);
            buf.append("' ,'");
            buf.append(camera.value);
            buf.append("' ,'");
            buf.append(camera.def);
            buf.append("');");
            myDBSet.execSQL(buf.toString());
            Log.v("Check", buf.toString());
        }

        for (Lens lens : equipment.lenses) {
            StringBuffer buf = new StringBuffer();
            buf.append("INSERT INTO ");
            buf.append(MY_DB_TABLE_SETCAMBW);
            buf.append(" (_id, cam, bw) VALUES (null, '");
            buf.append(lens.camera);
            buf.append("' ,'");
            buf.append(lens.name);
            buf.append("');");
            myDBSet.execSQL(buf.toString());
            Log.v("Check", buf.toString());
        }

        importData(myDBSet, equipment.filmEmpfindlichkeit);
        importData(myDBSet, equipment.brennweite);
        importData(myDBSet, equipment.nahzubehoer);
        importData(myDBSet, equipment.filter);
        importData(myDBSet, equipment.blitz);
        importData(myDBSet, equipment.fokus);
        importData(myDBSet, equipment.blende);
        importData(myDBSet, equipment.zeit);
        importData(myDBSet, equipment.messung);
        importData(myDBSet, equipment.plusminus);
        importData(myDBSet, equipment.makro);
        importData(myDBSet, equipment.makrovf);
        importData(myDBSet, equipment.filterVF);
        importData(myDBSet, equipment.makroVF2);
        importData(myDBSet, equipment.filterVF2);
        importData(myDBSet, equipment.blitzKorr);
        importData(myDBSet, equipment.filmTyp);
        importData(myDBSet, equipment.sonder);
        importData(myDBSet, equipment.filmFormat);

        myDBSet.setTransactionSuccessful();
        myDBSet.endTransaction();

        myDBSet.close();

    }

    private void importData(SQLiteDatabase database, ArrayList<Setting> settings) {

        for (Setting setting : settings) {
            StringBuffer buf = new StringBuffer();
            buf.append("INSERT INTO ");
            buf.append(setting.getType());
            buf.append(" (_id, name, value, def) VALUES (null, '");
            buf.append(setting.getValue());
            buf.append("' ,'");
            buf.append(setting.shouldBeDisplayed());
            buf.append("' ,'");
            buf.append(setting.isDefaultValue());
            buf.append("');");
            database.execSQL(buf.toString());
            Log.v("Check", buf.toString());
        }

    }
//
//	public void createOrRebuildNummernTable(Context mContext) {
//
//		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
//				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);
//		myDBNummer
//				.execSQL("CREATE TABLE IF NOT EXISTS "
//						+ DB.MY_DB_TABLE_NUMMER
//						+ " (title varchar(100) primary key, value integer,camera varchar(100), datum varchar(100), bilder integer, pic varchar(999))"
//						+ ";");
//		myDBNummer.close();
//	}
//
//	public void createOrRebuildFilmTable(Context mContext) {
//		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
//				Context.MODE_PRIVATE, null);
//		myDBFilm.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DB.MY_DB_FILM_TABLE
//				+ " (_id integer primary key autoincrement, filmdatum varchar(100), picuhrzeit varchar(100), filmtitle varchar(100), filmcamera varchar(100), filmformat varchar(100), filmempfindlichkeit varchar(100), filmtyp varchar(100), filmsonder varchar(100), filmsonders varchar(100), picfokus varchar(100), picblende varchar(100), piczeit varchar(100), picmessung varchar(100), pickorr varchar(100), picmakro varchar(100), picmakrovf varchar(100), picfilter varchar(100), picfiltervf varchar(100), picblitz varchar(100), picblitzkorr varchar(100), picnotiz varchar(100), pickameranotiz varchar(100), picobjektiv varchar(100),piclong varchar(100),piclat varchar(100),filmnotiz varchar(100), picnummer varchar(100))"
//				+ ";");
//		myDBFilm.close();
//	}

	
	public boolean deleteSetting(Context context, String settingType,
			String value) {

		SQLiteDatabase db = context.openOrCreateDatabase(getDBName(context),
				Context.MODE_PRIVATE, null);

		try {
			db.execSQL("DELETE FROM " + settingType + " WHERE name = '" + value
					+ "'");
			db.close();

		} catch (Exception e) {
			db.close();

			return false;
		}

		return true;
	}

	public void deleteLens(Context mContext, String lens) {

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);

		try {
			db.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETCAMBW
					+ " WHERE bw = '" + lens + "'");
			db.close();
		} catch (Exception e) {
			Log.v("Check", "Fehler Delete : " + e);
			db.close();
		}
	}

	public void deleteLensFromCamera(Context mContext, String lens,
			String camera) {

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);

		try {
			db.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETCAMBW
					+ " WHERE bw = '" + lens + "' AND cam = '" + camera + "'");
			db.close();
		} catch (Exception e) {
			Log.v("Check", "Fehler Delete : " + e);
			db.close();
		}
	}

	public boolean addSetting(Context mContext, String settingType,
			String name, int value) {

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);
		db.execSQL("INSERT INTO " + settingType + " Values (" + null + ",'"
				+ "" + name + "" + "','" + value + "','" + 0 + "');");
		db.close();

		return true;
	}

	public void updateSetting(Context mContext, String settingType,
			String name, int value) {

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);
		db.execSQL("UPDATE " + settingType + " SET value = '" + value
				+ "' WHERE name = '" + name + "'");
		db.close();
	}

	public boolean addLensToCamera(Context mContext, String camera, String lens) {

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);
		db.execSQL("INSERT INTO " + DB.MY_DB_TABLE_SETCAMBW + " Values ("
				+ null + ",'" + "" + camera + "" + "','" + lens + "');");
		db.close();

		return true;
	}

	/**
	 * This will retrieve a List of ALL settings, with additional data for each
	 * particular value, if it is enabled and should be shown in the UI. This is
	 * used by the configuration dialog (EditSettings).
	 * 
	 * @param mContext
	 * @param database
	 * @param settingName
	 *            Use on of the constants in this file. Each Constant represents
	 *            a table (for a certain type of settings) in the db.
	 * @return
	 */
	public ArrayList<Setting> getAllSettings(Context mContext,
			String settingName) {

		ArrayList<Setting> values = new ArrayList<Setting>();

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT name, value, def FROM " + settingName,
				null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					values.add(new Setting(settingName, c.getString(c
							.getColumnIndex("name")), c.getInt(c
							.getColumnIndex("value")), c.getInt(c
							.getColumnIndex("def"))));

				} while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		
		Collections.sort(values, new SettingsComparator(settingName));
		
		return values;
	}

	public boolean setDefaultVal(Context mContext, String settingName,
			String newDefault) {

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);

		db.execSQL("UPDATE " + settingName + " SET def = '" + 0 + "'");
		db.execSQL("UPDATE " + settingName + " SET def = '" + 1
				+ "' WHERE name = '" + newDefault + "'");
		db.close();

		return true;
	}

	/**
	 * 
	 * @return Returns a list of items, usable for various portions of the UI
	 */
	public ArrayList<String> getActivatedSettingsData(Context mContext,
			String settingName) {

		ArrayList<Setting> values = new ArrayList<Setting>();

		SQLiteDatabase myDB = mContext.openOrCreateDatabase(
				getDBName(mContext), Context.MODE_PRIVATE, null);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT name, value FROM ");
		sql.append(settingName);
		sql.append(" WHERE value = '1'");
		Cursor c = myDB.rawQuery(new String(sql), null);

		if (c != null) {
			if (c.moveToFirst()) {
				do {
					values.add(new Setting(null, c.getString(c.getColumnIndex("name")), 0, 0));

				} while (c.moveToNext());
			}
		}
		c.close();
		myDB.close();
		
		Collections.sort(values, new SettingsComparator(settingName));
		
		// We need a List of Strings here, so we have to convert ... 
		
		ArrayList<String> namesOnly = new ArrayList<String>();
		
		for (Setting s : values) {
			namesOnly.add(s.getValue());
		}
		
		return namesOnly;
	}

	public int getDefaultSettingNumber(Context mContext, String settingName) {

		ArrayList<Setting> values = new ArrayList<Setting>();
		
		SQLiteDatabase myDB = mContext.openOrCreateDatabase(
				getDBName(mContext), Context.MODE_PRIVATE, null);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT name, value, def FROM ");
		sql.append(settingName);
		sql.append(" WHERE value = '1'");

		Cursor c = myDB.rawQuery(new String(sql), null);
;
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					values.add(new Setting(null, c.getString(c.getColumnIndex("name")), 0, c.getInt(c.getColumnIndex("def"))));
				} while (c.moveToNext());
			}
		}
		c.close();
		myDB.close();
		
		// We need to sort in order to find the right index for the default settings in NewPictureActivity.
		
		Collections.sort(values, new SettingsComparator(settingName));
		
		int defaultValueIndex = 0;
		
		for (Setting value : values) {
			if (value.isDefaultValueB()) {
				defaultValueIndex = values.indexOf(value);
				break;
			}
		}
		
		return defaultValueIndex;
	}

	public ArrayList<String> getLensesForCamera(Context mContext, String camera) {

		ArrayList<String> values = new ArrayList<String>();

		SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
				Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cam, bw FROM ");
		sql.append(DB.MY_DB_TABLE_SETCAMBW);
		sql.append(" WHERE cam = '");
		sql.append(camera);
		sql.append("'");

		Cursor c = db.rawQuery(new String(sql), null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					values.add(c.getString(c.getColumnIndex("bw")));

				} while (c.moveToNext());
			}
		}
		c.close();
		db.close();

		return values;
	}

    public ArrayList<Camera> getAllCameras(Context mContext) {

        ArrayList<Camera> values = new ArrayList<Camera>();

        SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
                Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT name, value, def FROM " + DB.MY_DB_TABLE_SETCAM,
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    values.add(new Camera(c.getString(c.getColumnIndex("name")), c.getInt(c.getColumnIndex("value")), c.getInt(c.getColumnIndex("def"))));
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();

        return values;

    }

    public ArrayList<Lens> getAllLenses(Context mContext) {

        ArrayList<Lens> values = new ArrayList<Lens>();

        SQLiteDatabase db = mContext.openOrCreateDatabase(getDBName(mContext),
                Context.MODE_PRIVATE, null);

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT cam, bw FROM ");
        sql.append(DB.MY_DB_TABLE_SETCAMBW);

        Cursor c = db.rawQuery(new String(sql), null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    values.add(new Lens(c.getString(c.getColumnIndex("bw")), c.getString(c.getColumnIndex("cam"))));
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();

        return values;

    }

    
}