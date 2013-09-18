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
 * Hierbei handelt es sich um alle Einstellungen ! Die anderen Einstellungs-Activitys sind nur Teilmengen von dieser f�r eine bessere Aufteilung wenn gew�nscht!
 */

import java.util.ArrayList;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Setting;
import unisiegen.photographers.settings.SettingsViewHolder;
import unisiegen.photographers.settings.SettingsViewPart;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class EditSettingsActivity extends Activity {

	public static final String GEO_TAG = "geoTag";
	private static final String SETTINGS_TABLE = "SettingsTable";
	private static final String LOAD_SET = "LoadSet";
	private static final String FIRSTSTART = "FIRSTSTART";

	public static final int ALLGEMEIN_POSITION = 0;
	public static final int KAMERA_POSITION = 1;
	public static final int FOKUS_POSITION = 2;
	public static final int BLENDE_POSITION = 3;
	public static final int ZEITE_POSITION = 4;
	public static final int MESS_POSITION = 5;
	public static final int KORREKTUR_POSITION = 6;
	public static final int MAKRO_POSITION = 7;
	public static final int MAKROVF_POSITION = 8;
	public static final int FILTER_POSITION = 9;
	public static final int FILTERVF_POSITION = 10;
	public static final int FILMFORMAT_POSITION = 11;
	public static final int ASA_POSITION = 12;
	public static final int BLITZ_POSITION = 13;
	public static final int BLITZKORR_POSITION = 14;
	public static final int SOND_POSITION = 15;

	public static final int BRENNWEITE_POSITION = 1337; // is not shown as view,
														// but as a subview of
														// camera

	/*
	 * Sonstige Variablen
	 */
	private Integer contentIndex = 0;
	private TextView tv1;
	private Button weiter, close;
	SharedPreferences settings;
	private PopupWindow pw;
	Context mContext;
	private ViewPager viewPager;
	private TitlePageIndicator settingsPageIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.slidenewsettings);
		mContext = this;
		settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		SettingsPager settingsAdapter = new SettingsPager(this);
		viewPager.setAdapter(settingsAdapter);
		settingsPageIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		settingsPageIndicator.setViewPager(viewPager);

		if (settings.getInt(FIRSTSTART, 0) == 1) {
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.post(new Runnable() {
				public void run() {
					openTutorial();
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt(FIRSTSTART, 2);
					editor.commit();
				}
			});

		} else if (settings.getInt(FIRSTSTART, 0) == 3) {
			ViewGroup view1 = (ViewGroup) getWindow().getDecorView();
			view1.post(new Runnable() {
				public void run() {
					openTutorial();
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt(FIRSTSTART, 5);
					editor.commit();
				}
			});
		}
	}

	public void onResume() {
		super.onResume();
		contentIndex = 0;
	}

	/*
	 * Tutorial
	 */
	public void openTutorial() {

		final String[] tutorialContent = getResources().getStringArray(
				R.array.strings_tutorial_3);

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layoutOwn1 = inflater.inflate(R.layout.popup,
				(ViewGroup) findViewById(R.id.widget), false);

		pw = new PopupWindow(layoutOwn1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pw.setAnimationStyle(7);
		pw.setBackgroundDrawable(new BitmapDrawable());
		tv1 = (TextView) layoutOwn1.findViewById(R.id.textview_pop);
		tv1.setText(tutorialContent[contentIndex]);
		contentIndex++;

		weiter = (Button) layoutOwn1.findViewById(R.id.button_popup);
		weiter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (contentIndex == tutorialContent.length) {
					pw.dismiss();
				} else {
					tv1.setText(tutorialContent[contentIndex]);
					contentIndex++;
					if (contentIndex == 2) {
						pw.dismiss();
						viewPager.setCurrentItem(4, true);
						pw.showAtLocation(layoutOwn1, Gravity.CENTER, 0, 0);
					}
					if (contentIndex == 4) {
						openOptionsMenu();
					}
					if (contentIndex == 5) {
						pw.dismiss();
						viewPager.setCurrentItem(1, true);
						pw.showAtLocation(layoutOwn1, Gravity.CENTER, 0, 0);
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

	/*
	 * Hilfsklassen f�r Custom-ListViews und SlideViews
	 */

	public void setFooterColor(int footerColor) {
		settingsPageIndicator.setFooterColor(footerColor);
	}

	private class SettingsPager extends PagerAdapter implements TitleProvider {

		private String[] pageTitles = null;
		private LayoutInflater inflater = getLayoutInflater();

		@Override
		public int getItemPosition(Object object) {
			// TODO: Do something useful
			return POSITION_NONE;
		}

		public SettingsPager(Context context) {
			super();
			pageTitles = getResources().getStringArray(
					R.array.settings_slide_contents);
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
			return pageTitles.length;
		}

		@Override
		public Object instantiateItem(View view, int position) {

			View myView = null;

			switch (position) {
			case ALLGEMEIN_POSITION:
				myView = createAllgemeinView();
				break;
			case KAMERA_POSITION:
				myView = createKameraView();
				break;
			case FOKUS_POSITION:
				myView = new SettingsViewPart(mContext, R.string.focus,
						DB.MY_DB_TABLE_SETFOK).getView();
				break;
			case BLENDE_POSITION:
				myView = new SettingsViewPart(mContext, R.string.aperture,
						DB.MY_DB_TABLE_SETBLE).getView();
				break;
			case ZEITE_POSITION:
				myView = new SettingsViewPart(mContext, R.string.exposure,
						DB.MY_DB_TABLE_SETZEI).getView();
				break;
			case MESS_POSITION:
				myView = new SettingsViewPart(mContext, R.string.measurement,
						DB.MY_DB_TABLE_SETMES).getView();
				break;
			case KORREKTUR_POSITION:
				myView = new SettingsViewPart(mContext,
						R.string.exposure_correction, DB.MY_DB_TABLE_SETKOR)
						.getView();
				break;
			case MAKRO_POSITION:
				myView = new SettingsViewPart(mContext, R.string.focus,
						DB.MY_DB_TABLE_SETNM).getView();
				break;
			case MAKROVF_POSITION:
				myView = new SettingsViewPart(mContext, R.string.macro_vf,
						DB.MY_DB_TABLE_SETMVF).getView();
				break;
			case FILTER_POSITION:
				myView = new SettingsViewPart(mContext, R.string.filter,
						DB.MY_DB_TABLE_SETFIL).getView();
				break;
			case FILTERVF_POSITION:
				myView = new SettingsViewPart(mContext, R.string.filter_vf,
						DB.MY_DB_TABLE_SETFVF).getView();
				break;
			case FILMFORMAT_POSITION:
				myView = new SettingsViewPart(mContext, R.string.film_formats,
						DB.MY_DB_TABLE_SETFF).getView();
				break;
			case ASA_POSITION:
				myView = new SettingsViewPart(mContext, R.string.film_speed,
						DB.MY_DB_TABLE_SETEMP).getView();
				break;
			case BLITZ_POSITION:
				myView = new SettingsViewPart(mContext, R.string.flash,
						DB.MY_DB_TABLE_SETBLI).getView();
				break;
			case BLITZKORR_POSITION:
				myView = new SettingsViewPart(mContext,
						R.string.flash_correction, DB.MY_DB_TABLE_SETKOR)
						.getView();
				break;
			case SOND_POSITION:
				myView = new SettingsViewPart(mContext, R.string.processing,
						DB.MY_DB_TABLE_SETSON).getView();
				break;
			}

			((ViewPager) view).addView(myView);
			return myView;

		}

		private View createKameraView() {
			View view = inflater.inflate(R.layout.settingsauswahl, null, false);
			TextView freecell = (TextView) view.findViewById(R.id.freecell);
			TableLayout tablor = (TableLayout) view.findViewById(R.id.tablor);
			final ListView myList = (ListView) view
					.findViewById(android.R.id.list);
			final Button addKate = (Button) view.findViewById(R.id.addkamera);
			final EditText Kat = (EditText) view
					.findViewById(R.id.kameramodell);
			freecell.setText(getString(R.string.camera_models));
			tablor.setBackgroundResource(R.drawable.shaperedtable);
			tablor.setPadding(4, 0, -2, 0);
			final ArrayList<Setting> valuesCam = DB.getDB().getAllSettings(
					mContext, DB.MY_DB_TABLE_SETCAM);
			final ArrayAdapter<Setting> listAdapter = new CamArrayAdapter(
					mContext, valuesCam);
			myList.setAdapter(listAdapter);

			myList.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0,
						final View arg1, final int arg2, long arg3) {
					Display display = ((WindowManager) mContext
							.getSystemService(Context.WINDOW_SERVICE))
							.getDefaultDisplay();
					LayoutInflater inflaterOwn = (LayoutInflater) mContext
							.getSystemService(LAYOUT_INFLATER_SERVICE);
					View layoutOwn = inflaterOwn.inflate(
							R.layout.longclickspec,
							(ViewGroup) findViewById(R.id.testen), false);
					Button deleteButton = (Button) layoutOwn
							.findViewById(R.id.deletebutton);
					Button cancelButton = (Button) layoutOwn
							.findViewById(R.id.cancelbutton);
					Button editButton = (Button) layoutOwn
							.findViewById(R.id.editbutton);
					Button specButton = (Button) layoutOwn
							.findViewById(R.id.specbutton);
					deleteButton.setText(getString(R.string.delete_entry));
					editButton.setText(getString(R.string.make_default));
					editButton.setVisibility(Button.GONE);
					specButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							// BW F�NGT AN

							LinearLayout lins = (LinearLayout) arg1;
							final TextView textis = (TextView) ((LinearLayout) lins
									.getChildAt(0)).getChildAt(0);
							Display display2 = ((WindowManager) mContext
									.getSystemService(Context.WINDOW_SERVICE))
									.getDefaultDisplay();
							LayoutInflater inflaterOwn2 = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layoutOwn2 = inflaterOwn2.inflate(
									R.layout.settingsauswahlspec, null, false);
							TextView freecellspec = (TextView) layoutOwn2
									.findViewById(R.id.freecell);
							TableLayout tablorspec = (TableLayout) layoutOwn2
									.findViewById(R.id.tablor);
							final ListView myListspec = (ListView) layoutOwn2
									.findViewById(android.R.id.list);
							final Button addKatespec = (Button) layoutOwn2
									.findViewById(R.id.addkamera);
							final EditText Katspec = (EditText) layoutOwn2
									.findViewById(R.id.kameramodell);
							freecellspec
									.setText(getString(R.string.choose_lens));
							tablorspec
									.setBackgroundResource(R.drawable.shaperedtable);
							tablorspec.setPadding(2, 2, 2, 2);
							final ArrayList<Setting> aplanetsspec = DB.getDB()
									.getAllSettings(mContext,
											DB.MY_DB_TABLE_SETBW);
							final ArrayAdapter<Setting> listAdapterspec = new SettingsArrayAdapterSpec(
									mContext, aplanetsspec,
									BRENNWEITE_POSITION, textis.getText()
											.toString());
							myListspec.setAdapter(listAdapterspec);
							myListspec
									.setOnItemLongClickListener(new OnItemLongClickListener() {
										@Override
										public boolean onItemLongClick(
												AdapterView<?> arg0,
												final View arg1,
												final int arg2, long arg3) {
											AlertDialog.Builder builder = new AlertDialog.Builder(
													mContext);
											builder.setMessage(
													getString(R.string.delete_entry))
													.setCancelable(false)
													.setPositiveButton(
															getString(R.string.yes),
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	LinearLayout test = (LinearLayout) arg1;
																	TextView tec = (TextView) test
																			.getChildAt(0);

																	DB.getDB()
																			.deleteLens(
																					mContext,
																					tec.getText()
																							.toString());
																	DB.getDB()
																			.deleteSetting(
																					mContext,
																					DB.MY_DB_TABLE_SETBW,
																					tec.getText()
																							.toString());
																	aplanetsspec
																			.remove(arg2);

																	listAdapterspec
																			.clear();
																	ArrayList<Setting> values = DB
																			.getDB()
																			.getAllSettings(
																					mContext,
																					DB.MY_DB_TABLE_SETBW);
																	for (Setting s : values) {
																		listAdapterspec
																				.add(s);
																	}
																	viewPager = (ViewPager) findViewById(R.id.viewPager);
																	SettingsPager adapter = new SettingsPager(
																			mContext);
																	viewPager
																			.setAdapter(adapter);
																	settingsPageIndicator = (TitlePageIndicator) findViewById(R.id.titles);
																	settingsPageIndicator
																			.setViewPager(viewPager);
																	viewPager
																			.setCurrentItem(
																					1,
																					false);
																}
															})
													.setNegativeButton(
															getString(R.string.no),
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																}
															});
											AlertDialog alert = builder
													.create();
											alert.show();
											return true;
										}
									});
							addKatespec
									.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {
											InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													Katspec.getApplicationWindowToken(),
													0);
											boolean vorhanden = false;
											for (int i = 0; i < aplanetsspec
													.size(); i++) {
												vorhanden = aplanetsspec
														.get(i)
														.toString()
														.equals(Katspec
																.getText()
																.toString());
												if (vorhanden) {
													i = (aplanetsspec.size() - 1);
												}
											}
											if (vorhanden
													|| Katspec.getText()
															.toString()
															.length() == 0
													|| Katspec.getText()
															.toString().trim()
															.length() == 0) {
												Toast.makeText(
														getApplicationContext(),
														getString(R.string.empty_or_existing_entry),
														Toast.LENGTH_SHORT)
														.show();
											} else {
												DB.getDB().addSetting(
														mContext,
														DB.MY_DB_TABLE_SETBW,
														Katspec.getText()
																.toString(), 1);
												String camera = listAdapter
														.getItem(arg2)
														.getValue().toString();
												String lens = Katspec.getText()
														.toString();
												DB.getDB().addLensToCamera(
														mContext, camera, lens);

												aplanetsspec.add(new Setting(
														DB.MY_DB_TABLE_SETBW,
														Katspec.getText()
																.toString(), 1,
														0));
												Katspec.setText("");
												listAdapterspec
														.notifyDataSetChanged();
												listAdapter
														.notifyDataSetChanged();
											}
										}
									});
							int width2 = display2.getWidth();
							int height2 = display2.getHeight();
							PopupWindow popUp = new PopupWindow(layoutOwn2,
									(int) (width2 / 1.05),
									(int) (height2 / 1.05), true);
							popUp.setAnimationStyle(7);
							popUp.setBackgroundDrawable(new BitmapDrawable());
							pw.dismiss();
							popUp.showAtLocation(layoutOwn2, Gravity.CENTER, 0,
									0);
						}
					});
					editButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							LinearLayout lins = (LinearLayout) arg1;
							TextView texti = (TextView) ((LinearLayout) lins
									.getChildAt(0)).getChildAt(0);

							DB.getDB().setDefaultVal(mContext,
									DB.MY_DB_TABLE_SETCAM,
									texti.getText().toString());

							myList.setAdapter(listAdapter);
							Toast.makeText(mContext,
									getString(R.string.default_saved),
									Toast.LENGTH_SHORT).show();
							pw.dismiss();
						}
					});
					deleteButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							LinearLayout lins = (LinearLayout) arg1;
							TextView texti = (TextView) ((LinearLayout) lins
									.getChildAt(0)).getChildAt(0);
							DB.getDB().deleteSetting(mContext,
									DB.MY_DB_TABLE_SETCAM,
									texti.getText().toString());

							valuesCam.remove(arg2);
							listAdapter.notifyDataSetChanged();
							pw.dismiss();
						}
					});
					cancelButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							pw.dismiss();
						}
					});

					pw = new PopupWindow(layoutOwn, ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT, true);
					pw.setAnimationStyle(7);
					pw.setBackgroundDrawable(new BitmapDrawable());
					pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
					return true;
				}
			});
			// BW H�RT AUF

			addKate.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							addKate.getApplicationWindowToken(), 0);
					boolean vorhanden = false;
					for (int i = 0; i < valuesCam.size(); i++) {
						vorhanden = valuesCam.get(i).getValue().toString()
								.equals(Kat.getText().toString());
						if (vorhanden) {
							i = (valuesCam.size() - 1);
						}
					}
					if (vorhanden || Kat.getText().toString().length() == 0
							|| Kat.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.empty_or_existing_entry),
								Toast.LENGTH_SHORT).show();
					} else {
						DB.getDB().addSetting(mContext, DB.MY_DB_TABLE_SETCAM,
								Kat.getText().toString(), 1);
						valuesCam.add(new Setting(DB.MY_DB_TABLE_SETCAM, Kat
								.getText().toString(), 1, 0));
						listAdapter.notifyDataSetChanged();

						// BW F�NGT AN
						Display display2 = ((WindowManager) mContext
								.getSystemService(Context.WINDOW_SERVICE))
								.getDefaultDisplay();
						LayoutInflater inflaterOwn2 = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layoutOwn2 = inflaterOwn2.inflate(
								R.layout.settingsauswahlspec, null, false);
						TextView freecellspec = (TextView) layoutOwn2
								.findViewById(R.id.freecell);
						TableLayout tablorspec = (TableLayout) layoutOwn2
								.findViewById(R.id.tablor);
						final ListView myListspec = (ListView) layoutOwn2
								.findViewById(android.R.id.list);
						final Button addKatespec = (Button) layoutOwn2
								.findViewById(R.id.addkamera);
						final EditText Katspec = (EditText) layoutOwn2
								.findViewById(R.id.kameramodell);
						freecellspec.setText(getString(R.string.choose_lens));
						tablorspec
								.setBackgroundResource(R.drawable.shaperedtable);
						tablorspec.setPadding(2, 2, 2, 2);
						final ArrayList<Setting> aplanetsspec = DB.getDB()
								.getAllSettings(mContext, DB.MY_DB_TABLE_SETBW);
						final ArrayAdapter<Setting> listAdapterspec = new SettingsArrayAdapterSpec(
								mContext, aplanetsspec, BRENNWEITE_POSITION,
								Kat.getText().toString());
						myListspec.setAdapter(listAdapterspec);
						myListspec
								.setOnItemLongClickListener(new OnItemLongClickListener() {
									@Override
									public boolean onItemLongClick(
											AdapterView<?> arg0,
											final View arg1, final int arg2,
											long arg3) {
										AlertDialog.Builder builder = new AlertDialog.Builder(
												mContext);
										builder.setMessage(
												getString(R.string.question_delete))
												.setCancelable(false)
												.setPositiveButton(
														getString(R.string.yes),
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																LinearLayout test = (LinearLayout) arg1;
																TextView tec = (TextView) test
																		.getChildAt(0);
																DB.getDB()
																		.deleteLens(
																				mContext,
																				tec.getText()
																						.toString());
																DB.getDB()
																		.deleteSetting(
																				mContext,
																				DB.MY_DB_TABLE_SETBW,
																				tec.getText()
																						.toString());

																aplanetsspec
																		.clear();
																ArrayList<Setting> values = DB
																		.getDB()
																		.getAllSettings(
																				mContext,
																				DB.MY_DB_TABLE_SETBW);
																for (Setting s : values) {
																	aplanetsspec
																			.add(s);
																}

																viewPager = (ViewPager) findViewById(R.id.viewPager);
																SettingsPager adapter = new SettingsPager(
																		mContext);
																viewPager
																		.setAdapter(adapter);
																settingsPageIndicator = (TitlePageIndicator) findViewById(R.id.titles);
																settingsPageIndicator
																		.setViewPager(viewPager);
																viewPager
																		.setCurrentItem(
																				1,
																				false);
															}
														})
												.setNegativeButton(
														getString(R.string.no),
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int which) {
															}
														});
										AlertDialog alert = builder.create();
										alert.show();
										return true;
									}
								});
						addKatespec
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(Katspec
												.getApplicationWindowToken(), 0);
										boolean vorhanden = false;
										for (int i = 0; i < aplanetsspec.size(); i++) {
											vorhanden = aplanetsspec
													.get(i)
													.toString()
													.equals(Katspec.getText()
															.toString());
											if (vorhanden) {
												i = (aplanetsspec.size() - 1);
											}
										}
										if (vorhanden
												|| Katspec.getText().toString()
														.length() == 0
												|| Katspec.getText().toString()
														.trim().length() == 0) {
											Toast.makeText(
													getApplicationContext(),
													getString(R.string.empty_or_existing_entry),
													Toast.LENGTH_SHORT).show();
										} else {
											DB.getDB().addSetting(
													mContext,
													DB.MY_DB_TABLE_SETBW,
													Katspec.getText()
															.toString(), 1);
											String camera = valuesCam
													.get(valuesCam.size() - 1)
													.getValue().toString();
											String lens = Katspec.getText()
													.toString();
											DB.getDB().addLensToCamera(
													mContext, camera, lens);

											aplanetsspec.add(new Setting(
													DB.MY_DB_TABLE_SETBW,
													Katspec.getText()
															.toString(), 1, 0));
											Katspec.setText("");
											listAdapterspec
													.notifyDataSetChanged();
											listAdapter.notifyDataSetChanged();
										}
									}
								});
						Katspec.setOnKeyListener(new OnKeyListener() {
							@Override
							public boolean onKey(View v, int keyCode,
									KeyEvent event) {
								if ((event.getAction() == KeyEvent.ACTION_DOWN)
										&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
									InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(
											Katspec.getApplicationWindowToken(),
											0);
									return true;
								}
								return false;
							}
						});
						int width2 = display2.getWidth();
						int height2 = display2.getHeight();
						PopupWindow popUp = new PopupWindow(layoutOwn2,
								(int) (width2 / 1.05), (int) (height2 / 1.05),
								true);
						popUp.setAnimationStyle(7);
						popUp.setBackgroundDrawable(new BitmapDrawable());
						popUp.showAtLocation(layoutOwn2, Gravity.CENTER, 0, 0);
						// TESTEN ENDE
						Kat.setText("");
					}
				}
			});
			// BW H�RT AUF

			Kat.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN)
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								Kat.getApplicationWindowToken(), 0);
						return true;
					}
					return false;
				}
			});
			return view;
		}

		private View createAllgemeinView() {

			View view = inflater.inflate(R.layout.mainsettings, null, false);
			final SharedPreferences.Editor editors = settings.edit();

			final Button tutorialBack = (Button) view
					.findViewById(R.id.ButtonTutorialBack);
			tutorialBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt(FIRSTSTART, 0);
					editor.commit();
				}
			});

			final ToggleButton geotag = (ToggleButton) view
					.findViewById(R.id.geotag);
			geotag.setTextOff(getString(R.string.off));
			geotag.setTextOn(getString(R.string.on));
			geotag.setGravity(Gravity.LEFT);
			
			geotag.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if (geotag.isChecked()) {
						editors.putString(GEO_TAG, "ja");
						editors.commit();
					} else {
						editors.putString(GEO_TAG, "nein");
						editors.commit();
					}					
				}
			});

			if (settings.getString(GEO_TAG, "nein") == "nein") {
				geotag.setChecked(false);
				Log.v("Check", "geoTag auf false");
			} else {
				geotag.setChecked(true);
				Log.v("Check", "geoTag auf true");
			}

			final Spinner blendenstufen = (Spinner) view
					.findViewById(R.id.blenden);
			ArrayList<String> blendenarray = new ArrayList<String>();
			blendenarray.add(getString(R.string.one_one));
			blendenarray.add(getString(R.string.one_two));
			blendenarray.add(getString(R.string.one_three));
			ArrayAdapter<String> blendenadapter = new ArrayAdapter<String>(
					mContext, android.R.layout.simple_spinner_item,
					blendenarray);
			blendenstufen.setAdapter(blendenadapter);
			blendenstufen
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							editors.putString("blendenstufe", blendenstufen
									.getSelectedItem().toString());
							editors.commit();

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}

					});
			if (settings.getString("blendenstufe", getString(R.string.one_one))
					.equals(getString(R.string.one_one))) {
				blendenstufen.setSelection(0);
			} else if (settings.getString("blendenstufe",
					getString(R.string.one_one)).equals(
					getString(R.string.one_two))) {
				blendenstufen.setSelection(1);
			} else {
				blendenstufen.setSelection(2);
			}

			final Spinner zeitstempel = (Spinner) view
					.findViewById(R.id.zeitstempel);
			ArrayList<String> zeitstempelarray = new ArrayList<String>();
			zeitstempelarray.add(getString(R.string.on));
			zeitstempelarray.add(getString(R.string.off));
			zeitstempelarray.add(getString(R.string.minus_one_minute));
			ArrayAdapter<String> zeitstempeladapter = new ArrayAdapter<String>(
					mContext, android.R.layout.simple_spinner_item,
					zeitstempelarray);
			zeitstempel.setAdapter(zeitstempeladapter);
			zeitstempel.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					editors.putString("zeitStempel", zeitstempel
							.getSelectedItem().toString());
					editors.commit();

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}

			});
			if (settings.getString("zeitStempel", getString(R.string.on))
					.equals(getString(R.string.on))) {
				zeitstempel.setSelection(0);
			} else if (settings
					.getString("zeitStempel", getString(R.string.on)).equals(
							getString(R.string.off))) {
				zeitstempel.setSelection(1);
			} else {
				zeitstempel.setSelection(2);
			}

			final Spinner verlang = (Spinner) view.findViewById(R.id.verlang);
			ArrayList<String> verlangarray = new ArrayList<String>();
			verlangarray.add(getString(R.string.factor));
			verlangarray.add(getString(R.string.aperture_adjusting));
			ArrayAdapter<String> verlangadapter = new ArrayAdapter<String>(
					mContext, android.R.layout.simple_spinner_item,
					verlangarray);
			verlang.setAdapter(verlangadapter);
			verlang.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					editors.putString("Verlaengerung", verlang
							.getSelectedItem().toString());
					editors.commit();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}

			});
			if (settings.getString("Verlaengerung", getString(R.string.factor))
					.equals(getString(R.string.factor))) {
				verlang.setSelection(0);
			} else {
				verlang.setSelection(1);
			}

			return view;
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
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 2)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 3)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 4)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 5)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 6)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 7)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 8)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 9)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 10)
				setFooterColor(0xFFBB0000);
			if (viewPager.getCurrentItem() == 11)
				setFooterColor(0xFF009900);
			if (viewPager.getCurrentItem() == 12)
				setFooterColor(0xFF009900);
			if (viewPager.getCurrentItem() == 13)
				setFooterColor(0xFF0000BB);
			if (viewPager.getCurrentItem() == 14)
				setFooterColor(0xFF0000BB);
			if (viewPager.getCurrentItem() == 15)
				setFooterColor(0xFF0000BB);

			return pageTitles[position % pageTitles.length];
		}

	}

	// Extra ArrayAdapter f�r die Camera Zellen (Diese beinhalten noch die
	// Objektive)
	private class CamArrayAdapter extends ArrayAdapter<Setting> {

		private LayoutInflater inflater;

		public CamArrayAdapter(Context context, ArrayList<Setting> planetList) {
			super(context, R.layout.list_itemcam, R.id.listItemText, planetList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Setting planet = (Setting) this.getItem(position);
			CheckBox checkBox;
			TextView textView;
			LinearLayout grid;
			LinearLayout grid2 = null;
			TextView testtext;

			convertView = inflater.inflate(R.layout.list_itemcam, null);
			convertView.setClickable(false);
			convertView.setFocusable(false);
			// Find the child views.
			textView = (TextView) convertView.findViewById(R.id.listItemText);
			checkBox = (CheckBox) convertView.findViewById(R.id.check);
			grid = (LinearLayout) convertView.findViewById(R.id.gridview);
			testtext = (TextView) inflater.inflate(R.layout.objektive, grid,
					false);

			grid.setClickable(false);
			grid.setFocusable(false);
			grid.setEnabled(false);
			grid.clearFocus();

			int lineBreakControl = 0;

			ArrayList<String> values = DB.getDB().getLensesForCamera(mContext,
					planet.getValue().toString());
			if (values.size() == 0) {
				grid.setVisibility(LinearLayout.GONE);
			}
			for (String value : values) {

				if ((lineBreakControl % 3) == 0 || lineBreakControl == 0) {
					grid2 = null;
					grid2 = (LinearLayout) inflater.inflate(
							R.layout.objektivegrid, grid, false);
					grid.addView(grid2);
				}
				testtext = (TextView) inflater.inflate(R.layout.objektive,
						grid2, false);
				testtext.setText(value);
				grid2.addView(testtext);
				lineBreakControl++;

			}

			checkBox.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					Setting planet = (Setting) cb.getTag();
					planet.setDisplay(cb.isChecked());

					int value = 0;
					if (cb.isChecked() == true) {
						value = 1;
					}

					DB.getDB().updateSetting(mContext, DB.MY_DB_TABLE_SETCAM,
							planet.getValue(), value);
				}
			});

			// Tag the CheckBox with the Planet it is displaying, so that we can
			// access the planet in onClick() when the CheckBox is toggled.
			checkBox.setTag(planet);

			// Display planet data
			if (planet.shouldBeDisplayed() == 1) {
				checkBox.setChecked(true);
			} else if (planet.shouldBeDisplayed() == 0) {
				checkBox.setChecked(false);
			}
			textView.setText(planet.getValue());

			settings = PreferenceManager.getDefaultSharedPreferences(mContext);
			textView.setTextColor(0xFF000000);

			return convertView;
		}

	}

	private class SettingsArrayAdapterSpec extends ArrayAdapter<Setting> {

		private LayoutInflater inflater;
		String camera;

		public SettingsArrayAdapterSpec(Context context,
				ArrayList<Setting> planetList, int number, String cam) {
			super(context, R.layout.list_item, R.id.listItemText, planetList);
			camera = cam;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Setting planet = (Setting) this.getItem(position);
			CheckBox checkBox;
			final TextView textView;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);

				textView = (TextView) convertView
						.findViewById(R.id.listItemText);
				checkBox = (CheckBox) convertView.findViewById(R.id.check);
				convertView.setTag(new SettingsViewHolder(textView, checkBox));

				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Setting planet = (Setting) cb.getTag();
						planet.setDisplay(cb.isChecked());

						if (cb.isChecked() == true) {

							DB.getDB().addLensToCamera(mContext, camera,
									planet.getValue().toString());
							viewPager = (ViewPager) findViewById(R.id.viewPager);
							SettingsPager adapter = new SettingsPager(mContext);
							viewPager.setAdapter(adapter);
							settingsPageIndicator = (TitlePageIndicator) findViewById(R.id.titles);
							settingsPageIndicator.setViewPager(viewPager);
							viewPager.setCurrentItem(1, false);

						} else {
							DB.getDB().deleteLensFromCamera(mContext,
									planet.getValue().toString(), camera);
							viewPager = (ViewPager) findViewById(R.id.viewPager);

							SettingsPager adapter = new SettingsPager(mContext);
							viewPager.setAdapter(adapter);
							settingsPageIndicator = (TitlePageIndicator) findViewById(R.id.titles);
							settingsPageIndicator.setViewPager(viewPager);
							viewPager.setCurrentItem(1, false);
						}
					}
				});
			} else {
				SettingsViewHolder viewHolder = (SettingsViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}
			checkBox.setTag(planet);

			ArrayList<String> brennweiten = DB.getDB().getLensesForCamera(
					mContext, camera);
			if (brennweiten.contains(planet.getValue())) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}

			textView.setText(planet.getValue());
			settings = PreferenceManager.getDefaultSharedPreferences(mContext);
			if (planet.getValue() == settings.getString("KamDef", "")
					|| planet.getValue().equals(
							settings.getString("FormatDef", ""))
					|| planet.getValue().equals(
							settings.getString("EmpfDef", ""))
					|| planet.getValue().equals(
							settings.getString("MakroDef", ""))
					|| planet.getValue().equals(
							settings.getString("FilterDef", ""))
					|| planet.getValue().equals(
							settings.getString("BlitzDef", ""))
					|| planet.getValue().equals(
							settings.getString("SonderDef", ""))) {
				textView.setTextColor(0xFF0000AA);
			} else {
				textView.setTextColor(0xFF000000);
			}

			return convertView;
		}

	}

	public void setSetButtonColors(Button[] buttons) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		int selected = prefs.getInt(LOAD_SET, 1) - 1;

		for (int i = 0; i < buttons.length; i++) {
			if (i == selected) {
				buttons[i].setTextColor(0xff00cc00);
			} else {
				buttons[i].setTextColor(0xff000000);
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settingsmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.opt_settingsToFactoryDefaults) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(getString(R.string.reset_warning))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									new ResetSettings().execute();
								}
							})
					.setNegativeButton(getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		} else if (item.getItemId() == R.id.opt_loadSetOfSettings) {
			LayoutInflater inflaterOwn1 = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layoutOwn1 = inflaterOwn1.inflate(R.layout.popupmenu,
					(ViewGroup) findViewById(R.id.users), false);
			final SharedPreferences.Editor prefsEditor = settings.edit();
			final Button setone = (Button) layoutOwn1.findViewById(R.id.setone);
			final Button settwo = (Button) layoutOwn1.findViewById(R.id.settwo);
			final Button setthree = (Button) layoutOwn1
					.findViewById(R.id.setthree);
			final Button setfour = (Button) layoutOwn1
					.findViewById(R.id.setfour);
			setSetButtonColors(new Button[] { setone, settwo, setthree, setfour });
			setone.setText(settings.getString("SetButtonOne",
					getString(R.string.set_default)));
			settwo.setText(settings.getString("SetButtonTwo",
					getString(R.string.set_two)));
			setthree.setText(settings.getString("SetButtonThree",
					getString(R.string.set_three)));
			setfour.setText(settings.getString("SetButtonFour",
					getString(R.string.set_four)));

			OnClickListener loadSetListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (v == setfour) {
						prefsEditor.putInt(LOAD_SET, 4);
						prefsEditor.putString(SETTINGS_TABLE, DB.MY_DB_SET3);
					} else if (v == setthree) {
						prefsEditor.putInt(LOAD_SET, 3);
						prefsEditor.putString(SETTINGS_TABLE, DB.MY_DB_SET2);
					} else if (v == settwo) {
						prefsEditor.putInt(LOAD_SET, 2);
						prefsEditor.putString(SETTINGS_TABLE, DB.MY_DB_SET1);
					} else {
						prefsEditor.putInt(LOAD_SET, 1);
						prefsEditor.putString(SETTINGS_TABLE, DB.MY_DB_SET);
					}

					prefsEditor.commit();
					ViewPager viewPager111 = (ViewPager) findViewById(R.id.viewPager);
					SettingsPager adapter111 = new SettingsPager(mContext);
					viewPager111.setAdapter(adapter111);

					pw.dismiss();
					settingsPageIndicator.setCurrentItem(0);
					viewPager.setCurrentItem(0, false);
					Toast.makeText(getApplicationContext(),
							getString(R.string.set_loaded), Toast.LENGTH_SHORT)
							.show();
				}
			};

			setone.setOnClickListener(loadSetListener);
			settwo.setOnClickListener(loadSetListener);
			setthree.setOnClickListener(loadSetListener);
			setfour.setOnClickListener(loadSetListener);

			pw = new PopupWindow(layoutOwn1,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pw.setAnimationStyle(-1);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(layoutOwn1, Gravity.CENTER, 0, 0);
			return true;
		} else if (item.getItemId() == R.id.opt_about_this_app) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.about_this_app));
			alertDialog.setMessage(Html.fromHtml(getString(R.string.info)));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alertDialog.show();
			TextView message = (TextView) alertDialog
					.findViewById(android.R.id.message);
			message.setTextColor(Color.WHITE); // Workaround to prevent dialogue
												// text to change color when
												// touched.
			Linkify.addLinks(message, Linkify.WEB_URLS);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public class ResetSettings extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		public ResetSettings() {
			dialog = new ProgressDialog(mContext);
		}

		protected void onPreExecute() {
			this.dialog.setMessage(getString(R.string.reset));
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			viewPager = (ViewPager) findViewById(R.id.viewPager);
			SettingsPager adapter = new SettingsPager(mContext);
			viewPager.setAdapter(adapter);
			settingsPageIndicator = (TitlePageIndicator) findViewById(R.id.titles);
			settingsPageIndicator.setViewPager(viewPager);
			settingsPageIndicator.setCurrentItem(0);
			viewPager.setCurrentItem(0, false);
		}

		protected Boolean doInBackground(final String... args) {
			try {

				DB.getDB().createOrRebuildSettingsTable(mContext);

			} catch (Exception e) {
				Log.v("DEBUG", "Fehler bei Set-Erstellung : " + e);
			}
			return null;
		}
	}

}