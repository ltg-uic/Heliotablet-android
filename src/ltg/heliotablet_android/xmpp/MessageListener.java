package ltg.heliotablet_android.xmpp;

import org.jivesoftware.smack.packet.Message;

public interface MessageListener {
	
	public void processMessage(Message m);

}
