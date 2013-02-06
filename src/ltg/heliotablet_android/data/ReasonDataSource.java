package ltg.heliotablet_android.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;

public class ReasonDataSource {

	private SQLiteDatabase database;
	private ReasonDBOpenHelper reasonDBHelper;
	private String[] reasonColumns = { ReasonDBOpenHelper.COLUMN_ID,
			ReasonDBOpenHelper.COLUMN_TYPE, ReasonDBOpenHelper.COLUMN_ANCHOR,
			ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP,
			ReasonDBOpenHelper.COLUMN_ORIGIN,
			ReasonDBOpenHelper.COLUMN_REASON_TEXT, ReasonDBOpenHelper.COLUMN_FLAG, ReasonDBOpenHelper.COLUMN_ISREADONLY };
	private String[] reasonImageColumns = { ReasonDBOpenHelper.COLUMN_ID,
			ReasonDBOpenHelper.COLUMN_IMAGE_RAW, ReasonDBOpenHelper.COLUMN_URL,
			ReasonDBOpenHelper.COLUMN_REASON_ID };
	private Context context;

	
	private static ReasonDataSource rInstance;

	public static ReasonDataSource getInstance(Context context) {
		if (rInstance == null) {
			rInstance = new ReasonDataSource(context);
		}
		return rInstance;
	}
	
	private ReasonDataSource(Context context) {
		reasonDBHelper = new ReasonDBOpenHelper(context);
		this.context = context;
	}

	public Reason createReason(Reason reason) {
		ContentValues values = new ContentValues();
		values.put(ReasonDBOpenHelper.COLUMN_TYPE, reason.getType());
		values.put(ReasonDBOpenHelper.COLUMN_ANCHOR, reason.getAnchor());
		values.put(ReasonDBOpenHelper.COLUMN_ORIGIN, reason.getOrigin());
		values.put(ReasonDBOpenHelper.COLUMN_REASON_TEXT,
				reason.getReasonText());
		
		java.util.Date date= new java.util.Date();
		values.put(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP, new Timestamp(date.getTime()).toString());
		values.put(ReasonDBOpenHelper.COLUMN_FLAG, reason.getFlag());
		values.put(ReasonDBOpenHelper.COLUMN_ISREADONLY, reason.isReadonly());

		long reasonId = database.insert(ReasonDBOpenHelper.TABLE_REASON, null,
				values);
		
		Cursor cursor = database.query(ReasonDBOpenHelper.TABLE_REASON,
		        reasonColumns, ReasonDBOpenHelper.COLUMN_ID + " = " + reasonId, null,
		        null, null, null);
		cursor.moveToFirst();
		Reason newReason = cursorToReason(cursor);
		cursor.close();
		return newReason;
	}
	
	public ContentValues getReasonContentValues(Reason reason) {
		ContentValues values = new ContentValues();
		values.put(ReasonDBOpenHelper.COLUMN_TYPE, reason.getType());
		values.put(ReasonDBOpenHelper.COLUMN_ANCHOR, reason.getAnchor());
		values.put(ReasonDBOpenHelper.COLUMN_ORIGIN, reason.getOrigin());
		values.put(ReasonDBOpenHelper.COLUMN_REASON_TEXT,
				reason.getReasonText());
		values.put(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP, reason
				.getLastTimestamp().toString());
		values.put(ReasonDBOpenHelper.COLUMN_FLAG, reason.getFlag());
		values.put(ReasonDBOpenHelper.COLUMN_ISREADONLY, reason.isReadonly());
		
		return values;
	}


	public ReasonImage createImageReason(ReasonImage reasonImage) {
		ContentValues values = new ContentValues();
		values.put(ReasonDBOpenHelper.COLUMN_CREATION_TIME, reasonImage.getCreationTime().toString());
		values.put(ReasonDBOpenHelper.COLUMN_URL, reasonImage.getUrl());
		values.put(ReasonDBOpenHelper.COLUMN_REASON_ID, reasonImage.getReasonId());
		return null;
	}

	public void deleteReason(Long reasonId) {
		database.delete(ReasonDBOpenHelper.TABLE_REASON, ReasonDBOpenHelper.COLUMN_ID
		        + " = " + reasonId, null);
	}

	public void deleteReasonImage(Long reasonImageId) {
		database.delete(ReasonDBOpenHelper.TABLE_REASON_IMAGE, ReasonDBOpenHelper.COLUMN_ID
		        + " = " + reasonImageId, null);
	}

	public void deleteReasonImageByReasonId(Long reasonId) {
		database.delete(ReasonDBOpenHelper.TABLE_REASON_IMAGE, ReasonDBOpenHelper.COLUMN_REASON_ID
		        + " = " + reasonId, null);
	}
	
	
	public boolean checkReasonExists(Long id) {
		   Cursor cursor = database.rawQuery("select 1 from " + reasonDBHelper.TABLE_REASON + " where _id=%s", new String[] { id.toString() });
		   boolean exists = (cursor.getCount() > 0);
		   cursor.close();
		   return exists;
	}
	
	public List<Reason> getAllReasons() {
	    List<Reason> reasons = new ArrayList<Reason>();

	    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	    qb.setTables(ReasonDBOpenHelper.TABLE_REASON);
	    Cursor cursor = qb.query(database, null, null, null, null, null, ReasonDBOpenHelper.COLUMN_ANCHOR);
//	    Cursor cursor = database.query(ReasonDBOpenHelper.TABLE_REASON,
//	        reasonColumns, null, null, null, null, null);

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
	
	
	public List<Reason> getReasonsByTypeAndAnchorAndFlag(String type, String anchor, String flag){
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ReasonDBOpenHelper.TABLE_REASON);
		qb.appendWhere(ReasonDBOpenHelper.COLUMN_TYPE +" = "+type+" AND " + ReasonDBOpenHelper.COLUMN_ANCHOR +" = "+ anchor +" AND " + ReasonDBOpenHelper.COLUMN_FLAG +" = "+ flag);
		Cursor c = qb.query(database, null, null, null, null, null, null);
        try {
      
            
            if(c.getCount() != 0){
            	
            	List<Reason> reasons = new ArrayList<Reason>();
            	
            	if (c.moveToFirst()) {
                	do {
                		Reason reason = cursorToReason(c);
                    	reasons.add(reason);
                	} while (c.moveToNext());
                	
                	return reasons;
            	}
            }
            
            
        } catch (Exception e) {
            throw new SQLiteException(e.getMessage());
        }
        
        return null;
	}
	
	 public Reason cursorToReason(Cursor c) {
		    Reason reason = new Reason();
		    reason.setId(c.getLong(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ID)));
    		reason.setAnchor(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ANCHOR)));
    		reason.setFlag(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_FLAG)));
    		reason.setType(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_TYPE)));
    		reason.setReasonText(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_REASON_TEXT)));
    		reason.setOrigin(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ORIGIN)));
    		reason.setLastTimestamp(Timestamp.valueOf(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP))));
    		reason.setReadonly(c.getInt(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ISREADONLY)) == 1);
		    return reason;
	 }

	public void open() throws SQLException {
		database = reasonDBHelper.getWritableDatabase();
	}

	public void close() {
		reasonDBHelper.close();
	}

}
