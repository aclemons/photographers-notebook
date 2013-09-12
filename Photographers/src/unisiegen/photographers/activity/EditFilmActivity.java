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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.content.Context;
import android.hardware.Camera;
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
import android.widget.Toast;
import android.widget.ToggleButton;

public class EditFilmActivity extends PhotographersNotebookActivity {

	String filmTitle;
	Film film;
	Context mContext;
	TextView tv1, tv2, weiter, close;
	Button cancel, save, vorschau;
	PopupWindow pw;
	Spinner spinnerCamera, spinnerFF, spinnerSS, spinnerSSS, spinnerEM,
			spinnerTY;
	ToggleButton titleButton;
	EditText titleText, filmnotiz;
	Camera mCamera;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfilmone);
		filmTitle = getIntent().getStringExtra("ID");
		mContext = this;
		filmnotiz = (EditText) findViewById(R.id.filmnotiz);
		
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
				
				if (titleText.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), getString(R.string.empty_title), Toast.LENGTH_SHORT).show();
				} else if (DB.getDB().checkIfFilmTitleIsTaken(getApplicationContext(), titleText.getText().toString())) {
					Toast.makeText(getApplicationContext(), getString(R.string.title_taken), Toast.LENGTH_LONG).show();
				} else {
						
						Film f = new Film();
						f.Titel = titleText.getText().toString();
						f.Filmnotiz = filmnotiz.getText().toString();
						f.Datum = android.text.format.DateFormat.format(
								"dd.MM.yyyy", new java.util.Date()).toString();
						f.Kamera = spinnerCamera.getSelectedItem().toString();
						f.Filmformat = spinnerFF.getSelectedItem().toString();
						f.Empfindlichkeit = spinnerEM.getSelectedItem().toString();
						f.Filmtyp = spinnerTY.getSelectedItem().toString();
						f.Sonderentwicklung1 = spinnerSS.getSelectedItem()
								.toString();
						f.Sonderentwicklung2 = spinnerSSS.getSelectedItem()
								.toString();
						
						finish();				
				}
			}
			
		});
		
		titleText = (EditText) findViewById(R.id.texttitle);
		titleText.setEnabled(false);
		
		titleButton = (ToggleButton) findViewById(R.id.toggletitle);
		titleButton.setVisibility(ToggleButton.GONE);
		
		vorschau = (Button) findViewById(R.id.vorschau);
		vorschau.setVisibility(Button.GONE);
			
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		film = DB.getDB().getFilm(mContext, filmTitle);;
		
		spinnerCamera = setupSpinner(R.id.spinnerCamera, DB.MY_DB_TABLE_SETCAM, film.Kamera);
		spinnerFF = setupSpinner(R.id.spinnerFF, DB.MY_DB_TABLE_SETFF, film.Filmformat);
		spinnerSS = setupSpinner(R.id.spinnerSS, DB.MY_DB_TABLE_SETSON, film.Sonderentwicklung1);
		spinnerSSS = setupSpinner(R.id.spinnerSSS, DB.MY_DB_TABLE_SETSON, film.Sonderentwicklung2);
		spinnerEM = setupSpinner(R.id.spinnerEM, DB.MY_DB_TABLE_SETEMP, film.Empfindlichkeit);
		spinnerTY = setupSpinner(R.id.spinnerTY, DB.MY_DB_TABLE_SETTYP, film.Filmtyp);
		
		titleText.setText(film.Titel);
		filmnotiz.setText(film.Filmnotiz);

	}

	private Spinner setupSpinner(int uiID, String tableName, String selectedSetting) {

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
		
		//TODO: Wenn das falsche Set ausgewählt ist, Fehler anzeigen!
		
		return spinner;
	}

}
