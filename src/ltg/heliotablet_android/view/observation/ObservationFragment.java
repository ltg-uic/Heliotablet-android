package ltg.heliotablet_android.view.observation;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.R.id;
import ltg.heliotablet_android.R.layout;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.controller.ObservationReasonController;
import ltg.heliotablet_android.view.theory.CircleViewDefaultTouchListener;
import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ObservationFragment extends Fragment {

	private View observationView;
	private ObservationReasonController observationController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		observationView = inflater
				.inflate(R.layout.observation_activity, container, false);
		
		  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
                 .penaltyLog()
                 .build());
		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
         .detectLeakedClosableObjects()
         .penaltyLog()
         .penaltyDeath()
         .build());
		
		observationController = ObservationReasonController.getInstance(getActivity());

			
		setupListeners();
		
		return observationView;
	}
	
	/**
	 * Setups the listeners
	 */
	private void setupListeners() {
		// add listeners to all the draggable planetViews
		// get all the colors and the

		ViewGroup planetColorsView = (FrameLayout) observationView
				.findViewById(R.id.shape_colors_include);
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
	}

}
