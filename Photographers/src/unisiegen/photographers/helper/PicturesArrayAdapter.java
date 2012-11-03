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