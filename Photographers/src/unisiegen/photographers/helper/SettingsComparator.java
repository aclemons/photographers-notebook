package unisiegen.photographers.helper;

import java.util.Comparator;

import android.util.Log;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Setting;

public class SettingsComparator implements Comparator<Setting> {
	
	String settingsName;
	String[] alwaysOnTop = { "Auto", "Unendlich", "Normal", "Keiner" }; // TODO: English version!
	
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
		
		// BLITZKORR
		if (settingsName.equals(DB.MY_DB_TABLE_SETKOR) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "");
			secondSetting = secondSetting.replace(",", ".").replace("+", "");
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
				String nenner1 = firstSetting.substring(firstSetting.indexOf("/") + 1 , firstSetting.length());
				Float float1 = 1 / stringToFloat(nenner1);
				firstSetting = float1.toString();
			}
			
			if (secondSetting.contains("/")) {
				String nenner2 = secondSetting.substring(secondSetting.indexOf("/") + 1 , secondSetting.length());
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
		
		//FILTER-VF
		if (settingsName.equals(DB.MY_DB_TABLE_SETFVF) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("x", "").trim();
			secondSetting = secondSetting.replace(",", ".").replace("x", "").trim();
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//MAKRO-VF2
		if (settingsName.equals(DB.MY_DB_TABLE_SETMVF2) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "");
			secondSetting = secondSetting.replace(",", ".").replace("+", "");
			returnValue = -compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//FILTER-VF2
		if (settingsName.equals(DB.MY_DB_TABLE_SETFVF2) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "");
			secondSetting = secondSetting.replace(",", ".").replace("+", "");
			returnValue = -compareStringsAsFloats(firstSetting, secondSetting);
		}		
		
		//FILM EMPFINDLICHKEIT
		if (settingsName.equals(DB.MY_DB_TABLE_SETEMP) && !specialSettingFound) {
			firstSetting = firstSetting.replace("ISO", "").trim();
			secondSetting = secondSetting.replace("ISO", "").trim();
			
			if (firstSetting.contains("/")) {
				firstSetting = firstSetting.substring(0, firstSetting.indexOf("/"));
			}
			
			if (secondSetting.contains("/")) {
				secondSetting = secondSetting.substring(0, secondSetting.indexOf("/"));
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
