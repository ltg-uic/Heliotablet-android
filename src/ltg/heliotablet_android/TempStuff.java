package ltg.heliotablet_android;

public class TempStuff {


//	public SQLiteCursorLoader getSqliteCursorLoader(String anchor) throws NullPointerException {
//		FragmentActivity mainActivity = (FragmentActivity)context;
//
//		//find the loader
//		Fragment findFragmentByTag = mainActivity.getSupportFragmentManager().findFragmentByTag(anchor+"_THEORY");
//		if(findFragmentByTag == null)
//			throw new NullPointerException("TheoryReasonController anchor: " + anchor + " loader null.");
//
//		//find the loader
//		TheoryViewFragment tf = (TheoryViewFragment)findFragmentByTag;
//		Loader<Cursor> loader = tf.getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID);
//		SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
//		return sqliteLoader;
//	}
//
//
//	public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
//		super.deleteReason(reason, isScheduledForViewRemoval);
//
//		TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(reason.getAnchor());
//
//		if( isScheduledForViewRemoval )
//		theoryPlanetView.removeFlagFromCircleViewMap(reason.getFlag());
//	}
//
//
//	public void updateViews(List<Reason> allReasons, String anchor) {
//		ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(anchor)));
//		TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(anchor);
//		if( !imReasonSet.isEmpty() ) {
//			theoryPlanetView.updateCircleView(imReasonSet);
//		} else {
//			theoryPlanetView.removeAllViews();
//		}
//
//
//
//		public SQLiteCursorLoader getSqliteCursorLoader(String anchor) throws NullPointerException {
//			FragmentActivity mainActivity = (FragmentActivity)context;
//
//			//find the loader
//			Fragment findFragmentByTag = mainActivity.getSupportFragmentManager().findFragmentByTag(anchor+"_OBSERVATION");
//
//			if(findFragmentByTag == null)
//				throw new NullPointerException("ObservationReasonController anchor: " + anchor + " loader null.");
//
//			ObservationViewFragment obs = (ObservationViewFragment)findFragmentByTag;
//			Loader<Cursor> loader = obs.getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_OBS_LOADER_ID);
//			SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
//			return sqliteLoader;
//		}
//
//
//		public void updateViews(List<Reason> allReasons, String anchor) {
//			ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(anchor)));
//			ObservationAnchorView observationView = observationViewsToAnchors.get(anchor);
//			if( !imReasonSet.isEmpty() ) {
//				observationView.updateObservationCircleView(imReasonSet);
//			}
//		}
//
//
//		public void deleteReason(Reason reason, boolean isScheduledForViewRemoval) {
//			super.deleteReason(reason, isScheduledForViewRemoval);
//
//			ObservationAnchorView observationAnchorView = observationViewsToAnchors.get(reason.getAnchor());
//
////			if( isScheduledForViewRemoval )
////				observationAnchorView.removeFlagFromCircleViewMap(reason.getFlag());
//		}

//}


}
