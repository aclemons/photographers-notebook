package unisiegen.photographers.activity;

import java.util.ArrayList;
import java.util.HashMap;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SlideNewSettingsSon extends Activity {

	/*
	 * Sonstige Variablen
	 */
	private static final String[] CONTENT = new String[] { "Blitz",
			"Blitz-Korr", "Sonderentwicklung" };

	SharedPreferences settings;
	PopupWindow pw;
	int setButtonClicked = 1;
	int i = 0;
	Context mContext;
	ViewPager viewPager;
	TitlePageIndicator mIndicator;

	/*
	 * User-Interface Variablen
	 */
	TableLayout tablor7, tablor6, tablor1, tablorspec, tablor, tablor3;
	TextView freecell7, freecell6, freecell1, freecellspec, freecell,
			freecell3;
	CheckBox checki7, checki6, checki1, checkispec, checki, checki3;
	Button addKate7, addKate6, addKate1, addKatespec, addKate, addKate3;
	EditText Kat7, Kat6, Kat1, Katspec, Kat3, Kat, katText0, katText1,
			katText2, katText3, katText4, katText5, katText6, katText7,
			katText8, katText9, katText10;
	ListView myList7, myList6, myList3, myList1, myListspec, myList,
			myListView0, myListView1, myListView2, myListView3, myListView4,
			myListView5, myListView6, myListView7, myListView8, myListView9,
			myListView10;
	View slideView0, slideView1, slideView2, slideView3, slideView4,
			slideView5, slideView6, slideView7, slideView8, slideView9,
			slideView10;

	/*
	 * Hash-Map f�r Default Werte der Spinner
	 */
	HashMap<String, Integer> defCheck0, defCheck1, defCheck2, defCheck3,
			defCheck4, defCheck5, defCheck6, defCheck7, defCheck8, defCheck9,
			defCheck10, defCheck11, defCheck12, defCheck13, defCheck14,
			defCheck15;

	/*
	 * Custom ArrayAdapter und ArrayList f�r ListView
	 */
	ArrayList<Settings> aplanets7, aplanets6, planet1, planet2, planet3,
			planet4, planet5, planet6, planet7, planet0, planets3, planets4,
			planets5, aplanetsspec, aplanets, aplanets1, aplanets2;
	ArrayAdapter<Settings> listAdapte10, listAdapte9, listAdapter7,
			listAdapter6, listAdapter1, listAdapter, listAdapterspec,
			listAdapte1, listAdapte2, listAdapte3, listAdapte4, listAdapte5,
			listAdapte6, listAdapte7, listAdapte8, listAdapte0;

	/*
	 * Datenbank Variablen
	 */

	SQLiteDatabase myDBNummer = null;
	static String MY_DB_NUMMER = "Nummern";
	final static String MY_DB_TABLE_NUMMER = "Nummer";

	SQLiteDatabase myDBFilm = null;
	static String MY_DB_FILM = "Filme";
	final static String MY_DB_FILM_TABLE = "Film";

	SQLiteDatabase myDB = null;
	SQLiteDatabase myDBSet = null;
	static String MY_DB_NAME;

	final static String MY_DB_SET = "Foto";
	final static String MY_DB_SET1 = "FotoSettingsOne";
	final static String MY_DB_SET2 = "FotoSettingsTwo";
	final static String MY_DB_SET3 = "FotoSettingsThree";

	final static String MY_DB_TABLE_SETCAMBW = "SettingsCameraBW";
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
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle) LifeCycle Methoden
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidenewsettings);
		mContext = this;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		MY_DB_NAME = settings.getString("SettingsTable", "Foto");
		readDB();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		MyPagerAdapter adapter = new MyPagerAdapter(this);
		viewPager.setAdapter(adapter);
		mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		mIndicator.setViewPager(viewPager);
	}

	/*
	 * Datenbank Methoden
	 */

	private void onCreateDBAndDBTabledFilm() {
		myDBFilm = mContext.openOrCreateDatabase(MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		myDBFilm.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_FILM_TABLE
				+ " (_id integer primary key autoincrement, filmdatum varchar(100), picuhrzeit varchar(100), filmtitle varchar(100), filmcamera varchar(100), filmformat varchar(100), filmempfindlichkeit varchar(100), filmtyp varchar(100), filmsonder varchar(100), filmsonders varchar(100), picfokus varchar(100), picblende varchar(100), piczeit varchar(100), picmessung varchar(100), pickorr varchar(100), picmakro varchar(100), picmakrovf varchar(100), picfilter varchar(100), picfiltervf varchar(100), picblitz varchar(100), picblitzkorr varchar(100), picnotiz varchar(100), pickameranotiz varchar(100), picobjektiv varchar(100), picnummer varchar(100))"
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

	private void writeDB(String TableName, String Name, int Value) {
		onCreateDBAndDBTabled();
		myDB.execSQL("INSERT INTO " + TableName + " Values (" + null + ",'"
				+ "" + Name + "" + "','" + Value + "','" + 0 + "');");
		myDB.close();
		Log.v("Check", "writeDB()");
	}

	private void readDB() {
		onCreateDBAndDBTabled();
		aplanets = new ArrayList<Settings>();
		aplanetsspec = new ArrayList<Settings>();
		aplanets1 = new ArrayList<Settings>();
		aplanets2 = new ArrayList<Settings>();
		aplanets6 = new ArrayList<Settings>();
		aplanets7 = new ArrayList<Settings>();
		defCheck0 = new HashMap<String, Integer>();
		defCheck1 = new HashMap<String, Integer>();
		defCheck2 = new HashMap<String, Integer>();
		defCheck3 = new HashMap<String, Integer>();
		defCheck4 = new HashMap<String, Integer>();
		defCheck5 = new HashMap<String, Integer>();
		defCheck6 = new HashMap<String, Integer>();
		defCheck7 = new HashMap<String, Integer>();
		defCheck8 = new HashMap<String, Integer>();
		defCheck9 = new HashMap<String, Integer>();
		defCheck10 = new HashMap<String, Integer>();
		defCheck11 = new HashMap<String, Integer>();
		defCheck12 = new HashMap<String, Integer>();
		defCheck13 = new HashMap<String, Integer>();
		defCheck14 = new HashMap<String, Integer>();
		defCheck15 = new HashMap<String, Integer>();

		planets3 = new ArrayList<Settings>();
		planets4 = new ArrayList<Settings>();
		planets5 = new ArrayList<Settings>();

		planet0 = new ArrayList<Settings>();
		planet1 = new ArrayList<Settings>();
		planet2 = new ArrayList<Settings>();
		planet3 = new ArrayList<Settings>();
		planet4 = new ArrayList<Settings>();
		planet5 = new ArrayList<Settings>();
		planet6 = new ArrayList<Settings>();
		planet7 = new ArrayList<Settings>();
		Log.v("Check", "readDB()");
		/*
		 * Cursor cb = myDB.rawQuery("SELECT name,value FROM " +
		 * MY_DB_TABLE_SETBW, null); if (cb != null ) { if (cb.moveToFirst()) {
		 * do { planets2.add(new
		 * Settings(cb.getString(cb.getColumnIndex("name")),
		 * cb.getInt(cb.getColumnIndex("value")))); } while (cb.moveToNext()); }
		 * }
		 */
		Cursor cc = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETNM, null);
		if (cc != null) {
			if (cc.moveToFirst()) {
				do {
					defCheck0.put(cc.getString(cc.getColumnIndex("name")),
							cc.getInt(cc.getColumnIndex("def")));

					planets3.add(new Settings(cc.getString(cc
							.getColumnIndex("name")), cc.getInt(cc
							.getColumnIndex("value"))));
				} while (cc.moveToNext());
			}
		}

		Cursor cd = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETFIL, null);
		if (cd != null) {
			if (cd.moveToFirst()) {
				do {
					defCheck1.put(cd.getString(cd.getColumnIndex("name")),
							cd.getInt(cd.getColumnIndex("def")));
					planets4.add(new Settings(cd.getString(cd
							.getColumnIndex("name")), cd.getInt(cd
							.getColumnIndex("value"))));
				} while (cd.moveToNext());
			}
		}

		Cursor ce = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETBLI, null);
		if (ce != null) {
			if (ce.moveToFirst()) {
				do {
					defCheck2.put(ce.getString(ce.getColumnIndex("name")),
							ce.getInt(ce.getColumnIndex("def")));
					planets5.add(new Settings(ce.getString(ce
							.getColumnIndex("name")), ce.getInt(ce
							.getColumnIndex("value"))));
				} while (ce.moveToNext());
			}
		}

		Cursor ce1 = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETFOK, null);
		if (ce1 != null) {
			if (ce1.moveToFirst()) {
				do {
					defCheck3.put(ce1.getString(ce1.getColumnIndex("name")),
							ce1.getInt(ce1.getColumnIndex("def")));
					planet0.add(new Settings(ce1.getString(ce1
							.getColumnIndex("name")), ce1.getInt(ce1
							.getColumnIndex("value"))));
				} while (ce1.moveToNext());
			}
		}
		Cursor ce2 = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETBLE, null);
		if (ce2 != null) {
			if (ce2.moveToFirst()) {
				do {
					defCheck4.put(ce2.getString(ce2.getColumnIndex("name")),
							ce2.getInt(ce2.getColumnIndex("def")));
					planet1.add(new Settings(ce2.getString(ce2
							.getColumnIndex("name")), ce2.getInt(ce2
							.getColumnIndex("value"))));
				} while (ce2.moveToNext());
			}
		}
		Cursor ce3 = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETZEI, null);
		if (ce3 != null) {
			if (ce3.moveToFirst()) {
				do {
					defCheck5.put(ce3.getString(ce3.getColumnIndex("name")),
							ce3.getInt(ce3.getColumnIndex("def")));
					planet2.add(new Settings(ce3.getString(ce3
							.getColumnIndex("name")), ce3.getInt(ce3
							.getColumnIndex("value"))));
				} while (ce3.moveToNext());
			}
		}
		Cursor ce4 = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETMES, null);
		if (ce4 != null) {
			if (ce4.moveToFirst()) {
				do {
					defCheck6.put(ce4.getString(ce4.getColumnIndex("name")),
							ce4.getInt(ce4.getColumnIndex("def")));
					planet3.add(new Settings(ce4.getString(ce4
							.getColumnIndex("name")), ce4.getInt(ce4
							.getColumnIndex("value"))));
				} while (ce4.moveToNext());
			}
		}
		Cursor ce5 = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETPLU, null);
		if (ce5 != null) {
			if (ce5.moveToFirst()) {
				do {
					defCheck7.put(ce5.getString(ce5.getColumnIndex("name")),
							ce5.getInt(ce5.getColumnIndex("def")));
					planet4.add(new Settings(ce5.getString(ce5
							.getColumnIndex("name")), ce5.getInt(ce5
							.getColumnIndex("value"))));
				} while (ce5.moveToNext());
			}
		}
		// BLUB
		if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Faktor (*)")) {
			Log.v("Check", "TRUE");
			Cursor ce6 = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETMVF, null);
			if (ce6 != null) {
				if (ce6.moveToFirst()) {
					do {
						defCheck8.put(
								ce6.getString(ce6.getColumnIndex("name")),
								ce6.getInt(ce6.getColumnIndex("def")));
						planet5.add(new Settings(ce6.getString(ce6
								.getColumnIndex("name")), ce6.getInt(ce6
								.getColumnIndex("value"))));
					} while (ce6.moveToNext());
				}
			}
			Cursor ce7 = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETFVF, null);
			if (ce7 != null) {
				if (ce7.moveToFirst()) {
					do {
						defCheck9.put(
								ce7.getString(ce7.getColumnIndex("name")),
								ce7.getInt(ce7.getColumnIndex("def")));
						planet6.add(new Settings(ce7.getString(ce7
								.getColumnIndex("name")), ce7.getInt(ce7
								.getColumnIndex("value"))));
					} while (ce7.moveToNext());
				}
			}
			ce6.close();
			ce7.close();
		} else if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Blendenzugaben (+)")) {
			Cursor ce6 = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETMVF2, null);
			if (ce6 != null) {
				if (ce6.moveToFirst()) {
					do {
						defCheck8.put(
								ce6.getString(ce6.getColumnIndex("name")),
								ce6.getInt(ce6.getColumnIndex("def")));
						planet5.add(new Settings(ce6.getString(ce6
								.getColumnIndex("name")), ce6.getInt(ce6
								.getColumnIndex("value"))));
					} while (ce6.moveToNext());
				}
			}
			Cursor ce7 = myDB.rawQuery("SELECT name,value,def FROM "
					+ MY_DB_TABLE_SETFVF2, null);
			if (ce7 != null) {
				if (ce7.moveToFirst()) {
					do {
						defCheck9.put(
								ce7.getString(ce7.getColumnIndex("name")),
								ce7.getInt(ce7.getColumnIndex("def")));
						planet6.add(new Settings(ce7.getString(ce7
								.getColumnIndex("name")), ce7.getInt(ce7
								.getColumnIndex("value"))));
					} while (ce7.moveToNext());
				}
			}
			ce6.close();
			ce7.close();
		}

		Cursor ce8 = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETKOR, null);
		if (ce8 != null) {
			if (ce8.moveToFirst()) {
				do {
					defCheck10.put(ce8.getString(ce8.getColumnIndex("name")),
							ce8.getInt(ce8.getColumnIndex("def")));
					planet7.add(new Settings(ce8.getString(ce8
							.getColumnIndex("name")), ce8.getInt(ce8
							.getColumnIndex("value"))));
				} while (ce8.moveToNext());
			}
		}

		// cb.close();
		ce8.close();
		cc.close();
		cd.close();
		ce.close();
		ce1.close();
		ce2.close();
		ce3.close();
		ce4.close();
		ce5.close();

		ce8.close();

		Cursor c = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETCAM, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					defCheck11.put(c.getString(c.getColumnIndex("name")),
							c.getInt(c.getColumnIndex("def")));
					aplanets.add(new Settings(c.getString(c
							.getColumnIndex("name")), c.getInt(c
							.getColumnIndex("value"))));
				} while (c.moveToNext());
			}
		}

		Cursor cspec = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETBW, null);
		if (cspec != null) {
			if (cspec.moveToFirst()) {
				do {
					aplanetsspec.add(new Settings(cspec.getString(cspec
							.getColumnIndex("name")), cspec.getInt(cspec
							.getColumnIndex("value"))));
				} while (cspec.moveToNext());
			}
		}
		cspec.close();

		Cursor ca = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETFF, null);
		if (ca != null) {
			if (ca.moveToFirst()) {
				do {
					defCheck12.put(ca.getString(ca.getColumnIndex("name")),
							ca.getInt(ca.getColumnIndex("def")));
					aplanets1.add(new Settings(ca.getString(ca
							.getColumnIndex("name")), ca.getInt(ca
							.getColumnIndex("value"))));
				} while (ca.moveToNext());
			}
		}

		Cursor caa = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETEMP, null);
		if (caa != null) {
			if (caa.moveToFirst()) {
				do {
					defCheck13.put(caa.getString(caa.getColumnIndex("name")),
							caa.getInt(caa.getColumnIndex("def")));
					aplanets7.add(new Settings(caa.getString(caa
							.getColumnIndex("name")), caa.getInt(caa
							.getColumnIndex("value"))));
				} while (caa.moveToNext());
			}
		}

		Cursor cb = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETBW, null);
		if (cb != null) {
			if (cb.moveToFirst()) {
				do {
					defCheck14.put(cb.getString(cb.getColumnIndex("name")),
							cb.getInt(cb.getColumnIndex("def")));
					aplanets2.add(new Settings(cb.getString(cb
							.getColumnIndex("name")), cb.getInt(cb
							.getColumnIndex("value"))));
				} while (cb.moveToNext());
			}
		}

		Cursor cf = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETSON, null);
		if (cf != null) {
			if (cf.moveToFirst()) {
				do {
					defCheck15.put(cf.getString(cf.getColumnIndex("name")),
							cf.getInt(cf.getColumnIndex("def")));
					aplanets6.add(new Settings(cf.getString(cf
							.getColumnIndex("name")), cf.getInt(cf
							.getColumnIndex("value"))));
				} while (cf.moveToNext());
			}
		}
		myDB.close();
		ca.close();
		caa.close();
		cb.close();
		cf.close();
		stopManagingCursor(cspec);
		stopManagingCursor(ca);
		stopManagingCursor(caa);
		stopManagingCursor(cb);

	}

	private void deletefromDB(String TableName, String Name) {
		onCreateDBAndDBTabled();
		try {
			myDB.execSQL("DELETE FROM " + TableName + " WHERE name = '" + Name
					+ "'");
			myDB.close();
		} catch (Exception e) {
			Log.v("Check", "Fehler Delete : " + e);
			myDB.close();
		}
	}

	private void makedefaultDB(String TableName, String Name) {
		onCreateDBAndDBTabled();
		try {
			myDB.execSQL("UPDATE " + TableName + " SET def = '" + 0 + "'");
			myDB.execSQL("UPDATE " + TableName + " SET def = '" + 1
					+ "' WHERE name = '" + Name + "'");
			myDB.close();
		} catch (Exception e) {
			Log.v("Check", "Fehler beim Standartsetzen : " + e);
			myDB.close();
		}
	}

	private void editfromDB(String TableName, String Name, int value) {
		onCreateDBAndDBTabled();
		try {
			myDB.execSQL("UPDATE " + TableName + " SET value = '" + value
					+ "' WHERE name = '" + Name + "'");
			myDB.close();
		} catch (Exception e) {
			Log.v("Check", "Fehler Delete : " + e);
			myDB.close();
		}
	}

	private void onCreateDBAndDBTabled() {
		Log.v("Check", "onCreate : " + MY_DB_NAME);
		myDB = mContext.openOrCreateDatabase(MY_DB_NAME, Context.MODE_PRIVATE,
				null);
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETCAMBW
				+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
				+ ";");
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

	/*
	 * Hilfsklassen etc.
	 */

	public void setFooterColor(int footerColor) {
		mIndicator.setFooterColor(footerColor);
	}

	private class MyPagerAdapter extends PagerAdapter implements TitleProvider {

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@SuppressWarnings("unused")
		private ArrayList<View> views;
		LayoutInflater inflater = getLayoutInflater();

		public MyPagerAdapter(Context context) {
			views = new ArrayList<View>();

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
			return 3;
		}

		@Override
		public Object instantiateItem(View view, int position) {
			View myView = null;
			Log.v("Check", "GET POSITION : " + position);
			if (position == 0) {
				slideView9 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView9.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView9.findViewById(R.id.tablor);
				addKate3 = (Button) slideView9.findViewById(R.id.addkamera);
				katText9 = ((EditText) slideView9
						.findViewById(R.id.kameramodell));
				myListView9 = (ListView) slideView9
						.findViewById(android.R.id.list);

				freecell3.setText("Blitz");
				tablor3.setBackgroundResource(R.drawable.shapebluetable);
				listAdapte9 = new SettingsArrayAdapter(mContext, planets5, 56);
				myListView9.setAdapter(listAdapte9);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView9
						.setOnItemLongClickListener(new OnItemLongClickListener() {
							@Override
							public boolean onItemLongClick(AdapterView<?> arg0,
									final View arg1, final int arg2, long arg3) {
								Display display = ((WindowManager) mContext
										.getSystemService(Context.WINDOW_SERVICE))
										.getDefaultDisplay();
								LayoutInflater inflaterOwn = (LayoutInflater) mContext
										.getSystemService(LAYOUT_INFLATER_SERVICE);
								View layoutOwn = inflaterOwn.inflate(
										R.layout.longclick,
										(ViewGroup) findViewById(R.id.testen),
										false);
								Button deleteButton = (Button) layoutOwn
										.findViewById(R.id.deletebutton);
								Button cancelButton = (Button) layoutOwn
										.findViewById(R.id.cancelbutton);
								Button editButton = (Button) layoutOwn
										.findViewById(R.id.editbutton);
								deleteButton
										.setText("     Eintrag l�schen     ");
								editButton
										.setText("     Als Standardwert     ");
								editButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												LinearLayout lins = (LinearLayout) arg1;
												TextView texti = (TextView) lins
														.getChildAt(0);
												SharedPreferences.Editor editor = settings
														.edit();
												editor.putString("MakroDef",
														texti.getText()
																.toString());
												editor.commit();
												SharedPreferences.Editor editor1 = settings
														.edit();

												makedefaultDB(
														MY_DB_TABLE_SETBLI,
														texti.getText()
																.toString());
												readDB();
												listAdapte9
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														"Standardwert gespeichert",
														Toast.LENGTH_SHORT)
														.show();
												pw.dismiss();
											}
										});
								deleteButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												LinearLayout lins = (LinearLayout) arg1;
												TextView texti = (TextView) lins
														.getChildAt(0);

												// HIER

												deletefromDB(
														MY_DB_TABLE_SETBLI,
														texti.getText()
																.toString());
												readDB();
												listAdapte9 = new SettingsArrayAdapter(
														mContext, planets5, 56);
												myListView9
														.setAdapter(listAdapte9);
												listAdapte9
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														"Eintrag gel�scht!",
														Toast.LENGTH_SHORT)
														.show();

												// --------------
												pw.dismiss();
											}
										});
								cancelButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												pw.dismiss();
											}
										});
								int width = display.getWidth();
								int height = display.getHeight();
								pw = new PopupWindow(layoutOwn,
										(int) (width / 1.6),
										(int) (height / 2.5), true);
								pw.setAnimationStyle(7);
								pw.setBackgroundDrawable(new BitmapDrawable());
								pw.showAtLocation(layoutOwn, Gravity.CENTER, 0,
										0);
								return true;
							}
						});
				addKate3.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						boolean vorhanden = false;
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								katText9.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planets5.size(); i++) {
							vorhanden = planets5.get(i).getName().toString()
									.equals(katText9.getText().toString());
							if (vorhanden) {
								i = (planets5.size() - 1);
							}
						}
						if (vorhanden
								|| katText9.getText().toString().length() == 0
								|| katText9.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									"Das Textfeld ist leer oder das Objekt existiert bereits!",
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(MY_DB_TABLE_SETBLI, katText9.getText()
									.toString(), 0);
							readDB();
							katText9.setText("");
							listAdapte9 = new SettingsArrayAdapter(mContext,
									planets5, 56);
							myListView9.setAdapter(listAdapte9);
							// planets5.add(new
							// Settings(katText9.getText().toString(),0));
							listAdapte9.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									"Eintrag hinzugef�gt!", Toast.LENGTH_SHORT)
									.show();
						}

						// --------------
					}
				});
				katText9.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText9.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView9;
			} else if (position == 1) {
				slideView10 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView10.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView10.findViewById(R.id.tablor);
				addKate3 = (Button) slideView10.findViewById(R.id.addkamera);
				katText10 = ((EditText) slideView10
						.findViewById(R.id.kameramodell));
				myListView10 = (ListView) slideView10
						.findViewById(android.R.id.list);

				freecell3.setText("Blitz Korrektur");
				tablor3.setBackgroundResource(R.drawable.shapebluetable);
				listAdapte10 = new SettingsArrayAdapter(mContext, planet7, 17);
				myListView10.setAdapter(listAdapte10);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView10
						.setOnItemLongClickListener(new OnItemLongClickListener() {
							@Override
							public boolean onItemLongClick(AdapterView<?> arg0,
									final View arg1, final int arg2, long arg3) {
								Display display = ((WindowManager) mContext
										.getSystemService(Context.WINDOW_SERVICE))
										.getDefaultDisplay();
								LayoutInflater inflaterOwn = (LayoutInflater) mContext
										.getSystemService(LAYOUT_INFLATER_SERVICE);
								View layoutOwn = inflaterOwn.inflate(
										R.layout.longclick,
										(ViewGroup) findViewById(R.id.testen),
										false);
								Button deleteButton = (Button) layoutOwn
										.findViewById(R.id.deletebutton);
								Button cancelButton = (Button) layoutOwn
										.findViewById(R.id.cancelbutton);
								Button editButton = (Button) layoutOwn
										.findViewById(R.id.editbutton);
								deleteButton
										.setText("     Eintrag l�schen     ");
								editButton
										.setText("     Als Standardwert     ");
								editButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												LinearLayout lins = (LinearLayout) arg1;
												TextView texti = (TextView) lins
														.getChildAt(0);
												SharedPreferences.Editor editor = settings
														.edit();
												editor.putString("MakroDef",
														texti.getText()
																.toString());
												editor.commit();
												SharedPreferences.Editor editor1 = settings
														.edit();

												makedefaultDB(
														MY_DB_TABLE_SETKOR,
														texti.getText()
																.toString());
												readDB();
												listAdapte10
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														"Standardwert gespeichert",
														Toast.LENGTH_SHORT)
														.show();
												pw.dismiss();
											}
										});
								deleteButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												LinearLayout lins = (LinearLayout) arg1;
												TextView texti = (TextView) lins
														.getChildAt(0);

												// HIER

												deletefromDB(
														MY_DB_TABLE_SETKOR,
														texti.getText()
																.toString());
												readDB();
												listAdapte10 = new SettingsArrayAdapter(
														mContext, planet7, 17);
												myListView10
														.setAdapter(listAdapte10);
												listAdapte10
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														"Eintrag gel�scht!",
														Toast.LENGTH_SHORT)
														.show();

												// --------------
												pw.dismiss();
											}
										});
								cancelButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												pw.dismiss();
											}
										});
								int width = display.getWidth();
								int height = display.getHeight();
								pw = new PopupWindow(layoutOwn,
										(int) (width / 1.6),
										(int) (height / 2.5), true);
								pw.setAnimationStyle(7);
								pw.setBackgroundDrawable(new BitmapDrawable());
								pw.showAtLocation(layoutOwn, Gravity.CENTER, 0,
										0);
								return true;
							}
						});
				addKate3.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						boolean vorhanden = false;
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								katText10.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet7.size(); i++) {
							vorhanden = planet7.get(i).getName().toString()
									.equals(katText10.getText().toString());
							if (vorhanden) {
								i = (planet7.size() - 1);
							}
						}
						if (vorhanden
								|| katText10.getText().toString().length() == 0
								|| katText10.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									"Das Textfeld ist leer oder das Objekt existiert bereits!",
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(MY_DB_TABLE_SETKOR, katText10.getText()
									.toString(), 0);
							readDB();
							katText10.setText("");
							listAdapte10 = new SettingsArrayAdapter(mContext,
									planet7, 17);
							myListView10.setAdapter(listAdapte10);
							// planet7.add(new
							// Settings(katText10.getText().toString(),0));
							listAdapte10.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									"Eintrag hinzugef�gt!", Toast.LENGTH_SHORT)
									.show();
						}

						// --------------
					}
				});
				katText10.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText10.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView10;
			}

			else if (position == 2) {
				// SONDER SETTINGS
				// ---------------------------------------------------------------------------
				View vf = inflater.inflate(R.layout.settingsauswahl, null,
						false);
				if (true) {
					/*
					 * View �ndern
					 */
					freecell6 = (TextView) vf.findViewById(R.id.freecell);
					tablor6 = (TableLayout) vf.findViewById(R.id.tablor);
					myList6 = (ListView) vf.findViewById(android.R.id.list);
					addKate6 = (Button) vf.findViewById(R.id.addkamera);
					Kat6 = (EditText) vf.findViewById(R.id.kameramodell);

					/*
					 * Text und Farbe �ndern
					 */
					freecell6.setText("Sonderentwicklung");
					tablor6.setBackgroundResource(R.drawable.shapebluetable);

					tablor6.setPadding(4, 0, -2, 0);

					/*
					 * Liste �ndern
					 */
					listAdapter6 = new SettingsArrayAdapter(mContext,
							aplanets6, 67);
					myList6.setAdapter(listAdapter6);

					myList6.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								final View arg1, final int arg2, long arg3) {

							// HIER

							Display display = ((WindowManager) mContext
									.getSystemService(Context.WINDOW_SERVICE))
									.getDefaultDisplay();
							LayoutInflater inflaterOwn = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layoutOwn = inflaterOwn.inflate(
									R.layout.longclick,
									(ViewGroup) findViewById(R.id.testen),
									false);
							Button deleteButton = (Button) layoutOwn
									.findViewById(R.id.deletebutton);
							Button cancelButton = (Button) layoutOwn
									.findViewById(R.id.cancelbutton);
							Button editButton = (Button) layoutOwn
									.findViewById(R.id.editbutton);
							deleteButton.setText("     Eintrag l�schen     ");
							editButton.setText("     Als Standardwert     ");

							deleteButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LinearLayout lins = (LinearLayout) arg1;
											TextView texti = (TextView) lins
													.getChildAt(0);
											deletefromDB(MY_DB_TABLE_SETSON,
													texti.getText().toString());
											readDB();
											listAdapter6 = new SettingsArrayAdapter(
													mContext, aplanets6, 67);
											myList6.setAdapter(listAdapter6);
											listAdapter6.notifyDataSetChanged();
											Toast.makeText(
													getApplicationContext(),
													"Eintrag gel�scht!",
													Toast.LENGTH_SHORT).show();
											pw.dismiss();
										}
									});

							editButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LinearLayout lins = (LinearLayout) arg1;
											TextView texti = (TextView) lins
													.getChildAt(0);
											makedefaultDB(MY_DB_TABLE_SETSON,
													texti.getText().toString());
											readDB();
											listAdapter6.notifyDataSetChanged();
											Toast.makeText(mContext,
													"Standardwert gespeichert",
													Toast.LENGTH_SHORT).show();
											pw.dismiss();
										}
									});

							cancelButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											pw.dismiss();

										}
									});
							int width = display.getWidth();
							int height = display.getHeight();
							pw = new PopupWindow(layoutOwn,
									(int) (width / 1.6), (int) (height / 2.5),
									true);
							pw.setAnimationStyle(7);
							pw.setBackgroundDrawable(new BitmapDrawable());
							pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);

							return true;

							// HIER ENDE
						}
					});
					addKate6.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									addKate6.getApplicationWindowToken(), 0);
							boolean vorhanden = false;
							for (int i = 0; i < aplanets6.size(); i++) {
								vorhanden = aplanets6.get(i).getName()
										.toString()
										.equals(Kat6.getText().toString());
								if (vorhanden) {
									i = (aplanets6.size() - 1);
								}
							}
							if (vorhanden
									|| Kat6.getText().toString().length() == 0
									|| Kat6.getText().toString().trim()
											.length() == 0) {
								Toast.makeText(
										getApplicationContext(),
										"Das Textfeld ist leer oder das Objekt existiert bereits!",
										Toast.LENGTH_SHORT).show();
							} else {
								writeDB(MY_DB_TABLE_SETSON, Kat6.getText()
										.toString(), 0);
								readDB();
								Kat6.setText("");
								listAdapter6 = new SettingsArrayAdapter(
										mContext, aplanets6, 67);
								myList6.setAdapter(listAdapter6);
								// aplanets6.add(new
								// Settings(Kat6.getText().toString(),0));
								listAdapter6.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(),
										"Eintrag hinzugef�gt!",
										Toast.LENGTH_SHORT).show();
							}
						}
					});

					Kat6.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							// If the event is a key-down event on the "enter"
							// button
							if ((event.getAction() == KeyEvent.ACTION_DOWN)
									&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										Kat6.getApplicationWindowToken(), 0);
								return true;
							}
							return false;
						}
					});
				}
				myView = vf;
				// ((ViewPager) view).addView(vf);
				// ---------------------------------------------------------------------------
			}
			// myView = v;
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
				setFooterColor(0xFF0000BB);
			if (viewPager.getCurrentItem() == 1)
				setFooterColor(0xFF0000BB);
			if (viewPager.getCurrentItem() == 2)
				setFooterColor(0xFF0000BB);
			if (viewPager.getCurrentItem() == 3)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 4)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 5)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 6)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 7)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 8)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 9)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 10)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 11)
				setFooterColor(0xFF009900);
			if (viewPager.getCurrentItem() == 12)
				setFooterColor(0xFF009900);
			if (viewPager.getCurrentItem() == 13)
				setFooterColor(0xFF0000BB);
			if (viewPager.getCurrentItem() == 14)
				setFooterColor(0xFF0000BB);
			if (viewPager.getCurrentItem() == 15)
				setFooterColor(0xFF0000BB);

			return SlideNewSettingsSon.CONTENT[position
					% SlideNewSettingsSon.CONTENT.length];
		}

	}

	private static class Settings {
		private String name = "";
		private boolean checked = false;

		public Settings(String name, int Status) {
			this.name = name;
			if (Status == 1) {
				checked = true;
			} else
				checked = false;
		}

		public String getName() {
			return name;
		}

		public int isChecked() {
			if (checked) {
				return 1;
			} else
				return 0;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public String toString() {
			if (checked) {
				return 1 + " " + name;
			} else
				return 0 + " " + name;
		}
	}

	private static class SettingsViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		@SuppressWarnings("unused")
		public SettingsViewHolder() {
		}

		public SettingsViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public TextView getTextView() {
			return textView;
		}
	}

	private class SettingsArrayAdapter extends ArrayAdapter<Settings> {

		private LayoutInflater inflater;
		int nummer = 0;

		public SettingsArrayAdapter(Context context,
				ArrayList<Settings> planetList, int number) {
			super(context, R.layout.list_item, R.id.listItemText, planetList);
			nummer = number;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Settings planet = (Settings) this.getItem(position);

			CheckBox checkBox;
			TextView textView;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);

				textView = (TextView) convertView
						.findViewById(R.id.listItemText);
				checkBox = (CheckBox) convertView.findViewById(R.id.check);

				convertView.setTag(new SettingsViewHolder(textView, checkBox));

				checkBox.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Settings planet = (Settings) cb.getTag();
						planet.setChecked(cb.isChecked());
						int value = 0;
						if (cb.isChecked() == true) {
							value = 1;
						}
						if (nummer == 19) {
							editfromDB(MY_DB_TABLE_SETFF, planet.name, value);
						} else if (nummer == 1337) {
							editfromDB(MY_DB_TABLE_SETBW, planet.name, value);
						} else if (nummer == 51) {
							editfromDB(MY_DB_TABLE_SETNM, planet.name, value);
						} else if (nummer == 79) {
							editfromDB(MY_DB_TABLE_SETFIL, planet.name, value);
						} else if (nummer == 56) {
							editfromDB(MY_DB_TABLE_SETBLI, planet.name, value);
						} else if (nummer == 67) {
							editfromDB(MY_DB_TABLE_SETSON, planet.name, value);
						} else if (nummer == 76) {
							editfromDB(MY_DB_TABLE_SETEMP, planet.name, value);
						} else if (nummer == 90) {
							editfromDB(MY_DB_TABLE_SETFOK, planet.name, value);
						} else if (nummer == 50) {
							editfromDB(MY_DB_TABLE_SETBLE, planet.name, value);
						} else if (nummer == 34) {
							editfromDB(MY_DB_TABLE_SETZEI, planet.name, value);
						} else if (nummer == 39) {
							editfromDB(MY_DB_TABLE_SETMES, planet.name, value);
						} else if (nummer == 42) {
							editfromDB(MY_DB_TABLE_SETPLU, planet.name, value);
						} else if (nummer == 63) {
							editfromDB(MY_DB_TABLE_SETMVF, planet.name, value);
						} else if (nummer == 88) {
							editfromDB(MY_DB_TABLE_SETFVF, planet.name, value);
						} else if (nummer == 17) {
							editfromDB(MY_DB_TABLE_SETKOR, planet.name, value);
						}
					}
				});
			} else {
				SettingsViewHolder viewHolder = (SettingsViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			checkBox.setTag(planet);

			if (planet.isChecked() == 1) {
				checkBox.setChecked(true);
			} else if (planet.isChecked() == 0) {
				checkBox.setChecked(false);
			}
			textView.setText(planet.getName());
			settings = PreferenceManager.getDefaultSharedPreferences(mContext);
			Log.v("Check", "Nummer : " + nummer + "   -   Check auf Null: "
					+ (defCheck12 == null));
			if (nummer == 19 && defCheck12.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 51 && defCheck0.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 79 && defCheck1.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 56 && defCheck2.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 67 && defCheck15.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 76 && defCheck13.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 90 && defCheck3.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 50 && defCheck4.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 34 && defCheck5.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 39 && defCheck6.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 42 && defCheck7.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 63 && defCheck8.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 88 && defCheck9.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else if (nummer == 17 && defCheck10.get(planet.getName()) == 1) {
				textView.setTextColor(0xFF0000AA);
			} else {
				textView.setTextColor(0xFF000000);
			}

			return convertView;
		}

	}

	public Object onRetainNonConfigurationInstance() {
		return aplanets;
	}

	public void setSetButtonColor(Button button1, Button button2,
			Button button3, Button button4) {
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		if (settings.getInt("LoadSet", 1) == 1) {
			button1.setTextColor(0xff00cc00);
			button2.setTextColor(0xff000000);
			button3.setTextColor(0xff000000);
			button4.setTextColor(0xff000000);
		} else if (settings.getInt("LoadSet", 1) == 2) {
			button1.setTextColor(0xff000000);
			button2.setTextColor(0xff00cc00);
			button3.setTextColor(0xff000000);
			button4.setTextColor(0xff000000);
		} else if (settings.getInt("LoadSet", 1) == 3) {
			button1.setTextColor(0xff000000);
			button2.setTextColor(0xff000000);
			button3.setTextColor(0xff00cc00);
			button4.setTextColor(0xff000000);
		} else if (settings.getInt("LoadSet", 1) == 4) {
			button1.setTextColor(0xff000000);
			button2.setTextColor(0xff000000);
			button3.setTextColor(0xff000000);
			button4.setTextColor(0xff00cc00);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) Men� und
	 * Set Methoden
	 */

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settingsmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.opt_perso) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(
					"M�chten Sie wirklich alle Einstellungen zur�cksetzen ?")
					.setCancelable(false)
					.setPositiveButton("Ja",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									new resetSettings().execute();
								}
							})
					.setNegativeButton("Nein",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		} else if (item.getItemId() == R.id.opt_setload) {
			LayoutInflater inflaterOwn1 = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layoutOwn1 = inflaterOwn1.inflate(R.layout.popupmenu,
					(ViewGroup) findViewById(R.id.users), false);
			final SharedPreferences.Editor editor11 = settings.edit();
			final Button setsave1 = (Button) layoutOwn1
					.findViewById(R.id.setbutton);
			final View setview1 = (View) layoutOwn1.findViewById(R.id.setview);
			final Button setone1 = (Button) layoutOwn1
					.findViewById(R.id.setone);
			final Button settwo1 = (Button) layoutOwn1
					.findViewById(R.id.settwo);
			final Button setthree1 = (Button) layoutOwn1
					.findViewById(R.id.setthree);
			final Button setfour1 = (Button) layoutOwn1
					.findViewById(R.id.setfour);
			setsave1.setText("Laden");
			setSetButtonColor(setone1, settwo1, setthree1, setfour1);
			setone1.setText(settings.getString("SetButtonOne", "Default"));
			settwo1.setText(settings.getString("SetButtonTwo", "Set-Zwei"));
			setthree1.setText(settings.getString("SetButtonThree", "Set-Drei"));
			setfour1.setText(settings.getString("SetButtonFour", "Set-Vier"));
			setone1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setsave1.setVisibility(Button.VISIBLE);
					setview1.setVisibility(View.VISIBLE);
					setButtonClicked = 1;
					setSetButtonColor(setone1, settwo1, setthree1, setfour1);
					setone1.setTextColor(0xaa000000);

				}
			});
			settwo1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setsave1.setVisibility(Button.VISIBLE);
					setview1.setVisibility(View.VISIBLE);
					setButtonClicked = 2;
					setSetButtonColor(setone1, settwo1, setthree1, setfour1);
					settwo1.setTextColor(0xaa000000);

				}
			});
			setthree1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setsave1.setVisibility(Button.VISIBLE);
					setview1.setVisibility(View.VISIBLE);
					setButtonClicked = 3;
					setSetButtonColor(setone1, settwo1, setthree1, setfour1);
					setthree1.setTextColor(0xaa000000);

				}
			});
			setfour1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setsave1.setVisibility(Button.VISIBLE);
					setview1.setVisibility(View.VISIBLE);
					setButtonClicked = 4;
					setSetButtonColor(setone1, settwo1, setthree1, setfour1);
					setfour1.setTextColor(0xaa000000);

				}
			});
			setsave1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (setButtonClicked == 1) {
						editor11.putInt("LoadSet", 1);
						editor11.putString("SettingsTable", MY_DB_SET);
						editor11.commit();
						MY_DB_NAME = MY_DB_SET;
						readDB();
						ViewPager viewPager111 = (ViewPager) findViewById(R.id.viewPager);
						MyPagerAdapter adapter111 = new MyPagerAdapter(mContext);
						viewPager111.setAdapter(adapter111);

					} else if (setButtonClicked == 2) {
						editor11.putInt("LoadSet", 2);
						editor11.putString("SettingsTable", MY_DB_SET1);
						editor11.commit();
						MY_DB_NAME = MY_DB_SET1;
						readDB();
						ViewPager viewPager111 = (ViewPager) findViewById(R.id.viewPager);
						MyPagerAdapter adapter111 = new MyPagerAdapter(mContext);
						viewPager111.setAdapter(adapter111);

					} else if (setButtonClicked == 3) {
						editor11.putInt("LoadSet", 3);
						editor11.putString("SettingsTable", MY_DB_SET2);
						editor11.commit();
						MY_DB_NAME = MY_DB_SET2;
						readDB();
						ViewPager viewPager111 = (ViewPager) findViewById(R.id.viewPager);
						MyPagerAdapter adapter111 = new MyPagerAdapter(mContext);
						viewPager111.setAdapter(adapter111);

					} else if (setButtonClicked == 4) {
						editor11.putInt("LoadSet", 4);
						editor11.putString("SettingsTable", MY_DB_SET3);
						editor11.commit();
						MY_DB_NAME = MY_DB_SET3;
						readDB();
						ViewPager viewPager111 = (ViewPager) findViewById(R.id.viewPager);
						MyPagerAdapter adapter111 = new MyPagerAdapter(mContext);
						viewPager111.setAdapter(adapter111);

					}
					pw.dismiss();
					mIndicator.setCurrentItem(0);
					viewPager.setCurrentItem(0, false);
					Toast.makeText(getApplicationContext(),
							"Set wurde erfolgreich geladen!",
							Toast.LENGTH_SHORT).show();
				}
			});
			pw = new PopupWindow(layoutOwn1,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pw.setAnimationStyle(-1);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(layoutOwn1, Gravity.CENTER, 0, 0);
			return true;
		} else if (item.getItemId() == R.id.opt_setset) {
			LayoutInflater inflaterOwn = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layoutOwn = inflaterOwn.inflate(R.layout.popupmenu,
					(ViewGroup) findViewById(R.id.users), false);
			final SharedPreferences.Editor editor1 = settings.edit();
			final EditText setedit = (EditText) layoutOwn
					.findViewById(R.id.setedit);
			final Button setsave = (Button) layoutOwn
					.findViewById(R.id.setbutton);
			final View setview = (View) layoutOwn.findViewById(R.id.setview);
			final Button setone = (Button) layoutOwn.findViewById(R.id.setone);
			final Button settwo = (Button) layoutOwn.findViewById(R.id.settwo);
			final Button setthree = (Button) layoutOwn
					.findViewById(R.id.setthree);
			final Button setfour = (Button) layoutOwn
					.findViewById(R.id.setfour);
			setSetButtonColor(setone, settwo, setthree, setfour);
			setone.setText(settings.getString("SetButtonOne", "Default"));
			settwo.setText(settings.getString("SetButtonTwo", "Set-Zwei"));
			setthree.setText(settings.getString("SetButtonThree", "Set-Drei"));
			setfour.setText(settings.getString("SetButtonFour", "Set.Vier"));
			setone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (settings.getInt("LoadSet", 1) != 1) {
						setsave.setVisibility(Button.VISIBLE);
						setedit.setVisibility(EditText.VISIBLE);
						setview.setVisibility(View.VISIBLE);
						setButtonClicked = 1;
						setSetButtonColor(setone, settwo, setthree, setfour);
						setone.setTextColor(0xaa000000);
					} else {
						Toast.makeText(
								getApplicationContext(),
								"Das geladene Set kann nicht �berschrieben werden!",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			settwo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (settings.getInt("LoadSet", 1) != 2) {
						setsave.setVisibility(Button.VISIBLE);
						setedit.setVisibility(EditText.VISIBLE);
						setview.setVisibility(View.VISIBLE);
						setButtonClicked = 2;
						setSetButtonColor(setone, settwo, setthree, setfour);
						settwo.setTextColor(0xaa000000);
					} else {
						Toast.makeText(
								getApplicationContext(),
								"Das geladene Set kann nicht �berschrieben werden!",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			setthree.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (settings.getInt("LoadSet", 1) != 3) {
						setsave.setVisibility(Button.VISIBLE);
						setedit.setVisibility(EditText.VISIBLE);
						setview.setVisibility(View.VISIBLE);
						setButtonClicked = 3;
						setSetButtonColor(setone, settwo, setthree, setfour);
						setthree.setTextColor(0xaa000000);
					} else {
						Toast.makeText(
								getApplicationContext(),
								"Das geladene Set kann nicht �berschrieben werden!",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			setfour.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (settings.getInt("LoadSet", 1) != 4) {
						setsave.setVisibility(Button.VISIBLE);
						setedit.setVisibility(EditText.VISIBLE);
						setview.setVisibility(View.VISIBLE);
						setButtonClicked = 4;
						setSetButtonColor(setone, settwo, setthree, setfour);
						setfour.setTextColor(0xaa000000);
					} else {
						Toast.makeText(
								getApplicationContext(),
								"Das geladene Set kann nicht �berschrieben werden!",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			setsave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (setedit.getText().toString().length() == 0
							|| setedit.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(),
								"Leerer Name ist nicht erlaubt!",
								Toast.LENGTH_SHORT).show();
					} else {
						if (setButtonClicked == 1) {
							editor1.putInt("LoadSet", 1);
							editor1.putString("SetButtonOne", setedit.getText()
									.toString());
							editor1.commit();
							setone.setText(setedit.getText().toString());
							setone.setTextColor(0xff00cc00);
							setedit.setText("");
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									setedit.getApplicationWindowToken(), 0);
							new makeSet(MY_DB_SET).execute();

						} else if (setButtonClicked == 2) {
							editor1.putInt("LoadSet", 2);
							editor1.putString("SetButtonTwo", setedit.getText()
									.toString());
							editor1.commit();
							settwo.setText(setedit.getText().toString());
							settwo.setTextColor(0xff00cc00);
							setedit.setText("");
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									setedit.getApplicationWindowToken(), 0);
							new makeSet(MY_DB_SET1).execute();

						} else if (setButtonClicked == 3) {
							editor1.putInt("LoadSet", 3);
							editor1.putString("SetButtonThree", setedit
									.getText().toString());
							editor1.commit();
							setthree.setText(setedit.getText().toString());
							setthree.setTextColor(0xff00cc00);
							setedit.setText("");
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									setedit.getApplicationWindowToken(), 0);
							new makeSet(MY_DB_SET2).execute();

						} else if (setButtonClicked == 4) {
							editor1.putInt("LoadSet", 4);
							editor1.putString("SetButtonFour", setedit
									.getText().toString());
							editor1.commit();
							setfour.setText(setedit.getText().toString());
							setfour.setTextColor(0xff00cc00);
							setedit.setText("");
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									setedit.getApplicationWindowToken(), 0);
							new makeSet(MY_DB_SET3).execute();

						}
						pw.dismiss();
					}
				}
			});
			pw = new PopupWindow(layoutOwn,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pw.setAnimationStyle(-1);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public class makeSet extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;
		String thisset;

		public makeSet(String set) {
			dialog = new ProgressDialog(mContext);
			thisset = set;
		}

		protected void onPreExecute() {
			this.dialog.setMessage("...saving settings-set...");
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			ViewPager viewPager1 = (ViewPager) findViewById(R.id.viewPager);
			MyPagerAdapter adapter1 = new MyPagerAdapter(mContext);
			viewPager1.setAdapter(adapter1);
			mIndicator.setCurrentItem(0);
			viewPager.setCurrentItem(0, false);
			Toast.makeText(
					getApplicationContext(),
					"Set wurde erfolgreich erstellt und und kann nun bearbeitet werden!",
					Toast.LENGTH_SHORT).show();

		}

		protected Boolean doInBackground(final String... args) {
			// try {
			myDBSet = mContext.openOrCreateDatabase(thisset,
					Context.MODE_PRIVATE, null);
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETCAMBW
					+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETCAM
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETFF
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETEMP
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETBW
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETNM
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETFIL
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETBLI
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETSON
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETTYP
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETFOK
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETBLE
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETZEI
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETMES
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETPLU
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETMAK
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETMVF
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETFVF
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETKOR
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETMVF2
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ MY_DB_TABLE_SETFVF2
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");

			ArrayList<String> TableNames = new ArrayList<String>();
			TableNames.add("SettingsCamera");
			TableNames.add("SettingsFilmFormat");
			TableNames.add("SettingsFilmEmpf");
			TableNames.add("SettingsBrennweite");
			TableNames.add("SettingsNahzubehor");
			TableNames.add("SettingsFilter");
			TableNames.add("SettingsBlitz");
			TableNames.add("SettingsSonder");
			TableNames.add("SettingsFokus");
			TableNames.add("SettingsBlende");
			TableNames.add("SettingsZeit");
			TableNames.add("SettingsMessung");
			TableNames.add("SettingsPlusMinus");
			TableNames.add("SettingsMakro");
			TableNames.add("SettingsMakroVF");
			TableNames.add("SettingsFilterVF");
			TableNames.add("SettingsMakroVF2");
			TableNames.add("SettingsFilterVF2");
			TableNames.add("SettingsBlitzKorr");
			TableNames.add("SettingsFilmTyp");

			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETTYP);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETCAM);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETCAMBW);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFIL);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETEMP);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETNM);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETSON);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBLI);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBW);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFF);

			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFOK);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBLE);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETZEI);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMES);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETPLU);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMAK);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMVF);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFVF);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMVF2);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFVF2);
			myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETKOR);

			onCreateDBAndDBTabled();

			for (int xy = 0; xy < TableNames.size(); xy++) {
				Cursor c = myDB.rawQuery("SELECT name,value,def FROM "
						+ TableNames.get(xy), null);
				if (c != null) {
					if (c.moveToFirst()) {
						do {
							ContentValues initialValues = new ContentValues();
							initialValues.clear();
							initialValues.put("name",
									c.getString(c.getColumnIndex("name")));
							initialValues.put("value",
									c.getInt(c.getColumnIndex("value")));
							initialValues.put("def",
									c.getInt(c.getColumnIndex("def")));
							myDBSet.insert(TableNames.get(xy), null,
									initialValues);
						} while (c.moveToNext());
					}
				}
				c.close();
				stopManagingCursor(c);
			}
			myDB.close();
			myDBSet.close();

			SharedPreferences.Editor editor = settings.edit();
			editor.putString("SettingsTable", thisset);
			editor.commit();
			MY_DB_NAME = thisset;
			readDB();

			return null;
		}
	}

	public class resetSettings extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		public resetSettings() {
			dialog = new ProgressDialog(mContext);
		}

		protected void onPreExecute() {
			this.dialog.setMessage("...reset to default...");
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("SettingsTable", MY_DB_SET);
			editor.commit();
			MY_DB_NAME = MY_DB_SET;
			readDB();
			viewPager = (ViewPager) findViewById(R.id.viewPager);
			MyPagerAdapter adapter = new MyPagerAdapter(mContext);
			viewPager.setAdapter(adapter);
			mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
			mIndicator.setViewPager(viewPager);
			mIndicator.setCurrentItem(0);
			viewPager.setCurrentItem(0, false);
		}

		protected Boolean doInBackground(final String... args) {
			try {
				myDBSet = mContext.openOrCreateDatabase(MY_DB_SET,
						Context.MODE_PRIVATE, null);
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETCAMBW
						+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETCAM
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETFF
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETEMP
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETBW
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETNM
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETFIL
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETBLI
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETSON
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETTYP
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETFOK
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETBLE
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETZEI
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETMES
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETPLU
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETMAK
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETMVF
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETFVF
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETKOR
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETMVF2
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");
				myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_SETFVF2
						+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
						+ ";");

				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETTYP);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETCAM);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETCAMBW);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFIL);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETEMP);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETNM);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETSON);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBLI);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBW);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFF);

				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFOK);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBLE);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETZEI);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMES);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETPLU);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMAK);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMVF);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFVF);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMVF2);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFVF2);
				myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETKOR);

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
						+ " Values (" + null + ",'" + "24x36" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
						+ " Values (" + null + ",'" + "4,5x6" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
						+ " Values (" + null + ",'" + "6x6" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
						+ " Values (" + null + ",'" + "6x7" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
						+ " Values (" + null + ",'" + "6x9" + "','" + 1 + "','"
						+ 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 25/15�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 40/17�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 50/18�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 64/19�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 100/21�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 125/22�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 160/23�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 200/24�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 320/26�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 400/27�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 640/29�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 800/30�" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 1000/31�" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 1600/33�" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 3200/36�" + "','"
						+ 1 + "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'" + "Farbnegativ (I: CN)"
						+ "','" + 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'" + "Farbdia (I: CR)" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'"
						+ "Schwarzwei�-Negativ (I: SW)" + "','" + 1 + "','" + 0
						+ "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'"
						+ "Schwarzwei�-Dia (I: SWR)" + "','" + 1 + "','" + 0
						+ "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'"
						+ "Farbdia/Kunstlicht (I: CT)" + "','" + 1 + "','" + 0
						+ "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "NL +1" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "NL +2" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "NL +3" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "NL +4" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "NL +5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "ZR 10" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "ZR 20" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "ZR 30" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "Balgen" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Gelb" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Orange" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Rot" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Gelbgr�n" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Gr�n" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Blau" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "KR" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "KB" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "UV" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Pol" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "ND x2" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "ND x4" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "ND x6" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Soft" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "TTL" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Auto" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Multiblitz 2" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Multiblitz 3+" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Manuell 1/1" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Manuell 1/2" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Manuell 1/4" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Manuell 1/8" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
						+ " Values (" + null + ",'" + "Manuell 1/16" + "','"
						+ 1 + "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Normal" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Push 1" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Push 2" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Push 3" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Pull 1" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Pull 2" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Cross" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
						+ " Values (" + null + ",'" + "Lowcolor" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "Auto" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "Unendlich" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "20 m" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "10 m" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "8 m" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "5 m" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "4 m" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "3 m" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "2 m" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "1,5 m" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "1 m" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "0,8 m" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "0,7 m" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "0,5 m" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
						+ " Values (" + null + ",'" + "0,3 m" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "Auto" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "1,0" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "1,4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "2,0" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "2,8" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "5,6" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "8" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "11" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "16" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "22" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "32" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "45" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
						+ " Values (" + null + ",'" + "64" + "','" + 1 + "','"
						+ 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "Auto" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/8000" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/4000" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/2000" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/1000" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/500" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/250" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/125" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/60" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/30" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/15" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/8" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1/2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "1s" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "2s" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "4s" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "8s" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
						+ " Values (" + null + ",'" + "15s" + "','" + 1 + "','"
						+ 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
						+ " Values (" + null + ",'" + "Licht" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
						+ " Values (" + null + ",'" + "Mehrfeld" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
						+ " Values (" + null + ",'" + "Integral" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
						+ " Values (" + null + ",'" + "Spot" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
						+ " Values (" + null + ",'" + "Multispot" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+5" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+2,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+1,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "+0,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "0" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-0,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-1" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-1,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-2,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-3" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
						+ " Values (" + null + ",'" + "-5" + "','" + 1 + "','"
						+ 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "+2,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "+1,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "+0,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "0" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "-0,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "-1" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "-1,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "-2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "-2,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
						+ " Values (" + null + ",'" + "-3" + "','" + 1 + "','"
						+ 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x16" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x12" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x8" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x6" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x3" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
						+ " Values (" + null + ",'" + "x1.5" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+3,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+2,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+1,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
						+ " Values (" + null + ",'" + "+0,5" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x16" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x12" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x8" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x6" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x3" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
						+ " Values (" + null + ",'" + "x1.5" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+4" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+3,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+2,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+1,5" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
						+ 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
						+ " Values (" + null + ",'" + "+0,5" + "','" + 1
						+ "','" + 0 + "');");

				myDBSet.close();

				onCreateDBAndDBNumber();
				myDBNummer.execSQL("DELETE FROM " + MY_DB_TABLE_NUMMER);
				myDBNummer.close();

				onCreateDBAndDBTabledFilm();
				myDBFilm.execSQL("DELETE FROM " + MY_DB_FILM_TABLE);
				myDBFilm.close();

				settings = PreferenceManager
						.getDefaultSharedPreferences(mContext);
				SharedPreferences.Editor editor = settings.edit();
				editor.clear();
				editor.commit();

				SharedPreferences.Editor editor1 = settings.edit();
				editor1.putInt("FIRSTSTART", 1);
				editor1.commit();

			} catch (Exception e) {
				Log.v("DEBUG", "Fehler bei Set-Erstellung : " + e);
			}
			return null;
		}
	}

}