package ltg.heliotablet_android.view.theory;

import java.util.HashMap;
import java.util.Iterator;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.controller.TheoryReasonController;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class TheoryFragmentWithSQLiteLoaderNestFragments extends Fragment {

	private TheoryReasonController theoryController;
	private ViewGroup theoriesView;
	private ReasonDBOpenHelper db = null;
	private HashMap<String, View> planetColors = new HashMap<String, View>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		theoriesView = (ViewGroup) inflater.inflate(
				R.layout.theories_activity_nested, container, false);
		ViewGroup planetColorsView = (ViewGroup) theoriesView.findViewById(R.id.colors_include);
		
		int childCount = planetColorsView.getChildCount();
		for (int i = 0; i < planetColorsView.getChildCount(); i++) {
	       CircleView child = (CircleView) planetColorsView.getChildAt(i);
	       getPlanetColors().put(child.getFlag(), child);
	    }
		
		
		
		 db= ReasonDBOpenHelper.getInstance(this.getActivity());
//		  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
//                  .penaltyLog()
//                  .build());
//		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
//          .detectLeakedClosableObjects()
//          .penaltyLog()
//          .penaltyDeath()
//          .build());
		
		theoryController = TheoryReasonController.getInstance(getActivity());

		setupListeners();
		
		//getData(ReasonContentProvider.CONTENT_URI);
		
		return theoriesView;
	}

	/**
	 * Setups the listeners
	 */
	private void setupListeners() {
		// add listeners to all the draggable planetViews
		// get all the colors and the

		ViewGroup planetColorsView = (FrameLayout) theoriesView
				.findViewById(R.id.colors_include);
		// get all the colors and the
		int childCount = planetColorsView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View color = planetColorsView.getChildAt(i);
			color.setOnTouchListener(new CircleViewDefaultTouchListener());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}

	public HashMap<String, View> getPlanetColors() {
		return planetColors;
	}

	public void setPlanetColors(HashMap<String, View> planetColors) {
		this.planetColors = planetColors;
	}

}
