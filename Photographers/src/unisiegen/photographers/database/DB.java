package unisiegen.photographers.database;

import java.util.ArrayList;
import java.util.List;

import unisiegen.photographers.export.Allgemein;
import unisiegen.photographers.export.BildObjekt;
import unisiegen.photographers.export.Film;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class is able to create new databases for Settings and Films. 
 * Furthermore, existing Databases can be reset to their default values,
 * which is a set of useful values for your gear and an empty film 
 * library. 
 */
public class DB {
	
	private static DB instance;
	
	public final static String MY_DB_NUMMER = "Nummern";
	public static final String MY_DB_FILM = "Filme";
	
	public final static String MY_DB_TABLE_NUMMER = "Nummer";
	public final static String MY_DB_FILM_TABLE = "Film";
	

	public final static String MY_DB_SET = "Foto";
	public final static String MY_DB_SET1 = "FotoSettingsOne";
	public final static String MY_DB_SET2 = "FotoSettingsTwo";
	public final static String MY_DB_SET3 = "FotoSettingsThree";
	
	public final static String MY_DB_TABLE_SETCAM = "SettingsCamera";
	public final static String MY_DB_TABLE_SETCAMBW = "SettingsCameraBW";
	public final static String MY_DB_TABLE_SETFF = "SettingsFilmFormat";
	public final static String MY_DB_TABLE_SETEMP = "SettingsFilmEmpf";
	public final static String MY_DB_TABLE_SETBW = "SettingsBrennweite";
	public final static String MY_DB_TABLE_SETNM = "SettingsNahzubehor";
	public final static String MY_DB_TABLE_SETFIL = "SettingsFilter";
	public final static String MY_DB_TABLE_SETBLI = "SettingsBlitz";
	public final static String MY_DB_TABLE_SETSON = "SettingsSonder";
	public final static String MY_DB_TABLE_SETFOK = "SettingsFokus";
	public final static String MY_DB_TABLE_SETBLE = "SettingsBlende";
	public final static String MY_DB_TABLE_SETZEI = "SettingsZeit";
	public final static String MY_DB_TABLE_SETMES = "SettingsMessung";
	public final static String MY_DB_TABLE_SETPLU = "SettingsPlusMinus";
	public final static String MY_DB_TABLE_SETMAK = "SettingsMakro";
	public final static String MY_DB_TABLE_SETMVF = "SettingsMakroVF";
	public final static String MY_DB_TABLE_SETFVF = "SettingsFilterVF";
	public final static String MY_DB_TABLE_SETMVF2 = "SettingsMakroVF2";
	public final static String MY_DB_TABLE_SETFVF2 = "SettingsFilterVF2";
	public final static String MY_DB_TABLE_SETKOR = "SettingsBlitzKorr";
	public final static String MY_DB_TABLE_SETTYP = "SettingsFilmTyp";
	
	
	private DB() {}
	
	public static DB getDB(){
		if(instance == null){
			instance = new DB();
		}
		return instance;
	}
	
	
	public void createOrRebuildSettingsTable(Context context) throws Exception {

		Log.v("DatabaseCreator", "rebuildSettings() was called...");
		
		SQLiteDatabase myDBSet = null;		
		
		myDBSet	= context.openOrCreateDatabase(MY_DB_SET,
				Context.MODE_PRIVATE, null);
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETCAMBW
				+ " (_id integer primary key autoincrement, cam varchar(100), bw varchar(100))"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETCAM
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETEMP
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETBW
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETNM
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFIL
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETBLI
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETSON
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETTYP
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");

		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFOK
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETBLE
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETZEI
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMES
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETPLU
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMAK
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMVF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFVF
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETKOR
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETMVF2
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");
		myDBSet.execSQL("CREATE TABLE IF NOT EXISTS "
				+ MY_DB_TABLE_SETFVF2
				+ " (_id integer primary key autoincrement, name varchar(100), value integer, def integer)"
				+ ";");

		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETTYP);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETCAM);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFIL);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETEMP);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETNM);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETSON);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBLI);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBW);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFF);

		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFOK);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETBLE);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETZEI);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMES);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETPLU);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMAK);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMVF);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFVF);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETMVF2);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETFVF2);
		myDBSet.execSQL("DELETE FROM " + MY_DB_TABLE_SETKOR);

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
				+ " Values (" + null + ",'" + "24x36" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
				+ " Values (" + null + ",'" + "4,5x6" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
				+ " Values (" + null + ",'" + "6x6" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
				+ " Values (" + null + ",'" + "6x7" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF
				+ " Values (" + null + ",'" + "6x9" + "','" + 1 + "','"
				+ 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 25/15\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 40/17\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 50/18\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 64/19\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 100/21\u00b0" + "','" + 1
				+ "','" + 1 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 125/22\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 160/23\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 200/24\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 320/26\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 400/27\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 640/29\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 800/30\u00b0" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 1000/31\u00b0" + "','"
				+ 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 1600/33\u00b0" + "','"
				+ 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP
				+ " Values (" + null + ",'" + "ISO 3200/36\u00b0" + "','"
				+ 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
				+ " Values (" + null + ",'" + "Farbnegativ (I: CN)"
				+ "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
				+ " Values (" + null + ",'" + "Farbdia (I: CR)" + "','"
				+ 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
				+ " Values (" + null + ",'"
				+ "Schwarzwei\u00DF-Negativ (I: SW)" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
				+ " Values (" + null + ",'"
				+ "Schwarzwei\u00DF-Dia (I: SWR)" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP
				+ " Values (" + null + ",'"
				+ "Farbdia/Kunstlicht (I: CT)" + "','" + 1 + "','" + 0
				+ "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "Keiner" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "NL +1" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "NL +2" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "NL +3" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "NL +4" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "NL +5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "ZR 10" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "ZR 20" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "ZR 30" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM
				+ " Values (" + null + ",'" + "Balgen" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Keiner" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Gelb" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Orange" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Rot" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Gelbgr\u00FCn" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Gr\u00FCn" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Blau" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "KR" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "KB" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "UV" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Pol" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "ND x2" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "ND x4" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "ND x6" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL
				+ " Values (" + null + ",'" + "Soft" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Kein Blitz" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "TTL" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Auto" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Multiblitz 2" + "','"
				+ 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Multiblitz 3+" + "','"
				+ 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Manuell 1/1" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Manuell 1/2" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Manuell 1/4" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Manuell 1/8" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI
				+ " Values (" + null + ",'" + "Manuell 1/16" + "','"
				+ 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Normal" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Push 1" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Push 2" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Push 3" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Pull 1" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Pull 2" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Cross" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON
				+ " Values (" + null + ",'" + "Lowcolor" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "Auto" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "Unendlich" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "20 m" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "10 m" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "8 m" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "5 m" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "4 m" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "3 m" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "2 m" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "1,5 m" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "1 m" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "0,8 m" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "0,7 m" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "0,5 m" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK
				+ " Values (" + null + ",'" + "0,3 m" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "Auto" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "1,0" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "1,4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "2,0" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "2,8" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "5,6" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "8" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "11" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "16" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "22" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "32" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "45" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE
				+ " Values (" + null + ",'" + "64" + "','" + 1 + "','"
				+ 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "Auto" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/8000" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/4000" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/2000" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/1000" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/500" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/250" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/125" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/60" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/30" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/15" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/8" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1/2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "1s" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "2s" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "4s" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "8s" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI
				+ " Values (" + null + ",'" + "15s" + "','" + 1 + "','"
				+ 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
				+ " Values (" + null + ",'" + "Licht" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
				+ " Values (" + null + ",'" + "Mehrfeld" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
				+ " Values (" + null + ",'" + "Integral" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
				+ " Values (" + null + ",'" + "Spot" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES
				+ " Values (" + null + ",'" + "Multispot" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+5" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+2,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+1,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "+0,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "0" + "','" + 1 + "','"
				+ 1 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-0,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-1" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-1,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-2,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-3" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU
				+ " Values (" + null + ",'" + "-5" + "','" + 1 + "','"
				+ 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "+2,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "+1,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "+0,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "0" + "','" + 1 + "','"
				+ 1 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "-0,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "-1" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "-1,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "-2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "-2,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR
				+ " Values (" + null + ",'" + "-3" + "','" + 1 + "','"
				+ 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "Keiner" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x16" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x12" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x8" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x6" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x3" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF
				+ " Values (" + null + ",'" + "x1.5" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "Keiner" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+3,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+2,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+1,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2
				+ " Values (" + null + ",'" + "+0,5" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "Keiner" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x16" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x12" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x8" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x6" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x3" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF
				+ " Values (" + null + ",'" + "x1.5" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "Keiner" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+4" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+3,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+3" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+2,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+2" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+1,5" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+1" + "','" + 1 + "','"
				+ 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2
				+ " Values (" + null + ",'" + "+0,5" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.close();

	}

	public List<Film> getFilme(Context context) {
		
		List<Film> filme = new ArrayList<Film>();
		
		SQLiteDatabase myDBNummer = context.openOrCreateDatabase(MY_DB_NUMMER, Context.MODE_PRIVATE, null);
		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM, Context.MODE_PRIVATE, null);
		
		Cursor c = myDBNummer.rawQuery("SELECT title,camera,datum,bilder,pic FROM " + MY_DB_TABLE_NUMMER, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {					
					Film film = new Film();
					filme.add(film);
					ArrayList<BildObjekt> bilder = new ArrayList<BildObjekt>();					
					film.Allgemein = new Allgemein();
					film.Bilder = bilder;
					
					film.Allgemein.Titel = c.getString(c.getColumnIndex("title"));
					film.Allgemein.Kamera = c.getString(c.getColumnIndex("camera")); 
					film.Allgemein.setIcon(c.getString(c.getColumnIndex("pic")));
					
					// TODO: Restliche Filmdaten aus der ersten Zeile der Bilder Tabelle holen // WTFFFFFFFF???
					Cursor c1 = myDBFilm
					.rawQuery(
							"SELECT _id,filmtitle,filmnotiz,picuhrzeit,picnummer, picobjektiv, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
									+ DB.MY_DB_FILM_TABLE
									+ " WHERE filmtitle = '"
									+ film.Allgemein.Titel + "'", null);
					
					if (c1 != null) {
						if (c1.moveToFirst()) {
							film.Allgemein.Filmnotiz = c1.getString(c1.getColumnIndex("filmnotiz"));
							film.Allgemein.Filmformat = c1.getString(c1.getColumnIndex("filmformat"));
							film.Allgemein.Filmtyp = c1.getString(c1.getColumnIndex("filmtyp"));
							film.Allgemein.Empfindlichkeit = c1.getString(c1.getColumnIndex("filmempfindlichkeit"));
							film.Allgemein.Sonderentwicklung1 = c1.getString(c1.getColumnIndex("filmsonder"));
							film.Allgemein.Sonderentwicklung2 = c1.getString(c1.getColumnIndex("filmsonders"));							
						}
					}					
					c1.close();
					
					// Bilder holen					
					Cursor c2 = myDBFilm
							.rawQuery(
									"SELECT _id,picfokus,picuhrzeit,piclat,piclong,filmdatum,picobjektiv, picblende,piczeit,picmessung, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM "
											+ DB.MY_DB_FILM_TABLE
											+ " WHERE filmtitle = '"
											+ film.Allgemein.Titel + "'", null);
					if (c2 != null) {
						if (c2.moveToFirst()) {
							do {
								bilder.add(new BildObjekt(
										c2.getString(c2.getColumnIndex("picnummer")),
										c2.getString(c2.getColumnIndex("picobjektiv")),
										c2.getString(c2.getColumnIndex("picblende")),
										c2.getString(c2.getColumnIndex("piczeit")),
										c2.getString(c2.getColumnIndex("picfokus")),
										c2.getString(c2.getColumnIndex("picfilter")),
										c2.getString(c2.getColumnIndex("picmakro")),
										c2.getString(c2.getColumnIndex("picfiltervf")),
										c2.getString(c2.getColumnIndex("picmakrovf")),
										c2.getString(c2.getColumnIndex("picmessung")),
										c2.getString(c2.getColumnIndex("pickorr")),
										c2.getString(c2.getColumnIndex("picblitz")),
										c2.getString(c2.getColumnIndex("picblitzkorr")), 
										c2.getString(c2.getColumnIndex("picuhrzeit")) + " - " + c2.getString(c2.getColumnIndex("filmdatum")),
										"Lat : " + c2.getString(c2.getColumnIndex("piclat")) + " - Long : " + c2.getString(c2.getColumnIndex("piclong")),
										c2.getString(c2.getColumnIndex("picnotiz")),
										c2.getString(c2.getColumnIndex("pickameranotiz"))));
							} while (c2.moveToNext());
						}
					}
					c2.close();					
					
				} while (c.moveToNext());
			}
		}
		c.close();
		myDBNummer.close();
		myDBFilm.close();
		
		return filme;
	}
	
}
