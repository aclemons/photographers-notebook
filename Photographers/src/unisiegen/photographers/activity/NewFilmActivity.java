package unisiegen.photographers.activity;

/**
 * In dieser Activity kann ein neuer Film angelegt werden. Titel, Vorschaubild etc.
 */

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class NewFilmActivity extends Activity {

	/*
	 * Sonstige Variablen
	 */
	SharedPreferences settings;
	Context mContext;
	private static final String[] puContent = new String[] {
			"Hier k\u00F6nnen Sie einen Filmtitel ausw\u00E4hlen oder ihn automatisch festlegen lassen.",
			"Die zuvor ausgew\u00E4hlten Kameras und Filmeinstellungen sind hier selektierbar.",
			"Hier w\u00E4hlen Sie die zuvor angelegten Kameras und Filmeinstellungen aus.",
			"Auch hier k\u00F6nnen Sie diese durch Tippen auf die Men\u00FC-Taste \u00E4ndern.",
			"Wenn Sie die Auswahl getroffen haben, tippen Sie bitte auf Film beginnen." };
	int design = 0;
	int camdef = 0;
	int ffdef = 0;
	int ssdef = 0;
	int emdef = 0;
	int tydef = 0;
	Integer contentIndex = 0;
	byte[] pic, nopic;
	boolean mPreviewRunning = false;

	/*
	 * Spinner Variablen
	 */
	ArrayList<String> listCamera, listFF, listSS, listSSS, listEM, listTY;
	ArrayAdapter<String> adapterCamera, adapterFF, adapterSS, adapterSSS,
			adapterEM, adapterTY;
	HashMap<String, Integer> defCheck0, defCheck1, defCheck2, defCheck3;

	/*
	 * Interface Variablen
	 */
	TextView tv1, tv2, weiter, close, newFilm, vorschau, cancel;
	EditText filmnotiz;
	PopupWindow pw;
	Spinner spinnerCamera, spinnerFF, spinnerSS, spinnerSSS, spinnerEM,
			spinnerTY;
	ToggleButton titleButton;
	EditText titleText;
	Camera mCamera;

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
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle) LifeCycle-Methoden
	 */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfilmone);
		mContext = this;
		filmnotiz = (EditText) findViewById(R.id.filmnotiz);
		cancel = (Button) findViewById(R.id.cancelAll);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		vorschau = (Button) findViewById(R.id.vorschau);
		vorschau.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(),
						PreviewDemo.class);
				startActivityForResult(myIntent, 0);

			}
		});
		newFilm = (Button) findViewById(R.id.newAll);
		newFilm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("Foto", "showFilm");
				try {
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("Title", titleText.getText().toString());
					editor.putString("FilmNotiz", filmnotiz.getText()
							.toString());
					editor.putString("Datum", android.text.format.DateFormat
							.format("dd.MM.yyyy", new java.util.Date())
							.toString());
					editor.putString("Kamera", spinnerCamera.getSelectedItem()
							.toString());
					editor.putString("Filmformat", spinnerFF.getSelectedItem()
							.toString());
					editor.putString("Empfindlichkeit", spinnerEM
							.getSelectedItem().toString());
					editor.putString("Filmtyp", spinnerTY.getSelectedItem()
							.toString());
					editor.putString("Sonder1", spinnerSS.getSelectedItem()
							.toString());
					editor.putString("Sonder2", spinnerSSS.getSelectedItem()
							.toString());
					editor.putInt("BildNummerToBegin", 1);
					editor.putBoolean("EditMode", false);
					editor.commit();
					Log.v("Check", "Check if Bild vorhanden : " + (pic == null));
					Intent myIntent = new Intent(getApplicationContext(),
							SlideNewPic.class);
					if (pic != null) {
						myIntent.putExtra("image", pic);
					} else {
						Bitmap bm = BitmapFactory.decodeResource(
								getApplicationContext().getResources(),
								R.drawable.nopic);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
						nopic = baos.toByteArray();
						myIntent.putExtra("image", nopic);
					}
					finish();
					startActivityForResult(myIntent, 1);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Fehlerhafte Eingabe!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	@Override
	protected void onResume() {
		super.onResume();
		contentIndex = 0;
		MY_DB_NAME = settings.getString("SettingsTable", "Foto");
		defCheck0 = new HashMap<String, Integer>();
		defCheck1 = new HashMap<String, Integer>();
		defCheck2 = new HashMap<String, Integer>();
		defCheck3 = new HashMap<String, Integer>();
		readDB();
		titleText = (EditText) findViewById(R.id.texttitle);
		titleButton = (ToggleButton) findViewById(R.id.toggletitle);
		titleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Date dt = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				titleText.setText(df.format(dt) + " Film");
			}
		});
		spinnerCamera = (Spinner) findViewById(R.id.spinnerCamera);
		spinnerFF = (Spinner) findViewById(R.id.spinnerFF);
		spinnerSS = (Spinner) findViewById(R.id.spinnerSS);
		spinnerSSS = (Spinner) findViewById(R.id.spinnerSSS);
		spinnerEM = (Spinner) findViewById(R.id.spinnerEM);
		spinnerTY = (Spinner) findViewById(R.id.spinnerTY);
		adapterCamera = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listCamera);
		adapterFF = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listFF);
		adapterSS = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listSS);
		adapterSSS = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listSSS);
		adapterEM = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listEM);
		adapterTY = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listTY);
		spinnerCamera.setAdapter(adapterCamera);
		spinnerFF.setAdapter(adapterFF);
		spinnerSS.setAdapter(adapterSS);
		spinnerSSS.setAdapter(adapterSSS);
		spinnerEM.setAdapter(adapterEM);
		spinnerTY.setAdapter(adapterTY);
		spinnerCamera.setSelection(camdef);
		spinnerFF.setSelection(ffdef);
		spinnerSS.setSelection(ssdef);
		spinnerEM.setSelection(emdef);
		spinnerTY.setSelection(tydef);
		if (settings.getInt("FIRSTSTART", 0) == 1) {
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.post(new Runnable() {
				public void run() {
					popupmenue();
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("FIRSTSTART", 3);
					editor.commit();
				}
			});
		} else if (settings.getInt("FIRSTSTART", 0) == 2) {
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.post(new Runnable() {
				public void run() {
					popupmenue();
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("FIRSTSTART", 8);
					editor.commit();
				}
			});
		}
	}

	/*
	 * Datenbank Methoden
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

	private void readDB() {
		onCreateDBAndDBTabled();
		listCamera = new ArrayList<String>();
		listFF = new ArrayList<String>();
		listSS = new ArrayList<String>();
		listSSS = new ArrayList<String>();
		listEM = new ArrayList<String>();
		listTY = new ArrayList<String>();
		int number = 0;
		Cursor c = myDB.rawQuery("SELECT name,value FROM " + MY_DB_TABLE_SETCAM
				+ " WHERE value = '1'", null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					if (c.getString(c.getColumnIndex("name")).equals(
							settings.getString("KamDef", ""))) {
						camdef = number;
					}
					listCamera.add(c.getString(c.getColumnIndex("name")));
					number++;
				} while (c.moveToNext());
			} else {
				listCamera.add("Keine Auswahl");
			}
		}
		number = 0;
		Cursor ca = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETFF + " WHERE value = '1'", null);
		if (ca != null) {
			if (ca.moveToFirst()) {
				do {
					if (ca.getInt(ca.getColumnIndex("def")) == 1) {
						ffdef = number;
					}
					listFF.add(ca.getString(ca.getColumnIndex("name")));
					number++;
				} while (ca.moveToNext());
			} else {
				listFF.add("Keine Auswahl");
			}
		}
		number = 0;
		Cursor cf = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETSON + " WHERE value = '1'", null);
		if (cf != null) {
			listSSS.add(" ");
			if (cf.moveToFirst()) {
				do {
					if (cf.getInt(cf.getColumnIndex("def")) == 1) {
						ssdef = number;
					}
					listSS.add(cf.getString(cf.getColumnIndex("name")));
					listSSS.add(cf.getString(cf.getColumnIndex("name")));
					number++;
				} while (cf.moveToNext());
			} else {
				listSS.add("Keine Auswahl");
			}
		}
		number = 0;
		Cursor cg = myDB.rawQuery("SELECT name,value,def FROM "
				+ MY_DB_TABLE_SETEMP + " WHERE value = '1'", null);
		if (cg != null) {
			if (cg.moveToFirst()) {
				do {
					if (cg.getInt(cg.getColumnIndex("def")) == 1) {
						emdef = number;
					}
					listEM.add(cg.getString(cg.getColumnIndex("name")));
					number++;
				} while (cg.moveToNext());
			} else {
				listEM.add("Keine Auswahl");
			}
		}
		number = 0;
		Cursor ch = myDB.rawQuery("SELECT name,value FROM "
				+ MY_DB_TABLE_SETTYP + " WHERE value = '1'", null);
		if (ch != null) {
			if (ch.moveToFirst()) {
				do {
					listTY.add(ch.getString(ch.getColumnIndex("name")));
					number++;
				} while (ch.moveToNext());
			}
		}
		number = 0;
		myDB.close();
		c.close();
		ca.close();
		cf.close();
		cg.close();
		ch.close();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent) Bild übergabe (Wenn man ein Vorschau bild macht,
	 * geschieht dies in einer neuen Popup Activity, ist dies fertig übergibt
	 * diese Activity als Rückgabe Wert das Bild, welches zusammen mit den
	 * anderen Details zum Film gespeichert wird.
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			switch (resultCode) {
			case 1277:
				Bundle bundle = data.getExtras();
				pic = (byte[]) bundle.get("image");
				Log.v("Check",
						"SAVE : "
								+ BitmapFactory.decodeByteArray(pic, 0,
										pic.length));

				vorschau.setTextColor(0xFF00BB00);

				break;
			}
			break;
		}
	}

	/*
	 * Hilfe Methode
	 */

	public void popupmenue() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layoutOwn1 = inflater.inflate(R.layout.firstpopup,
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
					if (contentIndex == 3) {
						openOptionsMenu();
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

}