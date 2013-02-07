package ltg.heliotablet_android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ScratchPadFragment extends Fragment {

	int dx;
	int dy;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.sketch_activity, container, false);
		
		FrameLayout sketchView = (FrameLayout) view
				.findViewById(R.id.planetSketchView);

		int childCount = sketchView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View sketchItem = sketchView.getChildAt(i);
			sketchItem.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					drag(event, v);
					return true;
				}
			});
		}
		return view;
	}


	public void drag(MotionEvent event, View v) {

		FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) v
				.getLayoutParams();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dx = (int) event.getX();
			dy = (int) event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v
					.getLayoutParams();
			int left = lp.leftMargin + (x - dx);
			int top = lp.topMargin + (y - dy);
			lp.leftMargin = left;
			lp.topMargin = top;
			v.setLayoutParams(lp);
			break;
		}
	}

}
