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
 * Activity f�r die Auswahl eines Film (View die beim Start angezeigt wird)
 * Hier kann man einen bestehenden Film ausw�hlen, bearbeiten und betrachten
 * Oder man kann einen neuen Film starten
 * 
 * Hier werden einige Methoden genauer erkl�rt, viele der Methoden kommen in anderen Activitys in selber Art 
 * und Weise wieder vor und werden dort dann nicht mehr ausf�hrlich besprochen.
 */

import java.io.File;
import java.util.ArrayList;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.database.DataSource;
import unisiegen.photographers.helper.FilmExportTask;
import unisiegen.photographers.helper.FilmIconFactory;
import unisiegen.photographers.helper.FilmImportTask;
import unisiegen.photographers.helper.FilmsViewHolder;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FilmSelectionActivity extends PhotographersNotebookActivity {

	private static final int PICKFILE_RESULT_CODE = 1;
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

	FilmIconFactory filmIconFactory;

	private Context mContext;
	private Integer contentIndex = 0;

	ArrayList<Film> filme;
	ArrayAdapter<Film> adapter;
	private DataSource dataSource;

	@Override
	protected void onResume() {
		super.onResume();
		myList = (ListView) findViewById(android.R.id.list);
		ImageView backgroundimage = (ImageView) findViewById(R.id.image);
		contentIndex = 0;

		if (settings.getInt("FIRSTSTART", 0) == 0) {
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.post(new Runnable() {
				public void run() {
					popupmenue();
				}
			});
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("FIRSTSTART", 1);
			editor.commit();
		}

		
		Log.v("FilmSelectionActivity", "Trying to open Datasource...");
		dataSource = DataSource.getInst(mContext);		
		filme = dataSource.getFilms();

		if (filme.isEmpty()) {
			backgroundimage.setVisibility(View.VISIBLE);
		} else {
			backgroundimage.setVisibility(View.INVISIBLE);
		}

		filmIconFactory = new FilmIconFactory();

		adapter = new FilmsArrayAdapter(mContext, filme, 1);
		myList.setOnItemClickListener(clickListener);
		myList.setOnItemLongClickListener(longClickListener);
		myList.setAdapter(adapter);

	}

	public void refreshUI() {
		Log.v("Importer", "Refreshing UI...");
		onResume();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filmauswahl);
		mContext = this;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		if (settings.getInt("FIRSTSTART", 99) == 99) { // Only do this on the
														// very first start,
														// when FIRSTSTART is
														// not yet set.
			new ResetSettingsTask().execute();
		}
	}

	private final class EditFilmDialogAction implements OnClickListener {
		private final TextView title;

		private EditFilmDialogAction(TextView ids) {
			this.title = ids;
		}

		@Override
		public void onClick(View v) {
			SharedPreferences.Editor editor = settings.edit();

			Film film = DataSource.getInst(mContext).getFilm(title.getText().toString());

			editor.putString("Title", film.Titel);
			editor.putString("Datum", film.Datum);
			editor.putString("Kamera", film.Kamera);

			editor.putString("Filmformat", film.Filmformat);
			editor.putString("Empfindlichkeit", film.Empfindlichkeit);
			editor.putString("Filmtyp", film.Filmtyp);
			editor.putString("Sonder1", film.Sonderentwicklung1);
			editor.putString("Sonder2", film.Sonderentwicklung2);

			int biggestNumber = 0;
			for (Bild bild : film.Bilder) {

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
					NewPictureActivity.class);
			startActivityForResult(myIntent, 1);
			pw.dismiss();
		}
	}

	private final class DeleteFilmDialogAction implements OnClickListener {
		private final TextView ids;

		private DeleteFilmDialogAction(TextView ids) {
			this.ids = ids;
		}

		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(getString(R.string.question_delete));
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							DataSource.getInst(mContext).deleteFilm(ids.getText().toString());
							
							Toast.makeText(getApplicationContext(),
									getString(R.string.deleted),
									Toast.LENGTH_SHORT).show();
							pw.dismiss();
							onResume();
						}
					});
			builder.setNegativeButton(getString(R.string.no),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							pw.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public class FilmsArrayAdapter extends ArrayAdapter<Film> {

		private LayoutInflater inflater;

		public FilmsArrayAdapter(Context context, ArrayList<Film> planetList,
				int number) {
			super(context, R.layout.film_table_cell, R.id.filmtitle, planetList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Film selectedFilm = (Film) this.getItem(position);
			TextView textView;
			TextView textViewDate;
			TextView textViewCam;
			TextView textViewPics;
			ImageView imageViewBild;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.film_table_cell, null);
				textView = (TextView) convertView.findViewById(R.id.filmtitle);
				textViewDate = (TextView) convertView.findViewById(R.id.date);
				textViewCam = (TextView) convertView.findViewById(R.id.cam);
				textViewPics = (TextView) convertView.findViewById(R.id.fotos);
				imageViewBild = (ImageView) convertView
						.findViewById(R.id.imageview);
				convertView.setTag(new FilmsViewHolder(textView, textViewDate,
						textViewCam, textViewPics, imageViewBild));
			} else {
				FilmsViewHolder viewHolder = (FilmsViewHolder) convertView
						.getTag();
				textViewDate = viewHolder.getTextViewTime();
				textView = viewHolder.getTextViewName();
				textViewCam = viewHolder.getTextViewCam();
				textViewPics = viewHolder.getTextViewPics();
				imageViewBild = viewHolder.getBildView();
			}
			textViewDate.setText(selectedFilm.Datum);
			textView.setText(selectedFilm.Titel);
			textViewCam.setText(selectedFilm.Kamera);
			textViewPics.setText(selectedFilm.Pics + " "
					+ getString(R.string.pictures));

			imageViewBild.setImageBitmap(filmIconFactory
					.createBitmap(selectedFilm));
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

			LinearLayout layout = (LinearLayout) arg1;
			final TextView id = (TextView) layout.findViewById(R.id.filmtitle);

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
			Button editFilmButton = (Button) layoutOwn
					.findViewById(R.id.editFilmButton);

			editFilmButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							EditFilmActivity.class);
					i.putExtra("ID", id.getText().toString());
					pw.dismiss();
					startActivity(i);
				}
			});

			exportButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					exportFilm(id.getText().toString());
					pw.dismiss();
				}
			});
			deleteButton.setOnClickListener(new DeleteFilmDialogAction(id));
			editButton.setOnClickListener(new EditFilmDialogAction(id));
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pw.dismiss();
				}
			});

			pw = new PopupWindow(layoutOwn,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pw.setAnimationStyle(7);
			pw.setBackgroundDrawable(new ColorDrawable());
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);

			return true;
		}
	};

	public OnItemClickListener clickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			LinearLayout layout = (LinearLayout) arg1;
			TextView id = (TextView) layout.findViewById(R.id.filmtitle);

			Intent myIntent = new Intent(getApplicationContext(),
					FilmContentActivity.class);
			myIntent.putExtra("ID", id.getText().toString());
			startActivityForResult(myIntent, 0);
		}
	};

	/**
	 * Popup f�r Tutorial
	 */
	public void popupmenue() {

		Resources res = getResources();
		final String[] puContent = res
				.getStringArray(R.array.strings_tutorial_1);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.startmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_newfilm) {
			Intent newFilm = new Intent(getApplicationContext(),
					NewFilmActivity.class);
			startActivityForResult(newFilm, 0);
			return true;

		} else if (item.getItemId() == R.id.opt_openSettings) {
			Intent openSettings = new Intent(getApplicationContext(),
					EditSettingsActivity.class);
			startActivityForResult(openSettings, 0);
			return true;
		} else if (item.getItemId() == R.id.action_import_film) {
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.setType("file/*");
			startActivityForResult(
					Intent.createChooser(i, getString(R.string.import_film)),
					PICKFILE_RESULT_CODE);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case PICKFILE_RESULT_CODE:
			if (resultCode == RESULT_OK) {
				File file = new File(data.getData().getPath());
				new FilmImportTask(this, file, this).execute();
			}
			break;

		}
	}

	public void exportFilm(String FilmID) {
		new FilmExportTask(FilmID, this).execute();

	}

	public class ResetSettingsTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		public ResetSettingsTask() {
			dialog = new ProgressDialog(mContext);
		}

		protected void onPreExecute() {
			this.dialog.setMessage(getString(R.string.firststart));
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
		}

		protected Boolean doInBackground(final String... args) {
//			try {
//				long t0 = System.currentTimeMillis();
//				DB.getDB().createOrRebuildSettingsTable(mContext, DB.MY_DB_SET);
//				DB.getDB()
//						.createOrRebuildSettingsTable(mContext, DB.MY_DB_SET1);
//				DB.getDB()
//						.createOrRebuildSettingsTable(mContext, DB.MY_DB_SET2);
//				DB.getDB()
//						.createOrRebuildSettingsTable(mContext, DB.MY_DB_SET3);
//				long t1 = System.currentTimeMillis();
//				DB.getDB().createOrRebuildNummernTable(mContext);
//				long t2 = System.currentTimeMillis();
//				DB.getDB().createOrRebuildFilmTable(mContext);
//				long t3 = System.currentTimeMillis();
//
//				double setCreation = (double) t1 - (double) t0;
//				double nummernCreation = (double) t2 - (double) t1;
//				double filmCreation = (double) t3 - (double) t2;
//				Log.v("dbcreation", "Time used for Settings creation: "
//						+ setCreation + "ms.");
//				Log.v("dbcreation", "Time used for Film creation: "
//						+ (filmCreation + nummernCreation) + "ms.");
//
//			} catch (Exception e) {
//				Log.v("DEBUG", "Fehler bei Set-Erstellung : " + e);
//			}

			return null;
		}
	}

}