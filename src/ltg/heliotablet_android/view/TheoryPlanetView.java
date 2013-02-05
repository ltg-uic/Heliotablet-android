package ltg.heliotablet_android.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.R.color;
import ltg.heliotablet_android.data.Reason;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TheoryPlanetView extends LinearLayout {

	private String anchor;
	private HashMap<String, CircleView> flagToCircleView = new HashMap<String, CircleView>();
	
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
	
	public void updateCircleView(Reason reason){
		
		//it all ready exists
		if(flagToCircleView.containsKey(reason.getFlag())) {
			
			CircleView circleView = flagToCircleView.get(reason.getFlag());
			
			ArrayList<Reason> tag = (ArrayList<Reason>) circleView.getTag();
			tag.add(reason);
			
			circleView.setTag(tag);
			
		} else {
			//Create a new circle view
			
			CircleView cv = (CircleView) LayoutInflater.from(getContext()).inflate(R.layout.circle_view_layout, this, false);
			
			styleCircleView(cv,reason.getFlag());
			
			List<Reason> arrayList = new ArrayList<Reason>();
			arrayList.add(reason);
			
			cv.setTag(arrayList);
			cv.enableDoubleTap();
			//order the views
			
			flagToCircleView.put(reason.getFlag(), cv);
			
			 
			this.addView(cv);
			
	
			this.invalidate();
			
		}
	}

	private void styleCircleView(CircleView cv, String color) {
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
			textColor = textColorWhite;
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
	}

}
