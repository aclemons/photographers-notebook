package unisiegen.photographers.export;

import android.util.Log;


public class Export {

	public Export () {
	}

	public String exportFilm(Film film) {
		XMLExport exi = new XMLExport();
		String xml = exi.toXML(film);
		Log.v("Check",xml);
		return xml;
	}
	
}
