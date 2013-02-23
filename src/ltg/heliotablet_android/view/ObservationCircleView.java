package ltg.heliotablet_android.view;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.view.controller.TheoryReasonController;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.ImmutableSortedSet;

public class ObservationCircleView extends CircleLayout   {

	private String anchor;
	
	public ObservationCircleView(Context context) {
		super(context);
	}
	
	public ObservationCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
       
        
       
    }

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
		styleView();
	}

	private void styleView() {
			Resources resources = getResources();
			Drawable drawable = null;

			int textColorWhite = resources.getColor(R.color.White);
			int textColorBlack = resources.getColor(R.color.Black);

			if (anchor.equals(Reason.CONST_RED)) {
				drawable = resources.getDrawable(R.drawable.earth_shape);
			} else if (anchor.equals(Reason.CONST_BLUE)) {
				drawable = resources.getDrawable(R.drawable.neptune_shape);
			} else if (anchor.equals(Reason.CONST_BROWN)) {
				drawable = resources.getDrawable(R.drawable.mercury_shape);
			} else if (anchor.equals(Reason.CONST_YELLOW)) {
				drawable = resources.getDrawable(R.drawable.saturn_shape);
			} else if (anchor.equals(Reason.CONST_PINK)) {
				drawable = resources.getDrawable(R.drawable.venus_shape);
			} else if (anchor.equals(Reason.CONST_GREEN)) {
				drawable = resources.getDrawable(R.drawable.jupiter_shape);
			} else if (anchor.equals(Reason.CONST_GREY)) {
				drawable = resources.getDrawable(R.drawable.mars_shape);
			} else if (anchor.equals(Reason.CONST_ORANGE)) {
				drawable = resources.getDrawable(R.drawable.uranus_shape);
			}

			//this.setTextColor(textColor);
			this.setBackground(drawable);

	}
	
}