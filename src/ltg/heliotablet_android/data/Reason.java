package ltg.heliotablet_android.data;

import java.sql.Timestamp;

public class Reason {
	
	private long id;
	
	//THEORY OR OBERVATION
	private String type;
	
	//PLANET_NAME OR COLOR
	private String anchor;
	private Timestamp lastTimestamp;
	private String origin;
	private String reasonText;
	
	public final static String ANCHOR_MERCURY = "MERCURY";
	public final static String ANCHOR_VENUS = "VENUS";
	public final static String ANCHOR_EARTH = "EARTH";
	public final static String ANCHOR_MARS = "MARS";
	public final static String ANCHOR_SATURN = "SATURN";
	public final static String ANCHOR_JUPITER = "JUPITER";
	public final static String ANCHOR_NEPTUNE = "NEPTUNE";
	public final static String ANCHOR_URANUS = "URANUS";

	public final static String ANCHOR_RED = "RED";
	public final static String ANCHOR_ORANGE = "ORANGE";
	public final static String ANCHOR_BLUE = "BLUE";
	public final static String ANCHOR_GREEN = "GREEN";
	public final static String ANCHOR_PINK = "PINK";
	public final static String ANCHOR_YELLOW = "YELLOW";
	public final static String ANCHOR_GREY = "GREY";
	public final static String ANCHOR_BROWN = "BROWN";
	
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

}
