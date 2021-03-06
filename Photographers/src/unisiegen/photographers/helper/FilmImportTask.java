package unisiegen.photographers.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import unisiegen.photographers.activity.FilmSelectionActivity;
import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.database.DataSource;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.thoughtworks.xstream.XStream;

/**
 * Created by aboden on 23.07.14.
 */
public class FilmImportTask extends AsyncTask<String, Void, Boolean> {

    File file;
    Film film = new Film();
    private ProgressDialog dialog;
    Context context;
    Boolean import_success = true;
    
    FilmSelectionActivity myActivity;

    public FilmImportTask(Context context, File file, FilmSelectionActivity myActivity) {
        this.context = context;
        this.file = file;
        this.myActivity = myActivity;
        dialog = new ProgressDialog(context);
    }

    protected void onPreExecute() {
        this.dialog.setMessage(context.getString(R.string.import_data));
        this.dialog.show();
        Log.v("Check", "Bereite Import von Film vor");
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle(context.getString(R.string.info_title));
        if (import_success) {
            alert.setMessage(context.getString(R.string.info_backup_success));
        } else {
            alert.setMessage(context.getString(R.string.info_backup_failed));
        }
        alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alert.show();
        
        myActivity.refreshUI();
        
    }

    protected Boolean doInBackground(final String... args) {

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            import_success = false;
        }

        XStream xs = new XStream();
        xs.alias("Bild", Bild.class);
        xs.alias("Film", Film.class);

        if (input != null) {
            try {
                film = (Film) xs.fromXML(input);
                Log.v("Check", "Film importiert aus Datei: " + file.getAbsolutePath());
            } catch (Exception e) {
                Log.v("Check", "Import von Datei fehlgeschlagen: " + e.toString());
                import_success = false;
            }

            if (import_success) {
                try {
                	DataSource ds = DataSource.getInst(context);
                	long success = ds.addFilm(film);
                	
                	if(success == -1){
                		import_success = false;
                		return null;
                	}
                	
                	if(film.Bilder != null){
                		// need to reload from db...
                		Film dbFilm = ds.getFilm(film.Titel);
                		if(dbFilm == null){
                			import_success = false;
                    		return null;
                		}
                		
                		for(Bild bild : film.Bilder){
                			ds.addPhoto(dbFilm, bild);
                		}
                	}
                } catch (Exception e) {
                    e.printStackTrace();
                    import_success = false;
                }
            }
        }


        return null;
    }

}
