package ltg.heliotablet_android.view.controller;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.deprecated.ReasonDataSource;
import android.content.ContentValues;
import android.content.Context;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;

public class ReasonController {
	
	protected Context context;
	
	public SQLiteCursorLoader getSqliteCursorLoader(String anchor) {
		return null;
	}

	public void updateReason(Reason reason) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());
		String[] args = { String.valueOf(reason.getId()) };
		ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);  
		sqliteCursorLoader.update(ReasonDBOpenHelper.TABLE_REASON, reasonContentValues, "_id=?", args);
		
	}

	public void insertReason(Reason reason) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());
		ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);
		sqliteCursorLoader.insert(ReasonDBOpenHelper.TABLE_REASON, null, reasonContentValues);		
	}
	
	public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());

		String[] args = { String.valueOf(reason.getId()) };

		sqliteCursorLoader.delete(ReasonDBOpenHelper.TABLE_REASON,
				"_ID=?", args);
		
	}

}
