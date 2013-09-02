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

import java.util.List;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Setting;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsArrayAdapter extends ArrayAdapter<Setting> {

	private LayoutInflater inflater;

	public SettingsArrayAdapter(Context context, List<Setting> settings) {
		super(context, R.layout.list_item, R.id.listItemText, settings);
		// Cache the LayoutInflate to avoid asking for a new one each time.
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Planet to display
		Setting planet = (Setting) this.getItem(position);

		// The child views in each row.
		CheckBox checkBox;
		TextView textView;

		// Create a new row view
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, null);

			// Find the child views.
			textView = (TextView) convertView.findViewById(R.id.listItemText);
			checkBox = (CheckBox) convertView.findViewById(R.id.check);

			// Optimization: Tag the row with it's child views, so we don't
			// have to
			// call findViewById() later when we reuse the row.
			convertView.setTag(new SettingsViewHolder(textView, checkBox));

			// If CheckBox is toggled, update the planet it is tagged with.
			checkBox.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					Setting planet = (Setting) cb.getTag();
					planet.setDisplay(cb.isChecked());
					int value = 0;
					if (cb.isChecked() == true) {
						value = 1;
					}

					DB.getDB().updateSetting(getContext(), planet.getType(),
							planet.getValue(), value);
				}
			});
		}
		// Reuse existing row view
		else {
			// Because we use a ViewHolder, we avoid having to call
			// findViewById().
			SettingsViewHolder viewHolder = (SettingsViewHolder) convertView
					.getTag();
			checkBox = viewHolder.getCheckBox();
			textView = viewHolder.getTextView();
		}

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

		if (planet.isDefaultValueB()) {
			textView.setTextColor(0xFF0000AA);
		} else {
			textView.setTextColor(0xFF000000);
		}

		return convertView;
	}

}