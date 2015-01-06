/* Copyright (C) 2012 Sebastian Draxler, Alexander Boden, Christian Woehrl (Committers)
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

import java.util.ArrayList;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Film;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class EditFilmActivity extends PhotographersNotebookActivity {

	String filmTitle;
	Film film;
	Context mContext;
	Button cancel, save;
	PopupWindow pw;
	Spinner spinnerCamera, spinnerFF, spinnerSS, spinnerSSS, spinnerEM,
			spinnerTY;
	ToggleButton titleButton;
	EditText titleText, filmbezeichnung;
	TextView tv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfilmone);
		filmTitle = getIntent().getStringExtra("ID");
		mContext = this;
		filmbezeichnung = (EditText) findViewById(R.id.filmnotiz);

		cancel = (Button) findViewById(R.id.cancelAll);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		save = (Button) findViewById(R.id.newAll);
		save.setText(getString(R.string.save_changes));
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				film.Filmbezeichnung = filmbezeichnung.getText().toString();
				film.Kamera = spinnerCamera.getSelectedItem().toString();
				film.Filmformat = spinnerFF.getSelectedItem().toString();
				film.Empfindlichkeit = spinnerEM.getSelectedItem().toString();
				film.Filmtyp = spinnerTY.getSelectedItem().toString();
				film.Sonderentwicklung1 = spinnerSS.getSelectedItem()
						.toString();
				film.Sonderentwicklung2 = spinnerSSS.getSelectedItem()
						.toString();

				DB.getDB().updateFilmDetails(mContext, film);

				finish();
			}

		});

		titleText = (EditText) findViewById(R.id.texttitle);
		titleText.setEnabled(false); // Editing titles is not possible for now.

		titleButton = (ToggleButton) findViewById(R.id.toggletitle);
		titleButton.setVisibility(ToggleButton.GONE);

		TextView spacerView = (TextView) findViewById(R.id.freecell_spacer);
		spacerView.setVisibility(TextView.GONE);

		tv = (TextView) findViewById(R.id.freecell1);
		tv.setVisibility(TextView.GONE); // TODO: Maybe add new layout for this
											// activity, or merge activity with
											// NewFilmActivity.

	}

	@Override
	protected void onResume() {
		super.onResume();

		film = DB.getDB().getFilm(mContext, filmTitle);

		if (film == null) {
			Log.e("ERROR",
					"Film not found in database, nothing to edit here ...");
			finish();
		}

		spinnerCamera = setupSpinner(R.id.spinnerCamera, DB.MY_DB_TABLE_SETCAM,
				film.Kamera);
		spinnerFF = setupSpinner(R.id.spinnerFF, DB.MY_DB_TABLE_SETFF,
				film.Filmformat);
		spinnerSS = setupSpinner(R.id.spinnerSS, DB.MY_DB_TABLE_SETSON,
				film.Sonderentwicklung1);
		spinnerSSS = setupSpinner(R.id.spinnerSSS, DB.MY_DB_TABLE_SETSON,
				film.Sonderentwicklung2);
		spinnerEM = setupSpinner(R.id.spinnerEM, DB.MY_DB_TABLE_SETEMP,
				film.Empfindlichkeit);
		spinnerTY = setupSpinner(R.id.spinnerTY, DB.MY_DB_TABLE_SETTYP,
				film.Filmtyp);

		titleText.setText(film.Titel);
		filmbezeichnung.setText(film.Filmbezeichnung);

	}

	private Spinner setupSpinner(int uiID, String tableName,
			String selectedSetting) {

		int selectedItem;

		ArrayList<String> values = DB.getDB().getActivatedSettingsData(
				mContext, tableName);
		if (values.size() == 0) {
			values.add(getString(R.string.no_selection));
		}

		Spinner spinner = (Spinner) findViewById(uiID);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		if (values.contains(selectedSetting)) {
			selectedItem = values.indexOf(selectedSetting);
			spinner.setSelection(selectedItem);
		}

		// TODO: Wenn das falsche Set ausgew�hlt ist, Fehler anzeigen!

		return spinner;
	}

}
