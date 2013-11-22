package csci331.team.red.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import csci331.team.red.client.ClientEngine;
import csci331.team.red.shared.Alert;
import csci331.team.red.shared.Dialogue;
import csci331.team.red.shared.Incident;
import csci331.team.red.shared.Level;
import csci331.team.red.shared.Message;
import csci331.team.red.shared.Role;

/**
 * CSCI331 ML INTERFACE
 */
/**
 * Client end for KryoNet network communications
 * 
 * @see https://code.google.com/p/kryonet/
 * @author marius
 */
public class NetClient {
	private final ClientEngine gameClient;
	private final Client client;
	private String host = "127.0.0.1";
	private static final int timeout = 5000;

	/**
	 * Constructor for NetClient
	 * 
	 * @param gameClient
	 *            Reference to a {@link ClientEngine}
	 * @param host
	 *            Name or ip address of the Server to connect to
	 * @throws IOException
	 */
	public NetClient(final ClientEngine incomingGameClient, String host)
			throws IOException {
		this.host = host;
		this.gameClient = incomingGameClient;
		// connects to a server
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.connect(timeout, host, Network.tcpPort);
		this.send(Message.CONNECTED);
		/*
		 * change timeout to 60 secs so that client will not accidently
		 * disconnect
		 */
		setTimeout(60000);

		// ThreadedListener runs the listener methods on a different thread.
		client.addListener(new ThreadedListener(new Listener() {
			/**
			 * CSCI331 ML OVERRIDING
			 */
			/**
			 * Override received method of Listener to specify game specific
			 * management of received objects
			 */
			public void received(Connection connection, Object object) {
				if (object instanceof NetMessage) {
					final NetMessage netMsg = (NetMessage) object;
					// process message
					switch (netMsg.msg) {
					case ALERT:
						if (netMsg.obj instanceof Alert) {
							gameClient.addAlert((Alert) netMsg.obj);
						}
						break;
					case CONNECTED:
						// client should not receive this message
						break;
						// onPostureChange(msg, Posture Enum)
					case DIALOGUE:
						if (netMsg.obj instanceof List) {
							List<Dialogue> dialogueList = new LinkedList<Dialogue>();
							List<?> generic = (List<?>) netMsg.obj;

							for (Object obj : generic) {
								if (obj instanceof Dialogue) {
									dialogueList.add((Dialogue) obj);
								}
							}

							Dialogue[] dialogues = new Dialogue[dialogueList
									.size()];
							dialogueList.toArray(dialogues);

							gameClient.DisplayDialouge(dialogues);
						}
						break;
					case DISCONNECTED:
						gameClient.LeaveGame();
						break;
					case PAUSE:
						gameClient.PauseGame();
						break;
					case QUIT:
						gameClient.LeaveGame();
						break;
					case RESUME:
						gameClient.UnpauseGame();
						break;
					case SET_ROLE:
						if (netMsg.obj instanceof Role) 
						{
							 // In order to communicate with the GDX rendering thread, we need to spawn a runnable and post it to it
							
						      Gdx.app.postRunnable(new Runnable() {
						          @Override
						          public void run() {
						             // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
						        	  gameClient.SetRole((Role) netMsg.obj);
						          }
						       });
						    }
							
							
						
						break;
					case START_LEVEL:
						if (netMsg.obj instanceof Level) {
							gameClient.setLevel((Level) netMsg.obj);
						}
						break;
					case START_INCIDENT:
						if (netMsg.obj instanceof Incident) {
							gameClient.startIncident((Incident) netMsg.obj);
						}
						break;
					default:
						break;
					}
				}
			}

			/**
			 * Called when the remote end is no longer connected. Used to trap
			 * accidental disconnects cause server won't be able to tell us that
			 * it disconnected
			 */
			public void disconnected(Connection connection) {
				gameClient.LeaveGame();
			}
		})); // end of addListener
	} // end of constructor

	/**
	 * CSCI331 ML STATICBINDING
	 * 
	 * Explain how the system will decide which method to invoke/variable to
	 * access.
	 */
	/**
	 * @param msg
	 *            Send an Enumerated {@link Message}
	 */
	public void send(Message msg) {
		send(msg, null);
	}

	/**
	 * Send an Enumerated {@link Message} and a registered (
	 * {@link Kryo#register(Class)}) Object
	 * 
	 * @param msg
	 * @param obj
	 */
	public void send(Message msg, Object obj) {
		NetMessage netMsg = new NetMessage(msg, obj);
		client.sendTCP(netMsg);
	}

	/**
	 * Sets {@line Connection} timeout to a value between 0 and 60 seconds
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		if ((timeout > 0) && (timeout < 60001))
			client.setTimeout(timeout);
	}
}
