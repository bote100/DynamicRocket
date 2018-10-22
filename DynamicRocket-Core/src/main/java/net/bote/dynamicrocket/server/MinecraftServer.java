package net.bote.dynamicrocket.server;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Elias Arndt
 * created on 30.09.2018 at 17:07
 *
 * This is a running spigot server of the DynamicRocket network.
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

        That the weirds bug in my life. I cannot do anything with the "listeners"
        section. The methods and variables in the normal bungeecord API just returning a Collection
        with a long String of Arrays - I can not do anything. Send help *-*

        So long this bug isn't fixed, this system doesn't really work.
        Good luck with the source, guys

        if(isLobby) {
            System.out.println("5");
            List<String> list = cfg.getStringList("listeners.priorities");
            System.out.println("6");
            System.out.println("Liste: " + list);

            list.add((String)getName());
            cfg.set("listeners.priorities", list);
            System.out.println("7");
        }

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        DynamicRocketCore.getInstance().updateBungeeCord();

    }

    /**
     * @return name of the server
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the log as String of a server
     * @param servername name of the server
     * @return the latest log in string format
     */
    @Deprecated
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

    /**
     * Get the log as String of a server
     * @return the latest log in string format
     */
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
    public void onStart(long millis) {
        DynamicRocketCore.getInstance().getConsole().sendMessage("Started " + getName() + " in " + millis / 1000 + "s");
    }

    @Override
    public void onStop() {
        DynamicRocketCore.getInstance().getInstance().getInstance().getConsole().sendMessage(getName() + " stopped.");
    }
}
