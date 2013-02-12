package ltg.heliotablet_android.view;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.R;
import ltg.heliotablet_android.R.color;
import ltg.heliotablet_android.TheoryFragmentWithSQLiteLoader;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.data.ReasonDataSource;
import ltg.heliotablet_android.view.PopoverView.PopoverViewDelegate;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

public class CircleView extends RelativeLayout implements PopoverViewDelegate  {

	private GestureDetector gestureDetector;
	private String flag;
	private int textColor;
	private TextView reasonTextView;
	private String type;
	private ImmutableSortedSet<Reason> imReasons;

	private RelativeLayout viewPagerLayout;
	private Reason reasonNeedsUpdate;
	
	
	public CircleView(Context context) {
		super(context);
	}
	
	public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.circle_view, this, true);
        reasonTextView = (TextView) getChildAt(0);

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        this.flag = a.getString(R.styleable.CircleView_flag);
        this.setTextColor(a.getColor(R.styleable.CircleView_textColor, color.White));
        a.recycle();
        
      
        
        
    	viewPagerLayout = (RelativeLayout) inflater.inflate(
				R.layout.activity_screen_slide, this, false);
    	
    	
        reasonTextView.setTextColor(this.getTextColor());
        this.enableDoubleTap();
    }
	
	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		
		imReasons = (ImmutableSortedSet<Reason>) tag;
		
		if( imReasons != null) {
			this.reasonTextView.setText(""+ imReasons.size());
		}
		
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
	    	
	    	showPopover(imReasons);
	        return super.onDoubleTap(e);
	    }
	}
	
	public void showPopover(ImmutableSortedSet<Reason> popOverReasonSet) {
		
		ArrayList<View> pages = new ArrayList<View>();
		ViewPager pager = new ViewPager(getContext());
		ViewPager vPager = (ViewPager) viewPagerLayout
				.findViewById(R.id.pager);
		
		for (Reason reason : popOverReasonSet) {
			View layout = View.inflate(getContext(),
					R.layout.popover_view, null);
			layout.setTag(reason);
			
			final EditText editText = (EditText) layout.findViewById(R.id.mainEditText);
			
			editText.setText(reason.getReasonText());
			
			if( reason.isReadonly()) {
				editText.setKeyListener(null);
				editText.setBackgroundColor(color.reasonEditTextColor);
			} else {
				editText.setFocusable(true);
			    editText.setFocusableInTouchMode(true);
	            editText.setClickable(true);
				editText.requestFocus();
			}

			pages.add(layout);
		}
		
		vPager.setAdapter(new PopoverViewAdapter(pages));
		vPager.setOffscreenPageLimit(5);
		vPager.setCurrentItem(0);
        ViewGroup parent = (ViewGroup) this.getParent().getParent().getParent();

		
		PopoverView popoverView = new PopoverView(getContext(), viewPagerLayout);
		popoverView.setContentSizeForViewInPopover(new Point(370, 220));
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
		ViewPager vPager = (ViewPager) view.findViewById(R.id.pager);
		PopoverViewAdapter adapter = (PopoverViewAdapter) vPager.getAdapter();
		EditText editText = (EditText) adapter.findViewById(0, R.id.mainEditText);
		editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
		editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
		editText.requestFocus();
		editText.setSelection(editText.getText().length());
	}

	@Override
	public void popoverViewWillDismiss(PopoverView view) {
		
		ViewPager vPager = (ViewPager) view.findViewById(R.id.pager);
		PopoverViewAdapter adapter = (PopoverViewAdapter) vPager.getAdapter();
		EditText editText = (EditText) adapter.findViewById(0, R.id.mainEditText);
		
		InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		
		String newReasonText = editText.getText().toString();
		
		reasonNeedsUpdate = null;
		
		if( !Strings.isNullOrEmpty(newReasonText) ) {
			View reasonView = adapter.getView(0);
			Reason viewReason = (Reason) reasonView.getTag();
			
			//if it has been updated
			if( !viewReason.equals(newReasonText)) {
				reasonNeedsUpdate = Reason.newInstance(viewReason);
				reasonNeedsUpdate.setReasonText(editText.getText().toString());
			}
		}

		

	}

	@Override
	public void popoverViewDidDismiss(PopoverView view) {
		
		
		//lets update
		if( reasonNeedsUpdate != null ) {
			
			
			Activity mainActivity = (Activity) CircleView.this.getContext();
			LoaderManager loaderManager = mainActivity.getLoaderManager();
			
			//find the loader
			TheoryFragmentWithSQLiteLoader tf = (TheoryFragmentWithSQLiteLoader) mainActivity.getFragmentManager().findFragmentByTag(getContext().getString(R.string.fragment_tag_theory));
			Loader<Cursor> loader = tf.getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_LOADER_ID);
			SQLiteCursorLoader updateLoader = (SQLiteCursorLoader)loader;
			
			String[] args= { String.valueOf(reasonNeedsUpdate.getId()) };

			ContentValues reasonContentValues = ReasonDataSource.getReasonContentValues(reasonNeedsUpdate);  
			updateLoader.update(ReasonDBOpenHelper.TABLE_REASON, reasonContentValues, "_id=?", args);
			

		}
		
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

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	    reasonTextView.setTextColor(textColor);
	}

	public void setReducedAlpha(float circleViewAlpha) {
		Drawable background = getBackground();
		background.setAlpha((int) circleViewAlpha);
		setBackground(background);
		reasonTextView.setAlpha(circleViewAlpha);
	}
	
	@Override
	public String toString() {
		return "Flag: " + this.getFlag() + " Reasons: " + imReasons.toString();
	}
	
}


