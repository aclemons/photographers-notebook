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
package unisiegen.photographers.settings;

import java.util.ArrayList;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Setting;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is a hack... as I seem to be too stupid to subclass from View in a
 * usable way...
 * 
 * @author sdraxler
 */
public class SettingsViewPart {

	private LayoutInflater inflater;
	private View view;
	private TextView title;

	private TableLayout layout;

	private ListView list;
	private ArrayList<Setting> values;
	private SettingsArrayAdapter listAdapter;

	public View getView() {
		return view;
	}

	public SettingsViewPart(final Context context, int titleID,
			final String settingName) {

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.settingsauswahl, null, false);

		title = (TextView) view.findViewById(R.id.freecell);
		title.setText(context.getString(titleID));

		layout = (TableLayout) view.findViewById(R.id.tablor);
		Button buttonAdd = (Button) view.findViewById(R.id.addkamera);
		final EditText textNewItem = ((EditText) view
				.findViewById(R.id.kameramodell));
		list = (ListView) view.findViewById(android.R.id.list);

		// layout.setBackgroundResource(R.drawable.shaperedtable);

		values = DB.getDB().getAllSettings(context, settingName);

		listAdapter = new SettingsArrayAdapter(context, values);
		list.setAdapter(listAdapter);

		layout.setPadding(4, 0, -2, 0);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					final View arg1, final int arg2, long arg3) {
				View layoutOwn = inflater.inflate(R.layout.longclick,
						(ViewGroup) view.findViewById(R.id.testen), false);
				Button deleteButton = (Button) layoutOwn
						.findViewById(R.id.deletebutton);
				Button cancelButton = (Button) layoutOwn
						.findViewById(R.id.cancelbutton);
				Button setDefaultButton = (Button) layoutOwn
						.findViewById(R.id.editbutton);
				deleteButton.setText(context.getString(R.string.delete_entry));
				setDefaultButton.setText(context
						.getString(R.string.make_default));
				setDefaultButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						LinearLayout lins = (LinearLayout) arg1;
						TextView texti = (TextView) lins.getChildAt(0);

						DB.getDB().setDefaultVal(context, settingName,
								texti.getText().toString());

						listAdapter.clear();
						values = DB.getDB()
								.getAllSettings(context, settingName);
						for (Setting s : values) {
							listAdapter.add(s);
						}

						Toast.makeText(context,
								context.getString(R.string.default_saved),
								Toast.LENGTH_SHORT).show();

						Object o = v.getTag();
						if (o instanceof PopupWindow) {
							PopupWindow pw = (PopupWindow) o;
							pw.dismiss();
						}
					}
				});
				deleteButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						LinearLayout lins = (LinearLayout) arg1;
						TextView texti = (TextView) lins.getChildAt(0);

						DB.getDB().deleteSetting(context, settingName,
								texti.getText().toString());
						listAdapter.clear();
						// activity.readDB(); // TODO: Die zeile muss später
						// raus...
						values = DB.getDB()
								.getAllSettings(context, settingName);
						for (Setting s : values) {
							listAdapter.add(s);
						}

						Toast.makeText(context,
								context.getString(R.string.deleted),
								Toast.LENGTH_SHORT).show();

						Object o = v.getTag();
						if (o instanceof PopupWindow) {
							PopupWindow pw = (PopupWindow) o;
							pw.dismiss();
						}
					}
				});
				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Object o = v.getTag();
						if (o instanceof PopupWindow) {
							PopupWindow pw = (PopupWindow) o;
							pw.dismiss();
						}
					}
				});

				PopupWindow pw = new PopupWindow(layoutOwn,
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT, true);
				pw.setAnimationStyle(7);
				pw.setBackgroundDrawable(new ColorDrawable());
				pw.showAtLocation(layoutOwn, Gravity.CENTER, 0, 0);
				setDefaultButton.setTag(pw);
				deleteButton.setTag(pw);
				cancelButton.setTag(pw);

				return true;
			}
		});
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				boolean vorhanden = false;
				String newVal = textNewItem.getText().toString();
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						textNewItem.getApplicationWindowToken(), 0);

				for (Setting s : values) {
					vorhanden = s.getValue().equals(newVal);
					break;
				}

				if (vorhanden || newVal.length() == 0
						|| newVal.trim().length() == 0) {

					Toast.makeText(
							context,
							context.getString(R.string.empty_or_existing_entry),
							Toast.LENGTH_SHORT).show();
				} else {

					DB.getDB().addSetting(context, settingName, newVal, 1);
					listAdapter.clear();
					values = DB.getDB().getAllSettings(context, settingName);
					for (Setting s : values) {
						listAdapter.add(s);
					}

					textNewItem.setText("");

					Toast.makeText(context,
							context.getString(R.string.entry_saved),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		textNewItem.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager imm = (InputMethodManager) context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							textNewItem.getApplicationWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
	}
}