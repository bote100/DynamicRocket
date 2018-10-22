package net.bote.rocketapi.bridge;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Elias Arndt
 * created on 22.10.2018 at 14:15
 */

public class RocketPlayer {

    /*
    Variables
     */
    private UUID uuid;
    private boolean isForgeUser;
    private String name;
    private Collection<String> perms;
    private Collection<String> groups;
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
    public RocketPlayer(UUID uuid, boolean isForgeUser, String name, Collection<String> perms, Collection<String> groups, String servername, MinecraftServer server) {
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

    public Collection<String> getPerms() {
        return perms;
    }

    public Collection<String> getGroups() {
        return groups;
    }

    public String getServerName() {
        return servername;
    }

    public MinecraftServer getServer() { return server; }

    public boolean hasPermission(String permission ) { return getPerms().contains(permission); }

    public void sendMessage(String message) {
        ProxyServer.getInstance().getPlayer(this.uuid).sendMessage(new TextComponent(message));
    }

    public void kick(String reason) {
        ProxyServer.getInstance().getPlayer(this.uuid).disconnect(new TextComponent(reason));
    }

}