package ltg.heliotablet_android.view;

import com.google.common.collect.ImmutableSortedSet;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.R.color;
import ltg.heliotablet_android.data.Reason;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ObservationCircleView extends RelativeLayout implements ICircleView {

	private String flag;
	private int textColor;
	private TextView reasonTextView;
	private ImmutableSortedSet<Reason> imReasons;
	private String anchor;
	
	public ObservationCircleView(Context context) {
		super(context);
	}
	
	public ObservationCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.circle_view, this, true);
        reasonTextView = (TextView) getChildAt(0);
        
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        this.flag = a.getString(R.styleable.CircleView_flag);
        this.setTextColor(a.getColor(R.styleable.CircleView_textColor, color.White));
        a.recycle();
        
       // this.setLayoutParams(new RelativeLayout.LayoutParams(77,77));
        reasonTextView.setTextColor(this.getTextColor());
    }
	
	public int getReasonsSize() {
		if( imReasons != null ){
			imReasons.size();
		}
		return 0;
	}
	
	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		
		imReasons = (ImmutableSortedSet<Reason>) tag;
		
		if( imReasons != null) {
			this.reasonTextView.setText(""+ imReasons.size());
		}
		
	}

	public void makeToast(String toastText) {
		Toast toast = Toast.makeText(getContext(), toastText,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
		toast.show();
	}
	
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public void setReasonText(String reasonText) {
		this.reasonTextView.setText(reasonText);
	}
	
	public String getReasonText() {
		return this.reasonTextView.getText().toString();
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	    reasonTextView.setTextColor(textColor);
	}

	public void setReducedAlpha(float circleViewAlpha) {
		Drawable background = getBackground();
		background.setAlpha((int) circleViewAlpha);
		setBackground(background);
		reasonTextView.setAlpha(circleViewAlpha);
	}
	
	protected void makeTransparent(boolean isTransparent) {
		if( isTransparent )
			this.setReducedAlpha(100f);
		else
			this.setReducedAlpha(255f);
		
	}
	
	@Override
	public String toString() {
		return "Flag: " + this.getFlag() + " Reasons: " + imReasons.toString();
	}
	
	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}


}