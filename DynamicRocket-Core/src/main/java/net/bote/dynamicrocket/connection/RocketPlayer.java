package net.bote.dynamicrocket.connection;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.lib.Document;
import net.bote.dynamicrocket.server.MinecraftServer;

import java.util.List;
import java.util.UUID;

/**
 * @author Elias Arndt
 * created on 20.10.2018 at 15:06
 *
 * This is a player of the network. This object implements the {@link CommandSender} interface.
 * That means, that the player can execute a {@link net.bote.dynamicrocket.command.RocketCommand}.
 */

public class RocketPlayer implements CommandSender {

    /*
    Variables
     */
    private UUID uuid;
    private boolean isForgeUser;
    private String name;
    private List<String> perms;
    private List<String> groups;
    private String servername;
    private MinecraftServer server;

    /**
     * Constructor for a new RocketPlayer.
     * @param uuid uuid of player
     * @param isForgeUser if player is a forge user
     * @param name name of player
     * @param perms permissions of player
     * @param groups groups of player
     * @param servername name of the current server of the player
     * @param server current {@link MinecraftServer} of the player
     */
    public RocketPlayer(UUID uuid, boolean isForgeUser, String name, List<String> perms, List<String> groups, String servername, MinecraftServer server) {
        this.uuid = uuid;
        this.isForgeUser = isForgeUser;
        this.name = name;
        this.perms = perms;
        this.groups = groups;
        this.servername = servername;
        this.server = server;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isForgeUser() {
        return isForgeUser;
    }

    public String getName() {
        return name;
    }

    public List<String> getPerms() {
        return perms;
    }

    public List<String> getGroups() {
        return groups;
    }

    public String getServerName() {
        return servername;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public void setServer(MinecraftServer mcserver) {
        this.server = mcserver;
    }

    public boolean hasPermission(String permission ) { return getPerms().contains(permission); }

    @Override
    public void sendMessage(String message) {
        System.out.println("sending message in rocketplayer.class");
        DynamicRocketCore.getInstance().getCommunicationServer().getServer().getBroadcastOperations()
                .sendEvent("PlayerMessage",
                        new Document().append("uuid", this.uuid.toString()).append("message", message).convertToJson());
    }

    public void update() {
        DynamicRocketCore.getInstance().getCommunicationServer().getServer().getBroadcastOperations().sendEvent("PlayerInfo",
                new Document().append("uuid", this.uuid.toString()).convertToJson());
    }

    public void kick(String reason) {
        Document doc = new Document().append("uuid", this.uuid);
        if(reason != null) doc.append("reason", reason);

        DynamicRocketCore.getInstance().getCommunicationServer().getServer().getBroadcastOperations().sendEvent("PlayerKick",
                doc.convertToJson());
    }

}
