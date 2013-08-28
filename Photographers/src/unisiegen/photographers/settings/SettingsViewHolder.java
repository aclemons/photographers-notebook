package unisiegen.photographers.settings;

import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsViewHolder {

	private CheckBox checkBox;
	private TextView textView;

	public SettingsViewHolder(TextView textView, CheckBox checkBox) {
		this.checkBox = checkBox;
		this.textView = textView;
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public TextView getTextView() {
		return textView;
	}
}