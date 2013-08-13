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

public class Setting {
	
	// corresponds with the database table
	public String type;
	
	// The value (of a certain type), as displayed in the UI
	public String value;
	
	// true if selected in settings dialog = should be displayed as possible value in the UI.
	private boolean shouldBeDisplayed = false;

	public Setting(String type, String value, int shouldBeDisplayed) {
		this.type = type;
		this.value = value;
		if (shouldBeDisplayed == 1) {
			this.shouldBeDisplayed = true;
		} else
			this.shouldBeDisplayed = false;
	}

	public String getValue() {
		return value;
	}

	public int shouldBeDisplayed() {
		if (shouldBeDisplayed) {
			return 1;
		} else
			return 0;
	}

	public void setDisplay(boolean checked) {
		this.shouldBeDisplayed = checked;
	}

	public String toString() {
		if (shouldBeDisplayed) {
			return type + "::" + value + "::1";
		} else
			return type + "::" + value + "::0";
	}
}