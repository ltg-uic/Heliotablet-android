package ltg.heliotablet_android.view;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;

public class ObservationAnchorView extends CircleLayout   {

	private String anchor;
	
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
		styleView();
	}

	private void styleView() {
			Resources resources = getResources();
			LayerDrawable drawable = null;

			int textColorWhite = resources.getColor(R.color.White);
			int textColorBlack = resources.getColor(R.color.Black);

			if (anchor.equals(Reason.CONST_RED)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.earth_shape);
			} else if (anchor.equals(Reason.CONST_BLUE)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.neptune_shape);
			} else if (anchor.equals(Reason.CONST_BROWN)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.mercury_shape);
			} else if (anchor.equals(Reason.CONST_YELLOW)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.saturn_shape);
			} else if (anchor.equals(Reason.CONST_PINK)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.venus_shape);
			} else if (anchor.equals(Reason.CONST_GREEN)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.jupiter_shape);
			} else if (anchor.equals(Reason.CONST_GREY)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.mars_shape);
			} else if (anchor.equals(Reason.CONST_ORANGE)) {
				drawable = (LayerDrawable) resources.getDrawable(R.drawable.uranus_shape);
			}

			//this.setTextColor(textColor);
			this.setBackground(drawable);

	}
	
}