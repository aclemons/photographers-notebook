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
 * In dieser Activity kann ein neuer Film angelegt werden. Titel, Vorschaubild etc.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.database.DataSource;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
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
	Integer contentIndex = 0;
	byte[] pic, nopic;
	boolean mPreviewRunning = false;

	/*
	 * Interface Variablen
	 */
	TextView tv1, tv2, weiter, close, newFilm, cancel;
	EditText filmbezeichnung;
	PopupWindow pw;
	Spinner spinnerCamera, spinnerFF, spinnerSS, spinnerSSS, spinnerEM,
			spinnerTY;
	ToggleButton titleButton;
	EditText titleText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfilmone);
		mContext = this;
		filmbezeichnung = (EditText) findViewById(R.id.filmnotiz);
		cancel = (Button) findViewById(R.id.cancelAll);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		newFilm = (Button) findViewById(R.id.newAll);
		newFilm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("Foto", "showFilm");

				if (titleText.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.empty_title), Toast.LENGTH_SHORT)
							.show();
				} else if (DataSource.getInst(getApplicationContext()).isFilmTitleTaken(titleText.getText().toString())) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.title_taken), Toast.LENGTH_LONG)
							.show();
				} else {
					try {
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("Title", titleText.getText().toString());
						editor.putString("FilmBezeichnung", filmbezeichnung.getText().toString());
						editor.putString(
								"Datum",
								android.text.format.DateFormat.format(
										"dd.MM.yyyy", new java.util.Date())
										.toString());
						editor.putString("Kamera", spinnerCamera.getSelectedItem().toString());
						editor.putString("Filmformat", spinnerFF.getSelectedItem().toString());
						editor.putString("Empfindlichkeit", spinnerEM.getSelectedItem().toString());
						editor.putString("Filmtyp", spinnerTY.getSelectedItem().toString());
						editor.putString("Sonder1", spinnerSS.getSelectedItem().toString());
						editor.putString("Sonder2", spinnerSSS.getSelectedItem().toString());
						editor.putInt("BildNummerToBegin", 1);
						editor.putBoolean("EditMode", false);
						editor.commit();

						Film f = new Film();
						f.Titel = titleText.getText().toString();
						f.Filmbezeichnung = filmbezeichnung.getText().toString();
						f.Datum = android.text.format.DateFormat.format("dd.MM.yyyy", new java.util.Date()).toString();
						f.Kamera = spinnerCamera.getSelectedItem().toString();
						f.Filmformat = spinnerFF.getSelectedItem().toString();
						f.Empfindlichkeit = spinnerEM.getSelectedItem().toString();
						f.Filmtyp = spinnerTY.getSelectedItem().toString();
						f.Sonderentwicklung1 = spinnerSS.getSelectedItem().toString();
						f.Sonderentwicklung2 = spinnerSSS.getSelectedItem().toString();

						DataSource.getInst(mContext).addFilm(f);
						// TODO: Might be a better idea to give the film as parameter to the new activity...
						Intent myIntent = new Intent(getApplicationContext(), NewPictureActivity.class);
						startActivityForResult(myIntent, 1);
						
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.input_error),
								Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			}

		});
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);

	}

	@Override
	protected void onResume() {
		super.onResume();
		contentIndex = 0;

		titleText = (EditText) findViewById(R.id.texttitle);
		titleButton = (ToggleButton) findViewById(R.id.toggletitle);
		titleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (titleButton.isChecked()) {
					setAutomaticTitle();
				} else {
					titleText.setText("");
				}
			}
		});

		spinnerCamera = setupSpinner(R.id.spinnerCamera, DB.MY_DB_TABLE_SETCAM);
		spinnerFF = setupSpinner(R.id.spinnerFF, DB.MY_DB_TABLE_SETFF);
		spinnerSS = setupSpinner(R.id.spinnerSS, DB.MY_DB_TABLE_SETSON);
		spinnerSSS = setupSpinner(R.id.spinnerSSS, DB.MY_DB_TABLE_SETSON);
		spinnerEM = setupSpinner(R.id.spinnerEM, DB.MY_DB_TABLE_SETEMP);
		spinnerTY = setupSpinner(R.id.spinnerTY, DB.MY_DB_TABLE_SETTYP);

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

	private Spinner setupSpinner(int uiID, String tableName) {

		ArrayList<String> values = DB.getDB().getActivatedSettingsData(
				mContext, tableName);
		int defaultValue = DB.getDB().getDefaultSettingNumber(mContext,
				tableName);
		if (values.size() == 0) {
			values.add(getString(R.string.no_selection));
		}

		Spinner spinner = (Spinner) findViewById(uiID);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		if (defaultValue >= values.size()) {
			spinner.setSelection(0);
		} else {
			spinner.setSelection(defaultValue);
		}

		return spinner;
	}

	public void popupmenue() {
		Resources res = getResources();
		final String[] puContent = res
				.getStringArray(R.array.strings_tutorial_2);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layoutOwn1 = inflater.inflate(R.layout.popup,
				(ViewGroup) findViewById(R.id.widget), false);

		pw = new PopupWindow(layoutOwn1, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pw.setAnimationStyle(7);
		pw.setBackgroundDrawable(new ColorDrawable());
		tv1 = (TextView) layoutOwn1.findViewById(R.id.textview_pop);
		tv1.setText(puContent[contentIndex]);
		contentIndex++;

		weiter = (Button) layoutOwn1.findViewById(R.id.button_popup);
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

	private void setAutomaticTitle() {
		Date dt = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		titleText.setText(df.format(dt) + " " + getString(R.string.film));
	}

}
