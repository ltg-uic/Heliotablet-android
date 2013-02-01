package ltg.heliotablet_android.view;


import java.util.ArrayList;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.R.color;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.view.PopoverView.PopoverViewDelegate;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CircleView extends RelativeLayout implements PopoverViewDelegate  {

	private GestureDetector gestureDetector;
	private String flag;
	private int textColor;
	private TextView reasonTextView;
	private String type;
	private ArrayList<Reason> reasons = new ArrayList<Reason>();

	private RelativeLayout viewPagerLayout;
	
	
	public CircleView(Context context) {
		super(context);
	}
	
	public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        this.flag = a.getString(R.styleable.CircleView_flag);
        this.textColor = a.getColor(R.styleable.CircleView_textColor, color.White);
        a.recycle();
        
      
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.circle_view, this, true);
        
    	viewPagerLayout = (RelativeLayout) inflater.inflate(
				R.layout.activity_screen_slide, this, false);
    	
    	
        reasonTextView = (TextView) getChildAt(0);
        reasonTextView.setTextColor(this.textColor);
    }
	
	public void enableDoubleTap() {
        gestureDetector = new GestureDetector(getContext(), new CircleDoubleTapGestureListener());
        this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 gestureDetector.onTouchEvent(event);
				 return true;
			}
		}); 
	}
	
	class CircleDoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
	    // event when double tap occurs
	    @Override
	    public boolean onDoubleTap(MotionEvent e) {
	    	showPopover(reasons);
	        return super.onDoubleTap(e);
	    }
	}
	
	public void showPopover(ArrayList<Reason> reasons) {
		ViewPager pager = new ViewPager(getContext());

		ViewPager vPager = (ViewPager) viewPagerLayout
				.findViewById(R.id.pager);
		View view1 = View.inflate(getContext(),
				R.layout.popover_view, null);
		View view2 = View.inflate(getContext(),R.layout.fragment_screen_slide_page, null);
		
		ArrayList<View> pages = new ArrayList<View>();
		pages.add(view1);
		pages.add(view2);
		vPager.setAdapter(new PopoverViewAdapter(pages));
        ViewGroup parent = (ViewGroup) this.getParent().getParent().getParent();

		
		PopoverView popoverView = new PopoverView(getContext(), viewPagerLayout);
		popoverView.setContentSizeForViewInPopover(new Point(400, 400));
		popoverView.setDelegate(this);
		popoverView.showPopoverFromRectInViewGroup(
				parent,
				PopoverView.getFrameForView(this),
				PopoverView.PopoverArrowDirectionAny, true);
		
	}
	
	@Override
	public void popoverViewWillShow(PopoverView view) {
	}

	@Override
	public void popoverViewDidShow(PopoverView view) {
	}

	@Override
	public void popoverViewWillDismiss(PopoverView view) {
	}

	@Override
	public void popoverViewDidDismiss(PopoverView view) {
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
	
}


