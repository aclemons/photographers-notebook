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

import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;

public class FilmExportTask extends AsyncTask<String, Void, Boolean> {

    String FilmID;
    String fileName;
    private ProgressDialog dialog;
    Context context;

    public FilmExportTask(String _FilmID, Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
        FilmID = _FilmID;
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

        Film film = DB.getDB().getFilm(context, FilmID);

        fileName = FilmID + ".xml";

        XStream xs = new XStream();
        xs.alias("Bild", Bild.class);
        xs.alias("Film", Film.class);

        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    context.MODE_WORLD_READABLE);
            xs.toXML(film, fos);
            fos.close();
            Log.v("Check", "XML Export: " + fileName + " was written.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("Check", "Failes to write XML Export: " + fileName);
        }

        return null;
    }
}