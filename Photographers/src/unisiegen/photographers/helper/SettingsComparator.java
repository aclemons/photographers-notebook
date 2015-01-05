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

import java.util.Comparator;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Setting;
import android.util.Log;

public class SettingsComparator implements Comparator<Setting> {
	
	String settingsName;
	String[] alwaysOnTop = { "Auto", "Unendlich", "Infinity", "Normal", "Keiner" , "None" }; 
	
	public SettingsComparator(String settingsName) {
		this.settingsName = settingsName;
	}
	
	private Float stringToFloat(String string) {
		Float value = (float) 1; 
		if (string != null) {
			try {
				value = Float.valueOf(string);
			} catch (NumberFormatException e) {
				Log.v("SettingsComparator", e.toString());
			} 
		}
		
		return value;
	}
	
	private int compareStringsAsFloats(String firstString, String secondString) {	
		Float firstFloat = (float) 0;
		Float secondFloat = (float) 0;
		
		if (firstString != null && secondString != null) {
			try {
				firstFloat = Float.valueOf(firstString);
				secondFloat = Float.valueOf(secondString);
			} catch (NumberFormatException e) {
				Log.v("SettingsComparator", e.toString());
			} 
		}
		
		return (firstFloat > secondFloat ? 1 : (firstFloat == secondFloat ? 0 : -1));
	}
	
	@Override
	public int compare(Setting lhs, Setting rhs) {
				
		int returnValue = 0;
		boolean specialSettingFound = false;
		
		String firstSetting = lhs.getValue().trim();
		String secondSetting = rhs.getValue().trim();
		
		// Always display certain Strings on Top
		for (String s : alwaysOnTop) {
			if (firstSetting.equals(s)) { returnValue = -1; specialSettingFound = true; break; }
			if (secondSetting.equals(s)) { returnValue = 1; specialSettingFound = true; break; }
		}
		
		if (!specialSettingFound) {
			returnValue = firstSetting.compareTo(secondSetting);
		}
		
		// BLENDE
		if (settingsName.equals(DB.MY_DB_TABLE_SETBLE) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").trim();
			secondSetting = secondSetting.replace(",", ".").trim();
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		// PLUSMINUS
		if (settingsName.equals(DB.MY_DB_TABLE_SETPLU) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("+", "").trim();
			returnValue = -compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		// BLITZKORR (identisch zu PLUSMINUS)
		if (settingsName.equals(DB.MY_DB_TABLE_SETKOR) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("+", "").trim();
			returnValue = -compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//FOKUS
		if (settingsName.equals(DB.MY_DB_TABLE_SETFOK) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("m", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("m", "").trim();
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//ZEIT
		if (settingsName.equals(DB.MY_DB_TABLE_SETZEI) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("s", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("s", "").trim();
			
			if (firstSetting.contains("/")) {
				String nenner1 = firstSetting.substring(firstSetting.indexOf("/") + 1 , firstSetting.length()).trim();
				Float float1 = 1 / stringToFloat(nenner1);
				firstSetting = float1.toString();
			}
			
			if (secondSetting.contains("/")) {
				String nenner2 = secondSetting.substring(secondSetting.indexOf("/") + 1 , secondSetting.length()).trim();
				Float float2 = 1 / stringToFloat(nenner2);
				secondSetting = float2.toString();
			}
			
			returnValue = -compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//MAKRO-FV
		if (settingsName.equals(DB.MY_DB_TABLE_SETMVF) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("x", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("x", "").trim();
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//FILTER-VF (identisch zu MAKRO-FV)
		if (settingsName.equals(DB.MY_DB_TABLE_SETFVF) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("x", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("x", "").trim();
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//MAKRO-VF2
		if (settingsName.equals(DB.MY_DB_TABLE_SETMVF2) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("+", "").trim();
			returnValue = -compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//FILTER-VF2 (identisch zu MAKRO-VF2)
		if (settingsName.equals(DB.MY_DB_TABLE_SETFVF2) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("+", "").trim();
			returnValue = -compareStringsAsFloats(firstSetting, secondSetting);
		}		
		
		//FILM EMPFINDLICHKEIT
		if (settingsName.equals(DB.MY_DB_TABLE_SETEMP) && !specialSettingFound) {
			firstSetting = firstSetting.replace("ISO", "").trim();
			secondSetting = secondSetting.replace("ISO", "").trim();
			
			if (firstSetting.contains("/")) {
				firstSetting = firstSetting.substring(0, firstSetting.indexOf("/")).trim();
			}
			
			if (secondSetting.contains("/")) {
				secondSetting = secondSetting.substring(0, secondSetting.indexOf("/")).trim();
			}			

			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//BLITZ
		if (settingsName.equals(DB.MY_DB_TABLE_SETBLI) && !specialSettingFound) {
			
			Float firstValue = (float) 0;
			Float secondValue = (float) 0;
			
			
			if (firstSetting.contains("/")) {
				firstSetting = firstSetting.substring(firstSetting.indexOf("/") + 1, firstSetting.length()).trim();
				firstValue = stringToFloat(firstSetting);
			}
			
			if (secondSetting.contains("/")) {
				secondSetting = secondSetting.substring(secondSetting.indexOf("/") + 1, secondSetting.length()).trim();
				secondValue = stringToFloat(secondSetting);
			}	
			
			if (firstValue != 0 && secondValue != 0) {
				returnValue = compareStringsAsFloats(firstValue.toString(), secondValue.toString());
			}
		}
		
		return returnValue;
		
	}

}
