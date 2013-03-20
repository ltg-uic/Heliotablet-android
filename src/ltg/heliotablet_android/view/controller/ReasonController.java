package ltg.heliotablet_android.view.controller;



import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.theory.CircleView;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

	

	public String getUserName() {
		
		String userName = settings.getString(
				context.getString(R.string.user_name), "");
		
		return userName;
		
	}
	
	public void deleteReasonByOriginAndType(Reason reason) throws NullPointerException{
		SQLiteCursorLoader sqliteCursorLoader = null;
		
		try {
			sqliteCursorLoader = getAnchorSqliteCursorLoader(reason.getAnchor());
		} catch(NullPointerException e) {
			throw new NullPointerException("deleteReasonByOriginAndType null");
		}
		String[] args = { reason.getAnchor(), reason.getFlag(), reason.getOrigin() };

		sqliteCursorLoader.delete(ReasonDBOpenHelper.TABLE_REASON, ReasonDBOpenHelper.COLUMN_ANCHOR + "=? AND " + ReasonDBOpenHelper.COLUMN_FLAG + "=? AND " + ReasonDBOpenHelper.COLUMN_ORIGIN + "=?", args);
	}
	
	public void updateReasonByOriginAndColorAndAnchorAndType(Reason reason) {
		SQLiteCursorLoader sqliteCursorLoader = null;
		
		try {
			sqliteCursorLoader = getAnchorSqliteCursorLoader(reason.getAnchor());
		} catch(NullPointerException e) {
			throw new NullPointerException("updateReasonByOriginAndType null");
		}
		String[] args = { reason.getType(), reason.getAnchor(), reason.getFlag(), reason.getOrigin() };

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
