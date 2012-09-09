package unisiegen.photographers.activity;

/**
 * Hier "schießt" man die neuen Fotos! Man nimmt die Bildeinstellungen vor und speichert das Bild.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SlideNewPic extends Activity {

	/*
	 * Sonstige Variablen
	 */
	private static final String[] CONTENT = new String[] { "Optionen I/II",
			"Optionen II/II", "Notizen" };
	SharedPreferences settings;
	Context mContext;
	LocationManager locManager;
	LocationListener locationListener;

	byte[] pics;
	double piclong = 0;
	double piclat = 0;
	boolean bildtoedit;
	int picturesNumber;
	int defblende = 0;
	int deffokus = 0;
	int defzeit = 0;
	int defmessung = 0;
	int defkorr = 0;
	int defmakro = 0;
	int defmakrovf = 0;
	int deffilter = 0;
	int deffiltervf = 0;
	int defblitz = 0;
	int defblitzkorr = 0;
	int edit = 1;

	/*
	 * Spinner Variablen
	 */
	HashMap<String, Integer> blende, filtervf, objektiv, zeit, fokus, filter,
			makro, mess, belichtung, makrovf, blitz, blitzkorr;
	Spinner spinner_blende, spinner_filter_vf, spinner_objektiv, spinner_zeit,
			spinner_fokus, spinner_filter, spinner_makro, spinner_messmethode,
			spinner_belichtungs_korrektur, spinner_makro_vf, spinner_blitz,
			spinner_blitz_korrektur;
	ArrayAdapter<String> ad_spinner_blende, ad_spinner_zeit,
			ad_spinner_filter_vf, ad_spinner_objektiv, ad_spinner_focus,
			ad_spinner_filter, ad_spinner_makro, ad_spinner_messmethode,
			ad_spinner_belichtungs_korrektur, ad_spinner_makro_vf,
			ad_spinner_blitz, ad_spinner_blitz_korrektur;
	ArrayList<String> al_spinner_blende, al_spinner_filter_vf,
			al_spinner_objektiv, al_spinner_zeit, al_spinner_focus,
			al_spinner_filter, al_spinner_makro, al_spinner_messmethode,
			al_spinner_belichtungs_korrektur, al_spinner_makro_vf,
			al_spinner_blitz, al_spinner_blitz_korrektur;

	/*
	 * User-Interface Variablen
	 */
	EditText edit_notizen, edit_kamera_notizen;
	PopupWindow pw;
	Button plus, minus;
	Button aufnehmen;
	TextView nummerView;
	ViewPager viewPager;
	TitlePageIndicator mIndicator;

	/*
	 * Datenbank-Variablen
	 */
	SQLiteDatabase myDBSet = null;
	SQLiteDatabase myDB = null;
	SQLiteDatabase myDBFilm = null;
	SQLiteDatabase myDBNummer = null;
	static String MY_DB_NAME;
	static String MY_DB_NUMMER = "Nummern";
	static String MY_DB_FILM = "Filme";
	final static String MY_DB_TABLE_NUMMER = "Nummer";
	final static String MY_DB_FILM_TABLE = "Film";
	final static String MY_DB_SET = "Foto";
	final static String MY_DB_SET1 = "FotoSettingsOne";
	final static String MY_DB_SET2 = "FotoSettingsTwo";
	final static String MY_DB_SET3 = "FotoSettingsThree";
	final static String MY_DB_TABLE_SETCAM = "SettingsCamera";
	final static String MY_DB_TABLE_SETFF = "SettingsFilmFormat";
	final static String MY_DB_TABLE_SETEMP = "SettingsFilmEmpf";
	final static String MY_DB_TABLE_SETBW = "SettingsBrennweite";
	final static String MY_DB_TABLE_SETNM = "SettingsNahzubehor";
	final static String MY_DB_TABLE_SETFIL = "SettingsFilter";
	final static String MY_DB_TABLE_SETBLI = "SettingsBlitz";
	final static String MY_DB_TABLE_SETSON = "SettingsSonder";
	final static String MY_DB_TABLE_SETFOK = "SettingsFokus";
	final static String MY_DB_TABLE_SETBLE = "SettingsBlende";
	final static String MY_DB_TABLE_SETZEI = "SettingsZeit";
	final static String MY_DB_TABLE_SETMES = "SettingsMessung";
	final static String MY_DB_TABLE_SETPLU = "SettingsPlusMinus";
	final static String MY_DB_TABLE_SETMAK = "SettingsMakro";
	final static String MY_DB_TABLE_SETMVF = "SettingsMakroVF";
	final static String MY_DB_TABLE_SETFVF = "SettingsFilterVF";
	final static String MY_DB_TABLE_SETMVF2 = "SettingsMakroVF2";
	final static String MY_DB_TABLE_SETFVF2 = "SettingsFilterVF2";
	final static String MY_DB_TABLE_SETKOR = "SettingsBlitzKorr";
	final static String MY_DB_TABLE_SETTYP = "SettingsFilmTyp";
	final static String MY_DB_TABLE_SETCAMBW = "SettingsCameraBW";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause() LifeCycle Methoden
	 */

	@Override
	public void onPause() {
		super.onPause();
		try {
			locManager.removeUpdates(locationListener);
		} catch (Exception e) {
			Log.v("Check", "Es war kein GPS - Verf\u00FCgbar");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MY_DB_NAME = settings.getString("SettingsTable", "Foto");
		if (settings.getString("geoTag", "ja").equals("ja")) {
			getLocation();
		}
		fuellen();
		try {
			spinner_blende.setSelection(defblende);
			spinner_filter_vf.setSelection(deffiltervf);
			spinner_zeit.setSelection(defzeit);
			spinner_fokus.setSelection(deffokus);
			spinner_filter.setSelection(deffilter);
			spinner_makro.setSelection(defmakro);
			spinner_messmethode.setSelection(defmessung);
			spinner_belichtungs_korrektur.setSelection(defkorr);
			spinner_makro_vf.setSelection(defmakrovf);
			spinner_blitz.setSelection(defblitz);
			spinner_blitz_korrektur.setSelection(defblitzkorr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidenewfilm);
		mContext = this;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		MY_DB_NAME = settings.getString("SettingsTable", "Foto");

		nummerView = (TextView) findViewById(R.id.TextView_nr);
		bildtoedit = false;
		blende = new HashMap<String, Integer>();
		filtervf = new HashMap<String, Integer>();
		objektiv = new HashMap<String, Integer>();
		zeit = new HashMap<String, Integer>();
		fokus = new HashMap<String, Integer>();
		filter = new HashMap<String, Integer>();
		makro = new HashMap<String, Integer>();
		mess = new HashMap<String, Integer>();
		belichtung = new HashMap<String, Integer>();
		makrovf = new HashMap<String, Integer>();
		blitz = new HashMap<String, Integer>();
		blitzkorr = new HashMap<String, Integer>();

		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		int aktuellebildnummer = settings.getInt("BildNummerToBegin", 1);
		nummerView.setText("Bild " + aktuellebildnummer);

		if (settings.getBoolean("EditMode", false)) {
			picturesNumber = settings.getInt("BildNummern", 1);
		} else {
			Bundle extras = getIntent().getExtras();
			pics = extras.getByteArray("image");
		}
		MY_DB_NAME = settings.getString("SettingsTable", "Foto");
		fuellen();

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		MyPagerAdapter adapter = new MyPagerAdapter(this);
		viewPager.setAdapter(adapter);
		mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		mIndicator.setViewPager(viewPager);

		plus = (Button) findViewById(R.id.button_plus);
		minus = (Button) findViewById(R.id.button_minus);

		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nummerView.setText("Bild "
						+ (Integer.valueOf(nummerView.getText().toString()
								.replaceAll("[\\D]", "")) + 1));
				getPic(nummerView.getText().toString(),
						settings.getString("Title", " "));
				if (bildtoedit) {
					aufnehmen.setText("Änderungen speichern");
				} else {
					aufnehmen.setText("Foto aufnehmen");
				}

			}
		});
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Integer.valueOf(nummerView.getText().toString()
						.replaceAll("[\\D]", "")) > 1) {
					nummerView.setText("Bild "
							+ (Integer.valueOf(nummerView.getText().toString()
									.replaceAll("[\\D]", "")) - 1));
					getPic(nummerView.getText().toString(),
							settings.getString("Title", " "));
				}
				if (bildtoedit) {
					aufnehmen.setText("Änderungen speichern");
				} else {
					aufnehmen.setText("Foto aufnehmen");
				}
			}
		});

		aufnehmen = (Button) findViewById(R.id.button_aufnehmen);
		aufnehmen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = settings.edit();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String str = sdf.format(new Date());

				try {
					editor.putString("Uhrzeit", str);

					editor.commit();
					if (bildtoedit) {
						editFilm();
					} else {
						writeFilm();
						myDBNummer.close();
					}
					myDBFilm.close();
					Toast.makeText(getApplicationContext(), "Bild erstellt!",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"Fehlerhafte Eingabe!", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	/*
	 * Falls ein Bild bearbeitet werden soll, setze Spinner auf die Werte des
	 * Bildes
	 */
	private void getPic(String Bildnummer, String FilmTitel) {
		onCreateDBAndDBTabledFilm();
		Cursor c = myDBFilm
				.rawQuery(
						"SELECT _id,picfokus,picblende,piczeit,picmessung,picobjektiv, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM "
								+ MY_DB_FILM_TABLE
								+ " WHERE filmtitle = '"
								+ FilmTitel
								+ "' AND picnummer = '"
								+ Bildnummer + "'", null);
		if (c != null) {
			if (c.moveToFirst()) {
				bildtoedit = true;
				do {
					try {
						if (!blende.isEmpty()) {
							spinner_blende.setSelection(blende.get(c
									.getString(c.getColumnIndex("picblende"))));
						}
						if (!filtervf.isEmpty()) {
							spinner_filter_vf
									.setSelection(filtervf.get(c.getString(c
											.getColumnIndex("picfiltervf"))));
						}
						if (!objektiv.isEmpty()) {
							spinner_objektiv
									.setSelection(objektiv.get(c.getString(c
											.getColumnIndex("picobjektiv"))));
						}
						if (!zeit.isEmpty()) {
							spinner_zeit.setSelection(zeit.get(c.getString(c
									.getColumnIndex("piczeit"))));
						}
						if (!fokus.isEmpty()) {
							spinner_fokus.setSelection(fokus.get(c.getString(c
									.getColumnIndex("picfokus"))));
						}
						if (!filter.isEmpty()) {
							spinner_filter.setSelection(filter.get(c
									.getString(c.getColumnIndex("picfilter"))));
						}
						if (!makro.isEmpty()) {
							spinner_makro.setSelection(makro.get(c.getString(c
									.getColumnIndex("picmakro"))));
						}
						if (!mess.isEmpty()) {
							spinner_messmethode
									.setSelection(mess.get(c.getString(c
											.getColumnIndex("picmessung"))));
						}
						if (!belichtung.isEmpty()) {
							spinner_belichtungs_korrektur
									.setSelection(belichtung.get(c.getString(c
											.getColumnIndex("pickorr"))));
						}
						if (!makrovf.isEmpty()) {
							spinner_makro_vf
									.setSelection(makrovf.get(c.getString(c
											.getColumnIndex("picmakrovf"))));
						}
						if (!blitz.isEmpty()) {
							spinner_blitz.setSelection(blitz.get(c.getString(c
									.getColumnIndex("picblitz"))));
						}
						if (!blitzkorr.isEmpty()) {
							spinner_blitz_korrektur.setSelection(blitzkorr
									.get(c.getString(c
											.getColumnIndex("picblitzkorr"))));
						}
						edit_notizen.setText(c.getString(c
								.getColumnIndex("picnotiz")));
						edit_kamera_notizen.setText(c.getString(c
								.getColumnIndex("pickameranotiz")));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} while (c.moveToNext());
			} else {
				bildtoedit = false;
				Log.v("Check", "Kein Bild vorhanden");
			}
		}
		c.close();
		myDBFilm.close();
	}

	/*
	 * GPS Location für GeoTag der Bilder
	 */
	private void getLocation() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String provider = LocationManager.GPS_PROVIDER;
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				piclat = location.getLatitude();
				piclong = location.getLongitude();
			}

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};

		locManager.requestLocationUpdates(provider, 120000, 100,
				locationListener);
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		} else {
			Toast toast2 = Toast
					.makeText(
							this,
							"GPS ist ausgeschaltet! Schalten sie es jetzt an um den Bildern ein GeoTag hinzuzuf\u00FCgen",
							Toast.LENGTH_LONG);
			toast2.show();
		}
	}

	/*
	 * Datenbank Methoden
	 */

	private void onCreateDBAndDBTabledFilm() {
		myDBFilm = mContext.openOrCreateDatabase(MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		myDBFilm.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_FILM_TABLE
				+ " (_id integer primary key autoincrement, filmdatum varchar(100), picuhrzeit varchar(100), filmtitle varchar(100), filmcamera varchar(100), filmformat varchar(100), filmempfindlichkeit varchar(100), filmtyp varchar(100), filmsonder varchar(100), filmsonders varchar(100), picfokus varchar(100), picblende varchar(100), piczeit varchar(100), picmessung varchar(100), pickorr varchar(100), picmakro varchar(100), picmakrovf varchar(100), picfilter varchar(100), picfiltervf varchar(100), picblitz varchar(100), picblitzkorr varchar(100), picnotiz varchar(100), pickameranotiz varchar(100), picobjektiv varchar(100),piclong varchar(100),piclat varchar(100),filmnotiz varchar(100), picnummer varchar(100))"
				+ ";");
	}

	private void onCreateDBAndDBNumber() {
		myDBNummer = mContext.openOrCreateDatabase(MY_DB_NUMMER,
				Context.MODE_PRIVATE, null);
		myDBNummer
				.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_NUMMER
						+ " (title varchar(100) primary key, value integer,camera varchar(100), datum varchar(100), bilder integer, pic varchar(999))"
						+ ";");
	}

	private void writeFilm() { // Speichern des Bilds
		Log.v("Check", "writeFilm()");
		onCreateDBAndDBTabledFilm();
		onCreateDBAndDBNumber();
		picturesNumber++;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext); // datum
																			// zeit
																			// einfügen
																			// !!

		myDBFilm.execSQL("INSERT INTO " + MY_DB_FILM_TABLE + " Values (" + null
				+ ",'" + settings.getString("Datum", " ") + "','"
				+ settings.getString("Uhrzeit", " ") + "','"
				+ settings.getString("Title", " ") + "','"
				+ settings.getString("Kamera", " ") + "','"
				+ settings.getString("Filmformat", " ") + "','"
				+ settings.getString("Empfindlichkeit", " ") + "','"
				+ settings.getString("Filmtyp", " ") + "','"
				+ settings.getString("Sonder1", " ") + "','"
				+ settings.getString("Sonder2", " ") + "','"
				+ spinner_fokus.getSelectedItem().toString() + "','"
				+ spinner_blende.getSelectedItem().toString() + "','"
				+ spinner_zeit.getSelectedItem().toString() + "','"
				+ spinner_messmethode.getSelectedItem().toString() + "','"
				+ spinner_belichtungs_korrektur.getSelectedItem().toString()
				+ "','" + spinner_makro.getSelectedItem().toString() + "','"
				+ spinner_makro_vf.getSelectedItem().toString() + "','"
				+ spinner_filter.getSelectedItem().toString() + "','"
				+ spinner_filter_vf.getSelectedItem().toString() + "','"
				+ spinner_blitz.getSelectedItem().toString() + "','"
				+ spinner_blitz_korrektur.getSelectedItem().toString() + "','"
				+ edit_notizen.getText().toString() + "','"
				+ edit_kamera_notizen.getText().toString() + "','"
				+ spinner_objektiv.getSelectedItem().toString() + "','"
				+ String.valueOf(piclong) + "','" + String.valueOf(piclat)
				+ "','" + settings.getString("FilmNotiz", " ") + "','"
				+ nummerView.getText().toString() + "');");
		if (settings.getBoolean("EditMode", false)) {
			myDBNummer.execSQL("UPDATE " + MY_DB_TABLE_NUMMER
					+ " SET bilder = '" + picturesNumber + "' WHERE title = '"
					+ settings.getString("Title", " ") + "';");
		} else {
			String encodedImage = Base64.encodeToString(pics, Base64.DEFAULT);
			myDBNummer.execSQL("INSERT OR REPLACE INTO " + MY_DB_TABLE_NUMMER
					+ " Values ('" + settings.getString("Title", " ") + "',"
					+ null + ",'" + settings.getString("Kamera", " ") + "','"
					+ settings.getString("Datum", " ") + "','" + picturesNumber
					+ "','" + encodedImage + "');");
		}
		Log.v("Foto", "Eintrag vorm Speichern : " + pics);
		nummerView.setText("Bild "
				+ (Integer.valueOf(nummerView.getText().toString()
						.replaceAll("[\\D]", "")) + 1));
	}

	private void editFilm() { // Bearbeite ein gespeichertes Bild
		Log.v("Check", "editFilm()");
		onCreateDBAndDBTabledFilm();
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		myDBFilm.execSQL("UPDATE " + MY_DB_FILM_TABLE + " SET filmdatum = '"
				+ settings.getString("Datum", " ") + "', filmtitle = '"
				+ settings.getString("Title", " ") + "', filmcamera = '"
				+ settings.getString("Kamera", " ") + "', filmformat = '"
				+ settings.getString("Filmformat", " ")
				+ "', filmempfindlichkeit = '"
				+ settings.getString("Empfindlichkeit", " ") + "', filmtyp = '"
				+ settings.getString("Filmtyp", " ") + "', filmsonder = '"
				+ settings.getString("Sonder1", " ") + "', filmsonders = '"
				+ settings.getString("Sonder2", " ") + "', picfokus = '"
				+ spinner_fokus.getSelectedItem().toString()
				+ "', picblende = '"
				+ spinner_blende.getSelectedItem().toString()
				+ "', piczeit = '" + spinner_zeit.getSelectedItem().toString()
				+ "', picmessung = '"
				+ spinner_messmethode.getSelectedItem().toString()
				+ "', pickorr = '"
				+ spinner_belichtungs_korrektur.getSelectedItem().toString()
				+ "', picmakro = '"
				+ spinner_makro.getSelectedItem().toString()
				+ "', picmakrovf = '"
				+ spinner_makro_vf.getSelectedItem().toString()
				+ "', picfilter = '"
				+ spinner_filter.getSelectedItem().toString()
				+ "', picfiltervf = '"
				+ spinner_filter_vf.getSelectedItem().toString()
				+ "', picblitz = '"
				+ spinner_blitz.getSelectedItem().toString()
				+ "', picblitzkorr = '"
				+ spinner_blitz_korrektur.getSelectedItem().toString()
				+ "', picnotiz = '" + edit_notizen.getText().toString()
				+ "', pickameranotiz = '"
				+ edit_kamera_notizen.getText().toString()
				+ "', picobjektiv = '"
				+ spinner_objektiv.getSelectedItem().toString()
				+ "', picnummer = '" + nummerView.getText().toString()
				+ "' WHERE filmtitle = '" + settings.getString("Title", " ")
				+ "' AND picnummer = '" + nummerView.getText().toString()
				+ "';");

	}

	private void onCreateDBAndDBTabled() {

		myDB = mContext.openOrCreateDatabase(MY_DB_NAME, Context.MODE_PRIVATE,
				null);

		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETCAM
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETEMP
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETBW
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETNM
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFIL
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETBLI
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETSON
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETTYP
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");

		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFOK
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETBLE
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETZEI
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMES
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETPLU
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMAK
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMVF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFVF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETKOR
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMVF2
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFVF2
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");

	}

	private void fuellen() {
		onCreateDBAndDBTabled();
		al_spinner_blende = new ArrayList<String>();
		al_spinner_filter_vf = new ArrayList<String>();
		al_spinner_objektiv = new ArrayList<String>();
		al_spinner_zeit = new ArrayList<String>();
		al_spinner_focus = new ArrayList<String>();
		al_spinner_filter = new ArrayList<String>();
		al_spinner_makro = new ArrayList<String>();
		al_spinner_messmethode = new ArrayList<String>();
		al_spinner_belichtungs_korrektur = new ArrayList<String>();
		al_spinner_makro_vf = new ArrayList<String>();
		al_spinner_blitz = new ArrayList<String>();
		al_spinner_blitz_korrektur = new ArrayList<String>();
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		int index = 0;
		Cursor camBWCursor = myDB.rawQuery(
				"SELECT cam,bw FROM " + MY_DB_TABLE_SETCAMBW + " WHERE cam = '"
						+ settings.getString("Kamera", "") + "'", null);
		if (camBWCursor != null) {
			if (camBWCursor.moveToFirst()) {
				do {
					al_spinner_objektiv.add(camBWCursor.getString(camBWCursor
							.getColumnIndex("bw")));
					objektiv.put(camBWCursor.getString(camBWCursor
							.getColumnIndex("bw")), index);
					index++;
				} while (camBWCursor.moveToNext());
			} else {
				al_spinner_objektiv.add("Keine Auswahl");
			}
		}
		camBWCursor.close();
		index = 0;

		Cursor c_blende = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETBLE + " WHERE value = '" + edit + "'", null);
		if (c_blende != null) {
			if (c_blende.moveToFirst()) {
				do {
					if (c_blende.getInt(c_blende.getColumnIndex("def")) == 1) {
						defblende = index;
					}

					blende.put(
							c_blende.getString(c_blende.getColumnIndex("name")),
							index);
					index++;

					if ((c_blende.getString(c_blende.getColumnIndex("name"))
							.equals("Auto"))) {
						al_spinner_blende.add(c_blende.getString(c_blende
								.getColumnIndex("name")));
					} else {
						if (settings.getString("blendenstufe", "1/1").equals(
								"1/1")) {
							al_spinner_blende.add(c_blende.getString(c_blende
									.getColumnIndex("name")));
						} else if (settings.getString("blendenstufe", "1/1")
								.equals("1/2")) {
							al_spinner_blende.add(c_blende.getString(c_blende
									.getColumnIndex("name")));
							al_spinner_blende.add(c_blende.getString(c_blende
									.getColumnIndex("name")) + "+1/2");
						} else {
							al_spinner_blende.add(c_blende.getString(c_blende
									.getColumnIndex("name")));
							al_spinner_blende.add(c_blende.getString(c_blende
									.getColumnIndex("name")) + "+1/2");
							al_spinner_blende.add(c_blende.getString(c_blende
									.getColumnIndex("name")) + "+1/3");
						}
					}

				} while (c_blende.moveToNext());
			} else {
				al_spinner_blende.add("Keine Auswahl");
			}
		}
		c_blende.close();
		index = 0;

		Cursor c_zeit = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETZEI + " WHERE value = '" + edit + "'", null);
		if (c_zeit != null) {
			if (c_zeit.moveToFirst()) {
				do {
					if (c_zeit.getInt(c_zeit.getColumnIndex("def")) == 1) {
						defzeit = index;
					}
					zeit.put(c_zeit.getString(c_zeit.getColumnIndex("name")),
							index);
					index++;
					al_spinner_zeit.add(c_zeit.getString(c_zeit
							.getColumnIndex("name")));

				} while (c_zeit.moveToNext());
			} else {
				al_spinner_zeit.add("Keine Auswahl");
			}
		}
		c_zeit.close();
		index = 0;

		Cursor c_focus = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETFOK + " WHERE value = '" + edit + "'", null);
		if (c_focus != null) {
			if (c_focus.moveToFirst()) {
				do {
					if (c_focus.getInt(c_focus.getColumnIndex("def")) == 1) {
						deffokus = index;
					}
					fokus.put(
							c_focus.getString(c_focus.getColumnIndex("name")),
							index);
					index++;
					al_spinner_focus.add(c_focus.getString(c_focus
							.getColumnIndex("name")));
				} while (c_focus.moveToNext());
			} else {
				al_spinner_focus.add("Keine Auswahl");
			}
		}
		c_focus.close();
		index = 0;

		Cursor c_filter = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETFIL + " WHERE value = '" + edit + "'", null);
		if (c_filter != null) {
			if (c_filter.moveToFirst()) {
				do {
					if (c_filter.getInt(c_filter.getColumnIndex("def")) == 1) {
						deffilter = index;
					}
					filter.put(
							c_filter.getString(c_filter.getColumnIndex("name")),
							index);
					index++;
					al_spinner_filter.add(c_filter.getString(c_filter
							.getColumnIndex("name")));
				} while (c_filter.moveToNext());
			} else {
				al_spinner_filter.add("Keine Auswahl");
			}
		}
		c_filter.close();
		index = 0;

		Cursor c_makro = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETNM + " WHERE value = '" + edit + "'", null);
		if (c_makro != null) {
			if (c_makro.moveToFirst()) {
				do {
					if (c_makro.getInt(c_makro.getColumnIndex("def")) == 1) {
						defmakro = index;
					}
					makro.put(
							c_makro.getString(c_makro.getColumnIndex("name")),
							index);
					index++;
					Log.v("Check", "Makro ist nicht leer");
					al_spinner_makro.add(c_makro.getString(c_makro
							.getColumnIndex("name")));
				} while (c_makro.moveToNext());
			} else {
				al_spinner_makro.add("Keine Auswahl");
			}
		} else {
			Log.v("Check", "NULL :(");
		}
		c_makro.close();
		index = 0;

		Cursor c_messmethode = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETMES + " WHERE value = '" + edit + "'", null);
		if (c_messmethode != null) {
			if (c_messmethode.moveToFirst()) {
				do {
					if (c_messmethode.getInt(c_messmethode
							.getColumnIndex("def")) == 1) {
						defmessung = index;
					}
					mess.put(c_messmethode.getString(c_messmethode
							.getColumnIndex("name")), index);
					index++;
					al_spinner_messmethode.add(c_messmethode
							.getString(c_messmethode.getColumnIndex("name")));
				} while (c_messmethode.moveToNext());
			} else {
				al_spinner_messmethode.add("Keine Auswahl");
			}
		}
		c_messmethode.close();
		index = 0;

		Cursor c_belichtungs_korrektur = myDB.rawQuery(
				"SELECT name,value,def FROM " + MY_DB_TABLE_SETPLU
						+ " WHERE value = '" + edit + "'", null);
		if (c_belichtungs_korrektur != null) {
			if (c_belichtungs_korrektur.moveToFirst()) {
				do {
					if (c_belichtungs_korrektur.getInt(c_belichtungs_korrektur
							.getColumnIndex("def")) == 1) {
						defkorr = index;
					}
					belichtung.put(c_belichtungs_korrektur
							.getString(c_belichtungs_korrektur
									.getColumnIndex("name")), index);
					index++;
					al_spinner_belichtungs_korrektur
							.add(c_belichtungs_korrektur
									.getString(c_belichtungs_korrektur
											.getColumnIndex("name")));
				} while (c_belichtungs_korrektur.moveToNext());
			} else {
				al_spinner_belichtungs_korrektur.add("Keine Auswahl");
			}
		}
		c_belichtungs_korrektur.close();
		index = 0;

		if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Faktor (*)")) {
			Cursor change = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETFVF + " WHERE value = '" + edit + "'",
					null);
			if (change != null) {
				if (change.moveToFirst()) {
					do {
						if (change.getInt(change.getColumnIndex("def")) == 1) {
							deffiltervf = index;
						}
						filtervf.put(
								change.getString(change.getColumnIndex("name")),
								index);
						index++;
						Log.v("Check", "Index Test : " + index);
						al_spinner_filter_vf.add(change.getString(change
								.getColumnIndex("name")));
					} while (change.moveToNext());
				} else {
					al_spinner_filter_vf.add("Keine Auswahl");
				}
			}
			index = 0;
			Cursor changes = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETMVF + " WHERE value = '" + edit + "'",
					null);
			if (changes != null) {
				if (changes.moveToFirst()) {
					do {
						if (changes.getInt(changes.getColumnIndex("def")) == 1) {
							defmakrovf = index;
						}
						makrovf.put(changes.getString(changes
								.getColumnIndex("name")), index);
						index++;
						al_spinner_makro_vf.add(changes.getString(changes
								.getColumnIndex("name")));
					} while (changes.moveToNext());
				} else {
					al_spinner_makro_vf.add("Keine Auswahl");
				}
			}
			change.close();
			changes.close();
			index = 0;
		} else if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Blendenzugaben (+)")) {
			Cursor change2 = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETFVF2 + " WHERE value = '" + edit + "'",
					null);
			if (change2 != null) {
				if (change2.moveToFirst()) {
					do {
						if (change2.getInt(change2.getColumnIndex("def")) == 1) {
							deffiltervf = index;
						}
						filtervf.put(change2.getString(change2
								.getColumnIndex("name")), index);
						index++;
						al_spinner_filter_vf.add(change2.getString(change2
								.getColumnIndex("name")));
					} while (change2.moveToNext());
				} else {
					al_spinner_filter_vf.add("Keine Auswahl");
				}
			}
			index = 0;
			Cursor changes2 = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETMVF2 + " WHERE value = '" + edit + "'",
					null);
			if (changes2 != null) {
				if (changes2.moveToFirst()) {
					do {
						if (changes2.getInt(changes2.getColumnIndex("def")) == 1) {
							defmakrovf = index;
						}
						makrovf.put(changes2.getString(changes2
								.getColumnIndex("name")), index);
						index++;
						al_spinner_makro_vf.add(changes2.getString(changes2
								.getColumnIndex("name")));
					} while (changes2.moveToNext());
				} else {
					al_spinner_makro_vf.add("Keine Auswahl");
				}
			}
			change2.close();
			changes2.close();
			index = 0;
		}

		Cursor c_blitz = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETBLI + " WHERE value = '" + edit + "'", null);
		if (c_blitz != null) {
			if (c_blitz.moveToFirst()) {
				do {
					if (c_blitz.getInt(c_blitz.getColumnIndex("def")) == 1) {
						defblitz = index;
					}
					blitz.put(
							c_blitz.getString(c_blitz.getColumnIndex("name")),
							index);
					index++;
					al_spinner_blitz.add(c_blitz.getString(c_blitz
							.getColumnIndex("name")));
				} while (c_blitz.moveToNext());
			} else {
				al_spinner_blitz.add("Keine Auswahl");
			}
		}
		c_blitz.close();
		index = 0;

		Cursor c_blitz_korrektur = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETKOR + " WHERE value = '" + edit + "'", null);
		if (c_blitz_korrektur != null) {
			if (c_blitz_korrektur.moveToFirst()) {
				do {
					if (c_blitz_korrektur.getInt(c_blitz_korrektur
							.getColumnIndex("def")) == 1) {
						defblitzkorr = index;
					}
					blitzkorr.put(c_blitz_korrektur.getString(c_blitz_korrektur
							.getColumnIndex("name")), index);
					index++;
					al_spinner_blitz_korrektur
							.add(c_blitz_korrektur.getString(c_blitz_korrektur
									.getColumnIndex("name")));
				} while (c_blitz_korrektur.moveToNext());
			} else {
				al_spinner_blitz_korrektur.add("Keine Auswahl");
			}
		}
		c_blitz_korrektur.close();

	}

	/*
	 * HilfsKlassen für SlideView etc.
	 */

	public void setFooterColor(int footerColor) { // Farbe für die Balken unter
													// dem Titel (SlideView)
		mIndicator.setFooterColor(footerColor);
	}

	private class MyPagerAdapter extends PagerAdapter implements TitleProvider {

		private ArrayList<View> views;

		public MyPagerAdapter(Context context) {
			views = new ArrayList<View>();
			LayoutInflater inflater = getLayoutInflater();

			// Allgemein
			// ---------------------------------------------------------------------------
			View v1 = inflater.inflate(R.layout.pictab1, null, false);

			final SharedPreferences.Editor editors = settings.edit();

			spinner_objektiv = (Spinner) v1
					.findViewById(R.id.spinner_brennweite);
			ad_spinner_objektiv = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_spinner_item, al_spinner_objektiv);
			spinner_objektiv.setAdapter(ad_spinner_objektiv);
			spinner_objektiv
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							editors.putString("Objektiv", spinner_objektiv
									.getSelectedItem().toString());
							editors.commit();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			spinner_blende = (Spinner) v1.findViewById(R.id.spinner_blende);
			ArrayAdapter<String> ad_spinner_blende = new ArrayAdapter<String>(
					mContext, android.R.layout.simple_spinner_item,
					al_spinner_blende);
			spinner_blende.setAdapter(ad_spinner_blende);
			spinner_blende
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							editors.putString("blende", spinner_blende
									.getSelectedItem().toString());
							editors.commit();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			spinner_zeit = (Spinner) v1.findViewById(R.id.spinner_zeit);
			ad_spinner_zeit = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_spinner_item, al_spinner_zeit);
			spinner_zeit.setAdapter(ad_spinner_zeit);
			spinner_zeit
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							editors.putString("zeit", spinner_zeit
									.getSelectedItem().toString());
							editors.commit();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			spinner_fokus = (Spinner) v1.findViewById(R.id.spinner_fokus);
			ArrayAdapter<String> ad_spinner_focus = new ArrayAdapter<String>(
					mContext, android.R.layout.simple_spinner_item,
					al_spinner_focus);
			spinner_fokus.setAdapter(ad_spinner_focus);
			spinner_fokus
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							editors.putString("fokus", spinner_fokus
									.getSelectedItem().toString());
							editors.commit();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			spinner_filter = (Spinner) v1.findViewById(R.id.spinner_filter);
			ArrayAdapter<String> ad_spinner_filter = new ArrayAdapter<String>(
					mContext, android.R.layout.simple_spinner_item,
					al_spinner_filter);
			spinner_filter.setAdapter(ad_spinner_filter);
			spinner_filter
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							editors.putString("filter", spinner_filter
									.getSelectedItem().toString());
							editors.commit();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			spinner_makro = (Spinner) v1.findViewById(R.id.spinner_makro);
			ad_spinner_makro = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_spinner_item, al_spinner_makro);
			spinner_makro.setAdapter(ad_spinner_makro);
			spinner_makro
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							editors.putString("makro", spinner_makro
									.getSelectedItem().toString());
							editors.commit();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			views.add(v1);

			View v2 = inflater.inflate(R.layout.pictab2, null, false);
			if (true) {

				spinner_filter_vf = (Spinner) v2
						.findViewById(R.id.spinner_filter_vf);
				ad_spinner_filter_vf = new ArrayAdapter<String>(mContext,
						android.R.layout.simple_spinner_item,
						al_spinner_filter_vf);
				spinner_filter_vf.setAdapter(ad_spinner_filter_vf);
				spinner_filter_vf
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("FilterVF", spinner_filter_vf
										.getSelectedItem().toString());
								editors.commit();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});

				spinner_messmethode = (Spinner) v2
						.findViewById(R.id.spinner_messmethode);
				ArrayAdapter<String> ad_spinner_messmethode = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						al_spinner_messmethode);
				spinner_messmethode.setAdapter(ad_spinner_messmethode);
				spinner_messmethode
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("messmethode",
										spinner_messmethode.getSelectedItem()
												.toString());
								editors.commit();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});

				spinner_belichtungs_korrektur = (Spinner) v2
						.findViewById(R.id.spinner_belichtungs_korrektur);
				ArrayAdapter<String> ad_spinner_belichtungs_korrektur = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						al_spinner_belichtungs_korrektur);
				spinner_belichtungs_korrektur
						.setAdapter(ad_spinner_belichtungs_korrektur);
				spinner_belichtungs_korrektur
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("belichtungs_korrektur",
										spinner_belichtungs_korrektur
												.getSelectedItem().toString());
								editors.commit();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});

				spinner_makro_vf = (Spinner) v2
						.findViewById(R.id.spinner_makro_vf);
				ArrayAdapter<String> ad_spinner_makro_vf = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						al_spinner_makro_vf);
				spinner_makro_vf.setAdapter(ad_spinner_makro_vf);
				spinner_makro_vf
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("makro_vf", spinner_makro_vf
										.getSelectedItem().toString());
								editors.commit();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});

				spinner_blitz = (Spinner) v2.findViewById(R.id.spinner_blitz);
				ArrayAdapter<String> ad_spinner_blitz = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						al_spinner_blitz);
				spinner_blitz.setAdapter(ad_spinner_blitz);
				spinner_blitz
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("blitz", spinner_blitz
										.getSelectedItem().toString());
								editors.commit();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});

				spinner_blitz_korrektur = (Spinner) v2
						.findViewById(R.id.spinner_blitz_korrektur);
				ArrayAdapter<String> ad_spinner_blitz_korrektur = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						al_spinner_blitz_korrektur);
				spinner_blitz_korrektur.setAdapter(ad_spinner_blitz_korrektur);
				spinner_blitz_korrektur
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("blitz_korrektur",
										spinner_blitz_korrektur
												.getSelectedItem().toString());
								editors.commit();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
			}
			views.add(v2);

			View v3 = inflater.inflate(R.layout.pictab3, null, false);
			if (true) {

				edit_notizen = (EditText) v3.findViewById(R.id.edit_notizen);
				edit_kamera_notizen = (EditText) v3
						.findViewById(R.id.edit_kamera_notizen);
			}
			views.add(v3);
		}

		@Override
		public void destroyItem(View view, int arg1, Object object) {
			((ViewPager) view).removeView((LinearLayout) object);
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View view, int position) {
			View myView = views.get(position);
			((ViewPager) view).addView(myView);
			return myView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public String getTitle(int position) {
			if (viewPager.getCurrentItem() == 0)
				setFooterColor(0xFF000000);
			if (viewPager.getCurrentItem() == 1)
				setFooterColor(0xFF000000);
			if (viewPager.getCurrentItem() == 2)
				setFooterColor(0xFF000000);
			return SlideNewPic.CONTENT[position % SlideNewPic.CONTENT.length];
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) Menü
	 * Methoden
	 */

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.opt_sett3) {
			if (settings.getString("allinone", "ja").equals("nein")) {
				LayoutInflater inflaterOwn = (LayoutInflater) mContext
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layoutOwn = inflaterOwn.inflate(R.layout.popupoption,
						(ViewGroup) findViewById(R.id.users), false);
				final ImageButton setone = (ImageButton) layoutOwn
						.findViewById(R.id.setone);
				final ImageButton settwo = (ImageButton) layoutOwn
						.findViewById(R.id.settwo);
				final ImageButton setthree = (ImageButton) layoutOwn
						.findViewById(R.id.setthree);
				final ImageButton setfour = (ImageButton) layoutOwn
						.findViewById(R.id.setfour);

				setone.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent3 = new Intent(getApplicationContext(),
								SlideNewSettingsGen.class);
						startActivityForResult(myIntent3, 0);
						pw.dismiss();
					}
				});

				settwo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent3 = new Intent(getApplicationContext(),
								SlideNewSettingsCam.class);
						startActivityForResult(myIntent3, 0);
						pw.dismiss();
					}
				});

				setthree.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent3 = new Intent(getApplicationContext(),
								SlideNewSettingsPic.class);
						startActivityForResult(myIntent3, 0);
						pw.dismiss();
					}
				});

				setfour.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent3 = new Intent(getApplicationContext(),
								SlideNewSettingsSon.class);
						startActivityForResult(myIntent3, 0);
						pw.dismiss();
					}
				});

				pw = new PopupWindow(layoutOwn,
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT, true);
				pw.setAnimationStyle(-1);
				pw.setBackgroundDrawable(new BitmapDrawable());
				pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
			} else {
				Intent myIntent3 = new Intent(getApplicationContext(),
						SlideNewSettings.class);
				startActivityForResult(myIntent3, 0);
			}
			return true;
		} else if (item.getItemId() == R.id.opt_sett1) {
			finish();
			startActivity(new Intent(getApplicationContext(),
					FilmAuswahlActivity.class));
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}
