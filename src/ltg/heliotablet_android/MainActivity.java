package ltg.heliotablet_android;

import ltg.heliotablet_android.view.controller.TheoryReasonController;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity  {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private TheoryReasonController theoryController;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		theoryController = TheoryReasonController.getInstance(this);

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	
				
		setupTabs(); 		
	}

	private void setupTabs() {
		// TODO Auto-generated method stub
		
		//Theories tab
		
		
		Tab tab = actionBar.newTab()
							.setText(R.string.tab_title_theories)
							.setTabListener(new TabListener<TheoryFragment>(this, "theories", TheoryFragment.class));
		actionBar.addTab(tab);
		
		tab = actionBar.newTab()
				.setText(R.string.tab_title_observations)
				.setTabListener(new TabListener<ObservationFragment>(this, "observations", ObservationFragment.class));
		    
		actionBar.addTab(tab);
		
		tab = actionBar.newTab()
				.setText(R.string.tab_title_scratch_pad)
				.setTabListener(new TabListener<ScratchPadFragment>(this, "scratchpad", ScratchPadFragment.class));
		    
		actionBar.addTab(tab);
		
//		theoriesTab.set
//		theoriesTab.setTabListener(this);
//		actionBar.addTab(fragment, true);

		
		
		// For each of the sections in the app, add a tab to the action bar.
//				actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
//						.setTabListener(this));
//				actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
//						.setTabListener(this));
//				actionBar.addTab(actionBar.newTab().setText(R.string.title_section3)
//						.setTabListener(this));

		
	}
	
	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

	}


//	@Override
//	public void onTabSelected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//		
//		// When the given tab is selected, switch to the corresponding page in
//		// the ViewPager.
//		int position = tab.getPosition();
//		if( position == 0) {
//			Fragment fragment = new TheoryFragment();
//			Bundle args = new Bundle();
//			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//			fragment.setArguments(args);
//			getSupportFragmentManager().beginTransaction()
//			.replace(R.id.container, fragment).commit();
//		} else if( position == 2) {
//				Fragment fragment = new SketchFragment();
//				Bundle args = new Bundle();
//				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//				fragment.setArguments(args);
//				getSupportFragmentManager().beginTransaction()
//				.replace(R.id.container, fragment).commit();
//		} else {
//			Fragment fragment = new DummySectionFragment();
//			Bundle args = new Bundle();
//			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//			fragment.setArguments(args);
//			getSupportFragmentManager().beginTransaction()
//			.replace(R.id.container, fragment).commit();
//		}
//	}
//
//	@Override
//	public void onTabUnselected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//	}
//
//	@Override
//	public void onTabReselected(ActionBar.Tab tab,
//			FragmentTransaction fragmentTransaction) {
//		
//	}

//	/**
//	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//	 * one of the sections/tabs/pages.
//	 */
//	public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//		public SectionsPagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int position) {
//			// getItem is called to instantiate the fragment for the given page.
//			// Return a DummySectionFragment (defined as a static inner class
//			// below) with the page number as its lone argument.
//			
//			if( position == 0) {
//				Fragment fragment = new TheoryFragment();
//				Bundle args = new Bundle();
//				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//				fragment.setArguments(args);
//				return fragment;
//			} else {
//				Fragment fragment = new DummySectionFragment();
//				Bundle args = new Bundle();
//				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//				fragment.setArguments(args);
//				return fragment;
//			}
//			
//		
//		}
//
//		@Override
//		public int getCount() {
//			// Show 3 total pages.
//			return 4;
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			switch (position) {
//			case 0:
//				return getString(R.string.tab_title_theories).toUpperCase();
//			case 1:
//				return getString(R.string.tab_title_observations).toUpperCase();
//			case 2:
//				return getString(R.string.tab_title_scratch_pad).toUpperCase();
//			case 3:
//				return getString(R.string.title_section4).toUpperCase();	
//			}
//			return null;
//		}
//	}
//
//	/**
//	 * A dummy fragment representing a section of the app, but that simply
//	 * displays dummy text.
//	 */
//	public static class DummySectionFragment extends Fragment {
//		/**
//		 * The fragment argument representing the section number for this
//		 * fragment.
//		 */
//		public static final String ARG_SECTION_NUMBER = "section_number";
//
//		public DummySectionFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			// Create a new TextView and set its text to the fragment's section
//			// number argument value.
//			TextView textView = new TextView(getActivity());
//			textView.setGravity(Gravity.CENTER);
//			textView.setText(Integer.toString(getArguments().getInt(
//					ARG_SECTION_NUMBER)));
//			
//			
//			
//			return textView;
//		}
//	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}
	
	private void showXMPPLogin() {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		
        View view = inflater.inflate(R.layout.authenticator_activity, null);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("XMPP Login");
        builder.setView(view);
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create();
        builder.show();
   	 	System.out.println("XMPP !!!!");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_settings:
	        showXMPPLogin();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onResume() {
		theoryController.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		theoryController.close();
		super.onPause();
	}

}
