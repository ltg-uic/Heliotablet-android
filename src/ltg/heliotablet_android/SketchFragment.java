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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SketchFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.sketch_activity, container, false);
		// Draggable colored circles
		RelativeLayout planetColorsView = (RelativeLayout) view
				.findViewById(R.id.planetSketchView);

		// get all the colors and the
		int childCount = planetColorsView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View sketchItem = planetColorsView.getChildAt(i);
			sketchItem.setOnTouchListener(new SketchTouchListener());
//			sketchItem.setOnDragListener(new SketchDragListener());
		}

		return view;
	}

	private final class SketchTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.VISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}

	class SketchDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				break;
			case DragEvent.ACTION_DRAG_ENDED:

				float x = event.getX();
				float y = event.getY();
				
		        View view = (View) event.getLocalState();
		        view.setX(x);
		        view.setY(y);

			default:
				break;
			}
			return true;
		}
	}

}
