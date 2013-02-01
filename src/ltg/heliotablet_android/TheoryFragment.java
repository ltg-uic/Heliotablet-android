package ltg.heliotablet_android;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.CircleViewDefaultTouchListener;
import ltg.heliotablet_android.view.PopoverView;
import ltg.heliotablet_android.view.PopoverView.PopoverViewDelegate;
import ltg.heliotablet_android.view.TheoryPlanetView;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TheoryFragment extends Fragment {

	private ReasonDataSource reasonDatasource;
	
	private ViewGroup theoriesView;
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();
	private GestureDetector gestureDetector;
	private View slideScreen;
	private RelativeLayout viewPagerLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		reasonDatasource = new ReasonDataSource(this.getActivity());
		reasonDatasource.open();
		
		theoriesView = (ViewGroup) inflater.inflate(R.layout.theories_activity_vertical,
				container, false);
		
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
					LinearLayout container = (LinearLayout) targetView;
					LayoutParams layoutParams = dragged.getLayoutParams();
					layoutParams.height = 81;
					layoutParams.width = 80;

					RelativeLayout rel = (RelativeLayout) dragged;

					CircleView cv = (CircleView) dragged;
					cv.setReasonText("2");
					cv.setOnTouchListener(null);
					cv.enableDoubleTap();
					
					container.addView(cv);
					cv.setVisibility(View.VISIBLE);
					
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
	public void onResume() {
		reasonDatasource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		reasonDatasource.close();
		super.onPause();
	}
	
	
	
	
}
