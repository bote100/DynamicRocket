package net.bote.rocketapi.events;

import net.bote.rocketapi.RocketAPI;
import net.bote.rocketapi.lib.Document;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Elias Arndt
 * created on 21.10.2018 at 19:19
 */

public class ConnectListener implements Listener {

    @EventHandler
    public void onConnect(ServerConnectedEvent e) {

        RocketAPI.getInstance().getClient().getSocket().emit("RegisterPlayer",
                new Document().append("uuid", e.getPlayer().getUniqueId().toString()).convertToJson());

    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        RocketAPI.getInstance().getClient().getSocket().emit("PlayerDisconnect",
                new Document().append("uuid", e.getPlayer().getUniqueId().toString()).convertToJson());
    }

}
