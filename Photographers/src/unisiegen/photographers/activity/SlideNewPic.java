package unisiegen.photographers.activity;

/**
 * Hier "schie�t" man die neuen Fotos! Man nimmt die Bildeinstellungen vor und speichert das Bild.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.export.BildObjekt;
import unisiegen.photographers.export.Film;
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

public class SlideNewPic extends PhotographersNotebookActivity {

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

	static String MY_DB_NAME;

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
		if (settings.getString("geoTag", "nein").equals("ja")) {
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

		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		int aktuellebildnummer = settings.getInt("BildNummerToBegin", 1);
		nummerView.setText(getString(R.string.picture) + " "
				+ aktuellebildnummer);

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

		ArrayList<BildObjekt> bilder = DB.getDB().getBild(mContext, filmTitle,
				picNumber);
		if (bilder == null || bilder.size() != 1) {
			bildtoedit = false;
			Log.v("Check", "Kein Bild vorhanden");
		} else {
			BildObjekt bild = bilder.get(0);
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
	private BildObjekt getBildFromUI() {
		BildObjekt b = new BildObjekt();
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
		BildObjekt b = getBildFromUI();

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
		BildObjekt b = getBildFromUI();
		DB.getDB().updatePicture(mContext, f, b);
	}

	private void fuellen() {
		al_spinner_filter_vf = new ArrayList<String>();
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

		al_spinner_objektiv = DB.getDB().getLensesForCamera(mContext,
				MY_DB_NAME, settings.getString("Kamera", ""));
		for (String lens : al_spinner_objektiv) {
			objektiv.put(lens, index);
			index++;
		}
		if (al_spinner_objektiv.size() == 0) {
			al_spinner_objektiv.add(getString(R.string.no_selection));
		}

		// Get default aperture values from Database. Add additional values
		// ad-hoc: 1/2 or 1/3 stop values are added manually, not from database!
		defblende = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETBLE);
		ArrayList<String> tmp = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETBLE);
		al_spinner_blende = new ArrayList<String>();
		index = 0;
		for (String aperture : tmp) {
			if (aperture.equals("Auto")) {
				al_spinner_blende.add(aperture);
				blende.put(aperture, index++);
			} else {
				if (settings.getString("blendenstufe", "1/1").equals("1/1")) {
					al_spinner_blende.add(aperture);
					blende.put(aperture, index++);
				} else if (settings.getString("blendenstufe", "1/1").equals(
						"1/2")) {
					al_spinner_blende.add(aperture);
					blende.put(aperture, index++);
					al_spinner_blende.add(aperture + " + 1/2");
					blende.put(aperture + " + 1/2", index++);
				} else {
					al_spinner_blende.add(aperture);
					blende.put(aperture, index++);
					al_spinner_blende.add(aperture + " + 1/3");
					blende.put(aperture + " + 1/3", index++);
					al_spinner_blende.add(aperture + " + 2/3");
					blende.put(aperture + " + 2/3", index++);
				}
			}
		}
		if (tmp.size() == 0) {
			al_spinner_blende.add(getString(R.string.no_selection));
		}

		index = 0;
		defzeit = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETZEI);
		al_spinner_zeit = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETZEI);
		for (String time : al_spinner_zeit) {
			zeit.put(time, index++);
		}
		if (al_spinner_zeit.size() == 0) {
			al_spinner_zeit.add(getString(R.string.no_selection));
		}

		index = 0;
		deffokus = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETFOK);
		al_spinner_focus = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETFOK);
		for (String focus : al_spinner_focus) {
			fokus.put(focus, index++);
		}
		if (al_spinner_focus.size() == 0) {
			al_spinner_focus.add(getString(R.string.no_selection));
		}

		index = 0;
		deffilter = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETFIL);
		al_spinner_filter = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETFIL);
		for (String tmpFilter : al_spinner_filter) {
			filter.put(tmpFilter, index++);
		}
		if (al_spinner_filter.size() == 0) {
			al_spinner_filter.add(getString(R.string.no_selection));
		}

		index = 0;
		defmakro = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETNM);
		al_spinner_makro = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETNM);
		for (String tmpMakro : al_spinner_makro) {
			makro.put(tmpMakro, index++);
		}
		if (al_spinner_makro.size() == 0) {
			al_spinner_makro.add(getString(R.string.no_selection));
		}

		index = 0;
		defmessung = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETMES);
		al_spinner_messmethode = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETMES);
		for (String messMethode : al_spinner_messmethode) {
			mess.put(messMethode, index++);
		}
		if (al_spinner_messmethode.size() == 0) {
			al_spinner_messmethode.add(getString(R.string.no_selection));
		}

		index = 0;
		defkorr = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETPLU);
		al_spinner_belichtungs_korrektur = DB.getDB().getActivatedSettingsData(
				mContext, MY_DB_NAME, DB.MY_DB_TABLE_SETPLU);
		for (String korrektur : al_spinner_belichtungs_korrektur) {
			belichtung.put(korrektur, index++);
		}
		if (al_spinner_belichtungs_korrektur.size() == 0) {
			al_spinner_belichtungs_korrektur
					.add(getString(R.string.no_selection));
		}

		String filterVFTable = DB.MY_DB_TABLE_SETFVF;
		String makroVFTable = DB.MY_DB_TABLE_SETMVF;

		if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Faktor (*)")) {

			// values are already correct....

		} else if (settings.getString("Verlaengerung", "Faktor (*)").equals(
				"Blendenzugaben (+)")) {

			filterVFTable = DB.MY_DB_TABLE_SETFVF2;
			makroVFTable = DB.MY_DB_TABLE_SETMVF2;

		}

		index = 0;
		deffiltervf = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				filterVFTable);
		al_spinner_filter_vf = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, filterVFTable);
		for (String filterVF : al_spinner_filter_vf) {
			filtervf.put(filterVF, index++);
		}
		if (al_spinner_filter_vf.size() == 0) {
			al_spinner_filter_vf.add(getString(R.string.no_selection));
		}

		index = 0;
		defmakrovf = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				makroVFTable);
		al_spinner_makro_vf = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, makroVFTable);
		for (String makroVF : al_spinner_makro_vf) {
			makrovf.put(makroVF, index++);
		}
		if (al_spinner_makro_vf.size() == 0) {
			al_spinner_makro_vf.add(getString(R.string.no_selection));
		}

		index = 0;
		defblitz = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETBLI);
		al_spinner_blitz = DB.getDB().getActivatedSettingsData(mContext,
				MY_DB_NAME, DB.MY_DB_TABLE_SETBLI);
		for (String blitzval : al_spinner_blitz) {
			blitz.put(blitzval, index++);
		}
		if (al_spinner_blitz.size() == 0) {
			al_spinner_blitz.add(getString(R.string.no_selection));
		}

		index = 0;
		defblitzkorr = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETKOR);
		al_spinner_blitz_korrektur = DB.getDB().getActivatedSettingsData(
				mContext, MY_DB_NAME, DB.MY_DB_TABLE_SETKOR);
		for (String blitzval : al_spinner_blitz_korrektur) {
			blitzkorr.put(blitzval, index++);
		}
		if (al_spinner_blitz_korrektur.size() == 0) {
			al_spinner_blitz_korrektur.add(getString(R.string.no_selection));
		}
	}

	/*
	 * HilfsKlassen f�r SlideView etc.
	 */
	public void setFooterColor(int footerColor) {
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

}
