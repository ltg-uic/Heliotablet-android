package ltg.heliotablet_android;

import java.util.HashMap;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.CircleViewDefaultTouchListener;
import ltg.heliotablet_android.view.TheoryPlanetView;
import ltg.heliotablet_android.view.controller.TheoryReasonController;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.SimpleCursorAdapter;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;

public class TheoryFragmentWithSQLiteLoader extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	

	private TheoryReasonController theoryController;
	private ViewGroup theoriesView;
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();
	private ReasonDBOpenHelper db = null;

	private SQLiteCursorLoader loader = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		theoriesView = (ViewGroup) inflater.inflate(
				R.layout.theories_activity_vertical, container, false);
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
		setupLoader();
		
		
		//getData(ReasonContentProvider.CONTENT_URI);
		
		return theoriesView;
	}

	private void setupLoader() {
		getLoaderManager().initLoader(ReasonDBOpenHelper.ALL_REASONS_LOADER_ID, null, this);
		getLoaderManager().initLoader(ReasonDBOpenHelper.INSERT_REASON_LOADER_ID, null, this);
		LoaderManager.enableDebugLogging(true);
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
					Button insertButton = new Button(getActivity());
			        insertButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			        insertButton.setText("Insert");
			        insertButton.setOnClickListener(new OnClickListener() {
						
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
							
							Loader<Cursor> insertLoader = TheoryFragmentWithSQLiteLoader.this.getLoaderManager().getLoader(ReasonDBOpenHelper.INSERT_REASON_LOADER_ID);
							
							if( insertLoader == null) {
								TheoryFragmentWithSQLiteLoader.this.getLoaderManager().initLoader(ReasonDBOpenHelper.INSERT_REASON_LOADER_ID, null, TheoryFragmentWithSQLiteLoader.this);
								insertLoader = TheoryFragmentWithSQLiteLoader.this.getLoaderManager().getLoader(ReasonDBOpenHelper.INSERT_REASON_LOADER_ID);
								
							} 
							
							SQLiteCursorLoader sqlInsertLoader = (SQLiteCursorLoader)insertLoader;
							
							sqlInsertLoader.insert(ReasonDBOpenHelper.TABLE_REASON, null, reasonContentValues);
							
							//Bundle args = new Bundle();
						    //args.putParcelable(ARGS_PARAMS, params);
							
							//TheoryFragmentWithSQLiteLoader.this.getLoaderManager().restartLoader(ReasonDBOpenHelper.INSERT_REASON_LOADER_ID, args, TheoryFragmentWithSQLiteLoader.this);
							//TheoryFragmentWithSQLiteLoader.this.getActivity().getContentResolver().insert(ReasonContentProvider.CONTENT_URI, reasonContentValues);
							System.out.println("click!!");
							
						}
					});
			        
			        tv.addView(insertButton);
			        
			    	Button deleteButton = new Button(getActivity());
			        deleteButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			        deleteButton.setText("Delete");
			        deleteButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Loader<Cursor> deleteLoader = TheoryFragmentWithSQLiteLoader.this.getLoaderManager().getLoader(ReasonDBOpenHelper.DELETE_REASON_LOADER_ID);
							Bundle args = new Bundle();
						    args.putString("ID", String.valueOf(1) );
							if( deleteLoader == null) {
								
								TheoryFragmentWithSQLiteLoader.this.getLoaderManager().initLoader(ReasonDBOpenHelper.DELETE_REASON_LOADER_ID, args, TheoryFragmentWithSQLiteLoader.this);
								deleteLoader = TheoryFragmentWithSQLiteLoader.this.getLoaderManager().getLoader(ReasonDBOpenHelper.DELETE_REASON_LOADER_ID);
							} else {
								TheoryFragmentWithSQLiteLoader.this.getLoaderManager().restartLoader(ReasonDBOpenHelper.DELETE_REASON_LOADER_ID, args, TheoryFragmentWithSQLiteLoader.this);
							}
							
							SQLiteCursorLoader sqlDeleteLoader = (SQLiteCursorLoader)deleteLoader;
						    String[] ids= { String.valueOf(1) };

							sqlDeleteLoader.delete(db.TABLE_REASON,  "_ID=?", ids);
							
							
							
							
						}
					});
			        
			        tv.addView(deleteButton);
			        
			        Button updateButton = new Button(getActivity());
			        updateButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			        updateButton.setText("Update");
			        updateButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Loader<Cursor> deleteLoader = TheoryFragmentWithSQLiteLoader.this.getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_LOADER_ID);
							if( deleteLoader == null) {
								TheoryFragmentWithSQLiteLoader.this.getLoaderManager().initLoader(ReasonDBOpenHelper.DELETE_REASON_LOADER_ID, null, TheoryFragmentWithSQLiteLoader.this);
								deleteLoader = TheoryFragmentWithSQLiteLoader.this.getLoaderManager().getLoader(ReasonDBOpenHelper.DELETE_REASON_LOADER_ID);
							} else {
								TheoryFragmentWithSQLiteLoader.this.getLoaderManager().restartLoader(ReasonDBOpenHelper.DELETE_REASON_LOADER_ID, null, TheoryFragmentWithSQLiteLoader.this);
							}
							
							SQLiteCursorLoader sqlDeleteLoader = (SQLiteCursorLoader)deleteLoader;
						    String[] ids= { String.valueOf(1) };

						    Reason earthRed2 = new Reason();
							
							earthRed2.setAnchor(Reason.CONST_EARTH);
							earthRed2.setFlag(Reason.CONST_RED);
							earthRed2.setOrigin("tony");
							earthRed2.setReasonText("FUCKING its the biggest");
							earthRed2.setType(Reason.TYPE_THEORY);
							earthRed2.setReadonly(false);
						    
							String[] uid = { String.valueOf(2) };
						    
							ContentValues reasonContentValues = ReasonDataSource.getReasonContentValues(earthRed2);

							
							sqlDeleteLoader.update(db.TABLE_REASON, reasonContentValues, "_id=?", ids);
							
							
							
							
						}
					});
			        
			        tv.addView(updateButton);
			        
			        
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

					if (targetView instanceof TheoryPlanetView) {
						TheoryPlanetView tv = (TheoryPlanetView) targetView;
						theoryController.addReason(tv.getAnchor(),
								cv.getFlag(), false);
					} else if (targetView instanceof CircleView) {
						TheoryPlanetView tv = (TheoryPlanetView) targetView
								.getParent();
						theoryController.addReason(tv.getAnchor(),
								cv.getFlag(), false);
					}

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
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		 
		switch (id) {
	      case ReasonDBOpenHelper.ALL_REASONS_LOADER_ID:
	    	  System.out.println("ALL REASONS");
	    	  return new SQLiteCursorLoader(this.getActivity(), db, "SELECT _id, anchor, type, flag, reasonText, isReadOnly, lastTimestamp, origin FROM reason WHERE type = '" + ReasonDBOpenHelper.TYPE_THEORY + "' ORDER BY anchor;", null);
	    	  
	      case ReasonDBOpenHelper.INSERT_REASON_LOADER_ID:
	    	  System.out.println("INSERT REASONS");
	    	  return new SQLiteCursorLoader(this.getActivity(), db, "SELECT _id, anchor, type, flag, reasonText, isReadOnly, lastTimestamp, origin FROM reason WHERE _id = (select last_insert_rowid());", null);
	    	  
	      case ReasonDBOpenHelper.DELETE_REASON_LOADER_ID:
	      		System.out.println("DELETE REASONS");
	      		return new SQLiteCursorLoader(this.getActivity(), db, "SELECT _id, anchor, type, flag, reasonText, isReadOnly, lastTimestamp, origin FROM reason ORDER BY anchor;", null);

		 }
		
		 return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		
		switch (loader.getId()) {
	      case ReasonDBOpenHelper.ALL_REASONS_LOADER_ID:
	    	  updateAllViews(data);
	    	  System.out.println("ALL REASONS");
	    	  break;
	      case ReasonDBOpenHelper.INSERT_REASON_LOADER_ID:
	    	  updateViews(data);
	    	  break;
	      case ReasonDBOpenHelper.DELETE_REASON_LOADER_ID:
	    	  System.out.println("Delete");
	    	  //updateViews(data);
	    	  break;
		 }
//		System.out.println("hey");

//		
		Log.d("THEORY FRAGMENT", "onLoadFinished");
	}
	
	private void updateAllViews(Cursor data) {
		if( data != null && data.getCount() > 0 ) {
	    	data.moveToFirst();
	  	    while (!data.isAfterLast()) {
	  	      Reason reason =  ReasonDataSource.cursorToReason(data);
	  	      System.out.println("REASON: " + reason.toString());
				theoryController.addReason(reason);

	  	      data.moveToNext();
	  	    }
    	}
	}

	private void updateViews(Cursor data) {
		if( data != null && data.getCount() > 0 ) {
	    	data.moveToFirst();
	  	    while (!data.isAfterLast()) {
	  	      Reason reason =  ReasonDataSource.cursorToReason(data);
	  	      System.out.println("REASON: " + reason.toString());
				theoryController.addReason(reason);

	  	      data.moveToNext();
	  	    }
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

}
