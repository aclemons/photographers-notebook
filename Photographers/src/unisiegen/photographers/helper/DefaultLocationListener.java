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