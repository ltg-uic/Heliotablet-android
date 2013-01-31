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
	
	
	public final static String CONST_MERCURY = "MERCURY";
	public final static String CONST_VENUS = "VENUS";
	public final static String CONST_EARTH = "EARTH";
	public final static String CONST_MARS = "MARS";
	public final static String CONST_SATURN = "SATURN";
	public final static String CONST_JUPITER = "JUPITER";
	public final static String CONST_NEPTUNE = "NEPTUNE";
	public final static String CONST_URANUS = "URANUS";

	public final static String CONST_RED = "RED";
	public final static String CONST_ORANGE = "ORANGE";
	public final static String CONST_BLUE = "BLUE";
	public final static String CONST_GREEN = "GREEN";
	public final static String CONST_PINK = "PINK";
	public final static String CONST_YELLOW = "YELLOW";
	public final static String CONST_GREY = "GREY";
	public final static String CONST_BROWN = "BROWN";
	
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

}
