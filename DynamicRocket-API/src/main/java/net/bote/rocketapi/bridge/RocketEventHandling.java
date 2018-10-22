package net.bote.rocketapi.bridge;

import com.google.gson.JsonObject;
import net.bote.rocketapi.RocketAPI;
import net.bote.rocketapi.bridge.server.GroupConfiguration;
import net.bote.rocketapi.lib.Document;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * @author Elias Arndt
 * created on 20.10.2018 at 21:00
 *
 * This class contains called event for socketIO trafic
 *
 * Thanks to Daniel Riegler for support me at some bugs in socketIO!
 */

public class RocketEventHandling {

    Client client;

    /**
     * Constructor for this class
     * @param client the SocketIO Client of this server
     */
    public RocketEventHandling(Client client) {
        this.client = client;
        handlePlayerInfo();
        handleMessageTrafic();
        handleReload();
        handleKick();
    }

    /**
     * Contains eventhandler for a Player-Info-Request
     */
    public void handlePlayerInfo() {
        client.getSocket().on("PlayerInfo", (objects) -> {
            final JsonObject object = JsonObject.class.cast(objects[0]);
            Document doc = new Document(object);
            ProxiedPlayer p = null;

            if(doc.getString("name") != null) {
                p = ProxyServer.getInstance().getPlayer(doc.getString("name"));
            } else {
                p = ProxyServer.getInstance().getPlayer(UUID.fromString(doc.getString("uuid")));
            }

            if(p == null) return;

            RocketPlayer player = new RocketPlayer(p.getUniqueId(),
                    p.isForgeUser(),
                    p.getName(),
                    p.getPermissions(),
                    p.getGroups(),
                    p.getServer().getInfo().getName(),
                    new MinecraftServer(GroupConfiguration.getGroupConfiguration(p.getServer().getInfo().getName().split("-")[0]), p.getServer().getInfo().getName()));

            client.getSocket().emit("PlayerInfoCallback", RocketAPI.getInstance().getGson().toJson(player));
        });
    }

    /**
     * Contains event handler to send a player a message
     */
    public void handleMessageTrafic() {
        client.getSocket().on("PlayerMessage", (objects) -> {
            final JsonObject object = JsonObject.class.cast(objects[0]);
            Document doc = new Document(object);

            ProxiedPlayer p = null;

            if(doc.contains("uuid")) {
                p = ProxyServer.getInstance().getPlayer(UUID.fromString(doc.getString("uuid")));
            } else {
                ProxyServer.getInstance().getPlayer(doc.getString("name"));
            }

            p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', doc.getString("message"))));
        });
    }

    /**
     * Contains event handler to reload the bungeecord
     */
    public void handleReload() {
        client.getSocket().on("BungeeCordUpdate", (obj) -> {
            BungeeCord.getInstance().config.load();
            BungeeCord.getInstance().reloadMessages();
            BungeeCord.getInstance().stopListeners();
            BungeeCord.getInstance().startListeners();
        });
    }

    /**
     * Contains event handler to kick a player
     */
    public void handleKick() {
        client.getSocket().on("PlayerKick", (obj) -> {
            final JsonObject object = JsonObject.class.cast(obj[0]);
            Document doc = new Document(object);
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(UUID.fromString(doc.getString("uuid")));

            if(doc.contains("reason")) {
                p.disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', doc.getString("reason"))));
            } else {
                p.disconnect(new TextComponent("§3DynamicRocket\n§7§m---------------------\n\n§cYou got kicked!"));
            }
        });
    }



}
