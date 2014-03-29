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
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import unisiegen.photographers.model.Setting;
import android.content.ContentValues;
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

	private String getDBName(Context context) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String dbName = prefs.getString("SettingsTable", DB.MY_DB_SET);

		return dbName;
	}

	public void createOrRebuildSettingsTable(Context context) throws Exception {

		String database = getDBName(context);
		createOrRebuildSettingsTable(context, database);
	}

	public void createOrRebuildSettingsTable(Context context, String database)
			throws Exception {

		Log.v("DatabaseCreator", "rebuildSettings() was called...");

		SQLiteDatabase myDBSet = context.openOrCreateDatabase(database,
				Context.MODE_PRIVATE, null);

		myDBSet.beginTransaction();
		// needs special care
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETCAMBW
				+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
				+ ";");

		// all other tables are the same...
		for (String tableName : tableNames) {
			StringBuffer buf = new StringBuffer();
			buf.append("CREATE TABLE IF NOT EXISTS ");
			buf.append(tableName);
			buf.append(" (_id integer primary key autoincrement, name varchar(100), value integer, def integer);");
			myDBSet.execSQL(buf.toString());

			buf = new StringBuffer();
			buf.append("DELETE FROM ");
			buf.append(tableName);
			myDBSet.execSQL(buf.toString());
		}

		Resources res = context.getResources();

		setDefaultSettings(myDBSet, res, R.array.setff, MY_DB_TABLE_SETFF);
		setDefaultSettings(myDBSet, res, R.array.setemp, MY_DB_TABLE_SETEMP);
		setDefaultSettings(myDBSet, res, R.array.settyp, MY_DB_TABLE_SETTYP);
		setDefaultSettings(myDBSet, res, R.array.setnm, MY_DB_TABLE_SETNM);
		setDefaultSettings(myDBSet, res, R.array.setfil, MY_DB_TABLE_SETFIL);
		setDefaultSettings(myDBSet, res, R.array.setbli, MY_DB_TABLE_SETBLI);
		setDefaultSettings(myDBSet, res, R.array.setson, MY_DB_TABLE_SETSON);
		setDefaultSettings(myDBSet, res, R.array.setfok, MY_DB_TABLE_SETFOK);
		setDefaultSettings(myDBSet, res, R.array.setble, MY_DB_TABLE_SETBLE);
		setDefaultSettings(myDBSet, res, R.array.setzei, MY_DB_TABLE_SETZEI);
		setDefaultSettings(myDBSet, res, R.array.setmes, MY_DB_TABLE_SETMES);
		setDefaultSettings(myDBSet, res, R.array.setplu, MY_DB_TABLE_SETPLU);
		setDefaultSettings(myDBSet, res, R.array.setkor, MY_DB_TABLE_SETKOR);
		setDefaultSettings(myDBSet, res, R.array.setmvf, MY_DB_TABLE_SETMVF);
		setDefaultSettings(myDBSet, res, R.array.setmvf2, MY_DB_TABLE_SETMVF2);
		setDefaultSettings(myDBSet, res, R.array.setfvf, MY_DB_TABLE_SETFVF);
		setDefaultSettings(myDBSet, res, R.array.setfvf2, MY_DB_TABLE_SETFVF2);
		myDBSet.setTransactionSuccessful();
		myDBSet.endTransaction();

		myDBSet.close();

		setDefaultVal(context, MY_DB_TABLE_SETKOR, "0");
		setDefaultVal(context, MY_DB_TABLE_SETPLU, "0");
	}

	private void setDefaultSettings(SQLiteDatabase database, Resources res,
			int stringArrayName, String tableName) {
		String[] valueArray = res.getStringArray(stringArrayName);
		if (valueArray != null) {
			for (String value : valueArray) {
				StringBuffer buf = new StringBuffer();
				buf.append("INSERT INTO ");
				buf.append(tableName);
				buf.append(" Values (null,'");
				buf.append(value);
				buf.append("','1', '0');");
				database.execSQL(buf.toString());
			}
		}
	}

	public void createOrRebuildNummernTable(Context mContext) {

		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);
		myDBNummer
				.execSQL("CREATE TABLE IF NOT EXISTS "
						+ DB.MY_DB_TABLE_NUMMER
						+ " (title varchar(100) primary key, value integer,camera varchar(100), datum varchar(100), bilder integer, pic varchar(999))"
						+ ";");
		myDBNummer.close();
	}

	public void createOrRebuildFilmTable(Context mContext) {
		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		myDBFilm.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_FILM_TABLE
				+ " (_id integer primary key autoincrement, filmdatum varchar(100), picuhrzeit varchar(100), filmtitle varchar(100), filmcamera varchar(100), filmformat varchar(100), filmempfindlichkeit varchar(100), filmtyp varchar(100), filmsonder varchar(100), filmsonders varchar(100), picfokus varchar(100), picblende varchar(100), piczeit varchar(100), picmessung varchar(100), pickorr varchar(100), picmakro varchar(100), picmakrovf varchar(100), picfilter varchar(100), picfiltervf varchar(100), picblitz varchar(100), picblitzkorr varchar(100), picnotiz varchar(100), pickameranotiz varchar(100), picobjektiv varchar(100),piclong varchar(100),piclat varchar(100),filmnotiz varchar(100), picnummer varchar(100))"
				+ ";");
		myDBFilm.close();
	}

	public ArrayList<Film> getFilme(Context context) {

		ArrayList<Film> filme = new ArrayList<Film>();

		try {
			SQLiteDatabase myDBNummer = context.openOrCreateDatabase(
					MY_DB_NUMMER, Context.MODE_PRIVATE, null);
			SQLiteDatabase myDBFilm = context.openOrCreateDatabase(
					DB.MY_DB_FILM, Context.MODE_PRIVATE, null);

			Cursor c = myDBNummer.rawQuery(
					"SELECT title,camera,datum,bilder,pic FROM "
							+ MY_DB_TABLE_NUMMER + 
							" ORDER BY datum DESC", null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						Film film = new Film();
						filme.add(film);

						film.Titel = c.getString(c.getColumnIndex("title"));
						film.Kamera = c.getString(c.getColumnIndex("camera"));
						film.Datum = c.getString(c.getColumnIndex("datum"));
						film.Pics = c.getString(c.getColumnIndex("bilder"));
						film.setIcon(c.getString(c.getColumnIndex("pic")));

						Cursor c1 = myDBFilm
								.rawQuery(
										"SELECT _id,filmtitle,filmnotiz,picuhrzeit,picnummer, picobjektiv, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
												+ DB.MY_DB_FILM_TABLE
												+ " WHERE filmtitle = '"
												+ film.Titel + "'", null);

						if (c1 != null) {
							if (c1.moveToFirst()) {
								film.Filmbezeichnung = c1.getString(c1
										.getColumnIndex("filmnotiz"));
								film.Filmformat = c1.getString(c1
										.getColumnIndex("filmformat"));
								film.Filmtyp = c1.getString(c1
										.getColumnIndex("filmtyp"));
								film.Empfindlichkeit = c1.getString(c1
										.getColumnIndex("filmempfindlichkeit"));
								film.Sonderentwicklung1 = c1.getString(c1
										.getColumnIndex("filmsonder"));
								film.Sonderentwicklung2 = c1.getString(c1
										.getColumnIndex("filmsonders"));
							}
						}
						c1.close();

						// Bilder holen
						film.Bilder = this.getBilder(context, film.Titel, null);

					} while (c.moveToNext());
				}
			}
			c.close();
			myDBNummer.close();
			myDBFilm.close();
		} catch (Exception e) {

		}
		return filme;
	}

	public void deleteFilms(Context context, String[] filmTitel) {

		SQLiteDatabase myDBNummer = context.openOrCreateDatabase(MY_DB_NUMMER,
				Context.MODE_PRIVATE, null);
		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		myDBNummer.delete(DB.MY_DB_TABLE_NUMMER, "title=?", filmTitel);
		myDBFilm.delete(DB.MY_DB_FILM_TABLE, "filmtitle=?", filmTitel);

		myDBNummer.close();
		myDBFilm.close();
	}
	
	public boolean checkIfFilmTitleIsTaken(Context context, String newTitle) {
		
		boolean titleAlreadyTaken = false;
		
		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		
		Cursor c = myDBFilm.rawQuery("SELECT filmtitle FROM " + DB.MY_DB_FILM_TABLE + " WHERE filmtitle = '" + newTitle + "'", null);
		
		if (c.getCount() > 0) { 
			titleAlreadyTaken = true;
		}
		
		c.close();
		myDBFilm.close();
		
		return titleAlreadyTaken;
	}
	
	public Film getFilm(Context context, String title) {

		Film film = new Film();

		SQLiteDatabase myDBNummer = context.openOrCreateDatabase(MY_DB_NUMMER,
				Context.MODE_PRIVATE, null);
		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		Cursor c = myDBNummer.rawQuery(
				"SELECT title,camera,datum,bilder, pic FROM "
						+ DB.MY_DB_TABLE_NUMMER + " WHERE title = '" + title
						+ "'", null);

		if (c != null) {
			if (c.moveToFirst()) {

				film.Titel = c.getString(c.getColumnIndex("title"));
				film.Kamera = c.getString(c.getColumnIndex("camera"));
				film.Datum = c.getString(c.getColumnIndex("datum"));
				film.setIcon(c.getString(c.getColumnIndex("pic")));

				// TODO: Restliche Filmdaten aus der ersten Zeile der Bilder
				// Tabelle holen // WTFFFFFFFF???
				Cursor c1 = myDBFilm
						.rawQuery(
								"SELECT _id,filmtitle,filmnotiz,picuhrzeit,picnummer, picobjektiv, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
										+ DB.MY_DB_FILM_TABLE
										+ " WHERE filmtitle = '"
										+ film.Titel
										+ "'", null);

				if (c1 != null) {
					if (c1.moveToFirst()) {
						film.Filmbezeichnung = c1.getString(c1
								.getColumnIndex("filmnotiz"));
						film.Filmformat = c1.getString(c1
								.getColumnIndex("filmformat"));
						film.Filmtyp = c1.getString(c1
								.getColumnIndex("filmtyp"));
						film.Empfindlichkeit = c1.getString(c1
								.getColumnIndex("filmempfindlichkeit"));
						film.Sonderentwicklung1 = c1.getString(c1
								.getColumnIndex("filmsonder"));
						film.Sonderentwicklung2 = c1.getString(c1
								.getColumnIndex("filmsonders"));
					}
				}
				c1.close();

				film.Bilder = this.getBilder(context, title, null);
			}
		}
		c.close();

		myDBFilm.close();
		myDBNummer.close();

		return film;
	}

	public ArrayList<Bild> getBild(Context context, String filmTitle,
			String bildNumemr) {

		ArrayList<Bild> bilder = getBilder(context, filmTitle, bildNumemr);
		return bilder;
	}

	private ArrayList<Bild> getBilder(Context context, String title,
			String bildNummer) {

		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT _id,picfokus,picuhrzeit,piclat,piclong,filmdatum,picobjektiv, picblende,piczeit,picmessung, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" WHERE filmtitle = '");
		sql.append(title);
		if (bildNummer == null) {
			sql.append("' AND picnummer != 'Bild 0';"); // Ignore the dummy pic
														// "Bild 0"
		} else {
			sql.append("' AND picnummer = '");
			sql.append(bildNummer);
			sql.append("';");
		}

		Cursor c2 = myDBFilm.rawQuery(new String(sql), null);

		ArrayList<Bild> bilder = new ArrayList<Bild>();
		if (c2 != null) {
			if (c2.moveToFirst()) {
				do {
					bilder.add(new Bild(
							c2.getString(c2.getColumnIndex("picnummer")),
							c2.getString(c2.getColumnIndex("picobjektiv")),
							c2.getString(c2.getColumnIndex("picblende")),
							c2.getString(c2.getColumnIndex("piczeit")),
							c2.getString(c2.getColumnIndex("picfokus")),
							c2.getString(c2.getColumnIndex("picfilter")),
							c2.getString(c2.getColumnIndex("picmakro")),
							c2.getString(c2.getColumnIndex("picfiltervf")),
							c2.getString(c2.getColumnIndex("picmakrovf")),
							c2.getString(c2.getColumnIndex("picmessung")),
							c2.getString(c2.getColumnIndex("pickorr")),
							c2.getString(c2.getColumnIndex("picblitz")),
							c2.getString(c2.getColumnIndex("picblitzkorr")),
							c2.getString(c2.getColumnIndex("picuhrzeit"))
									+ " - "
									+ c2.getString(c2
											.getColumnIndex("filmdatum")),
							"Lat : "
									+ c2.getString(c2.getColumnIndex("piclat"))
									+ " - Long : "
									+ c2.getString(c2.getColumnIndex("piclong")),
							c2.getString(c2.getColumnIndex("picnotiz")), c2
									.getString(c2
											.getColumnIndex("pickameranotiz"))));
				} while (c2.moveToNext());
			}
		}
		c2.close();
		myDBFilm.close();

		return bilder;
	}

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

	public void updatePicture(Context mContext, Film film, Bild bild) {

		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" SET picfokus = '");
		sql.append(bild.Fokus);
		sql.append("', picblende = '");
		sql.append(bild.Blende);
		sql.append("', piczeit = '");
		sql.append(bild.Zeit);
		sql.append("', picmessung = '");
		sql.append(bild.Messmethode);
		sql.append("', pickorr = '");
		sql.append(bild.Belichtungskorrektur);
		sql.append("', picmakro = '");
		sql.append(bild.Makro);
		sql.append("', picmakrovf = '");
		sql.append(bild.MakroVF);
		sql.append("', picfilter = '");
		sql.append(bild.Filter);
		sql.append("', picfiltervf = '");
		sql.append(bild.FilterVF);
		sql.append("', picblitz = '");
		sql.append(bild.Blitz);
		sql.append("', picblitzkorr = '");
		sql.append(bild.Blitzkorrektur);
		sql.append("', picnotiz = '");
		sql.append(bild.Notiz);
		sql.append("', pickameranotiz = '");
		sql.append(bild.KameraNotiz);
		sql.append("', picobjektiv = '");
		sql.append(bild.Objektiv);
		sql.append("' WHERE filmtitle = '");
		sql.append(film.Titel);
		sql.append("' AND picnummer = '");
		sql.append(bild.Bildnummer);
		sql.append("';");
		myDBFilm.execSQL(new String(sql));
		myDBFilm.close();
	}

	public void deletePicture(Context mContext, Film film, Bild bild) {

		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" WHERE filmtitle = '");
		sql.append(film.Titel);
		sql.append("' AND picnummer = '");
		sql.append(bild.Bildnummer);
		sql.append("';");

		myDBFilm.execSQL(new String(sql));
		myDBFilm.close();

		ContentValues dataToInsert = new ContentValues();
		dataToInsert.put("bilder", film.Bilder.size() - 1);
		myDBNummer.update(DB.MY_DB_TABLE_NUMMER, dataToInsert, "title=?",
				new String[] { film.Titel });

		myDBNummer.close();
	}

	private void addPicture(Context mContext, Film f, Bild b) {

		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" Values (" + null);
		sql.append(",'");
		sql.append(f.Datum);
		sql.append("','");
		sql.append(b.Zeitstempel);
		sql.append("','");
		sql.append(f.Titel);
		sql.append("','");
		sql.append(f.Kamera);
		sql.append("','");
		sql.append(f.Filmformat);
		sql.append("','");
		sql.append(f.Empfindlichkeit);
		sql.append("','");
		sql.append(f.Filmtyp);
		sql.append("','");
		sql.append(f.Sonderentwicklung1);
		sql.append("','");
		sql.append(f.Sonderentwicklung2);
		sql.append("','");
		sql.append(b.Fokus);
		sql.append("','");
		sql.append(b.Blende);
		sql.append("','");
		sql.append(b.Zeit);
		sql.append("','");
		sql.append(b.Messmethode);
		sql.append("','");
		sql.append(b.Belichtungskorrektur);
		sql.append("','");
		sql.append(b.Makro);
		sql.append("','");
		sql.append(b.MakroVF);
		sql.append("','");
		sql.append(b.Filter);
		sql.append("','");
		sql.append(b.FilterVF);
		sql.append("','");
		sql.append(b.Blitz);
		sql.append("','");
		sql.append(b.Blitzkorrektur);
		sql.append("','");
		sql.append(b.Notiz);
		sql.append("','");
		sql.append(b.KameraNotiz);
		sql.append("','");
		sql.append(b.Objektiv);
		sql.append("','");

		String[] geotagParts = b.GeoTag.split("' , '");

		// lat
		sql.append(geotagParts[0]);
		sql.append("','");
		// long
		sql.append(geotagParts[1]);
		sql.append("','");

		sql.append(f.Filmbezeichnung);
		sql.append("','");
		sql.append(b.Bildnummer);
		sql.append("');");

		myDBFilm.execSQL(new String(sql));

		myDBFilm.close();
	}

	public void addPictureUpdateNummer(Context mContext, Film f, Bild b,
			int picturesNumber) {

		addPicture(mContext, f, b);

		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(DB.MY_DB_TABLE_NUMMER);
		sql.append(" SET bilder = '");
		sql.append(String.valueOf(picturesNumber));
		sql.append("' WHERE title = '");
		sql.append(f.Titel);
		sql.append("';");

		myDBNummer.execSQL(new String(sql));
		myDBNummer.close();
	}

	public void addPictureCreateNummer(Context mContext, Film f, Bild b,
			int picturesNumber, String encodedImage) {

		addPicture(mContext, f, b);

		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT OR REPLACE INTO ");
		sql.append(DB.MY_DB_TABLE_NUMMER);
		sql.append(" Values ('");
		sql.append(f.Titel);
		sql.append("'," + null);
		sql.append(",'");
		sql.append(f.Kamera);
		sql.append("','");
		sql.append(f.Datum);
		sql.append("',");
		sql.append(String.valueOf(picturesNumber));
		sql.append(",'");
		sql.append(encodedImage);
		sql.append("');");

		myDBNummer.execSQL(new String(sql));
		myDBNummer.close();
	}

	public void updateFilmDetails(Context mContext, Film film) {
		
		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		myDBFilm.execSQL("UPDATE " + DB.MY_DB_FILM_TABLE + " SET filmcamera = '" + film.Kamera
				+ "' WHERE filmtitle = '" + film.Titel + "'");
		myDBFilm.execSQL("UPDATE " + DB.MY_DB_FILM_TABLE + " SET filmnotiz = '" + film.Filmbezeichnung
				+ "' WHERE filmtitle = '" + film.Titel + "'");
		myDBFilm.execSQL("UPDATE " + DB.MY_DB_FILM_TABLE + " SET filmformat = '" + film.Filmformat
				+ "' WHERE filmtitle = '" + film.Titel + "'");
		myDBFilm.execSQL("UPDATE " + DB.MY_DB_FILM_TABLE + " SET filmempfindlichkeit = '" + film.Empfindlichkeit
				+ "' WHERE filmtitle = '" + film.Titel + "'");
		myDBFilm.execSQL("UPDATE " + DB.MY_DB_FILM_TABLE + " SET filmtyp = '" + film.Filmtyp
				+ "' WHERE filmtitle = '" + film.Titel + "'");
		myDBFilm.execSQL("UPDATE " + DB.MY_DB_FILM_TABLE + " SET filmsonder = '" + film.Sonderentwicklung1
				+ "' WHERE filmtitle = '" + film.Titel + "'");
		myDBFilm.execSQL("UPDATE " + DB.MY_DB_FILM_TABLE + " SET filmsonders = '" + film.Sonderentwicklung2
				+ "' WHERE filmtitle = '" + film.Titel + "'");
		myDBFilm.close();
		
		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(DB.MY_DB_NUMMER,
				Context.MODE_PRIVATE, null);
		myDBNummer.execSQL("UPDATE " + DB.MY_DB_TABLE_NUMMER + " SET camera = '" + film.Kamera
				+ "' WHERE title = '" + film.Titel + "'");
		
		myDBNummer.close();
			
	}

}