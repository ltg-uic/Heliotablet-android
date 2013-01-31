package ltg.heliotablet_android;

import java.sql.Timestamp;
import java.util.Calendar;

import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.CircleView;
import ltg.heliotablet_android.view.PopoverView;
import ltg.heliotablet_android.view.PopoverView.PopoverViewDelegate;
import ltg.heliotablet_android.view.TheoryPlanetView;
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

	private ReasonDataSource reasonDatasource;
	
	private ViewGroup theoriesView;
	private View slideScreen;
	private RelativeLayout viewPagerLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		reasonDatasource = new ReasonDataSource(this.getActivity());
		reasonDatasource.open();
		
		theoriesView = (ViewGroup) inflater.inflate(R.layout.theories_activity_vertical,
				container, false);
		viewPagerLayout = (RelativeLayout) inflater.inflate(
				R.layout.activity_screen_slide, container, false);
		
		//add listeners to all the draggable planetViews
		// get all the colors and the
		int childCount = theoriesView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View possibleView = theoriesView.getChildAt(i);
			if( possibleView instanceof TheoryPlanetView ) {
				possibleView.setOnDragListener(new TargetViewDragListener());
			}
		}

		ViewGroup planetColorsView = (FrameLayout) theoriesView.findViewById(R.id.colors_include);
		// get all the colors and the
		childCount = planetColorsView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View color = planetColorsView.getChildAt(i);
			color.setOnTouchListener(new CustomViewTouchListener());
		}

		View earthView = theoriesView.findViewById(R.id.earthView);

		Button button = (Button) earthView.findViewById(R.id.testButton);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("onClickButton", "test button");
				
				Reason reason = new Reason();
				reason.setAnchor("test");
				reason.setType("dfdf");
				reason.setOrigin("otdfd");
				Calendar calendar = Calendar.getInstance();
				reason.setLastTimestamp(new Timestamp(calendar.getTime().getTime()));
				reason.setReasonText("fuck u");
				
				Long long1 = reasonDatasource.createReason(reason);
				System.out.println("REason id" + long1 );

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
						(ViewGroup) theoriesView.getParent(),
						PopoverView.getFrameForView(v),
						PopoverView.PopoverArrowDirectionAny, true);

			}
		});

		return theoriesView;
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

					CircleView cv = (CircleView) dragged;
					Log.i("COLOR", cv.getFlag());
					cv.initGestures();
					
					container.addView(dragged);
					dragged.setVisibility(View.VISIBLE);
					dragged.setOnTouchListener(null);
					
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
	
	
	
	@Override
	public void onResume() {
		reasonDatasource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		reasonDatasource.close();
		super.onPause();
	}

}
