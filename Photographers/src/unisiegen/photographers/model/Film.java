/* Copyright (C) 2012 Sebastian Draxler, Alexander Boden, Christian Woehrl (Committers)
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

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Film {

	public int	ID;
	public String Titel;
	public String Kamera;
	public String Filmbezeichnung;
	public String Filmformat;
	public String Empfindlichkeit;
	public String Filmtyp;
	public String Sonderentwicklung1;
	public String Sonderentwicklung2;
	public String Datum;
	public Bitmap icon;
	public String iconData;
	public String Pics;
	public String version;

	public ArrayList<Bild> Bilder;

	public Film() {
	}

	public void setIcon(String iconData) {
		this.iconData = iconData;
		byte[] imageData = Base64.decode(iconData, Base64.DEFAULT);
		this.icon = BitmapFactory.decodeByteArray(imageData, 0,
				imageData.length);
	}

	public String toString() {
		StringBuilder sr = new StringBuilder();
		sr.append(Titel);
		sr.append(" ");
		sr.append(Kamera);
		sr.append(" ");
		if (Filmbezeichnung != null && Filmbezeichnung.length() > 0)
			sr.append(Filmbezeichnung);
		return sr.toString();
	}

}
