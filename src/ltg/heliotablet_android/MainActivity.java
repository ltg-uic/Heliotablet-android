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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setupTabs(); 		
	}

	private void setupTabs() {
		// TODO Auto-generated method stub
		
		//Theories tab
		
		
		Tab tab = actionBar.newTab()
							.setText(R.string.tab_title_theories)
							.setTabListener(new TabListener<TheoryFragmentWithSQLiteLoaderNestFragments>(this, getString(R.string.fragment_tag_theory), TheoryFragmentWithSQLiteLoaderNestFragments.class));
		actionBar.addTab(tab);
		
		tab = actionBar.newTab()
				.setText(R.string.tab_title_observations)
				.setTabListener(new TabListener<ObservationFragment>(this, getString(R.string.fragment_tag_observation), ObservationFragment.class));
		    
		actionBar.addTab(tab);
		tab = actionBar.newTab()
				.setText(R.string.tab_title_scratch_pad)
				.setTabListener(new TabListener<ScratchPadFragment>(this, getString(R.string.fragment_tag_scratchpad), ScratchPadFragment.class));
		    
		actionBar.addTab(tab);
		
	}
	
	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private Fragment currentFragment;


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
				//ft.commit();
			} else {
				
				
				// If it exists, simply attach it in order to show it
				ft.show(mFragment);
				//ft.commit();
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				//ft.detach(mFragment);
				ft.hide(mFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

	}

	
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
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
