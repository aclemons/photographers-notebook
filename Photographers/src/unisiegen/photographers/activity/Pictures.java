package unisiegen.photographers.activity;

import unisiegen.photographers.export.BildObjekt;

class Pictures {
	private String name = "";
	private String time = "";
	private String timestamp = "";
	private String objektiv = "";
	private String blende = "";

	public Pictures(BildObjekt b) {
		this.name = b.Bildnummer;
		this.time = b.Zeit;
		this.blende = b.Blende;
		this.objektiv = b.Objektiv;
		this.timestamp = b.Zeitstempel;
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public String getBlende() {
		return blende;
	}

	public String getZeitstempel() {
		return timestamp;
	}
}