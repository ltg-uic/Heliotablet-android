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
			ReasonDBOpenHelper.COLUMN_REASON_TEXT, ReasonDBOpenHelper.COLUMN_FLAG };
	private String[] reasonImageColumns = { ReasonDBOpenHelper.COLUMN_ID,
			ReasonDBOpenHelper.COLUMN_IMAGE_RAW, ReasonDBOpenHelper.COLUMN_URL,
			ReasonDBOpenHelper.COLUMN_REASON_ID };

	public ReasonDataSource(Context context) {
		reasonDBHelper = new ReasonDBOpenHelper(context);
	}

	public Long createReason(Reason reason) {
		ContentValues values = new ContentValues();
		values.put(ReasonDBOpenHelper.COLUMN_TYPE, reason.getType());
		values.put(ReasonDBOpenHelper.COLUMN_ANCHOR, reason.getAnchor());
		values.put(ReasonDBOpenHelper.COLUMN_ORIGIN, reason.getOrigin());
		values.put(ReasonDBOpenHelper.COLUMN_REASON_TEXT,
				reason.getReasonText());
		values.put(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP, reason
				.getLastTimestamp().toString());
		values.put(ReasonDBOpenHelper.COLUMN_FLAG, reason.getFlag());

		long reasonId = database.insert(ReasonDBOpenHelper.TABLE_REASON, null,
				values);
		return reasonId;
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
                		Reason reason = new Reason();
                		reason.setId(c.getLong(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ID)));
                		reason.setAnchor(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ANCHOR)));
                		reason.setFlag(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_FLAG)));
                		reason.setType(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_TYPE)));
                		reason.setReasonText(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_REASON_TEXT)));
                		reason.setOrigin(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_ORIGIN)));
                		reason.setLastTimestamp(Timestamp.valueOf(c.getString(c.getColumnIndex(ReasonDBOpenHelper.COLUMN_LAST_TIMESTAMP))));
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

	public void open() throws SQLException {
		database = reasonDBHelper.getWritableDatabase();
	}

	public void close() {
		reasonDBHelper.close();
	}

}
