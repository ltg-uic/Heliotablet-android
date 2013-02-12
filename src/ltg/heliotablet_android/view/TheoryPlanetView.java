package ltg.heliotablet_android.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class TheoryPlanetView extends LinearLayout {

	private String anchor;
	private HashMap<String, CircleView> flagToCircleView = new HashMap<String, CircleView>();
	private HashMap<String, Boolean> flagToTransparent = new HashMap<String, Boolean>();
	private float circleViewAlpha = 150f;
	
	ImmutableSet<String> allFlags = ImmutableSet.of(Reason.CONST_BLUE, Reason.CONST_BROWN, Reason.CONST_GREEN, Reason.CONST_GREY, Reason.CONST_ORANGE, Reason.CONST_PINK, Reason.CONST_RED, Reason.CONST_YELLOW);

	
	public TheoryPlanetView(Context context) {
		super(context);
	}

	public TheoryPlanetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.TheoryPlanetView);
        this.setAnchor(a.getString(R.styleable.TheoryPlanetView_anchor));
        a.recycle();
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	
	static final Comparator<Reason> ISREADYONLY_ORDER = new Comparator<Reason>() {
		public int compare(Reason r1, Reason r2) {
			return (r2.isReadonly() == r1.isReadonly() ? 0 : 1);
		}
	};

	public void updateCircleView(ImmutableSortedSet<Reason> imReasonSet) {
		
		
		for (String flag : allFlags) {
			
			//filter for each COLOR
			ImmutableSortedSet<Reason> newUnsortedReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(imReasonSet, Reason.getFlagPredicate(flag)));
			
			
			
			if(!newUnsortedReasonSet.isEmpty()) {

				
		
				//now sort by isReadOnly
				ImmutableSortedSet<Reason> newSortedReasonSet = ImmutableSortedSet.<Reason>orderedBy(ISREADYONLY_ORDER).addAll(newUnsortedReasonSet).build();
				
				 ImmutableSortedSet<Reason> reverseOrder = newSortedReasonSet.descendingSet();
				//true is first, we want false first
				//check if it was there already
				CircleView circleView = flagToCircleView.get(flag);
				
				//if null we are creating it for the first, no need to diff
				if( circleView == null) {
					circleView = (CircleView) LayoutInflater.from(getContext()).inflate(R.layout.circle_view_layout, this, false);
					flagToCircleView.put(flag, circleView);
					styleCircleView(circleView,flag, false);
					 
					this.addView(circleView);
					
				}
				
				//just replace
				
				circleView.setTag(reverseOrder);
				this.invalidate();
				
				
				
				
				
			}
			
		
		}
		
	}
	
	public void updateCircleView(Reason reason){
		
		//it all ready exists
		String flag = reason.getFlag();
		
		if(flagToCircleView.containsKey(flag)) {
			
			CircleView circleView = flagToCircleView.get(flag);
			
			ArrayList<Reason> reasons = (ArrayList<Reason>) circleView.getTag();
			
			Iterable matches = Iterables.filter(reasons, Reason.getIdPredicate(reason.getId()));
			System.out.println("Matches");
			
			Reason oldReason = null;
			for (Iterator<Reason> r = matches.iterator(); r.hasNext();) { 
		        Reason next = r.next();
		        oldReason = next;
		    } 
			
			if( oldReason !=  null) {
				reasons.remove(oldReason);
			}
			
			reasons.add(reason);
			//check to see if it should be dimmed
			boolean isTransparent = true;
			for (Reason r : reasons) {
				if(r.isReadonly() == false) {
					isTransparent = false;
				}
			}
			
			if( isTransparent ) {
				circleView.setReducedAlpha(circleViewAlpha);
				circleView.invalidate();
			} else {
				circleView.setReducedAlpha(255);
				circleView.invalidate();
			}
			
			circleView.setTag(reasons);
			
			this.invalidate();
			
		} else {
			//Create a new circle view
			
			CircleView cv = (CircleView) LayoutInflater.from(getContext()).inflate(R.layout.circle_view_layout, this, false);
			
			styleCircleView(cv,flag, reason.isReadonly());
			
			List<Reason> arrayList = new ArrayList<Reason>();
			arrayList.add(reason);
			
			cv.setTag(arrayList);
			cv.enableDoubleTap();
			
			//order the views
			
			flagToCircleView.put(flag, cv);
			
			 
			this.addView(cv);
			this.invalidate();
			
		}
	}

	private void styleCircleView(CircleView cv, String color, boolean isTransparent) {
		Resources resources = getResources();
		Drawable drawable = null;
		
		int textColor = 0;
		int textColorWhite = resources.getColor(R.color.White);
		int textColorBlack = resources.getColor(R.color.Black);
		
		if( color.equals(Reason.CONST_RED)) {
			drawable = resources.getDrawable(R.drawable.earth_shape);
			textColor = textColorWhite;
		} else if( color.equals(Reason.CONST_BLUE)) {
			drawable = resources.getDrawable(R.drawable.neptune_shape);
			textColor = textColorBlack;
		} else if( color.equals(Reason.CONST_BROWN)) {
			drawable = resources.getDrawable(R.drawable.mercury_shape);
			textColor = textColorWhite;
		} else if( color.equals(Reason.CONST_YELLOW)) {
			drawable = resources.getDrawable(R.drawable.saturn_shape);
			textColor = textColorBlack;
		} else if( color.equals(Reason.CONST_PINK)) {	
			drawable = resources.getDrawable(R.drawable.venus_shape);
			textColor = textColorBlack;
		} else if( color.equals(Reason.CONST_GREEN)) {
			drawable = resources.getDrawable(R.drawable.jupiter_shape);
			textColor = textColorBlack;
		} else if( color.equals(Reason.CONST_GREY)) {
			drawable = resources.getDrawable(R.drawable.mars_shape);
			textColor = textColorBlack;
		} else if( color.equals(Reason.CONST_ORANGE)) {
			drawable = resources.getDrawable(R.drawable.uranus_shape);
			textColor = textColorBlack;
			
		}
		
		
		cv.setTextColor(textColor);
		cv.setBackground(drawable);
		
		if( isTransparent ) {
			cv.setReducedAlpha(circleViewAlpha);
			//cv.invalidate();
		}
	}
	
	@Override
	public String toString() {
			return Objects.toStringHelper(this).add("anchor", this.anchor)
					.add("flagToCircleView", this.flagToCircleView)
					.toString();
	}

}
