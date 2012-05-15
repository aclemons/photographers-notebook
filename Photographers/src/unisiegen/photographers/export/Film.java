package unisiegen.photographers.export;

import java.util.ArrayList;

public class Film {
	
	public Allgemein Allgemein;
	public ArrayList<BildObjekt> Bilder;
	
	public Film(Allgemein _film, ArrayList<BildObjekt> _filmbilderliste) {
		Allgemein = _film;
		Bilder = _filmbilderliste;
	}
	
	public Film() {
	}

	public class Allgemein {

	public String Titel;
	public String Kamera;
	public String Filmnotiz;
	public String Filmformat;
	public String Empfindlichkeit;
	public String Filmtyp;
	public String Sonderentwicklung1;
	public String Sonderentwicklung2;
	
	public Allgemein() {
		
	}
	
	public Allgemein(String _titel, String _kamera, String _filmnotiz, String _filmformat, 
					  String _empfindlichkeit, String _filmtyp, String _sonderentwicklung1, 
					  String _sonderentwicklung2) 
	{
		Titel = _titel;
		Kamera = _kamera;
		Filmnotiz = _filmnotiz;
		Filmformat = _filmformat;
		Empfindlichkeit = _empfindlichkeit;
		Filmtyp = _filmtyp;
		Sonderentwicklung1 = _sonderentwicklung1;
		Sonderentwicklung2 = _sonderentwicklung2;
	}
	
	}
	public class BildObjekt {
		
		public String Bildnummer;
		public String Objektiv;
		public String Blende;
		public String Zeit;
		public String Fokus;
		public String Filter;
		public String Makro;
		public String FilterVF;
		public String MakroVF;
		public String Messmethode;
		public String Belichtungskorrektur;
		public String Blitz;
		public String Blitzkorrektur;
		public String Zeitstempel;
		public String GeoTag;
		public String Notiz;
		public String KameraNotiz;
		
		public BildObjekt(String _bildnummer, String _objektiv, String _blende, String _zeit, 
				 	      String _fokus, String _filter, String _makro, String _filterVF, 
				 	      String _makroVF,String _messmethode, String _belichtungskorrektur, 
				 	      String _blitz, String _blitzkorrektur, String _zeitstempel, String _geoTag, 
				 	      String _notiz, String _kameraNotiz) 
		{
			Bildnummer = _bildnummer;
			Objektiv = _objektiv;
			Blende = _blende;
			Zeit = _zeit;
			Fokus = _fokus;
			Filter = _filter;
			Makro = _makro;
			FilterVF = _filterVF;
			MakroVF = _makroVF;
			Messmethode = _messmethode;
			Belichtungskorrektur = _belichtungskorrektur;
			Blitz = _blitz;
			Blitzkorrektur = _blitzkorrektur;
			Zeitstempel = _zeitstempel;
			GeoTag = _geoTag;
			Notiz = _notiz;
			KameraNotiz = _kameraNotiz;
		}
	}
}
