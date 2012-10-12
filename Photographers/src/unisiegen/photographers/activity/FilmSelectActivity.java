package unisiegen.photographers.activity;

/**
 * In dieser Activity sieht man den ausgew�hlten Film mit allen Infos und kann sich die zugeh�rigen Bilder betrachten und ausw�hlen.
 */

import java.util.ArrayList;
import java.util.HashMap;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.export.BildObjekt;
import unisiegen.photographers.export.Film;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

	static String MY_DB_NAME;

	/*
	 * Sonstige Variablen
	 */
	Context mContext;
	ArrayList<Pictures> listItems;
	boolean minimizes;
	ArrayList<Integer> listItemsID;
	ArrayAdapter<Pictures> adapter;
	SharedPreferences settings;
	private Film film;

	/*
	 * Es Es werden ArrayListen mit allen Eintr�gen die "gechecked" sind
	 * erstellt, diese werden dann den "Spinnern" zugeordnet um diese mit den
	 * richtigen Daten zu f�llen! Die HashMaps dienen f�r den Standart wert. Sie
	 * beinhalten sp�ter den Index des "Standart-Werts" und dieser kann dann
	 * einfach dem Spinner �bergeben werden um die richtige Vorauswahl zu
	 * treffen.
	 */
	private void fuellen() {
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
		for (String brennweite : DB.getDB().getSettingForSpinner(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETBW)) {
			al_spinner_objektiv.add(brennweite);
			objektiv.put(brennweite, index++);
		}

		index = 0;
		for (String b : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETBLE)) {
			al_spinner_blende.add(b);
			blende.put(b, index++);
		}

		index = 0;
		for (String z : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETZEI)) {
			al_spinner_zeit.add(z);
			zeit.put(z, index++);
		}

		index = 0;
		for (String f : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETFOK)) {
			al_spinner_focus.add(f);
			fokus.put(f, index++);
		}

		index = 0;
		for (String f : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETFIL)) {
			al_spinner_filter.add(f);
			filter.put(f, index++);
		}

		index = 0;
		for (String f : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETNM)) {
			al_spinner_makro.add(f);
			makro.put(f, index++);
		}

		index = 0;
		for (String f : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETMES)) {
			al_spinner_messmethode.add(f);
			mess.put(f, index++);
		}

		index = 0;
		for (String f : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETPLU)) {
			al_spinner_belichtungs_korrektur.add(f);
			belichtung.put(f, index++);
		}

		if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Faktor (*)")) {

			index = 0;
			for (String f : DB.getDB().getSettingForSpinner(mContext,
					MY_DB_NAME, DB.MY_DB_TABLE_SETFVF)) {
				al_spinner_filter_vf.add(f);
				filtervf.put(f, index++);
			}

			index = 0;
			for (String f : DB.getDB().getSettingForSpinner(mContext,
					MY_DB_NAME, DB.MY_DB_TABLE_SETMVF)) {
				al_spinner_makro_vf.add(f);
				makrovf.put(f, index++);
			}

		} else if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Blendenzugaben (+)")) {

			index = 0;
			for (String f : DB.getDB().getSettingForSpinner(mContext,
					MY_DB_NAME, DB.MY_DB_TABLE_SETFVF2)) {
				al_spinner_filter_vf.add(f);
				filtervf.put(f, index++);
			}

			index = 0;
			for (String f : DB.getDB().getSettingForSpinner(mContext,
					MY_DB_NAME, DB.MY_DB_TABLE_SETMVF2)) {
				al_spinner_makro_vf.add(f);
				makrovf.put(f, index++);
			}
		}

		index = 0;
		for (String f : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETBLI)) {
			al_spinner_blitz.add(f);
			blitz.put(f, index++);
		}

		index = 0;
		for (String f : DB.getDB().getSettingForSpinner(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETKOR)) {
			al_spinner_blitz_korrektur.add(f);
			blitzkorr.put(f, index++);
		}
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

				film = DB.getDB().getFilm(mContext,
						getIntent().getExtras().getString("ID"));
				editor.putString("Title", film.Titel);

				editor.putString("Datum", film.Datum);
				editor.putString("Kamera", film.Kamera);
				editor.putInt("BildNummern", film.Bilder.size());

				editor.putString("Filmformat", film.Filmformat);
				editor.putString("Empfindlichkeit", film.Empfindlichkeit);
				editor.putString("Filmtyp", film.Filmtyp);
				editor.putString("Sonder1", film.Sonderentwicklung1);
				editor.putString("Sonder2", film.Sonderentwicklung2);

				int biggestNumber = 0;
				for (BildObjekt bild : film.Bilder) {

					Integer bildNummer = Integer.valueOf(bild.Bildnummer
							.replaceAll("[\\D]", ""));
					if (bildNummer > biggestNumber) {
						biggestNumber = bildNummer;
					}
					editor.putInt("BildNummerToBegin", bildNummer + 1);
				}

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

		film = DB.getDB().getFilm(mContext,
				getIntent().getExtras().getString("ID"));

		filmtit = (TextView) findViewById(R.id.filmtitle);
		filmtit.setText(film.Titel);

		filmcam = (TextView) findViewById(R.id.filmcam);
		filmcam.setText(film.Kamera);

		TextView datum = (TextView) findViewById(R.id.datuminfo);
		datum.setText(film.Datum);

		bilderimfilm = film.Bilder.size();
		ImageView vorschauImage = (ImageView) findViewById(R.id.vorschau);

		byte[] data = Base64.decode(film.iconData, Base64.DEFAULT);
		vorschauImage.setImageBitmap(BitmapFactory.decodeByteArray(data, 0,
				data.length));

		TextView filmnotiz = (TextView) findViewById(R.id.filmnotiz);
		filmnotiz.setText(film.Filmnotiz);

		TextView filmformat = (TextView) findViewById(R.id.filmformat);
		filmformat.setText(film.Filmformat);

		TextView filmtyp = (TextView) findViewById(R.id.filmtyp);
		filmtyp.setText(film.Filmtyp);

		TextView filmemp = (TextView) findViewById(R.id.filmemp);
		filmemp.setText(film.Empfindlichkeit);

		TextView filmsonder = (TextView) findViewById(R.id.filmsonder);
		filmsonder.setText(film.Sonderentwicklung1);

		TextView filmsonders = (TextView) findViewById(R.id.filmsonders);
		filmsonders.setText(film.Sonderentwicklung2);

		listItems = new ArrayList<Pictures>();

		for (BildObjekt b : film.Bilder) {
			listItems.add(new Pictures(b));
		}

		Log.v("Check", "LISTITEMS : " + listItems.size());

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

					String selektiertesBild = (String) third.getText();
					for (BildObjekt bild : film.Bilder) {
						if (bild.Bildnummer.equals(selektiertesBild)) {

							{
								final TextView zeitStempel = (TextView) v1
										.findViewById(R.id.zeitStempel);
								zeitStempel.setText(bild.Zeitstempel);
								final TextView zeitGeo = (TextView) v1
										.findViewById(R.id.geoTag);
								zeitGeo.setText(bild.GeoTag);
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
								final TextView filtervft = (TextView) v1
										.findViewById(R.id.showfiltervf);
								filtervfedit = (Spinner) v1
										.findViewById(R.id.editfiltervf);
								filtervft.setVisibility(TextView.GONE);
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
								picTitle.setText(bild.Bildnummer);
							}
							// wegen Namenskonflikten in 2 Blöcke unterteilt
							// *grml*
							{
								picblendeedit.setSelection(blende
										.get(bild.Blende));
								filtervfedit.setSelection(filtervf
										.get(bild.FilterVF));
								objektivedit.setSelection(objektiv
										.get(bild.Objektiv));
								piczeitedit.setSelection(zeit.get(bild.Zeit));
								picfocusedit.setSelection(fokus.get(bild.Fokus));
								picfilteredit.setSelection(filter
										.get(bild.Filter));
								picmakroedit.setSelection(makro.get(bild.Makro));
								picmessungedit.setSelection(mess
										.get(bild.Messmethode));
								picplusedit.setSelection(belichtung
										.get(bild.Belichtungskorrektur));
								picmakrovfedit.setSelection(makrovf
										.get(bild.MakroVF));
								picblitzedit.setSelection(blitz.get(bild.Blitz));
								picblitzkorredit.setSelection(blitzkorr
										.get(bild.Blitzkorrektur));
								picnotizedit.setText(bild.Notiz);
								picnotizcamedit.setText(bild.KameraNotiz);
							}
							break;
						}
					}

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
							
							for(BildObjekt bild : film.Bilder){
								if(bild.Bildnummer.equals(third.getText())){
									
									bild.Fokus = picfocusedit.getSelectedItem().toString();
									bild.Blende = picblendeedit.getSelectedItem().toString();
									bild.Zeit = piczeitedit.getSelectedItem().toString();
									picmessungedit.getSelectedItem().toString();
									picplusedit.getSelectedItem().toString();
									picmakroedit.getSelectedItem().toString();
									picmakrovfedit.getSelectedItem().toString();
									bild.Filter = picfilteredit.getSelectedItem().toString();
									bild.FilterVF = filtervfedit.getSelectedItem().toString();
									bild.Blitz = picblitzedit.getSelectedItem().toString();
									bild.Blitzkorrektur = picblitzkorredit.getSelectedItem().toString();
									bild.Notiz = picnotizedit.getText().toString();
									bild.KameraNotiz = picnotizcamedit.getText().toString();
									bild.Objektiv = objektivedit.getSelectedItem().toString();
									
									DB.getDB().updatePicture(mContext, film, bild);
									break;
								}
							}							
							pwblub.dismiss();
							onResume();
						}
					});

					pwblub.setAnimationStyle(7);
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
											
											for(BildObjekt bild : film.Bilder){
												if(bild.Bildnummer.equals(third.getText())){
													DB.getDB().deletePicture(mContext, film, bild);
													break;
												}
											}
											
											bilderimfilm -= 1;											
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

		public MyPagerAdapter(Context context) {
			views = new ArrayList<View>();
			LayoutInflater inflater = getLayoutInflater();

			for (BildObjekt bild : film.Bilder) {
				View v = inflater.inflate(R.layout.filminfobox, null, false);
				final TextView zeitStempel = (TextView) v
						.findViewById(R.id.zeitStempel);
				zeitStempel.setText(bild.Zeitstempel + " - " + film.Datum);
				final TextView zeitGeo = (TextView) v.findViewById(R.id.geoTag);
				zeitGeo.setText(bild.GeoTag);
				final TextView objektiv = (TextView) v
						.findViewById(R.id.showobjektiv);
				objektiv.setText(bild.Objektiv);
				final TextView filtervf = (TextView) v
						.findViewById(R.id.showfiltervf);
				filtervf.setText(bild.FilterVF);
				final TextView picfocus = (TextView) v
						.findViewById(R.id.showfokus);
				picfocus.setText(bild.Fokus);
				final TextView picblende = (TextView) v
						.findViewById(R.id.showblende);
				picblende.setText(bild.Blende);
				final TextView piczeit = (TextView) v
						.findViewById(R.id.showzeit);
				piczeit.setText(bild.Zeit);
				final TextView picmessung = (TextView) v
						.findViewById(R.id.showmessung);
				picmessung.setText(bild.Messmethode);
				final TextView picplus = (TextView) v
						.findViewById(R.id.showbelichtung);
				picplus.setText(bild.Belichtungskorrektur);
				final TextView picmakro = (TextView) v
						.findViewById(R.id.showmakro);
				picmakro.setText(bild.Makro);
				final TextView picmakrovf = (TextView) v
						.findViewById(R.id.showmakrovf);
				picmakrovf.setText(bild.MakroVF);
				final TextView picfilter = (TextView) v
						.findViewById(R.id.showfilter);
				picfilter.setText(bild.Filter);
				final TextView picblitz = (TextView) v
						.findViewById(R.id.showblitz);
				picblitz.setText(bild.Blitz);
				final TextView picblitzkorr = (TextView) v
						.findViewById(R.id.showblitzkorr);
				picblitzkorr.setText(bild.Blitzkorrektur);
				final TextView picnotiz = (TextView) v
						.findViewById(R.id.shownotiz);
				picnotiz.setText(bild.Notiz);
				final TextView picnotizcam = (TextView) v
						.findViewById(R.id.shownotizkam);
				picnotizcam.setText(bild.KameraNotiz);
				final TextView picTitle = (TextView) v
						.findViewById(R.id.pictitle);
				picTitle.setText(bild.Bildnummer);

				views.add(v);
			}
		}

		@Override
		public void destroyItem(View view, int arg1, Object object) {
			// Es werden immer nur die 2 nächsten und die 2 letzten Views
			// "gespeichert" bzw. berechnet, der Rest wird erstmal gelöscht.

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
		public Object instantiateItem(View view, int position) {
			// Das vorpuffern, wenn die View bald drankommt... s.o.
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
		private String timestamp = "";
		private String objektiv = "";
		private String blende = "";

		public Pictures(BildObjekt b) {
			this.name = b.Bildnummer;
			this.time = b.Zeit;
			this.blende  = b.Blende;
			this.objektiv = b.Objektiv;
			this.timestamp = b.Zeitstempel;
		}

		public String getName() {
			return name;
		}

		public String getTime() {
			return time;
		}
		
		public String getBlende(){
			return blende;
		}
		
		public String getZeitstempel(){
			return timestamp;
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

		public PicturesArrayAdapter(Context context,
				ArrayList<Pictures> planetList, int number) {
			super(context, R.layout.film_item, R.id.listItemText, planetList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Pictures p = (Pictures) this.getItem(position);
			TextView textViewApertureTime;
			TextView textView;
			TextView textViewTime;

			if (convertView == null) {
				
				convertView = inflater.inflate(R.layout.film_item, null);
				textView = (TextView) convertView.findViewById(R.id.listItemText);
				textViewApertureTime = (TextView) convertView.findViewById(R.id.listItemBlendeZeit);
				textViewTime = (TextView) convertView.findViewById(R.id.listItemTextTime);
				convertView.setTag(new PicturesViewHolder(textView, textViewTime, textViewApertureTime));
				
			} else {
				PicturesViewHolder viewHolder = (PicturesViewHolder) convertView
						.getTag();
				textViewTime = viewHolder.getTextViewTime();
				textView = viewHolder.getTextViewName();
				textViewApertureTime = viewHolder.getTextViewObjektiv();
			}
			textViewTime.setText(p.getZeitstempel());
			textView.setText(p.getName());
			textViewApertureTime.setText(p.getBlende() + " - " + p.getTime());

			return convertView;
		}
	}

}