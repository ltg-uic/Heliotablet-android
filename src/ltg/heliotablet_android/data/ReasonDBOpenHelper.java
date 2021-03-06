package ltg.heliotablet_android.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReasonDBOpenHelper extends SQLiteOpenHelper {

    public static final int ALL_REASONS_THEORY_LOADER_ID = 0;
    public static final int INSERT_REASON_THEORY_LOADER_ID = 1;
    public static final int UPDATE_REASON_THEORY_LOADER_ID = 2;
    public static final int DELETE_REASON_THEORY_LOADER_ID = 3;

    public static final int ALL_REASONS_OBS_LOADER_ID = 0;
    public static final int INSERT_REASON_OBS_LOADER_ID = 1;
    public static final int UPDATE_REASON_OBS_LOADER_ID = 2;
    public static final int DELETE_REASON_OBS_LOADER_ID = 3;
    public static final String TABLE_REASON = "reason";
    public static final String TABLE_REASON_IMAGE = "reasonImage";
    public static final String COLUMN_ID = "_id";
    // REASON Table
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_LAST_TIMESTAMP = "lastTimestamp";
    public static final String COLUMN_ORIGIN = "origin";
    public static final String COLUMN_REASON_TEXT = "reasonText";
    public static final String COLUMN_ANCHOR = "anchor";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_ISREADONLY = "isReadOnly";
    public static final String[] reasonColumns = {ReasonDBOpenHelper.COLUMN_ID,
            ReasonDBOpenHelper.COLUMN_TYPE, ReasonDBOpenHelper.COLUMN_ANCHOR,
            ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP,
            ReasonDBOpenHelper.COLUMN_ORIGIN,
            ReasonDBOpenHelper.COLUMN_REASON_TEXT, ReasonDBOpenHelper.COLUMN_FLAG, ReasonDBOpenHelper.COLUMN_ISREADONLY};
    public static final String CREATE_TABLE_REASON = "CREATE TABLE "
            + TABLE_REASON + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_REASON_TEXT
            + "  TEXT, " + COLUMN_LAST_TIMESTAMP + " TIMESTAMP DEFAULT NULL, "
            + COLUMN_ORIGIN + "  TEXT NOT NULL, " + COLUMN_TYPE
            + "  TEXT NOT NULL, " + COLUMN_ANCHOR + "  TEXT NOT NULL, "
            + COLUMN_FLAG + " TEXT NOT NULL, " + COLUMN_ISREADONLY
            + " Boolean DEFAULT TRUE );";
    // REASON_IMAGE table
    public static final String COLUMN_IMAGE_RAW = "imageRaw";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_REASON_ID = "reasonId";
    public static final String CREATE_TABLE_REASON_IMAGE = "CREATE TABLE "
            + TABLE_REASON_IMAGE + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_IMAGE_RAW
            + "  BLOB, " + " + COLUMN_CREATION_TIME TIMESTAMP DEFAULT NULL, "
            + COLUMN_REASON_ID + "  TEXT NOT NULL, " + " FOREIGN KEY ("
            + COLUMN_REASON_ID + ") REFERENCES " + TABLE_REASON + " ("
            + COLUMN_ID + "));";
    public static final String COLUMN_CREATION_TIME = "creationTime";
    public final static String TYPE_THEORY = "THEORY";
    public final static String TYPE_OBSERVATION = "OBSERVATION";
    private static final String DATABASE_NAME = "reasons.db";
    private static final int DATABASE_VERSION = 1;
    private static ReasonDBOpenHelper rInstance;
    private SQLiteDatabase database;

    public ReasonDBOpenHelper(Context context, String name,
                              CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private ReasonDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static ReasonDBOpenHelper getInstance(Context context) {
        if (rInstance == null) {
            rInstance = new ReasonDBOpenHelper(context);
        }
        return rInstance;
    }

    public static Reason cursorToReason(Cursor c) {
        Reason reason = new Reason();
        reason.setId(c.getLong(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ID)));
        reason.setAnchor(c.getString(c
                .getColumnIndex(ReasonDBOpenHelper.COLUMN_ANCHOR)));
        reason.setFlag(c.getString(c
                .getColumnIndex(ReasonDBOpenHelper.COLUMN_FLAG)));
        reason.setType(c.getString(c
                .getColumnIndex(ReasonDBOpenHelper.COLUMN_TYPE)));
        reason.setReasonText(c.getString(c
                .getColumnIndex(ReasonDBOpenHelper.COLUMN_REASON_TEXT)));
        reason.setOrigin(c.getString(c
                .getColumnIndex(ReasonDBOpenHelper.COLUMN_ORIGIN)));
        reason.setLastTimestamp(Timestamp.valueOf(c.getString(c
                .getColumnIndex(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP))));
        reason.setReadonly(c.getInt(c
                .getColumnIndex(ReasonDBOpenHelper.COLUMN_ISREADONLY)) == 1);
        return reason;
    }

    public static ContentValues getReasonContentValues(Reason reason) {
        ContentValues values = new ContentValues();
        values.put(ReasonDBOpenHelper.COLUMN_TYPE, reason.getType());
        values.put(ReasonDBOpenHelper.COLUMN_ANCHOR, reason.getAnchor());
        values.put(ReasonDBOpenHelper.COLUMN_ORIGIN, reason.getOrigin());
        values.put(ReasonDBOpenHelper.COLUMN_REASON_TEXT,
                reason.getReasonText());
        java.util.Date date = new java.util.Date();
        values.put(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP, new Timestamp(date.getTime()).toString());
        values.put(ReasonDBOpenHelper.COLUMN_FLAG, reason.getFlag());
        values.put(ReasonDBOpenHelper.COLUMN_ISREADONLY, reason.isReadonly());

        return values;
    }

    public static ContentValues getReasonContentValues(Reason reason, String localOrigin) {
        ContentValues values = new ContentValues();
        values.put(ReasonDBOpenHelper.COLUMN_TYPE, reason.getType());
        values.put(ReasonDBOpenHelper.COLUMN_ANCHOR, reason.getAnchor());
        values.put(ReasonDBOpenHelper.COLUMN_ORIGIN, reason.getOrigin());
        values.put(ReasonDBOpenHelper.COLUMN_REASON_TEXT,
                reason.getFlag());
        java.util.Date date = new java.util.Date();
        values.put(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP, new Timestamp(date.getTime()).toString());
        values.put(ReasonDBOpenHelper.COLUMN_FLAG, reason.getFlag());

        boolean isReadyOnly = true;
        if (localOrigin.toLowerCase().equals(reason.getOrigin().toLowerCase())) {
            isReadyOnly = false;
        } else {
            isReadyOnly = true;
        }

        values.put(ReasonDBOpenHelper.COLUMN_ISREADONLY, isReadyOnly);

        return values;
    }

    public static ContentValues getReasonContentValues(Note reason) {
        ContentValues values = new ContentValues();
        values.put(ReasonDBOpenHelper.COLUMN_TYPE, reason.getType());
        values.put(ReasonDBOpenHelper.COLUMN_ANCHOR, reason.getAnchor());
        values.put(ReasonDBOpenHelper.COLUMN_ORIGIN, reason.getOrigin());
        values.put(ReasonDBOpenHelper.COLUMN_REASON_TEXT,
                reason.getReason());
        java.util.Date date = new java.util.Date();
        values.put(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP, new Timestamp(date.getTime()).toString());
        values.put(ReasonDBOpenHelper.COLUMN_FLAG, reason.getColor());
        values.put(ReasonDBOpenHelper.COLUMN_ISREADONLY, false);

        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(CREATE_TABLE_REASON);
        //seedDb(db);
        // db.execSQL(CREATE_TABLE_REASON_IMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ReasonDBOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_IMAGE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_IMAGE);
        onCreate(db);
    }

    public void seedDb(SQLiteDatabase db) {

        Reason earthRed1 = new Reason();
        earthRed1.setAnchor(Reason.CONST_BROWN);
        earthRed1.setFlag(Reason.CONST_YELLOW);
        earthRed1.setOrigin("Derek");
        earthRed1.setReasonText("testing");
        earthRed1.setType(Reason.TYPE_OBSERVATION);
        earthRed1.setReadonly(false);

        createReason(earthRed1, db);

        Reason earthRed2 = new Reason();

        earthRed2.setAnchor(Reason.CONST_BROWN);
        earthRed2.setFlag(Reason.CONST_YELLOW);
        earthRed2.setOrigin("Tony");
        earthRed2.setReasonText("Because Yellow moved faster than Red.");
        earthRed2.setType(Reason.TYPE_OBSERVATION);
        earthRed2.setReadonly(true);

        createReason(earthRed2, db);

        Reason earthRed21 = new Reason();

        earthRed21.setAnchor(Reason.CONST_BROWN);
        earthRed21.setFlag(Reason.CONST_YELLOW);
        earthRed21.setOrigin("Tom");
        earthRed21.setReasonText("Because they saw Yellow in front of Red.");
        earthRed21.setType(Reason.TYPE_OBSERVATION);
        earthRed21.setReadonly(true);

        createReason(earthRed21, db);


    }

    public void createReason(Reason reason) {
        this.createReason(reason, database);
    }

    public void createReason(Reason reason, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(ReasonDBOpenHelper.COLUMN_TYPE, reason.getType());
        values.put(ReasonDBOpenHelper.COLUMN_ANCHOR, reason.getAnchor());
        values.put(ReasonDBOpenHelper.COLUMN_ORIGIN, reason.getOrigin());
        values.put(ReasonDBOpenHelper.COLUMN_REASON_TEXT,
                reason.getReasonText());

        java.util.Date date = new java.util.Date();
        values.put(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP,
                new Timestamp(date.getTime()).toString());
        values.put(ReasonDBOpenHelper.COLUMN_FLAG, reason.getFlag());
        values.put(ReasonDBOpenHelper.COLUMN_ISREADONLY, reason.isReadonly());

        long reasonId = database.insert(ReasonDBOpenHelper.TABLE_REASON, null,
                values);

        //Cursor cursor = database.query(ReasonDBOpenHelper.TABLE_REASON,
//		        reasonColumns, ReasonDBOpenHelper.COLUMN_ID + " = " + reasonId, null,
//		        null, null, null);
//		cursor.close();

    }

    public List<Reason> getAllReasons() {
        List<Reason> reasons = new ArrayList<Reason>();

        //SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //qb.setTables(ReasonDBOpenHelper.TABLE_REASON);
        Cursor cursor = getReadableDatabase().query(ReasonDBOpenHelper.TABLE_REASON,
                reasonColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Reason reason = cursorToReason(cursor);
            reasons.add(reason);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return reasons;
    }

    public List<Reason> getAllReasonsDump() {
        List<Reason> reasons = new ArrayList<Reason>();

        //SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //qb.setTables(ReasonDBOpenHelper.TABLE_REASON);
        Cursor cursor = getReadableDatabase().query(ReasonDBOpenHelper.TABLE_REASON,
                reasonColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Reason reason = cursorToReason(cursor);
            Log.i("SQLITE DUMP", reason.toString());
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return reasons;
    }

    public void deleteReason(Reason note) {
        String[] args = {note.getAnchor(), note.getFlag(), note.getOrigin()};
        getReadableDatabase().delete(ReasonDBOpenHelper.TABLE_REASON, ReasonDBOpenHelper.COLUMN_ANCHOR + "=? AND " + ReasonDBOpenHelper.COLUMN_FLAG + "=? AND " + ReasonDBOpenHelper.COLUMN_ORIGIN + "=?", args);
    }

    public void insertReason(ContentValues reasonContentValues) {
        long insert = getReadableDatabase().insert(ReasonDBOpenHelper.TABLE_REASON, null, reasonContentValues);
        Log.d("INSERT", "NEW INSERT: " + insert);
    }

    public void deleteAll() {
        String delete = "DELETE FROM " + ReasonDBOpenHelper.TABLE_REASON;
        getReadableDatabase().delete(ReasonDBOpenHelper.TABLE_REASON, null, null);
    }

}
