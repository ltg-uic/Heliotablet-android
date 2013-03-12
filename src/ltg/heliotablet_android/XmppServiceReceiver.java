package ltg.heliotablet_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class XmppServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(".TalkMyPhone.ACTION");
        context.startService(serviceIntent);
	}

}
