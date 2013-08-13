/* Copyright (C) 2012 Nico Castelli, Christopher Maiworm 
 * Copyright (C) 2012 Sebastian Draxler, Alexander Boden, Christian Woehrl (Committers)
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

package unisiegen.photographers.database;

import java.util.ArrayList;
import java.util.List;

import unisiegen.photographers.model.Bild;
import unisiegen.photographers.model.Film;
import unisiegen.photographers.model.Setting;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class is able to create new databases for Settings and Films.
 * Furthermore, existing Databases can be reset to their default values, which
 * is a set of useful values for your gear and an empty film library.
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

	private DB() {
	}

	public static DB getDB() {
		if (instance == null) {
			instance = new DB();
		}
		return instance;
	}

	public void createOrRebuildSettingsTable(Context context) throws Exception {

		Log.v("DatabaseCreator", "rebuildSettings() was called...");

		SQLiteDatabase myDBSet = context.openOrCreateDatabase(MY_DB_SET,
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

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF + " Values (" + null
				+ ",'" + "24x36" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF + " Values (" + null
				+ ",'" + "4,5x6" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF + " Values (" + null
				+ ",'" + "6x6" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF + " Values (" + null
				+ ",'" + "6x7" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFF + " Values (" + null
				+ ",'" + "6x9" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 25/15\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 40/17\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 50/18\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 64/19\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 100/21\u00b0" + "','" + 1 + "','" + 1
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 125/22\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 160/23\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 200/24\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 320/26\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 400/27\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 640/29\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 800/30\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 1000/31\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 1600/33\u00b0" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETEMP + " Values ("
				+ null + ",'" + "ISO 3200/36\u00b0" + "','" + 1 + "','" + 0
				+ "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP + " Values ("
				+ null + ",'" + "Farbnegativ (I: CN)" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP + " Values ("
				+ null + ",'" + "Farbdia (I: CR)" + "','" + 1 + "','" + 0
				+ "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP + " Values ("
				+ null + ",'" + "Schwarzwei\u00DF-Negativ (I: SW)" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP + " Values ("
				+ null + ",'" + "Schwarzwei\u00DF-Dia (I: SWR)" + "','" + 1
				+ "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETTYP + " Values ("
				+ null + ",'" + "Farbdia/Kunstlicht (I: CT)" + "','" + 1
				+ "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "Keiner" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "NL +1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "NL +2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "NL +3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "NL +4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "NL +5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "ZR 10" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "ZR 20" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "ZR 30" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETNM + " Values (" + null
				+ ",'" + "Balgen" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Keiner" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Gelb" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Orange" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Rot" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Gelbgr\u00FCn" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Gr\u00FCn" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Blau" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "KR" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "KB" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "UV" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Pol" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "ND x2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "ND x4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "ND x6" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFIL + " Values ("
				+ null + ",'" + "Soft" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Kein Blitz" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "TTL" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Auto" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Multiblitz 2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Multiblitz 3+" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Manuell 1/1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Manuell 1/2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Manuell 1/4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Manuell 1/8" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLI + " Values ("
				+ null + ",'" + "Manuell 1/16" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Normal" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Push 1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Push 2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Push 3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Pull 1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Pull 2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Cross" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETSON + " Values ("
				+ null + ",'" + "Lowcolor" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "Auto" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "Unendlich" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "20 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "10 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "8 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "5 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "4 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "3 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "2 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "1,5 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "1 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "0,8 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "0,7 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "0,5 m" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFOK + " Values ("
				+ null + ",'" + "0,3 m" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "Auto" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "1,0" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "1,4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "2,0" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "2,8" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "5,6" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "8" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "11" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "16" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "22" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "32" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "45" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETBLE + " Values ("
				+ null + ",'" + "64" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "Auto" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/8000" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/4000" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/2000" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/1000" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/500" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/250" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/125" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/60" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/30" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/15" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/8" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1/2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "1s" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "2s" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "4s" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "8s" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETZEI + " Values ("
				+ null + ",'" + "15s" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES + " Values ("
				+ null + ",'" + "Licht" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES + " Values ("
				+ null + ",'" + "Mehrfeld" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES + " Values ("
				+ null + ",'" + "Integral" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES + " Values ("
				+ null + ",'" + "Spot" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMES + " Values ("
				+ null + ",'" + "Multispot" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+2,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+1,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "+0,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "0" + "','" + 1 + "','" + 1 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-0,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-1,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-2,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETPLU + " Values ("
				+ null + ",'" + "-5" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "+3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "+2,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "+2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "+1,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "+1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "+0,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "0" + "','" + 1 + "','" + 1 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "-0,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "-1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "-1,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "-2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "-2,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETKOR + " Values ("
				+ null + ",'" + "-3" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "Keiner" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x16" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x12" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x8" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x6" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF + " Values ("
				+ null + ",'" + "x1.5" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "Keiner" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+3,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+2,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+1,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETMVF2 + " Values ("
				+ null + ",'" + "+0,5" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "Keiner" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x16" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x12" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x8" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x6" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF + " Values ("
				+ null + ",'" + "x1.5" + "','" + 1 + "','" + 0 + "');");

		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "Keiner" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+4" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+3,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+3" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+2,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+2" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+1,5" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+1" + "','" + 1 + "','" + 0 + "');");
		myDBSet.execSQL("INSERT INTO " + MY_DB_TABLE_SETFVF2 + " Values ("
				+ null + ",'" + "+0,5" + "','" + 1 + "','" + 0 + "');");

		myDBSet.close();
	}

	public void createOrRebuildNummernTable(Context mContext) {

		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);
		myDBNummer
				.execSQL("CREATE TABLE IF NOT EXISTS "
						+ DB.MY_DB_TABLE_NUMMER
						+ " (title varchar(100) primary key, value integer,camera varchar(100), datum varchar(100), bilder integer, pic varchar(999))"
						+ ";");
		myDBNummer.close();
	}

	public void createOrRebuildFilmTable(Context mContext) {
		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		myDBFilm.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DB.MY_DB_FILM_TABLE
				+ " (_id integer primary key autoincrement, filmdatum varchar(100), picuhrzeit varchar(100), filmtitle varchar(100), filmcamera varchar(100), filmformat varchar(100), filmempfindlichkeit varchar(100), filmtyp varchar(100), filmsonder varchar(100), filmsonders varchar(100), picfokus varchar(100), picblende varchar(100), piczeit varchar(100), picmessung varchar(100), pickorr varchar(100), picmakro varchar(100), picmakrovf varchar(100), picfilter varchar(100), picfiltervf varchar(100), picblitz varchar(100), picblitzkorr varchar(100), picnotiz varchar(100), pickameranotiz varchar(100), picobjektiv varchar(100),piclong varchar(100),piclat varchar(100),filmnotiz varchar(100), picnummer varchar(100))"
				+ ";");
		myDBFilm.close();
	}

	public ArrayList<Film> getFilme(Context context) {

		ArrayList<Film> filme = new ArrayList<Film>();

		try {
			SQLiteDatabase myDBNummer = context.openOrCreateDatabase(
					MY_DB_NUMMER, Context.MODE_PRIVATE, null);
			SQLiteDatabase myDBFilm = context.openOrCreateDatabase(
					DB.MY_DB_FILM, Context.MODE_PRIVATE, null);

			Cursor c = myDBNummer.rawQuery(
					"SELECT title,camera,datum,bilder,pic FROM "
							+ MY_DB_TABLE_NUMMER, null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						Film film = new Film();
						filme.add(film);

						film.Titel = c.getString(c.getColumnIndex("title"));
						film.Kamera = c.getString(c.getColumnIndex("camera"));
						film.Datum = c.getString(c.getColumnIndex("datum"));
						film.Pics = c.getString(c.getColumnIndex("bilder"));
						film.setIcon(c.getString(c.getColumnIndex("pic")));

						Cursor c1 = myDBFilm
								.rawQuery(
										"SELECT _id,filmtitle,filmnotiz,picuhrzeit,picnummer, picobjektiv, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
												+ DB.MY_DB_FILM_TABLE
												+ " WHERE filmtitle = '"
												+ film.Titel + "'", null);

						if (c1 != null) {
							if (c1.moveToFirst()) {
								film.Filmnotiz = c1.getString(c1
										.getColumnIndex("filmnotiz"));
								film.Filmformat = c1.getString(c1
										.getColumnIndex("filmformat"));
								film.Filmtyp = c1.getString(c1
										.getColumnIndex("filmtyp"));
								film.Empfindlichkeit = c1.getString(c1
										.getColumnIndex("filmempfindlichkeit"));
								film.Sonderentwicklung1 = c1.getString(c1
										.getColumnIndex("filmsonder"));
								film.Sonderentwicklung2 = c1.getString(c1
										.getColumnIndex("filmsonders"));
							}
						}
						c1.close();

						// Bilder holen
						film.Bilder = this.getBilder(context, film.Titel, null);

					} while (c.moveToNext());
				}
			}
			c.close();
			myDBNummer.close();
			myDBFilm.close();
		} catch (Exception e) {

		}
		return filme;
	}

	public void deleteFilms(Context context, String[] filmTitel) {

		SQLiteDatabase myDBNummer = context.openOrCreateDatabase(MY_DB_NUMMER,
				Context.MODE_PRIVATE, null);
		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		myDBNummer.delete(DB.MY_DB_TABLE_NUMMER, "title=?", filmTitel);
		myDBFilm.delete(DB.MY_DB_FILM_TABLE, "filmtitle=?", filmTitel);

		myDBNummer.close();
		myDBFilm.close();
	}

	public Film getFilm(Context context, String title) {

		Film film = new Film();

		SQLiteDatabase myDBNummer = context.openOrCreateDatabase(MY_DB_NUMMER,
				Context.MODE_PRIVATE, null);
		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		Cursor c = myDBNummer.rawQuery(
				"SELECT title,camera,datum,bilder, pic FROM "
						+ DB.MY_DB_TABLE_NUMMER + " WHERE title = '" + title
						+ "'", null);

		if (c != null) {
			if (c.moveToFirst()) {

				film.Titel = c.getString(c.getColumnIndex("title"));
				film.Kamera = c.getString(c.getColumnIndex("camera"));
				film.Datum = c.getString(c.getColumnIndex("datum"));
				film.setIcon(c.getString(c.getColumnIndex("pic")));

				// TODO: Restliche Filmdaten aus der ersten Zeile der Bilder
				// Tabelle holen // WTFFFFFFFF???
				Cursor c1 = myDBFilm
						.rawQuery(
								"SELECT _id,filmtitle,filmnotiz,picuhrzeit,picnummer, picobjektiv, filmformat, filmtyp, filmempfindlichkeit, filmsonder, filmsonders FROM "
										+ DB.MY_DB_FILM_TABLE
										+ " WHERE filmtitle = '"
										+ film.Titel
										+ "'", null); 

				if (c1 != null) {
					if (c1.moveToFirst()) {
						film.Filmnotiz = c1.getString(c1
								.getColumnIndex("filmnotiz"));
						film.Filmformat = c1.getString(c1
								.getColumnIndex("filmformat"));
						film.Filmtyp = c1.getString(c1
								.getColumnIndex("filmtyp"));
						film.Empfindlichkeit = c1.getString(c1
								.getColumnIndex("filmempfindlichkeit"));
						film.Sonderentwicklung1 = c1.getString(c1
								.getColumnIndex("filmsonder"));
						film.Sonderentwicklung2 = c1.getString(c1
								.getColumnIndex("filmsonders"));
					}
				}
				c1.close();

				film.Bilder = this.getBilder(context, title, null);
			}
		}
		c.close();

		myDBFilm.close();
		myDBNummer.close();

		return film;
	}

	public ArrayList<Bild> getBild(Context context, String filmTitle,
			String bildNumemr) {

		ArrayList<Bild> bilder = getBilder(context, filmTitle, bildNumemr);
		return bilder;
	}

	private ArrayList<Bild> getBilder(Context context, String title,
			String bildNummer) {

		SQLiteDatabase myDBFilm = context.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT _id,picfokus,picuhrzeit,piclat,piclong,filmdatum,picobjektiv, picblende,piczeit,picmessung, picnummer, pickorr,picmakro,picmakrovf,picfilter,picfiltervf,picblitz,picblitzkorr,picnotiz,pickameranotiz FROM ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" WHERE filmtitle = '");
		sql.append(title);
		if (bildNummer == null) {
			sql.append("' AND picnummer != 'Bild 0';"); // Ignore the dummy pic "Bild 0" 
		} else {
			sql.append("' AND picnummer = '");
			sql.append(bildNummer);
			sql.append("';");
		}

		Cursor c2 = myDBFilm.rawQuery(new String(sql), null);

		ArrayList<Bild> bilder = new ArrayList<Bild>();
		if (c2 != null) {
			if (c2.moveToFirst()) {
				do {
					bilder.add(new Bild(
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
							c2.getString(c2.getColumnIndex("picuhrzeit"))
									+ " - "
									+ c2.getString(c2
											.getColumnIndex("filmdatum")),
							"Lat : "
									+ c2.getString(c2.getColumnIndex("piclat"))
									+ " - Long : "
									+ c2.getString(c2.getColumnIndex("piclong")),
							c2.getString(c2.getColumnIndex("picnotiz")), c2
									.getString(c2
											.getColumnIndex("pickameranotiz"))));
				} while (c2.moveToNext());
			}
		}
		c2.close();
		myDBFilm.close();

		return bilder;
	}
	
	public boolean deleteSetting(Context mContext, String database, String settingType, String value){
		
		SQLiteDatabase db = mContext.openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
		
		try {
			db.execSQL("DELETE FROM " + settingType + " WHERE name = '" + value + "'");
			db.close();
					
		} catch (Exception e) {
			db.close();
			
			return false;
		}
		
		return true;
	}
	
	public boolean saveSetting(Context mContext, String database, String settingType, String name, int value){
		
		SQLiteDatabase db = mContext.openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
		db = mContext.openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
		db.execSQL("INSERT INTO " + settingType + " Values (" + null + ",'"
				+ "" + name + "" + "','" + value + "','" + 0 + "');");
		db.close();
		
		return true;
	}

	/**
	 * This will retrieve a List of ALL settings, with additional data for
	 * each particular value, if it is enabled and should be shown in the UI.
	 * This is used by the configuration dialog (EditSettings).
	 * 
	 * @param mContext
	 * @param database
	 * @param settingName
	 *            Use on of the constants in this file. Each Constant represents
	 *            a table (for a certain type of settings) in the db.
	 * @return
	 */
	public ArrayList<Setting> getAllSettings(Context mContext, String database, String settingName) {

		ArrayList<Setting> values = new ArrayList<Setting>();
		
		SQLiteDatabase db = mContext.openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT name, value FROM " + settingName, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					values.add(new Setting(settingName, c.getString(c.getColumnIndex("name")), c.getInt(c.getColumnIndex("value"))));
					
				} while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		
		return values;
	}
	
	public boolean setDefaultVal(Context mContext, String database, String settingName, String newDefault){
		
		SQLiteDatabase db = mContext.openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
		
		db.execSQL("UPDATE " + settingName + " SET def = '" + 0 + "'");
		db.execSQL("UPDATE " + settingName + " SET def = '" + 1 + "' WHERE name = '" + newDefault + "'");
		db.close();
		
		return true;
	}

	/**
	 * 
	 * @return Returns a list of items, usable for various portions of the UI
	 */
	public ArrayList<String> getActivatedSettingsData(Context mContext, String database, String settingName) {

		ArrayList<String> values = new ArrayList<String>();

		SQLiteDatabase myDB = mContext.openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT name, value FROM ");
		sql.append(settingName);
		sql.append(" WHERE value = '1'");
		Cursor c = myDB.rawQuery(new String(sql), null);

		if (c != null) {
			if (c.moveToFirst()) {
				do {
					values.add(c.getString(c.getColumnIndex("name")));

				} while (c.moveToNext());
			}
		}
		c.close();
		myDB.close();

		return values;
	}

	public int getDefaultSettingNumber(Context mContext, String database,
			String settingName) {

		SQLiteDatabase myDB = mContext.openOrCreateDatabase(database,
				Context.MODE_PRIVATE, null);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT name, value, def FROM ");
		sql.append(settingName);
		sql.append(" WHERE value = '1'");

		Cursor c = myDB.rawQuery(new String(sql), null);
		int count = 0;
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					if (c.getInt(c.getColumnIndex("def")) == 1) {
						c.close();
						myDB.close();

						return count;
					}
					count++;

				} while (c.moveToNext());
			}
		}

		c.close();
		myDB.close();

		return 0;
	}


	public ArrayList<String> getLensesForCamera(Context mContext,
			String dbName, String camera) {

		ArrayList<String> values = new ArrayList<String>();

		SQLiteDatabase db = mContext.openOrCreateDatabase(dbName,
				Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cam, bw FROM ");
		sql.append(DB.MY_DB_TABLE_SETCAMBW);
		sql.append(" WHERE cam = '");
		sql.append(camera);
		sql.append("'");

		Cursor c = db.rawQuery(new String(sql), null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					values.add(c.getString(c.getColumnIndex("bw")));

				} while (c.moveToNext());
			}
		}
		c.close();
		db.close();

		return values;
	}

	public void updatePicture(Context mContext, Film film, Bild bild) {

		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" SET picfokus = '");
		sql.append(bild.Fokus);
		sql.append("', picblende = '");
		sql.append(bild.Blende);
		sql.append("', piczeit = '");
		sql.append(bild.Zeit);
		sql.append("', picmessung = '");
		sql.append(bild.Messmethode);
		sql.append("', pickorr = '");
		sql.append(bild.Belichtungskorrektur);
		sql.append("', picmakro = '");
		sql.append(bild.Makro);
		sql.append("', picmakrovf = '");
		sql.append(bild.MakroVF);
		sql.append("', picfilter = '");
		sql.append(bild.Filter);
		sql.append("', picfiltervf = '");
		sql.append(bild.FilterVF);
		sql.append("', picblitz = '");
		sql.append(bild.Blitz);
		sql.append("', picblitzkorr = '");
		sql.append(bild.Blitzkorrektur);
		sql.append("', picnotiz = '");
		sql.append(bild.Notiz);
		sql.append("', pickameranotiz = '");
		sql.append(bild.KameraNotiz);
		sql.append("', picobjektiv = '");
		sql.append(bild.Objektiv);
		sql.append("' WHERE filmtitle = '");
		sql.append(film.Titel);
		sql.append("' AND picnummer = '");
		sql.append(bild.Bildnummer);
		sql.append("';");
		myDBFilm.execSQL(new String(sql));
		myDBFilm.close();
	}

	public void deletePicture(Context mContext, Film film, Bild bild) {

		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);
		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" WHERE filmtitle = '");
		sql.append(film.Titel);
		sql.append("' AND picnummer = '");
		sql.append(bild.Bildnummer);
		sql.append("';");

		myDBFilm.execSQL(new String(sql));
		myDBFilm.close();

		ContentValues dataToInsert = new ContentValues();
		dataToInsert.put("bilder", film.Bilder.size() - 1);
		myDBNummer.update(DB.MY_DB_TABLE_NUMMER, dataToInsert, "title=?",
				new String[] { film.Titel });

		myDBNummer.close();
	}

	private void addPicture(Context mContext, Film f, Bild b) {

		SQLiteDatabase myDBFilm = mContext.openOrCreateDatabase(DB.MY_DB_FILM,
				Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(DB.MY_DB_FILM_TABLE);
		sql.append(" Values (" + null);
		sql.append(",'");
		sql.append(f.Datum);
		sql.append("','");
		sql.append(b.Zeitstempel);
		sql.append("','");
		sql.append(f.Titel);
		sql.append("','");
		sql.append(f.Kamera);
		sql.append("','");
		sql.append(f.Filmformat);
		sql.append("','");
		sql.append(f.Empfindlichkeit);
		sql.append("','");
		sql.append(f.Filmtyp);
		sql.append("','");
		sql.append(f.Sonderentwicklung1);
		sql.append("','");
		sql.append(f.Sonderentwicklung2);
		sql.append("','");
		sql.append(b.Fokus);
		sql.append("','");
		sql.append(b.Blende);
		sql.append("','");
		sql.append(b.Zeit);
		sql.append("','");
		sql.append(b.Messmethode);
		sql.append("','");
		sql.append(b.Belichtungskorrektur);
		sql.append("','");
		sql.append(b.Makro);
		sql.append("','");
		sql.append(b.MakroVF);
		sql.append("','");
		sql.append(b.Filter);
		sql.append("','");
		sql.append(b.FilterVF);
		sql.append("','");
		sql.append(b.Blitz);
		sql.append("','");
		sql.append(b.Blitzkorrektur);
		sql.append("','");
		sql.append(b.Notiz);
		sql.append("','");
		sql.append(b.KameraNotiz);
		sql.append("','");
		sql.append(b.Objektiv);
		sql.append("','");

		String[] geotagParts = b.GeoTag.split("' , '");
		
		// lat
		sql.append(geotagParts[0]);
		sql.append("','");
		// long
		sql.append(geotagParts[1]);
		sql.append("','");

		sql.append(f.Filmnotiz);
		sql.append("','");
		sql.append(b.Bildnummer);
		sql.append("');");

		myDBFilm.execSQL(new String(sql));

		myDBFilm.close();
	}

	public void addPictureUpdateNummer(Context mContext, Film f, Bild b,
			int picturesNumber) {

		addPicture(mContext, f, b);

		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(DB.MY_DB_TABLE_NUMMER);
		sql.append(" SET bilder = '");
		sql.append(String.valueOf(picturesNumber));
		sql.append("' WHERE title = '");
		sql.append(f.Titel);
		sql.append("';");

		myDBNummer.execSQL(new String(sql));
		myDBNummer.close();
	}

	public void addPictureCreateNummer(Context mContext, Film f, Bild b,
			int picturesNumber, String encodedImage) {

		addPicture(mContext, f, b);

		SQLiteDatabase myDBNummer = mContext.openOrCreateDatabase(
				DB.MY_DB_NUMMER, Context.MODE_PRIVATE, null);

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT OR REPLACE INTO ");
		sql.append(DB.MY_DB_TABLE_NUMMER);
		sql.append(" Values ('");
		sql.append(f.Titel);
		sql.append("'," + null);
		sql.append(",'");
		sql.append(f.Kamera);
		sql.append("','");
		sql.append(f.Datum);
		sql.append("',");
		sql.append(String.valueOf(picturesNumber));
		sql.append(",'");
		sql.append(encodedImage);
		sql.append("');");

		myDBNummer.execSQL(new String(sql));
		myDBNummer.close();
	}

}