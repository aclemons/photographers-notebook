package unisiegen.photographers.activity;

import android.widget.ImageView;
import android.widget.TextView;

class FilmsViewHolder {
	private TextView textViewTime;
	private TextView textViewName;
	private TextView textViewCam;
	private TextView textViewPics;
	private ImageView imageViewBild;

	public FilmsViewHolder(TextView textViewname, TextView textViewtime,
			TextView textViewcam, TextView textViewpics, ImageView Bilds) {

		this.textViewTime = textViewtime;
		this.textViewName = textViewname;
		this.textViewCam = textViewcam;
		this.textViewPics = textViewpics;
		this.imageViewBild = Bilds;
	}

	public TextView getTextViewName() {
		return textViewName;
	}

	public TextView getTextViewTime() {
		return textViewTime;
	}

	public TextView getTextViewCam() {
		return textViewCam;
	}

	public TextView getTextViewPics() {
		return textViewPics;
	}

	public ImageView getBildView() {
		return imageViewBild;
	}
}