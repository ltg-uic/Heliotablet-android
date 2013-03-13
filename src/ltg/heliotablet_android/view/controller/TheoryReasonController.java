package ltg.heliotablet_android.view.controller;

import java.util.HashMap;
import java.util.List;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.theory.TheoryPlanetView;
import ltg.heliotablet_android.view.theory.TheoryViewFragment;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;

import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;

public class TheoryReasonController extends ReasonController {

	private static TheoryReasonController tInstance;
	
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();
	
	private TheoryReasonController(Context context) {
		this.context = context;
	}
	
	public static TheoryReasonController getInstance(Context context) {
		if (tInstance == null) {
			tInstance = new TheoryReasonController(context);
		}
		return tInstance;
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
	

	public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
		super.deleteReason(reason, isScheduledForViewRemoval);
		
		TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(reason.getAnchor());
		
		if( isScheduledForViewRemoval )
		theoryPlanetView.removeFlagFromCircleViewMap(reason.getFlag());
	}
	
	public SQLiteCursorLoader getSqliteCursorLoader(String anchor) throws NullPointerException {
		FragmentActivity mainActivity = (FragmentActivity)context;
		
		//find the loader
		Fragment findFragmentByTag = mainActivity.getSupportFragmentManager().findFragmentByTag(anchor);
		if(findFragmentByTag == null)
			throw new NullPointerException("TheoryReasonController anchor: " + anchor + " loader null."); 
		
		//find the loader
		TheoryViewFragment tf = (TheoryViewFragment)findFragmentByTag;
		Loader<Cursor> loader = tf.getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID);
		SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
		return sqliteLoader;
	}
	
	
}
