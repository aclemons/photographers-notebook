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

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class DefaultLocationListener implements LocationListener {

	Location last;

	public void onLocationChanged(Location location) {
		last = location;
		Log.v("GPS", "New Location: " + location);
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public Location getLast() {
		return last;
	}
	
	public void setLast(Location last){
		this.last = last;
	}

}