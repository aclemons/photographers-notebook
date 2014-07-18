package unisiegen.photographers.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Camera;
import unisiegen.photographers.model.Equipment;
import unisiegen.photographers.model.Lens;
import unisiegen.photographers.model.Setting;


/**
 * Created by aboden on 16.07.14.
 */

public class EquipmentImportTask extends AsyncTask<String, Void, Boolean> {

    File file;
    Equipment equipment = new Equipment();
    private ProgressDialog dialog;
    Context context;

    public EquipmentImportTask(Context context, File file) {
        this.context = context;
        this.file = file;
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
    }

    protected Boolean doInBackground(final String... args) {

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                // TODO: Meldung dass Import fehlgeschlagen ist für den User?
            }
        }

        //TODO: Equipment Objekt in Datenbank schreiben

        /*
        equipment.cameras = DB.getDB().getAllCameras(context);
        equipment.lenses = DB.getDB().getAllLenses(context);
        equipment.filmFormat = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETFF);
        equipment.filmEmpfindlichkeit = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETEMP);
        equipment.brennweite = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETBW);
        equipment.nahzubehoer = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETNM);
        equipment.filter = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETFIL);
        equipment.blitz = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETBLI);
        equipment.fokus = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETFOK);
        equipment.blende = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETBLE);
        equipment.zeit = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETZEI);
        equipment.messung = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETMES);
        equipment.plusminus = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETPLU);
        equipment.makro = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETMAK);
        equipment.makrovf = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETMVF);
        equipment.filterVF = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETFVF);
        equipment.makroVF2 = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETMVF2);
        equipment.filterVF2 = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETFVF2);
        equipment.blitzKorr = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETKOR);
        equipment.filmTyp = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETTYP);
        */

        return null;
    }

}


