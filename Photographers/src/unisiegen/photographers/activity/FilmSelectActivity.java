package unisiegen.photographers.activity;

/**
 * In dieser Activity sieht man den ausgew�hlten Film mit allen Infos und kann sich die zugeh�rigen Bilder betrachten und ausw�hlen.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class FilmSelectActivity extends Activity {

	/*
	 * User-Interface Elemente
	 */
	TextView freecell;
	int design = 0;
	int bilderimfilm;
	LinearLayout infoBlock1;
	TextView filmcam;
	LinearLayout infoBlock2;
	TextView filmtit;
	TitlePageIndicator mIndicator;
	PopupWindow pw;
	Spinner objektivedit = null;
	Spinner filtervfedit = null;
	Spinner picfocusedit = null;
	Spinner picblendeedit = null;
	Spinner piczeitedit = null;
	Spinner picmessungedit = null;
	Spinner picplusedit = null;
	Spinner picmakroedit = null;
	Spinner picmakrovfedit = null;
	Spinner picfilteredit = null;
	Spinner picblitzedit = null;
	Spinner picblitzkorredit = null;
	EditText picnotizedit = null;
	EditText picnotizcamedit = null;

	/*
	 * Arrayadapter und ArrayListen der Einstellungen
	 */
	ArrayAdapter<String> ad_spinner_blende;
	ArrayAdapter<String> ad_spinner_zeit;
	ArrayAdapter<String> ad_spinner_filter_vf;
	ArrayAdapter<String> ad_spinner_objektiv;
	ArrayAdapter<String> ad_spinner_focus;
	ArrayAdapter<String> ad_spinner_filter;
	ArrayAdapter<String> ad_spinner_makro;
	ArrayAdapter<String> ad_spinner_messmethode;
	ArrayAdapter<String> ad_spinner_belichtungs_korrektur;
	ArrayAdapter<String> ad_spinner_makro_vf;
	ArrayAdapter<String> ad_spinner_blitz;
	ArrayAdapter<String> ad_spinner_blitz_korrektur;
	ArrayList<String> al_spinner_blende;
	ArrayList<String> al_spinner_filter_vf;
	ArrayList<String> al_spinner_objektiv;
	ArrayList<String> al_spinner_zeit;
	ArrayList<String> al_spinner_focus;
	ArrayList<String> al_spinner_filter;
	ArrayList<String> al_spinner_makro;
	ArrayList<String> al_spinner_messmethode;
	ArrayList<String> al_spinner_belichtungs_korrektur;
	ArrayList<String> al_spinner_makro_vf;
	ArrayList<String> al_spinner_blitz;
	ArrayList<String> al_spinner_blitz_korrektur;
	HashMap<String, Integer> blende;
	HashMap<String, Integer> filtervf;
	HashMap<String, Integer> objektiv;
	HashMap<String, Integer> zeit;
	HashMap<String, Integer> fokus;
	HashMap<String, Integer> filter;
	HashMap<String, Integer> makro;
	HashMap<String, Integer> mess;
	HashMap<String, Integer> belichtung;
	HashMap<String, Integer> makrovf;
	HashMap<String, Integer> blitz;
	HashMap<String, Integer> blitzkorr;

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

	/*
	 * Sonstige Variablen
	 */
	ArrayList<Integer> idslist;
	Context mContext;
	ArrayList<Pictures> listItems;
	boolean minimizes;
	String filmID;
	String filmTitle;
	ArrayList<Integer> listItemsID;
	ArrayAdapter<Pictures> adapter;
	SharedPreferences settings;

	/*
	 * Datenbank und Spinnerf�ll Methoden
	 */

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
						+ " (title varchar(100), value integer primary key,camera varchar(100), datum varchar(100), bilder integer, pic varchar(999))"
						+ ";");
	}

	/*
	 * Es Es werden ArrayListen mit allen Eintr�gen die "gechecked" sind
	 * erstellt, diese werden dann den "Spinnern" zugeordnet um diese mit den
	 * richtigen Daten zu f�llen! Die HashMaps dienen f�r den Standart wert. Sie
	 * beinhalten sp�ter den Index des "Standart-Werts" und dieser kann dann
	 * einfach dem Spinner �bergeben werden um die richtige Vorauswahl zu
	 * treffen.
	 */
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

		int index = 0;
		Cursor camBWCursor = myDB.rawQuery("SELECT name FROM "
				+ MY_DB_TABLE_SETBW + " ", null);
		if (camBWCursor != null) {
			if (camBWCursor.moveToFirst()) {
				do {
					Log.v("Check", "Objektiv ist nicht leer");
					al_spinner_objektiv.add(camBWCursor.getString(camBWCursor
							.getColumnIndex("name")));
					objektiv.put(camBWCursor.getString(camBWCursor
							.getColumnIndex("name")), index);
					index++;
				} while (camBWCursor.moveToNext());
			}
		}
		camBWCursor.close();
		index = 0;

		Cursor c_blende = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETBLE + "  ", null);
		if (c_blende != null) {
			if (c_blende.moveToFirst()) {
				do {
					blende.put(
							c_blende.getString(c_blende.getColumnIndex("name")),
							index);
					index++;
					al_spinner_blende.add(c_blende.getString(c_blende
							.getColumnIndex("name")));
				} while (c_blende.moveToNext());
			}
		}
		c_blende.close();
		index = 0;

		Cursor c_zeit = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETZEI + "  ", null);
		if (c_zeit != null) {
			if (c_zeit.moveToFirst()) {
				do {
					zeit.put(c_zeit.getString(c_zeit.getColumnIndex("name")),
							index);
					index++;
					Log.v("Check",
							"Zeit ist :  "
									+ c_zeit.getString(c_zeit
											.getColumnIndex("name")));
					al_spinner_zeit.add(c_zeit.getString(c_zeit
							.getColumnIndex("name")));
				} while (c_zeit.moveToNext());
			}
		}
		c_zeit.close();
		index = 0;

		Cursor c_focus = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETFOK + "  ", null);
		if (c_focus != null) {
			if (c_focus.moveToFirst()) {
				do {
					fokus.put(
							c_focus.getString(c_focus.getColumnIndex("name")),
							index);
					index++;
					al_spinner_focus.add(c_focus.getString(c_focus
							.getColumnIndex("name")));
				} while (c_focus.moveToNext());
			}
		}
		c_focus.close();
		index = 0;

		Cursor c_filter = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETFIL + "  ", null);
		if (c_filter != null) {
			if (c_filter.moveToFirst()) {
				do {
					filter.put(
							c_filter.getString(c_filter.getColumnIndex("name")),
							index);
					index++;
					al_spinner_filter.add(c_filter.getString(c_filter
							.getColumnIndex("name")));
				} while (c_filter.moveToNext());
			}
		}
		c_filter.close();
		index = 0;

		Cursor c_makro = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETNM + "  ", null);
		if (c_makro != null) {
			if (c_makro.moveToFirst()) {
				do {
					makro.put(
							c_makro.getString(c_makro.getColumnIndex("name")),
							index);
					index++;
					Log.v("Check", "Makro ist nicht leer");
					al_spinner_makro.add(c_makro.getString(c_makro
							.getColumnIndex("name")));
				} while (c_makro.moveToNext());
			}
		} else {
			Log.v("Check", "NULL :(");
		}
		c_makro.close();
		index = 0;

		Cursor c_messmethode = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETMES + "  ", null);
		if (c_messmethode != null) {
			if (c_messmethode.moveToFirst()) {
				do {
					mess.put(c_messmethode.getString(c_messmethode
							.getColumnIndex("name")), index);
					index++;
					al_spinner_messmethode.add(c_messmethode
							.getString(c_messmethode.getColumnIndex("name")));
				} while (c_messmethode.moveToNext());
			}
		}
		c_messmethode.close();
		index = 0;

		Cursor c_belichtungs_korrektur = myDB.rawQuery(
				"SELECT name,value FROM " + MY_DB_TABLE_SETPLU + "  ", null);
		if (c_belichtungs_korrektur != null) {
			if (c_belichtungs_korrektur.moveToFirst()) {
				do {
					belichtung.put(c_belichtungs_korrektur
							.getString(c_belichtungs_korrektur
									.getColumnIndex("name")), index);
					index++;
					al_spinner_belichtungs_korrektur
							.add(c_belichtungs_korrektur
									.getString(c_belichtungs_korrektur
											.getColumnIndex("name")));
				} while (c_belichtungs_korrektur.moveToNext());
			}
		}
		c_belichtungs_korrektur.close();
		index = 0;

		if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Faktor (*)")) {
			Cursor change = myDB.rawQuery("SELECT name,value FROM "
					+ MY_DB_TABLE_SETFVF + "  ", null);
			if (change != null) {
				if (change.moveToFirst()) {
					do {
						filtervf.put(
								change.getString(change.getColumnIndex("name")),
								index);
						index++;
						al_spinner_filter_vf.add(change.getString(change
								.getColumnIndex("name")));
					} while (change.moveToNext());
				}
			}
			index = 0;
			Cursor changes = myDB.rawQuery("SELECT name,value FROM "
					+ MY_DB_TABLE_SETMVF + "  ", null);
			if (changes != null) {
				if (changes.moveToFirst()) {
					do {
						makrovf.put(changes.getString(changes
								.getColumnIndex("name")), index);
						index++;
						al_spinner_makro_vf.add(changes.getString(changes
								.getColumnIndex("name")));
					} while (changes.moveToNext());
				}
			}
			change.close();
			changes.close();
			index = 0;
		} else if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Blendenzugaben (+)")) {
			Cursor change2 = myDB.rawQuery("SELECT name,value FROM "
					+ MY_DB_TABLE_SETFVF2 + "  ", null);
			if (change2 != null) {
				if (change2.moveToFirst()) {
					do {
						filtervf.put(change2.getString(change2
								.getColumnIndex("name")), index);
						index++;
						al_spinner_filter_vf.add(change2.getString(change2
								.getColumnIndex("name")));
					} while (change2.moveToNext());
				}
			}
			index = 0;
			Cursor changes2 = myDB.rawQuery("SELECT name,value FROM "
					+ MY_DB_TABLE_SETMVF2 + "  ", null);
			if (changes2 != null) {
				if (changes2.moveToFirst()) {
					do {
						makrovf.put(changes2.getString(changes2
								.getColumnIndex("name")), index);
						index++;
						al_spinner_makro_vf.add(changes2.getString(changes2
								.getColumnIndex("name")));
					} while (changes2.moveToNext());
				}
			}
			change2.close();
			changes2.close();
			index = 0;
		}

		Cursor c_blitz = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETBLI + "  ", null);
		if (c_blitz != null) {
			if (c_blitz.moveToFirst()) {
				do {
					blitz.put(
							c_blitz.getString(c_blitz.getColumnIndex("name")),
							index);
					index++;
					al_spinner_blitz.add(c_blitz.getString(c_blitz
							.getColumnIndex("name")));
				} while (c_blitz.moveToNext());
			}
		}
		c_blitz.close();
		index = 0;

		Cursor c_blitz_korrektur = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETKOR + "  ", null);
		if (c_blitz_korrektur != null) {
			if (c_blitz_korrektur.moveToFirst()) {
				do {
					blitzkorr.put(c_blitz_korrektur.getString(c_blitz_korrektur
							.getColumnIndex("name")), index);
					index++;
					al_spinner_blitz_korrektur
							.add(c_blitz_korrektur.getString(c_blitz_korrektur
									.getColumnIndex("name")));
				} while (c_blitz_korrektur.moveToNext());
			}
		}
		c_blitz_korrektur.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle) Lifecycle-Methoden
	 */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.filmselect);
		Bundle extras = getIntent().getExtras();
		filmID = extras.getString("ID");
		filmTitle = "";
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		minimizes = settings.getBoolean("minimize", false);
		MY_DB_NAME = settings.getString("SettingsTable", "Foto");
		infoBlock1 = (LinearLayout) findViewById(R.id.infoblock1);
		infoBlock2 = (LinearLayout) findViewById(R.id.infoblock2);
		Button goon = (Button) findViewById(R.id.button_goon);
		goon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = settings.edit();
				onCreateDBAndDBNumber();
				Cursor c = myDBNummer.rawQuery(
						"SELECT title,camera,datum,bilder FROM "
								+ MY_DB_TABLE_NUMMER + " WHERE title = '"
								+ filmTitle + "'", null);
				if (c != null) {
					if (c.moveToFirst()) {
						do {
							editor.putString("Title",
									c.getString(c.getColumnIndex("title")));
							editor.putString("Datum",
									c.getString(c.getColumnIndex("datum")));
							editor.putString("Kamera",
									c.getString(c.getColumnIndex("camera")));
							editor.putInt("BildNummern",
									c.getInt(c.getColumnIndex("bilder")));
						} while (c.moveToNext());
					}
				}
				myDBNummer.close();
				c.close();
				stopManagingCursor(c);
				onCreateDBAndDBTabledFilm();
				Cursor c1 = myDBFilm
						.rawQuery(
								"SELECT _id,filmtitle,picuhrzeit,picnummer, picobjektiv, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
										+ MY_DB_FILM_TABLE
										+ " WHERE filmtitle = '"
										+ filmTitle
										+ "'", null);
				if (c1 != null) {
					int i = 0;
					if (c1.moveToFirst()) {
						do {
							editor.putString("Filmformat", c1.getString(c1
									.getColumnIndex("filmformat")));
							editor.putString("Empfindlichkeit", c1.getString(c1
									.getColumnIndex("filmempfindlichkeit")));
							editor.putString("Filmtyp",
									c1.getString(c1.getColumnIndex("filmtyp")));
							editor.putString("Sonder1", c1.getString(c1
									.getColumnIndex("filmsonder")));
							editor.putString("Sonder2", c1.getString(c1
									.getColumnIndex("filmsonders")));
							if (Integer.valueOf(c1
									.getString(c1.getColumnIndex("picnummer"))
									.toString().replaceAll("[\\D]", "")) > i) {
								editor.putInt(
										"BildNummerToBegin",
										Integer.valueOf(c1
												.getString(
														c1.getColumnIndex("picnummer"))
												.toString()
												.replaceAll("[\\D]", "")) + 1);
							}
						} while (c1.moveToNext());
					}
				}
				myDBFilm.close();
				c1.close();
				editor.putBoolean("EditMode", true);
				editor.commit();
				Intent myIntent = new Intent(getApplicationContext(),
						SlideNewPic.class);
				startActivityForResult(myIntent, 1);
			}
		});

		final ImageButton minimize = (ImageButton) findViewById(R.id.mini);

		if (minimizes) {
			minimize.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.expander_ic_minimized));
			infoBlock1.setVisibility(LinearLayout.GONE);
			infoBlock2.setVisibility(LinearLayout.GONE);
		} else {
			minimize.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.expander_ic_maximized));
			infoBlock1.setVisibility(LinearLayout.VISIBLE);
			infoBlock2.setVisibility(LinearLayout.VISIBLE);
		}
		Log.v("Check", "Minimiert ? : " + minimizes);
		minimize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("Check", "Minimiert (im OnClick) ? : " + minimizes);
				if (!minimizes) {
					infoBlock1.setVisibility(LinearLayout.GONE);
					infoBlock2.setVisibility(LinearLayout.GONE);
					minimize.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.expander_ic_minimized));
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("minimize", true);
					minimizes = true;
					editor.commit();
				} else {
					infoBlock1.setVisibility(LinearLayout.VISIBLE);
					infoBlock2.setVisibility(LinearLayout.VISIBLE);
					minimize.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.expander_ic_maximized));
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("minimize", false);
					minimizes = false;
					;
					editor.commit();
				}

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		onCreateDBAndDBNumber();
		Cursor c = myDBNummer.rawQuery(
				"SELECT datum, title,camera,bilder, pic FROM "
						+ MY_DB_TABLE_NUMMER + " WHERE title = '" + filmID
						+ "'", null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					filmtit = (TextView) findViewById(R.id.filmtitle);
					filmtit.setText(c.getString(c.getColumnIndex("title")));
					filmTitle = c.getString(c.getColumnIndex("title"));

					filmcam = (TextView) findViewById(R.id.filmcam);
					filmcam.setText(" "
							+ c.getString(c.getColumnIndex("camera")) + " ");

					TextView datum = (TextView) findViewById(R.id.datuminfo);
					datum.setText(" " + c.getString(c.getColumnIndex("datum"))
							+ " ");

					bilderimfilm = c.getInt(c.getColumnIndex("bilder"));
					ImageView vorschauImage = (ImageView) findViewById(R.id.vorschau);
					vorschauImage.setImageBitmap(BitmapFactory.decodeByteArray(
							Base64.decode(c.getString(c.getColumnIndex("pic")),
									Base64.DEFAULT), 0, (Base64.decode(
									c.getString(c.getColumnIndex("pic")),
									Base64.DEFAULT)).length));

				} while (c.moveToNext());
			}
		}
		c.close();
		myDBNummer.close();

		onCreateDBAndDBTabledFilm();
		Cursor c1 = myDBFilm
				.rawQuery(
						"SELECT _id,filmtitle,filmnotiz,picuhrzeit,picnummer, picobjektiv, picblende, piczeit, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
								+ MY_DB_FILM_TABLE
								+ " WHERE filmtitle = '"
								+ filmTitle + "'", null);
		if (c1 != null) {
			@SuppressWarnings("unused")
			int i = 1;
			listItems = new ArrayList<Pictures>();
			listItemsID = new ArrayList<Integer>();
			if (c1.moveToFirst()) {
				do {
					TextView filmnotiz = (TextView) findViewById(R.id.filmnotiz);
					filmnotiz.setText(" "
							+ c1.getString(c1.getColumnIndex("filmnotiz"))
							+ " ");

					TextView filmformat = (TextView) findViewById(R.id.filmformat);
					filmformat.setText(" "
							+ c1.getString(c1.getColumnIndex("filmformat"))
							+ " ");
					TextView filmtyp = (TextView) findViewById(R.id.filmtyp);
					filmtyp.setText(" "
							+ c1.getString(c1.getColumnIndex("filmtyp")) + " ");
					TextView filmemp = (TextView) findViewById(R.id.filmemp);
					filmemp.setText(" "
							+ c1.getString(c1
									.getColumnIndex("filmempfindlichkeit"))
							+ " ");
					TextView filmsonder = (TextView) findViewById(R.id.filmsonder);
					filmsonder.setText(" "
							+ c1.getString(c1.getColumnIndex("filmsonder"))
							+ " ");
					TextView filmsonders = (TextView) findViewById(R.id.filmsonders);
					filmsonders.setText(" "
							+ c1.getString(c1.getColumnIndex("filmsonders"))
							+ " ");

					listItems.add(new Pictures(c1.getString(c1
							.getColumnIndex("picnummer")), c1.getString(c1
							.getColumnIndex("picuhrzeit")), "Zeit: " + c1.getString(c1
							.getColumnIndex("piczeit")) + " - Blende: " + c1.getString(c1.getColumnIndex("picblende"))));
					i++;
					listItemsID.add(c1.getInt(c1.getColumnIndex("_id")));
					Log.v("Check", "LISTITEMS : " + listItems.size());
				} while (c1.moveToNext());
			}
		}
		myDBFilm.close();
		c1.close();

		adapter = new PicturesArrayAdapter(mContext, listItems, 1);
		ListView myList = (ListView) findViewById(android.R.id.list);
		myList.setOnItemClickListener(myClickListener);
		myList.setOnItemLongClickListener(myLongClickListener);
		myList.setAdapter(adapter);
	}

	/*
	 * Itemclick Methoden
	 */

	public OnItemLongClickListener myLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {

			LinearLayout first = (LinearLayout) arg1;
			LinearLayout second = (LinearLayout) first.getChildAt(0);
			final TextView third = (TextView) second.getChildAt(0);

			Display display = ((WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			LayoutInflater inflaterOwn = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layoutOwn = inflaterOwn.inflate(R.layout.longclick,
					(ViewGroup) findViewById(R.id.testen), false);
			Button deleteButton = (Button) layoutOwn
					.findViewById(R.id.deletebutton);
			Button cancelButton = (Button) layoutOwn
					.findViewById(R.id.cancelbutton);
			Button editButton = (Button) layoutOwn
					.findViewById(R.id.editbutton);
			deleteButton.setText("     Bild l\u00F6schen     ");
			editButton.setText("     Bild bearbeiten     ");

			editButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Context mContext2 = mContext;
					Display display = ((WindowManager) mContext2
							.getSystemService(Context.WINDOW_SERVICE))
							.getDefaultDisplay();
					int width = display.getWidth();
					int height = display.getHeight();
					LayoutInflater inflaterOwn = (LayoutInflater) mContext2
							.getSystemService(LAYOUT_INFLATER_SERVICE);
					View v1 = inflaterOwn.inflate(R.layout.filminfoboxedit,
							(ViewGroup) findViewById(R.id.testen), false);

					fuellen();

					onCreateDBAndDBTabledFilm();
					Cursor c = myDBFilm
							.rawQuery(
									"SELECT _id,picuhrzeit,filmdatum,picfokus,piclat,piclong,picobjektiv, picblende,piczeit,picmessung, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM "
											+ MY_DB_FILM_TABLE
											+ " WHERE picnummer = '"
											+ third.getText() + "'", null);
					if (c != null) {
						if (c.moveToFirst()) {
							do {
								final TextView zeitStempel = (TextView) v1
										.findViewById(R.id.zeitStempel);
								zeitStempel.setText(c.getString(c
										.getColumnIndex("picuhrzeit"))
										+ " - "
										+ c.getString(c
												.getColumnIndex("filmdatum")));

								final TextView zeitGeo = (TextView) v1
										.findViewById(R.id.geoTag);
								zeitGeo.setText("Lat : "
										+ c.getString(c
												.getColumnIndex("piclat"))
										+ " - Long : "
										+ c.getString(c
												.getColumnIndex("piclong")));

								final TextView objektiv = (TextView) v1
										.findViewById(R.id.showobjektiv);
								objektivedit = (Spinner) v1
										.findViewById(R.id.editobjektiv);
								objektiv.setVisibility(TextView.GONE);
								objektivedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_objektiv = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_objektiv);
								objektivedit.setAdapter(ad_spinner_objektiv);

								final TextView filtervf = (TextView) v1
										.findViewById(R.id.showfiltervf);
								filtervfedit = (Spinner) v1
										.findViewById(R.id.editfiltervf);
								filtervf.setVisibility(TextView.GONE);
								filtervfedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_filter_vf = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_filter_vf);
								filtervfedit.setAdapter(ad_spinner_filter_vf);

								final TextView picfocus = (TextView) v1
										.findViewById(R.id.showfokus);
								picfocusedit = (Spinner) v1
										.findViewById(R.id.editfokus);
								picfocus.setVisibility(TextView.GONE);
								picfocusedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_focus = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_focus);
								picfocusedit.setAdapter(ad_spinner_focus);

								final TextView picblende = (TextView) v1
										.findViewById(R.id.showblende);
								picblendeedit = (Spinner) v1
										.findViewById(R.id.editblende);
								picblende.setVisibility(TextView.GONE);
								picblendeedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_blende = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_blende);
								picblendeedit.setAdapter(ad_spinner_blende);

								final TextView piczeit = (TextView) v1
										.findViewById(R.id.showzeit);
								piczeitedit = (Spinner) v1
										.findViewById(R.id.editzeit);
								piczeit.setVisibility(TextView.GONE);
								piczeitedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_zeit = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_zeit);
								piczeitedit.setAdapter(ad_spinner_zeit);

								final TextView picmessung = (TextView) v1
										.findViewById(R.id.showmessung);
								picmessungedit = (Spinner) v1
										.findViewById(R.id.editmessung);
								picmessung.setVisibility(TextView.GONE);
								picmessungedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_messmethode = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_messmethode);
								picmessungedit
										.setAdapter(ad_spinner_messmethode);

								final TextView picplus = (TextView) v1
										.findViewById(R.id.showbelichtung);
								picplusedit = (Spinner) v1
										.findViewById(R.id.editbelichtung);
								picplus.setVisibility(TextView.GONE);
								picplusedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_belichtungs_korrektur = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_belichtungs_korrektur);
								picplusedit
										.setAdapter(ad_spinner_belichtungs_korrektur);

								final TextView picmakro = (TextView) v1
										.findViewById(R.id.showmakro);
								picmakroedit = (Spinner) v1
										.findViewById(R.id.editmakro);
								picmakro.setVisibility(TextView.GONE);
								picmakroedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_makro = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_makro);
								picmakroedit.setAdapter(ad_spinner_makro);

								final TextView picmakrovf = (TextView) v1
										.findViewById(R.id.showmakrovf);
								picmakrovfedit = (Spinner) v1
										.findViewById(R.id.editmakrovf);
								picmakrovf.setVisibility(TextView.GONE);
								picmakrovfedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_makro_vf = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_makro_vf);
								picmakrovfedit.setAdapter(ad_spinner_makro_vf);

								final TextView picfilter = (TextView) v1
										.findViewById(R.id.showfilter);
								picfilteredit = (Spinner) v1
										.findViewById(R.id.editfilter);
								picfilter.setVisibility(TextView.GONE);
								picfilteredit.setVisibility(Spinner.VISIBLE);
								ad_spinner_filter = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_filter);
								picfilteredit.setAdapter(ad_spinner_filter);

								final TextView picblitz = (TextView) v1
										.findViewById(R.id.showblitz);
								picblitzedit = (Spinner) v1
										.findViewById(R.id.editblitz);
								picblitz.setVisibility(TextView.GONE);
								picblitzedit.setVisibility(Spinner.VISIBLE);
								ad_spinner_blitz = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_blitz);
								picblitzedit.setAdapter(ad_spinner_blitz);

								final TextView picblitzkorr = (TextView) v1
										.findViewById(R.id.showblitzkorr);
								picblitzkorredit = (Spinner) v1
										.findViewById(R.id.editblitzkorr);
								picblitzkorr.setVisibility(TextView.GONE);
								picblitzkorredit.setVisibility(Spinner.VISIBLE);
								ad_spinner_blitz_korrektur = new ArrayAdapter<String>(
										mContext,
										android.R.layout.simple_spinner_item,
										al_spinner_blitz_korrektur);
								picblitzkorredit
										.setAdapter(ad_spinner_blitz_korrektur);

								final TextView picnotiz = (TextView) v1
										.findViewById(R.id.shownotiz);
								picnotizedit = (EditText) v1
										.findViewById(R.id.editnotiz);
								picnotiz.setVisibility(TextView.GONE);
								picnotizedit.setVisibility(Spinner.VISIBLE);

								final TextView picnotizcam = (TextView) v1
										.findViewById(R.id.shownotizkam);
								picnotizcamedit = (EditText) v1
										.findViewById(R.id.editnotizkam);
								picnotizcam.setVisibility(TextView.GONE);
								picnotizcamedit.setVisibility(Spinner.VISIBLE);

								final TextView picTitle = (TextView) v1
										.findViewById(R.id.pictitle);
								picTitle.setText(c.getString(c
										.getColumnIndex("picnummer")));

							} while (c.moveToNext());
						}
					}
					c.close();
					myDBFilm.close();

					// Spinner setten !!

					onCreateDBAndDBTabledFilm();
					Cursor c1 = myDBFilm
							.rawQuery(
									"SELECT _id,picfokus,picblende,piczeit,picmessung,picobjektiv, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM "
											+ MY_DB_FILM_TABLE
											+ " WHERE filmtitle = '"
											+ filmtit.getText().toString()
											+ "' AND picnummer = '"
											+ third.getText() + "'", null);
					if (c1 != null) {
						if (c1.moveToFirst()) {
							do {
								try {
									Log.v("Check",
											"null check "
													+ c1.getString(c1
															.getColumnIndex("pickorr")));
									picblendeedit.setSelection(blende.get(c1.getString(c1
											.getColumnIndex("picblende"))));
									filtervfedit.setSelection(filtervf.get(c1.getString(c1
											.getColumnIndex("picfiltervf"))));
									objektivedit.setSelection(objektiv.get(c1.getString(c1
											.getColumnIndex("picobjektiv"))));
									piczeitedit.setSelection(zeit.get(c1
											.getString(c1
													.getColumnIndex("piczeit"))));
									picfocusedit.setSelection(fokus.get(c1.getString(c1
											.getColumnIndex("picfokus"))));
									picfilteredit.setSelection(filter.get(c1.getString(c1
											.getColumnIndex("picfilter"))));
									picmakroedit.setSelection(makro.get(c1.getString(c1
											.getColumnIndex("picmakro"))));
									picmessungedit.setSelection(mess.get(c1.getString(c1
											.getColumnIndex("picmessung"))));
									picplusedit.setSelection(belichtung.get(c1
											.getString(c1
													.getColumnIndex("pickorr"))));
									picmakrovfedit.setSelection(makrovf.get(c1.getString(c1
											.getColumnIndex("picmakrovf"))));
									picblitzedit.setSelection(blitz.get(c1.getString(c1
											.getColumnIndex("picblitz"))));
									picblitzkorredit.setSelection(blitzkorr.get(c1.getString(c1
											.getColumnIndex("picblitzkorr"))));
									picnotizedit.setText(c1.getString(c1
											.getColumnIndex("picnotiz")));
									picnotizcamedit.setText(c1.getString(c1
											.getColumnIndex("pickameranotiz")));
								} catch (Exception e) {

								}
							} while (c1.moveToNext());
						} else {
							Log.v("Check", "Kein Bild vorhanden");
						}
					}
					c1.close();
					myDBFilm.close();

					final PopupWindow pwblub = new PopupWindow(v1,
							(int) (width), (int) (height), true);

					Button cancel = (Button) v1.findViewById(R.id.cancel);
					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pwblub.dismiss();
						}
					});

					Button save = (Button) v1.findViewById(R.id.save);
					save.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// update
							onCreateDBAndDBTabledFilm();
							myDBFilm.execSQL("UPDATE "
									+ MY_DB_FILM_TABLE
									+ " SET picfokus = '"
									+ picfocusedit.getSelectedItem().toString()
									+ "', picblende = '"
									+ picblendeedit.getSelectedItem()
											.toString()
									+ "', piczeit = '"
									+ piczeitedit.getSelectedItem().toString()
									+ "', picmessung = '"
									+ picmessungedit.getSelectedItem()
											.toString()
									+ "', pickorr = '"
									+ picplusedit.getSelectedItem().toString()
									+ "', picmakro = '"
									+ picmakroedit.getSelectedItem().toString()
									+ "', picmakrovf = '"
									+ picmakrovfedit.getSelectedItem()
											.toString()
									+ "', picfilter = '"
									+ picfilteredit.getSelectedItem()
											.toString()
									+ "', picfiltervf = '"
									+ filtervfedit.getSelectedItem().toString()
									+ "', picblitz = '"
									+ picblitzedit.getSelectedItem().toString()
									+ "', picblitzkorr = '"
									+ picblitzkorredit.getSelectedItem()
											.toString() + "', picnotiz = '"
									+ picnotizedit.getText().toString()
									+ "', pickameranotiz = '"
									+ picnotizcamedit.getText().toString()
									+ "', picobjektiv = '"
									+ objektivedit.getSelectedItem().toString()
									+ "' WHERE filmtitle = '"
									+ filmtit.getText().toString()
									+ "' AND picnummer = '"
									+ third.getText().toString() + "';");
							myDBFilm.close();
							pwblub.dismiss();
							onResume();
						}
					});

					pwblub.setAnimationStyle(7);
					// pw.setBackgroundDrawable(new BitmapDrawable());
					pwblub.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.infobg));
					pwblub.showAtLocation(v1, Gravity.CENTER, 0, 0);
					pw.dismiss();

				}
			});

			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage(
							"Wollen Sie den Eintrag wirklich l\u00F6schen ? ?")
							.setCancelable(false)
							.setPositiveButton("Ja",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											onCreateDBAndDBTabledFilm();
											myDBFilm.execSQL("DELETE FROM "
													+ MY_DB_FILM_TABLE
													+ " WHERE picnummer = '"
													+ third.getText()
															.toString() + "'");
											myDBFilm.close();
											onCreateDBAndDBNumber();
											ContentValues dataToInsert = new ContentValues();
											dataToInsert.put("bilder",
													bilderimfilm - 1);
											bilderimfilm -= 1;
											myDBNummer.update(
													MY_DB_TABLE_NUMMER,
													dataToInsert, "title=?",
													new String[] { filmtit
															.getText()
															.toString() });

											myDBNummer.close();
											pw.dismiss();
											onResume();
											Toast.makeText(
													getApplicationContext(),
													"Bild gel\u00F6scht",
													Toast.LENGTH_SHORT).show();
										}
									})
							.setNegativeButton("Nein",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											pw.dismiss();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();

				}
			});

			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pw.dismiss();

				}
			});

			int width = display.getWidth();
			int height = display.getHeight();
			pw = new PopupWindow(layoutOwn, (int) (width / 1.6),
					(int) (height / 2.5), true);
			pw.setAnimationStyle(7);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);

			return true;
		}
	};

	public OnItemClickListener myClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			final Context mContext2 = mContext;
			Display display = ((WindowManager) mContext2
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();
			LayoutInflater inflaterOwn = (LayoutInflater) mContext2
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layoutOwn = inflaterOwn.inflate(R.layout.slidi,
					(ViewGroup) findViewById(R.id.testen), false);
			ViewPager viewPager = (ViewPager) layoutOwn
					.findViewById(R.id.viewPager);
			MyPagerAdapter adapter = new MyPagerAdapter(mContext2);
			viewPager.setAdapter(adapter);
			viewPager.getAdapter().setPrimaryItem(viewPager, 2, null);

			mIndicator = (TitlePageIndicator) layoutOwn
					.findViewById(R.id.indicator);
			mIndicator.setViewPager(viewPager);
			mIndicator.setFooterColor(0xFF000000);

			pw = new PopupWindow(layoutOwn, (int) (width), (int) (height), true);
			pw.setAnimationStyle(7);
			// pw.setBackgroundDrawable(new BitmapDrawable());
			pw.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.infobg));
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
			viewPager.setCurrentItem(arg2);

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) Men�
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

	/*
	 * Pageadapter f�r das hin- und herwischen zwischen den Bildenr. W�hlt man
	 * ein Bild aus, wird ein "Popup" ge�ffnet in der alle Informationen zu dem
	 * Bild vorhanden sind in dieser Ansicht l�sst sich dann auch zwischen den
	 * Bildern hin- und herwechseln. Es wird einfach eine ArrayList<Views>
	 * gef�llt. Quasi fertige Views in eine Liste, die beim Wischen
	 * durchgegangen wird.
	 */

	private class MyPagerAdapter extends PagerAdapter implements TitleProvider {

		private ArrayList<View> views;

		@SuppressWarnings("unused")
		public MyPagerAdapter(Context context) {
			views = new ArrayList<View>();
			LayoutInflater inflater = getLayoutInflater();
			idslist = new ArrayList<Integer>();

			onCreateDBAndDBTabledFilm();
			Cursor c = myDBFilm
					.rawQuery(
							"SELECT _id,picfokus,picuhrzeit,piclat,piclong,filmdatum,picobjektiv, picblende,piczeit,picmessung, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM "
									+ MY_DB_FILM_TABLE
									+ " WHERE filmtitle = '"
									+ filmTitle + "'", null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						idslist.add(c.getInt(c.getColumnIndex("_id")));
						View v = inflater.inflate(R.layout.filminfobox, null,
								false);

						final TextView zeitStempel = (TextView) v
								.findViewById(R.id.zeitStempel);
						zeitStempel.setText(c.getString(c
								.getColumnIndex("picuhrzeit"))
								+ " - "
								+ c.getString(c.getColumnIndex("filmdatum")));
						final TextView zeitGeo = (TextView) v
								.findViewById(R.id.geoTag);
						zeitGeo.setText("Lat : "
								+ c.getString(c.getColumnIndex("piclat"))
								+ " - Long : "
								+ c.getString(c.getColumnIndex("piclong")));

						final TextView objektiv = (TextView) v
								.findViewById(R.id.showobjektiv);
						final Spinner objektivedit = (Spinner) v
								.findViewById(R.id.editobjektiv);
						objektiv.setText(c.getString(c
								.getColumnIndex("picobjektiv")) + " ");

						final TextView filtervf = (TextView) v
								.findViewById(R.id.showfiltervf);
						final Spinner filtervfedit = (Spinner) v
								.findViewById(R.id.editfiltervf);
						filtervf.setText(c.getString(c
								.getColumnIndex("picfiltervf")) + " ");

						final TextView picfocus = (TextView) v
								.findViewById(R.id.showfokus);
						final Spinner picfocusedit = (Spinner) v
								.findViewById(R.id.editfokus);
						picfocus.setText(c.getString(c
								.getColumnIndex("picfokus")) + " ");

						final TextView picblende = (TextView) v
								.findViewById(R.id.showblende);
						final Spinner picblendeedit = (Spinner) v
								.findViewById(R.id.editblende);
						picblende.setText(c.getString(c
								.getColumnIndex("picblende")) + " ");

						final TextView piczeit = (TextView) v
								.findViewById(R.id.showzeit);
						final Spinner piczeitedit = (Spinner) v
								.findViewById(R.id.editzeit);
						piczeit.setText(c.getString(c.getColumnIndex("piczeit"))
								+ " ");

						final TextView picmessung = (TextView) v
								.findViewById(R.id.showmessung);
						final Spinner picmessungedit = (Spinner) v
								.findViewById(R.id.editmessung);
						picmessung.setText(c.getString(c
								.getColumnIndex("picmessung")) + " ");

						final TextView picplus = (TextView) v
								.findViewById(R.id.showbelichtung);
						final Spinner picplusedit = (Spinner) v
								.findViewById(R.id.editbelichtung);
						picplus.setText(c.getString(c.getColumnIndex("pickorr"))
								+ " ");

						final TextView picmakro = (TextView) v
								.findViewById(R.id.showmakro);
						final Spinner picmakroedit = (Spinner) v
								.findViewById(R.id.editmakro);
						picmakro.setText(c.getString(c
								.getColumnIndex("picmakro")) + " ");

						final TextView picmakrovf = (TextView) v
								.findViewById(R.id.showmakrovf);
						final Spinner picmakrovfedit = (Spinner) v
								.findViewById(R.id.editmakrovf);
						picmakrovf.setText(c.getString(c
								.getColumnIndex("picmakrovf")) + " ");

						final TextView picfilter = (TextView) v
								.findViewById(R.id.showfilter);
						final Spinner picfilteredit = (Spinner) v
								.findViewById(R.id.editfilter);
						picfilter.setText(c.getString(c
								.getColumnIndex("picfilter")) + " ");

						final TextView picblitz = (TextView) v
								.findViewById(R.id.showblitz);
						final Spinner picblitzedit = (Spinner) v
								.findViewById(R.id.editblitz);
						picblitz.setText(c.getString(c
								.getColumnIndex("picblitz")) + " ");

						final TextView picblitzkorr = (TextView) v
								.findViewById(R.id.showblitzkorr);
						final Spinner picblitzkorredit = (Spinner) v
								.findViewById(R.id.editblitzkorr);
						picblitzkorr.setText(c.getString(c
								.getColumnIndex("picblitzkorr")) + " ");

						final TextView picnotiz = (TextView) v
								.findViewById(R.id.shownotiz);
						final EditText picnotizedit = (EditText) v
								.findViewById(R.id.editnotiz);
						picnotiz.setText(c.getString(c
								.getColumnIndex("picnotiz")) + " ");

						final TextView picnotizcam = (TextView) v
								.findViewById(R.id.shownotizkam);
						final EditText picnotizcamedit = (EditText) v
								.findViewById(R.id.editnotizkam);
						picnotizcam.setText(c.getString(c
								.getColumnIndex("pickameranotiz")) + " ");

						final TextView picTitle = (TextView) v
								.findViewById(R.id.pictitle);
						picTitle.setText(c.getString(c
								.getColumnIndex("picnummer")));

						views.add(v);

					} while (c.moveToNext());
				}
			}
			c.close();
			myDBFilm.close();

		}

		@Override
		public void destroyItem(View view, int arg1, Object object) { // Es
																		// werden
																		// immer
																		// nur
																		// die 2
																		// n�chsten
																		// und 2
																		// letzen
																		// Views
																		// "gespeichert"
																		// bzw.
																		// berechnet,
																		// der
																		// Rest
																		// wird
																		// erstmal
																		// gel�scht
			((ViewPager) view).removeView((LinearLayout) object);
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size(); // Wieviele Views zum Wischen
		}

		@Override
		public Object instantiateItem(View view, int position) { // Das
																	// Vorpuffern,
																	// wenn die
																	// View bald
																	// drankommt...
																	// s.o.
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
		public String getTitle(int position) { // Kommt vom TitleProvider um den
												// Titel einer View festzulegen
			if (position == 0) {
				return " >";
			} else if (position == (views.size() - 1)) {
				return "< ";
			}
			return "<  >";
		}

	}

	/*
	 * Hilfsklasse f�r Bildobjekte (Nur f�r die Custom Zelle der Liste)
	 */

	private static class Pictures {
		private String name = "";
		private String time = "";
		private String objektiv = "";

		public Pictures(String name, String time, String objektiv) {
			this.name = name;
			this.time = time;
			this.objektiv = objektiv;
		}

		public String getName() {
			return name;
		}

		public String getTime() {
			return time;
		}

		public String getObjektiv() {
			return this.objektiv;
		}
	}

	/*
	 * Viewholder f�r Picture Elemente
	 */

	private static class PicturesViewHolder {
		private TextView textViewTime;
		private TextView textViewName;
		private TextView textViewObjektiv;

		public PicturesViewHolder(TextView textViewname, TextView textViewtime,
				TextView textViewobjektiv) {
			this.textViewTime = textViewtime;
			this.textViewObjektiv = textViewobjektiv;
			this.textViewName = textViewname;
		}

		public TextView getTextViewName() {
			return textViewName;
		}

		public TextView getTextViewTime() {
			return textViewTime;
		}

		public TextView getTextViewObjektiv() {
			return this.textViewObjektiv;
		}
	}

	/*
	 * Custom Array Adapter f�r custom List-Zeilen
	 */

	private class PicturesArrayAdapter extends ArrayAdapter<Pictures> {

		private LayoutInflater inflater;
		@SuppressWarnings("unused")
		int nummer = 0;

		public PicturesArrayAdapter(Context context,
				ArrayList<Pictures> planetList, int number) {
			super(context, R.layout.film_item, R.id.listItemText, planetList);
			nummer = number;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Pictures planet = (Pictures) this.getItem(position);
			TextView textViewObj;
			TextView textView;
			TextView textViewTime;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.film_item, null);
				textView = (TextView) convertView
						.findViewById(R.id.listItemText);
				textViewTime = (TextView) convertView
						.findViewById(R.id.listItemTextTime);
				textViewObj = (TextView) convertView
						.findViewById(R.id.listItemObjektiv);
				convertView.setTag(new PicturesViewHolder(textView,
						textViewTime, textViewObj));
			} else {
				PicturesViewHolder viewHolder = (PicturesViewHolder) convertView
						.getTag();
				textViewTime = viewHolder.getTextViewTime();
				textView = viewHolder.getTextViewName();
				textViewObj = viewHolder.getTextViewObjektiv();
			}
			textViewTime.setText(planet.getTime());
			textView.setText(planet.getName());
			textViewObj.setText(planet.getObjektiv());

			return convertView;
		}
	}

}