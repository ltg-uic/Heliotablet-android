package ltg.heliotablet_android.view;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;

public class CircleViewDefaultTouchListener implements OnTouchListener {
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
			// view.setVisibility(View.VISIBLE);
			break;
		}
		return true;
	}
}