package unisiegen.photographers.helper;

import java.util.Comparator;

import android.util.Log;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Setting;

public class SettingsComparator implements Comparator<Setting> {
	
	String settingsName;
	String[] alwaysOnTop = { "Auto", "Unendlich" , "Normal", "Keiner" }; // TODO: English version!
	
	public SettingsComparator(String settingsName) {
		this.settingsName = settingsName;
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
		
		String firstSetting = lhs.getValue();
		String secondSetting = rhs.getValue();
		
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
			firstSetting = firstSetting.replace(",", ".");
			secondSetting = secondSetting.replace(",", ".");
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		// PLUSMINUS
		if (settingsName.equals(DB.MY_DB_TABLE_SETPLU) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "");
			secondSetting = secondSetting.replace(",", ".").replace("+", "");
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		// BLITZKORR
		if (settingsName.equals(DB.MY_DB_TABLE_SETKOR) && !specialSettingFound) {
			firstSetting = firstSetting.replace(",", ".").replace("+", "");
			secondSetting = secondSetting.replace(",", ".").replace("+", "");
			returnValue = compareStringsAsFloats(firstSetting, secondSetting);
		}
		
		//FOKUS
		if (settingsName.equals(DB.MY_DB_TABLE_SETFOK) && !specialSettingFound) {
			
		}
		
		//ZEIT
		if (settingsName.equals(DB.MY_DB_TABLE_SETZEI) && !specialSettingFound) {
			
		}
		
		//MAKRO-FV
		if (settingsName.equals(DB.MY_DB_TABLE_SETMVF) && !specialSettingFound) {
			
		}
		
		//FILTER-VF
		if (settingsName.equals(DB.MY_DB_TABLE_SETFVF) && !specialSettingFound) {
			
		}
		
		//FILM EMPFINDLICHKEIT
		if (settingsName.equals(DB.MY_DB_TABLE_SETEMP) && !specialSettingFound) {
			
		}
		
		//BLITZ
		if (settingsName.equals(DB.MY_DB_TABLE_SETBLI) && !specialSettingFound) {
			
		}
		
		return returnValue;
		
	}

}
