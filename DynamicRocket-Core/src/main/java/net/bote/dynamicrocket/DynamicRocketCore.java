package net.bote.dynamicrocket;

import net.bote.dynamicrocket.command.*;
import net.bote.dynamicrocket.connection.RocketPlayer;
import net.bote.dynamicrocket.console.Console;
import net.bote.dynamicrocket.console.ConsoleUtils;
import net.bote.dynamicrocket.files.FileManager;
import net.bote.dynamicrocket.lib.Document;
import net.bote.dynamicrocket.messenger.CommunicationServer;
import net.bote.dynamicrocket.server.ServerManager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Elias Arndt
 *
 * This class is the instance of DynamicRocket.
 */

public class DynamicRocketCore {

    private static DynamicRocketCore core;
    private CommunicationServer server;
    private ArrayList<RocketPlayer> onlinePlayers = new ArrayList<>();
    private ConsoleUtils consoleUtils;

    // Constructor
    public DynamicRocketCore() {}

    /**
     * method to get other manager classes.
     * @return instance
     */
    public static DynamicRocketCore getInstance() { return core; }

    /**
     * method to get the command manager class. As example you can add new commands here.
     * @return CommandMnanager instance
     */
    public CommandManager getCommandManager() { return CommandManager.getInstance();}

    /**
     * This is method returns the socket.io server of DynamicRocket.
     * @return SocketIO-server
     */
    public CommunicationServer getCommunicationServer() { return server; }

    /**
     * get the console class of DynamicRocket
     * @return Console instance
     */
    public Console getConsole() { return new Console(); }

    /**
     * get file manager of DynamicRocket
     * @return FileManager instance
     */
    public FileManager getFileManager() { return FileManager.getInstance();}

    /**
     * Get the ConsoleUtils class
     * @return ConsoleUtils class
     */
    public ConsoleUtils getConsoleUtils() {
        if(this.consoleUtils == null) this.consoleUtils = new ConsoleUtils();

        return this.consoleUtils;
    }

    /**
     * returns the instance of the ServerManager class.
     * @return ServerManager instance
     */
    public ServerManager getServerManager() { return ServerManager.getInstance();}

    /**
     * set the SocketIO-server
     * @param arg0 new SocketIO-server instance
     */
    public void setCommunicateServer(CommunicationServer arg0) { this.server = arg0; }

    /**
     * Get the online {@link RocketPlayer} playing on the network.
     * @return arraylist with all players
     */
    public ArrayList<RocketPlayer> getOnlinePlayers() { return this.onlinePlayers; }

    /**
     * set instance of DynamicRocketCore
     * @param instance instance of DynamicRocketCore
     */
    public static void setInstance(DynamicRocketCore instance) { core = instance; }

    /**
     * Get player with his name out of all players. Using for loop. If the player doesn't exists
     * the method returns null.
     * @param name name of player
     * @return the player
     */
    public RocketPlayer getPlayer(String name) {
        if(!playerExists(name)) return null;
        // want use for loop *-*
        for (RocketPlayer all : onlinePlayers) {
            if(all.getName().equals(name)) {
                return all;
            }
        }
        return null;
    }

    /**
     * Get player with his uuid out of all players. Using for loop. If the player doesn't exists
     * the method returns null.
     * @param uuid uuid of player
     * @return the player
     */
    public RocketPlayer getPlayer(UUID uuid) {
        if(!playerExists(uuid)) return null;
        // want use for loop *-*
        for (RocketPlayer all : onlinePlayers) {
            if(all.getUUID().equals(uuid)) {
                return all;
            }
        }
        return null;
    }

    /**
     * Checks if a player is playing on network. Searching player with name identifier.
     * @param name name of the player
     * @return if player is online
     */
    public boolean playerExists(String name) {

        // lambda would be shit. omega lol
        for(RocketPlayer all : onlinePlayers) {
            if(all.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Checks if a player is playing on network. Searching player with {@link UUID} identifier.
     * @param uuid {@link UUID} of the player
     * @return if player is online
     */
    public boolean playerExists(UUID uuid) {

        // lambda would be shit. omega lol
        for(RocketPlayer all : onlinePlayers) {
            if(all.getUUID().equals(uuid)) return true;
        }
        return false;
    }

    /**
     * Sending event to bungeecord server, to update the config.yml for enabling new started servers. An
     * empty document is handed over.
     */
    public void updateBungeeCord() {
        getCommunicationServer().getServer().getBroadcastOperations().sendEvent("BungeeCordUpdate", new Document());
    }

    /**
     * Send a message to a player
     * @param message message to send
     * @param name receiver
     */
    public void sendMessage(String message, String name) {
        getCommunicationServer().getServer().getBroadcastOperations().sendEvent("PlayerMessage",
                new Document().append("name", name).append("message", message).convertToJson());
    }

}
