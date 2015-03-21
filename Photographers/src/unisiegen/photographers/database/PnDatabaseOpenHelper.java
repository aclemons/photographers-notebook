package unisiegen.photographers.database;

import java.io.File;
import java.util.ArrayList;

import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PnDatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "pn_data";

	private static final int DATABASE_VERSION = 1;

	public static final String COLUMN_ID = "ID";

	public static final String TABLE_FILMROLL = "Filmroll";
	public static final String COLUMN_TITLE = "Title";
	public static final String COLUMN_CREATEDDATE = "CreatedDate";
	public static final String COLUMN_INSERTEDINCAMERA = "InsertedInCamera";
	public static final String COLUMN_REMOVEDFROMCAMERA = "RemovedFromCamera";
	public static final String COLUMN_CAMERA = "Camera";
	public static final String COLUMN_FILMMAKERTYPE = "FilmMakerType";
	public static final String COLUMN_FILMTYPE = "FilmType";
	public static final String COLUMN_FILMFORMAT = "FilmFormat";
	public static final String COLUMN_ASA = "ASA";
	public static final String COLUMN_SPECIALDEVELOPMENT1 = "SpecialDevelopment1";
	public static final String COLUMN_SPECIALDEVELOPMENT2 = "SpecialDevelopment2";

	public static final String TABLE_PHOTO = "Photo";
	public static final String COLUMN_FILMROLLID = "FilmRollID";
	public static final String COLUMN_PHOTONUMBER = "PhotoNumber";
	public static final String COLUMN_EXPOSUREDATE = "ExposureDate";
	public static final String COLUMN_LENS = "Lens";
	public static final String COLUMN_APERTURE = "Aperture";
	public static final String COLUMN_SHUTTERSPEED = "ShutterSpeed";
	public static final String COLUMN_FOCUSDISTANCE = "FocusDistance";
	public static final String COLUMN_FILTER = "Filter";
	public static final String COLUMN_MAKRO = "Makro";
	public static final String COLUMN_FILTERVF = "FilterVF";
	public static final String COLUMN_MAKROVF = "MakroVF";
	public static final String COLUMN_FLASH = "Flash";
	public static final String COLUMN_FLASHCORRECTION = "FlashCorrection";
	public static final String COLUMN_EXPOSUREMEASUREMETHOD = "ExposureMeasureMethod";
	public static final String COLUMN_EXPORUSEMEASURECORRECTION = "ExporuseMeasureCorrection";
	public static final String COLUMN_NOTE = "Note";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_LATITUDE = "latitude";

	public static final String TABLE_SETTING = "Setting";
	public static final String COLUMN_GEARSETID = "GearSetID";
	public static final String COLUMN_SETTINGTYPE = "SettingType";
	public static final String COLUMN_SETTINGNAME = "SettingName";
	public static final String COLUMN_ISDISPLAYED = "IsDisplayed";
	public static final String COLUMN_ISDEFAULTSELECTION = "IsDefaultSelection";

	public static final String TABLE_GEARSET = "GearSet";
	public static final String COLUMN_SETNAME = "SetName";
	public static final String COLUMN_SETDESCRIPTION = "SetDescription";

	public static final String TABLE_CAMERALENSCOMBINATION = "CameraLensCombination";
	// ID
	public static final String COLUMN_CAMERAID = "CameraID";
	public static final String COLUMN_LENSID = "LensID";
	// GearSetID

	private static final String SETTING_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_SETTING + " (" + COLUMN_ID
			+ " integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_GEARSETID + " integer NOT NULL, " + COLUMN_SETTINGTYPE
			+ " text, " + COLUMN_SETTINGNAME + " text, " + COLUMN_ISDISPLAYED
			+ " integer, " + COLUMN_ISDEFAULTSELECTION + " integer, "
			+ "FOREIGN KEY (" + COLUMN_GEARSETID + ") " + "REFERENCES "
			+ TABLE_GEARSET + " (" + COLUMN_ID + ")" + ");";

	private static final String GEARSET_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_GEARSET + " (" + COLUMN_ID
			+ " integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_SETNAME + " text, " + COLUMN_SETDESCRIPTION + " text);";

	private static final String CAMLENS_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_CAMERALENSCOMBINATION + " (" + COLUMN_ID
			+ " integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_CAMERAID + " integer, " + COLUMN_LENSID + " integer, "
			+ COLUMN_GEARSETID + " integer NOT NULL, " + "FOREIGN KEY ("
			+ COLUMN_GEARSETID + ") " + "REFERENCES " + TABLE_GEARSET + "("
			+ COLUMN_ID + "), " + "FOREIGN KEY (" + COLUMN_CAMERAID + ") "
			+ "REFERENCES " + TABLE_SETTING + "(" + COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_LENSID + ") " + "REFERENCES "
			+ TABLE_SETTING + "(" + COLUMN_ID + ")" + ");";

	private static final String FILMROLL_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_FILMROLL + " (" + COLUMN_ID
			+ " integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_TITLE + " text, " + COLUMN_CREATEDDATE + " text, "
			+ COLUMN_INSERTEDINCAMERA + " text, " + COLUMN_REMOVEDFROMCAMERA
			+ " text, " + COLUMN_CAMERA + " text, " + COLUMN_FILMMAKERTYPE
			+ " text, " + COLUMN_FILMTYPE + " text, " + COLUMN_FILMFORMAT
			+ " text, " + COLUMN_ASA + " text, " + COLUMN_SPECIALDEVELOPMENT1
			+ " text, " + COLUMN_SPECIALDEVELOPMENT2 + " text);";

	private static final String PHOTO_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_PHOTO + " (" + COLUMN_ID
			+ " integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_FILMROLLID + " integer NOT NULL, " + COLUMN_PHOTONUMBER
			+ " integer, " + COLUMN_EXPOSUREDATE + " text, " + COLUMN_LENS
			+ " text, " + COLUMN_APERTURE + " text, " + COLUMN_SHUTTERSPEED
			+ " text, " + COLUMN_FOCUSDISTANCE + " text, " + COLUMN_FILTER
			+ " text, " + COLUMN_MAKRO + " text, " + COLUMN_FILTERVF
			+ " text, " + COLUMN_MAKROVF + " text, " + COLUMN_FLASH + " text, "
			+ COLUMN_FLASHCORRECTION + " text, " + COLUMN_EXPOSUREMEASUREMETHOD
			+ " text, " + COLUMN_EXPORUSEMEASURECORRECTION + " text, "
			+ COLUMN_NOTE + " text, " + COLUMN_LONGITUDE + " text, "
			+ COLUMN_LATITUDE + " text, " + "FOREIGN KEY (" + COLUMN_FILMROLLID
			+ ") REFERENCES " + TABLE_FILMROLL + " (" + COLUMN_ID + "));";

	private static final String logFile = "importLegacyDB.log";

	private Context context;
	private SQLiteDatabase db;
	private boolean log = false;

	private DatabaseImportLogger logger;

	public PnDatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		this.db = db;

		File file = new File(context.getFilesDir(), logFile);
		logger = new DatabaseImportLogger(file, true);
		log = logger.open();

		long t1 = System.currentTimeMillis();

		db.execSQL(GEARSET_TABLE_CREATE);
		if (log)
			logger.log(String
					.format("OK: Created new Table: %s", TABLE_GEARSET));
		db.execSQL(SETTING_TABLE_CREATE);
		if (log)
			logger.log(String
					.format("OK: Created new Table: %s", TABLE_SETTING));
		db.execSQL(CAMLENS_TABLE_CREATE);
		if (log)
			logger.log(String.format("OK: Created new Table: %s",
					TABLE_CAMERALENSCOMBINATION));

		db.execSQL(PHOTO_TABLE_CREATE);
		if (log)
			logger.log(String.format("OK: Created new Table: %s", TABLE_PHOTO));
		db.execSQL(FILMROLL_TABLE_CREATE);
		if (log)
			logger.log(String.format("OK: Created new Table: %s",
					TABLE_FILMROLL));

		if (legacyDataExist()) {
			importLegacyData();
			// remove old database

		} else {
			// establish new default data for settings
		}

		long t2 = System.currentTimeMillis();
		double importTime = (double) t2 - (double) t1;

		if (log)
			logger.log(String.format(
					"Legacy Database import finished. Duration: %d ms",
					(int) importTime));
		logger.close();
	}

	private void importLegacyData() {

		// import all data from different tables into a new one
		String[] gearSets = new String[] { DB.MY_DB_SET, DB.MY_DB_SET1,
				DB.MY_DB_SET2, DB.MY_DB_SET3 };
		for (String set : gearSets) {
			createSet(set);
			importSettingsForSet(set);
			createCamLensConnection(set);
		}
		importFilmRolls();
	}

	
	/**
	 * @deprecated This is old DB API and will be removed.
	 */
	private ArrayList<Film> getFilmeOldAPI() {

		ArrayList<Film> filme = new ArrayList<Film>();

		try {
			SQLiteDatabase myDBNummer = context.openOrCreateDatabase(
					DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);
			SQLiteDatabase myDBFilm = context.openOrCreateDatabase(
					DB.MY_DB_FILM, Context.MODE_PRIVATE, null);

			Cursor c = myDBNummer.rawQuery(
					"SELECT title,camera,datum,bilder,pic FROM "
							+ DB.MY_DB_TABLE_NUMMER + " ORDER BY datum DESC",
					null);
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
						film.Bilder = getBilderOldAPI(film.Titel, null);

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
	

	/**
	 * @deprecated This is old DB API and will be removed.
	 */
	private ArrayList<Bild> getBilderOldAPI(String title, String bildNummer) {

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

	private void importFilmRolls() {

		ArrayList<Film> filme = getFilmeOldAPI();
		for (Film film : filme) {

			// Create a Filmroll
			ContentValues cvFilm = new ContentValues();
			// cv.put("ID", "");
			cvFilm.put(COLUMN_TITLE, film.Titel);
			cvFilm.put(COLUMN_CREATEDDATE, film.Datum);
			cvFilm.put(COLUMN_INSERTEDINCAMERA, "");
			cvFilm.put(COLUMN_REMOVEDFROMCAMERA, "");
			cvFilm.put(COLUMN_CAMERA, film.Kamera);
			cvFilm.put(COLUMN_FILMMAKERTYPE, film.Filmbezeichnung);
			cvFilm.put(COLUMN_FILMTYPE, film.Filmtyp);
			cvFilm.put(COLUMN_FILMFORMAT, film.Filmformat);
			cvFilm.put(COLUMN_ASA, film.Empfindlichkeit);
			cvFilm.put(COLUMN_SPECIALDEVELOPMENT1, film.Sonderentwicklung1);
			cvFilm.put(COLUMN_SPECIALDEVELOPMENT2, film.Sonderentwicklung2);

			long result = db.insert(TABLE_FILMROLL, null, cvFilm);
			if (result == -1) {
				if (log)
					logger.log(String
							.format("FAILED: to create Filmroll: %s, %s %s from legacy data",
									film.Titel, film.Filmtyp, film.Kamera));
			} else {
				if (log)
					logger.log(String.format(
							"OK: Created Filmroll: %s, %s %s from legacy data",
							film.Titel, film.Filmtyp, film.Kamera));
			}

			Cursor filmRollC = db.query(TABLE_FILMROLL, null, COLUMN_TITLE
					+ " like ?", new String[] { film.Titel }, null, null, null);
			filmRollC.moveToFirst();
			int newFilmrollID = filmRollC.getInt(0);

			for (Bild bild : film.Bilder) {

				// Create a Photo
				ContentValues cvPhoto = new ContentValues();
				// cv.put("ID", "");
				cvPhoto.put(COLUMN_FILMROLLID, newFilmrollID);
				cvPhoto.put(COLUMN_PHOTONUMBER, bild.Bildnummer);
				cvPhoto.put(COLUMN_EXPOSUREDATE, bild.Zeitstempel);
				cvPhoto.put(COLUMN_LENS, bild.Objektiv);
				cvPhoto.put(COLUMN_APERTURE, bild.Blende);
				cvPhoto.put(COLUMN_SHUTTERSPEED, bild.Zeit);
				cvPhoto.put(COLUMN_FOCUSDISTANCE, bild.Fokus);
				cvPhoto.put(COLUMN_FILTER, bild.Filter);
				cvPhoto.put(COLUMN_MAKRO, bild.Makro);
				cvPhoto.put(COLUMN_FILTERVF, bild.FilterVF);
				cvPhoto.put(COLUMN_MAKROVF, bild.MakroVF);
				cvPhoto.put(COLUMN_FLASH, bild.Blitz);
				cvPhoto.put(COLUMN_FLASHCORRECTION, bild.Blitzkorrektur);
				cvPhoto.put(COLUMN_EXPOSUREMEASUREMETHOD, bild.Messmethode);
				cvPhoto.put(COLUMN_EXPORUSEMEASURECORRECTION, "");
				cvPhoto.put(COLUMN_NOTE, bild.Notiz);
				String[] latlon = bild.GeoTag.split(" - ");
				String lat = latlon[0].substring("Lat : ".length());
				String lon = latlon[1].substring("Long : ".length());
				cvPhoto.put(COLUMN_LONGITUDE, lat);
				cvPhoto.put(COLUMN_LATITUDE, lon);

				long result2 = db.insert(TABLE_PHOTO, null, cvPhoto);
				if (result2 == -1) {
					if (log)
						logger.log(String
								.format("FAILED: to create Photo: %s, %s from legacy data",
										bild.Bildnummer, bild.Objektiv));
				} else {
					if (log)
						logger.log(String.format(
								"OK: Created Photo: %s, %s from legacy data",
								bild.Bildnummer, bild.Objektiv));
				}
			}
		}
	}

	private void createCamLensConnection(String myDbSet) {

		Cursor gearset = db.query(TABLE_GEARSET, null, COLUMN_SETNAME
				+ " like ?", new String[] { myDbSet }, null, null, null);
		gearset.moveToFirst();
		int gearSetID = gearset.getInt(0);

		SQLiteDatabase legacyDB = context.openOrCreateDatabase(myDbSet,
				Context.MODE_PRIVATE, null);
		Cursor c = legacyDB.rawQuery("SELECT cam, bw FROM "
				+ DB.MY_DB_TABLE_SETCAMBW, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {

					// get id of the set
					// gearSetID

					// get id of a camera
					String camName = c.getString(c.getColumnIndex("cam"));
					Cursor cams = db
							.query(TABLE_SETTING, new String[] { COLUMN_ID },
									COLUMN_GEARSETID + " == ? AND "
											+ COLUMN_SETTINGNAME + " like ?",
									new String[] { String.valueOf(gearSetID),
											camName }, null, null, null);
					cams.moveToFirst();
					int camID = cams.getInt(0);

					// get id of a lens
					String lensName = c.getString(c.getColumnIndex("bw"));
					Cursor lenses = db
							.query(TABLE_SETTING, new String[] { COLUMN_ID },
									COLUMN_GEARSETID + " == ? AND "
											+ COLUMN_SETTINGNAME + " like ?",
									new String[] { String.valueOf(gearSetID),
											lensName }, null, null, null);
					lenses.moveToFirst();
					int lensID = lenses.getInt(0);

					ContentValues cv = new ContentValues();
					// cv.put("ID", "");
					cv.put(COLUMN_CAMERAID, camID);
					cv.put(COLUMN_LENSID, lensID);
					cv.put(COLUMN_GEARSETID, gearSetID);

					long result = db.insert(TABLE_CAMERALENSCOMBINATION, null,
							cv);
					if (result == -1) {
						if (log)
							logger.log(String
									.format("FAILED: to create CameraLensCombination: %s, %s from legacy data",
											camName, lensName));
					} else {
						if (log)
							logger.log(String
									.format("OK: Created CameraLensCombination: %s, %s from legacy data",
											camName, lensName));
					}

				} while (c.moveToNext());
			}
		}
		c.close();
		legacyDB.close();
	}

	private void createSet(String myDbSet) {

		ContentValues cv = new ContentValues();
		// cv.put("ID", "");
		cv.put(COLUMN_SETNAME, myDbSet);
		cv.put(COLUMN_SETDESCRIPTION,
				"Imported Set. Change this to your liking.");
		long result = db.insert(TABLE_GEARSET, null, cv);
		if (result == -1) {
			if (log)
				logger.log(String.format(
						"FAILED: to create GearSet: %s from legacy data",
						myDbSet));
		} else {
			if (log)
				logger.log(String.format(
						"OK: Created GearSet: %s from legacy data", myDbSet));
		}
	}

	private void importSettingsForSet(String myDbSet) {

		Cursor gearset = db.query(TABLE_GEARSET, null, COLUMN_SETNAME
				+ " like ?", new String[] { myDbSet }, null, null, null);
		gearset.moveToFirst();
		int gearSetID = gearset.getInt(0);

		SQLiteDatabase legacyDB = context.openOrCreateDatabase(myDbSet,
				Context.MODE_PRIVATE, null);
		for (String tableName : DB.tableNames) {
			Cursor c = legacyDB.rawQuery("SELECT name, value, def FROM "
					+ tableName, null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {

						ContentValues cv = new ContentValues();
						// cv.put("ID", "");
						cv.put(COLUMN_GEARSETID, gearSetID);
						cv.put(COLUMN_SETTINGTYPE, tableName);
						cv.put(COLUMN_SETTINGNAME,
								c.getString(c.getColumnIndex("name")));
						cv.put(COLUMN_ISDISPLAYED,
								c.getInt(c.getColumnIndex("value")));
						cv.put(COLUMN_ISDEFAULTSELECTION,
								c.getInt(c.getColumnIndex("def")));

						long result = db.insert(TABLE_SETTING, null, cv);
						if (result == -1) {
							if (log)
								logger.log(String
										.format("FAILED: to create Setting: %s, %s from legacy data",
												tableName,
												c.getString(c
														.getColumnIndex("name"))));
						} else {
							if (log)
								logger.log(String
										.format("OK: Created Setting: %s, %s from legacy data",
												tableName,
												c.getString(c
														.getColumnIndex("name"))));
						}

					} while (c.moveToNext());
				}
			}
			c.close();
		}
		legacyDB.close();
	}

	private boolean legacyDataExist() {

		SQLiteDatabase myDBSet = null;

		try {
			myDBSet = context.openOrCreateDatabase(DB.MY_DB_SET,
					Context.MODE_PRIVATE, null);

			if (myDBSet != null) {
				Cursor c = myDBSet.rawQuery("SELECT * FROM "
						+ DB.MY_DB_TABLE_SETZEI + ";", null);
				if (c != null && c.getCount() > 0) {
					c.close();
					return true;
				}
			}

		} catch (Exception e) {
			return false;
		} finally {
			if (myDBSet != null)
				myDBSet.close();
		}
		return false;

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}


}
