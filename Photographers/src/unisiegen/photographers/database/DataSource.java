package unisiegen.photographers.database;

import java.util.ArrayList;

import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {

	private SQLiteDatabase database;
	private PnDatabaseOpenHelper dbHelper;

	private static DataSource instance;

	private DataSource(Context context) {

		// for debugging purposes
		/*
		if (context.deleteDatabase("pn_data")) {
			Log.v("DB", "Old pn_data database removed.");
		} else {
			Log.v("DB", "ATTENTION: Old pn_data database could not be removed.");
		}
		*/
		// for debugging purposes
		

		if (dbHelper == null) {
			dbHelper = new PnDatabaseOpenHelper(context);
		}

		if (database == null) {
			database = dbHelper.getWritableDatabase();
		}
	}

	public static DataSource getInst(Context context) {
		if (instance == null) {
			instance = new DataSource(context);
		}

		return instance;
	}


	public void close() {
		if (database != null) {
			database.close();
		}
		dbHelper.close();
	}


	// TODO: Move to List instead of ArrayList?
	public ArrayList<Film> getFilms() {

		Cursor filmC = database.query(PnDatabaseOpenHelper.TABLE_FILMROLL,
				null, null, null, null, null, null);

		ArrayList<Film> result = parseFilmsFromCursor(filmC);

		return result;
	}
	
	
	public Film getFilm(String title) {

		Cursor cursor = database.query(PnDatabaseOpenHelper.TABLE_FILMROLL, null, PnDatabaseOpenHelper.COLUMN_TITLE + " == ?", 
				new String[]{title}, null, null, null);

		ArrayList<Film> result = parseFilmsFromCursor(cursor);
		
		if(result.size() != 1){
			// TODO: There is a Problem...
			Log.v("DATA_SOURCE", "While Querying for a FilmRoll Title, more than one element was returned: There are duplicates in the Database.");
			return null;
		}		

		return result.get(0);
	}
	

	private ArrayList<Film> parseFilmsFromCursor(Cursor c) {

		ArrayList<Film> result = new ArrayList<Film>();

		if (c != null && c.moveToFirst()) {
			do {
				Film film = new Film();
				film.Bilder = new ArrayList<Bild>();
				film.ID = c.getInt(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_ID));
				film.Titel = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_TITLE));
				film.Datum = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_CREATEDDATE));
				film.Empfindlichkeit = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_ASA));
				film.Filmbezeichnung = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FILMMAKERTYPE));
				film.Filmformat = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FILMFORMAT));
				film.Filmtyp = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FILMTYPE));
				film.Kamera = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_CAMERA));
				film.Pics = "0"; // TODO: Count that manually
				film.Sonderentwicklung1 = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_SPECIALDEVELOPMENT1));
				film.Sonderentwicklung2 = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_SPECIALDEVELOPMENT2));

				result.add(film);

				int filmRollID = c.getInt(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_ID));
				film.Bilder = getPhotosForFilm(filmRollID);
				film.Pics = String.valueOf(film.Bilder.size());

			} while (c.moveToNext());
		}

		return result;
	}

	private ArrayList<Bild> getPhotosForFilm(int filmRollID) {

		Cursor c = database.query(PnDatabaseOpenHelper.TABLE_PHOTO, null,
				PnDatabaseOpenHelper.COLUMN_FILMROLLID + " == ?",
				new String[] { String.valueOf(filmRollID) }, null, null, null);

		ArrayList<Bild> result = new ArrayList<Bild>();

		if (c.moveToFirst()) {
			do {

				Bild bild = new Bild();
				bild.ID = c.getInt(c.getColumnIndex(PnDatabaseOpenHelper.COLUMN_ID));
				bild.Bildnummer = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_PHOTONUMBER));
				bild.Belichtungskorrektur = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_EXPORUSEMEASURECORRECTION));
				bild.Blende = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_APERTURE));
				bild.Blitz = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FLASH));
				bild.Blitzkorrektur = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FLASHCORRECTION));
				bild.Filter = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FILTER));
				bild.FilterVF = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FILTERVF));
				bild.Fokus = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_FOCUSDISTANCE));
				bild.GeoTag = ""; // TODO!!!!
				// bild.KameraNotiz =
				// c.getString(c.getColumnIndex(PnDatabaseOpenHelper.COLUMN_));
				bild.Makro = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_MAKRO));
				bild.MakroVF = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_MAKROVF));
				bild.Messmethode = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_EXPOSUREMEASUREMETHOD));
				bild.Notiz = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_NOTE));
				bild.Objektiv = c.getString(c
						.getColumnIndex(PnDatabaseOpenHelper.COLUMN_LENS));
				bild.Zeit = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_SHUTTERSPEED));
				bild.Zeitstempel = c
						.getString(c
								.getColumnIndex(PnDatabaseOpenHelper.COLUMN_EXPOSUREDATE));
				result.add(bild);

			} while (c.moveToNext());
		}

		return result;
	}
	
	
	public void updateFilm(Film film){

		ContentValues updateValues = new ContentValues();
		updateValues.put(PnDatabaseOpenHelper.COLUMN_FILMMAKERTYPE, film.Filmbezeichnung);
		updateValues.put(PnDatabaseOpenHelper.COLUMN_CAMERA, film.Kamera);
		updateValues.put(PnDatabaseOpenHelper.COLUMN_FILMFORMAT, film.Filmformat);
		updateValues.put(PnDatabaseOpenHelper.COLUMN_ASA, film.Empfindlichkeit);
		updateValues.put(PnDatabaseOpenHelper.COLUMN_FILMTYPE, film.Filmtyp);
		updateValues.put(PnDatabaseOpenHelper.COLUMN_SPECIALDEVELOPMENT1, film.Sonderentwicklung1);
		updateValues.put(PnDatabaseOpenHelper.COLUMN_SPECIALDEVELOPMENT2, film.Sonderentwicklung2);		
		
		database.update(PnDatabaseOpenHelper.TABLE_FILMROLL, updateValues, PnDatabaseOpenHelper.COLUMN_ID + "=" + film.ID, null);
	}

	public void deleteFilm(String titel) {
		
		Film tempFilm = getFilm(titel);
		if(tempFilm == null){
			// Log an error
			return;
		}
		
		int deletedPhotos = database.delete(PnDatabaseOpenHelper.TABLE_PHOTO, PnDatabaseOpenHelper.COLUMN_FILMROLLID + " == ?", new String []{ String.valueOf(tempFilm.ID)});
		int deletedFilms  = database.delete(PnDatabaseOpenHelper.TABLE_FILMROLL, PnDatabaseOpenHelper.COLUMN_ID + " == ?", new String []{ String.valueOf(tempFilm.ID)});
		
		Log.v("DB", String.format("Deleted film: %s from database. %d Photos were remove, %d Films were removed.", 
				tempFilm.Titel, deletedPhotos, deletedFilms));
		
	}
	
	public void deletePhoto(Bild photo) {
		
		int deletedPhotos = 
				database.delete(PnDatabaseOpenHelper.TABLE_PHOTO, 
						PnDatabaseOpenHelper.COLUMN_ID + " == ?", 
						new String []{ String.valueOf(photo.ID)});
		
		Log.v("DB", String.format("Deleted photo: %d from database. %d photos deleted", photo.ID, deletedPhotos));		
	}

	public boolean isFilmTitleTaken(String titleToTest){
		
		return getFilm(titleToTest) != null ? true : false;
	}
	
	
	private ContentValues convertPhotoToContentValues(Film film, Bild photo){
		
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(PnDatabaseOpenHelper.COLUMN_FILMROLLID, film.ID);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_PHOTONUMBER, photo.Bildnummer);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_EXPORUSEMEASURECORRECTION, photo.Belichtungskorrektur);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_APERTURE, photo.Blende);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_FLASH, photo.Blitz);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_FLASHCORRECTION, photo.Blitzkorrektur);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_FILTER, photo.Filter);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_FILTERVF, photo.FilterVF);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_FOCUSDISTANCE, photo.Fokus);
		// Geotag
		contentValues.put(PnDatabaseOpenHelper.COLUMN_MAKRO, photo.Makro);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_MAKROVF, photo.MakroVF);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_EXPOSUREMEASUREMETHOD, photo.Messmethode);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_NOTE, photo.Notiz);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_LENS, photo.Objektiv);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_SHUTTERSPEED, photo.Zeit);
		contentValues.put(PnDatabaseOpenHelper.COLUMN_EXPOSUREDATE, photo.Zeitstempel);
		
		return contentValues;
	}
	
	public void addPhoto(Film film, Bild photo){
		
		long rowID = database.insert(PnDatabaseOpenHelper.TABLE_PHOTO, null, convertPhotoToContentValues(film, photo));

		Log.v("DB", String.format("Added a new photo. ID of the new row is %d.", rowID));
	}
	
	
	public void updatePhoto(Film film, Bild photo){

		if(photo.ID == 0){
			Log.v("DB", String.format("Trying to update a photo that has no ID. Will probably break."));
		}
		int updatedRows = database.update(PnDatabaseOpenHelper.TABLE_PHOTO, convertPhotoToContentValues(film, photo), 
				PnDatabaseOpenHelper.COLUMN_ID + "=?", new String[] {String.valueOf(photo.ID)});		
		
		Log.v("DB", String.format("Updated photo " + photo.ID + ". %d rows updated.", updatedRows));
	}

	public long addFilm(Film film) {
		
		// check if film does exist already
		if(isFilmTitleTaken(film.Titel)){
			return -1;
		}
		
		ContentValues values = new ContentValues();

		//values.put(PnDatabaseOpenHelper.COLUMN_FILMROLLID, film.ID);
		values.put(PnDatabaseOpenHelper.COLUMN_TITLE, film.Titel);
		values.put(PnDatabaseOpenHelper.COLUMN_CREATEDDATE, film.Datum);
		values.put(PnDatabaseOpenHelper.COLUMN_INSERTEDINCAMERA, "");
		values.put(PnDatabaseOpenHelper.COLUMN_REMOVEDFROMCAMERA, "");
		values.put(PnDatabaseOpenHelper.COLUMN_CAMERA, film.Kamera);
		values.put(PnDatabaseOpenHelper.COLUMN_FILMMAKERTYPE, film.Filmbezeichnung); // check if this is what you expect it to be
		values.put(PnDatabaseOpenHelper.COLUMN_FILMTYPE, film.Filmtyp);
		values.put(PnDatabaseOpenHelper.COLUMN_FILMFORMAT, film.Filmformat);
		values.put(PnDatabaseOpenHelper.COLUMN_ASA, film.Empfindlichkeit);
		values.put(PnDatabaseOpenHelper.COLUMN_SPECIALDEVELOPMENT1, film.Sonderentwicklung1);
		values.put(PnDatabaseOpenHelper.COLUMN_SPECIALDEVELOPMENT2, film.Sonderentwicklung2);
				
		long rowID = database.insert(PnDatabaseOpenHelper.TABLE_FILMROLL, null, values);

		Log.v("DB", String.format("Added a new photo. ID of the new row is %d.", rowID));
		
		return rowID;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public ArrayList<Integer> getGearSets(){
		
		Cursor c = database.query(PnDatabaseOpenHelper.TABLE_GEARSET, null, null, null, null, null, null);
		ArrayList<Integer> result = new ArrayList<Integer>();

		if (c != null && c.moveToFirst()) {
			do {
				result.add(c.getInt(c.getColumnIndex(PnDatabaseOpenHelper.COLUMN_ID)));
				
			} while (c.moveToNext());
		}

		return result;		
	}
	
}
