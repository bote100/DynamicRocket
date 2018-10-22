package net.bote.dynamicrocket.server;

/**
 * @author Elias Arndt
 * created on 30.09.2018 at 17:07
 */

public interface MinecraftServerCallback {

    /**
     * called, when a server successfully started.
     * @param millis Start-time of the server
     */
    void onStart(long millis);

    /**
     * Called, when the server stops.
     */
    void onStop();

}
