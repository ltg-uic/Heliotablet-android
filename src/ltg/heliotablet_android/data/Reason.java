package ltg.heliotablet_android.data;

import java.sql.Timestamp;

public class Reason {
	
	private long id;
	
	//THEORY OR OBERVATION
	private String type;
	
	//PLANET_NAME OR COLOR that the flag is attached to
	private String anchor;
	private String flag;
	
	private Timestamp lastTimestamp;
	private String origin;
	private String reasonText;
	private boolean isReadonly;
	
	
	public final static String CONST_MERCURY = "mercury";
	public final static String CONST_VENUS = "venus";
	public final static String CONST_EARTH = "earth";
	public final static String CONST_MARS = "mars";
	public final static String CONST_SATURN = "saturn";
	public final static String CONST_JUPITER = "jupiter";
	public final static String CONST_NEPTUNE = "neptune";
	public final static String CONST_URANUS = "uranus";

	public final static String CONST_RED = "red";
	public final static String CONST_ORANGE = "orange";
	public final static String CONST_BLUE = "blue";
	public final static String CONST_GREEN = "green";
	public final static String CONST_PINK = "pink";
	public final static String CONST_YELLOW = "yellow";
	public final static String CONST_GREY = "grey";
	public final static String CONST_BROWN = "brown";
	
	public final static String TYPE_THEORY = "THEORY";
	public final static String TYPE_OBSERVATION = "OBSERVATION";
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(Timestamp lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getReasonText() {
		return reasonText;
	}

	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isReadonly() {
		return isReadonly;
	}

	public void setReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

}
