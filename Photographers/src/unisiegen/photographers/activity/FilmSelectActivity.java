package unisiegen.photographers.activity;

/**
 * In dieser Activity sieht man den ausgew�hlten Film mit allen Infos und kann sich die zugeh�rigen Bilder betrachten und ausw�hlen.
 */

import java.util.ArrayList;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.export.BildObjekt;
import unisiegen.photographers.export.Film;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
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
import com.viewpagerindicator.TitleProvider;

public class FilmSelectActivity extends PhotographersNotebookActivity {
	
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
		Button goon = (Button) findViewById(R.id.button_goon);
		goon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = settings.edit();

				film = DB.getDB().getFilm(mContext,
						getIntent().getExtras().getString("ID"));
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
		ImageView vorschauImage = (ImageView) findViewById(R.id.vorschau);

		byte[] data = Base64.decode(film.iconData, Base64.DEFAULT);
		vorschauImage.setImageBitmap(BitmapFactory.decodeByteArray(data, 0,
				data.length));

		TextView filmnotiz = (TextView) findViewById(R.id.filmnotiz);
		filmnotiz.setText(film.Filmnotiz);

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

		ArrayList<Pictures> listItems = new ArrayList<Pictures>();

		for (BildObjekt b : film.Bilder) {
			listItems.add(new Pictures(b));
		}

		Log.v("Check", "LISTITEMS : " + listItems.size());

		PicturesArrayAdapter adapter = new PicturesArrayAdapter(mContext, listItems, 1);
		ListView myList = (ListView) findViewById(android.R.id.list);
		myList.setOnItemClickListener(myClickListener);
		myList.setOnItemLongClickListener(myLongClickListener);
		myList.setAdapter(adapter);
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

			Display display = ((WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
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
							SlideNewPic.class);
					myIntent.putExtra("picToEdit", selektiertesBild);
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

											for (BildObjekt bild : film.Bilder) {
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

			int width = display.getWidth();
			int height = display.getHeight();
			pw = new PopupWindow(layoutOwn, (int) (width / 1.6),
					(int) (height / 2.5), true);
			pw.setAnimationStyle(7);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);

			return true;
		}
	};

	public OnItemClickListener myClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			final Context mContext2 = mContext;
			Display display = ((WindowManager) mContext2
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();
			LayoutInflater inflaterOwn = (LayoutInflater) mContext2
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layoutOwn = inflaterOwn.inflate(R.layout.slidi,
					(ViewGroup) findViewById(R.id.testen), false);
			ViewPager viewPager = (ViewPager) layoutOwn
					.findViewById(R.id.viewPager);
			MyPagerAdapter adapter = new MyPagerAdapter(mContext2);
			viewPager.setAdapter(adapter);
			viewPager.getAdapter().setPrimaryItem(viewPager, 2, null);

			mIndicator = (TitlePageIndicator) layoutOwn
					.findViewById(R.id.indicator);
			mIndicator.setViewPager(viewPager);
			mIndicator.setFooterColor(0xFF000000);

			pw = new PopupWindow(layoutOwn, (int) (width), (int) (height), true);
			pw.setAnimationStyle(7);
			// pw.setBackgroundDrawable(new BitmapDrawable());
			pw.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.infobg));
			pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
			viewPager.setCurrentItem(arg2);

		}
	};


	/*
	 * Pageadapter f�r das hin- und herwischen zwischen den Bildenr. W�hlt man
	 * ein Bild aus, wird ein "Popup" ge�ffnet in der alle Informationen zu dem
	 * Bild vorhanden sind in dieser Ansicht l�sst sich dann auch zwischen den
	 * Bildern hin- und herwechseln. Es wird einfach eine ArrayList<Views>
	 * gef�llt. Quasi fertige Views in eine Liste, die beim Wischen
	 * durchgegangen wird.
	 */
	private class MyPagerAdapter extends PagerAdapter implements TitleProvider {

		private ArrayList<View> views;

		public MyPagerAdapter(Context context) {
			views = new ArrayList<View>();
			LayoutInflater inflater = getLayoutInflater();

			for (BildObjekt bild : film.Bilder) {
				View v = inflater.inflate(R.layout.filminfobox, null, false);
				final TextView zeitStempel = (TextView) v
						.findViewById(R.id.zeitStempel);
				zeitStempel.setText(bild.Zeitstempel);
				final TextView zeitGeo = (TextView) v.findViewById(R.id.geoTag);
				zeitGeo.setText(bild.GeoTag);
				final TextView objektiv = (TextView) v
						.findViewById(R.id.showobjektiv);
				objektiv.setText(bild.Objektiv);
				final TextView filtervf = (TextView) v
						.findViewById(R.id.showfiltervf);
				filtervf.setText(bild.FilterVF);
				final TextView picfocus = (TextView) v
						.findViewById(R.id.showfokus);
				picfocus.setText(bild.Fokus);
				final TextView picblende = (TextView) v
						.findViewById(R.id.showblende);
				picblende.setText(bild.Blende);
				final TextView piczeit = (TextView) v
						.findViewById(R.id.showzeit);
				piczeit.setText(bild.Zeit);
				final TextView picmessung = (TextView) v
						.findViewById(R.id.showmessung);
				picmessung.setText(bild.Messmethode);
				final TextView picplus = (TextView) v
						.findViewById(R.id.showbelichtung);
				picplus.setText(bild.Belichtungskorrektur);
				final TextView picmakro = (TextView) v
						.findViewById(R.id.showmakro);
				picmakro.setText(bild.Makro);
				final TextView picmakrovf = (TextView) v
						.findViewById(R.id.showmakrovf);
				picmakrovf.setText(bild.MakroVF);
				final TextView picfilter = (TextView) v
						.findViewById(R.id.showfilter);
				picfilter.setText(bild.Filter);
				final TextView picblitz = (TextView) v
						.findViewById(R.id.showblitz);
				picblitz.setText(bild.Blitz);
				final TextView picblitzkorr = (TextView) v
						.findViewById(R.id.showblitzkorr);
				picblitzkorr.setText(bild.Blitzkorrektur);
				final TextView picnotiz = (TextView) v
						.findViewById(R.id.shownotiz);
				picnotiz.setText(bild.Notiz);
				final TextView picnotizcam = (TextView) v
						.findViewById(R.id.shownotizkam);
				picnotizcam.setText(bild.KameraNotiz);
				final TextView picTitle = (TextView) v
						.findViewById(R.id.pictitle);
				picTitle.setText(bild.Bildnummer);

				views.add(v);
			}
		}

		@Override
		public void destroyItem(View view, int arg1, Object object) {
			// Es werden immer nur die 2 nächsten und die 2 letzten Views
			// "gespeichert" bzw. berechnet, der Rest wird erstmal gelöscht.

			((ViewPager) view).removeView((LinearLayout) object);
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size(); // Wieviele Views zum Wischen
		}

		@Override
		public Object instantiateItem(View view, int position) {
			// Das vorpuffern, wenn die View bald drankommt... s.o.
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
		public String getTitle(int position) { // Kommt vom TitleProvider um den
												// Titel einer View festzulegen
			if (position == 0) {
				return " >";
			} else if (position == (views.size() - 1)) {
				return "< ";
			}
			return "<  >";
		}

	}

}