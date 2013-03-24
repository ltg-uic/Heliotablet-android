package ltg.heliotablet_android;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import ltg.commons.LTGEvent;
import ltg.commons.LTGEventHandler;
import ltg.commons.NotAnLTGEventException;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.pubsub.PresenceState;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class XmppService extends IntentService {

	private final IBinder xmppBinder = new XmppBinder();

	private static final String TAG = "XmppService";
	private static final String DOMAIN = "54.243.60.48";
	private static final String USERNAME = "android";
	private static final String PASSWORD = "android";

	// action messages
	public static final String CHAT_ACTION_RECEIVED_MESSAGE = "CHAT_ACTION";
	public static final String MESSAGE_TEXT_CHAT = "MESSAGE_TEXT";
	public static final String GROUP_CHAT = "GROUP_CHAT";
	public static final String SINGLE_CHAT = "SINGLE_CHAT";
	public static final String CHAT_TYPE = "CHAT_TYPE";
	public static final String GROUP_CHAT_NAME = "GROUP_CHAT_NAME";
	public static final String RECONNECT = "RECONNECT";
	public static final String SHOW_LOGIN = "SHOW_LOGIN";
	public static final String DISCONNECT = "DISCONNECT";
	public static final String STARTUP = "STARTUP";
	public static final String DESTORY = "DESTORY";
	public static final String ERROR = "ERROR";
	public static final String DO_LOGIN = "DO_LOGIN";
	public static final String SEND_MESSAGE_CHAT = "SEND_MESSAGE";
	public static final String ACTIVITY_MESSAGER = "ACTIVITY_MESSAGER";
	public static final String CONNECT = "CONNECT";
	public static final String XMPP_MESSAGE = "XMPP_MESSAGE";

	public static final String LTG_EVENT = "LTG_EVENT";

	public static final String LTG_EVENT_RECEIVED = "LTG_EVENT_RECEIVED";
	public static final String LTG_EVENT_SENT = "LTG_EVENT_SENT";

	public static final String GROUP_CHAT_CREATED = "GROUP_CHAT_CREATED";

	public static final String DISCONNECTED_FOR_UI = "DISCONNECTED_FOR_UI";

	public static String SEND_GROUP_MESSAGE = "SEND_GROUP_MESSAGE";

	private static volatile Looper serviceLooper;
	private static volatile ServiceHandler serviceHandler;
	private Messenger activityMessenger;
	private XMPPConnection xmppConnection;
	private long handlerThreadId;
	private MultiUserChat groupChat;
	private String chatType = null;
	private String groupChatRoom = null;
	private String groupChatName = null;

	// listeners
	private ConnectionCreationListener connectionCreationListener;
	private ConnectionListener connectionGeneralListener;
	private ArrayList<PacketListener> packetListeners = new ArrayList<PacketListener>();

	public XmppService() {
		super("XMPP SERVICE");
	}

	// Handler of incoming messages from clients.
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			onHandleIntent((Intent) msg.obj);
		}
	}

	private SmackAndroid smackAndroid;

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.i(TAG, "onHandleIntent");

		String action = intent.getAction();
		Bundle extras = intent.getExtras();
		if (action.equals(STARTUP)) {
			Object messExtra = extras.get(ACTIVITY_MESSAGER);

			if (messExtra != null) {
				activityMessenger = (Messenger) messExtra;
			}

			chatType = (String) extras.get(CHAT_TYPE);


		} else if (action.equals(SEND_MESSAGE_CHAT)) {
			Object extra = extras.get(MESSAGE_TEXT_CHAT);
			if (extra != null) {
				String text = (String) extra;

				Log.i("XMPPChatDemoActivity", "Sending text " + text + " to "
						+ "aperritano");
				Message msg = new Message("aperritano@"
						+ xmppConnection.getHost(), Message.Type.chat);
				msg.setBody(text);
				if (xmppConnection != null) {
					xmppConnection.sendPacket(msg);
				}
			}
		} else if (action.equals(SEND_GROUP_MESSAGE)) {
			LTGEvent extra = (LTGEvent) extras.get(LTG_EVENT_SENT);
			if (xmppConnection != null) {
				if( xmppConnection.isAuthenticated()) {
				if (extra != null) {

					String serializeEvent = LTGEventHandler
							.serializeEvent(extra);

					Log.i("XMPPChatDemoActivity", "Sending text "
							+ serializeEvent + " to " + "aperritano");

					Message msg = new Message(groupChatName + "@conference."
							+ xmppConnection.getHost(), Message.Type.groupchat);
					msg.setBody(serializeEvent);
					if (xmppConnection != null) {
						xmppConnection.sendPacket(msg);
					}
				}
				}
			}
		} else if (action.equals(DO_LOGIN)) {
			doLogin();

			// if( groupChatRoom != null)
			// doGroupChat(groupChatRoom);

		} else if (action.equals(CONNECT)) {
			
			groupChatRoom = (String) extras.get(GROUP_CHAT_NAME);
			groupChatName = StringUtils.parseName(groupChatRoom);
			
			doConnection();
		} else if (action.equals(RECONNECT)) {
			doConnection();
		} else if (action.equals(DISCONNECT)) {
			if( groupChat != null ) {
				groupChat.leave();
				sendMessageToUI("Disconnecting....from group chat");
			}
			
			if( xmppConnection != null) {
				sendMessageToUI("Disconnecting....from connection");
				xmppConnection.disconnect();
			}
			
		} else if (action.equals(GROUP_CHAT)) {
			doGroupChat(groupChatRoom);
			sendMessageToUI("Joined Group chat!!");
			Intent i = new Intent(GROUP_CHAT_CREATED);
			sendIntentToUI(i);
		} else if (action.equals(DESTORY)) {
			removeListeners();
			groupChat = null;
			xmppConnection = null;
			
			Intent i = new Intent(DISCONNECTED_FOR_UI);
			sendIntentToUI(i);
			
		} else if (action.equals(LTG_EVENT_RECEIVED)) {
			String json = intent.getStringExtra(XMPP_MESSAGE);
			if( !json.contains("event") )
				return;
			
			try {
				LTGEvent deserializeEvent = LTGEventHandler
						.deserializeEvent(json);
				Intent i = new Intent(LTG_EVENT_RECEIVED);
				i.putExtra(LTG_EVENT, (Serializable) deserializeEvent);
				sendIntentToUI(i);
			} catch (IOException e) {
				Log.e(TAG, "Problem Deserializing Event", e);
			} catch (NotAnLTGEventException e) {
				Log.e(TAG, "Non ltg event", e);
				sendMessageToUI("Non LTG Event captured");
			}

		}

	}


	public void doConnection() {

		if (xmppConnection == null) {

			AndroidConnectionConfiguration connectionConfiguration = new AndroidConnectionConfiguration(
					DOMAIN, 5222, "TABLET");
			connectionConfiguration.setSASLAuthenticationEnabled(false);
			connectionConfiguration.setDebuggerEnabled(true);
			// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			XMPPConnection.DEBUG_ENABLED = true;
			xmppConnection = new XMPPConnection(connectionConfiguration);

			Connection
					.addConnectionCreationListener(createConnectionListener());

			try {
				xmppConnection.connect();
				xmppConnection
						.addConnectionListener(createGeneralConnectionListener());

			} catch (final XMPPException e) {
				sendMessageToUI("There was a problem connecting to " + DOMAIN);
				Log.e(TAG, "Could not connect to Xmpp server.", e);
			}
		}
	}

	public ConnectionCreationListener createConnectionListener() {
		connectionCreationListener = new ConnectionCreationListener() {

			@Override
			public void connectionCreated(final Connection connection) {
				final Intent i = new Intent(DO_LOGIN);
				final android.os.Message newMessage = serviceHandler
						.obtainMessage();
				newMessage.obj = i;
				sendToServiceHandler(i);
				sendMessageToUI("Connection Successful!!");
			}
		};

		return connectionCreationListener;
	}

	public ConnectionListener createGeneralConnectionListener() {
		connectionGeneralListener = new ConnectionListener() {

			@Override
			public void connectionClosed() {
				final Intent i = new Intent(DESTORY);
				final android.os.Message newMessage = serviceHandler
						.obtainMessage();
				newMessage.obj = i;
				sendToServiceHandler(i);
				sendMessageToUI("Connection is being destroyed normally");

			}

			@Override
			public void connectionClosedOnError(Exception e) {
				final Intent i = new Intent(DESTORY);
				final android.os.Message newMessage = serviceHandler
						.obtainMessage();
				newMessage.obj = i;
				sendToServiceHandler(i);
				sendMessageToUI("Connection is being destroyed do to an error");

			}

			@Override
			public void reconnectingIn(int seconds) {
				// TODO Auto-generated method stub

			}

			@Override
			public void reconnectionSuccessful() {
				// TODO Auto-generated method stub

			}

			@Override
			public void reconnectionFailed(Exception e) {
				// TODO Auto-generated method stub

			}
		};

		return connectionGeneralListener;
	}

	public void removeListeners() {
		Connection.removeConnectionCreationListener(connectionCreationListener);
		xmppConnection.removeConnectionListener(connectionGeneralListener);
		for (PacketListener packetListener : packetListeners) {
			groupChat.removeMessageListener(packetListener);
		}
	}

	public void doLogin() {

		if (xmppConnection != null) {

			SharedPreferences settings = getSharedPreferences(
					getString(R.string.xmpp_prefs), MODE_PRIVATE);
			String storedUserName = settings.getString(
					getString(R.string.user_name), null);
			String storedPassword = settings.getString(
					getString(R.string.password), null);

			String error = null;
			try {

				xmppConnection.login(storedUserName, storedPassword);

				while(xmppConnection.isAuthenticated() == false) {
					sendMessageToUI("Waiting to login to chat");
				}
				
				final Intent i = new Intent(GROUP_CHAT);
				final android.os.Message newMessage = serviceHandler
						.obtainMessage();
				newMessage.obj = i;
				sendToServiceHandler(i);
			} catch (XMPPException e) {
				Log.e(TAG, "Could not login into xmpp server.", e);
				showToast("There was a problem logging in with l: "
						+ storedUserName + " p: " + storedPassword);
			} catch (Exception e) {
				Log.e(TAG, "too many logins", e);

				showToast("Problem in DoLogin");
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread("XMPP SERVICE MOTHA FUCKA");
		thread.start();
		handlerThreadId = thread.getId();
		serviceLooper = thread.getLooper();
		serviceHandler = new ServiceHandler(serviceLooper);
		smackAndroid = SmackAndroid.init(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sendToServiceHandler(startId, intent);
		return START_REDELIVER_INTENT;
	}

	public void doGroupChat(String chatroom) {
		if (xmppConnection != null) {
			if (xmppConnection.isAuthenticated() && chatroom != null) {
				// Initialize and join chatRoom
				if (groupChat == null)
					groupChat = new MultiUserChat(xmppConnection, chatroom);

				try {
					groupChat.join(xmppConnection.getUser());
					
					while(groupChat.isJoined() == false) {
						sendMessageToUI("Waiting to join group chat");
					}
					
					
				} catch (XMPPException e) {
					Log.e(TAG, "ERROR CONNECTING TO GROUP CHAT", e);
				}

				PacketListener pl = new PacketListener() {
					@Override
					public void processPacket(Packet packet) {
						Message message = (Message) packet;
						processMessage(message);
					}
				};

				packetListeners.add(pl);

				xmppConnection.addPacketListener(pl, new PacketTypeFilter(
						Message.class));

			}
		}
	}

	public void doSingleChat() {
		PacketListener pl = new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				processMessage(message);
			}
		};

		packetListeners.add(pl);
		xmppConnection.addPacketListener(pl,
				new PacketTypeFilter(Message.class));

	}

	protected void processMessage(Message message) {
		String fromName = null;
		if (message.getFrom() != null) {
			fromName = StringUtils.parseBareAddress(message.getFrom());
			
		}
		if (message.getBody() != null) {

			String currentUser = StringUtils.parseName(xmppConnection.getUser());
			
			if( fromName.contains(currentUser))
				return;
			
			
			// LTG EVENT
			Intent intent = new Intent(LTG_EVENT_RECEIVED);
			intent.putExtra(XMPP_MESSAGE, message.getBody());
			sendToServiceHandler(intent);

		} else {
			// Log.i(TAG, packet.toXML());
		}
	}

	protected void sendIntentToUI(Intent intent) {
		android.os.Message newMessage = serviceHandler.obtainMessage();
		newMessage.obj = intent;
		try {
			activityMessenger.send(newMessage);
		} catch (RemoteException e) {
			Log.e(TAG, "Could not create message to UI to show login", e);
		}

	}

	protected void sendMessageToUI(String text) {
		Intent i = new Intent(ERROR);
		i.putExtra(XMPP_MESSAGE, text);

		android.os.Message newMessage = serviceHandler.obtainMessage();
		newMessage.obj = i;
		try {
			activityMessenger.send(newMessage);
		} catch (RemoteException e) {
			Log.e(TAG, "Could not create message to UI to show login", e);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		serviceLooper.quit();
		xmppConnection.disconnect();
		smackAndroid.onDestroy();
	}

	public static boolean sendToServiceHandler(int i, Intent intent) {
		if (serviceHandler != null) {
			android.os.Message msg = serviceHandler.obtainMessage();
			msg.arg1 = i;
			msg.obj = intent;
			serviceHandler.sendMessage(msg);
			return true;
		} else {
			Log.i(TAG,
					"sendToServiceHandler() called with " + intent.getAction()
							+ " when service handler is null");
			return false;
		}
	}

	public static boolean sendToServiceHandler(Intent intent) {
		return sendToServiceHandler(0, intent);
	}

	public static Looper getServiceLooper() {
		return serviceLooper;
	}

	public static String getThreadSignature() {
		final Thread t = Thread.currentThread();
		return new StringBuilder(t.getName()).append("[id=").append(t.getId())
				.append(", priority=").append(t.getPriority()).append("]")
				.toString();
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class XmppBinder extends Binder {
		public XmppService getService() {
			return XmppService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return xmppBinder;
	}

}
