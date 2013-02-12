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
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors;
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

	public void setTheoryViewsToAnchors(
			HashMap<String, TheoryPlanetView> theoryViewsToAnchors) {
		this.theoryViewsToAnchors = theoryViewsToAnchors;
		
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

	
	public void addReason(Reason reason) {
		
		if(cachedReasons.isEmpty()) {
			cachedReasons.add(reason);
			String anchor = reason.getAnchor();
			TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(anchor);
			theoryPlanetView.updateCircleView(reason);
		} else {
			
			Iterable matches = Iterables.filter(cachedReasons, Reason.getIdPredicate(reason.getId()));
			
			
			
			boolean found = false;
			Reason oldReason = null;
			for (Iterator<Reason> r = matches.iterator(); r.hasNext();) { 
				System.out.println("Matches");
		        Reason next = r.next();
		    
		        if( reason.compareTo(next) == 0 ) {
		        	oldReason = next;
		        	cachedReasons.add(reason);
					String anchor = reason.getAnchor();
					TheoryPlanetView theoryPlanetView = theoryViewsToAnchors
							.get(anchor);
					theoryPlanetView.updateCircleView(reason);
		        }
		        
		        
		        found = true;
		    } 

//			if( oldReason != null )
//				cachedReasons.remove(oldReason);
			
			if (!found) {
				cachedReasons.add(reason);
				TheoryPlanetView theoryPlanetView = theoryViewsToAnchors
						.get(reason.getAnchor());
				theoryPlanetView.updateCircleView(reason);
			}
			
		}
		
		
		
	}
	
	public void addReason(String anchor, String flag, boolean isReadOnly) {
		TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(anchor);
		
		Reason reason = new Reason();
		reason.setType(Reason.TYPE_THEORY);
		reason.setAnchor(anchor);
		reason.setFlag(flag);
		reason.setReadonly(isReadOnly);
		reason.setOrigin("GET FROM PREFENCES");
		
		Reason createReason = reasonDatasource.createReason(reason);
		
		theoryPlanetView.updateCircleView(createReason);
		
		
	}

	public void updateViews(List<Reason> allReasons) {
		
		 //Break the list up into chunks by ANCHOR aka PLANET NAME
		
							
		for (String anchor : allAnchors) {
			ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(anchor)));
			TheoryPlanetView theoryPlanetView = theoryViewsToAnchors.get(anchor);
			theoryPlanetView.updateCircleView(imReasonSet);
		}
		
		
		
		
		// TODO Auto-generated method stub
		
	}
	
	
}
