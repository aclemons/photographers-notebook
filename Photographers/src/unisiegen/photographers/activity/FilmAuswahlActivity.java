package unisiegen.photographers.activity;

/**
 * Activity f�r die Auswahl eines Film (View die beim Start angezeigt wird)
 * Hier kann man einen bestehenden Film ausw�hlen, bearbeiten und betrachten
 * Oder man kann einen neuen Film starten
 * 
 * Hier werden einige Methoden genauer erkl�rt, viele der Methoden kommen in anderen Activitys in selber Art 
 * und Weise wieder vor und werden dort dann nicht mehr ausf�hrlich besprochen.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.export.Allgemein;
import unisiegen.photographers.export.BildObjekt;
import unisiegen.photographers.export.Film;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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

import com.thoughtworks.xstream.XStream;

public class FilmAuswahlActivity extends Activity {

	/*
	 * Datenbank-Variablen
	 */
	SQLiteDatabase myDBSet = null;
	SQLiteDatabase myDB = null;
	SQLiteDatabase myDBFilm = null;
	SQLiteDatabase myDBNummer = null;
	
	static String MY_DB_NAME;
	
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
	SharedPreferences settings;
	ArrayList<Films> listItems;
	ArrayAdapter<Films> adapter;
	
	public static Context mContext;
	public static LayoutInflater inflater;
	public Integer contentIndex = 0;
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
		List<Film> filme = DB.getDB().getFilme(mContext);		
		
		for(Film film : filme){
			listItems.add(new Films(film));
			gesamtPics = gesamtPics + film.Bilder.size();
		}
		
		if (gesamtPics == 0) {
			myList.setVisibility(ListView.GONE);
			image.setVisibility(ImageView.VISIBLE);
		} else {
			myList.setVisibility(ListView.VISIBLE);
			image.setVisibility(ImageView.GONE);
		}
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
	 * Datenbank-Methoden (Die SQL Datenbanken werden ge�ffnet bzw. falls sie
	 * noch nicht entstehen, werden sie erstellt)
	 */

	private void onCreateDBAndDBNumber() {
		myDBNummer = mContext.openOrCreateDatabase(DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);
		myDBNummer
				.execSQL("CREATE TABLE IF NOT EXISTS "
						+ DB.MY_DB_TABLE_NUMMER
						+ " (title varchar(100) primary key, value integer,camera varchar(100), datum varchar(100), bilder integer, pic varchar(999))"
						+ ";");
	}

	private void onCreateDBAndDBTabledFilm() {
		myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		myDBFilm.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_FILM_TABLE
				+ " (_id integer primary key autoincrement, filmdatum varchar(100), picuhrzeit varchar(100), filmtitle varchar(100), filmcamera varchar(100), filmformat varchar(100), filmempfindlichkeit varchar(100), filmtyp varchar(100), filmsonder varchar(100), filmsonders varchar(100), picfokus varchar(100), picblende varchar(100), piczeit varchar(100), picmessung varchar(100), pickorr varchar(100), picmakro varchar(100), picmakrovf varchar(100), picfilter varchar(100), picfiltervf varchar(100), picblitz varchar(100), picblitzkorr varchar(100), picnotiz varchar(100), pickameranotiz varchar(100), picobjektiv varchar(100),piclong varchar(100),piclat varchar(100),filmnotiz varchar(100), picnummer varchar(100))"
				+ ";");
	}

	/*
	 * Hilfs-Klasse f�r die Anzeige der Filmobjekte in einer Listen-Zeile (Die
	 * Attribute die Angezeigt werden, werden f�r jeden Film als ein Objekt
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
		
		public Films(Film film){
			this(film.Allgemein.Titel,
					film.Bilder.get(0).Zeit,
					film.Allgemein.Kamera,
					null,
					film.Allgemein.iconData);
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
	 * ViewHolder f�r die Filmobjekte der Einzelenden Listen-Zeilen (Zum
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
	 * Custom Array-Adapter f�r Custom-Listen-Zeilen (Es handelt sich um eine
	 * Liste, welche Custom-"Zellen" zum Anzeigen verwendet, hierf�r wird ein
	 * custom ArrayAdapter ben�tigt, der die Attribute den richtigen Stellen in
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
							"Wollen Sie den Eintrag wirklich l\u00F6schen ?")
							.setCancelable(false)
							.setPositiveButton("Ja",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											onCreateDBAndDBNumber();
											onCreateDBAndDBTabledFilm();

											myDBFilm.delete(DB.MY_DB_FILM_TABLE,
													"filmtitle=?",
													new String[] { ids
															.getText()
															.toString() });

											myDBNummer.delete(
													DB.MY_DB_TABLE_NUMMER,
													"title=?",
													new String[] { ids
															.getText()
															.toString() });
											Toast.makeText(
													getApplicationContext(),
													"Film gel\u00F6scht",
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
									+ DB.MY_DB_TABLE_NUMMER + " WHERE title = '"
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
											+ DB.MY_DB_FILM_TABLE
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
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) Men�
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
	 * Popup f�r Tutorial
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
			Log.v("DEBUG", "First start detected.");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("SettingsTable", DB.MY_DB_SET);			
			editor.commit();
			MY_DB_NAME = DB.MY_DB_SET;
		}

		/*
		 * Es werden nochmal alle Tabellen in der SQL Datenbank neu erstellt und
		 * neu gef�llt... nicht sehr elegant :)
		 */
		protected Boolean doInBackground(final String... args) {
			try {
				
				DB.getDB().createOrRebuildSettingsTable(mContext);

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
							+ DB.MY_DB_TABLE_NUMMER + " WHERE title = '" + FilmID
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
									+ DB.MY_DB_FILM_TABLE
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
									+ DB.MY_DB_FILM_TABLE
									+ " WHERE filmtitle = '"
									+ _title + "'", null);
			if (c11 != null) {
				if (c11.moveToFirst()) {
					do {
						Bilder.add(new BildObjekt(
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
					new Allgemein(_title, _kamera, _notiz, _filmformat,
							_empfindlichkeit, _filmtyp, _sonder1, _sonder2),
					Bilder);
			
			
			String FILENAME = _title + ".xml";
			
			XStream xs = new XStream();
			xs.alias("Bild", BildObjekt.class);
			xs.alias("Film", Film.class);					
			
			try {
				FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_WORLD_READABLE);
				xs.toXML(Film, fos);
				fos.close();
				Log.v("Check","XML Export: " + FILENAME + " was written.");
			} catch (IOException e) {
				e.printStackTrace();
				Log.v("Check","Failes to write XML Export: " + FILENAME);
			}
			
			return null;
		}
	}

}