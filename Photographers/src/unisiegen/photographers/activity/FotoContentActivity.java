package unisiegen.photographers.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

import java.util.ArrayList;

import unisiegen.photographers.database.DB;
import unisiegen.photographers.helper.FilmIconFactory;
import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;

public class FotoContentActivity extends PhotographersNotebookActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidi);
    }

    public void onResume() {

        super.onResume();

        String filmID = "";
        filmID = getIntent().getExtras().getString("ID");

        int selectedItem = 0;
        selectedItem = getIntent().getExtras().getInt("selectedItem");

        Film film = new Film();

        if (filmID.length() > 0) {
            film = DB.getDB().getFilm(getApplicationContext(), filmID); }
        else { finish(); }

        Bitmap b = new FilmIconFactory().createBitmap(film);
        Drawable drawable = new BitmapDrawable(getResources(), b);
        getActionBar().setIcon(drawable);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyPagerAdapter adapter = new MyPagerAdapter(getApplicationContext(), film);
        viewPager.setAdapter(adapter);
        viewPager.getAdapter().setPrimaryItem(viewPager, 2, null);

        TitlePageIndicator mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        mIndicator.setFooterColor(0xFF000000);

        viewPager.setCurrentItem(selectedItem);

    }

    /*
         * Pageadapter f�r das hin- und herwischen zwischen den Bildenr. W�hlt
         * man ein Bild aus, wird ein "Popup" ge�ffnet in der alle Informationen
         * zu dem Bild vorhanden sind in dieser Ansicht l�sst sich dann auch
         * zwischen den Bildern hin- und herwechseln. Es wird einfach eine
         * ArrayList<Views> gef�llt. Quasi fertige Views in eine Liste, die beim
         * Wischen durchgegangen wird.
         */
    private class MyPagerAdapter extends PagerAdapter implements TitleProvider {

        private LayoutInflater inflater;
        private Film film;

        public MyPagerAdapter(Context context, Film film) {
            inflater = getLayoutInflater();
            this.film = film;
        }

        private View createView(Bild bild) {

            View v = inflater.inflate(R.layout.filminfobox, null, false);

            final TextView zeitStempel = (TextView) v
                    .findViewById(R.id.zeitStempel);
            zeitStempel.setText(bild.Zeitstempel);
            final TextView zeitGeo = (TextView) v.findViewById(R.id.geoTag);
            zeitGeo.setText(bild.GeoTag);
            final TextView objektiv = (TextView) v
                    .findViewById(R.id.showobjektiv);
            objektiv.setText(bild.Objektiv);
            final TextView filtervf = (TextView) v
                    .findViewById(R.id.showfiltervf);
            filtervf.setText(bild.FilterVF);
            final TextView picfocus = (TextView) v
                    .findViewById(R.id.showfokus);
            picfocus.setText(bild.Fokus);
            final TextView picblende = (TextView) v
                    .findViewById(R.id.showblende);
            picblende.setText(bild.Blende);
            final TextView piczeit = (TextView) v
                    .findViewById(R.id.showzeit);
            piczeit.setText(bild.Zeit);
            final TextView picmessung = (TextView) v
                    .findViewById(R.id.showmessung);
            picmessung.setText(bild.Messmethode);
            final TextView picplus = (TextView) v
                    .findViewById(R.id.showbelichtung);
            picplus.setText(bild.Belichtungskorrektur);
            final TextView picmakro = (TextView) v
                    .findViewById(R.id.showmakro);
            picmakro.setText(bild.Makro);
            final TextView picmakrovf = (TextView) v
                    .findViewById(R.id.showmakrovf);
            picmakrovf.setText(bild.MakroVF);
            final TextView picfilter = (TextView) v
                    .findViewById(R.id.showfilter);
            picfilter.setText(bild.Filter);
            final TextView picblitz = (TextView) v
                    .findViewById(R.id.showblitz);
            picblitz.setText(bild.Blitz);
            final TextView picblitzkorr = (TextView) v
                    .findViewById(R.id.showblitzkorr);
            picblitzkorr.setText(bild.Blitzkorrektur);
            final TextView picnotiz = (TextView) v
                    .findViewById(R.id.shownotiz);
            picnotiz.setText(bild.Notiz);
            final TextView picnotizcam = (TextView) v
                    .findViewById(R.id.shownotizkam);
            picnotizcam.setText(bild.KameraNotiz);
//            final TextView picTitle = (TextView) v
//                    .findViewById(R.id.pictitle);
//            picTitle.setText(bild.Bildnummer);

            return v;
        }

        @Override
        public void destroyItem(View view, int arg1, Object object) {
            // Es werden immer nur die 2 nächsten und die 2 letzten Views
            // "gespeichert" bzw. berechnet, der Rest wird erstmal gelöscht.

            ((ViewPager) view).removeView((LinearLayout) object);
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return film.Bilder.size(); // Wieviele Views zum Wischen
        }

        @Override
        public Object instantiateItem(View view, int position) {
            // Das vorpuffern, wenn die View bald drankommt... s.o.
            Bild bild = film.Bilder.get(position);
            View myView = createView(bild);
            ((ViewPager) view).addView(myView);
            return myView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public String getTitle(int position) { // Kommt vom TitleProvider um den
            // Titel einer View festzulegen
            return film.Bilder.get(position).Bildnummer;
        }

    }


}
