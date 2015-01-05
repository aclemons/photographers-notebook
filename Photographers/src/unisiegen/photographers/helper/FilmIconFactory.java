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

package unisiegen.photographers.helper;

import unisiegen.photographers.model.Film;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class FilmIconFactory {

	public Bitmap createBitmap(Film film) {

		// Size of the Bitmap in pixels
		int x = 200;
		int y = 200;

		// Get the values we want to display
		String iso = film.Empfindlichkeit;
		String type = film.Filmformat;
		String brand = film.Filmtyp;

		// Define the default variables for the design. Each icon consists of
		// three stripes
		// with individual colors, heights, and texts they can display (apart of
		// the top stripe
		// which is only decoration).
		//
		// In the current version, all stripes are displayed in the same pastel
		// color.

		int heightTop = 30;
		String colorTop = "#04B431";

		int heightMiddle = 80;
		String colorMiddle = "#04B431";
		String colorMiddleText = "white";

		String colorBottom = "#04B431";
		String colorBottomText = "white";

		int marginText = 5; // Margin for the text on the bottom stripe
		int textSize = 40; // Standard text size

		// Variables for the text to be displayed on the icon (will be modified
		// later)
		String middleText = "Film";
		String bottomLeftText = "";
		String bottomRightText = "";

		// Define different styles for different film types. We also set the
		// text on the badge according to the kind of film.

		// final String color_red = "#DD597D";
		final String color_cyan = "#44B4D5";
		// final String color_violet = "#9588EC";
		final String color_orange = "#FFAC62";
		final String color_green = "#93BF96";
		final String color_lgray = "#999999";
		final String color_dgray = "#555555";

		if (brand != null) {
			if (brand.contains("I: CR")) { // TODO: read these from the string
											// resources.

				middleText = "CR";

				heightTop = 30;
				colorTop = color_green;

				heightMiddle = 80;
				colorMiddle = color_green;
				colorMiddleText = "white";

				colorBottom = color_green;
				colorBottomText = "white";

			} else if (brand.contains("I: CT")) {

				middleText = "CT";

				heightTop = 30;
				colorTop = color_cyan;

				heightMiddle = 80;
				colorMiddle = color_cyan;
				colorMiddleText = "white";

				colorBottom = color_cyan;
				colorBottomText = "white";

			} else if (brand.contains("I: CN")) {

				middleText = "CN";

				heightTop = 30;
				colorTop = color_orange;

				heightMiddle = 80;
				colorMiddle = color_orange;
				colorMiddleText = "white";

				colorBottom = color_orange;
				colorBottomText = "white";

			} else if (brand.contains("I: SWR")) {

				middleText = "SWR";

				heightTop = 30;
				colorTop = color_lgray;

				heightMiddle = 80;
				colorMiddle = color_lgray;
				colorMiddleText = "white";

				colorBottom = color_lgray;
				colorBottomText = "white";

			} else if (brand.contains("I: SW")) {

				middleText = "SW";

				heightTop = 30;
				colorTop = color_dgray;

				heightMiddle = 80;
				colorMiddle = color_dgray;
				colorMiddleText = "white";

				colorBottom = color_dgray;
				colorBottomText = "white";

			}

		}

		// Some re-formatting of the database defaults...
		// if (name.length() == 0) { name = badgeName; }

		if (iso.contains("/")) {
			iso = iso.substring(0, iso.indexOf("/"));
		}
		if (iso.contains("ISO ")) {
			iso = iso.replace("ISO ", "");
		}
		if (iso.length() > 4) {
			iso = iso.substring(0, 5);
		} // As iso can be set by the user in the settings we should make sure
			// the text does not get too long...
		bottomLeftText = iso;

		if (type.contains("24x36")) {
			bottomRightText = "135";
		} // Here we define only two values right now ... 135er or 120er film.
			// If the user has entered something other than the defaults here,
			// we display nothing.
		if (type.contains("4,5x6") || type.contains("6x6")
				|| type.contains("6x7") || type.contains("6x9")) {
			bottomRightText = "120";
		}

		// Finally, we create the Bitmap ...
		Bitmap returnedBitmap = Bitmap.createBitmap(x, y,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(returnedBitmap);

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		// Top stripe
		canvas.drawPaint(paint);
		paint.setColor(Color.parseColor(colorTop));
		canvas.drawRect(0, 0, x, heightTop, paint);

		// Middle stripe
		paint.setColor(Color.parseColor(colorMiddle));
		canvas.drawRect(0, heightTop, x, (heightTop + heightMiddle), paint);

		// Bottom stripe
		paint.setColor(Color.parseColor(colorBottom));
		canvas.drawRect(0, (heightTop + heightMiddle), x, y, paint);

		// Text middle stripe
		paint.setTextSize(textSize + 50);
		Rect bounds = new Rect(); // Trick to center text vertically ...
		paint.getTextBounds("A", 0, 1, bounds);
		paint.setColor(Color.parseColor(colorMiddleText));
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(middleText, x >> 1,
				(heightTop + (heightMiddle >> 1) + (bounds.height() >> 1)),
				paint);

		// Text bottom stripe
		paint.setTextSize(textSize);
		paint.setColor(Color.parseColor(colorBottomText));
		paint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(bottomRightText, (x - marginText), (y - marginText),
				paint);

		paint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(bottomLeftText, (0 + marginText), (y - marginText),
				paint);

		return returnedBitmap;

	}

}
