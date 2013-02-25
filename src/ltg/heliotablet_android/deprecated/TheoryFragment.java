package ltg.heliotablet_android.deprecated;

import java.util.HashMap;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.R.id;
import ltg.heliotablet_android.R.layout;
import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.CircleViewDefaultTouchListener;
import ltg.heliotablet_android.view.TheoryPlanetView;
import ltg.heliotablet_android.view.controller.TheoryReasonController;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class TheoryFragment extends Fragment {

	private TheoryReasonController theoryController;
	
	private ViewGroup theoriesView;
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		theoriesView = (ViewGroup) inflater.inflate(R.layout.theories_activity_vertical,
				container, false);
		theoryController = TheoryReasonController.getInstance(getActivity());

		setupListeners();
		
		
		
		return theoriesView;
	}

	/**
	 * Setups the listeners 
	 */
	private void setupListeners() {
		//add listeners to all the draggable planetViews
		// get all the colors and the
		int childCount = theoriesView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View possibleView = theoriesView.getChildAt(i);
			if( possibleView instanceof TheoryPlanetView ) {
				TheoryPlanetView tv = (TheoryPlanetView) possibleView;
				tv.setOnDragListener(new TargetViewDragListener());
				theoryViewsToAnchors.put(tv.getAnchor(), tv);
			}
		}

		theoryController.setTheoryViewsToAnchors(theoryViewsToAnchors);
		
		ViewGroup planetColorsView = (FrameLayout) theoriesView.findViewById(R.id.colors_include);
		// get all the colors and the
		childCount = planetColorsView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View color = planetColorsView.getChildAt(i);
			color.setOnTouchListener(new CircleViewDefaultTouchListener());
		}
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
					
					if( targetView instanceof TheoryPlanetView ) {
						TheoryPlanetView tv = (TheoryPlanetView) targetView;
						//theoryController.addReason(tv.getAnchor(), cv.getFlag(), false );
					} else if( targetView instanceof CircleView ) {
						TheoryPlanetView tv = (TheoryPlanetView) targetView.getParent();
						//theoryController.addReason(tv.getAnchor(), cv.getFlag(), false );
					}
					
					//cv.setVisibility(View.VISIBLE);
					
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
	public void onResume() {
//		theoryController.open();
		super.onResume();
	}

	@Override
	public void onPause() {
//		theoryController.close();
		super.onPause();
	}
	
}
