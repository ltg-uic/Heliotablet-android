package ltg.heliotablet_android.view.controller;

import java.util.HashMap;
import java.util.List;

import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.theory.CircleView;
import ltg.heliotablet_android.view.theory.TheoryFragmentWithSQLiteLoaderNestFragments;
import ltg.heliotablet_android.view.theory.TheoryPlanetView;
import ltg.heliotablet_android.view.theory.TheoryViewFragment;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.View;

import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;

public class TheoryReasonController extends ReasonController {

	private static TheoryReasonController tInstance;
	
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();
	
	private TheoryReasonController(Context context) {
		super(context);
	}
	
	public static TheoryReasonController getInstance(Context context) {
		if (tInstance == null) {
			tInstance = new TheoryReasonController(context);
		}
		return tInstance;
	}
	
	
	public void add(String anchor, TheoryPlanetView theoryview) {
		this.theoryViewsToAnchors.put(anchor, theoryview);
	}

	public void setTheoryViewsToAnchors(
			HashMap<String, TheoryPlanetView> theoryViewsToAnchors) {
		this.theoryViewsToAnchors = theoryViewsToAnchors;
		
	}
	
	public HashMap<String, TheoryPlanetView> getTheoryViewsToAnchors() {
		return theoryViewsToAnchors;
	}
	
	public void operationTheory(Reason reason, String anchor, String command) throws NullPointerException {
		FragmentActivity mainActivity = (FragmentActivity)context;
		
		//find the loader
		Fragment findFragmentByTag = mainActivity.getSupportFragmentManager().findFragmentByTag(anchor+"_THEORY");
		if(findFragmentByTag == null)
			return;
		
		//find the loader
		TheoryViewFragment tf = (TheoryViewFragment)findFragmentByTag;
		tf.dbOperation(reason, command);
	}
	
	public void showPlanetColor(String flag, int visible) {
		MainActivity act = (MainActivity)context;
		TheoryFragmentWithSQLiteLoaderNestFragments fragment = (TheoryFragmentWithSQLiteLoaderNestFragments) act.getSectionsPagerAdapter().getItem(0);
	}
	
}
