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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import unisiegen.photographers.activity.R;
import unisiegen.photographers.database.DB;
import unisiegen.photographers.database.DataSource;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
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

public class FilmExportTask extends AsyncTask<String, Void, Boolean> {

    String FilmTitle;
    String fileName;
    private ProgressDialog dialog;
    Context context;

    public FilmExportTask(String _FilmID, Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
        FilmTitle = _FilmID;
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
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Film Export");
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
        
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.export_film)));
    }

    protected Boolean doInBackground(final String... args) {

        Film film = DataSource.getInst(context).getFilm(FilmTitle); 
        
        fileName = FilmTitle + ".xml";
        
        File file = new File(context.getFilesDir(), fileName);
               
        try {
            // Adds the used version of Photographers Notebook to the xml file ... maybe useful if we want to change anything later.
            film.version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.v("Check", e.toString());
        }
        
        XStream xs = new XStream();
        xs.alias("Bild", Bild.class);
        xs.alias("Film", Film.class);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            xs.toXML(film, fos);
            fos.close();
            Log.v("Check", "XML Export: " + file.getAbsolutePath() + " was written.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("Check", "Failes to write XML Export: " + file.getAbsolutePath());
        }

        return null;
    }
}