package unisiegen.photographers.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Camera;
import unisiegen.photographers.model.Equipment;
import unisiegen.photographers.model.Lens;
import unisiegen.photographers.model.Setting;


/**
 * Created by aboden on 16.07.14.
 */

public class EquipmentExportTask extends AsyncTask<String, Void, Boolean> {

    Equipment equipment = new Equipment();

    String date = android.text.format.DateFormat.format(
            "dd.MM.yyyy", new java.util.Date()).toString();
    String fileName = "Equipment_Backup_PhotographersNotebook_" + date + ".xml";
    private ProgressDialog dialog;
    Context context;

    public EquipmentExportTask(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    protected void onPreExecute() {
        this.dialog.setMessage(context.getString(R.string.export));
        this.dialog.show();
        Log.v("Check", "Pre");
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        File file = new File(context.getFilesDir() + "/" + fileName);

        Uri u1 = null;
        u1 = Uri.fromFile(file);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Equipment Export");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        sendIntent.setType("text/html");
        context.startActivity(sendIntent);
    }

    protected Boolean doInBackground(final String... args) {

        try {
            // Adds the used version of Photographers Notebook to the xml file ... maybe useful if we want to change anything later.
            equipment.version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.v("Check", e.toString());
        }
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
        equipment.sonder = DB.getDB().getAllSettings(context, DB.MY_DB_TABLE_SETSON);


        XStream xs = new XStream();
        xs.processAnnotations(Equipment.class);
        xs.processAnnotations(Lens.class);
        xs.processAnnotations(Camera.class);
        xs.processAnnotations(Setting.class);

        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    context.MODE_WORLD_READABLE);
            xs.toXML(equipment, fos);
            fos.close();
            Log.v("Check", "XML Export: " + fileName + " was written.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("Check", "Failed to write XML Export: " + fileName);
        }

        return null;
    }

}


