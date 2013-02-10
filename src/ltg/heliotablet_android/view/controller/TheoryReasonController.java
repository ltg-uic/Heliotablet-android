package ltg.heliotablet_android.view.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.TheoryPlanetView;
import android.content.Context;

public class TheoryReasonController {

	private static TheoryReasonController tInstance;
	private Context context;
	private ReasonDataSource reasonDatasource;
	private HashMap<String, TheoryPlanetView> theoryViewsToAnchors;
	private ArrayList<Reason> cachedReasons = new ArrayList<Reason>();

	private TheoryReasonController(Context context) {
		this.context = context;
//		reasonDatasource = ReasonDataSource.getInstance(context);
//		reasonDatasource.open();
//		
//		createTestData();
	}
	
	public static TheoryReasonController getInstance(Context context) {
		if (tInstance == null) {
			tInstance = new TheoryReasonController(context);
		}
		return tInstance;
	}
	
	public void createTestData() {
		Reason earthRed1 = new Reason();
		earthRed1.setAnchor(Reason.CONST_EARTH);
		earthRed1.setFlag(Reason.CONST_RED);
		earthRed1.setOrigin("bob");
		earthRed1.setReasonText("Earth is red be it sucks");
		earthRed1.setType(Reason.TYPE_THEORY);
		earthRed1.setReadonly(true);
		
		reasonDatasource.createReason(earthRed1);
		
		Reason earthRed2 = new Reason();
		
		earthRed2.setAnchor(Reason.CONST_EARTH);
		earthRed2.setFlag(Reason.CONST_RED);
		earthRed2.setOrigin("tony");
		earthRed2.setReasonText("because its the biggest");
		earthRed2.setType(Reason.TYPE_THEORY);
		earthRed2.setReadonly(false);
		
		reasonDatasource.createReason(earthRed2);
		
		Reason marsORANGE = new Reason();
		
		marsORANGE.setAnchor(Reason.CONST_MARS);
		marsORANGE.setFlag(Reason.CONST_ORANGE);
		marsORANGE.setOrigin("tony");
		marsORANGE.setReasonText("YEAH YEAH ");
		marsORANGE.setType(Reason.TYPE_THEORY);
		marsORANGE.setReadonly(true);
		
		reasonDatasource.createReason(marsORANGE);
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
			for (Iterator<Reason> r = matches.iterator(); r.hasNext();) { 
				System.out.println("Matches");
		        Reason next = r.next();
		        
		        if( reason.compareTo(next) == 1 ) {
		        	cachedReasons.remove(next);
		        	cachedReasons.add(reason);
					String anchor = reason.getAnchor();
					TheoryPlanetView theoryPlanetView = theoryViewsToAnchors
							.get(anchor);
					theoryPlanetView.updateCircleView(reason);
		        }
		        
		        
		        found = true;
		    } 

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
	
	
}
