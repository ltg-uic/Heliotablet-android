package ltg.heliotablet_android.view;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.R.color;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TheoryPlanetView extends LinearLayout {

	private String anchor;
	
	public TheoryPlanetView(Context context) {
		super(context);
	}

	public TheoryPlanetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        this.setAnchor(a.getString(R.styleable.TheoryPlanetView_anchor));
        a.recycle();
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

}
