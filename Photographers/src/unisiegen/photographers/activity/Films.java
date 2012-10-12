package unisiegen.photographers.activity;

import unisiegen.photographers.export.Film;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

class Films {
	private String name = "";
	private String time = "";
	private String cam = "";
	private String pics = "";
	private Bitmap bild;

	public Films(String name, String time, String cam, String pics,
			String Bild) {
		this.name = name;
		this.time = time;
		this.cam = cam;
		this.pics = pics;
		this.bild = BitmapFactory.decodeByteArray(
				Base64.decode(Bild, Base64.DEFAULT), 0,
				(Base64.decode(Bild, Base64.DEFAULT)).length);
	}

	public Films(Film film) {
		this(film.Titel, film.Bilder.get(0).Zeit, film.Kamera, "",
				film.iconData);
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public String getCam() {
		return cam;
	}

	public String getPics() {
		return pics;
	}

	public Bitmap getBild() {
		return bild;
	}
}