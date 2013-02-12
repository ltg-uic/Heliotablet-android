package ltg.heliotablet_android.data;

import java.sql.Timestamp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;

public class Reason implements Comparable<Reason>, Parcelable {

	private long id;

	// THEORY OR OBERVATION
	private String type;

	// PLANET_NAME OR COLOR that the flag is attached to
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
	public final static String CONST_GREY = "gray";
	public final static String CONST_BROWN = "brown";

	public final static String TYPE_THEORY = "THEORY";
	public final static String TYPE_OBSERVATION = "OBSERVATION";

	public Reason(String anchor, String flag, String type, String origin,
			boolean isReadOnly) {
		this.anchor = anchor;
		this.flag = flag;
		this.origin = origin;
		this.type = type;
		this.isReadonly = isReadOnly;
	}

	public Reason(Parcel parcel) {
		id = parcel.readLong();
		lastTimestamp = Timestamp.valueOf(parcel.readString());
		type = parcel.readString();
		origin = parcel.readString();
		reasonText = parcel.readString();
		anchor = parcel.readString();
		flag = parcel.readString();
		isReadonly = parcel.readInt() == 1;
	}

	public Reason() {

	}

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

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", Long.toString(this.id))
				.add("reasonText", this.reasonText).add("anchor", this.anchor)
				.add("type", this.type).add("flag", this.flag)
				.add("origin", this.origin).add("isReadonly", this.isReadonly)
				.toString();
	}

	@Override
	public int compareTo(Reason other) {
		return ComparisonChain.start().compare(this.id, other.id)
				.compare(this.reasonText, other.reasonText)
				.compare(this.anchor, other.anchor)
				.compare(this.type, other.type).compare(this.flag, other.flag)
				.compare(this.origin, other.origin)
				.compareTrueFirst(this.isReadonly, other.isReadonly).result();
	}

	public static Predicate<Reason> getIdPredicate(final Long id) {
		Predicate<Reason> idPredicate = new Predicate<Reason>() {

			@Override
			public boolean apply(Reason input) {
				if (id.equals(input.getId()))
					return true;

				return false;
			}
		};
		return idPredicate;
	}
	
	public static Predicate<Reason> getAnchorPredicate(final String anchor) {
		Predicate<Reason> anchorPredicate = new Predicate<Reason>() {

			@Override
			public boolean apply(Reason input) {
	
				if (anchor.equals(input.getAnchor()))
					return true;

				return false;
			}
		};
		return anchorPredicate;
	}
	
	public static Predicate<Reason> getFlagPredicate(final String flag) {
		Predicate<Reason> flagPredicate = new Predicate<Reason>() {

			@Override
			public boolean apply(Reason input) {
	
				if (flag.equals(input.getFlag()))
					return true;

				return false;
			}
		};
		return flagPredicate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(id);
		parcel.writeString(lastTimestamp.toString());
		parcel.writeString(type);
		parcel.writeString(origin);
		parcel.writeString(reasonText);
		parcel.writeString(anchor);
		parcel.writeString(flag);
		parcel.writeInt(isReadonly ? 1 : 0);
	}

}
