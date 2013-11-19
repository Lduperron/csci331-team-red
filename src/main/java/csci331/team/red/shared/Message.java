package csci331.team.red.shared;

import java.util.List;

/**
 * Enumerated values for message to be passed from client to server.
 */
public enum Message {

	/**
	 * Send an alert to client, expects {@link Alert} object to be sent with
	 * this message.
	 */
	ALERT,

	/**
	 * Connected to client/server, and waiting for game start
	 */
	CONNECTED,

	/**
	 * Sends dialogue to client/server, expects a {@link List} of
	 * {@link Dialogue} objects to be sent with this message.
	 */
	DIALOGUE,

	/**
	 * Disconnected to client/server, and game is reseting
	 */
	DISCONNECTED,

	/**
	 * Sends client Role to client, expects {@link Role} object to be sent with
	 * this message.
	 */
	SET_ROLE,

	/**
	 * Request the start of a level, expects {@link Level} object to be sent
	 * with this message.
	 */
	START_LEVEL,

	/**
	 * Starts a incident, expects {@link Incident} object to be sent with this
	 * message.
	 */
	START_INCIDENT,

	/**
	 * The user has paused the game
	 */
	PAUSE,

	/**
	 * The user has resumed the game
	 */
	RESUME,

	/**
	 * The user has quit the game
	 */
	QUIT
}