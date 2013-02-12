package ltg.heliotablet_android.view.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.TheoryPlanetView;
import android.content.Context;

public class TheoryReasonController {

	private static TheoryReasonController tInstance;
	private Context context;
	private ReasonDataSource reasonDatasource;
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();
	private ArrayList<Reason> cachedReasons = new ArrayList<Reason>();
	ImmutableSet<String> allAnchors = ImmutableSet.of(Reason.CONST_MERCURY, Reason.CONST_VENUS, Reason.CONST_EARTH, Reason.CONST_MARS, Reason.CONST_JUPITER, Reason.CONST_SATURN, Reason.CONST_NEPTUNE, Reason.CONST_URANUS);
	
	
	private TheoryReasonController(Context context) {
		this.context = context;
	}
	
	public static TheoryReasonController getInstance(Context context) {
		if (tInstance == null) {
			tInstance = new TheoryReasonController(context);
		}
		return tInstance;
	}
	
	public void open() {
		reasonDatasource.open();
	}

	public void close() {
		reasonDatasource.close();
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

	public void populateViews() {
		
		ArrayList<Reason> allReasons = (ArrayList<Reason>) reasonDatasource.getAllReasons();
		
	    Iterator<Entry<String, TheoryPlanetView>> entries =
	            theoryViewsToAnchors.entrySet().iterator();
	        while (entries.hasNext()) {
	            Entry<String, TheoryPlanetView> entry = entries.next();
	            System.out.println("Key = " + entry.getKey() + ", Value = "+ entry.getValue());
	            
	            TheoryPlanetView tv = entry.getValue();
	            
	            String tvAnchor = tv.getAnchor();
	            
	            for (Reason reason : allReasons) {
	            	if( reason.getAnchor().equals(tvAnchor)) {
	            		tv.updateCircleView(reason);
	            	}
				}
	            
	            
		}
	}

	public void updateViews(List<Reason> allReasons, String anchor) {
			ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(anchor)));
			TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(anchor);
			if( !imReasonSet.isEmpty() ) {
				theoryPlanetView.updateCircleView(imReasonSet);
		}
		
	}
	
	
}
