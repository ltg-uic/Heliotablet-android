package ltg.heliotablet_android.view.theory;

import java.util.HashMap;

import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.view.controller.OrderingViewData;
import ltg.heliotablet_android.view.observation.ObservationCircleView;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;

public class TheoryPlanetView extends LinearLayout {

	private String anchor;
	private HashMap<String, CircleView> flagToCircleView = new HashMap<String, CircleView>();
	private  boolean hasVoted = false;
	
	private static  ImmutableMap<String, Drawable> DRAWABLES; 

	private static ImmutableSet<String> allFlags = ImmutableSet.of(Reason.CONST_BLUE,
			Reason.CONST_BROWN, Reason.CONST_GREEN, Reason.CONST_GREY,
			Reason.CONST_ORANGE, Reason.CONST_PINK, Reason.CONST_RED,
			Reason.CONST_YELLOW);
	private String showPopoverFlag = null;
	   
	public TheoryPlanetView(Context context) {
		super(context);
	}

	public TheoryPlanetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TheoryPlanetView);
		this.setAnchor(a.getString(R.styleable.TheoryPlanetView_anchor));
		a.recycle();
		
		createDrawableMap();
	}
	
	public void resetTheoryView() {
		flagToCircleView = new HashMap<String, CircleView>();
		this.removeAllViews();
		this.invalidate();
		hasVoted = false;
	}
	
	private void createDrawableMap() {
		Resources resources = getResources();
		DRAWABLES =
		       new ImmutableMap.Builder<String, Drawable>()
		           .put(Reason.CONST_RED, resources.getDrawable(R.drawable.earth_shape))
		           .put(Reason.CONST_RED+"_d", resources.getDrawable(R.drawable.earth_shape_d))
		           .put(Reason.CONST_BLUE, resources.getDrawable(R.drawable.neptune_shape))
		           .put(Reason.CONST_BLUE+"_d", resources.getDrawable(R.drawable.neptune_shape_d))
		           .put(Reason.CONST_BROWN, resources.getDrawable(R.drawable.mercury_shape))
		           .put(Reason.CONST_BROWN+"_d", resources.getDrawable(R.drawable.mercury_shape_d))
		           .put(Reason.CONST_YELLOW, resources.getDrawable(R.drawable.saturn_shape))
		           .put(Reason.CONST_YELLOW+"_d", resources.getDrawable(R.drawable.saturn_shape_d))
		           .put(Reason.CONST_PINK, resources.getDrawable(R.drawable.venus_shape))
		           .put(Reason.CONST_PINK+"_d", resources.getDrawable(R.drawable.venus_shape_d))
		           .put(Reason.CONST_GREEN, resources.getDrawable(R.drawable.jupiter_shape))
		           .put(Reason.CONST_GREEN+"_d", resources.getDrawable(R.drawable.jupiter_shape_d))
		           .put(Reason.CONST_GREY, resources.getDrawable(R.drawable.mars_shape))
		           .put(Reason.CONST_GREY+"_d", resources.getDrawable(R.drawable.mars_shape_d))
		           .put(Reason.CONST_ORANGE, resources.getDrawable(R.drawable.uranus_shape))
		           .put(Reason.CONST_ORANGE+"_d", resources.getDrawable(R.drawable.uranus_shape_d))
		           .build();

		}
	
	
	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	
	public void checkCircleView(String flag) {
		CircleView circleView = flagToCircleView.get(flag);
		
		ImmutableSortedSet<Reason> reasons =(ImmutableSortedSet<Reason>) circleView.getTag();;
		if( reasons.size() <= 1 ) {
			flagToCircleView.remove(flag);
		}
	}

	public void updateCircleView(ImmutableSortedSet<Reason> imReasonSet) {
		
		for (String flag : allFlags) {

			// filter for each COLOR
			ImmutableSortedSet<Reason> newSortedByFlagReasonSet = ImmutableSortedSet
					.copyOf(Iterables.filter(imReasonSet,
							Reason.getFlagPredicate(flag)));

			if (!newSortedByFlagReasonSet.isEmpty()) {

				ImmutableSortedSet<Reason> newSortedByReadOnlyReasonSet = ImmutableSortedSet
						.orderedBy(OrderingViewData.isReadOnlyOrdering.reverse())
						.addAll(newSortedByFlagReasonSet).build();
				
				ImmutableSortedSet<Reason> newIsReadonlyReasonSet = ImmutableSortedSet
						.copyOf(Iterables.filter(newSortedByReadOnlyReasonSet,
								Reason.getIsReadOnlyFalsePredicate()));

				
					
				// true is first, we want false first
				// check if it was there already
				CircleView circleView = flagToCircleView.get(flag);

				// if null we are creating it for the first, no need to diff
				if (circleView == null) {
					circleView = (CircleView) LayoutInflater.from(getContext())
							.inflate(R.layout.circle_view_layout, this, false);

					
					circleView.setFlag(flag);
					circleView.setAnchor(this.anchor);
					flagToCircleView.put(flag, circleView);
					this.addView(circleView);

				}

				if( newIsReadonlyReasonSet.size() > 0 ) {
					styleCircleView(circleView, flag, false);
					MainActivity ma = (MainActivity) this.getContext();
					ma.addUsedPlanetColor(circleView.getFlag());
				} else {
					styleCircleView(circleView, flag, true);
				}
				
				circleView.setTag(newSortedByReadOnlyReasonSet);
				circleView.invalidate();
				this.invalidate();
				
				if( showPopoverFlag != null && flag.equals(flag)) {
					circleView.showPopover();
					showPopoverFlag = null;
				}
				

			} else {
				CircleView circleView = flagToCircleView.get(flag);
				if( circleView != null ) {
					this.flagToCircleView.remove(flag);
					this.removeView(circleView);
					this.invalidate();
				}
			}

		}

	}

	

	private void styleCircleView(CircleView cv, String color, boolean isTransparent) {
		Resources resources = getResources();
		Drawable drawable = null;

		int textColor = 0;
		int textColorWhite = resources.getColor(R.color.White);
		int textColorBlack = resources.getColor(R.color.Black);

		if (color.equals(Reason.CONST_RED)) {
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_RED);
			else
				drawable = DRAWABLES.get(Reason.CONST_RED+"_d");
			
			textColor = textColorWhite;
		} else if (color.equals(Reason.CONST_BLUE)) {
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_BLUE);
			else
				drawable = DRAWABLES.get(Reason.CONST_BLUE+"_d");
			
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_BROWN)) {
			
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_BROWN);
			else
				drawable = DRAWABLES.get(Reason.CONST_BROWN+"_d");
			
			textColor = textColorWhite;
		} else if (color.equals(Reason.CONST_YELLOW)) {
			
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_YELLOW);
			else
				drawable = DRAWABLES.get(Reason.CONST_YELLOW+"_d");
			
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_PINK)) {
			
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_PINK);
			else
				drawable = DRAWABLES.get(Reason.CONST_PINK+"_d");
			
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_GREEN)) {
			
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_GREEN);
			else
				drawable = DRAWABLES.get(Reason.CONST_GREEN+"_d");
			
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_GREY)) {
			
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_GREY);
			else
				drawable = DRAWABLES.get(Reason.CONST_GREY+"_d");
			
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_ORANGE)) {
			
			if( isTransparent == false )
				drawable = DRAWABLES.get(Reason.CONST_ORANGE);
			else
				drawable = DRAWABLES.get(Reason.CONST_ORANGE+"_d");
			
			textColor = textColorBlack;

		}
		cv.setTextColor(textColor);
		cv.setBackground(drawable);

	}
	
	public void removeFlagFromCircleViewMap(String flag) {
		flagToCircleView.remove(flag);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("anchor", this.anchor)
				.add("flagToCircleView", this.flagToCircleView).toString();
	}

	public void clearFlagMap() {
		flagToCircleView = new HashMap<String, CircleView>();		
	}

	public void showPopover(String popOverToShowFlag) {
		CircleView cv = flagToCircleView.get(popOverToShowFlag);
		if( cv != null ) {
			cv.showPopover();
		}
		
	}

	public boolean isHasVoted() {
		return hasVoted;
	}

	public void setHasVoted(boolean hasVoted) {
		this.hasVoted = hasVoted;
	}

}
