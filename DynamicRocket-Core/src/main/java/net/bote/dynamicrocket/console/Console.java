package net.bote.dynamicrocket.console;

import net.bote.dynamicrocket.connection.CommandSender;

/**
 * @author Elias Arndt
 *
 * Console of DynamicRocket. Implementing CommandSender.
 */
public class Console implements CommandSender {

	// Constuctor
	public Console() {}

	@Override
	/**
	 * Print {@link String} message in console.
	 */
	public void sendMessage(String messsage) {
		new Logger(messsage, LogLevel.INFO).log();
	}

	/**
	 * Send a raw message without prefix.
	 * @param message message
	 */
	public void sendRawMessage(String message) {
		System.out.println(message);
	}
}
