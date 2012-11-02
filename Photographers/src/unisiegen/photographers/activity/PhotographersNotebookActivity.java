package unisiegen.photographers.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PhotographersNotebookActivity extends Activity {

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.opt_openSettings) {
			Intent myIntent3 = new Intent(getApplicationContext(),
					EditSettingsActivity.class);
			startActivityForResult(myIntent3, 0);
			return true;
		} else if (item.getItemId() == R.id.opt_backToMenu) {
			finish();
			startActivity(new Intent(getApplicationContext(),
					FilmContentActivity.class));
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}
