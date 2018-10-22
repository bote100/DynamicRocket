package net.bote.rocketapi.console;

import net.bote.rocketapi.utility.LogLevel;
import net.bote.rocketapi.utility.Logger;

/**
 * @author Elias Arndt
 * created on 20.10.2018 at 18:51
 */

public class Console {

    // Constuctor
    public Console() {}

    /**
     * Print {@link String} message in console.
     * @param messsage message
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
