package net.bote.dynamicrocket.connection;

/**
 * @author Elias Arndt
 * created on 03.10.2018 at 00:16
 */

public interface CommandSender {

    /**
     * Send message as {@link String} to CommandSender object.
     * @param message message you would like to send
     */
    void sendMessage(String message);

}
