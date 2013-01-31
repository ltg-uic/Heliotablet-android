package ltg.heliotablet_android;

import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.PopoverView;
import ltg.heliotablet_android.view.PopoverView.PopoverViewDelegate;
import android.content.ClipData;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TheoryFragment extends Fragment {

	private View mainLayoutView;
	private View slideScreen;
	private RelativeLayout viewPagerLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainLayoutView = inflater.inflate(R.layout.theories_activity_vertical,
				container, false);
		viewPagerLayout = (RelativeLayout) inflater.inflate(
				R.layout.activity_screen_slide, container, false);
		View earthView = mainLayoutView.findViewById(R.id.earthView);
		View neptuneView = mainLayoutView.findViewById(R.id.neptuneView);
		View mercuryView = mainLayoutView.findViewById(R.id.mercuryView);
		// Draggable colored circles
		FrameLayout planetColorsView = (FrameLayout) mainLayoutView
				.findViewById(R.id.colors_include);

		earthView.setOnDragListener(new TargetViewDragListener());
		neptuneView.setOnDragListener(new TargetViewDragListener());
		mercuryView.setOnDragListener(new TargetViewDragListener());
		// earthView.setOnTouchListener(new MyTouchListener());

		// get all the colors and the
		int childCount = planetColorsView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View color = planetColorsView.getChildAt(i);
			color.setOnTouchListener(new CustomViewTouchListener());
			// color.setOnDragListener(new MyDragListener());
			System.out.println(color);
			// do whatever you want to with the view
		}

		Button button = (Button) earthView.findViewById(R.id.testButton);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("onClickButton", "test button");

				ViewPager pager = new ViewPager(TheoryFragment.this
						.getActivity());

				ViewPager vPager = (ViewPager) viewPagerLayout
						.findViewById(R.id.pager);

				vPager.setAdapter(new PagerAdapter() {

					@Override
					public boolean isViewFromObject(View arg0, Object arg1) {
						return arg0 == arg1;
					}

					@Override
					public int getCount() {
						return 2;
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * android.support.v4.view.PagerAdapter#instantiateItem(
					 * android.view.ViewGroup, int)
					 */
					@Override
					public Object instantiateItem(ViewGroup container,
							int position) {

						if (position == 0) {
							View view = View.inflate(container.getContext(),
									R.layout.popover_view, null);
							container.addView(view, LayoutParams.FILL_PARENT,
									LayoutParams.FILL_PARENT);
							return view;
						} else {
							View view = View.inflate(container.getContext(),
									R.layout.fragment_screen_slide_page, null);
							container.addView(view, LayoutParams.FILL_PARENT,
									LayoutParams.FILL_PARENT);
							return view;
						}
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * android.support.v4.view.PagerAdapter#destroyItem(android
					 * .view.ViewGroup, int, java.lang.Object)
					 */
					@Override
					public void destroyItem(ViewGroup container, int position,
							Object object) {
						container.removeView((View) object);
					}

				});

				PopoverView popoverView = new PopoverView(TheoryFragment.this
						.getActivity(), viewPagerLayout);
				popoverView.setContentSizeForViewInPopover(new Point(400, 400));
				popoverView.setDelegate(new PopoverViewDelegate() {

					@Override
					public void popoverViewWillShow(PopoverView view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void popoverViewWillDismiss(PopoverView view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void popoverViewDidShow(PopoverView view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void popoverViewDidDismiss(PopoverView view) {
						// TODO Auto-generated method stub

					}
				});
				popoverView.showPopoverFromRectInViewGroup(
						(ViewGroup) mainLayoutView.getParent(),
						PopoverView.getFrameForView(v),
						PopoverView.PopoverArrowDirectionAny, true);

			}
		});

		return mainLayoutView;
	}

	private final class CustomViewTouchListener implements OnTouchListener {
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

	class TargetViewDragListener implements OnDragListener {

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
					Log.i("DRAGE", "there equal");
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

					container.addView(dragged);
					dragged.setVisibility(View.VISIBLE);
					dragged.setOnTouchListener(null);
					CircleView cv = (CircleView) dragged;
					cv.initGestures();
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			case DragEvent.ACTION_DRAG_LOCATION:
				View v = (View) event.getLocalState();
				v.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			return true;
		}
	}

}
