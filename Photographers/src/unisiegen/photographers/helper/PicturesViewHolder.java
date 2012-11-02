package unisiegen.photographers.helper;

import android.widget.TextView;

class PicturesViewHolder {
	private TextView textViewTime;
	private TextView textViewName;
	private TextView textViewObjektiv;

	public PicturesViewHolder(TextView textViewname, TextView textViewtime,
			TextView textViewobjektiv) {
		this.textViewTime = textViewtime;
		this.textViewObjektiv = textViewobjektiv;
		this.textViewName = textViewname;
	}

	public TextView getTextViewName() {
		return textViewName;
	}

	public TextView getTextViewTime() {
		return textViewTime;
	}

	public TextView getTextViewObjektiv() {
		return this.textViewObjektiv;
	}
}