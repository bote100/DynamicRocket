package net.bote.rocketapi.bridge.interfaces;

/**
 * @author Elias Arndt
 * created on 22.10.2018 at 14:21
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
