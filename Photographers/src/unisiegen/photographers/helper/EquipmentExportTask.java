package unisiegen.photographers.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Camera;
import unisiegen.photographers.model.Film;
import unisiegen.photographers.model.Lens;
import unisiegen.photographers.model.Setting;

/**
 * Created by aboden on 16.07.14.
 */

public class EquipmentExportTask extends AsyncTask<String, Void, Boolean> {

    ArrayList<Camera> cameras = new ArrayList<Camera>();
    ArrayList<Lens> lenses = new ArrayList<Lens>();

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
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Film Export");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        sendIntent.setType("text/html");
        context.startActivity(sendIntent);
    }

    protected Boolean doInBackground(final String... args) {

        cameras = DB.getDB().getAllCameras(context);
        lenses = DB.getDB().getAllLenses(context);

        XStream xs = new XStream();
        xs.alias("Camera", Camera.class);
        xs.alias("Lens", Lens.class);

        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    context.MODE_WORLD_READABLE);
            xs.toXML(cameras, fos);
            xs.toXML(lenses, fos);
            fos.close();
            Log.v("Check", "XML Export: " + fileName + " was written.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("Check", "Failed to write XML Export: " + fileName);
        }

        return null;
    }

}
