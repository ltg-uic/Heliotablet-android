package ltg.heliotablet_android.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ReasonDBOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "reasons.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_REASON = "reason";
	public static final String TABLE_REASON_IMAGE = "reasonImage";

	public static final String COLUMN_ID = "_id";

	// REASON Table
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_LAST_TIMESTAMP = "lastTimestamp";
	public static final String COLUMN_ORIGIN = "origin";
	public static final String COLUMN_REASON_TEXT = "reasonText";
	public static final String COLUMN_ANCHOR = "anchor";

	// REASON_IMAGE table
	public static final String COLUMN_IMAGE_RAW = "imageRaw";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_REASON_ID = "reasonId";

	public static final String CREATE_TABLE_REASON = "CREATE TABLE "
			+ TABLE_REASON + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_REASON_TEXT
			+ "  TEXT NOT NULL, " + COLUMN_LAST_TIMESTAMP
			+ " TIMESTAMP DEFAULT NULL, " + COLUMN_ORIGIN + "  TEXT NOT NULL, "
			+ COLUMN_TYPE + "  TEXT NOT NULL, " + COLUMN_ANCHOR + "  TEXT NOT NULL );";
	public static final String CREATE_TABLE_REASON_IMAGE = "CREATE TABLE "
			+ TABLE_REASON_IMAGE + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_IMAGE_RAW
			+ "  BLOB, " + COLUMN_REASON_ID + "  TEXT NOT NULL, "
			+ " FOREIGN KEY ('+COLUMN_REASON_ID+') REFERENCES " + TABLE_REASON
			+ " ('+COLUMN_ID+'));";

	public ReasonDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public ReasonDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys=ON;");
		db.execSQL(CREATE_TABLE_REASON);
		db.execSQL(CREATE_TABLE_REASON_IMAGE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ReasonDBOpenHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_IMAGE);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_IMAGE);
		onCreate(db);
	}

}
