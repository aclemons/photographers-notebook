package unisiegen.photographers.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Camera;
import unisiegen.photographers.model.Equipment;
import unisiegen.photographers.model.Lens;
import unisiegen.photographers.model.Setting;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.thoughtworks.xstream.XStream;


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

        File newFile = new File(context.getFilesDir(), fileName);

        Uri uri = FileProvider.getUriForFile(context, "unisiegen.photographers.fileprovider", newFile);
        Log.v("Check", "Attempting to share " + newFile.getAbsolutePath());
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setData(uri);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Equipment Export");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("text/xml");
        
        // TODO: This is a hack that we use for the time beeing, as 2.3.3 seems to be buggy in terms of setting
        // security settings via IntentFlags
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(sendIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            Log.v("Check", "ACTIVITIES TO HANDLE SENDINTENT: " + packageName);
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.export_equipment)));
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
        
        File file = new File(context.getFilesDir(), fileName);
        
        try {
            FileOutputStream fos = new FileOutputStream(file);
            xs.toXML(equipment, fos);
            fos.close();
            Log.v("Check", "XML Export: " + file.getAbsolutePath() + " was written.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("Check", "Failed to write XML Export: " + file.getAbsolutePath());
        }

        return null;
    }

}


