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

package unisiegen.photographers.activity;

import java.util.ArrayList;

import unisiegen.photographers.database.DataSource;
import unisiegen.photographers.helper.DefaultLocationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PhotographersNotebookActivity extends Activity {

	public static final String ACTIVEGEARSET = "ActiveGearSet";
	
	private LocationManager locManager;
	private DefaultLocationListener locListener;

	protected void onResume() {
		super.onResume();

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		if (settings.getString(EditSettingsActivity.GEO_TAG, "nein").equals("ja")) {
			locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locListener = new DefaultLocationListener();
			locListener.setLast(locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 5, locListener);
		}
		
		int activegearSetId = settings.getInt(ACTIVEGEARSET, -1);
		if(activegearSetId == -1){
			// Log this also
			ArrayList<Integer> setIds = DataSource.getInst(this).getGearSets();
			if(setIds != null && setIds.size() != 0){
				activegearSetId = setIds.get(0).intValue();
				settings.edit().putInt(ACTIVEGEARSET, activegearSetId);
				
			} else {
				// Do some logging
			}
			
		}
		
		// check if this exists
		// if not, pick the first one, so nothing breaks.
		// in other activities, make use of this setting.
	}

	public void onPause() {
		super.onPause();
		if(locManager != null && locListener != null){
			locManager.removeUpdates(locListener);
		}
	}

	
	protected DefaultLocationListener getLocListener(){
		return locListener;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.generic_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.opt_openSettings) {
			Intent openSettings = new Intent(getApplicationContext(),
					EditSettingsActivity.class);
			startActivityForResult(openSettings, 0);
			return true;
		} else if (item.getItemId() == R.id.opt_backToMenu) {
			finish();
			startActivity(new Intent(getApplicationContext(),
					FilmSelectionActivity.class));
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}
