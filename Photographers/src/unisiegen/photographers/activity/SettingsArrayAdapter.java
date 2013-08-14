package unisiegen.photographers.activity;

import java.util.ArrayList;
import java.util.List;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Setting;
import unisiegen.photographers.settings.SettingsViewHolder;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsArrayAdapter extends ArrayAdapter<Setting> {

	/**
	 * 
	 */
	private final EditSettingsActivity editSettingsActivity;
	private LayoutInflater inflater;
	int nummer = 0;

	public SettingsArrayAdapter(EditSettingsActivity editSettingsActivity, Context context, List<Setting> planetList, int number) {
		super(context, R.layout.list_item, R.id.listItemText, planetList);
		this.editSettingsActivity = editSettingsActivity;
		nummer = number;
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
			textView = (TextView) convertView
					.findViewById(R.id.listItemText);
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
					
					SettingsArrayAdapter.this.editSettingsActivity.editfromDB(planet.getType(), planet.getValue(), value);
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
		this.editSettingsActivity.settings = PreferenceManager.getDefaultSharedPreferences(this.editSettingsActivity.mContext);
		if (nummer == this.editSettingsActivity.FILMFORMAT_POSITION && this.editSettingsActivity.checkFF.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.MAKRO_POSITION && this.editSettingsActivity.checkNM.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.FILTER_POSITION && this.editSettingsActivity.checkFil.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.BLITZ_POSITION && this.editSettingsActivity.checkBli.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.SOND_POSITION && this.editSettingsActivity.checkSon.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.ASA_POSITION && this.editSettingsActivity.checkEmp.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.FOKUS_POSITION && this.editSettingsActivity.checkFok.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.BLENDE_POSITION && this.editSettingsActivity.checkBle.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.ZEITE_POSITION && this.editSettingsActivity.checkZei.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.MESS_POSITION && this.editSettingsActivity.checkMes.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.KORREKTUR_POSITION && this.editSettingsActivity.checkPlu.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.MAKROVF_POSITION && this.editSettingsActivity.checkMakroVF.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.FILTERVF_POSITION && this.editSettingsActivity.checkFilterVF.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else if (nummer == this.editSettingsActivity.BLITZKORR_POSITION && this.editSettingsActivity.checkKor.get(planet.getValue()) == 1) {
			textView.setTextColor(0xFF0000AA);
		} else {
			textView.setTextColor(0xFF000000);
		}

		return convertView;
	}
	

}