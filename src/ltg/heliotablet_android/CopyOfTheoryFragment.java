package ltg.heliotablet_android;


import ltg.heliotablet_android.view.PopoverView;
import ltg.heliotablet_android.view.PopoverView.PopoverViewDelegate;
import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
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

public class CopyOfTheoryFragment extends Fragment implements PopoverViewDelegate {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.theories_activity_vertical,
				container, false);
//		View earthView = view.findViewById(R.id.earthView);
//		// Draggable colored circles
//		FrameLayout planetColorsView = (FrameLayout) view
//				.findViewById(R.id.colors_include);
//
//		earthView.setOnDragListener(new MyDragListener());
//		// earthView.setOnTouchListener(new MyTouchListener());
//
//		// get all the colors and the
//		int childCount = planetColorsView.getChildCount();
//		for (int i = 0; i < childCount; i++) {
//			View color = planetColorsView.getChildAt(i);
//			color.setOnTouchListener(new MyTouchListener());
//			color.setOnDragListener(new MyDragListener());
//			System.out.println(color);
//			// do whatever you want to with the view
//		}

		return view;
	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
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

				if (targetView.equals(sourceView)) {

				} else {
					sourceView.removeView(dragged);
					LinearLayout container = (LinearLayout) targetView;
					LayoutParams layoutParams = dragged.getLayoutParams();
					layoutParams.height = 81;
					layoutParams.width = 80;

					RelativeLayout rel = (RelativeLayout) dragged;

					int childCount = rel.getChildCount();
					for (int i = 0; i < childCount; i++) {
						TextView textView = (TextView) rel.getChildAt(i);
						textView.setTextSize(15);
						// do whatever you want to with the view
					}

					
					final GestureDetector gestureDetector = new GestureDetector(CopyOfTheoryFragment.this.getActivity(), new GestureDetector.SimpleOnGestureListener() {
			            @Override
			            public boolean onDoubleTap(MotionEvent e) {
			                Log.i("ONDOUBLE", "On double");
			                return true;
			            }
			        });
					
					dragged.setOnTouchListener(new OnTouchListener() {
					        @Override
					        public boolean onTouch(View v, MotionEvent event) {
					        	gestureDetector.onTouchEvent(event);
					        	
//					        	RelativeLayout rootView = (RelativeLayout) ((View) v.getParent()).findViewById(R.layout.theories_activity_vertical);
//					        	
//					        	PopoverView popoverView = new PopoverView(TheoryFragment.this.getActivity(), R.layout.popover_showed_view);
//					    		popoverView.setContentSizeForViewInPopover(new Point(320, 340));
//					    		//popoverView.setDelegate(TheoryFragment.this.getActivity().g);
//					    		popoverView.showPopoverFromRectInViewGroup(rootView, PopoverView.getFrameForView(v), PopoverView.PopoverArrowDirectionAny, true);
					            return true;
					        }
					    });
					
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
