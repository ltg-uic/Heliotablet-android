package ltg.heliotablet_android;

import java.util.Collections;
import java.util.List;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.ObservationAnchorView;
import ltg.heliotablet_android.view.ObservationCircleView;
import ltg.heliotablet_android.view.TheoryPlanetView;
import ltg.heliotablet_android.view.controller.TheoryReasonController;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.google.common.collect.Lists;

public class ObservationViewFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private String observationAnchor;
	private TheoryReasonController theoryController;
	private ReasonDBOpenHelper db = null;
	private ObservationAnchorView observationAnchorView;
	private SQLiteCursorLoader loader = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//init theory
		
		observationAnchorView = (ObservationAnchorView) inflater.inflate(R.layout.observation_anchor_view,
				container, false);
		observationAnchorView.setAnchor(observationAnchor);
		
		ObservationCircleView circleView = (ObservationCircleView)inflater.inflate(R.layout.observation_view_layout, container, false);
		
		observationAnchorView.addView(circleView);
		
		circleView = (ObservationCircleView)inflater.inflate(R.layout.observation_view_layout, container, false);
		observationAnchorView.addView(circleView);
		circleView = (ObservationCircleView)inflater.inflate(R.layout.observation_view_layout, container, false);
		observationAnchorView.addView(circleView);
		circleView = (ObservationCircleView)inflater.inflate(R.layout.observation_view_layout, container, false);
		observationAnchorView.addView(circleView);
		circleView = (ObservationCircleView)inflater.inflate(R.layout.observation_view_layout, container, false);
		observationAnchorView.addView(circleView);
		circleView = (ObservationCircleView)inflater.inflate(R.layout.observation_view_layout, container, false);
		observationAnchorView.addView(circleView);
		circleView = (ObservationCircleView)inflater.inflate(R.layout.observation_view_layout, container, false);
		observationAnchorView.addView(circleView);
		db = ReasonDBOpenHelper.getInstance(this.getActivity());
		
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

//		theoryController = TheoryReasonController.getInstance(getActivity());
//		theoryController.add(theoryAnchor, observationCircleView);
		//setupTestListeners();
		//setupLoader();

		return observationAnchorView;
	}

	private void setupLoader() {
		getLoaderManager().initLoader(ReasonDBOpenHelper.ALL_REASONS_LOADER_ID,
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
					sourceView.removeView(dragged);

					RelativeLayout rel = (RelativeLayout) dragged;

					CircleView cv = (CircleView) dragged;

					if (targetView instanceof TheoryPlanetView) {
						TheoryPlanetView tv = (TheoryPlanetView) targetView;
						Reason reason = new Reason(tv.getAnchor(),cv.getFlag(), Reason.TYPE_THEORY,"tony", false);
						theoryController.insertReason(reason);
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

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		switch (id) {
		case ReasonDBOpenHelper.ALL_REASONS_LOADER_ID:
			return new SQLiteCursorLoader(
					this.getActivity(),
					db,
					"SELECT _id, anchor, type, flag, reasonText, isReadOnly, lastTimestamp, origin FROM reason WHERE type = '"
							+ ReasonDBOpenHelper.TYPE_THEORY
							+ "' AND anchor = '"
							+ observationAnchor
							+ "' ORDER BY anchor, flag, isReadOnly;", null);
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		switch (loader.getId()) {
		case ReasonDBOpenHelper.ALL_REASONS_LOADER_ID:
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
				Reason reason = ReasonDataSource.cursorToReason(data);
				allReasons.add(reason);
				data.moveToNext();
			}
			quickDump(allReasons);
			theoryController.updateViews(allReasons, this.observationAnchor);
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
				R.styleable.ObservationViewFragment);
		
		
		observationAnchor = StringUtils.lowerCase((String) a.getText(R.styleable.ObservationViewFragment_android_label));
		a.recycle();

	}

}
