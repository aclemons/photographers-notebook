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