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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.export.BildObjekt;
import unisiegen.photographers.export.Film;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import com.thoughtworks.xstream.XStream;

public class FilmAuswahlActivity extends PhotographersNotebookActivity {

	/**
	 * This variable saves the name of the current set for your gear. If this is
	 * changed, a different database is used to store your gear... Sets may be
	 * broken at the moment. TODO: Test
	 */
	static String MY_DB_NAME;

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

	private Context mContext;
	private Integer contentIndex = 0;

	@Override
	protected void onResume() {
		super.onResume();
		ImageView image = (ImageView) findViewById(R.id.image);
		myList = (ListView) findViewById(android.R.id.list);
		TextView pics = (TextView) findViewById(R.id.picanzahl);
		contentIndex = 0;

		if (settings.getInt("FIRSTSTART", 0) == 1) {
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.post(new Runnable() {
				public void run() {
					popupmenue();
				}
			});
		}
		ArrayList<Films> listItems = new ArrayList<Films>();
		int gesamtPics = 0;
		List<Film> filme = DB.getDB().getFilme(mContext);

		for (Film film : filme) {
			listItems.add(new Films(film));
			gesamtPics = gesamtPics + film.Bilder.size();
		}

		if (gesamtPics == 0) {
			myList.setVisibility(ListView.GONE);
			image.setVisibility(ImageView.VISIBLE);
		} else {
			myList.setVisibility(ListView.VISIBLE);
			image.setVisibility(ImageView.GONE);
		}
		pics.setText(gesamtPics + " " + getString(R.string.pictures));
		ArrayAdapter<Films> adapter = new FilmsArrayAdapter(mContext,
				listItems, 1);
		myList.setOnItemClickListener(notlongClickListener);
		myList.setOnItemLongClickListener(longClickListener);
		myList.setAdapter(adapter);

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filmauswahl);
		mContext = this;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		if (settings.getInt("FIRSTSTART", 0) == 0) {

			new ResetSettingsTask().execute();
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("FIRSTSTART", 1);
			editor.commit();
		}
		Button newFilm = (Button) findViewById(R.id.newFilm);
		newFilm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(),
						NewFilmActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});
	}

	private final class EditFilmDialogAction implements OnClickListener {
		private final TextView ids;

		private EditFilmDialogAction(TextView ids) {
			this.ids = ids;
		}

		@Override
		public void onClick(View v) {
			SharedPreferences.Editor editor = settings.edit();

			Film film = DB.getDB().getFilm(mContext, ids.getText().toString());

			editor.putString("Title", film.Titel);
			editor.putString("Datum", film.Datum);
			editor.putString("Kamera", film.Kamera);

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

							DB.getDB().deleteFilms(mContext,
									new String[] { ids.getText().toString() });
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

	private class FilmsArrayAdapter extends ArrayAdapter<Films> {

		private LayoutInflater inflater;

		public FilmsArrayAdapter(Context context, ArrayList<Films> planetList,
				int number) {
			super(context, R.layout.sqltablecell, R.id.filmtitle, planetList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Films planet = (Films) this.getItem(position);
			TextView textView;
			TextView textViewDate;
			TextView textViewCam;
			TextView textViewPics;
			ImageView imageViewBild;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.sqltablecell, null);
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
			textViewDate.setText(planet.getDate());
			textView.setText(planet.getName());
			textViewCam.setText(planet.getCam());
			textViewPics.setText(planet.getPics() + " "
					+ getString(R.string.pictures));
			imageViewBild.setImageBitmap(planet.getBild());
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
			// 4 child 2 kind
			LinearLayout lin = (LinearLayout) arg1;
			LinearLayout lins = (LinearLayout) lin.getChildAt(1);
			final TextView ids = (TextView) ((LinearLayout) lins.getChildAt(2))
					.getChildAt(0);

			Display display = ((WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
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
			deleteButton.setText(getString(R.string.delete_film));
			editButton.setText(getString(R.string.continue_film));

			exportButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					exportFilm(ids.getText().toString());
					pw.dismiss();
				}
			});
			deleteButton.setOnClickListener(new DeleteFilmDialogAction(ids));
			editButton.setOnClickListener(new EditFilmDialogAction(ids));
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pw.dismiss();
				}
			});
			int width = display.getWidth();
			int height = display.getHeight();
			pw = new PopupWindow(layoutOwn, (int) (width / 1.6),
					(int) (height / 2), true);
			pw.setAnimationStyle(7);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
			return true;
		}
	};

	public OnItemClickListener notlongClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			LinearLayout lin = (LinearLayout) arg1;
			LinearLayout lins = (LinearLayout) lin.getChildAt(1);
			TextView ids = (TextView) ((LinearLayout) lins.getChildAt(2))
					.getChildAt(0);
			Intent myIntent = new Intent(getApplicationContext(),
					FilmSelectActivity.class);
			myIntent.putExtra("ID", ids.getText().toString());
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

	public void exportFilm(String FilmID) {
		new FilmExportTask(FilmID).execute();

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
			MY_DB_NAME = DB.MY_DB_SET;
		}

		protected Boolean doInBackground(final String... args) {
			try {
				DB.getDB().createOrRebuildSettingsTable(mContext);
				DB.getDB().createOrRebuildNummernTable(mContext);
				DB.getDB().createOrRebuildFilmTable(mContext);

			} catch (Exception e) {
				Log.v("DEBUG", "Fehler bei Set-Erstellung : " + e);
			}
			return null;
		}
	}

	public class FilmExportTask extends AsyncTask<String, Void, Boolean> {

		String FilmID;
		String fileName;
		private ProgressDialog dialog;

		public FilmExportTask(String _FilmID) {
			dialog = new ProgressDialog(mContext);
			FilmID = _FilmID;
		}

		protected void onPreExecute() {
			this.dialog.setMessage(getString(R.string.export));
			this.dialog.show();
			Log.v("Check", "Pre");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			File file = new File(getFilesDir() + "/" + fileName);

			Uri u1 = null;
			u1 = Uri.fromFile(file);
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Film Export");
			sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
			sendIntent.setType("text/html");
			startActivity(sendIntent);
		}

		protected Boolean doInBackground(final String... args) {

			Film film = DB.getDB().getFilm(mContext, FilmID);

			fileName = FilmID + ".xml";

			XStream xs = new XStream();
			xs.alias("Bild", BildObjekt.class);
			xs.alias("Film", Film.class);

			try {
				FileOutputStream fos = openFileOutput(fileName,
						Context.MODE_WORLD_READABLE);
				xs.toXML(film, fos);
				fos.close();
				Log.v("Check", "XML Export: " + fileName + " was written.");
			} catch (IOException e) {
				e.printStackTrace();
				Log.v("Check", "Failes to write XML Export: " + fileName);
			}

			return null;
		}
	}

}