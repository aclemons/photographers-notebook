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
	// value that is displays in all the dialogs
	public String name;
	// is it selected/activated to be used when creaing a film/picture
	private boolean checked = false;

	public Setting(String name, int Status) {
		this.name = name;
		if (Status == 1) {
			checked = true;
		} else
			checked = false;
	}

	public String getName() {
		return name;
	}

	public int isChecked() {
		if (checked) {
			return 1;
		} else
			return 0;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String toString() {
		if (checked) {
			return 1 + " " + name;
		} else
			return 0 + " " + name;
	}
}