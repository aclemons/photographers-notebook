package unisiegen.photographers.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import unisiegen.photographers.activity.EditSettingsActivity;
import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Camera;
import unisiegen.photographers.model.Equipment;
import unisiegen.photographers.model.Lens;
import unisiegen.photographers.model.Setting;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.thoughtworks.xstream.XStream;


/**
 * Created by aboden on 16.07.14.
 */

public class EquipmentImportTask extends AsyncTask<String, Void, Boolean> {
	
	EditSettingsActivity myActivity;
	
    File file;
    Equipment equipment = new Equipment();
    private ProgressDialog dialog;
    Context context;
    Boolean import_success = true;

    public EquipmentImportTask(Context context, File file, EditSettingsActivity myActivity) {
        this.context = context;
        this.file = file;
        this.myActivity = myActivity;
        dialog = new ProgressDialog(context);
    }

    protected void onPreExecute() {
        this.dialog.setMessage(context.getString(R.string.import_data));
        this.dialog.show();
        Log.v("Check", "Bereite Import von Equipment vor");
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
                myActivity.finish();
            	return;
            }
        });
        alert.show();

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
        xs.processAnnotations(Equipment.class);
        xs.processAnnotations(Lens.class);
        xs.processAnnotations(Camera.class);
        xs.processAnnotations(Setting.class);

        if (input != null) {
            try {
                equipment = (Equipment) xs.fromXML(input);
                Log.v("Check", "Equipment importiert aus Datei: " + file.getAbsolutePath());
            } catch (Exception e) {
                Log.v("Check", "Import von Datei fehlgeschlagen: " + e.toString());
                import_success = false;
            }

            if (import_success) {
                try {
                    DB.getDB().createSettingsTableFromEquipmentImport(context, equipment);
                } catch (Exception e) {
                    Log.v("Check", e.toString());
                    import_success = false;
                }
            }
        }


        return null;
    }

}


