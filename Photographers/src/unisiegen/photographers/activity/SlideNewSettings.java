package unisiegen.photographers.activity;

/**
 * Hierbei handelt es sich um alle Einstellungen ! Die anderen Einstellungs-Activitys sind nur Teilmengen von dieser f�r eine bessere Aufteilung wenn gew�nscht!
 */

import java.util.ArrayList;
import java.util.HashMap;

import unisiegen.photographers.database.DB;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class SlideNewSettings extends Activity {

	/*
	 * Sonstige Variablen
	 */
	private static String[] CONTENT = null; // The arrays are initialized in the onCreate() method.
	private static String[] puContent = null;
	
	Integer contentIndex = 0;
	TextView tv1, tv2;
	Button weiter, close;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle) LifeCycle-Methoden
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Resources res = getResources();
		CONTENT = res.getStringArray(R.array.settings_slide_contents);
		puContent = res.getStringArray(R.array.strings_tutorial_3);
		
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

		if (settings.getInt("FIRSTSTART", 0) == 1) {
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.post(new Runnable() {
				public void run() {
					popupmenue();
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("FIRSTSTART", 2);
					editor.commit();
				}
			});

		} else if (settings.getInt("FIRSTSTART", 0) == 3) {
			ViewGroup view1 = (ViewGroup) getWindow().getDecorView();
			view1.post(new Runnable() {
				public void run() {
					popupmenue();
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("FIRSTSTART", 5);
					editor.commit();
				}
			});

		}

	}

	public void onResume() {
		super.onResume();
		contentIndex = 0;
	}

	/*
	 * Popup f�r Hilfe
	 */

	public void popupmenue() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layoutOwn1 = inflater.inflate(R.layout.firstpopup,
				(ViewGroup) findViewById(R.id.widget), false);

		pw = new PopupWindow(layoutOwn1, 500, 500, true);
		pw.setAnimationStyle(7);
		pw.setBackgroundDrawable(new BitmapDrawable());
		tv1 = (TextView) layoutOwn1.findViewById(R.id.textview_pop);
		tv1.setText(puContent[contentIndex]);
		contentIndex++;

		weiter = (Button) layoutOwn1.findViewById(R.id.widget41);
		weiter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (contentIndex == puContent.length) {
					pw.dismiss();
				} else {
					tv1.setText(puContent[contentIndex]);
					contentIndex++;
					if (contentIndex == 2) {
						pw.dismiss();
						viewPager.setCurrentItem(4, true);
						pw.showAtLocation(layoutOwn1, Gravity.CENTER, 0, 0);
					}
					if (contentIndex == 4) {
						openOptionsMenu();
					}
					if (contentIndex == 5) {
						pw.dismiss();
						viewPager.setCurrentItem(1, true);
						pw.showAtLocation(layoutOwn1, Gravity.CENTER, 0, 0);
					}
				}
			}
		});

		close = (Button) layoutOwn1.findViewById(R.id.closebutton);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
		pw.showAtLocation(layoutOwn1, Gravity.CENTER, 0, 0);
	}

	/*
	 * Datenbank-Methoden
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
	}

	/*
	 * Zuordnung der Brennweiten/Objektive zu den Cameras
	 */
	private ArrayList<String> getListCAMBW(String Cam) {
		ArrayList<String> camList = new ArrayList<String>();
		onCreateDBAndDBTabled();

		Cursor camBWCursor = myDB.rawQuery("SELECT cam,bw FROM "
				+ DB.MY_DB_TABLE_SETCAMBW + " WHERE cam = '" + Cam + "'", null);
		if (camBWCursor != null) {
			if (camBWCursor.moveToFirst()) {
				do {
					camList.add(camBWCursor.getString(camBWCursor
							.getColumnIndex("bw")));
				} while (camBWCursor.moveToNext());
			}
		}
		myDB.close();
		camBWCursor.close();
		stopManagingCursor(camBWCursor);
		return camList;
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

		Cursor cc = myDB.rawQuery("SELECT name,value,def FROM "
				+ DB.MY_DB_TABLE_SETNM, null);
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
				+ DB.MY_DB_TABLE_SETFIL, null);
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
				+ DB.MY_DB_TABLE_SETBLI, null);
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
				+ DB.MY_DB_TABLE_SETFOK, null);
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
				+ DB.MY_DB_TABLE_SETBLE, null);
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
				+ DB.MY_DB_TABLE_SETZEI, null);
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
				+ DB.MY_DB_TABLE_SETMES, null);
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
				+ DB.MY_DB_TABLE_SETPLU, null);
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
		if (settings.getString("Verlaengerung", getString(R.string.factor)).equals(
				getString(R.string.factor))) {

			Cursor ce6 = myDB.rawQuery("SELECT name,value,def FROM "
					+ DB.MY_DB_TABLE_SETMVF, null);
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
					+ DB.MY_DB_TABLE_SETFVF, null);
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
					+ DB.MY_DB_TABLE_SETMVF2, null);
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
					+ DB.MY_DB_TABLE_SETFVF2, null);
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
				+ DB.MY_DB_TABLE_SETKOR, null);
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
				+ DB.MY_DB_TABLE_SETCAM, null);
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
				+ DB.MY_DB_TABLE_SETBW, null);
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
				+ DB.MY_DB_TABLE_SETFF, null);
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
				+ DB.MY_DB_TABLE_SETEMP, null);
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
				+ DB.MY_DB_TABLE_SETBW, null);
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
				+ DB.MY_DB_TABLE_SETSON, null);
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

	// Brennweiten
	private void deletefromDB(String TableName, String Name, String Bw) {
		onCreateDBAndDBTabled();
		try {
			myDB.execSQL("DELETE FROM " + TableName + " WHERE bw = '" + Bw
					+ "'");
			myDB.close();
		} catch (Exception e) {
			Log.v("Check", "Fehler Delete : " + e);
			myDB.close();
		}
	}

	// Set ausw�hlen
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
		myDB = mContext.openOrCreateDatabase(MY_DB_NAME, Context.MODE_PRIVATE,
				null);
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETCAMBW
				+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETCAM
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETFF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETEMP
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETBW
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETNM
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETFIL
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETBLI
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETSON
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETTYP
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");

		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETFOK
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETBLE
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETZEI
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETMES
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETPLU
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETMAK
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETMVF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETFVF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETKOR
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETMVF2
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDB.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_TABLE_SETFVF2
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");

	}

	/*
	 * Hilfsklassen f�r Custom-ListViews und SlideViews
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
			return 16;
		}

		@Override
		public Object instantiateItem(View view, int position) {
			View myView = null;
			if (position == 0) {
				View mainview = inflater.inflate(R.layout.mainsettings, null,
						false);

				final SharedPreferences.Editor editors = settings.edit();

				final Button tutorialBack = (Button) mainview
						.findViewById(R.id.ButtonTutorialBack);

				tutorialBack.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt("FIRSTSTART", 1);
						editor.commit();
					}
				});

				final ToggleButton geotag = (ToggleButton) mainview
						.findViewById(R.id.geotag);

				geotag.setTextOff(getString(R.string.off));
				geotag.setTextOn(getString(R.string.on));
				geotag.setGravity(Gravity.LEFT);
				geotag.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (!geotag.getText().toString().equals(getString(R.string.on))) {
							if (geotag.isChecked()) {
								editors.putString("geoTag", "ja");
								editors.commit();
							} else {
								editors.putString("geoTag", "nein");
								editors.commit();
							}
						} else {
							if (geotag.isChecked()) {
								editors.putString("geoTag", "ja");
								editors.commit();
							} else {
								editors.putString("geoTag", "nein");
								editors.commit();
							}
						}
					}
				});

				if (settings.getString("geoTag", "nein") == "nein") {
					geotag.setChecked(false);
					Log.v("Check", "geoTag auf false");
				} else {
					geotag.setChecked(true);
					Log.v("Check", "geoTag auf true");
				}

				final Spinner blendenstufen = (Spinner) mainview
						.findViewById(R.id.blenden);
				ArrayList<String> blendenarray = new ArrayList<String>();
				blendenarray.add(getString(R.string.one_one)); 
				blendenarray.add(getString(R.string.one_two));
				blendenarray.add(getString(R.string.one_three));
				ArrayAdapter<String> blendenadapter = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						blendenarray);
				blendenstufen.setAdapter(blendenadapter);
				blendenstufen
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("blendenstufe", blendenstufen
										.getSelectedItem().toString());
								editors.commit();

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}

						});
				if (settings.getString("blendenstufe", getString(R.string.one_one)).equals(getString(R.string.one_one))) {
					blendenstufen.setSelection(0);
				} else if (settings.getString("blendenstufe", getString(R.string.one_one)).equals(
						getString(R.string.one_two))) {
					blendenstufen.setSelection(1);
				} else {
					blendenstufen.setSelection(2);
				}

				final Spinner zeitstempel = (Spinner) mainview
						.findViewById(R.id.zeitstempel);
				ArrayList<String> zeitstempelarray = new ArrayList<String>();
				zeitstempelarray.add(getString(R.string.on));
				zeitstempelarray.add(getString(R.string.off));
				zeitstempelarray.add(getString(R.string.minus_one_minute));
				ArrayAdapter<String> zeitstempeladapter = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						zeitstempelarray);
				zeitstempel.setAdapter(zeitstempeladapter);
				zeitstempel
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								editors.putString("zeitStempel", zeitstempel
										.getSelectedItem().toString());
								editors.commit();

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}

						});
				if (settings.getString("zeitStempel", getString(R.string.on)).equals(getString(R.string.on))) {
					zeitstempel.setSelection(0);
				} else if (settings.getString("zeitStempel", getString(R.string.on))
						.equals(getString(R.string.off))) {
					zeitstempel.setSelection(1);
				} else {
					zeitstempel.setSelection(2);
				}

				final Spinner verlang = (Spinner) mainview
						.findViewById(R.id.verlang);
				ArrayList<String> verlangarray = new ArrayList<String>();
				verlangarray.add(getString(R.string.factor)); 
				verlangarray.add(getString(R.string.aperture_adjusting));
				ArrayAdapter<String> verlangadapter = new ArrayAdapter<String>(
						mContext, android.R.layout.simple_spinner_item,
						verlangarray);
				verlang.setAdapter(verlangadapter);
				verlang.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						editors.putString("Verlaengerung", verlang
								.getSelectedItem().toString());
						editors.commit();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}

				});
				if (settings.getString("verlang", getString(R.string.factor)).equals(
						getString(R.string.factor))) {
					verlang.setSelection(0);
				} else {
					verlang.setSelection(1);
				}

				// views.add(mainview);
				myView = mainview;
			} else

			if (position == 1) {
				View v = inflater
						.inflate(R.layout.settingsauswahl, null, false);
				if (true) {
					freecell = (TextView) v.findViewById(R.id.freecell);
					tablor = (TableLayout) v.findViewById(R.id.tablor);
					myList = (ListView) v.findViewById(android.R.id.list);
					addKate = (Button) v.findViewById(R.id.addkamera);
					Kat = (EditText) v.findViewById(R.id.kameramodell);
					freecell.setText(getString(R.string.camera_models));
					tablor.setBackgroundResource(R.drawable.shaperedtable);
					tablor.setPadding(4, 0, -2, 0);
					listAdapter = new CamArrayAdapter(mContext, aplanets, 0);
					myList.setAdapter(listAdapter);

					myList.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								final View arg1, final int arg2, long arg3) {
							Display display = ((WindowManager) mContext
									.getSystemService(Context.WINDOW_SERVICE))
									.getDefaultDisplay();
							LayoutInflater inflaterOwn = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layoutOwn = inflaterOwn.inflate(
									R.layout.longclickspec,
									(ViewGroup) findViewById(R.id.testen),
									false);
							Button deleteButton = (Button) layoutOwn
									.findViewById(R.id.deletebutton);
							Button cancelButton = (Button) layoutOwn
									.findViewById(R.id.cancelbutton);
							Button editButton = (Button) layoutOwn
									.findViewById(R.id.editbutton);
							Button specButton = (Button) layoutOwn
									.findViewById(R.id.specbutton);
							deleteButton
									.setText(getString(R.string.delete_entry));
							editButton.setText(getString(R.string.make_default));
							editButton.setVisibility(Button.GONE);
							specButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {

											// BW F�NGT AN

											LinearLayout lins = (LinearLayout) arg1;
											final TextView textis = (TextView) ((LinearLayout) lins
													.getChildAt(0))
													.getChildAt(0);
											Display display2 = ((WindowManager) mContext
													.getSystemService(Context.WINDOW_SERVICE))
													.getDefaultDisplay();
											LayoutInflater inflaterOwn2 = (LayoutInflater) mContext
													.getSystemService(LAYOUT_INFLATER_SERVICE);
											View layoutOwn2 = inflaterOwn2
													.inflate(
															R.layout.settingsauswahlspec,
															null, false);
											freecellspec = (TextView) layoutOwn2
													.findViewById(R.id.freecell);
											tablorspec = (TableLayout) layoutOwn2
													.findViewById(R.id.tablor);
											myListspec = (ListView) layoutOwn2
													.findViewById(android.R.id.list);
											addKatespec = (Button) layoutOwn2
													.findViewById(R.id.addkamera);
											Katspec = (EditText) layoutOwn2
													.findViewById(R.id.kameramodell);
											freecellspec
													.setText(getString(R.string.choose_lens));
											tablorspec
													.setBackgroundResource(R.drawable.shaperedtable);
											tablorspec.setPadding(2, 2, 2, 2);
											listAdapterspec = new SettingsArrayAdapterSpec(
													mContext, aplanetsspec,
													1337, textis.getText()
															.toString());
											myListspec
													.setAdapter(listAdapterspec);
											myListspec
													.setOnItemLongClickListener(new OnItemLongClickListener() {
														@Override
														public boolean onItemLongClick(
																AdapterView<?> arg0,
																final View arg1,
																final int arg2,
																long arg3) {
															AlertDialog.Builder builder = new AlertDialog.Builder(
																	mContext);
															builder.setMessage(
																	getString(R.string.delete_entry))
																	.setCancelable(
																			false)
																	.setPositiveButton(
																			getString(R.string.yes),
																			new DialogInterface.OnClickListener() {
																				public void onClick(
																						DialogInterface dialog,
																						int which) {
																					LinearLayout test = (LinearLayout) arg1;
																					TextView tec = (TextView) test
																							.getChildAt(0);
																					deletefromDB(
																							DB.MY_DB_TABLE_SETCAMBW,
																							tec.getText()
																									.toString(),
																							tec.getText()
																									.toString());
																					deletefromDB(
																							DB.MY_DB_TABLE_SETBW,
																							tec.getText()
																									.toString());
																					aplanetsspec
																							.remove(arg2);
																					listAdapterspec
																							.notifyDataSetChanged();
																					readDB();
																					viewPager = (ViewPager) findViewById(R.id.viewPager);
																					MyPagerAdapter adapter = new MyPagerAdapter(
																							mContext);
																					viewPager
																							.setAdapter(adapter);
																					mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
																					mIndicator
																							.setViewPager(viewPager);
																					viewPager
																							.setCurrentItem(
																									1,
																									false);
																				}
																			})
																	.setNegativeButton(
																			getString(R.string.no),
																			new DialogInterface.OnClickListener() {
																				public void onClick(
																						DialogInterface dialog,
																						int which) {
																				}
																			});
															AlertDialog alert = builder
																	.create();
															alert.show();
															return true;
														}
													});
											addKatespec
													.setOnClickListener(new View.OnClickListener() {

														public void onClick(
																View v) {
															InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
															imm.hideSoftInputFromWindow(
																	Katspec.getApplicationWindowToken(),
																	0);
															boolean vorhanden = false;
															for (int i = 0; i < aplanetsspec
																	.size(); i++) {
																vorhanden = aplanetsspec
																		.get(i)
																		.toString()
																		.equals(Katspec
																				.getText()
																				.toString());
																if (vorhanden) {
																	i = (aplanetsspec
																			.size() - 1);
																}
															}
															if (vorhanden
																	|| Katspec
																			.getText()
																			.toString()
																			.length() == 0
																	|| Katspec
																			.getText()
																			.toString()
																			.trim()
																			.length() == 0) {
																Toast.makeText(
																		getApplicationContext(),
																		getString(R.string.empty_or_existing_entry),
																		Toast.LENGTH_SHORT)
																		.show();
															} else {
																writeDB(DB.MY_DB_TABLE_SETBW,
																		Katspec.getText()
																				.toString(),
																		1);
																aplanetsspec
																		.add(new Settings(
																				Katspec.getText()
																						.toString(),
																				1));

																Katspec.setText("");
																listAdapterspec
																		.notifyDataSetChanged();
															}
														}
													});
											int width2 = display2.getWidth();
											int height2 = display2.getHeight();
											PopupWindow popUp = new PopupWindow(
													layoutOwn2,
													(int) (width2 / 1.05),
													(int) (height2 / 1.05),
													true);
											popUp.setAnimationStyle(7);
											popUp.setBackgroundDrawable(new BitmapDrawable());
											pw.dismiss();
											popUp.showAtLocation(layoutOwn2,
													Gravity.CENTER, 0, 0);
										}
									});
							editButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LinearLayout lins = (LinearLayout) arg1;
											TextView texti = (TextView) ((LinearLayout) lins
													.getChildAt(0))
													.getChildAt(0);

											makedefaultDB(
													DB.MY_DB_TABLE_SETCAM,
													texti.getText().toString());

											myList.setAdapter(listAdapter);
											Toast.makeText(mContext,
													getString(R.string.default_saved),
													Toast.LENGTH_SHORT).show();
											pw.dismiss();
										}
									});
							deleteButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LinearLayout lins = (LinearLayout) arg1;
											TextView texti = (TextView) ((LinearLayout) lins
													.getChildAt(0))
													.getChildAt(0);
											deletefromDB(DB.MY_DB_TABLE_SETCAM,
													texti.getText().toString());
											aplanets.remove(arg2);
											listAdapter.notifyDataSetChanged();
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
									(int) (width / 1.3), (int) (height / 1.6),
									true);
							pw.setAnimationStyle(7);
							pw.setBackgroundDrawable(new BitmapDrawable());
							pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
							return true;
						}
					});
					// BW H�RT AUF

					addKate.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									addKate.getApplicationWindowToken(), 0);
							boolean vorhanden = false;
							for (int i = 0; i < aplanets.size(); i++) {
								vorhanden = aplanets.get(i).getName()
										.toString()
										.equals(Kat.getText().toString());
								if (vorhanden) {
									i = (aplanets.size() - 1);
								}
							}
							if (vorhanden
									|| Kat.getText().toString().length() == 0
									|| Kat.getText().toString().trim().length() == 0) {
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.empty_or_existing_entry),
										Toast.LENGTH_SHORT).show();
							} else {
								writeDB(DB.MY_DB_TABLE_SETCAM, Kat.getText()
										.toString(), 1);
								aplanets.add(new Settings(Kat.getText()
										.toString(), 1));
								listAdapter.notifyDataSetChanged();

								// BW F�NGT AN
								Display display2 = ((WindowManager) mContext
										.getSystemService(Context.WINDOW_SERVICE))
										.getDefaultDisplay();
								LayoutInflater inflaterOwn2 = (LayoutInflater) mContext
										.getSystemService(LAYOUT_INFLATER_SERVICE);
								View layoutOwn2 = inflaterOwn2.inflate(
										R.layout.settingsauswahlspec, null,
										false);
								freecellspec = (TextView) layoutOwn2
										.findViewById(R.id.freecell);
								tablorspec = (TableLayout) layoutOwn2
										.findViewById(R.id.tablor);
								myListspec = (ListView) layoutOwn2
										.findViewById(android.R.id.list);
								addKatespec = (Button) layoutOwn2
										.findViewById(R.id.addkamera);
								Katspec = (EditText) layoutOwn2
										.findViewById(R.id.kameramodell);
								freecellspec
										.setText(getString(R.string.choose_lens));
								tablorspec
										.setBackgroundResource(R.drawable.shaperedtable);
								tablorspec.setPadding(2, 2, 2, 2);
								listAdapterspec = new SettingsArrayAdapterSpec(
										mContext, aplanetsspec, 1337, Kat
												.getText().toString());
								myListspec.setAdapter(listAdapterspec);
								myListspec
										.setOnItemLongClickListener(new OnItemLongClickListener() {
											@Override
											public boolean onItemLongClick(
													AdapterView<?> arg0,
													final View arg1,
													final int arg2, long arg3) {
												AlertDialog.Builder builder = new AlertDialog.Builder(
														mContext);
												builder.setMessage(
														getString(R.string.question_delete))
														.setCancelable(false)
														.setPositiveButton(
																getString(R.string.yes),
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																		LinearLayout test = (LinearLayout) arg1;
																		TextView tec = (TextView) test
																				.getChildAt(0);
																		deletefromDB(
																				DB.MY_DB_TABLE_SETCAMBW,
																				tec.getText()
																						.toString(),
																				tec.getText()
																						.toString());
																		deletefromDB(
																				DB.MY_DB_TABLE_SETBW,
																				tec.getText()
																						.toString());
																		aplanetsspec
																				.remove(arg2);
																		listAdapterspec
																				.notifyDataSetChanged();
																		readDB();
																		viewPager = (ViewPager) findViewById(R.id.viewPager);
																		MyPagerAdapter adapter = new MyPagerAdapter(
																				mContext);
																		viewPager
																				.setAdapter(adapter);
																		mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
																		mIndicator
																				.setViewPager(viewPager);
																		viewPager
																				.setCurrentItem(
																						1,
																						false);
																	}
																})
														.setNegativeButton(
																getString(R.string.no),
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int which) {
																	}
																});
												AlertDialog alert = builder
														.create();
												alert.show();
												return true;
											}
										});
								addKatespec
										.setOnClickListener(new View.OnClickListener() {
											public void onClick(View v) {
												InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
												imm.hideSoftInputFromWindow(
														Katspec.getApplicationWindowToken(),
														0);
												boolean vorhanden = false;
												for (int i = 0; i < aplanetsspec
														.size(); i++) {
													vorhanden = aplanetsspec
															.get(i)
															.toString()
															.equals(Katspec
																	.getText()
																	.toString());
													if (vorhanden) {
														i = (aplanetsspec
																.size() - 1);
													}
												}
												if (vorhanden
														|| Katspec.getText()
																.toString()
																.length() == 0
														|| Katspec.getText()
																.toString()
																.trim()
																.length() == 0) {
													Toast.makeText(
															getApplicationContext(),
															getString(R.string.empty_or_existing_entry),
															Toast.LENGTH_SHORT)
															.show();
												} else {
													writeDB(DB.MY_DB_TABLE_SETBW,
															Katspec.getText()
																	.toString(),
															1);
													aplanetsspec
															.add(new Settings(
																	Katspec.getText()
																			.toString(),
																	1));
													Katspec.setText("");
													listAdapterspec
															.notifyDataSetChanged();
												}
											}
										});
								Katspec.setOnKeyListener(new OnKeyListener() {
									@Override
									public boolean onKey(View v, int keyCode,
											KeyEvent event) {
										if ((event.getAction() == KeyEvent.ACTION_DOWN)
												&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
											InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													Katspec.getApplicationWindowToken(),
													0);
											return true;
										}
										return false;
									}
								});
								int width2 = display2.getWidth();
								int height2 = display2.getHeight();
								PopupWindow popUp = new PopupWindow(layoutOwn2,
										(int) (width2 / 1.05),
										(int) (height2 / 1.05), true);
								popUp.setAnimationStyle(7);
								popUp.setBackgroundDrawable(new BitmapDrawable());
								popUp.showAtLocation(layoutOwn2,
										Gravity.CENTER, 0, 0);
								// TESTEN ENDE
								Kat.setText("");
							}
						}
					});
					// BW H�RT AUF

					Kat.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if ((event.getAction() == KeyEvent.ACTION_DOWN)
									&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										Kat.getApplicationWindowToken(), 0);
								return true;
							}
							return false;
						}
					});
				}
				myView = v;
				// ((ViewPager) view).addView(v);

			}

			else if (position == 2) {
				slideView0 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView0.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView0.findViewById(R.id.tablor);
				addKate3 = (Button) slideView0.findViewById(R.id.addkamera);
				katText0 = ((EditText) slideView0
						.findViewById(R.id.kameramodell));
				myListView0 = (ListView) slideView0
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.focus));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte0 = new SettingsArrayAdapter(mContext, planet0, 90);
				myListView0.setAdapter(listAdapte0);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView0
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
								editButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												LinearLayout lins = (LinearLayout) arg1;
												TextView texti = (TextView) lins
														.getChildAt(0);

												makedefaultDB(
														DB.MY_DB_TABLE_SETFOK,
														texti.getText()
																.toString());
												readDB();
												listAdapte0
														.notifyDataSetChanged();

												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETFOK,
														texti.getText()
																.toString());
												readDB();
												listAdapte0 = new SettingsArrayAdapter(
														mContext, planet0, 90);
												myListView0
														.setAdapter(listAdapte0);
												listAdapte0
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText0.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet0.size(); i++) {
							vorhanden = planet0.get(i).getName().toString()
									.equals(katText0.getText().toString());
							if (vorhanden) {
								break;
							}
						}
						if (vorhanden
								|| katText0.getText().toString().length() == 0
								|| katText0.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETFOK, katText0.getText()
									.toString(), 1);

							readDB();
							katText0.setText("");
							listAdapte0 = new SettingsArrayAdapter(mContext,
									planet0, 90);
							myListView0.setAdapter(listAdapte0);
							listAdapte0.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText0.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText0.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView0;
			} else if (position == 3) {
				slideView1 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView1.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView1.findViewById(R.id.tablor);
				addKate3 = (Button) slideView1.findViewById(R.id.addkamera);
				katText1 = ((EditText) slideView1
						.findViewById(R.id.kameramodell));
				myListView1 = (ListView) slideView1
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.aperture));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte1 = new SettingsArrayAdapter(mContext, planet1, 50);
				myListView1.setAdapter(listAdapte1);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView1
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETBLE,
														texti.getText()
																.toString());
												readDB();
												listAdapte1
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETBLE,
														texti.getText()
																.toString());
												readDB();
												listAdapte1 = new SettingsArrayAdapter(
														mContext, planet1, 50);
												myListView1
														.setAdapter(listAdapte1);
												listAdapte1
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.entry_saved),
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
								katText1.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet1.size(); i++) {
							vorhanden = planet1.get(i).getName().toString()
									.equals(katText1.getText().toString());
							if (vorhanden) {
								i = (planet1.size() - 1);
							}
						}
						if (vorhanden
								|| katText1.getText().toString().length() == 0
								|| katText1.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETBLE, katText1.getText()
									.toString(), 1);
							readDB();
							katText1.setText("");
							listAdapte1 = new SettingsArrayAdapter(mContext,
									planet1, 50);
							myListView1.setAdapter(listAdapte1);
							listAdapte1.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText1.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText1.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView1;
			} else if (position == 4) {
				slideView2 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView2.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView2.findViewById(R.id.tablor);
				addKate3 = (Button) slideView2.findViewById(R.id.addkamera);
				katText2 = ((EditText) slideView2
						.findViewById(R.id.kameramodell));
				myListView2 = (ListView) slideView2
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.exposure));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte2 = new SettingsArrayAdapter(mContext, planet2, 34);
				myListView2.setAdapter(listAdapte2);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView2
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETZEI,
														texti.getText()
																.toString());
												readDB();
												listAdapte2
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETZEI,
														texti.getText()
																.toString());
												readDB();
												listAdapte2 = new SettingsArrayAdapter(
														mContext, planet2, 34);
												myListView2
														.setAdapter(listAdapte2);
												listAdapte2
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText2.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet2.size(); i++) {
							vorhanden = planet2.get(i).getName().toString()
									.equals(katText2.getText().toString());
							if (vorhanden) {
								i = (planet2.size() - 1);
							}
						}
						if (vorhanden
								|| katText2.getText().toString().length() == 0
								|| katText2.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETZEI, katText2.getText()
									.toString(), 1);
							readDB();
							katText2.setText("");
							listAdapte2 = new SettingsArrayAdapter(mContext,
									planet2, 34);
							myListView2.setAdapter(listAdapte2);
							// planet2.add(new
							// Settings(katText2.getText().toString(),0));
							listAdapte2.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText2.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText2.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView2;
			} else if (position == 5) {
				slideView3 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView3.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView3.findViewById(R.id.tablor);
				addKate3 = (Button) slideView3.findViewById(R.id.addkamera);
				katText3 = ((EditText) slideView3
						.findViewById(R.id.kameramodell));
				myListView3 = (ListView) slideView3
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.measurement));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte3 = new SettingsArrayAdapter(mContext, planet3, 39);
				myListView3.setAdapter(listAdapte3);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView3
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETMES,
														texti.getText()
																.toString());
												readDB();
												listAdapte3
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETMES,
														texti.getText()
																.toString());
												readDB();
												listAdapte3 = new SettingsArrayAdapter(
														mContext, planet3, 39);
												myListView3
														.setAdapter(listAdapte3);
												listAdapte3
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText3.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet3.size(); i++) {
							vorhanden = planet3.get(i).getName().toString()
									.equals(katText3.getText().toString());
							if (vorhanden) {
								i = (planet3.size() - 1);
							}
						}
						if (vorhanden
								|| katText3.getText().toString().length() == 0
								|| katText3.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETMES, katText3.getText()
									.toString(), 1);
							readDB();
							katText3.setText("");
							listAdapte3 = new SettingsArrayAdapter(mContext,
									planet3, 39);
							myListView3.setAdapter(listAdapte3);
							// planet3.add(new
							// Settings(katText3.getText().toString(),0));
							listAdapte3.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText3.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText3.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView3;
			} else if (position == 6) {
				slideView4 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView4.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView4.findViewById(R.id.tablor);
				addKate3 = (Button) slideView4.findViewById(R.id.addkamera);
				katText4 = ((EditText) slideView4
						.findViewById(R.id.kameramodell));
				myListView4 = (ListView) slideView4
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.exposure_correction));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte4 = new SettingsArrayAdapter(mContext, planet4, 42);
				myListView4.setAdapter(listAdapte4);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView4
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETPLU,
														texti.getText()
																.toString());
												readDB();
												listAdapte4
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETPLU,
														texti.getText()
																.toString());
												readDB();
												listAdapte4 = new SettingsArrayAdapter(
														mContext, planet4, 42);
												myListView4
														.setAdapter(listAdapte4);
												listAdapte4
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText4.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet4.size(); i++) {
							vorhanden = planet4.get(i).getName().toString()
									.equals(katText4.getText().toString());
							if (vorhanden) {
								i = (planet4.size() - 1);
							}
						}
						if (vorhanden
								|| katText4.getText().toString().length() == 0
								|| katText4.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETPLU, katText4.getText()
									.toString(), 1);
							readDB();
							katText4.setText("");
							listAdapte4 = new SettingsArrayAdapter(mContext,
									planet4, 42);
							myListView4.setAdapter(listAdapte4);
							// planet4.add(new
							// Settings(katText4.getText().toString(),0));
							listAdapte4.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText4.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText4.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView4;
			} else if (position == 7) {
				slideView5 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView5.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView5.findViewById(R.id.tablor);
				addKate3 = (Button) slideView5.findViewById(R.id.addkamera);
				katText5 = ((EditText) slideView5
						.findViewById(R.id.kameramodell));
				myListView5 = (ListView) slideView5
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.macro));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte5 = new SettingsArrayAdapter(mContext, planets3, 51);
				myListView5.setAdapter(listAdapte5);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView5
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
								editButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												LinearLayout lins = (LinearLayout) arg1;
												TextView texti = (TextView) lins
														.getChildAt(0);

												makedefaultDB(
														DB.MY_DB_TABLE_SETNM,
														texti.getText()
																.toString());
												readDB();
												listAdapte5
														.notifyDataSetChanged();

												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETNM,
														texti.getText()
																.toString());
												readDB();
												listAdapte5 = new SettingsArrayAdapter(
														mContext, planets3, 51);
												myListView5
														.setAdapter(listAdapte5);
												listAdapte5
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText5.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planets3.size(); i++) {
							vorhanden = planets3.get(i).getName().toString()
									.equals(katText5.getText().toString());
							if (vorhanden) {
								i = (planets3.size() - 1);
							}
						}
						if (vorhanden
								|| katText5.getText().toString().length() == 0
								|| katText5.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETNM, katText5.getText()
									.toString(), 1);
							readDB();
							katText5.setText("");
							listAdapte5 = new SettingsArrayAdapter(mContext,
									planets3, 51);
							myListView5.setAdapter(listAdapte5);
							// planets3.add(new
							// Settings(katText5.getText().toString(),0));
							listAdapte5.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText5.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText5.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView5;
			} else if (position == 8) {
				slideView6 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView6.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView6.findViewById(R.id.tablor);
				addKate3 = (Button) slideView6.findViewById(R.id.addkamera);
				katText6 = ((EditText) slideView6
						.findViewById(R.id.kameramodell));
				myListView6 = (ListView) slideView6
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.macro_vf));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte6 = new SettingsArrayAdapter(mContext, planet5, 63);
				myListView6.setAdapter(listAdapte6);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView6
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETMVF,
														texti.getText()
																.toString());
												readDB();
												listAdapte6
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETMVF,
														texti.getText()
																.toString());
												readDB();
												listAdapte6 = new SettingsArrayAdapter(
														mContext, planet5, 63);
												myListView6
														.setAdapter(listAdapte6);
												listAdapte6
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText6.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet5.size(); i++) {
							vorhanden = planet5.get(i).getName().toString()
									.equals(katText6.getText().toString());
							if (vorhanden) {
								i = (planet5.size() - 1);
							}
						}
						if (vorhanden
								|| katText6.getText().toString().length() == 0
								|| katText6.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETMVF, katText6.getText()
									.toString(), 1);
							readDB();
							katText6.setText("");
							listAdapte6 = new SettingsArrayAdapter(mContext,
									planet5, 63);
							myListView6.setAdapter(listAdapte6);
							// planet5.add(new
							// Settings(katText6.getText().toString(),0));
							listAdapte6.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText6.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText6.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView6;
			} else if (position == 9) {
				slideView7 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView7.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView7.findViewById(R.id.tablor);
				addKate3 = (Button) slideView7.findViewById(R.id.addkamera);
				katText7 = ((EditText) slideView7
						.findViewById(R.id.kameramodell));
				myListView7 = (ListView) slideView7
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.filter));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte7 = new SettingsArrayAdapter(mContext, planets4, 79);
				myListView7.setAdapter(listAdapte7);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView7
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETFIL,
														texti.getText()
																.toString());
												readDB();
												listAdapte7
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETFIL,
														texti.getText()
																.toString());
												readDB();
												listAdapte7 = new SettingsArrayAdapter(
														mContext, planets4, 79);
												myListView7
														.setAdapter(listAdapte7);
												listAdapte7
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText7.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planets4.size(); i++) {
							vorhanden = planets4.get(i).getName().toString()
									.equals(katText7.getText().toString());
							if (vorhanden) {
								i = (planets4.size() - 1);
							}
						}
						if (vorhanden
								|| katText7.getText().toString().length() == 0
								|| katText7.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETFIL, katText7.getText()
									.toString(), 1);
							readDB();
							katText7.setText("");
							listAdapte7 = new SettingsArrayAdapter(mContext,
									planets4, 79);
							myListView7.setAdapter(listAdapte7);
							// planets4.add(new
							// Settings(katText7.getText().toString(),0));
							listAdapte7.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText7.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText7.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView7;
			} else if (position == 10) {
				slideView8 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView8.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView8.findViewById(R.id.tablor);
				addKate3 = (Button) slideView8.findViewById(R.id.addkamera);
				katText8 = ((EditText) slideView8
						.findViewById(R.id.kameramodell));
				myListView8 = (ListView) slideView8
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.filter_vf));
				tablor3.setBackgroundResource(R.drawable.shaperedtable);
				listAdapte8 = new SettingsArrayAdapter(mContext, planet6, 88);
				myListView8.setAdapter(listAdapte8);

				// --------------

				tablor3.setPadding(4, 0, -2, 0);
				myListView8
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETFVF,
														texti.getText()
																.toString());
												readDB();
												listAdapte8
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETFVF,
														texti.getText()
																.toString());
												readDB();
												listAdapte8 = new SettingsArrayAdapter(
														mContext, planet6, 88);
												myListView8
														.setAdapter(listAdapte8);
												listAdapte8
														.notifyDataSetChanged();
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.deleted),
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
								katText8.getApplicationWindowToken(), 0);
						// HIER

						for (int i = 0; i < planet6.size(); i++) {
							vorhanden = planet6.get(i).getName().toString()
									.equals(katText8.getText().toString());
							if (vorhanden) {
								i = (planet6.size() - 1);
							}
						}
						if (vorhanden
								|| katText8.getText().toString().length() == 0
								|| katText8.getText().toString().trim()
										.length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETFVF, katText8.getText()
									.toString(), 1);
							readDB();
							katText8.setText("");
							listAdapte8 = new SettingsArrayAdapter(mContext,
									planet6, 88);
							myListView8.setAdapter(listAdapte8);
							// planet6.add(new
							// Settings(katText8.getText().toString(),0));
							listAdapte8.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
						}

						// --------------
					}
				});
				katText8.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									katText8.getApplicationWindowToken(), 0);
							return true;
						}
						return false;
					}
				});

				myView = slideView8;
			} else if (position == 13) {
				slideView9 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView9.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView9.findViewById(R.id.tablor);
				addKate3 = (Button) slideView9.findViewById(R.id.addkamera);
				katText9 = ((EditText) slideView9
						.findViewById(R.id.kameramodell));
				myListView9 = (ListView) slideView9
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.flash
						));
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETBLI,
														texti.getText()
																.toString());
												readDB();
												listAdapte9
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETBLI,
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
														getString(R.string.deleted),
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
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETBLI, katText9.getText()
									.toString(), 1);
							readDB();
							katText9.setText("");
							listAdapte9 = new SettingsArrayAdapter(mContext,
									planets5, 56);
							myListView9.setAdapter(listAdapte9);
							// planets5.add(new
							// Settings(katText9.getText().toString(),0));
							listAdapte9.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
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
			} else if (position == 14) {
				slideView10 = inflater.inflate(R.layout.settingsauswahl, null,
						false);

				freecell3 = (TextView) slideView10.findViewById(R.id.freecell);
				tablor3 = (TableLayout) slideView10.findViewById(R.id.tablor);
				addKate3 = (Button) slideView10.findViewById(R.id.addkamera);
				katText10 = ((EditText) slideView10
						.findViewById(R.id.kameramodell));
				myListView10 = (ListView) slideView10
						.findViewById(android.R.id.list);

				freecell3.setText(getString(R.string.flash_correction));
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
										.setText(getString(R.string.delete_entry));
								editButton
										.setText(getString(R.string.make_default));
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
														DB.MY_DB_TABLE_SETKOR,
														texti.getText()
																.toString());
												readDB();
												listAdapte10
														.notifyDataSetChanged();

												editor1.commit();
												Toast.makeText(
														mContext,
														getString(R.string.default_saved),
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
														DB.MY_DB_TABLE_SETKOR,
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
														getString(R.string.deleted),
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
									getString(R.string.empty_or_existing_entry),
									Toast.LENGTH_SHORT).show();
						} else {
							writeDB(DB.MY_DB_TABLE_SETKOR, katText10.getText()
									.toString(), 1);
							readDB();
							katText10.setText("");
							listAdapte10 = new SettingsArrayAdapter(mContext,
									planet7, 17);
							myListView10.setAdapter(listAdapte10);
							// planet7.add(new
							// Settings(katText10.getText().toString(),0));
							listAdapte10.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(),
									getString(R.string.entry_saved),
									Toast.LENGTH_SHORT).show();
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

			else if (position == 11) {
				View va = inflater.inflate(R.layout.settingsauswahl, null,
						false);
				if (true) {
					freecell1 = (TextView) va.findViewById(R.id.freecell);
					tablor1 = (TableLayout) va.findViewById(R.id.tablor);
					myList1 = (ListView) va.findViewById(android.R.id.list);
					addKate1 = (Button) va.findViewById(R.id.addkamera);
					Kat1 = (EditText) va.findViewById(R.id.kameramodell);
					freecell1.setText(getString(R.string.film_formats));
					tablor1.setBackgroundResource(R.drawable.shapegreentable);
					tablor1.setPadding(4, 0, -2, 0);
					listAdapter1 = new SettingsArrayAdapter(mContext,
							aplanets1, 19);
					myList1.setAdapter(listAdapter1);
					myList1.setOnItemLongClickListener(new OnItemLongClickListener() {
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
									.setText(getString(R.string.delete_entry));
							editButton.setText(getString(R.string.make_default));
							editButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LinearLayout lins = (LinearLayout) arg1;
											TextView texti = (TextView) lins
													.getChildAt(0);
											makedefaultDB(DB.MY_DB_TABLE_SETFF,
													texti.getText().toString());
											readDB();
											listAdapter1.notifyDataSetChanged();

											Toast.makeText(mContext,
													getString(R.string.default_saved),
													Toast.LENGTH_SHORT).show();
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
											deletefromDB(DB.MY_DB_TABLE_SETFF,
													texti.getText().toString());
											readDB();
											listAdapter1 = new SettingsArrayAdapter(
													mContext, aplanets1, 19);
											myList1.setAdapter(listAdapter1);
											listAdapter1.notifyDataSetChanged();
											Toast.makeText(
													getApplicationContext(),
													getString(R.string.deleted),
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
						}
					});
					addKate1.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									addKate1.getApplicationWindowToken(), 0);
							boolean vorhanden = false;
							for (int i = 0; i < aplanets1.size(); i++) {
								vorhanden = aplanets1.get(i).getName()
										.toString()
										.equals(Kat1.getText().toString());
								if (vorhanden) {
									i = (aplanets1.size() - 1);
								}
							}
							if (vorhanden
									|| Kat1.getText().toString().length() == 0
									|| Kat1.getText().toString().trim()
											.length() == 0) {
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.empty_or_existing_entry),
										Toast.LENGTH_SHORT).show();
							} else {
								writeDB(DB.MY_DB_TABLE_SETFF, Kat1.getText()
										.toString(), 1);
								readDB();
								Kat1.setText("");
								listAdapter1 = new SettingsArrayAdapter(
										mContext, aplanets1, 19);
								myList1.setAdapter(listAdapter1);
								// aplanets1.add(new
								// Settings(Kat1.getText().toString(),0));
								listAdapter1.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(),
										getString(R.string.entry_saved),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
					Kat1.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if ((event.getAction() == KeyEvent.ACTION_DOWN)
									&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										Kat1.getApplicationWindowToken(), 0);
								return true;
							}
							return false;
						}
					});
				}
				myView = va;
				// ((ViewPager) view).addView(va);

			} else if (position == 12) {
				View vg = inflater.inflate(R.layout.settingsauswahl, null,
						false);
				if (true) {
					freecell7 = (TextView) vg.findViewById(R.id.freecell);
					tablor7 = (TableLayout) vg.findViewById(R.id.tablor);
					myList7 = (ListView) vg.findViewById(android.R.id.list);
					addKate7 = (Button) vg.findViewById(R.id.addkamera);
					Kat7 = (EditText) vg.findViewById(R.id.kameramodell);
					freecell7.setText(getString(R.string.film_speed));
					tablor7.setBackgroundResource(R.drawable.shapegreentable);
					tablor7.setPadding(4, 0, -2, 0);
					listAdapter7 = new SettingsArrayAdapter(mContext,
							aplanets7, 76);
					myList7.setAdapter(listAdapter7);
					myList7.setOnItemLongClickListener(new OnItemLongClickListener() {
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
									.setText(getString(R.string.delete_entry));
							editButton.setText(getString(R.string.make_default));
							editButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LinearLayout lins = (LinearLayout) arg1;
											TextView texti = (TextView) lins
													.getChildAt(0);
											makedefaultDB(
													DB.MY_DB_TABLE_SETEMP,
													texti.getText().toString());
											readDB();
											listAdapter7.notifyDataSetChanged();
											Toast.makeText(mContext,
													getString(R.string.default_saved),
													Toast.LENGTH_SHORT).show();
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
											deletefromDB(DB.MY_DB_TABLE_SETEMP,
													texti.getText().toString());
											readDB();
											listAdapter7 = new SettingsArrayAdapter(
													mContext, aplanets7, 76);
											myList7.setAdapter(listAdapter7);
											listAdapter7.notifyDataSetChanged();
											Toast.makeText(
													getApplicationContext(),
													getString(R.string.deleted),
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
						}
					});
					addKate7.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									addKate7.getApplicationWindowToken(), 0);
							boolean vorhanden = false;
							for (int i = 0; i < aplanets7.size(); i++) {
								vorhanden = aplanets7.get(i).getName()
										.toString()
										.equals(Kat7.getText().toString());
								if (vorhanden) {
									i = (aplanets7.size() - 1);
								}
							}
							if (vorhanden
									|| Kat7.getText().toString().length() == 0
									|| Kat7.getText().toString().trim()
											.length() == 0) {
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.empty_or_existing_entry),
										Toast.LENGTH_SHORT).show();
							} else {
								writeDB(DB.MY_DB_TABLE_SETEMP, Kat7.getText()
										.toString(), 1);
								readDB();
								Kat7.setText("");
								listAdapter7 = new SettingsArrayAdapter(
										mContext, aplanets7, 76);
								myList7.setAdapter(listAdapter7);
								// aplanets7.add(new
								// Settings(Kat7.getText().toString(),0));
								listAdapter7.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(),
										getString(R.string.entry_saved),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
					Kat7.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if ((event.getAction() == KeyEvent.ACTION_DOWN)
									&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										Kat7.getApplicationWindowToken(), 0);
								return true;
							}
							return false;
						}
					});
				}
				myView = vg;
				// ((ViewPager) view).addView(vg);

			} else if (position == 15) {
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
					freecell6.setText(getString(R.string.processing));
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
							deleteButton
									.setText(getString(R.string.delete_entry));
							editButton.setText(getString(R.string.make_default));

							deleteButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LinearLayout lins = (LinearLayout) arg1;
											TextView texti = (TextView) lins
													.getChildAt(0);
											deletefromDB(DB.MY_DB_TABLE_SETSON,
													texti.getText().toString());
											readDB();
											listAdapter6 = new SettingsArrayAdapter(
													mContext, aplanets6, 67);
											myList6.setAdapter(listAdapter6);
											listAdapter6.notifyDataSetChanged();
											Toast.makeText(
													getApplicationContext(),
													getString(R.string.deleted),
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
											makedefaultDB(
													DB.MY_DB_TABLE_SETSON,
													texti.getText().toString());
											readDB();
											listAdapter6.notifyDataSetChanged();
											Toast.makeText(mContext,
													getString(R.string.default_saved),
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
										getString(R.string.empty_or_existing_entry),
										Toast.LENGTH_SHORT).show();
							} else {
								writeDB(DB.MY_DB_TABLE_SETSON, Kat6.getText()
										.toString(), 1);
								readDB();
								Kat6.setText("");
								listAdapter6 = new SettingsArrayAdapter(
										mContext, aplanets6, 67);
								myList6.setAdapter(listAdapter6);
								// aplanets6.add(new
								// Settings(Kat6.getText().toString(),0));
								listAdapter6.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(),
										getString(R.string.entry_saved),
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
				setFooterColor(0xFF000000);
			if (viewPager.getCurrentItem() == 1)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 2)
				setFooterColor(0xFFBB0000);
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

			return SlideNewSettings.CONTENT[position
					% SlideNewSettings.CONTENT.length];
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

	// Extra ArrayAdapter f�r die Camera Zellen (Diese beinhalten noch die
	// Objektive)
	private class CamArrayAdapter extends ArrayAdapter<Settings> {

		private LayoutInflater inflater;
		int nummer = 0;

		public CamArrayAdapter(Context context, ArrayList<Settings> planetList,
				int number) {
			super(context, R.layout.list_itemcam, R.id.listItemText, planetList);
			nummer = number;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Settings planet = (Settings) this.getItem(position);
			CheckBox checkBox;
			TextView textView;
			LinearLayout grid;
			LinearLayout grid2 = null;
			TextView testtext;

			convertView = inflater.inflate(R.layout.list_itemcam, null);
			convertView.setClickable(false);
			convertView.setFocusable(false);
			// Find the child views.
			textView = (TextView) convertView.findViewById(R.id.listItemText);
			checkBox = (CheckBox) convertView.findViewById(R.id.check);
			grid = (LinearLayout) convertView.findViewById(R.id.gridview);
			testtext = (TextView) inflater.inflate(R.layout.objektive, grid,
					false);

			grid.setClickable(false);
			grid.setFocusable(false);
			grid.setEnabled(false);
			grid.clearFocus();

			onCreateDBAndDBTabled();
			int kontrolle = 0;
			Cursor camBWCursor = myDB.rawQuery(
					"SELECT cam,bw FROM " + DB.MY_DB_TABLE_SETCAMBW
							+ " WHERE cam = '" + planet.getName() + "'", null);
			if (camBWCursor != null) {
				if (camBWCursor.moveToFirst()) {
					do {
						if ((kontrolle % 3) == 0 || kontrolle == 0) {
							grid2 = null;
							grid2 = (LinearLayout) inflater.inflate(
									R.layout.objektivegrid, grid, false);
							grid.addView(grid2);
						}
						testtext = (TextView) inflater.inflate(
								R.layout.objektive, grid2, false);
						testtext.setText(camBWCursor.getString(camBWCursor
								.getColumnIndex("bw")));
						grid2.addView(testtext);
						kontrolle++;
					} while (camBWCursor.moveToNext());
				}
			} else {
				grid.setVisibility(LinearLayout.GONE);
			}
			myDB.close();
			camBWCursor.close();
			stopManagingCursor(camBWCursor);
			checkBox.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					Settings planet = (Settings) cb.getTag();
					planet.setChecked(cb.isChecked());

					int value = 0;
					if (cb.isChecked() == true) {
						value = 1;
					}

					if (nummer == 0) {
						editfromDB(DB.MY_DB_TABLE_SETCAM, planet.name, value);
					}
				}
			});

			// Tag the CheckBox with the Planet it is displaying, so that we can
			// access the planet in onClick() when the CheckBox is toggled.
			checkBox.setTag(planet);

			// Display planet data
			if (planet.isChecked() == 1) {
				checkBox.setChecked(true);
			} else if (planet.isChecked() == 0) {
				checkBox.setChecked(false);
			}
			textView.setText(planet.getName());

			settings = PreferenceManager.getDefaultSharedPreferences(mContext);

			// ArrayList<String> camBWs = getListCAMBW(planet.getName());
			// ArrayAdapter camBWsAdapter = new
			// ArrayAdapter<String>(getApplicationContext(), R.layout.objektive,
			// camBWs);

			// testtext.setText("TEST");
			// grid.addView(testtext);

			textView.setTextColor(0xFF000000);

			return convertView;
		}

	}

	private class SettingsArrayAdapter extends ArrayAdapter<Settings> {

		private LayoutInflater inflater;
		int nummer = 0;

		public SettingsArrayAdapter(Context context,
				ArrayList<Settings> planetList, int number) {
			super(context, R.layout.list_item, R.id.listItemText, planetList);
			nummer = number;
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Planet to display
			Settings planet = (Settings) this.getItem(position);

			// The child views in each row.
			CheckBox checkBox;
			TextView textView;

			// Create a new row view
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);

				// Find the child views.
				textView = (TextView) convertView
						.findViewById(R.id.listItemText);
				checkBox = (CheckBox) convertView.findViewById(R.id.check);

				// Optimization: Tag the row with it's child views, so we don't
				// have to
				// call findViewById() later when we reuse the row.
				convertView.setTag(new SettingsViewHolder(textView, checkBox));

				// If CheckBox is toggled, update the planet it is tagged with.
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
							editfromDB(DB.MY_DB_TABLE_SETFF, planet.name, value);
						} else if (nummer == 1337) {
							editfromDB(DB.MY_DB_TABLE_SETBW, planet.name, value);
						} else if (nummer == 51) {
							editfromDB(DB.MY_DB_TABLE_SETNM, planet.name, value);
						} else if (nummer == 79) {
							editfromDB(DB.MY_DB_TABLE_SETFIL, planet.name,
									value);
						} else if (nummer == 56) {
							editfromDB(DB.MY_DB_TABLE_SETBLI, planet.name,
									value);
						} else if (nummer == 67) {
							editfromDB(DB.MY_DB_TABLE_SETSON, planet.name,
									value);
						} else if (nummer == 76) {
							editfromDB(DB.MY_DB_TABLE_SETEMP, planet.name,
									value);
						} else if (nummer == 90) {
							editfromDB(DB.MY_DB_TABLE_SETFOK, planet.name,
									value);
						} else if (nummer == 50) {
							editfromDB(DB.MY_DB_TABLE_SETBLE, planet.name,
									value);
						} else if (nummer == 34) {
							editfromDB(DB.MY_DB_TABLE_SETZEI, planet.name,
									value);
						} else if (nummer == 39) {
							editfromDB(DB.MY_DB_TABLE_SETMES, planet.name,
									value);
						} else if (nummer == 42) {
							editfromDB(DB.MY_DB_TABLE_SETPLU, planet.name,
									value);
						} else if (nummer == 63) {
							editfromDB(DB.MY_DB_TABLE_SETMVF, planet.name,
									value);
						} else if (nummer == 88) {
							editfromDB(DB.MY_DB_TABLE_SETFVF, planet.name,
									value);
						} else if (nummer == 17) {
							editfromDB(DB.MY_DB_TABLE_SETKOR, planet.name,
									value);
						}
					}
				});
			}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call
				// findViewById().
				SettingsViewHolder viewHolder = (SettingsViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			// Tag the CheckBox with the Planet it is displaying, so that we can
			// access the planet in onClick() when the CheckBox is toggled.
			checkBox.setTag(planet);

			// Display planet data
			if (planet.isChecked() == 1) {
				checkBox.setChecked(true);
			} else if (planet.isChecked() == 0) {
				checkBox.setChecked(false);
			}
			textView.setText(planet.getName());
			settings = PreferenceManager.getDefaultSharedPreferences(mContext);
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

	private class SettingsArrayAdapterSpec extends ArrayAdapter<Settings> {

		private LayoutInflater inflater;
		@SuppressWarnings("unused")
		int nummer = 0;
		String camera;

		public SettingsArrayAdapterSpec(Context context,
				ArrayList<Settings> planetList, int number, String cam) {
			super(context, R.layout.list_item, R.id.listItemText, planetList);
			nummer = number;
			camera = cam;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Settings planet = (Settings) this.getItem(position);
			CheckBox checkBox;
			final TextView textView;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);

				textView = (TextView) convertView
						.findViewById(R.id.listItemText);
				checkBox = (CheckBox) convertView.findViewById(R.id.check);
				convertView.setTag(new SettingsViewHolder(textView, checkBox));

				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Settings planet = (Settings) cb.getTag();
						planet.setChecked(cb.isChecked());

						@SuppressWarnings("unused")
						int value = 0;
						if (cb.isChecked() == true) {
							value = 1;

							ContentValues args = new ContentValues();
							args.put("cam", camera);
							args.put("bw", planet.getName());
							onCreateDBAndDBTabled();
							myDB.insert(DB.MY_DB_TABLE_SETCAMBW, null, args);
							myDB.close();
							readDB();
							viewPager = (ViewPager) findViewById(R.id.viewPager);
							MyPagerAdapter adapter = new MyPagerAdapter(
									mContext);
							viewPager.setAdapter(adapter);
							mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
							mIndicator.setViewPager(viewPager);
							viewPager.setCurrentItem(1, false);

						} else {
							onCreateDBAndDBTabled();
							myDB.delete(DB.MY_DB_TABLE_SETCAMBW,
									"cam = '" + camera + "' AND bw = '"
											+ planet.getName() + "'", null);
							myDB.close();
							readDB();
							viewPager = (ViewPager) findViewById(R.id.viewPager);

							MyPagerAdapter adapter = new MyPagerAdapter(
									mContext);
							viewPager.setAdapter(adapter);
							mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
							mIndicator.setViewPager(viewPager);
							viewPager.setCurrentItem(1, false);
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

			ArrayList<String> brennweiten = getListCAMBW(camera);
			if (brennweiten.contains(planet.getName())) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}

			textView.setText(planet.getName());
			settings = PreferenceManager.getDefaultSharedPreferences(mContext);
			if (planet.getName() == settings.getString("KamDef", "")
					|| planet.getName().equals(
							settings.getString("FormatDef", ""))
					|| planet.getName().equals(
							settings.getString("EmpfDef", ""))
					|| planet.getName().equals(
							settings.getString("MakroDef", ""))
					|| planet.getName().equals(
							settings.getString("FilterDef", ""))
					|| planet.getName().equals(
							settings.getString("BlitzDef", ""))
					|| planet.getName().equals(
							settings.getString("SonderDef", ""))) {
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

	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settingsmenu, menu);
		return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.opt_settingsToFactoryDefaults) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(
					"M\u00F6chten Sie wirklich alle Einstellungen zur\u00FCcksetzen ?")
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
		} else if (item.getItemId() == R.id.opt_loadSetOfSettings) {
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
			setsave1.setText(getString(R.string.load));
			setSetButtonColor(setone1, settwo1, setthree1, setfour1);
			setone1.setText(settings.getString("SetButtonOne", getString(R.string.set_default))); 
			settwo1.setText(settings.getString("SetButtonTwo", getString(R.string.set_two)));
			setthree1.setText(settings.getString("SetButtonThree", getString(R.string.set_three)));
			setfour1.setText(settings.getString("SetButtonFour", getString(R.string.set_four)));
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
							getString(R.string.set_loaded),
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
		} else if (item.getItemId() == R.id.opt_createNewSetOfSettings) {
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
			setone.setText(settings.getString("SetButtonOne", getString(R.string.set_default)));
			settwo.setText(settings.getString("SetButtonTwo", getString(R.string.set_two)));
			setthree.setText(settings.getString("SetButtonThree", getString(R.string.set_three)));
			setfour.setText(settings.getString("SetButtonFour", getString(R.string.set_four)));
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
								getString(R.string.set_not_saved),
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
								getString(R.string.set_not_saved),
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
								getString(R.string.set_not_saved),
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
								getString(R.string.set_not_saved),
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
								getString(R.string.empty_name_not_allowed),
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
			this.dialog.setMessage(getString(R.string.save_set));
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
					getString(R.string.set_saved),
					Toast.LENGTH_SHORT).show();

		}

		protected Boolean doInBackground(final String... args) {
			// try {
			myDBSet = mContext.openOrCreateDatabase(thisset,
					Context.MODE_PRIVATE, null);
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETCAMBW
					+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETCAM
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETFF
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETEMP
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETBW
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETNM
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETFIL
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETBLI
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETSON
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETTYP
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETFOK
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETBLE
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETZEI
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETMES
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETPLU
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETMAK
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETMVF
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETFVF
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETKOR
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETMVF2
					+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
					+ ";");
			myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
					+ DB.MY_DB_TABLE_SETFVF2
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

			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETTYP);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETCAM);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETCAMBW);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETFIL);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETEMP);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETNM);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETSON);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETBLI);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETBW);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETFF);

			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETFOK);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETBLE);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETZEI);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETMES);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETPLU);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETMAK);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETMVF);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETFVF);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETMVF2);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETFVF2);
			myDBSet.execSQL("DELETE FROM " + DB.MY_DB_TABLE_SETKOR);

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
			this.dialog.setMessage(getString(R.string.reset));
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

				DB.getDB().createOrRebuildSettingsTable(mContext);

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