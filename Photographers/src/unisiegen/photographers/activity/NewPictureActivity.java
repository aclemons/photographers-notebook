package unisiegen.photographers.activity;

/**
 * Hier "schie�t" man die neuen Fotos! Man nimmt die Bildeinstellungen vor und speichert das Bild.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
	LocationManager locManager;
	LocationListener locationListener;

	byte[] pics;
	double piclong = 0;
	double piclat = 0;
	boolean bildtoedit;
	int picturesNumber;
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

	static String MY_DB_NAME;


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
		if (settings.getString("geoTag", "nein").equals("ja")) {
			getLocation();
		}
		
		// Check if user wants to edit a certain picture, if yes update UI
		// accordingly.
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String selectedPic = bundle.getString("picToEdit");
			if (selectedPic != null) {
				nummerView.setText(selectedPic);
				updateUIFromPicture(selectedPic,
						settings.getString("Title", " "));
				aufnehmen.setText(getString(R.string.save_changes));
			}
		}
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidenewfilm);
		mContext = this;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		MY_DB_NAME = settings.getString("SettingsTable", "Foto");

		Resources res = getResources();
		CONTENT = res.getStringArray(R.array.pic_slide_contents);

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

		int aktuellebildnummer = settings.getInt("BildNummerToBegin", 1);
		nummerView.setText(getString(R.string.picture) + " "
				+ aktuellebildnummer);

		if (settings.getBoolean("EditMode", false)) {
			picturesNumber = settings.getInt("BildNummern", 1);
		} else {
			Bundle extras = getIntent().getExtras();
			pics = extras.getByteArray("image");
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
		System.out.println("foo");
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
			if (!blende.isEmpty()) {
				spinner_blende.setSelection(blende.get(bild.Blende));
			}
			if (!filtervf.isEmpty()) {
				spinner_filter_vf.setSelection(filtervf.get(bild.FilterVF));
			}
			if (!objektiv.isEmpty()) {
				spinner_objektiv.setSelection(objektiv.get(bild.Objektiv));
			}
			if (!zeit.isEmpty()) {
				spinner_zeit.setSelection(zeit.get(bild.Zeit));
			}
			if (!fokus.isEmpty()) {
				spinner_fokus.setSelection(fokus.get(bild.Fokus));
			}
			if (!filter.isEmpty()) {
				spinner_filter.setSelection(filter.get(bild.Filter));
			}
			if (!makro.isEmpty()) {
				spinner_makro.setSelection(makro.get(bild.Makro));
			}
			if (!mess.isEmpty()) {
				spinner_messmethode.setSelection(mess.get(bild.Messmethode));
			}
			if (!belichtung.isEmpty()) {
				spinner_belichtungs_korrektur.setSelection(belichtung
						.get(bild.Belichtungskorrektur));
			}
			if (!makrovf.isEmpty()) {
				spinner_makro_vf.setSelection(makrovf.get(bild.MakroVF));
			}
			if (!blitz.isEmpty()) {
				spinner_blitz.setSelection(blitz.get(bild.Blitz));
			}
			if (!blitzkorr.isEmpty()) {
				spinner_blitz_korrektur.setSelection(blitzkorr
						.get(bild.Blitzkorrektur));
			}
			edit_notizen.setText(bild.Notiz);
			edit_kamera_notizen.setText(bild.KameraNotiz);
		}
	}

	/*
	 * GPS Location f�r GeoTag der Bilder
	 */
	private void getLocation() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String locationProvider = LocationManager.GPS_PROVIDER;

		Location lastKnownLocation = locManager
				.getLastKnownLocation(locationProvider);
		if (lastKnownLocation != null) {
			piclat = lastKnownLocation.getLatitude();
			piclong = lastKnownLocation.getLongitude();
		}

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

		locManager.requestLocationUpdates(locationProvider, 120000, 100,
				locationListener);
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		} else {
			Toast toast2 = Toast.makeText(this, getString(R.string.gps_off),
					Toast.LENGTH_LONG);
			toast2.show();
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
		b.GeoTag = String.valueOf(piclong) + "' , '" + String.valueOf(piclat);
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
		film.Filmnotiz = settings.getString("FilmNotiz", " ");

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

			String encodedImage = Base64.encodeToString(pics, Base64.DEFAULT);
			DB.getDB().addPictureCreateNummer(mContext, f, b, picturesNumber,
					encodedImage);
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

	private class PictureSettingsPager extends PagerAdapter implements TitleProvider {

		private ArrayList<View> views;
		
		private Spinner setupSpecialSpinnerLenses(View view) {
			ArrayList<String> lenses = DB.getDB().getLensesForCamera(mContext, MY_DB_NAME, settings.getString("Kamera", ""));		
			if (lenses.size() == 0) {
				lenses.add(getString(R.string.no_selection));
			}		
			spinner_objektiv = (Spinner) view.findViewById(R.id.spinner_brennweite);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, lenses);
			spinner_objektiv.setAdapter(adapter);
			
			return spinner_objektiv;
		}
		
		private Spinner setupSpecialSpinnerApertures(View view) {
			// setup the special spinner for apertures
			// Get default aperture values from Database. Add additional values
			// ad-hoc: 1/2 or 1/3 stop values are added manually, not from database!
			int defaultAperture = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME, DB.MY_DB_TABLE_SETBLE);
			ArrayList<String> apertures = DB.getDB().getActivatedSettingsData(mContext, MY_DB_NAME, DB.MY_DB_TABLE_SETBLE);
			ArrayList<String> finalApertures = new ArrayList<String>();
			if (apertures.size() == 0) {
				finalApertures.add(getString(R.string.no_selection));
			} else {			
				for (String aperture : apertures) {
					if (aperture.equals("Auto")) {
						finalApertures.add(aperture);
					} else {
						if (settings.getString("blendenstufe", "1/1").equals("1/1")) {
							finalApertures.add(aperture);
						} else if (settings.getString("blendenstufe", "1/1").equals(
								"1/2")) {
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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, finalApertures);
			spinner_blende.setAdapter(adapter);
			spinner_blende.setSelection(defaultAperture);
			
			return spinner_blende;
		}
		
		Spinner setupSpinner(View view, int uiID, String dbName, String tableName) {

			ArrayList<String> values = DB.getDB().getActivatedSettingsData(mContext, dbName, tableName);
			int defaultValue = DB.getDB().getDefaultSettingNumber(mContext, dbName, tableName);
			if (values.size() == 0) {
				values.add(getString(R.string.no_selection));
			}

			Spinner spinner = (Spinner) view.findViewById(uiID);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, values);
			spinner.setAdapter(adapter);
			if (defaultValue >= values.size()) {
				spinner.setSelection(0);
			} else {
				spinner.setSelection(defaultValue);
			}

			return spinner;
		}

		public PictureSettingsPager(Context context) {
			final SharedPreferences.Editor editors = settings.edit();
			views = new ArrayList<View>();
			LayoutInflater inflater = getLayoutInflater();

			// shared listener for all spinners.
			OnItemSelectedListener listener = new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> view, View arg1, int arg2, long arg3) {
					
					if(view == spinner_objektiv){
						editors.putString("Objektiv", spinner_objektiv.getSelectedItem().toString());
					} else if(view == spinner_blende){
						editors.putString("blende", spinner_blende.getSelectedItem().toString());
					} else if(view == spinner_zeit){
						editors.putString("zeit", spinner_zeit.getSelectedItem().toString());
					} else if(view == spinner_fokus){
						editors.putString("fokus", spinner_fokus.getSelectedItem().toString());
					} else if(view == spinner_filter){
						editors.putString("filter", spinner_filter.getSelectedItem().toString());
					} else if(view == spinner_makro){
						editors.putString("makro", spinner_makro.getSelectedItem().toString());
					} else if(view == spinner_messmethode){
						editors.putString("messmethode", spinner_messmethode.getSelectedItem().toString());
					} else if(view == spinner_filter_vf){
						editors.putString("FilterVF", spinner_filter_vf.getSelectedItem().toString());
					} else if(view == spinner_belichtungs_korrektur){
						editors.putString("belichtungs_korrektur", spinner_belichtungs_korrektur.getSelectedItem().toString());
					} else if(view == spinner_makro_vf){
						editors.putString("makro_vf", spinner_makro_vf.getSelectedItem().toString());
					} else if(view == spinner_blitz){
						editors.putString("blitz", spinner_blitz.getSelectedItem().toString());
					} else if(view == spinner_blitz_korrektur){
						editors.putString("blitz_korrektur",spinner_blitz_korrektur.getSelectedItem().toString());	
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
			spinner_zeit = setupSpinner(firstPage, R.id.spinner_zeit, MY_DB_NAME, DB.MY_DB_TABLE_SETZEI);
			spinner_fokus = setupSpinner(firstPage, R.id.spinner_fokus, MY_DB_NAME, DB.MY_DB_TABLE_SETFOK);
			spinner_filter = setupSpinner(firstPage, R.id.spinner_filter, MY_DB_NAME, DB.MY_DB_TABLE_SETFIL);
			spinner_makro = setupSpinner(firstPage, R.id.spinner_makro, MY_DB_NAME, DB.MY_DB_TABLE_SETNM);
			
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
			if (settings.getString("Verlaengerung", "Faktor (*)").equals("Faktor (*)")) {
				// values are already correct....
			} else if (settings.getString("Verlaengerung", "Faktor (*)").equals("Blendenzugaben (+)")) {
				filterVFTable = DB.MY_DB_TABLE_SETFVF2;
				makroVFTable = DB.MY_DB_TABLE_SETMVF2;
			}
			
			spinner_messmethode = setupSpinner(secondPage, R.id.spinner_messmethode, MY_DB_NAME, DB.MY_DB_TABLE_SETMES);			
			spinner_filter_vf = setupSpinner(secondPage, R.id.spinner_filter_vf, MY_DB_NAME, filterVFTable);
			spinner_belichtungs_korrektur = setupSpinner(secondPage, R.id.spinner_belichtungs_korrektur, MY_DB_NAME, DB.MY_DB_TABLE_SETPLU);
			spinner_makro_vf = setupSpinner(secondPage, R.id.spinner_makro_vf, MY_DB_NAME, makroVFTable);
			spinner_blitz = setupSpinner(secondPage, R.id.spinner_blitz, MY_DB_NAME, DB.MY_DB_TABLE_SETBLI);
			spinner_blitz_korrektur = setupSpinner(secondPage, R.id.spinner_blitz_korrektur, MY_DB_NAME, DB.MY_DB_TABLE_SETKOR);
			
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
			edit_kamera_notizen = (EditText) thirdPage.findViewById(R.id.edit_kamera_notizen);

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

			return NewPictureActivity.CONTENT[position % NewPictureActivity.CONTENT.length];
		}

	}

}



