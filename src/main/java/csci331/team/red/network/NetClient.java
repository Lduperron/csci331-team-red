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
import csci331.team.red.shared.Result;
import csci331.team.red.shared.Role;

/**
 * Client end for KryoNet network communications
 * 
 * @see https://code.google.com/p/kryonet/
 * 
 *      CSCI331 ML INTERFACE
 * 
 *      Provides an network API for the {@link ClientEngine} that hides the
 *      complexity of the network implementation from the ClientEngine and
 *      protects network properties from external modification using controlled
 *      access
 * 
 *      CSCI331 ML SUBCLASS
 * 
 *      Implements Client specific functionality for {@link Network} - overrides
 *      {@link Listener} to implement calls to the {@link ClientEngine} API
 * 
 * @author marius
 */
public class NetClient extends Network {
	private final ClientEngine gameClient;
	private final Client client;
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
		this.gameClient = incomingGameClient;
		client = new Client(BUFFER_SIZE, BUFFER_SIZE);
		client.start();

		/**
		 * For consistency, the classes to be sent over the network are
		 * registered by the same method for both the client and server
		 * inherited from SuperClass Network.
		 */
		register(client);

		try {
			client.connect(timeout, host, tcpPort);
		} catch (IOException e) {
			throw new IOException("Unable to connect to Server");
		}
		this.send(Message.CONNECT);
		/**
		 * change timeout to 60 secs so that client will not accidently
		 * disconnect
		 */
		setTimeout(60000);

		/**
		 *  ThreadedListener runs the listener methods on a different thread.
		 */
		client.addListener(new ThreadedListener(new Listener() {
			private Dialogue[] dialogues;

			/**
			 * CSCI331 ML OVERRIDING
			 * 
			 * Override received method of Listener to specify game specific
			 * management of received objects
			 */
			public void received(Connection connection, Object object) {
				if (object instanceof NetMessage) {
					final NetMessage netMsg = (NetMessage) object;
					/**
					 *  Process the message received from the server
					 */
					switch (netMsg.msg) {
					case ALERT:
						if (netMsg.obj instanceof Alert) {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									gameClient.addAlert((Alert) netMsg.obj);
								}
							});
						}
						break;
					case DBRESULT:
						if (netMsg.obj instanceof Result) {
							gameClient.DatabaseQueryResult((Result) netMsg.obj);
						}
						break;
					case DIALOGUE:
						if (netMsg.obj instanceof List) {
							List<Dialogue> dialogueList = new LinkedList<Dialogue>();
							List<?> generic = (List<?>) netMsg.obj;

							for (Object obj : generic) {
								if (obj instanceof Dialogue) {
									dialogueList.add((Dialogue) obj);
								}
							}

							this.dialogues = new Dialogue[dialogueList.size()];
							dialogueList.toArray(dialogues);

							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									gameClient.DisplayDialouge(dialogues);
								}
							});
						}
						break;
					case DISCONNECT:
						// server requested a disconnect
						Gdx.app.postRunnable(new Runnable() {
							@Override
							public void run() {
								gameClient.LeaveGame();
							}
						});
						break;
					case PAUSE:
						Gdx.app.postRunnable(new Runnable() {
							@Override
							public void run() {
								gameClient.PauseGame();
							}
						});
						break;
					case QUIT:
						Gdx.app.postRunnable(new Runnable() {
							@Override
							public void run() {
								gameClient.LeaveGame();
							}
						});
						break;
					case RESUME:
						Gdx.app.postRunnable(new Runnable() {
							@Override
							public void run() {
								gameClient.UnpauseGame();
							}
						});
						break;
					case SET_ROLE:
						if (netMsg.obj instanceof Role) {
							/**
							 * In order to communicate with the GDX rendering
							 * thread, we need to spawn a runnable and post it
							 * to it
							 */
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									/**
									 * process the result, e.g. add it to an
									 * Array<Result> field of the
									 * ApplicationListener.
									 */
									gameClient.SetRole((Role) netMsg.obj);
								}
							});
						}
						break;
					case START_LEVEL:
						if (netMsg.obj instanceof Level) {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									gameClient.setLevel((Level) netMsg.obj);
								}
							});
						}
						break;
					case START_INCIDENT:
						if (netMsg.obj instanceof Incident) {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									gameClient
											.startIncident((Incident) netMsg.obj);
								}
							});
						}
						break;
					default:
						// invalid messages are simply ignored
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
				// connection to Server was lost
				gameClient.LeaveGame();
			}
		})); // end of addListener
	} // end of constructor

	/**
	 * Send an Enumerated {@link Message}
	 * 
	 * @param msg
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

	/**
	 * Disconnects the KryoNet Client
	 */
	public void kill() {
		client.stop();
	}
}
