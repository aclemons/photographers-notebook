package unisiegen.photographers.activity;

/**
 * In dieser Activity kann ein neuer Film angelegt werden. Titel, Vorschaubild etc.
 */

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import unisiegen.photographers.database.DB;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class NewFilmActivity extends PhotographersNotebookActivity {

	/*
	 * Sonstige Variablen
	 */
	SharedPreferences settings;
	Context mContext;

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

	static String MY_DB_NAME;

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
						TakePreviewPictureActivity.class);
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
							NewPictureActivity.class);
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
							getString(R.string.input_error), Toast.LENGTH_SHORT)
							.show();
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
				titleText.setText(df.format(dt) + " "
						+ getString(R.string.film));
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

	private void readDB() {

		int number = 0;
		listCamera = DB.getDB().getActivatedSettingsData(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETCAM);
		for (String cam : listCamera) {
			if (cam.equals(settings.getString("KamDef", ""))) {
				camdef = number;
			}
			number++;
		}
		if (listCamera.size() == 0) {
			listCamera.add(getString(R.string.no_selection));
		}

		ffdef = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETFF);
		listFF = DB.getDB().getActivatedSettingsData(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETFF);
		if (listFF.size() == 0) {
			listFF.add(getString(R.string.no_selection));
		}

		listSSS = DB.getDB().getActivatedSettingsData(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETSON);
		listSS = DB.getDB().getActivatedSettingsData(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETSON);
		ssdef = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETSON);
		if (listSSS.size() == 0) {
			listSSS.add(getString(R.string.no_selection));
			listSS.add(getString(R.string.no_selection));
		}

		listEM = DB.getDB().getActivatedSettingsData(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETEMP);
		emdef = DB.getDB().getDefaultSettingNumber(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETEMP);
		if (listEM.size() == 0) {
			listEM.add(getString(R.string.no_selection));
		}

		listTY = DB.getDB().getActivatedSettingsData(mContext, MY_DB_NAME,
				DB.MY_DB_TABLE_SETTYP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent) Bild �bergabe (Wenn man ein Vorschau bild macht,
	 * geschieht dies in einer neuen Popup Activity, ist dies fertig �bergibt
	 * diese Activity als R�ckgabe Wert das Bild, welches zusammen mit den
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
		Resources res = getResources();
		final String[] puContent = res
				.getStringArray(R.array.strings_tutorial_2);
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