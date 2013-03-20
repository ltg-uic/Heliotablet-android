package ltg.heliotablet_android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ltg.commons.LTGEvent;
import ltg.heliotablet_android.data.Note;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.LoginDialog;
import ltg.heliotablet_android.view.controller.NonSwipeableViewPager;
import ltg.heliotablet_android.view.observation.ObservationFragment;
import ltg.heliotablet_android.view.observation.ObservationViewFragment;
import ltg.heliotablet_android.view.theory.CircleView;
import ltg.heliotablet_android.view.theory.TheoryFragmentWithSQLiteLoaderNestFragments;
import ltg.heliotablet_android.view.theory.TheoryViewFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class MainActivity extends FragmentActivity implements TabListener,
		FragmentCommunicator {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private ActionBar actionBar;

	private static final String TAG = "MainActivity";
	public static String UPDATE_THEORY = "update_theory";
	public static String REMOVE_THEORY = "remove_theory";
	public static String NEW_THEORY = "new_theory";

	public static String NEW_OBSERVATION = "new_observation";
	public static String REMOVE_OBSERVATION = "remove_observation";
	public static String UPDATE_OBSERVATION = "update_observation";

	private Messenger activityMessenger;
	private MenuItem initMenu;
	private MenuItem connectMenu;
	private MenuItem disconnectMenu;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private NonSwipeableViewPager mViewPager;
	public FragmentCommunicator fragmentCommunicator;
	public ReasonDBOpenHelper dbHelper;

	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			Intent intent = (Intent) message.obj;
			if (intent != null) {
				if (intent.getAction().equals(
						XmppService.CHAT_ACTION_RECEIVED_MESSAGE)) {
					receiveIntent(intent);
				} else if (intent.getAction().equals(XmppService.SHOW_LOGIN)) {
					prepDialog().show();
				} else if (intent.getAction().equals(XmppService.ERROR)) {
					makeToast(intent);
				} else if (intent.getAction().equals(
						XmppService.LTG_EVENT_RECEIVED)) {
					receiveIntent(intent);
				}
			}

		};
	};

	public void createReasonIntent(Reason reason, String eventType) {
		SharedPreferences settings = null;

		if (eventType.contains("theory") || eventType.contains("observation")) {

			String origin = reason.getOrigin();
			if (origin == null) {
				settings = getSharedPreferences(getString(R.string.xmpp_prefs),
						MODE_PRIVATE);
				origin = settings.getString(getString(R.string.user_name), "");
			}

			LTGEvent event = new LTGEvent(eventType, origin, null,
					reason.toJSON());

			sendIntent(event);

		}

	}

	public void sendIntent(LTGEvent event) {
		Intent intent = new Intent();
		intent.setAction(XmppService.SEND_GROUP_MESSAGE);
		intent.putExtra(XmppService.LTG_EVENT_SENT, event);
		Message newMessage = Message.obtain();
		newMessage.obj = intent;
		XmppService.sendToServiceHandler(intent);

	}

	public void sendInitMessage() {
		new InitAsyncTask().execute();
	}

	private class InitAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			List<Reason> allReasons = dbHelper.getAllReasons();

			SharedPreferences settings = getSharedPreferences(
					getString(R.string.xmpp_prefs), MODE_PRIVATE);
			String userName = settings.getString(getString(R.string.user_name),
					"");

			StringBuilder message = new StringBuilder();

			ArrayNode notes = JsonNodeFactory.instance.arrayNode();

			for (Reason reason : allReasons) {
				notes.add(reason.toJSON());
			}

			ObjectNode response = JsonNodeFactory.instance.objectNode();

			response.put("notes", notes);

			LTGEvent ltgEvent = new LTGEvent("init_helio", userName, null,
					response);
			sendIntent(ltgEvent);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			makeToast("Init task run!!!");
		}
	}

	public void processNote(Reason n, String command) {
		if( n.getType().equals(Reason.TYPE_THEORY) ) {
			operationTheory(n, n.getAnchor(), command, false);
		} else if( n.getType().equals(Reason.TYPE_OBSERVATION) ) {
			operationObservation(n, n.getAnchor(), command, false);
		}
		
	}
	
	public void receiveIntent(Intent intent) {
		if (intent != null) {
			LTGEvent ltgEvent = (LTGEvent) intent
					.getSerializableExtra(XmppService.LTG_EVENT);

			SharedPreferences settings = getSharedPreferences(
					getString(R.string.xmpp_prefs), MODE_PRIVATE);

			String storedUserName = settings.getString(
					getString(R.string.user_name), "");

			if (ltgEvent.getOrigin().toLowerCase()
					.equals(storedUserName.toLowerCase())) {
				return;
			}

			if (ltgEvent != null) {
				if (ltgEvent.getPayload() != null) {
					JsonNode payload = ltgEvent.getPayload();
					if (ltgEvent.getType().equals("init_helio_diff")) {
						ArrayNode jsonNode = (ArrayNode) payload
								.get("additions");
						List<Reason> additions = new ArrayList<Reason>();
						for (JsonNode n : jsonNode) {
							
							Reason r = new Reason(n);
							if( storedUserName.equals(r.getOrigin())) {
								r.setReadonly(false);
							} else {
								r.setReadonly(true);
							}
							
							processNote(r, "insert");
							//additions.add(r);
						}

						jsonNode = (ArrayNode) payload.get("deletions");
						List<Reason> deletions = new ArrayList<Reason>();
						for (JsonNode n : jsonNode) {
							Reason r = new Reason(n);
							processNote(r, "remove");
//							deletions.add(r);
						}

						
						
						dbHelper.getAllReasonsDump();
//						if (!additions.isEmpty()) {
//							new InsertTask().execute(additions);
//						}
//
//						if (!deletions.isEmpty()) {
//							new DeleteTask().execute(deletions);
//						}
					} else {

						String color = payload.get("color").textValue();
						String anchor = payload.get("anchor").textValue();
						String reasonText = payload.get("reason").textValue();
						String origin = ltgEvent.getOrigin();
						if (ltgEvent.getType().equals("new_theory")) {

							Reason reason = new Reason(anchor, color,
									Reason.TYPE_THEORY, origin, true);
							reason.setReasonText(reasonText);
							this.operationTheory(reason, anchor, "insert", false);
							makeToast("theory insert anchor" + anchor
									+ "color: " + color);
						} else if (ltgEvent.getType().equals("new_observation")) {
							Reason reason = new Reason(anchor, color,
									Reason.TYPE_OBSERVATION, origin, true);
							reason.setReasonText(reasonText);

							this.operationObservation(reason, anchor, "insert", false);
							makeToast("obs new anchor" + anchor + "color: "
									+ color);
						} else if (ltgEvent.getType().equals("update_theory")) {

							Reason reason = new Reason(anchor, color,
									Reason.TYPE_THEORY, origin, true);
							reason.setReasonText(reasonText);
							this.operationTheory(reason, anchor, "update", false);

							makeToast("theory update anchor" + anchor
									+ "color: " + color);
						} else if (ltgEvent.getType().equals(
								"update_observation")) {
							Reason reason = new Reason(anchor, color,
									Reason.TYPE_OBSERVATION, origin, true);
							reason.setReasonText(reasonText);
							this.operationObservation(reason, anchor, "update", false);
							makeToast("obs update anchor" + anchor + "color: "
									+ color);

						} else if (ltgEvent.getType().equals("remove_theory")) {

							Reason reason = new Reason(anchor, color,
									Reason.TYPE_THEORY, origin, true);
							// tc.deleteReasonByOriginAndType(reason);
							try {
								this.operationTheory(reason, anchor, "remove", false);
							} catch (NullPointerException e) {
								makeToast("bad data on remove theory" + anchor
										+ "color: " + color);
							}
							makeToast("deleted theory anchor" + anchor
									+ "color: " + color);

						} else if (ltgEvent.getType().equals(
								"remove_observation")) {

							Reason reason = new Reason(anchor, color,
									Reason.TYPE_OBSERVATION, origin, true);
							// oc.deleteReasonByOriginAndType(reason);
							this.operationObservation(reason, anchor, "remove", false);
							makeToast("deleted observation anchor" + anchor
									+ "color: " + color);

						}
					}

				}

			}
			// makeToast("LTG EVENT Received: " + ltgEvent.toString());

		}
	}

	

	private class InsertTask extends AsyncTask<List<Reason>, Integer, String> {
		private String localOrigin;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			SharedPreferences settings = getSharedPreferences(
					getString(R.string.xmpp_prefs), MODE_PRIVATE);
			localOrigin = settings.getString(getString(R.string.user_name), "");
		}

		@Override
		protected String doInBackground(List<Reason>... params) {
			List<Reason> notes = params[0];

			for (Reason note : notes) {
				ContentValues reasonContentValues = ReasonDBOpenHelper
						.getReasonContentValues(note, localOrigin);
				dbHelper.insertReason(reasonContentValues);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			makeToast("INSERT INIT DIFF DONE!");
		}
	}

	private class DeleteTask extends AsyncTask<List<Reason>, Integer, String> {

		private String localOrigin = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			SharedPreferences settings = getSharedPreferences(
					getString(R.string.xmpp_prefs), MODE_PRIVATE);
			localOrigin = settings.getString(getString(R.string.user_name), "");
		}

		@Override
		protected String doInBackground(List<Reason>... params) {
			List<Reason> notes = params[0];

			for (Reason note : notes) {
				dbHelper.deleteReason(note);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			makeToast("DELETE INIT DIFF DONE!");
		}
	}

	
	private static ImmutableSet<String> allColors = ImmutableSet.of(Reason.CONST_BLUE,
			Reason.CONST_BROWN, Reason.CONST_GREEN, Reason.CONST_GREY,
			Reason.CONST_ORANGE, Reason.CONST_PINK, Reason.CONST_RED,
			Reason.CONST_YELLOW);
	
	private static ImmutableSet<String> allPlanets = ImmutableSet.of(Reason.CONST_EARTH,
			Reason.CONST_NEPTUNE, Reason.CONST_URANUS, Reason.CONST_VENUS,
			Reason.CONST_JUPITER, Reason.CONST_MERCURY, Reason.CONST_SATURN,
			Reason.CONST_MARS);
	
	
	private void resetDB() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				
//				for (String planet : allPlanets) {
//					operationTheory(null, null, "removeAll", true);
//				} 
//				
//				for (String planet : allColors) {
//					operationObservation(null, null, "removeAll", true);
//				} 
				
				dbHelper.deleteAll();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				makeToast("DELETE ALL DB DONE");
			}
		}.execute();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbHelper = ReasonDBOpenHelper.getInstance(MainActivity.this);

		setContentView(R.layout.activity_main);
		hardcodedUserNameXMPP();
		initXmppService();

		initTabs();
	}

	public void initTabs() {
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		setSectionsPagerAdapter(new SectionsPagerAdapter(
				getSupportFragmentManager()));

		// Set up the ViewPager with the sections adapter.
		setFragmentViewPager((NonSwipeableViewPager) findViewById(R.id.main_pager));
		getFragmentViewPager().setOffscreenPageLimit(3);
		getFragmentViewPager().setAdapter(getSectionsPagerAdapter());
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		// mViewPager
		// .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		// @Override
		// public void onPageSelected(int position) {
		// actionBar.setSelectedNavigationItem(position);
		// }
		// });

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < getSectionsPagerAdapter().getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(getSectionsPagerAdapter().getPageTitle(i))
					.setTabListener(this));
		}
	}

	public void initXmppService() {
		// XMPP bind
		activityMessenger = new Messenger(handler);
		Intent intent = new Intent(MainActivity.this, XmppService.class);
		intent.setAction(XmppService.STARTUP);
		intent.putExtra(XmppService.ACTIVITY_MESSAGER, activityMessenger);
		intent.putExtra(XmppService.CHAT_TYPE, XmppService.GROUP_CHAT);
		intent.putExtra(XmppService.GROUP_CHAT_NAME,
				getString(R.string.XMPP_CHAT_ROOM));
		startService(intent);
	}

	public AlertDialog prepDialog() {
		OnClickListener negative = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		};

		OnClickListener positive = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				AlertDialog ad = (AlertDialog) dialog;

				EditText usernameTextView = (EditText) ad
						.findViewById(R.id.usernameTextView);
				EditText passwordTextView = (EditText) ad
						.findViewById(R.id.passwordTextView);

				String username = org.apache.commons.lang3.StringUtils
						.stripToNull(usernameTextView.getText().toString());
				String password = org.apache.commons.lang3.StringUtils
						.stripToNull(passwordTextView.getText().toString());

				SharedPreferences settings = getSharedPreferences(
						getString(R.string.xmpp_prefs), MODE_PRIVATE);
				SharedPreferences.Editor prefEditor = settings.edit();
				prefEditor.putString(getString(R.string.user_name), username);
				prefEditor.putString(getString(R.string.password), password);
				prefEditor.commit();

				Intent intent = new Intent();
				intent.setAction(XmppService.CONNECT);
				Message newMessage = Message.obtain();
				newMessage.obj = intent;
				XmppService.sendToServiceHandler(intent);
			}
		};

		return LoginDialog.createLoginDialog(this, positive, negative, null);
	}

	public void sendXmppMessage(String text) {
		Intent intent = new Intent();
		intent.setAction(XmppService.SEND_MESSAGE_CHAT);
		intent.putExtra(XmppService.MESSAGE_TEXT_CHAT, text);
		Message newMessage = Message.obtain();
		newMessage.obj = intent;
		XmppService.sendToServiceHandler(intent);
	}

	private void hardcodedUserNameXMPP() {
		SharedPreferences settings = getSharedPreferences(
				getString(R.string.xmpp_prefs), MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = settings.edit();
		// prefEditor.clear();
		// prefEditor.commit();
		prefEditor.putString(getString(R.string.user_name), "obama");
		prefEditor.putString(getString(R.string.password), "obama");
		prefEditor.putString(getString(R.string.XMPP_HOST_KEY),
				getString(R.string.xmpp_host));
		prefEditor.putInt(getString(R.string.XMPP_PORT), 5222);
		prefEditor.commit();
	}

	public boolean shouldShowDialog() {

		SharedPreferences settings = getSharedPreferences(
				getString(R.string.xmpp_prefs), MODE_PRIVATE);
		String storedUserName = settings.getString(
				getString(R.string.user_name), "");
		String storedPassword = settings.getString(
				getString(R.string.password), "");

		if (storedPassword != null && storedUserName != null) {
			return false;
		}

		return true;

	}

	public void makeToast(Intent intent) {
		if (intent != null) {
			String stringExtra = intent
					.getStringExtra(XmppService.XMPP_MESSAGE);
			makeToast(stringExtra);
		}
	}

	public void makeToast(String toastText) {
		Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
		toast.show();
	}

	private void setupTabs() {
		// TODO Auto-generated method stub

		// Theories tab

		Tab tab = actionBar.newTab().setText(R.string.tab_title_theories)
				.setTabListener(this);
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText(R.string.tab_title_observations)
				.setTabListener(this);

		actionBar.addTab(tab);
		tab = actionBar.newTab().setText(R.string.tab_title_scratch_pad)
				.setTabListener(this);

		actionBar.addTab(tab);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		getFragmentViewPager().setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragments = null;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = Lists.newArrayList(
					new TheoryFragmentWithSQLiteLoaderNestFragments(),
					new ObservationFragment(), new ScratchPadFragment());
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.tab_title_theories);
			case 1:
				return getString(R.string.tab_title_observations);
			case 2:
				return getString(R.string.tab_title_scratch_pad);
			}
			return null;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		connectMenu = menu.getItem(0);
		disconnectMenu = menu.getItem(1);
		disconnectMenu.setEnabled(false);
		initMenu = menu.getItem(2);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_connect:

			// Intent intent1 = new Intent(MainActivity.this,
			// WizardDialogFragment.class);
			// startActivity(intent1);

			prepDialog().show();
			connectMenu.setEnabled(false);
			disconnectMenu.setEnabled(true);
			return true;
		case R.id.menu_disconnect:
			connectMenu.setEnabled(true);
			disconnectMenu.setEnabled(false);
			Intent intent = new Intent();
			intent.setAction(XmppService.DISCONNECT);
			Message newMessage = Message.obtain();
			newMessage.obj = intent;
			XmppService.sendToServiceHandler(intent);
			return true;
		case R.id.menu_init:
			this.sendInitMessage();
			return true;
		case R.id.menu_reset_db:
			this.resetDB();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public NonSwipeableViewPager getFragmentViewPager() {
		return mViewPager;
	}

	public void setFragmentViewPager(NonSwipeableViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	public SectionsPagerAdapter getSectionsPagerAdapter() {
		return mSectionsPagerAdapter;
	}

	public void setSectionsPagerAdapter(
			SectionsPagerAdapter mSectionsPagerAdapter) {
		this.mSectionsPagerAdapter = mSectionsPagerAdapter;
	}

	@Override
	public void addUsedPlanetColors(CircleView someView) {
		TheoryFragmentWithSQLiteLoaderNestFragments fragment = (TheoryFragmentWithSQLiteLoaderNestFragments) this
				.getSectionsPagerAdapter().getItem(0);
		fragment.addUsedPlanetColors(someView);
	}

	@Override
	public void showPlanetColor(String color) {
		TheoryFragmentWithSQLiteLoaderNestFragments fragment = (TheoryFragmentWithSQLiteLoaderNestFragments) this
				.getSectionsPagerAdapter().getItem(0);
		fragment.showPlanetColor(color);
	}

	public void addUsedPlanetColor(String color) {
		TheoryFragmentWithSQLiteLoaderNestFragments fragment = (TheoryFragmentWithSQLiteLoaderNestFragments) this
				.getSectionsPagerAdapter().getItem(0);
		fragment.addUsedPlanetColor(color);
	}

	public void operationTheory(Reason reason, String anchor, String command, boolean shouldSendIntent)
			throws NullPointerException {

		// find the loader
		Fragment findFragmentByTag = this.getSupportFragmentManager()
				.findFragmentByTag(anchor + "_THEORY");
		if (findFragmentByTag == null)
			return;

		// find the loader
		TheoryViewFragment tf = (TheoryViewFragment) findFragmentByTag;
		tf.dbOperation(reason, command, shouldSendIntent);
	}

	public void operationObservation(Reason reason, String anchor,
			String command, boolean shouldSendIntent) {
		// find the loader
		Fragment findFragmentByTag = this.getSupportFragmentManager()
				.findFragmentByTag(anchor + "_OBSERVATION");
		if (findFragmentByTag == null)
			return;

		// find the loader
		ObservationViewFragment of = (ObservationViewFragment) findFragmentByTag;
		of.dbOperation(reason, command, shouldSendIntent);
	}

}
