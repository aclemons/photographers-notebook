package unisiegen.photographers.export;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Film {
	
	public String Titel;
	public String Kamera;
	public String Filmnotiz;
	public String Filmformat;
	public String Empfindlichkeit;
	public String Filmtyp;
	public String Sonderentwicklung1;
	public String Sonderentwicklung2;
	public String Datum;
	public Bitmap icon;
	public String iconData;
	public String Pics;
	
	public ArrayList<BildObjekt> Bilder;	
	
	public Film() {
	}
	
	public void setIcon(String iconData) {
		this.iconData = iconData;
		byte [] imageData = Base64.decode(iconData, Base64.DEFAULT);
		this.icon = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
	}
	
}
