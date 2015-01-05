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
 * In dieser Activity sieht man den ausgew�hlten Film mit allen Infos und kann sich die zugeh�rigen Bilder betrachten und ausw�hlen.
 */

import java.util.Collections;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.helper.FilmExportTask;
import unisiegen.photographers.helper.FilmIconFactory;
import unisiegen.photographers.helper.PicturesArrayAdapter;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

public class FilmContentActivity extends PhotographersNotebookActivity {

	private Context mContext;

	/*
	 * User-Interface Elemente
	 */
	TextView freecell;
	int bilderimfilm;
	LinearLayout infoBlock1;
	TextView filmcam;
	LinearLayout infoBlock2;
	TextView filmtit;
	TitlePageIndicator mIndicator;
	PopupWindow pw;
	EditText picnotizedit = null;
	EditText picnotizcamedit = null;

	boolean minimizes;
	SharedPreferences settings;
	private Film film;
	private SharedPreferences.Editor editor;

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
		infoBlock1 = (LinearLayout) findViewById(R.id.infoblock1);
		infoBlock2 = (LinearLayout) findViewById(R.id.infoblock2);
		Button buttonContinueFilm = (Button) findViewById(R.id.button_goon);

		editor = settings.edit();

		film = DB.getDB().getFilm(mContext,
				getIntent().getExtras().getString("ID"));

		buttonContinueFilm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

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

				for (Bild bild : film.Bilder) {
					int bildNummer = Integer.valueOf(bild.Bildnummer
							.replaceAll("[\\D]", ""));
					if (bildNummer > biggestNumber) {
						biggestNumber = bildNummer;
					}
				}

				editor.putInt("BildNummerToBegin", biggestNumber + 1);
				editor.putBoolean("EditMode", true);
				editor.commit();
				Intent myIntent = new Intent(getApplicationContext(),
						NewPictureActivity.class);
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

	@SuppressLint("NewApi")
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

		Bitmap b = new FilmIconFactory().createBitmap(film);
		Drawable drawable = new BitmapDrawable(getResources(), b);
		if (android.os.Build.VERSION.SDK_INT >= 14) {
			try {
				getActionBar().setIcon(drawable);
			} catch (Exception e) {
				Log.v("check", e.toString());
			}
		}

		ImageView vorschauImage = (ImageView) findViewById(R.id.vorschau);
		vorschauImage.setImageBitmap(new FilmIconFactory().createBitmap(film));

		TextView filmbezeichnung = (TextView) findViewById(R.id.filmnotiz);
		filmbezeichnung.setText(film.Filmbezeichnung);

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

		Collections.sort(film.Bilder);

		PicturesArrayAdapter adapter = new PicturesArrayAdapter(mContext,
				film.Bilder, 1);
		ListView myList = (ListView) findViewById(android.R.id.list);
		myList.setOnItemClickListener(myClickListener);
		myList.setOnItemLongClickListener(myLongClickListener);
		myList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.filmmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.opt_openSettings) {
			Intent openSettings = new Intent(getApplicationContext(),
					EditSettingsActivity.class);
			startActivityForResult(openSettings, 0);
			return true;
		} else if (item.getItemId() == R.id.opt_editfilm) {
			Intent editFilm = new Intent(getApplicationContext(),
					EditFilmActivity.class);
			editFilm.putExtra("ID", film.Titel);
			startActivity(editFilm);
			return true;
		} else if (item.getItemId() == R.id.opt_exportfilm) {
			new FilmExportTask(film.Titel, this).execute();
			return true;
		} else if (item.getItemId() == R.id.opt_deletefilm) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.question_delete));
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							DB.getDB().deleteFilms(mContext,
									new String[] { film.Titel });
							finish();
						}
					});
			builder.setNegativeButton(getString(R.string.no),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			AlertDialog alert = builder.create();
			alert.show();

			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
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
			deleteButton.setText(getString(R.string.delete_picture));
			editButton.setText(getString(R.string.edit_picture));

			editButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pw.dismiss();
					String selektiertesBild = (String) third.getText();
					Intent myIntent = new Intent(getApplicationContext(),
							NewPictureActivity.class);
					myIntent.putExtra("picToEdit", selektiertesBild);
					editor.putString("Title", film.Titel);
					editor.putBoolean("EditMode", true);
					editor.commit();
					startActivity(myIntent);
				}
			});

			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage(getString(R.string.question_delete))
							.setCancelable(false)
							.setPositiveButton(getString(R.string.yes),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

											for (Bild bild : film.Bilder) {
												if (bild.Bildnummer
														.equals(third.getText())) {
													DB.getDB().deletePicture(
															mContext, film,
															bild);
													break;
												}
											}

											bilderimfilm -= 1;
											pw.dismiss();
											onResume();
											Toast.makeText(
													getApplicationContext(),
													getString(R.string.deleted),
													Toast.LENGTH_SHORT).show();
										}
									})
							.setNegativeButton(getString(R.string.no),
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

			pw = new PopupWindow(layoutOwn,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pw.setAnimationStyle(7);
			pw.setBackgroundDrawable(new ColorDrawable());
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);

			return true;
		}
	};

	public OnItemClickListener myClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent i = new Intent(getApplicationContext(),
					FotoContentActivity.class);
			i.putExtra("ID", film.Titel);
			i.putExtra("selectedItem", arg2);
			startActivity(i);
		}
	};

}