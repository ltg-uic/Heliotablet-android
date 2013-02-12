package ltg.heliotablet_android;

import java.util.HashMap;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonContentProvider;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.CircleViewDefaultTouchListener;
import ltg.heliotablet_android.view.TheoryPlanetView;
import ltg.heliotablet_android.view.controller.TheoryReasonController;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.os.Bundle;
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

public class TheoryFragmentWithLoader extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID = 0;

	private TheoryReasonController theoryController;
	private ViewGroup theoriesView;
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		theoriesView = (ViewGroup) inflater.inflate(
				R.layout.theories_activity_vertical, container, false);
		theoryController = TheoryReasonController.getInstance(getActivity());

		setupListeners();
		setupLoader();
		
		
		getData(ReasonContentProvider.CONTENT_URI);
		
		return theoriesView;
	}

	private void setupLoader() {
		// TODO Auto-generated method stub
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	/**
	 * Setups the listeners
	 */
	private void setupListeners() {
		// add listeners to all the draggable planetViews
		// get all the colors and the
		int childCount = theoriesView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View possibleView = theoriesView.getChildAt(i);
			if (possibleView instanceof TheoryPlanetView) {
				TheoryPlanetView tv = (TheoryPlanetView) possibleView;
				
				if( tv.getAnchor().equals(Reason.CONST_MARS)) {
					Button btnTag = new Button(getActivity());
			        btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			        btnTag.setText("Insert");
			        btnTag.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Reason marsORANGE = new Reason();
							
							marsORANGE.setAnchor(Reason.CONST_MARS);
							marsORANGE.setFlag(Reason.CONST_GREEN);
							marsORANGE.setOrigin("tony");
							marsORANGE.setReasonText("YEAH YEAHdfdfdfdfd ");
							marsORANGE.setType(Reason.TYPE_THEORY);
							marsORANGE.setReadonly(false);
							
							ContentValues reasonContentValues = ReasonDataSource.getReasonContentValues(marsORANGE);
							
							
							TheoryFragmentWithLoader.this.getActivity().getContentResolver().insert(ReasonContentProvider.CONTENT_URI, reasonContentValues);
							System.out.println("click!!");
							
						}
					});
			        
			        tv.addView(btnTag);
				}
				
				tv.setOnDragListener(new TargetViewDragListener());
				theoryViewsToAnchors.put(tv.getAnchor(), tv);
			}
		}

		
		theoryController.setTheoryViewsToAnchors(theoryViewsToAnchors);

		ViewGroup planetColorsView = (FrameLayout) theoriesView
				.findViewById(R.id.colors_include);
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

//					if (targetView instanceof TheoryPlanetView) {
//						TheoryPlanetView tv = (TheoryPlanetView) targetView;
//						theoryController.addReason(tv.getAnchor(),
//								cv.getFlag(), false);
//					} else if (targetView instanceof CircleView) {
//						TheoryPlanetView tv = (TheoryPlanetView) targetView
//								.getParent();
//						theoryController.addReason(tv.getAnchor(),
//								cv.getFlag(), false);
//					}

					// cv.setVisibility(View.VISIBLE);

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

	
	private void getData(Uri uri) {
		Cursor cursor = getActivity().getContentResolver().query(uri,
				ReasonDataSource.reasonColumns, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();

			Reason cursorToReason = ReasonDataSource.cursorToReason(cursor);
			System.out.println("GOT IT");
		}
		// Always close the cursor
		cursor.close();
	  }
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(TheoryFragmentWithLoader.this.getActivity(), ReasonContentProvider.CONTENT_URI,ReasonDataSource.reasonColumns, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		
		switch (loader.getId()) {
	      case LOADER_ID:
	    	  
	    	
	    	if( data != null ) {
		    	data.moveToFirst();
		  	    while (!data.isAfterLast()) {
		  	      Reason reason =  ReasonDataSource.cursorToReason(data);
					//theoryController.addReason(reason);

		  	      data.moveToNext();
		  	    }
	    	}
	    	  
	    	    System.out.println("hey" +
	    	    		"");
	    	    break;
		 }
//		System.out.println("hey");

//		
		Log.d("THEORY FRAGMENT", "onLoadFinished");
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		System.out.println("yo");
	}

}
