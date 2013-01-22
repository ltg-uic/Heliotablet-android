package ltg.heliotablet_android;



import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TheoryFragment extends Fragment {
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		  

		View view = inflater.inflate(R.layout.theories_activity, container, false);
		View earthView = view.findViewById(R.id.earthView);
		//Draggable colored circles
		RelativeLayout planetColorsView = (RelativeLayout) view.findViewById(R.id.colors_include);
		
		
		earthView.setOnDragListener(new MyDragListener());
		//earthView.setOnTouchListener(new MyTouchListener());
		
		//get all the colors and the
		int childCount = planetColorsView.getChildCount();
		for(int i = 0; i < childCount; i++) {
		    View color = planetColorsView.getChildAt(i);
		    color.setOnTouchListener(new MyTouchListener());
		    color.setOnDragListener(new MyDragListener());
		    System.out.println(color);
		    // do whatever you want to with the view
		}
				
		return view;
	}
	
	 private final class MyTouchListener implements OnTouchListener {
		    public boolean onTouch(View view, MotionEvent event) {
		    	
		    	switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					ClipData data = ClipData.newPlainText("", "");
			        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			        view.startDrag(data, shadowBuilder, view, 0);
			        view.setVisibility(View.INVISIBLE);
					break;

				case MotionEvent.ACTION_UP:
			        view.setVisibility(View.VISIBLE);
					break;
				}	
		    	return true;
		    }
		  }
	 

	 class MyDragListener implements OnDragListener {
		   
		    @Override
		    public boolean onDrag(View targetView, DragEvent event) {
		      int action = event.getAction();
		      switch (event.getAction()) {
		      case DragEvent.ACTION_DRAG_STARTED:
		        // Do nothing
		        break;
		      case DragEvent.ACTION_DRAG_ENTERED:
		        break;
		      case DragEvent.ACTION_DRAG_EXITED:
		        break;
		      case DragEvent.ACTION_DROP:
		    	  
		    	  
		    	  
		        // Dropped, reassign View to ViewGroup
		        View dragged = (View) event.getLocalState();
		        ViewGroup sourceView = (ViewGroup) dragged.getParent();
		        
		        if( targetView.equals(sourceView) ) {
		        	
		        } else {
		        sourceView.removeView(dragged);
		        LinearLayout container = (LinearLayout) targetView;
		        LayoutParams layoutParams = dragged.getLayoutParams();
		        layoutParams.height = 65;
		        layoutParams.width = 65;
		        
		        RelativeLayout rel = (RelativeLayout) dragged;
		        
		        int childCount = rel.getChildCount();
				for(int i = 0; i < childCount; i++) {
				    TextView textView = (TextView) rel.getChildAt(i);
				    textView.setTextSize(15);
				    // do whatever you want to with the view
				}
		        
		        
		        container.addView(dragged);
		        dragged.setVisibility(View.VISIBLE);
		        }
		        break;
		      case DragEvent.ACTION_DRAG_ENDED:
		      default:
		        break;
		      }
		      return true;
		    }
		  }
	


		

	
	
}
