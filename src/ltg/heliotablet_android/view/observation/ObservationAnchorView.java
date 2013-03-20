package ltg.heliotablet_android.view.observation;

import java.util.HashMap;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;

import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.MiscUtil;
import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.view.ICircleView;
import ltg.heliotablet_android.view.StyleCircleView;
import ltg.heliotablet_android.view.controller.OrderingViewData;
import ltg.heliotablet_android.view.theory.CircleView;
import ltg.heliotablet_android.view.theory.TheoryFragmentWithSQLiteLoaderNestFragments;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ObservationAnchorView extends CircleLayout implements ICircleView   {

	private String anchor;
	private HashMap<String, ObservationCircleView> flagToCircleView = new HashMap<String, ObservationCircleView>();
	private static  ImmutableMap<String, Drawable> DRAWABLES; 

	private static ImmutableSet<String> allFlags = ImmutableSet.of(Reason.CONST_BLUE,
			Reason.CONST_BROWN, Reason.CONST_GREEN, Reason.CONST_GREY,
			Reason.CONST_ORANGE, Reason.CONST_PINK, Reason.CONST_RED,
			Reason.CONST_YELLOW);
	private String showPopoverFlag = null;
	
	public ObservationAnchorView(Context context) {
		super(context);
	}
	
	public ObservationAnchorView(Context context, AttributeSet attrs) {
        super(context, attrs);
     
		createDrawableMap();
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
		StyleCircleView.styleView(this, anchor, getResources());
	}

	public boolean hasReadyOnlyFalse(String flag) {
		ObservationCircleView anchorView = flagToCircleView.get(flag);
		if( anchorView != null) {
			ImmutableSortedSet<Reason> editableReasons = ImmutableSortedSet
					.copyOf(Iterables.filter((ImmutableSortedSet<Reason>)anchorView.getTag(),
							Reason.getIsReadOnlyFalsePredicate()));
			return editableReasons.size() > 0;
		} else {
			return false;
		}
	}

	public void updateObservationCircleView(ImmutableSortedSet<Reason> imReasonSet) {

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
					ObservationCircleView anchorView = flagToCircleView.get(flag);
					Multiset<String> ms = HashMultiset.create();
					for(Reason reason : newSortedByReadOnlyReasonSet) {
						ms.add(reason.getReasonText());
					}
					
					
					// if null we are creating it for the first, no need to diff
					if (anchorView == null) {
						
						anchorView = (ObservationCircleView) LayoutInflater.from(getContext())
								.inflate(R.layout.observation_view_layout, this, false);

						anchorView.setFlag(flag);
						anchorView.setAnchor(this.anchor);
						flagToCircleView.put(flag, anchorView);
						this.addView(anchorView);

					}

					if( newIsReadonlyReasonSet.size() > 0 ) {
						styleCircleView(anchorView, flag, false);
					} else {
						styleCircleView(anchorView, flag, true);
					}
					// just replace

					
					anchorView.setReasonTextMultiSet(ms);
					anchorView.setTag(newSortedByReadOnlyReasonSet);
					anchorView.invalidate();
					this.invalidate();
					
					if( showPopoverFlag != null && flag.equals(flag)) {
						anchorView.showPopover();
						showPopoverFlag = null;
					}

				} else {
					ObservationCircleView circleView = flagToCircleView.get(flag);
					if( circleView != null ) {
						this.flagToCircleView.remove(flag);
						this.removeView(circleView);
						this.invalidate();
					}
				}

			}

		}
	
	private void styleCircleView(ObservationCircleView cv, String color, boolean isTransparent) {
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

	@Override
	public int getTextColor() {
		return 0;
	}

	@Override
	public void setTextColor(int textColor) {
	}

	public void clearFlagMap() {
		flagToCircleView = new HashMap<String, ObservationCircleView>();
	}

	public void showPopoverWithFlag(String flag) {
		this.showPopoverFlag   = flag;
	}
}