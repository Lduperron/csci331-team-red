package csci331.team.red.net;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import csci331.team.red.core.ClientEngine;
import csci331.team.red.shared.Level;
import csci331.team.red.shared.Message;

/**
 * Client end for KryoNet network communications
 * 
 * @see https://code.google.com/p/kryonet/
 * @author mariusloots
 */
public class NetClient {
	protected final ClientEngine gameClient;
	protected final Client client;
	protected String host = "127.0.0.1";

	/**
	 * Constructor for NetClient
	 * 
	 * @param gameClient
	 *            Reference to a {@link ClientEngine}
	 * @param host
	 *            Name or ip address of the Server to connect to
	 */
	public NetClient(final ClientEngine gameClient, String host) {
		this.host = host;
		this.gameClient = gameClient;
		// connects to a server
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		try {
			client.connect(5000, host, Network.tcpPort);
			this.send(Message.CONNECTED);
		} catch (IOException e) {
			// TODO: should I be catching this, or just pass it on to client?
			System.out.println("Client bombed");
			e.printStackTrace();
		}

		// ThreadedListener runs the listener methods on a different thread.
		client.addListener(new ThreadedListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof NetMessage) {
					NetMessage netMsg = (NetMessage) object;
					// process message
					switch (netMsg.msg) {
					case CONNECTED:

						break;
					case START_WAIT_LEVEL:

						break;
					case START_LEVEL_ONE:
						if (netMsg.obj instanceof Level) {
							Level level = (Level) netMsg.obj;
							// gameClient.startLevel(level);
							System.out.println("Starting level one...");
						}
						break;
					case START_LEVEL_TWO:
						if (netMsg.obj instanceof Level) {
							Level level = (Level) netMsg.obj;
							// gameClient.startLevel(level);
							System.out.println("Starting level two...");
						}
						break;
					case START_LEVEL_THREE:
						if (netMsg.obj instanceof Level) {
							Level level = (Level) netMsg.obj;
							// gameClient.startLevel(level);
							System.out.println("Starting level three...");
						}
						break;
					case READY:

						break;
					case PAUSE:

						break;
					case QUIT:

						break;
					default:
						break;
					}
				}
			}
		}));
	}

	/**
	 * @param msg
	 * Send an Enumerated {@link Message}
	 */
	public void send(Message msg) {
		send(msg, null);
	}

	/**
	 * Send an Enumerated {@link Message} and a registered ({@link Kryo#register(Class)}) Object
	 * @param msg
	 * @param obj
	 */
	public void send(Message msg, Object obj) {
		NetMessage netMsg = new NetMessage(msg, obj);
		client.sendTCP(netMsg);
	}
}
