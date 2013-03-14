package ltg.heliotablet_android.view.controller;



import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;


public class ReasonController {
	
	protected Context context;
	protected String userName;
	private SharedPreferences settings;
	
	
	public ReasonController(Context context) {
		this.context = context;
		settings = context.getSharedPreferences(context.getString(R.string.xmpp_prefs),context.MODE_PRIVATE);
		
	}
	
	public SQLiteCursorLoader getSqliteCursorLoader(String anchor) {
		return null;
	}

	public void updateReason(Reason reason) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());
		String[] args = { String.valueOf(reason.getId()) };
		ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);  
		sqliteCursorLoader.update(ReasonDBOpenHelper.TABLE_REASON, reasonContentValues, "_id=?", args);
	}

	public String getUserName() {
		
		String userName = settings.getString(
				context.getString(R.string.user_name), "");
		
		return userName;
		
	}
	
	
	public void insertReason(Reason reason) throws NullPointerException {
		SQLiteCursorLoader sqliteCursorLoader = null;
		
		try {
			sqliteCursorLoader = getAnchorSqliteCursorLoader(reason.getAnchor());
		} catch(NullPointerException e) {
			throw new NullPointerException("insertReason null");
		}
		
		ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);
		
		sqliteCursorLoader.insert(ReasonDBOpenHelper.TABLE_REASON, null, reasonContentValues);		
	}
	
	public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());

		String[] args = { String.valueOf(reason.getId()) };

		sqliteCursorLoader.delete(ReasonDBOpenHelper.TABLE_REASON,
				"_ID=?", args);
		
	}
	
	public void deleteReasonByOriginAndType(Reason reason) throws NullPointerException{
		SQLiteCursorLoader sqliteCursorLoader = null;
		
		try {
			sqliteCursorLoader = getAnchorSqliteCursorLoader(reason.getAnchor());
		} catch(NullPointerException e) {
			throw new NullPointerException("deleteReasonByOriginAndType null");
		}
		String[] args = { reason.getType(), reason.getAnchor(), reason.getFlag(), reason.getOrigin(), reason.getReasonText() };

		sqliteCursorLoader.delete(ReasonDBOpenHelper.TABLE_REASON,
				"TYPE=? and ANCHOR=? and FLAG=? and ORIGIN=? and REASONTEXT=?", args);
		
	}
	
	public SQLiteCursorLoader getAnchorSqliteCursorLoader(String anchor) throws NullPointerException {
		SQLiteCursorLoader sqliteCursorLoader =null;
		try {
			sqliteCursorLoader = getSqliteCursorLoader(anchor);
		} catch(NullPointerException e) {
			throw new NullPointerException("Insert reason sqlloader");
		}
		return sqliteCursorLoader;
	}

}
