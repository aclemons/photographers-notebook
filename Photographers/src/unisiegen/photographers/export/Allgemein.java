package unisiegen.photographers.export;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Allgemein {

	public String Titel;
	public String Kamera;
	public String Filmnotiz;
	public String Filmformat;
	public String Empfindlichkeit;
	public String Filmtyp;
	public String Sonderentwicklung1;
	public String Sonderentwicklung2;
	public Bitmap icon;
	public String iconData;

	public Allgemein() {

	}

	public Allgemein(String _titel, String _kamera, String _filmnotiz,
			String _filmformat, String _empfindlichkeit, String _filmtyp,
			String _sonderentwicklung1, String _sonderentwicklung2) {
		Titel = _titel;
		Kamera = _kamera;
		Filmnotiz = _filmnotiz;
		Filmformat = _filmformat;
		Empfindlichkeit = _empfindlichkeit;
		Filmtyp = _filmtyp;
		Sonderentwicklung1 = _sonderentwicklung1;
		Sonderentwicklung2 = _sonderentwicklung2;
	}

	public void setIcon(String iconData) {
		this.iconData = iconData;
		icon = BitmapFactory.decodeByteArray(
				Base64.decode(iconData, Base64.DEFAULT), 0,
				(Base64.decode(iconData, Base64.DEFAULT)).length);
	}

}