package unisiegen.photographers.model;

public class Setting {
	
	// corresponds with the database table
	public String type;
	// value that is displays in all the dialogs
	public String name;
	// is it selected/activated to be used when creaing a film/picture
	private boolean checked = false;

	public Setting(String name, int Status) {
		this.name = name;
		if (Status == 1) {
			checked = true;
		} else
			checked = false;
	}

	public String getName() {
		return name;
	}

	public int isChecked() {
		if (checked) {
			return 1;
		} else
			return 0;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String toString() {
		if (checked) {
			return 1 + " " + name;
		} else
			return 0 + " " + name;
	}
}