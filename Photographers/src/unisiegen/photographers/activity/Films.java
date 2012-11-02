package unisiegen.photographers.activity;

import unisiegen.photographers.export.Film;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

class Films {
	private String name = "";
	private String datum = "";
	private String cam = "";
	private String pics = "";
	private Bitmap bild;

	public Films(String name, String datum, String cam, String pics,
			String Bild) {
		this.name = name;
		this.datum = datum;
		this.cam = cam;
		this.pics = pics;
		
		byte [] imageData = Base64.decode(Bild, Base64.DEFAULT);
		this.bild = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
	}

	public Films(Film film) {
		this(film.Titel, film.Datum, film.Kamera, film.Pics,
				film.iconData);
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return datum;
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