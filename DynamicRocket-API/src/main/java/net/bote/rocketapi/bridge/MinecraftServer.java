package net.bote.rocketapi.bridge;

import net.bote.rocketapi.bridge.interfaces.MinecraftServerCallback;
import net.bote.rocketapi.bridge.server.GroupConfiguration;
import net.bote.rocketapi.bridge.server.ServerGroup;
import net.bote.rocketapi.utility.LogLevel;
import net.bote.rocketapi.utility.Logger;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Elias Arndt
 * created on 22.10.2018 at 14:20
 */

public class MinecraftServer implements MinecraftServerCallback {

    private GroupConfiguration configuration;
    private ServerGroup group;
    private boolean isLobby;
    private String name;

    /**
     * MinecraftServer constructor
     * @param configuration servergroup config
     * @param name name of the server
     */
    public MinecraftServer(GroupConfiguration configuration, String name) {
        this.configuration = configuration;
        this.group = new ServerGroup(configuration);
        this.isLobby = configuration.isLobby();
        this.name = name;
    }

    //=========///

    public GroupConfiguration getConfiguration() {
        return configuration;
    }

    public ServerGroup getGroup() {
        return group;
    }

    /**
     * Register the server in the config.yml of the bungeecord server. After writing and saving
     * the server sends a message to the bungeecord and reload the bungeecord.
     * @param port port of the new server
     */
    public void registerServer(int port) {
        new Logger("Register server [" + getName() + "] in BungeeCord.", LogLevel.INFO).log();
        File file = new File("running/BungeeCord/config.yml");
        if(!file.exists()) file.mkdir();

        Configuration cfg = null;
        try {
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.set("servers." + getName() + ".motd", getName());
        cfg.set("servers." + getName() + ".address", "localhost:" + port);
        cfg.set("servers." + getName() + ".restricted", false);

        /*
        if(isLobby) {
            System.out.println("5");
            List<String> list = cfg.getStringList("listeners.priorities");
            System.out.println("6");
            System.out.println("Liste: " + list);

            list.add((String)getName());
            cfg.set("listeners.priorities", list);
            System.out.println("7");
        }
        */

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BungeeCord.getInstance().config.load();
        BungeeCord.getInstance().reloadMessages();
        BungeeCord.getInstance().stopListeners();
        BungeeCord.getInstance().startListeners();

    }

    /**
     * @return name of the server
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the latest log in string format
     * @param servername name of the server
     */
    public String getLogToString(String servername) {
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("running/" + servername + "/logs/latest.log"));
            StringBuilder stringBuilder = new StringBuilder();
            String line  = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();

            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLogToString() {
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("running/" + this.name + "/logs/latest.log"));
            StringBuilder stringBuilder = new StringBuilder();
            String line  = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();

            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Boolean isLobbyServer() {
        return isLobby;
    }

    @Override
    public void onStart(long millis) {}

    @Override
    public void onStop() {}
}
