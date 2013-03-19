package ltg.heliotablet_android.view.theory;

import java.util.HashMap;

import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.view.controller.OrderingViewData;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

public class TheoryPlanetView extends LinearLayout {

	private String anchor;
	private HashMap<String, CircleView> flagToCircleView = new HashMap<String, CircleView>();

	private ImmutableSet<String> allFlags = ImmutableSet.of(Reason.CONST_BLUE,
			Reason.CONST_BROWN, Reason.CONST_GREEN, Reason.CONST_GREY,
			Reason.CONST_ORANGE, Reason.CONST_PINK, Reason.CONST_RED,
			Reason.CONST_YELLOW);
	   
	public TheoryPlanetView(Context context) {
		super(context);
	}

	public TheoryPlanetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TheoryPlanetView);
		this.setAnchor(a.getString(R.styleable.TheoryPlanetView_anchor));
		a.recycle();
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

					styleCircleView(circleView, flag);
					circleView.setFlag(flag);
					circleView.setAnchor(this.anchor);
					flagToCircleView.put(flag, circleView);
					this.addView(circleView);

				}

				if( newIsReadonlyReasonSet.size() > 0 ) {
					circleView.makeTransparent(false);
					MainActivity act = (MainActivity) this.getContext();
					TheoryFragmentWithSQLiteLoaderNestFragments fragment = (TheoryFragmentWithSQLiteLoaderNestFragments) act.getSectionsPagerAdapter().getItem(0);
					fragment.showPlanetColor(flag,View.INVISIBLE);
				} else
					circleView.makeTransparent(true);
				// just replace

				
				circleView.setTag(newSortedByReadOnlyReasonSet);
				circleView.invalidate();
				this.invalidate();

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

	

	private void styleCircleView(CircleView cv, String color) {
		Resources resources = getResources();
		Drawable drawable = null;

		int textColor = 0;
		int textColorWhite = resources.getColor(R.color.White);
		int textColorBlack = resources.getColor(R.color.Black);

		if (color.equals(Reason.CONST_RED)) {
			drawable = resources.getDrawable(R.drawable.earth_shape);
			textColor = textColorWhite;
		} else if (color.equals(Reason.CONST_BLUE)) {
			drawable = resources.getDrawable(R.drawable.neptune_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_BROWN)) {
			drawable = resources.getDrawable(R.drawable.mercury_shape);
			textColor = textColorWhite;
		} else if (color.equals(Reason.CONST_YELLOW)) {
			drawable = resources.getDrawable(R.drawable.saturn_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_PINK)) {
			drawable = resources.getDrawable(R.drawable.venus_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_GREEN)) {
			drawable = resources.getDrawable(R.drawable.jupiter_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_GREY)) {
			drawable = resources.getDrawable(R.drawable.mars_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_ORANGE)) {
			drawable = resources.getDrawable(R.drawable.uranus_shape);
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

}
