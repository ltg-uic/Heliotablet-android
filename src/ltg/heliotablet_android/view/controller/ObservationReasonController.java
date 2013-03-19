package ltg.heliotablet_android.view.controller;

import java.util.HashMap;
import java.util.List;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.observation.ObservationAnchorView;
import ltg.heliotablet_android.view.observation.ObservationViewFragment;
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

public class ObservationReasonController extends ReasonController {

	private static ObservationReasonController tInstance;
	private HashMap<String, ObservationAnchorView> observationViewsToAnchors = new HashMap<String, ObservationAnchorView>();
	
	private ObservationReasonController(Context context) {
		super(context);
	}
	
	public static ObservationReasonController getInstance(Context context) {
		if (tInstance == null) {
			tInstance = new ObservationReasonController(context);
		}
		return tInstance;
	}
	
	public void add(String anchor, ObservationAnchorView theoryview) {
		this.observationViewsToAnchors.put(anchor, theoryview);
	}


	public void updateViews(List<Reason> allReasons, String anchor) {
		ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(anchor)));
		ObservationAnchorView observationView = observationViewsToAnchors.get(anchor);
		if( !imReasonSet.isEmpty() ) {
			observationView.updateObservationCircleView(imReasonSet);
		}
	}

	public SQLiteCursorLoader getSqliteCursorLoader(String anchor) throws NullPointerException {
		FragmentActivity mainActivity = (FragmentActivity)context;
		
		//find the loader
		Fragment findFragmentByTag = mainActivity.getSupportFragmentManager().findFragmentByTag(anchor+"_OBSERVATION");
		
		if(findFragmentByTag == null)
			throw new NullPointerException("ObservationReasonController anchor: " + anchor + " loader null."); 
		
		ObservationViewFragment obs = (ObservationViewFragment)findFragmentByTag;
		Loader<Cursor> loader = obs.getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_OBS_LOADER_ID);
		SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
		return sqliteLoader;
	}
	
	public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
		super.deleteReason(reason, isScheduledForViewRemoval);
		
		ObservationAnchorView observationAnchorView = observationViewsToAnchors.get(reason.getAnchor());
		
//		if( isScheduledForViewRemoval )
//			observationAnchorView.removeFlagFromCircleViewMap(reason.getFlag());
	}
	
	public void operationObservation(Reason reason, String anchor, String command) {
		FragmentActivity mainActivity = (FragmentActivity)context;
		
		//find the loader
		Fragment findFragmentByTag = mainActivity.getSupportFragmentManager().findFragmentByTag(anchor+"_OBSERVATION");
		if(findFragmentByTag == null)
			return;
		
		//find the loader
		ObservationViewFragment of = (ObservationViewFragment)findFragmentByTag;
		of.dbOperation(reason, command);
	}

	
	
	
	
}
