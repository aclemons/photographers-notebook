package unisiegen.photographers.export;

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