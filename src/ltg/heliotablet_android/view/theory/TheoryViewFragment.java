package ltg.heliotablet_android.view.theory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ltg.heliotablet_android.ActivityCommunicator;
import ltg.heliotablet_android.FragmentCommunicator;
import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.controller.TheoryReasonController;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class TheoryViewFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private String theoryAnchor;
	private TheoryReasonController theoryController;
	private ReasonDBOpenHelper db = null;
	private TheoryPlanetView theoryView;
	private SQLiteCursorLoader loader = null;
	private Map<String, CircleView> toShowPlanetColors = new HashMap<String, CircleView>();
	private ViewGroup planetColorsView;
	private FragmentCommunicator activityCommunicator;

	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//init theory
		theoryView = (TheoryPlanetView) inflater.inflate(R.layout.dep_theory_view,
				container, false);
		theoryView.setAnchor(theoryAnchor);
		theoryView.setOnDragListener(new TargetViewDragListener());
		
		db = ReasonDBOpenHelper.getInstance(this.getActivity());
		
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		theoryController = TheoryReasonController.getInstance(getActivity());
		theoryController.add(theoryAnchor, theoryView);
		//setupTestListeners();
		
		setupLoader();

		return theoryView;
	}

	public void dbOperation(Reason reason, String type) {
		
		
		if( type.equals("insert")) {
			ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);
			
			Loader<Cursor> loader = getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID);
			SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
			sqliteLoader.insert(ReasonDBOpenHelper.TABLE_REASON, null, reasonContentValues);
		} else if( type.equals("remove")) {
			
			String[] args = { reason.getAnchor(), reason.getFlag(), reason.getOrigin() };
			Loader<Cursor> loader = getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID);
			SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
			sqliteLoader.delete(ReasonDBOpenHelper.TABLE_REASON, ReasonDBOpenHelper.COLUMN_ANCHOR + "=? AND " + ReasonDBOpenHelper.COLUMN_FLAG + "=? AND " + ReasonDBOpenHelper.COLUMN_ORIGIN + "=?", args);
			
			
			activityCommunicator.showPlanetColor(reason.getFlag());
			//theoryController.showPlanetColor(reason.getFlag(),View.VISIBLE);
			theoryController.sendIntent(reason, MainActivity.REMOVE_THEORY);
			theoryController.makeToast("Reason Deleted");
			
		} else if( type.equals("update")) {
			
			String[] args = { reason.getAnchor(), reason.getFlag(), reason.getOrigin() };
			
			Loader<Cursor> loader = getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID);
			
			SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader)loader;
			ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);
			sqliteLoader.update(ReasonDBOpenHelper.TABLE_REASON, reasonContentValues, ReasonDBOpenHelper.COLUMN_ANCHOR + "=? AND " + ReasonDBOpenHelper.COLUMN_FLAG + "=? AND " + ReasonDBOpenHelper.COLUMN_ORIGIN + "=?", args);
			
		}
		
		
	}
	private void addBackToPlanetView(String flag) {
		CircleView circleView = toShowPlanetColors.get(flag);
		planetColorsView.addView(circleView);
		planetColorsView.invalidate();
		
	}

	private void setupLoader() {
		getLoaderManager().initLoader(ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID,
				null, this);
		LoaderManager.enableDebugLogging(true);
	}
	
	
	class TargetViewDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View targetView, DragEvent event) {

			// Dropped, reassign View to ViewGroup
			View dragged = (View) event.getLocalState();
			ViewGroup sourceView = (ViewGroup) dragged.getParent();

			int action = event.getAction();
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:

				if (targetView.equals(sourceView)) {
					Log.i("DRAG", "there equal");
				} else {
					//dragged.setVisibility(View.INVISIBLE);
					sourceView.removeView(dragged);

					RelativeLayout rel = (RelativeLayout) dragged;

					CircleView cv = (CircleView) dragged;

					if (targetView instanceof TheoryPlanetView) {
						TheoryPlanetView tv = (TheoryPlanetView) targetView;
						tv.showPopoverWithFlag(cv.getFlag());
						TheoryViewFragment.this.activityCommunicator.addUsedPlanetColors(cv);
						String origin = TheoryViewFragment.this.theoryController.getUserName();
						Reason reason = new Reason(tv.getAnchor(),cv.getFlag(), Reason.TYPE_THEORY,origin, false);
						TheoryViewFragment.this.dbOperation(reason, "insert");
						TheoryViewFragment.this.theoryController.sendIntent(reason, MainActivity.NEW_THEORY);
					} 
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			case DragEvent.ACTION_DRAG_LOCATION:
				View v = (View) event.getLocalState();
				v.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			return true;
		}
	}

	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}



	public void putPlanetColorView(CircleView cv) {
		toShowPlanetColors.put(cv.getFlag(), cv);
	}
	
	public View getPlanetColorView(String flag) {
		return toShowPlanetColors.get(flag);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		switch (id) {
		case ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID:
			return new SQLiteCursorLoader(
					this.getActivity(),
					db,
					"SELECT _id, anchor, type, flag, reasonText, isReadOnly, lastTimestamp, origin FROM reason WHERE type = '"
							+ ReasonDBOpenHelper.TYPE_THEORY
							+ "' AND anchor = '"
							+ theoryAnchor
							+ "' ORDER BY anchor, flag, isReadOnly;", null);
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		switch (loader.getId()) {
		case ReasonDBOpenHelper.ALL_REASONS_THEORY_LOADER_ID:
			updateAllViews(data);
			break;
		}
		Log.d("THEORY FRAGMENT", "onLoadFinished");
	}
	
	private void updateAllViews(Cursor data) {
		if (data != null && data.getCount() > 0) {
			List<Reason> allReasons = Lists.newArrayList();
			allReasons = Collections.synchronizedList(allReasons);
			data.moveToFirst();
			while (!data.isAfterLast()) {
				Reason reason = ReasonDBOpenHelper.cursorToReason(data);
				allReasons.add(reason);
				data.moveToNext();
			}
			quickDump(allReasons);
			ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(theoryAnchor)));
			theoryView.updateCircleView(imReasonSet);
			//theoryController.updateViews(allReasons, this.theoryAnchor);
		} else {
			List<Reason> allReasons = Lists.newArrayList();
			if( theoryView.getChildCount() > 0) {
				theoryView.clearFlagMap();
				theoryView.removeAllViews();
				theoryView.invalidate();
			}
		}
	}

	private void quickDump(List<Reason> allReasons) {
		for (Reason reason : allReasons) {
			System.out.println("REASON: " + reason.toString());
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		System.out.println("yo");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);

		TypedArray a = activity.obtainStyledAttributes(attrs,
				R.styleable.TheoryViewFragment);
		
		
		theoryAnchor = StringUtils.lowerCase((String) a.getText(R.styleable.TheoryViewFragment_android_label));
		a.recycle();

	}

	public void addPlanetColorsView(ViewGroup planetColorsView) {
		this.planetColorsView = planetColorsView;
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activityCommunicator =(FragmentCommunicator)activity;
	}
	

}
