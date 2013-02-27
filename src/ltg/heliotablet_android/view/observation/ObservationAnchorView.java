package ltg.heliotablet_android.view.observation;

import java.util.HashMap;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.view.ICircleView;
import ltg.heliotablet_android.view.StyleCircleView;
import ltg.heliotablet_android.view.controller.OrderingViewData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ObservationAnchorView extends CircleLayout implements ICircleView   {

	private String anchor;
	private HashMap<String, ObservationCircleView> flagToCircleView = new HashMap<String, ObservationCircleView>();

	private ImmutableSet<String> allFlags = ImmutableSet.of(Reason.CONST_BLUE,
			Reason.CONST_BROWN, Reason.CONST_GREEN, Reason.CONST_GREY,
			Reason.CONST_ORANGE, Reason.CONST_PINK, Reason.CONST_RED,
			Reason.CONST_YELLOW);
	
	public ObservationAnchorView(Context context) {
		super(context);
	}
	
	public ObservationAnchorView(Context context, AttributeSet attrs) {
        super(context, attrs);
     
       
    }

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
		StyleCircleView.styleView(this, anchor, getResources());
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

					// if null we are creating it for the first, no need to diff
					if (anchorView == null) {
						
						anchorView = (ObservationCircleView) LayoutInflater.from(getContext())
								.inflate(R.layout.observation_view_layout, this, false);

						StyleCircleView.styleView((ICircleView)anchorView, flag, getResources());
						anchorView.setFlag(flag);
						anchorView.setAnchor(this.anchor);
						flagToCircleView.put(flag, anchorView);
						this.addView(anchorView);

					}

					if( newIsReadonlyReasonSet.size() > 0 )
						anchorView.makeTransparent(false);
					else
						anchorView.makeTransparent(true);
					// just replace

					Multiset<String> ms = HashMultiset.create();
					for(Reason reason : newSortedByReadOnlyReasonSet) {
						ms.add(reason.getReasonText());
					}
					anchorView.setReasonTextMultiSet(ms);
					anchorView.setTag(newSortedByReadOnlyReasonSet);
					anchorView.invalidate();
					this.invalidate();

				}

			}

		}

	@Override
	public int getTextColor() {
		return 0;
	}

	@Override
	public void setTextColor(int textColor) {
	}
		
}