package ltg.heliotablet_android.view.controller;

import java.util.HashMap;
import java.util.List;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.ObservationAnchorView;
import ltg.heliotablet_android.view.TheoryPlanetView;
import android.content.Context;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;

public class ObservationReasonController {

	private static ObservationReasonController tInstance;
	private Context context;
	private ReasonDataSource reasonDatasource;
	private HashMap<String, ObservationAnchorView> observationViewsToAnchors = new HashMap<String, ObservationAnchorView>();
	
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
	
	public void add(String anchor, ObservationAnchorView theoryview) {
		this.observationViewsToAnchors.put(anchor, theoryview);
	}

	public void setTheoryViewsToAnchors(
			HashMap<String, ObservationAnchorView> theoryViewsToAnchors) {
		this.observationViewsToAnchors = theoryViewsToAnchors;
		
	}
	
	public HashMap<String, ObservationAnchorView> getTheoryViewsToAnchors() {
		return observationViewsToAnchors;
	}

	public void updateViews(List<Reason> allReasons, String anchor) {
		ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(anchor)));
		ObservationAnchorView observationView = observationViewsToAnchors.get(anchor);
		if( !imReasonSet.isEmpty() ) {
			observationView.updateObservationCircleView(imReasonSet);
		}
	}
	
	private SQLiteCursorLoader getSqliteCursorLoader(String anchor) {
		return null;
	}

	public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
	}
	
	

	public void updateReason(Reason reason) {
	}

	public void insertReason(Reason reason) {
	}
	
	
	
}
