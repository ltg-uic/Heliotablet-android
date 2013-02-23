package ltg.heliotablet_android.view;

import ltg.heliotablet_android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class ObservationCircleView extends RelativeLayout {

	private String flag;

	public ObservationCircleView(Context context) {
		super(context);
	}
	
	public ObservationCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.circle_view, this, true);
//
//        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleView);
//        this.flag = a.getString(R.styleable.CircleView_flag);
//        //this.setTextColor(a.getColor(R.styleable.CircleView_textColor, color.White));
//        a.recycle();
        
        this.setLayoutParams(new RelativeLayout.LayoutParams(77,77));
        
    }

}