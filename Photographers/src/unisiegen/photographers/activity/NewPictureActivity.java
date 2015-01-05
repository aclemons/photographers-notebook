/* Copyright (C) 2012 Nico Castelli, Christopher Maiworm 
 * Copyright (C) 2012 Sebastian Draxler, Alexander Boden, Christian Woehrl (Committers)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unisiegen.photographers.activity;

/**
 * Hier "schie�t" man die neuen Fotos! Man nimmt die Bildeinstellungen vor und speichert das Bild.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.helper.DefaultLocationListener;
import unisiegen.photographers.helper.FilmIconFactory;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class NewPictureActivity extends PhotographersNotebookActivity {

	/*
	 * Sonstige Variablen
	 */

	static String[] CONTENT = null; // Array is filled in the onCreate() method.

	SharedPreferences settings;
	Context mContext;

	// Picture function deprecated.
	// byte[] pics;
	boolean bildtoedit;
	int picturesNumber;
	int edit = 1;

	/*
	 * Spinner Variablen
	 */

	Spinner spinner_blende, spinner_filter_vf, spinner_objektiv, spinner_zeit,
			spinner_fokus, spinner_filter, spinner_makro, spinner_messmethode,
			spinner_belichtungs_korrektur, spinner_makro_vf, spinner_blitz,
			spinner_blitz_korrektur;

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

	@Override
	public void onPause() {
		super.onPause();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		// Check if user wants to edit a certain picture, if yes update UI
		// accordingly.
		Bundle bundle = getIntent().getExtras();
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		if (bundle != null) {
			String selectedPic = bundle.getString("picToEdit");
			String filmToEdit = bundle.getString("filmToEdit");
			if (selectedPic != null) {
				nummerView.setText(selectedPic);
				updateUIFromPicture(selectedPic,
						settings.getString("Title", " "));
				aufnehmen.setText(getString(R.string.save_changes));
			}
		}

		Film film = DB.getDB().getFilm(mContext,
				settings.getString("Title", " "));
		Bitmap b = new FilmIconFactory().createBitmap(film);
		Drawable drawable = new BitmapDrawable(getResources(), b);
		if (android.os.Build.VERSION.SDK_INT >= 14) {
			try {
				getActionBar().setIcon(drawable);
			} catch (Exception e) {
				Log.v("check", e.toString());
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidenewfilm);
		mContext = this;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);

		Resources res = getResources();
		CONTENT = res.getStringArray(R.array.pic_slide_contents);

		nummerView = (TextView) findViewById(R.id.TextView_nr);
		bildtoedit = false;

		int aktuellebildnummer = settings.getInt("BildNummerToBegin", 1);
		nummerView.setText(getString(R.string.picture) + " "
				+ aktuellebildnummer);

		if (settings.getBoolean("EditMode", false)) {
			picturesNumber = settings.getInt("BildNummern", 1);
		} else {
			Bundle extras = getIntent().getExtras();
			// pics = extras.getByteArray("image");
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		PictureSettingsPager adapter = new PictureSettingsPager(this);
		viewPager.setAdapter(adapter);
		mIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		mIndicator.setViewPager(viewPager);

		plus = (Button) findViewById(R.id.button_plus);
		minus = (Button) findViewById(R.id.button_minus);

		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				incrementSelectedPicture();
				updateUIFromPicture(nummerView.getText().toString(),
						settings.getString("Title", " "));
				if (bildtoedit) {
					aufnehmen.setText(getString(R.string.save_changes));
				} else {
					aufnehmen.setText(getString(R.string.take_picture));
				}

			}
		});
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Integer.valueOf(nummerView.getText().toString()
						.replaceAll("[\\D]", "")) > 1) {
					decrementSelectedPicture();
					updateUIFromPicture(nummerView.getText().toString(),
							settings.getString("Title", " "));
				}
				if (bildtoedit) {
					aufnehmen.setText(getString(R.string.save_changes));
				} else {
					aufnehmen.setText(getString(R.string.take_picture));
				}
			}
		});

		aufnehmen = (Button) findViewById(R.id.button_aufnehmen);
		aufnehmen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = settings.edit();
				Calendar cal = Calendar.getInstance();

				if (settings.getString("zeitStempel", getString(R.string.on)) == getString(R.string.minus_one_minute)) {
					cal.add(Calendar.MINUTE, -1);
				}

				String zeit = "-";
				String datum = "-";

				if (settings.getString("zeitStempel", getString(R.string.on)) != getString(R.string.off)) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					datum = sdf.format(cal.getTime());
					sdf = new SimpleDateFormat("HH:mm");
					zeit = sdf.format(cal.getTime());
				}

				editor.putString("Uhrzeit", zeit);
				editor.putString("Datum", datum);
				editor.commit();

				try {
					if (bildtoedit) {
						editPicture();
					} else {
						saveNewPicture();
					}
					Toast.makeText(getApplicationContext(),
							getString(R.string.picture_taken),
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							getString(R.string.input_error), Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	/*
	 * Falls ein Bild bearbeitet werden soll, setze Spinner auf die Werte des
	 * Bildes
	 */
	private void updateUIFromPicture(String picNumber, String filmTitle) {

		ArrayList<Bild> bilder = DB.getDB().getBild(mContext, filmTitle,
				picNumber);
		if (bilder == null || bilder.size() != 1) {
			bildtoedit = false;
			Log.v("Check", "Kein Bild vorhanden");
		} else {
			Bild bild = bilder.get(0);
			bildtoedit = true;

			updateSpinner(spinner_blende, bild.Blende);
			updateSpinner(spinner_filter_vf, bild.FilterVF);
			updateSpinner(spinner_objektiv, bild.Objektiv);
			updateSpinner(spinner_zeit, bild.Zeit);
			updateSpinner(spinner_fokus, bild.Fokus);
			updateSpinner(spinner_filter, bild.Filter);
			updateSpinner(spinner_makro, bild.Makro);
			updateSpinner(spinner_messmethode, bild.Messmethode);
			updateSpinner(spinner_belichtungs_korrektur,
					bild.Belichtungskorrektur);
			updateSpinner(spinner_makro_vf, bild.MakroVF);
			updateSpinner(spinner_blitz, bild.Blitz);
			updateSpinner(spinner_blitz_korrektur, bild.Blitzkorrektur);

			edit_notizen.setText(bild.Notiz);
			edit_kamera_notizen.setText(bild.KameraNotiz);
		}
	}

	private void updateSpinner(Spinner spinner, String value) {
		for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
			if (spinner.getItemAtPosition(i).toString().equals(value)) {
				spinner.setSelection(i);
				break;
			}
		}
	}

	private void incrementSelectedPicture() {

		nummerView.setText(getString(R.string.picture)
				+ " "
				+ (Integer.valueOf(nummerView.getText().toString()
						.replaceAll("[\\D]", "")) + 1));
	}

	private void decrementSelectedPicture() {
		nummerView.setText(getString(R.string.picture)
				+ " "
				+ (Integer.valueOf(nummerView.getText().toString()
						.replaceAll("[\\D]", "")) - 1));
	}

	/**
	 * prepare a BildObjekt from UI values, to use for database operations etc.
	 */
	private Bild getBildFromUI() {
		Bild b = new Bild();
		b.Fokus = spinner_fokus.getSelectedItem().toString();
		b.Blende = spinner_blende.getSelectedItem().toString();
		b.Zeit = spinner_zeit.getSelectedItem().toString();
		b.Messmethode = spinner_messmethode.getSelectedItem().toString();
		b.Belichtungskorrektur = spinner_belichtungs_korrektur
				.getSelectedItem().toString();
		b.Makro = spinner_makro.getSelectedItem().toString();
		b.MakroVF = spinner_makro_vf.getSelectedItem().toString();
		b.Filter = spinner_filter.getSelectedItem().toString();
		b.FilterVF = spinner_filter_vf.getSelectedItem().toString();
		b.Blitz = spinner_blitz.getSelectedItem().toString();
		b.Blitzkorrektur = spinner_blitz_korrektur.getSelectedItem().toString();
		b.Notiz = edit_notizen.getText().toString();
		b.KameraNotiz = edit_kamera_notizen.getText().toString();
		b.Objektiv = spinner_objektiv.getSelectedItem().toString();
		// This is somewhat of a hack, as the Film object stores the geolocation
		// as a string, while the database stores two discinct values for
		// longitude and latitude. While saving, we have to split these values,
		// using the "' , '" String.
		DefaultLocationListener listener = getLocListener();
		if (getLocListener() == null) {
			// no geo information / no listener
			b.GeoTag = String.valueOf(0d) + "' , '" + String.valueOf(0d);
		} else {
			Location last = listener.getLast();
			if (last == null) {
				// no geo information / listener but no valid GPS position
				b.GeoTag = String.valueOf(0d) + "' , '" + String.valueOf(0d);
			} else {
				b.GeoTag = String.valueOf(last.getLongitude()) + "' , '"
						+ String.valueOf(last.getLatitude());
			}
		}
		b.Bildnummer = nummerView.getText().toString();
		b.Zeitstempel = settings.getString("Uhrzeit", " ");

		return b;
	}

	private Film getFilmFromSettings() {

		Film film = new Film();

		film.Datum = settings.getString("Datum", " ");
		film.Titel = settings.getString("Title", " ");
		film.Kamera = settings.getString("Kamera", " ");
		film.Filmformat = settings.getString("Filmformat", " ");
		film.Empfindlichkeit = settings.getString("Empfindlichkeit", " ");
		film.Filmtyp = settings.getString("Filmtyp", " ");
		film.Sonderentwicklung1 = settings.getString("Sonder1", " ");
		film.Sonderentwicklung2 = settings.getString("Sonder2", " ");
		film.Filmbezeichnung = settings.getString("FilmBezeichnung", " ");

		return film;
	}

	/**
	 * Save a newly created picture to the current selected film
	 */
	private void saveNewPicture() {
		Log.v("Check", "saveNewPicture()");
		picturesNumber++;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);

		Film f = DB.getDB().getFilm(mContext, settings.getString("Title", " "));
		if (f.Titel == null) {
			f = getFilmFromSettings();
		}
		Bild b = getBildFromUI();
		if (settings.getBoolean("EditMode", false)) {
			// ACHTUNG: DAS WIRD NIE AUFGERUFEN! WARUM IST DAS NIE AUF TRUE?
			DB.getDB().addPictureUpdateNummer(mContext, f, b, picturesNumber);

		} else {

			DB.getDB().addPictureCreateNummer(mContext, f, b, picturesNumber,
					null);
		}
		incrementSelectedPicture();
	}

	/**
	 * Updates the currently selected picture.
	 */
	private void editPicture() {
		Log.v("Check", "editPicture()");

		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		String filmTitle = settings.getString("Title", " ");
		Film f = DB.getDB().getFilm(mContext, filmTitle);
		Bild b = getBildFromUI();
		DB.getDB().updatePicture(mContext, f, b);
	}

	/*
	 * HilfsKlassen f�r SlideView etc.
	 */
	public void setFooterColor(int footerColor) {
		mIndicator.setFooterColor(footerColor);
	}

	private class PictureSettingsPager extends PagerAdapter implements
			TitleProvider {

		private ArrayList<View> views;

		private Spinner setupSpecialSpinnerLenses(View view) {
			ArrayList<String> lenses = DB.getDB().getLensesForCamera(mContext,
					settings.getString("Kamera", ""));
			if (lenses.size() == 0) {
				lenses.add(getString(R.string.no_selection));
			}
			spinner_objektiv = (Spinner) view
					.findViewById(R.id.spinner_brennweite);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_spinner_item, lenses);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_objektiv.setAdapter(adapter);

			return spinner_objektiv;
		}

		private Spinner setupSpecialSpinnerApertures(View view) {
			// setup the special spinner for apertures
			// Get default aperture values from Database. Add additional values
			// ad-hoc: 1/2 or 1/3 stop values are added manually, not from
			// database!
			int defaultAperture = DB.getDB().getDefaultSettingNumber(mContext,
					DB.MY_DB_TABLE_SETBLE);
			ArrayList<String> apertures = DB.getDB().getActivatedSettingsData(
					mContext, DB.MY_DB_TABLE_SETBLE);
			ArrayList<String> finalApertures = new ArrayList<String>();
			if (apertures.size() == 0) {
				finalApertures.add(getString(R.string.no_selection));
			} else {
				for (String aperture : apertures) {
					if (aperture.equals("Auto")) {
						finalApertures.add(aperture);
					} else {
						if (settings.getString("blendenstufe", "1/1").equals(
								"1/1")) {
							finalApertures.add(aperture);
						} else if (settings.getString("blendenstufe", "1/1")
								.equals("1/2")) {
							finalApertures.add(aperture);
							finalApertures.add(aperture + " + 1/2");
						} else {
							finalApertures.add(aperture);
							finalApertures.add(aperture + " + 1/3");
							finalApertures.add(aperture + " + 2/3");
						}
					}
				}
			}
			spinner_blende = (Spinner) view.findViewById(R.id.spinner_blende);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_spinner_item, finalApertures);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_blende.setAdapter(adapter);
			spinner_blende.setSelection(defaultAperture);

			return spinner_blende;
		}

		Spinner setupSpinner(View view, int uiID, String tableName) {

			ArrayList<String> values = DB.getDB().getActivatedSettingsData(
					mContext, tableName);
			int defaultValue = DB.getDB().getDefaultSettingNumber(mContext,
					tableName);
			if (values.size() == 0) {
				values.add(getString(R.string.no_selection));
			}

			Spinner spinner = (Spinner) view.findViewById(uiID);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_spinner_item, values);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setSelection(defaultValue);

			return spinner;
		}

		public PictureSettingsPager(Context context) {
			final SharedPreferences.Editor editors = settings.edit();
			views = new ArrayList<View>();
			LayoutInflater inflater = getLayoutInflater();

			// shared listener for all spinners.
			OnItemSelectedListener listener = new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> view, View arg1,
						int arg2, long arg3) {

					if (view == spinner_objektiv) {
						editors.putString("Objektiv", spinner_objektiv
								.getSelectedItem().toString());
					} else if (view == spinner_blende) {
						editors.putString("blende", spinner_blende
								.getSelectedItem().toString());
					} else if (view == spinner_zeit) {
						editors.putString("zeit", spinner_zeit
								.getSelectedItem().toString());
					} else if (view == spinner_fokus) {
						editors.putString("fokus", spinner_fokus
								.getSelectedItem().toString());
					} else if (view == spinner_filter) {
						editors.putString("filter", spinner_filter
								.getSelectedItem().toString());
					} else if (view == spinner_makro) {
						editors.putString("makro", spinner_makro
								.getSelectedItem().toString());
					} else if (view == spinner_messmethode) {
						editors.putString("messmethode", spinner_messmethode
								.getSelectedItem().toString());
					} else if (view == spinner_filter_vf) {
						editors.putString("FilterVF", spinner_filter_vf
								.getSelectedItem().toString());
					} else if (view == spinner_belichtungs_korrektur) {
						editors.putString("belichtungs_korrektur",
								spinner_belichtungs_korrektur.getSelectedItem()
										.toString());
					} else if (view == spinner_makro_vf) {
						editors.putString("makro_vf", spinner_makro_vf
								.getSelectedItem().toString());
					} else if (view == spinner_blitz) {
						editors.putString("blitz", spinner_blitz
								.getSelectedItem().toString());
					} else if (view == spinner_blitz_korrektur) {
						editors.putString("blitz_korrektur",
								spinner_blitz_korrektur.getSelectedItem()
										.toString());
					}
					editors.commit();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			};

			// Allgemein
			// ---------------------------------------------------------------------------
			View firstPage = inflater.inflate(R.layout.pictab1, null, false);

			spinner_objektiv = setupSpecialSpinnerLenses(firstPage);
			spinner_blende = setupSpecialSpinnerApertures(firstPage);
			spinner_zeit = setupSpinner(firstPage, R.id.spinner_zeit,
					DB.MY_DB_TABLE_SETZEI);
			spinner_fokus = setupSpinner(firstPage, R.id.spinner_fokus,
					DB.MY_DB_TABLE_SETFOK);
			spinner_filter = setupSpinner(firstPage, R.id.spinner_filter,
					DB.MY_DB_TABLE_SETFIL);
			spinner_makro = setupSpinner(firstPage, R.id.spinner_makro,
					DB.MY_DB_TABLE_SETNM);

			spinner_objektiv.setOnItemSelectedListener(listener);
			spinner_blende.setOnItemSelectedListener(listener);
			spinner_zeit.setOnItemSelectedListener(listener);
			spinner_fokus.setOnItemSelectedListener(listener);
			spinner_filter.setOnItemSelectedListener(listener);
			spinner_makro.setOnItemSelectedListener(listener);

			views.add(firstPage);

			// 2nd page
			View secondPage = inflater.inflate(R.layout.pictab2, null, false);
			String filterVFTable = DB.MY_DB_TABLE_SETFVF;
			String makroVFTable = DB.MY_DB_TABLE_SETMVF;
			if (settings.getString("Verlaengerung", "Faktor (*)").equals(
					"Faktor (*)")) {
				// values are already correct....
			} else if (settings.getString("Verlaengerung", "Faktor (*)")
					.equals("Blendenzugaben (+)")) {
				filterVFTable = DB.MY_DB_TABLE_SETFVF2;
				makroVFTable = DB.MY_DB_TABLE_SETMVF2;
			}

			spinner_messmethode = setupSpinner(secondPage,
					R.id.spinner_messmethode, DB.MY_DB_TABLE_SETMES);
			spinner_filter_vf = setupSpinner(secondPage,
					R.id.spinner_filter_vf, filterVFTable);
			spinner_belichtungs_korrektur = setupSpinner(secondPage,
					R.id.spinner_belichtungs_korrektur, DB.MY_DB_TABLE_SETPLU);
			spinner_makro_vf = setupSpinner(secondPage, R.id.spinner_makro_vf,
					makroVFTable);
			spinner_blitz = setupSpinner(secondPage, R.id.spinner_blitz,
					DB.MY_DB_TABLE_SETBLI);
			spinner_blitz_korrektur = setupSpinner(secondPage,
					R.id.spinner_blitz_korrektur, DB.MY_DB_TABLE_SETKOR);

			spinner_messmethode.setOnItemSelectedListener(listener);
			spinner_filter_vf.setOnItemSelectedListener(listener);
			spinner_belichtungs_korrektur.setOnItemSelectedListener(listener);
			spinner_makro_vf.setOnItemSelectedListener(listener);
			spinner_blitz.setOnItemSelectedListener(listener);
			spinner_blitz_korrektur.setOnItemSelectedListener(listener);

			views.add(secondPage);

			// 3rd Page
			View thirdPage = inflater.inflate(R.layout.pictab3, null, false);

			edit_notizen = (EditText) thirdPage.findViewById(R.id.edit_notizen);
			edit_kamera_notizen = (EditText) thirdPage
					.findViewById(R.id.edit_kamera_notizen);

			views.add(thirdPage);
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

			return NewPictureActivity.CONTENT[position
					% NewPictureActivity.CONTENT.length];
		}

	}

}
