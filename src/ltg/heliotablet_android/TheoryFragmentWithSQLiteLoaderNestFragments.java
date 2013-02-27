package ltg.heliotablet_android;

import java.util.HashMap;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.deprecated.ReasonDataSource;
import ltg.heliotablet_android.view.controller.TheoryReasonController;
import ltg.heliotablet_android.view.theory.CircleView;
import ltg.heliotablet_android.view.theory.CircleViewDefaultTouchListener;
import ltg.heliotablet_android.view.theory.TheoryPlanetView;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;

public class TheoryFragmentWithSQLiteLoaderNestFragments extends Fragment {

	private TheoryReasonController theoryController;
	private ViewGroup theoriesView;
	private ReasonDBOpenHelper db = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		theoriesView = (ViewGroup) inflater.inflate(
				R.layout.theories_activity_nested, container, false);
		 db= ReasonDBOpenHelper.getInstance(this.getActivity());
		  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
                  .penaltyLog()
                  .build());
		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
          .detectLeakedClosableObjects()
          .penaltyLog()
          .penaltyDeath()
          .build());
		
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

}
