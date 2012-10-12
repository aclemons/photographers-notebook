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
		
}
