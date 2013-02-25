package ltg.heliotablet_android.view.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import ltg.heliotablet_android.TheoryViewFragment;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.TheoryPlanetView;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;

public class ObservationReasonController {

	private static ObservationReasonController tInstance;
	private Context context;
	private ReasonDataSource reasonDatasource;
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();
	private ArrayList<Reason> cachedReasons = new ArrayList<Reason>();
	ImmutableSet<String> allAnchors = ImmutableSet.of(Reason.CONST_MERCURY, Reason.CONST_VENUS, Reason.CONST_EARTH, Reason.CONST_MARS, Reason.CONST_JUPITER, Reason.CONST_SATURN, Reason.CONST_NEPTUNE, Reason.CONST_URANUS);
	
	
	private ObservationReasonController(Context context) {
		this.context = context;
	}
	
	public static ObservationReasonController getInstance(Context context) {
		if (tInstance == null) {
			tInstance = new ObservationReasonController(context);
		}
		return tInstance;
	}
	
	public void open() {
		reasonDatasource.open();
	}

	public void close() {
		reasonDatasource.close();
	}
	
	public void add(String anchor, TheoryPlanetView theoryview) {
		this.theoryViewsToAnchors.put(anchor, theoryview);
	}

	public void setTheoryViewsToAnchors(
			HashMap<String, TheoryPlanetView> theoryViewsToAnchors) {
		this.theoryViewsToAnchors = theoryViewsToAnchors;
		
	}
	
	public HashMap<String, TheoryPlanetView> getTheoryViewsToAnchors() {
		return theoryViewsToAnchors;
	}

	public void updateViews(List<Reason> allReasons, String anchor) {
			ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(anchor)));
			TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(anchor);
			if( !imReasonSet.isEmpty() ) {
				theoryPlanetView.updateCircleView(imReasonSet);
		}
		
	}
	
	private SQLiteCursorLoader getSqliteCursorLoader(String anchor) {
		Activity mainActivity = (Activity)context;
		LoaderManager loaderManager = mainActivity.getLoaderManager();
		
		//find the loader
		TheoryViewFragment tf = (TheoryViewFragment) mainActivity.getFragmentManager().findFragmentByTag(anchor);
		Loader<Cursor> loader = tf.getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_LOADER_ID);
		SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
		return sqliteLoader;
	}

	public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());

		String[] args = { String.valueOf(reason.getId()) };

		sqliteCursorLoader.delete(ReasonDBOpenHelper.TABLE_REASON,
				"_ID=?", args);
		
		TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(reason.getAnchor());
		
		if( isScheduledForViewRemoval )
		theoryPlanetView.removeFlagFromCircleViewMap(reason.getFlag());
		
		
	}
	
	

	public void updateReason(Reason reason) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());
		String[] args = { String.valueOf(reason.getId()) };
		ContentValues reasonContentValues = ReasonDataSource.getReasonContentValues(reason);  
		sqliteCursorLoader.update(ReasonDBOpenHelper.TABLE_REASON, reasonContentValues, "_id=?", args);
		
	}

	public void insertReason(Reason reason) {
		SQLiteCursorLoader sqliteCursorLoader = getSqliteCursorLoader(reason.getAnchor());
		ContentValues reasonContentValues = ReasonDataSource.getReasonContentValues(reason);
		sqliteCursorLoader.insert(ReasonDBOpenHelper.TABLE_REASON, null, reasonContentValues);		
	}
	
	
	
}
