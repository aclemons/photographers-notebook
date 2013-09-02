/* Copyright (C) 2012 Nico Castelli, Christopher Maiworm 
 * Copyright (C) 2012 Sebastian Draxler, Alexander Boden, Christian Woehrl (Committers)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unisiegen.photographers.model;

public class Bild {

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

	public Bild(String _bildnummer, String _objektiv, String _blende,
			String _zeit, String _fokus, String _filter, String _makro,
			String _filterVF, String _makroVF, String _messmethode,
			String _belichtungskorrektur, String _blitz,
			String _blitzkorrektur, String _zeitstempel, String _geoTag,
			String _notiz, String _kameraNotiz) {
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

	public Bild() {

	}
	
	public String toString() {
		StringBuilder sr = new StringBuilder();
		sr.append(Bildnummer);
		sr.append(" ");
		sr.append(Objektiv);
		sr.append(" ");
		sr.append(Blende);
		sr.append(" ");
		sr.append(Zeit);
		return sr.toString();
	}
	
}