package unisiegen.photographers.activity;

/**
 * Activity für die Auswahl eines Film (View die beim Start angezeigt wird)
 * Hier kann man einen bestehenden Film auswählen, bearbeiten und betrachten
 * Oder man kann einen neuen Film starten
 * 
 * Hier werden einige Methoden genauer erklärt, viele der Methoden kommen in anderen Activitys in selber Art 
 * und Weise wieder vor und werden dort dann nicht mehr ausführlich besprochen.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import unisiegen.photographers.export.Export;
import unisiegen.photographers.export.Film;
import unisiegen.photographers.export.Film.BildObjekt;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class FilmAuswahlActivity extends Activity {

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
	final static String MY_DB_TABLE_SETCAMBW = "SettingsCameraBW";
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
	 * User-Interface Elemente
	 */
	TableLayout tableout;
	TableLayout table;
	LinearLayout scolli;
	TextView tv1, tv2;
	ListView myList;
	Button weiter, close;
	PopupWindow pw;

	/*
	 * Sonstige Variablen
	 */
	int design = 0;
	SharedPreferences settings;
	HashMap<String, Integer> FilmNummern;
	ArrayList<Films> listItems;
	ArrayAdapter<Films> adapter;
	public static Context mContext;
	public static LayoutInflater inflater;
	public Integer contentIndex = 0;
	public static int WochenSoll;
	public static int gesamt;
	private static final String[] puContent = new String[] {
			"Herzlich willkommen im Photographers Notebook!",
			"Zur Einf\u00FChrung zeigen wir Ihnen die wichtigsten Funktionen der App.",
			"Wenn Sie auf den Men\u00FC-Button tippen, k\u00F6nnen Sie die Einstellungen zu dieser App finden und die ersten Einstellungen t\u00E4tigen.",
			"Sind alle Einstellungen vorgenommen, starten Sie Ihren ersten Film, indem Sie den Button \"Neuer Film\" antippen.",
			"Sp\u00E4ter k\u00F6nnen Sie den Film auf dieser Seite wieder aufrufen und bei Bedarf fortf\u00FChren." };

	/*
	 * @see android.app.Activity#onResume() Lifecycle-Methoden
	 */

	@Override
	protected void onResume() {
		super.onResume();
		ImageView image = (ImageView) findViewById(R.id.image);
		myList = (ListView) findViewById(android.R.id.list);
		TextView pics = (TextView) findViewById(R.id.picanzahl);
		contentIndex = 0;
		if (settings.getInt("FIRSTSTART", 0) == 1) {
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.post(new Runnable() {
				public void run() {
					popupmenue();
				}
			});
		}
		listItems = new ArrayList<Films>();
		int gesamtPics = 0;
		onCreateDBAndDBNumber();
		Cursor c = myDBNummer.rawQuery(
				"SELECT title,camera,datum,bilder,pic FROM "
						+ MY_DB_TABLE_NUMMER, null);
		gesamtPics = 0;
		int nummern = 0;
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					listItems.add(new Films(c.getString(c
							.getColumnIndex("title")), c.getString(c
							.getColumnIndex("datum")), c.getString(c
							.getColumnIndex("camera")), c.getInt(c
							.getColumnIndex("bilder")) + " Bilder", c
							.getString(c.getColumnIndex("pic"))));
					gesamtPics += c.getInt(c.getColumnIndex("bilder"));
					FilmNummern.put(c.getString(c.getColumnIndex("title")),
							nummern);
				} while (c.moveToNext());
			}
		}
		if (gesamtPics == 0) {
			myList.setVisibility(ListView.GONE);
			image.setVisibility(ImageView.VISIBLE);
		} else {
			myList.setVisibility(ListView.VISIBLE);
			image.setVisibility(ImageView.GONE);
		}
		myDBNummer.close();
		c.close();
		stopManagingCursor(c);
		pics.setText(gesamtPics + " Bilder");
		adapter = new FilmsArrayAdapter(mContext, listItems, 1);
		myList.setOnItemClickListener(notlongClickListener);
		myList.setOnItemLongClickListener(longClickListener);
		myList.setAdapter(adapter);

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filmauswahl);
		inflater = getLayoutInflater();
		mContext = this;
		FilmNummern = new HashMap<String, Integer>();
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		if (settings.getInt("FIRSTSTART", 0) == 0) {
			new resetSettings().execute();
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("FIRSTSTART", 1);
			editor.commit();
		}
		Button newFilm = (Button) findViewById(R.id.newFilm);
		newFilm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(),
						NewFilmActivity.class);
				// Intent myIntent = new Intent(getApplicationContext(),
				// SlideNewPic.class);
				startActivityForResult(myIntent, 0);
			}
		});
	}

	/*
	 * Datenbank-Methoden (Die SQL Datenbanken werden geöffnet bzw. falls sie
	 * noch nicht entstehen, werden sie erstellt)
	 */

	private void onCreateDBAndDBNumber() {
		myDBNummer = mContext.openOrCreateDatabase(MY_DB_NUMMER,
				Context.MODE_PRIVATE, null);
		myDBNummer
				.execSQL("CREATE TABLE IF NOT EXISTS "
						+ MY_DB_TABLE_NUMMER
						+ " (title varchar(100) primary key, value integer,camera varchar(100), datum varchar(100), bilder integer, pic varchar(999))"
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

	/*
	 * Hilfs-Klasse für die Anzeige der Filmobjekte in einer Listen-Zeile (Die
	 * Attribute die Angezeigt werden, werden für jeden Film als ein Objekt
	 * gespeichert)
	 */

	private static class Films {
		private String name = "";
		private String time = "";
		private String cam = "";
		private String pics = "";
		private Bitmap bild;

		public Films(String name, String time, String cam, String pics,
				String Bild) {
			this.name = name;
			this.time = time;
			this.cam = cam;
			this.pics = pics;
			this.bild = BitmapFactory.decodeByteArray(
					Base64.decode(Bild, Base64.DEFAULT), 0,
					(Base64.decode(Bild, Base64.DEFAULT)).length);
		}

		public String getName() {
			return name;
		}

		public String getTime() {
			return time;
		}

		public String getCam() {
			return cam;
		}

		public String getPics() {
			return pics;
		}

		public Bitmap getBild() {
			return bild;
		}
	}

	/*
	 * ViewHolder für die Filmobjekte der Einzelenden Listen-Zeilen (Zum
	 * Speichern der Views um sie nicht immer erneut zu definieren)
	 */

	private static class FilmsViewHolder {
		private TextView textViewTime;
		private TextView textViewName;
		private TextView textViewCam;
		private TextView textViewPics;
		private ImageView imageViewBild;

		public FilmsViewHolder(TextView textViewname, TextView textViewtime,
				TextView textViewcam, TextView textViewpics, ImageView Bilds) {
			this.textViewTime = textViewtime;
			this.textViewName = textViewname;
			this.textViewCam = textViewcam;
			this.textViewPics = textViewpics;
			this.imageViewBild = Bilds;
		}

		public TextView getTextViewName() {
			return textViewName;
		}

		public TextView getTextViewTime() {
			return textViewTime;
		}

		public TextView getTextViewCam() {
			return textViewCam;
		}

		public TextView getTextViewPics() {
			return textViewPics;
		}

		public ImageView getBildView() {
			return imageViewBild;
		}
	}

	/*
	 * Custom Array-Adapter für Custom-Listen-Zeilen (Es handelt sich um eine
	 * Liste, welche Custom-"Zellen" zum Anzeigen verwendet, hierfür wird ein
	 * custom ArrayAdapter benötigt, der die Attribute den richtigen Stellen in
	 * den einzelnden Zellen zuordnet)
	 */

	private class FilmsArrayAdapter extends ArrayAdapter<Films> {

		private LayoutInflater inflater;
		@SuppressWarnings("unused")
		int nummer = 0;

		public FilmsArrayAdapter(Context context, ArrayList<Films> planetList,
				int number) {
			super(context, R.layout.sqltablecell, R.id.filmtitle, planetList);
			nummer = number;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Films planet = (Films) this.getItem(position);
			TextView textView;
			TextView textViewTime;
			TextView textViewCam;
			TextView textViewPics;
			ImageView imageViewBild;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.sqltablecell, null);
				textView = (TextView) convertView.findViewById(R.id.filmtitle);
				textViewTime = (TextView) convertView.findViewById(R.id.time);
				textViewCam = (TextView) convertView.findViewById(R.id.cam);
				textViewPics = (TextView) convertView.findViewById(R.id.fotos);
				imageViewBild = (ImageView) convertView
						.findViewById(R.id.imageview);
				convertView.setTag(new FilmsViewHolder(textView, textViewTime,
						textViewCam, textViewPics, imageViewBild));
			} else {
				FilmsViewHolder viewHolder = (FilmsViewHolder) convertView
						.getTag();
				textViewTime = viewHolder.getTextViewTime();
				textView = viewHolder.getTextViewName();
				textViewCam = viewHolder.getTextViewCam();
				textViewPics = viewHolder.getTextViewPics();
				imageViewBild = viewHolder.getBildView();
			}
			textViewTime.setText(planet.getTime());
			textView.setText(planet.getName());
			textViewCam.setText(planet.getCam());
			textViewPics.setText(planet.getPics());
			imageViewBild.setImageBitmap(planet.getBild());
			return convertView;
		}

	}

	/*
	 * Klicken auf eine Zeile (langer und kurzer klick)
	 */

	public OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// 4 child 2 kind
			LinearLayout lin = (LinearLayout) arg1;
			LinearLayout lins = (LinearLayout) lin.getChildAt(1);
			final TextView ids = (TextView) ((LinearLayout) lins.getChildAt(2))
					.getChildAt(0);

			Display display = ((WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			LayoutInflater inflaterOwn = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layoutOwn = inflaterOwn.inflate(R.layout.longclickwithexport,
					(ViewGroup) findViewById(R.id.testen), false);
			Button deleteButton = (Button) layoutOwn
					.findViewById(R.id.deletebutton);
			Button cancelButton = (Button) layoutOwn
					.findViewById(R.id.cancelbutton);
			Button exportButton = (Button) layoutOwn
					.findViewById(R.id.exportbutton);
			Button editButton = (Button) layoutOwn
					.findViewById(R.id.editbutton);
			deleteButton.setText("     Film l\u00F6schen     ");
			editButton.setText("     Film fortsetzen     ");

			exportButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					exportFilm(ids.getText().toString());
					pw.dismiss();
				}
			});

			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage(
							"Wollen Sie den Eintrag wirklich löschen ?")
							.setCancelable(false)
							.setPositiveButton("Ja",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											onCreateDBAndDBNumber();
											onCreateDBAndDBTabledFilm();

											myDBFilm.delete(MY_DB_FILM_TABLE,
													"filmtitle=?",
													new String[] { ids
															.getText()
															.toString() });

											myDBNummer.delete(
													MY_DB_TABLE_NUMMER,
													"title=?",
													new String[] { ids
															.getText()
															.toString() });
											Toast.makeText(
													getApplicationContext(),
													"Film gelöscht",
													Toast.LENGTH_SHORT).show();
											myDBFilm.close();
											myDBNummer.close();
											pw.dismiss();
											onResume();
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

			editButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences.Editor editor = settings.edit();
					onCreateDBAndDBNumber();
					Cursor c = myDBNummer.rawQuery(
							"SELECT title,camera,datum,bilder FROM "
									+ MY_DB_TABLE_NUMMER + " WHERE title = '"
									+ ids.getText().toString() + "'", null);
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
											+ ids.getText().toString() + "'",
									null);
					if (c1 != null) {
						int i = 0;
						if (c1.moveToFirst()) {
							do {
								editor.putString("Filmformat", c1.getString(c1
										.getColumnIndex("filmformat")));
								editor.putString(
										"Empfindlichkeit",
										c1.getString(c1
												.getColumnIndex("filmempfindlichkeit")));
								editor.putString("Filmtyp", c1.getString(c1
										.getColumnIndex("filmtyp")));
								editor.putString("Sonder1", c1.getString(c1
										.getColumnIndex("filmsonder")));
								editor.putString("Sonder2", c1.getString(c1
										.getColumnIndex("filmsonders")));
								if (Integer.valueOf(c1
										.getString(
												c1.getColumnIndex("picnummer"))
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
					pw.dismiss();
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
					(int) (height / 2), true);
			pw.setAnimationStyle(7);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
			return true;
		}
	};

	public OnItemClickListener notlongClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			LinearLayout lin = (LinearLayout) arg1;
			LinearLayout lins = (LinearLayout) lin.getChildAt(1);
			TextView ids = (TextView) ((LinearLayout) lins.getChildAt(2))
					.getChildAt(0);
			Intent myIntent = new Intent(getApplicationContext(),
					FilmSelectActivity.class);
			myIntent.putExtra("ID", ids.getText().toString());
			startActivityForResult(myIntent, 0);
		}
	};

	/*
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) Menü
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
	 * Popup für Tutorial
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

	public void exportFilm(String FilmID) {
		new prepareToExport(FilmID).execute();

	}

	public class resetSettings extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		public resetSettings() {
			dialog = new ProgressDialog(mContext);
		}

		protected void onPreExecute() {
			this.dialog.setMessage("...ersten Start vorbereiten...");
			this.dialog.setCancelable(false);
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
		}

		/*
		 * Es werden nochmal alle Tabellen in der SQL Datenbank neu erstellt und
		 * neu gefüllt... nicht sehr elegant :)
		 */
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
						+ " Values (" + null + ",'" + "ISO 25/15\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 40/17\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 50/18\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 64/19\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 100/21\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 125/22\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 160/23\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 200/24\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 320/26\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 400/27\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 640/29\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 800/30\u00b0" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 1000/31\u00b0" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 1600/33\u00b0" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
						+ " Values (" + null + ",'" + "ISO 3200/36\u00b0" + "','"
						+ 1 + "','" + 0 + "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'" + "Farbnegativ (I: CN)"
						+ "','" + 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'" + "Farbdia (I: CR)" + "','"
						+ 1 + "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'"
						+ "Schwarzwei\u00DF-Negativ (I: SW)" + "','" + 1 + "','" + 0
						+ "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'"
						+ "Schwarzwei\u00DF-Dia (I: SWR)" + "','" + 1 + "','" + 0
						+ "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
						+ " Values (" + null + ",'"
						+ "Farbdia/Kunstlicht (I: CT)" + "','" + 1 + "','" + 0
						+ "');");

				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
						+ " Values (" + null + ",'" + "Keiner" + "','" + 1
						+ "','" + 0 + "');");
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
						+ " Values (" + null + ",'" + "Keiner" + "','" + 1
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
						+ " Values (" + null + ",'" + "Gelbgr\u00FCn" + "','" + 1
						+ "','" + 0 + "');");
				myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
						+ " Values (" + null + ",'" + "Gr\u00FCn" + "','" + 1
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
						+ " Values (" + null + ",'" + "Kein Blitz" + "','" + 1
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
						+ 1 + "');");
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
						+ " Values (" + null + ",'" + "Keiner" + "','" + 1
						+ "','" + 0 + "');");
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
						+ " Values (" + null + ",'" + "Keiner" + "','" + 1
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
						+ " Values (" + null + ",'" + "Keiner" + "','" + 1
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
						+ " Values (" + null + ",'" + "Keiner" + "','" + 1
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

			} catch (Exception e) {
				Log.v("DEBUG", "Fehler bei Set-Erstellung : " + e);
			}
			return null;
		}
	}

	public class prepareToExport extends AsyncTask<String, Void, Boolean> {

		String Films;
		String _title = null;
		String FilmID;
		private ProgressDialog dialog;

		public prepareToExport(String _FilmID) {
			dialog = new ProgressDialog(mContext);
			FilmID = _FilmID;
		}

		protected void onPreExecute() {
			this.dialog.setMessage("...bereite Daten f\u00FCr Export vor...");
			this.dialog.show();
			Log.v("Check", "Pre");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			String FILENAME = _title + ".xml";

			try {
				FileOutputStream fos = openFileOutput(FILENAME,
						Context.MODE_WORLD_READABLE);
				fos.write(Films.getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File file = new File(getFilesDir() + "/" + _title + ".xml");

			Uri u1 = null;
			u1 = Uri.fromFile(file);
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Film Export");
			sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
			sendIntent.setType("text/html");
			startActivity(sendIntent);
		}

		protected Boolean doInBackground(final String... args) {
			String _kamera = null, _notiz = null, _filmformat = null, _empfindlichkeit = null, _filmtyp = null, _sonder1 = null, _sonder2 = null;
			ArrayList<BildObjekt> Bilder = new ArrayList<BildObjekt>();
			Film Film;
			/*
			 * Film - Infos holen
			 */
			onCreateDBAndDBNumber();
			Cursor c = myDBNummer.rawQuery(
					"SELECT datum, title,camera,bilder, pic FROM "
							+ MY_DB_TABLE_NUMMER + " WHERE title = '" + FilmID
							+ "'", null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						_title = c.getString(c.getColumnIndex("title"));
						_kamera = c.getString(c.getColumnIndex("camera"));
					} while (c.moveToNext());
				}
			}
			c.close();
			myDBNummer.close();

			onCreateDBAndDBTabledFilm();
			Cursor c1 = myDBFilm
					.rawQuery(
							"SELECT _id,filmtitle,filmnotiz,picuhrzeit,picnummer, picobjektiv, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
									+ MY_DB_FILM_TABLE
									+ " WHERE filmtitle = '"
									+ _title + "'", null);
			if (c1 != null) {
				if (c1.moveToFirst()) {
					do {
						_notiz = c1.getString(c1.getColumnIndex("filmnotiz"));
						_filmformat = c1.getString(c1
								.getColumnIndex("filmformat"));
						_filmtyp = c1.getString(c1.getColumnIndex("filmtyp"));
						_empfindlichkeit = c1.getString(c1
								.getColumnIndex("filmempfindlichkeit"));
						_sonder1 = c1
								.getString(c1.getColumnIndex("filmsonder"));
						_sonder2 = c1.getString(c1
								.getColumnIndex("filmsonders"));
					} while (c1.moveToNext());
				}
			}
			myDBFilm.close();
			c1.close();

			/*
			 * Bild - Infos holen
			 */
			Film puffer = new Film();
			onCreateDBAndDBTabledFilm();
			Cursor c11 = myDBFilm
					.rawQuery(
							"SELECT _id,picfokus,picuhrzeit,piclat,piclong,filmdatum,picobjektiv, picblende,piczeit,picmessung, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM "
									+ MY_DB_FILM_TABLE
									+ " WHERE filmtitle = '"
									+ _title + "'", null);
			if (c11 != null) {
				if (c11.moveToFirst()) {
					do {
						Bilder.add(puffer.new BildObjekt(
								c11.getString(c11.getColumnIndex("picnummer")),
								c11.getString(c11.getColumnIndex("picobjektiv")),
								c11.getString(c11.getColumnIndex("picblende")),
								c11.getString(c11.getColumnIndex("piczeit")),
								c11.getString(c11.getColumnIndex("picfokus")),
								c11.getString(c11.getColumnIndex("picfilter")),
								c11.getString(c11.getColumnIndex("picmakro")),
								c11.getString(c11.getColumnIndex("picfiltervf")),
								c11.getString(c11.getColumnIndex("picmakrovf")),
								c11.getString(c11.getColumnIndex("picmessung")),
								c11.getString(c11.getColumnIndex("pickorr")),
								c11.getString(c11.getColumnIndex("picblitz")),
								c11.getString(c11
										.getColumnIndex("picblitzkorr")), c11
										.getString(c11
												.getColumnIndex("picuhrzeit"))
										+ " - "
										+ c11.getString(c11
												.getColumnIndex("filmdatum")),
								"Lat : "
										+ c11.getString(c11
												.getColumnIndex("piclat"))
										+ " - Long : "
										+ c11.getString(c11
												.getColumnIndex("piclong")),
								c11.getString(c11.getColumnIndex("picnotiz")),
								c11.getString(c11
										.getColumnIndex("pickameranotiz"))));
					} while (c11.moveToNext());
				}
			}
			c11.close();
			myDBFilm.close();
			Film = new Film();
			Film = new Film(
					Film.new Allgemein(_title, _kamera, _notiz, _filmformat,
							_empfindlichkeit, _filmtyp, _sonder1, _sonder2),
					Bilder);
			Export exportObjekt = new Export();
			Films = exportObjekt.exportFilm(Film);

			return null;
		}
	}

}