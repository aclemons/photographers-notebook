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

package unisiegen.photographers.helper;

import java.util.ArrayList;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.model.Bild;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PicturesArrayAdapter extends ArrayAdapter<Bild> {

	private LayoutInflater inflater;

	public PicturesArrayAdapter(Context context,
			ArrayList<Bild> planetList, int number) {
		super(context, R.layout.film_item, R.id.listItemText, planetList);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Bild bild = (Bild) this.getItem(position);
		TextView textViewApertureTime;
		TextView textView;
		TextView textViewTime;

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.film_item, null);
			textView = (TextView) convertView.findViewById(R.id.listItemText);
			textViewApertureTime = (TextView) convertView
					.findViewById(R.id.listItemBlendeZeit);
			textViewTime = (TextView) convertView
					.findViewById(R.id.listItemTextTime);
			convertView.setTag(new PicturesViewHolder(textView, textViewTime,
					textViewApertureTime));

		} else {
			PicturesViewHolder viewHolder = (PicturesViewHolder) convertView
					.getTag();
			textViewTime = viewHolder.getTextViewTime();
			textView = viewHolder.getTextViewName();
			textViewApertureTime = viewHolder.getTextViewObjektiv();
		}
		textViewTime.setText(bild.Zeitstempel);
		textView.setText(bild.Bildnummer);
		textViewApertureTime.setText(bild.Blende + " - " + bild.Zeit);

		return convertView;
	}
}