package ltg.heliotablet_android.view;


import ltg.heliotablet_android.R;
import ltg.heliotablet_android.view.PopoverView.PopoverViewDelegate;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

public class CircleView extends RelativeLayout implements PopoverViewDelegate  {

	private GestureDetector gestureDetector;
	
	public CircleView(Context context) {
		super(context);
		init();
	}
	
	public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

	private void init() {
	}
 
	public void initGestures() {
		
		
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
			   
			   @Override
			public boolean onDoubleTap(MotionEvent e) {
				   Log.i("CLICK", "double tap event DOUBLE TAP");
				   float x = e.getX();
			        float y = e.getY();

			        Log.i("Double Tap", "Tapped at: (" + x + "," + y + ")");

			        ViewParent parent = CircleView.this.getParent().getParent().getParent();
			        
			        
//			        /RelativeLayout rootView = (RelativeLayout)findViewById(R.id.rootLayout);
					
					PopoverView popoverView = new PopoverView(CircleView.this.getContext(), R.layout.activity_screen_slide);
					popoverView.setContentSizeForViewInPopover(new Point(500, 340));
					popoverView.setDelegate(CircleView.this);
					popoverView.showPopoverFromRectInViewGroup((ViewGroup) parent, PopoverView.getFrameForView(CircleView.this), PopoverView.PopoverArrowDirectionAny, true);
			        
			        

				   
				return super.onDoubleTap(e);
			}
		   });
		
		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
	}
	
	@Override
	public void popoverViewWillShow(PopoverView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popoverViewDidShow(PopoverView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popoverViewWillDismiss(PopoverView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popoverViewDidDismiss(PopoverView view) {
		// TODO Auto-generated method stub
		
	}
}
