package unisiegen.photographers.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

public class DatabaseImportLogger {

	private File logFile;
	private BufferedWriter writer;
	private int linesImported = 1;
	private boolean logcatOutput;
	
	private static final String LOGCAT_TAG = "DB_IMPORT";

	public DatabaseImportLogger(File logFile, boolean logcat) {

		this.logFile = logFile;
		this.logcatOutput = logcat;
	}

	public boolean open() {

		if (writer == null) {
			try {
				writer = new BufferedWriter(new FileWriter(logFile));
			} catch (IOException e) {
			}

			if (writer != null) {
				return true;
			}
		}
		return false;
	}
	
	public void close(){
		
		if(writer != null){
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	
	public void log(String msg) {

		if(logcatOutput) Log.v(LOGCAT_TAG, String.format("%d : %s", linesImported, msg));
		try {
			writer.write(String.valueOf(linesImported++));
			writer.write(" : ");
			writer.write(msg);
			writer.write("\n");
		} catch (IOException e) {
		}		
	}

}
